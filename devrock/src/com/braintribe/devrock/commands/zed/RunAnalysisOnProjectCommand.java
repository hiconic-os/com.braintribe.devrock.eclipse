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
package com.braintribe.devrock.commands.zed;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.braintribe.devrock.api.selection.SelectionExtracter;
import com.braintribe.devrock.plugin.DevrockPlugin;
import com.braintribe.devrock.plugin.DevrockPluginStatus;
import com.braintribe.logging.Logger;

/**
 * run a zed analysis on the currently selected project
 * @author pit
 *
 */
public class RunAnalysisOnProjectCommand extends AbstractHandler  implements ZedRunnerTrait {
	private static Logger log = Logger.getLogger(RunAnalysisOnProjectCommand.class);
				
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		Job job = new Job("preparing zed's analysis") {						
			@Override
			public IStatus run(IProgressMonitor arg0) {
				try {
					_execute( event);
					return Status.OK_STATUS;
				} catch (ExecutionException e) {
					log.error( "can't run analysis job", e);
					return Status.CANCEL_STATUS;
				}
			}
		};
		job.schedule();		
				
		return null;
	}
				
	
	private Object _execute(ExecutionEvent event) throws ExecutionException {
		
		IProject project = SelectionExtracter.currentProject( SelectionExtracter.currentSelection());
		try {
			if (project != null) {
				ZedRunnerTrait.process( project);				
			} // project != null
		} catch (CoreException e) {
			DevrockPluginStatus status = new DevrockPluginStatus("Project [" + project.getName() + "] cannot be processed", e);
			DevrockPlugin.instance().log(status);
			return null;
		}						
		return null;
	}
	
	}
