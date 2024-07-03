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
package com.braintribe.devrock.ac.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.braintribe.devrock.ac.container.plugin.ArtifactContainerPlugin;
import com.braintribe.devrock.ac.container.plugin.ArtifactContainerStatus;
import com.braintribe.devrock.ac.container.updater.WorkspaceUpdater;
import com.braintribe.logging.Logger;

/**
 * command to update the full workspace
 * @author pit
 *
 */
public class BuildWorkspaceCommand extends AbstractHandler{
	private static Logger log = Logger.getLogger(BuildWorkspaceCommand.class);

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			WorkspaceUpdater updater = new WorkspaceUpdater();			
			updater.runAsJob();
			//updater.run( new NullProgressMonitor());
		} catch (Exception e) {
			String msg = "cannot run project updater";
			log.error(msg, e);
			ArtifactContainerStatus status = new ArtifactContainerStatus( msg, e);
			ArtifactContainerPlugin.instance().log(status);
		}
		return null;				
	}
			
		
	
}
