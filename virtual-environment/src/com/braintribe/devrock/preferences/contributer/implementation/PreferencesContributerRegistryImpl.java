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
package com.braintribe.devrock.preferences.contributer.implementation;

import java.util.Set;

import com.braintribe.cc.lcd.CodingSet;
import com.braintribe.devrock.preferences.contributer.PreferencesContributerImplementation;
import com.braintribe.devrock.preferences.contributer.PreferencesContributerRegistry;
import com.braintribe.devrock.preferences.contributer.PreferencesContributionDeclaration;

public class PreferencesContributerRegistryImpl implements PreferencesContributerRegistry {
	
	private Set<PreferencesContributionDeclaration> declarations = CodingSet.createHashSetBased(new PreferencesContributorDeclarationWrapperCodec());
	private Set<PreferencesContributerImplementation> implementations = CodingSet.createHashSetBased(new PreferencesContributorImplementationWrapperCodec());
	

	@Override
	public void addContributionDeclaration(PreferencesContributionDeclaration contributer) {
		declarations.add(contributer);

	}

	@Override
	public void removeContributionDeclaration(PreferencesContributionDeclaration contributer) {
		declarations.remove(contributer);

	}

	@Override
	public void addContributerImplementation(PreferencesContributerImplementation contributer) {
		implementations.add(contributer);

	}

	@Override
	public void removeContributerImplementation(PreferencesContributerImplementation contributer) {
		implementations.remove(contributer);
	}
	
	public Set<PreferencesContributionDeclaration> getDeclarations() {
		return declarations;
	}
	
	public Set<PreferencesContributerImplementation> getImplementations() {
		return implementations;
	}

}
