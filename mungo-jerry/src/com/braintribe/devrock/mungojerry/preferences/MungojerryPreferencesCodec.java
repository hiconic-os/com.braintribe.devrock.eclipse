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
package com.braintribe.devrock.mungojerry.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import com.braintribe.codec.Codec;
import com.braintribe.codec.CodecException;
import com.braintribe.model.malaclypse.cfg.preferences.mj.MungojerryPreferences;

public class MungojerryPreferencesCodec implements Codec<IPreferenceStore, MungojerryPreferences> {

	private IPreferenceStore store;
	private GwtPreferencesCodec gwtPreferencesCodec;
	private MavenPreferencesCodec mvPreferencesCodec;

	public MungojerryPreferencesCodec(IPreferenceStore store) {
		this.store = store;
		gwtPreferencesCodec = new GwtPreferencesCodec(store);
		mvPreferencesCodec = new MavenPreferencesCodec(store);

	}

	@Override
	public IPreferenceStore decode(MungojerryPreferences mjPreferences) throws CodecException {
		// gwt		
		gwtPreferencesCodec.decode( mjPreferences.getGwtPreferences());
		mvPreferencesCodec.decode(mjPreferences.getMavenPreferences());
		return store;
	}

	@Override
	public MungojerryPreferences encode(IPreferenceStore store) throws CodecException {
		MungojerryPreferences mjPreferences = MungojerryPreferences.T.create();
		mjPreferences.setGwtPreferences( gwtPreferencesCodec.encode(store));
		mjPreferences.setMavenPreferences( mvPreferencesCodec.encode(store));
		return mjPreferences;
	}

	@Override
	public Class<IPreferenceStore> getValueClass() {
		return IPreferenceStore.class;
	}

	
}
