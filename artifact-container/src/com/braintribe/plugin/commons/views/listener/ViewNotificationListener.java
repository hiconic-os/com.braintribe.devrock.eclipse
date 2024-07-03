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
package com.braintribe.plugin.commons.views.listener;

import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.part.ViewPart;

import com.braintribe.model.malaclypse.WalkMonitoringResult;

public interface ViewNotificationListener {
	/**
	 * the view (or actually rather the part) has been made visible 
	 * @param key - the name of the part {@link IWorkbenchPartReference}'s part name
	 */
	void acknowledgeVisibility( String key);
	
	/**
	 * the view (or the actually rather the {@link ViewPart}) has been made invisible
	 * @param key - the name of the part 
	 */
	void acknowledgeInvisibility( String key);
	
	/**
	 * the current project has changed 
	 * @param project - the {@link IProject} that is current (selected in the package explorer)
	 */
	void acknowledgeProjectChanged( IProject project);
	
	/**
	 * lock the current terminal, i.e. do not react to change notifications
	 * @param lock
	 */
	void acknowledgeLockTerminal( boolean lock);
	
	void acknowledgeExternalMonitorResult( WalkMonitoringResult result);
}
