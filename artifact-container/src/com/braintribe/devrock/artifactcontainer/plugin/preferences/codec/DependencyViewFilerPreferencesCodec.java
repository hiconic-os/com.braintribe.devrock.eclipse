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
import com.braintribe.model.malaclypse.cfg.preferences.ac.views.dependency.DependencyViewPreferences;
import com.braintribe.model.malaclypse.cfg.preferences.ac.views.dependency.FilterType;

public class DependencyViewFilerPreferencesCodec implements Codec<IPreferenceStore, DependencyViewPreferences> {
	private IPreferenceStore store;

	public DependencyViewFilerPreferencesCodec(IPreferenceStore store) {
		this.store = store;
	}

	@Override
	public IPreferenceStore decode(DependencyViewPreferences filterPreferences) throws CodecException {
		store.setValue( ArtifactContainerPreferenceConstants.PC_DEPVIEW_FILTER, filterPreferences.getFilterExpression()); 		
 		store.setValue( ArtifactContainerPreferenceConstants.PC_DEPVIEW_FILTER_STATE, filterPreferences.getFilterType().toString());
		return store;
	}

	@Override
	public DependencyViewPreferences encode(IPreferenceStore store) throws CodecException {
		DependencyViewPreferences filterPreferences = DependencyViewPreferences.T.create();
		filterPreferences.setFilterExpression( store.getString( ArtifactContainerPreferenceConstants.PC_DEPVIEW_FILTER));
		String filterTypeAsString = store.getString( ArtifactContainerPreferenceConstants.PC_DEPVIEW_FILTER_STATE);
		try {
			filterPreferences.setFilterType( FilterType.valueOf(filterTypeAsString));
		} catch (Exception e) {
			filterPreferences.setFilterType( FilterType.simple);
		}
		return filterPreferences;
	}

	@Override
	public Class<IPreferenceStore> getValueClass() {
		return IPreferenceStore.class;
	}

}
