// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.devrock.artifactcontainer.plugin.preferences.codec;

import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;

import com.braintribe.codec.Codec;
import com.braintribe.codec.CodecException;
import com.braintribe.codec.json.genericmodel.GenericModelJsonStringCodec;
import com.braintribe.devrock.artifactcontainer.plugin.preferences.ArtifactContainerPreferenceConstants;
import com.braintribe.model.malaclypse.cfg.preferences.svn.SourceRepositoryPairing;
import com.braintribe.model.malaclypse.cfg.preferences.svn.SvnPreferences;

public class SvnPreferencesCodec implements Codec<IPreferenceStore, SvnPreferences> {
	
	private IPreferenceStore store;
	private GenericModelJsonStringCodec<List<SourceRepositoryPairing>> codec = new GenericModelJsonStringCodec<List<SourceRepositoryPairing>>();

	public SvnPreferencesCodec(IPreferenceStore store) {
		this.store = store;
	}

	@Override
	public IPreferenceStore decode(SvnPreferences svnPreferences) throws CodecException {
		List<SourceRepositoryPairing> pairings = svnPreferences.getSourceRepositoryPairings();
		String value = codec.encode(pairings);
		store.setValue( ArtifactContainerPreferenceConstants.PC_SVN_SOURCE_REPOSITORY_PAIRINGS, value);
		
		/*
		String url = svnPreferences.getUrl();
		if (url != null) {
			store.setValue(ArtifactContainerPreferenceConstants.PC_SVN_URL, url);
		}
		else {
			store.setValue( ArtifactContainerPreferenceConstants.PC_SVN_URL, ArtifactContainerPreferenceConstants.PC_SVN_URL_AUTO);
		}
		store.setValue(ArtifactContainerPreferenceConstants.PC_SVN_PATH, svnPreferences.getWorkingCopy());
		*/
		return store;
	}

	@Override
	public SvnPreferences encode(IPreferenceStore store) throws CodecException {
		// legacy settings 
		SvnPreferences svnPreferences = SvnPreferences.T.create();
		String value = store.getString( ArtifactContainerPreferenceConstants.PC_SVN_URL);
		if (ArtifactContainerPreferenceConstants.PC_SVN_URL_AUTO.equalsIgnoreCase( value)) {
			svnPreferences.setUrl( value);
		}
		else {
			svnPreferences.setUrl( null);
		}
		svnPreferences.setWorkingCopy( store.getString( ArtifactContainerPreferenceConstants.PC_SVN_PATH));
		
		// real settings 	
		value = store.getString( ArtifactContainerPreferenceConstants.PC_SVN_SOURCE_REPOSITORY_PAIRINGS);
		if (value != null && value.length() > 0) {
			List<SourceRepositoryPairing> pairings = codec.decode(value);
			svnPreferences.setSourceRepositoryPairings(pairings);
		}
		return svnPreferences;
	}

	@Override
	public Class<IPreferenceStore> getValueClass() {		
		return IPreferenceStore.class;
	}

}
