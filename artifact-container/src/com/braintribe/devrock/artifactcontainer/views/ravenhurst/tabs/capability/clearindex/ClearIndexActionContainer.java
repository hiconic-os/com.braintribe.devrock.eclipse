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
package com.braintribe.devrock.artifactcontainer.views.ravenhurst.tabs.capability.clearindex;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.braintribe.devrock.artifactcontainer.views.ravenhurst.tabs.RavenhurstViewTab;
import com.braintribe.plugin.commons.views.actions.AbstractViewActionContainer;
import com.braintribe.plugin.commons.views.actions.ViewActionController;
import com.braintribe.plugin.commons.views.tabbed.ActiveTabProvider;
import com.braintribe.plugin.commons.views.tabbed.TabProvider;

/**
 * the action container for the ravenhurst tabs, supporting the clearing of maven metadata files
 *  
 * @author pit
 *
 */
public class ClearIndexActionContainer extends AbstractViewActionContainer<RavenhurstViewTab> implements ViewActionController<RavenhurstViewTab> {
	private ActiveTabProvider<RavenhurstViewTab> activeTabProvider;
	private Action clearAction;
	
	@Override
	public void setSelectionProvider(ActiveTabProvider<RavenhurstViewTab> provider) {
		this.activeTabProvider = provider;
	}

	@Override
	public void setTabProvider(TabProvider<RavenhurstViewTab> provider) {	
	}

	@Override
	public ViewActionController<RavenhurstViewTab> create() {
		// purge
		ImageDescriptor pomImportImageDescriptor = ImageDescriptor.createFromFile( ClearIndexActionContainer.class, "clear.gif");
		clearAction = new Action("Purge RH index of this repository", pomImportImageDescriptor) {

			@Override
			public void run() {		
				RavenhurstViewTab viewTab = activeTabProvider.provideActiveTab();
				viewTab.purgeIndex();
			}			
		};
		toolbarManager.add( clearAction);
		menuManager.add( clearAction);
		return this;
	}

	@Override
	public void controlAvailablity(RavenhurstViewTab tab) {		
		clearAction.setEnabled( true);
	}

	
	

}
