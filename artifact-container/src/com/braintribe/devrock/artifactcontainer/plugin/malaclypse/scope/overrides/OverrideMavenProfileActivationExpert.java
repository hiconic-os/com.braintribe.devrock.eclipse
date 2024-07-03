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

import com.braintribe.build.artifact.representations.artifact.maven.settings.MavenProfileActivationExpertImpl;
import com.braintribe.build.artifact.virtualenvironment.VirtualPropertyResolver;
import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;

public class OverrideMavenProfileActivationExpert extends MavenProfileActivationExpertImpl {	
	private VirtualPropertyResolver virtualPropertyResolver;
	
	@Required @Configurable
	public void setVirtualPropertyResolver(VirtualPropertyResolver virtualPropertyResolver) {
		this.virtualPropertyResolver = virtualPropertyResolver;
	}

	@Override
	protected String resolveProperty(String key) {
		String keyToUse = "${" + key + "}";
		
		String value = virtualPropertyResolver.resolve(keyToUse);
		if (value != null)
			return value;
		return super.resolveProperty(keyToUse);
	} 

	
}
