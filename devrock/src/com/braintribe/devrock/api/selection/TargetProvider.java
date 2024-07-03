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
package com.braintribe.devrock.api.selection;

import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IWorkingSet;

import com.braintribe.common.lcd.Pair;

/**
 * interface to deliver targets for import ... <br/>
 * actually, they reflect the current selected entry in the package explorer
 * <ul> 
 * <li>IWorkingSet</li>
 * <li>IProject</li>
 * </ul>
 * @author pit
 *
 */
public interface TargetProvider {
	
	/**
	 * returns a {@link SelectionTuple} of currently selected {@link IWorkingSet} (if any) and currently selected IProject (if any)
	 */
	Pair<IProject,IWorkingSet> getSelectionTuple();
	
	/**
	 * returns the currently selected {@link IWorkingSet} if any 
	 * @return - the currently selected {@link IWorkingSet} or null if none's selected (or cannot be derived from the current selection)
	 */
	IWorkingSet getTargetWorkingSet();
	/**
	 * returns the first currently selected {@link IProject} if any 
	 * @return - the currently selected {@link IProject} or null if none's selected (or cannot be derived from the current selection)
	 */
	IProject getTargetProject();
	
	/**
	 * returns all currently selected {@link IProject}s if any 
	 * @return - all the currently selected {@link IProject}s or null if none's selected (or cannot be derived from the current selection)
	 */
	Set<IProject> getTargetProjects();
	
	void clear(); 
}
