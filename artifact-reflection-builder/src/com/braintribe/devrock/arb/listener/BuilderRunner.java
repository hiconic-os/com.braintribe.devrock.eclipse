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
package com.braintribe.devrock.arb.listener;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.devrock.api.nature.JavaProjectBuilderHelper;
import com.braintribe.devrock.arb.builder.ArtifactReflectionBuilder;
import com.braintribe.devrock.arb.plugin.ArtifactReflectionBuilderPlugin;
import com.braintribe.devrock.arb.plugin.ArtifactReflectionBuilderStatus;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.logging.Logger;

/**
 * {@link WorkspaceModifyOperation} to run the builder.. 
 * @author pit
 *
 */
public class BuilderRunner extends WorkspaceModifyOperation {
	private static Logger log = Logger.getLogger(BuilderRunner.class);
	
	private IProject project;
	
	@Configurable @Required
	public void setProject(IProject project) {
		this.project = project;
	}
	

	@Override
	protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
		
		log.debug("called via resource change listener");
		
		// check if project has the builder attached
		Maybe<Boolean> maybe = JavaProjectBuilderHelper.hasBuilder(project, ArtifactReflectionBuilder.ID);
		if (maybe.isUnsatisfied()) {
			String msg = "cannot determine whether project [" + project.getName() + "] has the ARB attached " + maybe.whyUnsatisfied().stringify();
			log.error(msg);
			ArtifactReflectionBuilderStatus status = new ArtifactReflectionBuilderStatus(msg, IStatus.ERROR);
			ArtifactReflectionBuilderPlugin.instance().log(status);
		}
		
		if (maybe.get()) {		
			monitor.beginTask("Building " + project.getName(), IProgressMonitor.UNKNOWN);
			ArtifactReflectionBuilder.build(project);
		}
		monitor.done();
		
	}
	
	

	/**
	 * to be called as job 
	 */
	public void runAsJob() {
		Job job = new WorkspaceJob("Running artifact-reflection build on :" + project.getName()) {
			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) {				
				try {
					execute( monitor);
				} catch (Exception e) {
					ArtifactReflectionBuilderStatus status = new ArtifactReflectionBuilderStatus("cannot run artifact-reflection build on :" + project.getName(), e);
					ArtifactReflectionBuilderPlugin.instance().log(status);
				} 
				return Status.OK_STATUS;
			}
			
		};
		job.schedule();
	
	}

}
