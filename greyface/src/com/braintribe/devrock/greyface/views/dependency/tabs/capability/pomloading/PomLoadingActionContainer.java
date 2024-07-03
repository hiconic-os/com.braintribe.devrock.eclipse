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
package com.braintribe.devrock.greyface.views.dependency.tabs.capability.pomloading;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.braintribe.devrock.greyface.view.tab.GenericViewTab;
import com.braintribe.devrock.greyface.views.dependency.tabs.capability.AbstractDependencyViewActionContainer;
import com.braintribe.plugin.commons.views.actions.ViewActionContainer;
import com.braintribe.plugin.commons.views.actions.ViewActionController;

public class PomLoadingActionContainer extends AbstractDependencyViewActionContainer implements ViewActionContainer<GenericViewTab>, ViewActionController<GenericViewTab> {

	private Action pomLoadingAction;
	
	@Override
	public void controlAvailablity(GenericViewTab tab) {
		if (tab instanceof PomLoadingCapable) {
			pomLoadingAction.setEnabled( true);
		}
		else {
			pomLoadingAction.setEnabled(false);
		}
	}

	@Override
	public ViewActionController<GenericViewTab> create() {
		ImageDescriptor pomImageDescriptor = ImageDescriptor.createFromFile( PomLoadingActionContainer.class, "pom_obj.gif");
	

		pomLoadingAction = new Action("load pom", pomImageDescriptor) {		
			@Override
			public void run() {				
				GenericViewTab viewTab = activeTabProvider.provideActiveTab();
				if (viewTab instanceof PomLoadingCapable) {
					PomLoadingCapable clipboardCapable = (PomLoadingCapable) viewTab; 
					clipboardCapable.loadPom();					
				}
			}
			
		};
		toolbarManager.add(pomLoadingAction);
		menuManager.add( pomLoadingAction);
		return this;
	}

}
