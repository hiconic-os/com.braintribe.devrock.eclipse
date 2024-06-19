// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.devrock.ac.container.updater;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import com.braintribe.cfg.Configurable;
import com.braintribe.devrock.ac.container.ArtifactContainer;
import com.braintribe.devrock.ac.container.plugin.ArtifactContainerPlugin;
import com.braintribe.devrock.ac.container.plugin.ArtifactContainerStatus;
import com.braintribe.devrock.ac.container.registry.WorkspaceContainerRegistry;
import com.braintribe.devrock.api.selection.SelectionExtracter;
import com.braintribe.logging.Logger;
import com.braintribe.model.artifact.essential.VersionedArtifactIdentification;

/**
 * based on the passed/selected projects, it will find the DEPENDER of these projects and run a sync on it.
 * 
 * @author pit
 */
public class DependerUpdater extends WorkspaceModifyOperation {
	private static Logger log = Logger.getLogger(DependerUpdater.class);
	
	private Set<IProject> selectedProjects;
	private IProject touchedProject;
	private IWorkbenchWindow activeWorkbenchWindow;
	
	public DependerUpdater(IWorkbenchWindow activeWorkbenchWindow) {
		this.activeWorkbenchWindow = activeWorkbenchWindow;
	}
		
	@Configurable
	public void setSelectedProjects(Set<IProject> selectedProjects) {
		this.selectedProjects = new HashSet<>(selectedProjects);
	}

	@Configurable
	public void setTouchedProject(IProject touchedProject) {
		this.touchedProject = touchedProject;
	}


	@Override
	protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
		
		if (selectedProjects == null) {
			ISelection selection = SelectionExtracter.currentSelection(activeWorkbenchWindow);
			selectedProjects = SelectionExtracter.selectedProjects(selection);
		}
		
		
		if (touchedProject != null) {
			if (selectedProjects == null) {
				selectedProjects = new HashSet<>();
			}
			boolean b = selectedProjects.add(touchedProject);
			log.info( b ? "Added touched artifact: " + touchedProject.getName() : "touched artifact already present");
		}
		
		
		if (selectedProjects == null || selectedProjects.size() == 0)
			return;
		
		// get identification of selected projects
		WorkspaceContainerRegistry containerRegistry = ArtifactContainerPlugin.instance().containerRegistry();
		
		//List<ArtifactContainer> containersToScan = containerRegistry.getRegisteredContainers().stream().filter( c -> !selectedProjects.contains(c.getProject().getProject())).collect( Collectors.toList()); 
		
		// all available containers can be dependers, also one of the selected ..
		List<ArtifactContainer> containersToScan = containerRegistry.getRegisteredContainers().stream().filter( c -> c.getProject().getProject().isAccessible()).collect( Collectors.toList());
		
		// find all containers
		
		Set<IProject> dependers = new HashSet<IProject>();
		// 	iterate over containers 
		for (ArtifactContainer container : containersToScan) {
			// check if container has a dependency to any current projects, short cut logic, store matches in filtered list 
			for (IProject dependency : selectedProjects) {
				ArtifactContainer containerOfProject = containerRegistry.getContainerOfProject(dependency);
				if (containerOfProject == null) {
					log.info("Skipped: No container attached to project :" + dependency.getName());
					continue;
				}
				// relax specification : only one version of an artifact can be present in the container
				VersionedArtifactIdentification versionedArtifactIdentification = containerOfProject.getVersionedArtifactIdentification();
				String solutionName = versionedArtifactIdentification.getGroupId() + ":" + versionedArtifactIdentification.getArtifactId();
				Set<String> dependenciesSet = container.getDependenciesSet();
				if (dependenciesSet == null) {
					log.info("Skipped: container attached, yet no dependencies set :" + dependency.getName());
					break;
				}
				if (dependenciesSet.contains( solutionName)) {
					dependers.add( container.getProject().getProject());
					break; // just one match's enough
				}
			}
		}
				
		String input = selectedProjects.stream().map( p -> p.getName()).collect(Collectors.joining(","));
		// log
		if (dependers.size() > 0) {
			String output = dependers.stream().map( p -> p.getName()).collect(Collectors.joining(",\n"));
			String msg = "Identified dependers of [" + input + "]: \n" + output;
			log.info(msg);		
			ArtifactContainerStatus status = new ArtifactContainerStatus( msg, IStatus.INFO);
			ArtifactContainerPlugin.instance().log(status);
		}
		else {
			String msg = "No dependers found for: " + input;
			log.info(msg);
		}
 
		// call the updater..
	
		ProjectUpdater projectUpdater = new ProjectUpdater(activeWorkbenchWindow);
		projectUpdater.setSelectedProjects(dependers);		
		projectUpdater.execute( monitor);						
		
	}
	
	public void runAsJob() {
		Job job = new WorkspaceJob("Triggering sync on depender projects") {
			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) {
				// rescan				
				try {
					execute( monitor);
				} catch (Exception e) {
					ArtifactContainerStatus status = new ArtifactContainerStatus("cannot trigger update the selected depender containers", e);
					ArtifactContainerPlugin.instance().log(status);
				} 
				return Status.OK_STATUS;
			}
			
		};
		job.schedule();
	
	}

}
