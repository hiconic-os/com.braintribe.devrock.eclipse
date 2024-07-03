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
package com.braintribe.devrock.artifactcontainer.plugin.malaclypse.scope.overrides;

import com.braintribe.build.artifact.representations.RepresentationException;
import com.braintribe.build.artifact.representations.artifact.maven.settings.LocalRepositoryLocationProvider;
import com.braintribe.build.artifact.virtualenvironment.VirtualPropertyResolver;
import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.devrock.artifactcontainer.ArtifactContainerPlugin;
import com.braintribe.model.malaclypse.cfg.preferences.mv.MavenPreferences;


/**
 * an implementation of the {@link LocalRepositoryLocationProvider} that heads the override from the preferences, 
 * and the the {@link VirtualPropertyResolver}
 * @author pit
 *
 */
public class OverrideLocalRepositoryRetrievalExpert implements LocalRepositoryLocationProvider {	
	private VirtualPropertyResolver propertyResolver;
	private ArtifactContainerPlugin plugin = ArtifactContainerPlugin.getInstance();
	
	@Configurable  @Required
	public void setPropertyResolver(VirtualPropertyResolver propertyResolver) {
		this.propertyResolver = propertyResolver;
	}
	
	@Override
	public String getLocalRepository(String expression) throws RepresentationException {
		if (expression == null) {
			expression = "${env.M2_REPO}";
		}
		MavenPreferences preferences = plugin.getArtifactContainerPreferences(false).getMavenPreferences();
		String override = preferences.getLocalRepositoryOverride();
		if (override != null) {
			return propertyResolver.resolve(override);
		}	
		return propertyResolver.resolve(expression);
	}

}
