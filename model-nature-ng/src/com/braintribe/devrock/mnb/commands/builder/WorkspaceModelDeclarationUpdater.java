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
package com.braintribe.devrock.mnb.commands.builder;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import com.braintribe.devrock.mnb.plugin.ModelBuilderPlugin;
import com.braintribe.devrock.mnb.plugin.ModelBuilderStatus;

public class WorkspaceModelDeclarationUpdater extends WorkspaceModifyOperation {
	
	
	private List<IProject> projects;

	public WorkspaceModelDeclarationUpdater(List<IProject> projects) {
		this.projects = projects;
	}
	

	@Override
	protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
		ModelBuilder mb = new ModelBuilder();
		int i = 0;
		monitor.beginTask("Building model declarations ", projects.size());
		for (IProject project : projects) {
			if (monitor.isCanceled()) {
				ModelBuilderStatus status = new ModelBuilderStatus("model declaration builds aborted by user", IStatus.INFO);
				ModelBuilderPlugin.instance().log(status);
				return;
			}
			monitor.subTask( "resolving " + project.getName());
			mb.forceBuild(project);
			monitor.worked(++i);			
		}
	}
	
	public void runAsJob() {
		if (projects == null) {
			ModelBuilderStatus status = new ModelBuilderStatus("No projects to build configured", IStatus.WARNING);
			ModelBuilderPlugin.instance().log(status);
			return;
		}
		Job job = new WorkspaceJob("Triggering model declaration build on (" + projects.size() +") model projects") {
			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) {
								
				try {
					execute( monitor);
				} catch (Exception e) {
					ModelBuilderStatus status = new ModelBuilderStatus("cannot trigger update of all containers in the workspace", e);
					ModelBuilderPlugin.instance().log(status);
				} 
				return Status.OK_STATUS;
			}
			
		};
		job.schedule();
	
	}

}
