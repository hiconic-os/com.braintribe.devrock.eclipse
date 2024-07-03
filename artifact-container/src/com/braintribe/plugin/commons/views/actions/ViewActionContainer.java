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
package com.braintribe.plugin.commons.views.actions;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Display;

import com.braintribe.plugin.commons.views.tabbed.ActiveTabProvider;
import com.braintribe.plugin.commons.views.tabbed.TabProvider;
import com.braintribe.plugin.commons.views.tabbed.tabs.AbstractViewTab;

/**
 * a container for the actions that are linked to a certain tab of a tabbed view
 *  
 * @author pit
 *
 * @param <T> - something derived from the {@link AbstractViewTab}
 */
public interface ViewActionContainer<T extends AbstractViewTab> {
	/**
	 * the display the UI thread will need to run in
	 * @param display - the {@link Display}
	 */
	void setDisplay( Display display);
	/**
	 * representing the toolbar of the view 
	 * @param toolbarManager - {@link IToolBarManager}
	 */
	void setToolbarManager( IToolBarManager toolbarManager);
	/**
	 * representing the menu of the view 
	 * @param menuManager - the {@link IMenuManager}
	 */
	void setMenuManager( IMenuManager menuManager);
	/**
	 * provides the currently selected tab 
	 * @param provider - {@link ActiveTabProvider} (most commonly the view itself) 
	 */
	void setSelectionProvider( ActiveTabProvider<T> provider);
	/**
	 * provides ALL tabs of the view 
	 * @param provider - the {@link TabProvider} (most commonly the view itself)
	 */
	void setTabProvider( TabProvider<T> provider);
	/**
	 * creates the actions, incorporates them into the toolbar and menu, and returns
	 * the controller (which controls availability)
	 * @return - {@link ViewActionController}
	 */
	ViewActionController<T> create();
}
