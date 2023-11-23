// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.devrock.artifactcontainer.views.ravenhurst.tabs.capability.clearmetadata;

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
public class ClearMetadataActionContainer extends AbstractViewActionContainer<RavenhurstViewTab> implements ViewActionController<RavenhurstViewTab> {
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
		ImageDescriptor pomImportImageDescriptor = ImageDescriptor.createFromFile( ClearMetadataActionContainer.class, "maven_indexes.gif");
		clearAction = new Action("Clear maven metadata of this repository", pomImportImageDescriptor) {

			@Override
			public void run() {		
				RavenhurstViewTab viewTab = activeTabProvider.provideActiveTab();
				viewTab.purgeMetadata();
			}			
		};
		toolbarManager.add( clearAction);
		menuManager.add( clearAction);
		return this;
	}

	@Override
	public void controlAvailablity(RavenhurstViewTab tab) {		
		clearAction.setEnabled( tab.canPurge());
	}

	
	

}
