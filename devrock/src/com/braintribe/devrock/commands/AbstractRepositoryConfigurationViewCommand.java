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
package com.braintribe.devrock.commands;

import java.io.File;
import java.util.Date;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.braintribe.devrock.model.repository.RepositoryConfiguration;
import com.braintribe.devrock.plugin.DevrockPlugin;
import com.braintribe.devrock.plugin.DevrockPluginStatus;
import com.braintribe.devrock.ui.cfg.repository.RepositoryConfigurationInfoDialog;
import com.braintribe.gm.model.reason.Maybe;

/**
 * abstract base for 
 * @author pit
 *
 */
public abstract class AbstractRepositoryConfigurationViewCommand extends AbstractHandler{
	
	private Shell shell;
	
	protected abstract Maybe<Container> retrieveRepositoryMaybe();
	
	protected Shell getShell() {
		if (shell == null) {
			shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		}
		return shell;
	}

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
					
		RepositoryConfigurationInfoDialog dlg = new RepositoryConfigurationInfoDialog( getShell());
	
		Maybe<Container> repositoryMaybe = retrieveRepositoryMaybe();
		if (repositoryMaybe.isUnsatisfied()) {
			DevrockPluginStatus status = new DevrockPluginStatus( repositoryMaybe.whyUnsatisfied());
			DevrockPlugin.instance().log(status);
			return null;
		}
		Container loaded = repositoryMaybe.get(); 
		
		dlg.setRepositoryConfiguration( loaded.rfcg);
		dlg.setLastProcessingTime( loaded.processingTime);
		dlg.setTimestamp( loaded.timestamp);
		dlg.setOrigin( loaded.file);
		dlg.open();
		return null;
	}
	
	/**
	 * a helper to transfer complex data 
	 * @author pit
	 *
	 */
	protected class Container {
		public RepositoryConfiguration rfcg;
		public Date timestamp;
		public double processingTime;
		public File file;
		
	}
}
