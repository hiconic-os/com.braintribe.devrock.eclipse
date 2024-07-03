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
package com.braintribe.devrock.preferences.contributer;

/**
 * an interface any contributing plugin must implement:<br/>
 * the information that the {@link PreferencesContributerRegistry} requires to handle the preferences management.
 * @author Pit
 *
 */
public interface PreferencesContributionDeclaration extends PreferencesContributer{
	/**
	 * returns the tooltip that should be shown if the plugin is selected in the table
	 * @return - the tooltip as {@link String}
	 */
	String getTooltip();
	/**
	 * the file name of the preferences file
	 * @return - the name as {@link String}
	 */
	String getLocalFileName();
	/**
	 * the full path to the current preferences file (even if there's none currently)
	 * @return - the path as {@link String}
	 */
	String getFullFilePath();
	
	/**
	 * the partial path, i.e. the part relative to workspace 
	 * @return - the path as {@link String}
	 */
	String getPartialFilePath();
}
