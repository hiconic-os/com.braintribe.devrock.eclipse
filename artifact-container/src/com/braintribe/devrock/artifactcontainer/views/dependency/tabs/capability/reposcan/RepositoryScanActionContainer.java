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
package com.braintribe.devrock.artifactcontainer.views.dependency.tabs.capability.reposcan;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.braintribe.devrock.artifactcontainer.views.dependency.tabs.AbstractDependencyViewTab;
import com.braintribe.devrock.artifactcontainer.views.dependency.tabs.capability.AbstractDependencyViewActionContainer;
import com.braintribe.plugin.commons.views.actions.ViewActionContainer;
import com.braintribe.plugin.commons.views.actions.ViewActionController;

public class RepositoryScanActionContainer extends AbstractDependencyViewActionContainer implements ViewActionContainer<AbstractDependencyViewTab>, ViewActionController<AbstractDependencyViewTab>{

	private Action repositoryScanAction;
	
	@Override
	public ViewActionController<AbstractDependencyViewTab> create() {
		// run greyface 
		ImageDescriptor greyfaceImageDescriptor = ImageDescriptor.createFromFile( RepositoryScanCapable.class, "repositories.gif");
		repositoryScanAction = new Action("Import external dependency via Greyface", greyfaceImageDescriptor) {

			@Override
			public void run() {				
				AbstractDependencyViewTab viewTab = activeTabProvider.provideActiveTab();
				if (viewTab instanceof RepositoryScanCapable) {
					RepositoryScanCapable loadingTab = (RepositoryScanCapable) viewTab;
					loadingTab.scanRepository();
				}
			}			
		};
		toolbarManager.add(repositoryScanAction);
		menuManager.add(repositoryScanAction);
		return this;
	}

	@Override
	public void controlAvailablity(AbstractDependencyViewTab tab) {
		if (tab instanceof RepositoryScanCapable) {
			repositoryScanAction.setEnabled(true);
		}
		else {
			repositoryScanAction.setEnabled(false);
		}		
	}
	
	

}
