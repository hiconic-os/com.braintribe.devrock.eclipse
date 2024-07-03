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
package com.braintribe.eclipse.model.nature;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import com.braintribe.devrock.virtualenvironment.VirtualEnvironmentPlugin;
import com.braintribe.ve.api.VirtualEnvironment;
import com.braintribe.ve.impl.StandardEnvironment;

public class EclipseVirtualEnvironment implements VirtualEnvironment {
	public static final EclipseVirtualEnvironment INSTANCE = new EclipseVirtualEnvironment(); 

	private static final String missing = new String();
	
	@Override
	public String getProperty(String name) {
		return get(name, VirtualEnvironmentPlugin::getPropertyOverrides, StandardEnvironment.INSTANCE::getProperty);
	}
	
	@Override
	public String getEnv(String name) {
		return get(name, VirtualEnvironmentPlugin::getEnvironmentOverrides, StandardEnvironment.INSTANCE::getEnv);
	}
	
	
	private String get(String name, Supplier<Map<String, String>> overridesSupplier, Function<String, String> fallbackLookup) {
		if (VirtualEnvironmentPlugin.getOverrideActivation()) {
			Map<String, String> overrides = overridesSupplier.get();
			
			String value = overrides.getOrDefault(name, missing);
			
			if (value != missing)
				return value;
		}
		
		return fallbackLookup.apply(name);
	}

}
