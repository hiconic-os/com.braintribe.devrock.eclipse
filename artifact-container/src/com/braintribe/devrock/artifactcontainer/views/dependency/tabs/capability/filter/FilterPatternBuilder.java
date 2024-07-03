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
package com.braintribe.devrock.artifactcontainer.views.dependency.tabs.capability.filter;

import com.braintribe.devrock.artifactcontainer.ArtifactContainerPlugin;
import com.braintribe.model.malaclypse.cfg.preferences.ac.views.dependency.DependencyViewPreferences;

public class FilterPatternBuilder {

	public static String getPattern( ) {
		ArtifactContainerPlugin plugin = ArtifactContainerPlugin.getInstance();
		DependencyViewPreferences dvPreferences = plugin.getArtifactContainerPreferences(false).getDependencyViewPreferences();
		String filter = dvPreferences.getFilterExpression();
		if (filter == null || filter.length() == 0)
			return null;		
		try {
		
			switch (dvPreferences.getFilterType()) {
			case regexp:
				return filter;
			default:
				break;
			}
		} catch (Exception e) {
		}
		
		String pattern = filter.replace(".", "\\.");
		pattern = pattern.replace( "*", ".*");
		
		return pattern;
	}
}
