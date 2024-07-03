// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package com.braintribe.devrock.ac.container.updater;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import com.braintribe.devrock.ac.container.ArtifactContainer;
import com.braintribe.devrock.ac.container.plugin.ArtifactContainerPlugin;
import com.braintribe.devrock.ac.container.plugin.ArtifactContainerStatus;
import com.braintribe.devrock.ac.container.updater.ProjectUpdater.Mode;

/**
 * updates all containers of all projects in the workspace
 * 
 * @author pit
 *
 */
public class WorkspaceUpdater extends WorkspaceModifyOperation {

	@Override
	protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] projects = root.getProjects();
		
		if (projects == null || projects.length == 0) 
			return;
				
		
		try {
			monitor.beginTask("reinitializing containers", projects.length);
			int i = 0;
			for (IProject project : projects) {
				if (project.isAccessible() == false) {
					// do not sync inaccessible projects (closed projects for instance)
					continue;
				}
				if (monitor.isCanceled()) {
					ArtifactContainerStatus status = new ArtifactContainerStatus("reinitializing container aborted by user", IStatus.INFO);
					ArtifactContainerPlugin.instance().log(status);
					return;
				}
				monitor.subTask( "resolving " + project.getName());				
				ArtifactContainer container = ArtifactContainerPlugin.instance().containerRegistry().getContainerOfProject(project);
				if (container != null) {
					// reassign
					ArtifactContainer sibling = container.reinitialize(Mode.standard);
					// trigger resolve
					sibling.getClasspathEntries(false);
				}
				else {
					System.out.println("no container for [" + project.getName() + "]");
				}
				monitor.worked(i++);
			}
		} finally {
			monitor.done();
		}					
	}
	
	public void runAsJob() {
		Job job = new WorkspaceJob("Triggering full workspace sync") {
			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) {
				// rescan				
				try {
					execute( monitor);
				} catch (Exception e) {
					ArtifactContainerStatus status = new ArtifactContainerStatus("cannot trigger update of all containers in the workspace", e);
					ArtifactContainerPlugin.instance().log(status);
				} 
				return Status.OK_STATUS;
			}
			
		};
		job.schedule();
	
	}

}
