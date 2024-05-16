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
package com.braintribe.devrock.ac.container.plugin.listener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;

import com.braintribe.devrock.ac.container.plugin.ArtifactContainerPlugin;
import com.braintribe.devrock.ac.container.registry.ProjectInfoContainer;
import com.braintribe.devrock.ac.container.updater.DependerUpdater;
import com.braintribe.devrock.api.storagelocker.StorageLockerSlots;
import com.braintribe.devrock.plugin.DevrockPlugin;
import com.braintribe.logging.Logger;

/**
 * alternative {@link IResourceChangeListener} that only updates 
 * a) changed projects (via POM visitor) <br/>
 * b) dependers of changed projects 
 * c) dependers of imported/deleted/opened/closed projects
 * 
 * @author pit
 */
public class AdvancedResourceChangeListener implements IResourceChangeListener {
	private static Logger log = Logger.getLogger(AdvancedResourceChangeListener.class);
	
	private IProject projectClosedOrDeleted;
	private IProject projectPomTouched;
	private ResourceVisitor resourceVisitor = null;
				
	public AdvancedResourceChangeListener() {	
		resourceVisitor = new ResourceVisitor( this::acceptUpdateRequest);
	}
	
	public void acceptUpdateRequest(IProject project) {
		projectPomTouched = project;
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {			
		if (				
				(event.getResource() instanceof IProject) && 
				(
						(event.getType() == IResourceChangeEvent.PRE_DELETE ) ||
						(event.getType() == IResourceChangeEvent.PRE_CLOSE)
				)
			) {
			IProject iProject = (IProject) event.getResource();
			projectClosedOrDeleted = iProject;
			
			switch (event.getType()) {
				case IResourceChangeEvent.PRE_CLOSE: {
					// store the name of the closed project to remember it after a close/open cycle on Eclipse
					String name = iProject.getName();
					List<String> closedProjects = DevrockPlugin.instance().storageLocker().getValue( StorageLockerSlots.SLOT_AC_CLOSED_PROJECTS, null);
					// if nothing's there yet, create and add the list
					if (closedProjects == null) {
						closedProjects = new ArrayList<String>();
						DevrockPlugin.instance().storageLocker().setValue(StorageLockerSlots.SLOT_AC_CLOSED_PROJECTS, closedProjects);
					}
					if (!closedProjects.contains( name)) {
						closedProjects.add(name);
						
					}
					break;
				}
				case IResourceChangeEvent.PRE_DELETE: {
					// if closed project is deleted, forget about it 
					String name = iProject.getName();
					List<String> closedProjects = DevrockPlugin.instance().storageLocker().getValue( StorageLockerSlots.SLOT_AC_CLOSED_PROJECTS, null);
					if (closedProjects != null) {
						if (closedProjects.remove(name));
					}
					break;
				}
				default: break;
			}
						
		}
		
		IResourceDelta delta = event.getDelta();
		// no changes? 
		if (delta == null)
			return;
		
		int kind = delta.getKind();
		switch( kind ) {
			case IResourceDelta.CHANGED : {				
				int flags = delta.getFlags();
				if ((flags & IResourceDelta.MARKERS) != 0)
					return;
			}
		}
		
		// install visitor - to detect changes in the pom, see ResourceVisitor
		try {
			delta.accept( resourceVisitor);
		} catch (CoreException e1) {		
			e1.printStackTrace();
		}
		
		IResource deltaResource = delta.getResource();
		if (deltaResource != null) {

			// 	
			if (
					deltaResource instanceof IWorkspaceRoot &&						
					event.getType() == IResourceChangeEvent.POST_CHANGE ||
					event.getType() == IResourceChangeEvent.PRE_REFRESH
				) {			
					IWorkspaceRoot wroot = (IWorkspaceRoot) deltaResource;
					IProject[] projects = wroot.getProjects();
					log.info("num workspace project: [" + projects.length  + "]");
					Set<IProject> modifiedProjects = new HashSet<>( projects.length);
							
					for (IProject project : projects) {
						String name = project.getName();
						// check if the project has been re-openend (marked as closed, but now acessible again)
						if (project.isAccessible()) {
							List<String> closedProjects = DevrockPlugin.instance().storageLocker().getValue( StorageLockerSlots.SLOT_AC_CLOSED_PROJECTS, null);
							if (closedProjects != null) {
								if (closedProjects.contains(name)) {
									closedProjects.remove(name);
									modifiedProjects.add(project);
									//
									log.debug("Detected re-openend project: " + name);
								}
							}					
						}
						
						long eclipseProcessedmodificationStamp = project.getModificationStamp();
						ProjectInfoContainer projectInfoContainer = ArtifactContainerPlugin.instance().containerRegistry().getProjectInfoContainer(project);
						if (projectInfoContainer == null) {
							log.info("No info for project: "  + name);
							continue;
						}
						long acProcessedModificationStamp = projectInfoContainer.getCurrentModificationStamp();
						log.debug("e-stamp: " + eclipseProcessedmodificationStamp + ", ac-stamp:" + acProcessedModificationStamp + " <- " + name);
						if (eclipseProcessedmodificationStamp == 0 || eclipseProcessedmodificationStamp != acProcessedModificationStamp) {
							modifiedProjects.add(project);							
						}
						projectInfoContainer.setCurrentModificationStamp(eclipseProcessedmodificationStamp);					
					}
					
					if (projectClosedOrDeleted != null) {
						modifiedProjects.add(projectClosedOrDeleted);
						projectClosedOrDeleted = null; // null it again
					}
					
					
					// any projects changed ? 
					if (modifiedProjects.size() > 0 || projectPomTouched != null) {
						log.info("found projects to modify : " + modifiedProjects.stream().map( p -> p.getName()).collect(Collectors.joining(",")));
						if (projectPomTouched != null) {
							log.info("found project marked as pom-touched: " + projectPomTouched.getName());
						}
						DevrockPlugin.mcBridge().close();
						DependerUpdater dependerUpdater = new DependerUpdater(null);
						dependerUpdater.setSelectedProjects( modifiedProjects);
						dependerUpdater.setTouchedProject(projectPomTouched);
						dependerUpdater.runAsJob();
						projectPomTouched = null; // null it again
					}
					else {
						log.info("No projects to modify");
					}
					
				}
				
			}
	
		}
	
		

}
