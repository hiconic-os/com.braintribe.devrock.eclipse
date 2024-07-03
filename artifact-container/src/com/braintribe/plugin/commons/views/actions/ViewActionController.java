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

import com.braintribe.plugin.commons.views.tabbed.tabs.AbstractViewTab;

/**
 * controls the availability of the actions depending of the currently selected tab
 * @author pit
 *
 * @param <T> - a sub type of {@link AbstractViewTab}
 */
public interface ViewActionController<T extends AbstractViewTab> {
	/**
	 * enables or disables the action in question according the selected tab  
	 * @param tab - a instance derving from {@link AbstractViewTab}
	 */
	void controlAvailablity(T tab);
}
