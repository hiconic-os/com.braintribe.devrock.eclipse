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
package com.braintribe.devrock.greyface.process.scan;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.braintribe.model.artifact.Dependency;
import com.braintribe.model.artifact.processing.artifact.ArtifactProcessor;
import com.braintribe.model.malaclypse.cfg.preferences.gf.RepositorySetting;

/**
 * @author Pit
 *
 */
public class DependencyCache {
	private Map<RepositorySetting, Set<Dependency>> cachedSettingToDependencyMap = new HashMap<RepositorySetting, Set<Dependency>>();
	
	
	public synchronized boolean isCached( RepositorySetting source, Dependency dependency) {
		Set<Dependency> cachedDependencies = cachedSettingToDependencyMap.get( source);
		if (cachedDependencies == null) {
			cachedDependencies = new HashSet<Dependency>(1);
			cachedSettingToDependencyMap.put( source, cachedDependencies);
		}
		boolean alreadyProcessed = false;
		for (Dependency suspect : cachedDependencies) {							
			if (ArtifactProcessor.coarsestDependencyEquals( dependency, suspect)) {
				alreadyProcessed = true;
				break;
			}							
		}
		if (!alreadyProcessed) {
			cachedDependencies.add(dependency);
		}
		return alreadyProcessed;
	}
	
	
}
