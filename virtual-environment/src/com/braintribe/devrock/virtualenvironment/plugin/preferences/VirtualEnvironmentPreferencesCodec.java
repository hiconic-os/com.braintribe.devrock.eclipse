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
package com.braintribe.devrock.virtualenvironment.plugin.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import com.braintribe.codec.Codec;
import com.braintribe.codec.CodecException;
import com.braintribe.codec.json.genericmodel.GenericModelJsonStringCodec;
import com.braintribe.model.malaclypse.cfg.preferences.ve.VirtualEnvironmentPreferences;

public class VirtualEnvironmentPreferencesCodec implements Codec<IPreferenceStore, VirtualEnvironmentPreferences> {
	private GenericModelJsonStringCodec<VirtualEnvironmentPreferences> codec = new GenericModelJsonStringCodec<VirtualEnvironmentPreferences>();
	private IPreferenceStore store;
	
	public VirtualEnvironmentPreferencesCodec(IPreferenceStore store) {
		this.store = store;
	}

	@Override
	public IPreferenceStore decode(VirtualEnvironmentPreferences preferences) throws CodecException {		
		if (preferences == null)
			return store;
				
		String overrides= codec.encode(preferences);
		store.setValue( VirtualEnvironmentPreferenceConstants.PC_VE_OVERRIDES, overrides);
		return store;
	}

	@Override
	public VirtualEnvironmentPreferences encode(IPreferenceStore store) throws CodecException {				
		String overrides = store.getString(VirtualEnvironmentPreferenceConstants.PC_VE_OVERRIDES);
		if (overrides != null && overrides.length() > 0) {
			VirtualEnvironmentPreferences preferences = codec.decode(overrides); 		
			return preferences;
		}
		else {
			return VirtualEnvironmentPreferences.T.create();
		}
	}

	@Override
	public Class<IPreferenceStore> getValueClass() {	
		return IPreferenceStore.class;
	}

	
}
