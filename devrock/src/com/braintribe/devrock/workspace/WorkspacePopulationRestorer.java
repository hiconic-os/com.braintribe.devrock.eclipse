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
package com.braintribe.devrock.workspace;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import com.braintribe.devrock.eclipse.model.storage.StorageLockerPayload;
import com.braintribe.devrock.eclipse.model.workspace.ExportPackage;
import com.braintribe.devrock.eclipse.model.workspace.Workspace;
import com.braintribe.devrock.plugin.DevrockPlugin;
import com.braintribe.devrock.plugin.DevrockPluginStatus;

/**
 * {@link WorkspaceModifyOperation} implementation to run a workspace-restorer as a job  
 * @author pit
 *
 */
public class WorkspacePopulationRestorer extends WorkspaceModifyOperation {
	private ExportPackage content;

	/**
	 * @param file - the file with the {@link Workspace} 
	 */
	public WorkspacePopulationRestorer(File file) {	
		content = WorkspacePopulationMarshaller.load( file);		
	}
	
	/**
	 * @param content - the {@link Workspace} instance 
	 */
	public WorkspacePopulationRestorer(Workspace content) {
		ExportPackage ep = ExportPackage.T.create();
		ep.setWorkspace(content);
		this.content = ep;		
	}
			
	/**
	 * @param workspace
	 * @param storageLockerPayload
	 */
	public WorkspacePopulationRestorer(Workspace workspace, StorageLockerPayload storageLockerPayload) {
		ExportPackage ep = ExportPackage.T.create();
		ep.setStorageLockerPayload(storageLockerPayload);
		ep.setWorkspace(workspace);
		this.content = ep;
	}

	@Override
	protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {	
		WorkspacePopulationMarshaller wpm = new WorkspacePopulationMarshaller();
		wpm.restoreWorkspace(monitor, content);
		
	}
	
	/**
	 * threaded job to import workspace
	 */
	public void runAsJob() {
		Job job = new WorkspaceJob("Re-importing exported workspace") {
			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) {
				// import				
				try {
					execute( monitor);
				} catch (Exception e) {
					DevrockPluginStatus status = new DevrockPluginStatus("cannot re-import exported workspace", e);
					DevrockPlugin.instance().log(status);
				} 
				return Status.OK_STATUS;
			}
			
		};
		job.schedule();
	
	}
	

}
