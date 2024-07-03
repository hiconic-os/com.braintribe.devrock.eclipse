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
package com.braintribe.plugin.commons.views.tabbed.tabs;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.plugin.commons.views.TabItemImageListener;
import com.braintribe.plugin.commons.views.listener.ViewNotificationListener;

public abstract class AbstractViewTab implements ViewNotificationListener {

	protected TabItemImageListener tabImageListener;
	protected Display display;
	protected boolean active;
	protected boolean visible;
	protected AbstractViewTab instance;

	@Configurable @Required
	public void setTabImageListener(TabItemImageListener tabImageListener) {
		this.tabImageListener = tabImageListener;
	}

	public AbstractViewTab(Display display) {
		this.display = display;
		instance = this;
	}

	@Override
	public void acknowledgeVisibility(String key) {
		visible = true;
	}

	@Override
	public void acknowledgeInvisibility(String key) {
		visible = false;
	}
	
	public void acknowledgeActivation(){
		active = true;
	}
	public void acknowledgeDeactivation(){
		active = false;
	}
	
	public void dispose(){		
	}
		
	public abstract Composite createControl( Composite parent);
	
	

}
