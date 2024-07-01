// ============================================================================
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
package com.braintribe.devrock.artifactcontainer.views.dependency.tabs.capability.filter;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.braintribe.devrock.artifactcontainer.views.dependency.tabs.AbstractDependencyViewTab;
import com.braintribe.devrock.artifactcontainer.views.dependency.tabs.capability.AbstractDependencyViewActionContainer;
import com.braintribe.plugin.commons.views.actions.ViewActionContainer;
import com.braintribe.plugin.commons.views.actions.ViewActionController;

public class FilterActionContainer extends AbstractDependencyViewActionContainer implements ViewActionContainer<AbstractDependencyViewTab>, ViewActionController<AbstractDependencyViewTab> {
	
	private Action activateFilterAction;
	private Action deactivateFilterAction;
	
	@Override
	public ViewActionController<AbstractDependencyViewTab> create() {
		// edit filter
				ImageDescriptor filterImageDescriptor = ImageDescriptor.createFromFile( FilterCapable.class, "filter.gif");
				Action editFilter = new Action( "Edit filter", filterImageDescriptor) {
					@Override
					public void run() {			
						display.asyncExec( new Runnable() {					
							@Override
							public void run() {
								FilterDialog filterDialog = new FilterDialog( display.getActiveShell());						
								filterDialog.open();		
								// modal, so this is now after closed.. 
								for (AbstractDependencyViewTab tab : tabProvider.provideTabs()) {
									if (tab instanceof FilterCapable) {
										((FilterCapable) tab).applyFilter();
									}
								}
							}
						});
						super.run();
					}			
				};
				menuManager.add(editFilter);
				
				// activate filter
				ImageDescriptor filterOnImageDescriptor = ImageDescriptor.createFromFile( FilterCapable.class, "filter_on.gif");
				activateFilterAction = new Action( "Activate filter", filterOnImageDescriptor) {
					@Override
					public void run() {						
						AbstractDependencyViewTab viewTab = activeTabProvider.provideActiveTab(); 
						if (viewTab instanceof FilterCapable) {
							FilterCapable filterCapable = (FilterCapable) viewTab;
								filterCapable.filter();
								activateFilterAction.setEnabled( !filterCapable.isFilterActive());
								deactivateFilterAction.setEnabled( filterCapable.isFilterActive());					
						}				
					}			
				};
				menuManager.add(activateFilterAction);
				
				// deactivate filter 
				ImageDescriptor filterOffImageDescriptor = ImageDescriptor.createFromFile( FilterCapable.class, "filter_off.gif");
				deactivateFilterAction = new Action( "Deactivate filter", filterOffImageDescriptor) {
					@Override
					public void run() {				
						AbstractDependencyViewTab viewTab = activeTabProvider.provideActiveTab(); 
						if (viewTab instanceof FilterCapable) {
							FilterCapable filterCapable = (FilterCapable) viewTab;
							filterCapable.stopFilter();
							activateFilterAction.setEnabled( !filterCapable.isFilterActive());
							deactivateFilterAction.setEnabled( filterCapable.isFilterActive());
						}
					}			
				};
				menuManager.add(deactivateFilterAction);

			 
				
		return this;
	}

	@Override
	public void controlAvailablity(AbstractDependencyViewTab tab) {
		// filter
		if (tab instanceof FilterCapable) {
			FilterCapable filterCapable = (FilterCapable) tab;
			boolean active = filterCapable.isFilterActive();			
			activateFilterAction.setEnabled(!active);			
			deactivateFilterAction.setEnabled(active);			
		} else {
			activateFilterAction.setEnabled(false);
			deactivateFilterAction.setEnabled(false);
		}
				
	}
	
	

}
