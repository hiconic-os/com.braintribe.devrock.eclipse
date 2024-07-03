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

import com.braintribe.plugin.commons.views.tabbed.tabs.AbstractViewTab;

/**
 * an abstract implementation of the basic features of a {@link ViewActionContainer}
 * 
 * @author pit
 *
 * @param <T> - a sub type of {@link AbstractViewTab}
 */
public abstract class AbstractViewActionContainer<T extends AbstractViewTab> implements ViewActionContainer<T> {
	
	protected Display display;
	protected IToolBarManager toolbarManager;
	protected IMenuManager menuManager;
	
	
	@Override
	public void setDisplay(Display display) {
		this.display = display;
		
	}

	@Override
	public void setToolbarManager(IToolBarManager toolbarManager) {
		this.toolbarManager = toolbarManager;
		
	}

	@Override
	public void setMenuManager(IMenuManager menuManager) {
		this.menuManager = menuManager;
		
	}


}
