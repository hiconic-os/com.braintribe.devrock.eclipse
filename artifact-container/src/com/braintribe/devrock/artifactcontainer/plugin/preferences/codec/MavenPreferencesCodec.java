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
package com.braintribe.devrock.artifactcontainer.plugin.preferences.codec;

import org.eclipse.jface.preference.IPreferenceStore;

import com.braintribe.codec.Codec;
import com.braintribe.codec.CodecException;
import com.braintribe.devrock.artifactcontainer.plugin.preferences.ArtifactContainerPreferenceConstants;
import com.braintribe.model.malaclypse.cfg.preferences.mv.MavenPreferences;


public class MavenPreferencesCodec implements Codec<IPreferenceStore, MavenPreferences> {
	
	private IPreferenceStore store;

	public MavenPreferencesCodec(IPreferenceStore store) {
		this.store = store;
	}

	@Override
	public IPreferenceStore decode(MavenPreferences preferences) throws CodecException {		
		String confSettingsOverride = preferences.getConfSettingsOverride();
		if (confSettingsOverride == null)
			confSettingsOverride = "";
		store.setValue( ArtifactContainerPreferenceConstants.PC_MAVEN_CONF_OVERRIDE, confSettingsOverride);
		
		String userSettingsOverride = preferences.getUserSettingsOverride();
		if (userSettingsOverride == null)
			userSettingsOverride = "";
		store.setValue( ArtifactContainerPreferenceConstants.PC_MAVEN_USER_OVERRIDE, userSettingsOverride);
		
		String localRepositoryOverride = preferences.getLocalRepositoryOverride();
		if (localRepositoryOverride == null)
			localRepositoryOverride = "";
		store.setValue( ArtifactContainerPreferenceConstants.PC_MAVEN_LOCAL_OVERRIDE, localRepositoryOverride);
		
		return store;
	}

	@Override
	public MavenPreferences encode(IPreferenceStore store) throws CodecException {
		MavenPreferences preferences = MavenPreferences.T.create();
		
		String overrideConf = store.getString( ArtifactContainerPreferenceConstants.PC_MAVEN_CONF_OVERRIDE);
		if (overrideConf != null && overrideConf.length() > 0) {
			preferences.setConfSettingsOverride(overrideConf);
		}
		else {
			preferences.setConfSettingsOverride( null);
		}
		
		String overrideUser = store.getString( ArtifactContainerPreferenceConstants.PC_MAVEN_USER_OVERRIDE);
		if (overrideUser != null && overrideUser.length() > 0) {
			preferences.setUserSettingsOverride(overrideUser);
		}
		else {
			preferences.setUserSettingsOverride( null);
		}
		
		String overrideLocal = store.getString( ArtifactContainerPreferenceConstants.PC_MAVEN_LOCAL_OVERRIDE);
		if (overrideLocal != null && overrideLocal.length() > 0) {
			preferences.setLocalRepositoryOverride(overrideLocal);
		}
		else {
			preferences.setLocalRepositoryOverride( null);
		}
		return preferences;
	}

	@Override
	public Class<IPreferenceStore> getValueClass() {
		return IPreferenceStore.class;
	}

	
}
