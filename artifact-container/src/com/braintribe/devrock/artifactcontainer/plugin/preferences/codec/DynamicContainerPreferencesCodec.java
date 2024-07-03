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
import com.braintribe.model.malaclypse.cfg.denotations.clash.ResolvingInstant;
import com.braintribe.model.malaclypse.cfg.preferences.ac.container.DynamicContainerPreferences;

public class DynamicContainerPreferencesCodec implements Codec<IPreferenceStore, DynamicContainerPreferences> {
	private IPreferenceStore store;

	public DynamicContainerPreferencesCodec(IPreferenceStore store) {
		this.store = store;
	}

	@Override
	public IPreferenceStore decode(DynamicContainerPreferences preferences) throws CodecException {
		store.setValue( ArtifactContainerPreferenceConstants.PC_MAX_CONCURRENT_WALK_BATCH_SIZE, preferences.getConcurrentWalkBatchSize());
		ResolvingInstant instant = preferences.getClashResolvingInstant();
		store.setValue( ArtifactContainerPreferenceConstants.PC_CLASH_RESOLVING_INSTANT, instant.toString());
		store.setValue( ArtifactContainerPreferenceConstants.PC_CHAIN_WALKS, preferences.getChainArtifactSync());
		return store;
	}

	@Override
	public DynamicContainerPreferences encode(IPreferenceStore store) throws CodecException {
		DynamicContainerPreferences preferences = DynamicContainerPreferences.T.create();
		preferences.setConcurrentWalkBatchSize( store.getInt( ArtifactContainerPreferenceConstants.PC_MAX_CONCURRENT_WALK_BATCH_SIZE));
		
		String resolvingInstantAsString = store.getString( ArtifactContainerPreferenceConstants.PC_CLASH_RESOLVING_INSTANT);
		if (resolvingInstantAsString != null && resolvingInstantAsString.length() > 0) {
			preferences.setClashResolvingInstant( ResolvingInstant.valueOf(resolvingInstantAsString));
		}		
		preferences.setChainArtifactSync( store.getBoolean( ArtifactContainerPreferenceConstants.PC_CHAIN_WALKS));
		
		return preferences;
	}

	@Override
	public Class<IPreferenceStore> getValueClass() {
		return IPreferenceStore.class;
	}

}
