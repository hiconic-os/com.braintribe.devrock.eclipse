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

import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;

import com.braintribe.codec.Codec;
import com.braintribe.codec.CodecException;
import com.braintribe.devrock.artifactcontainer.plugin.preferences.ArtifactContainerPreferenceConstants;
import com.braintribe.model.malaclypse.cfg.preferences.ac.qi.QuickImportPreferences;
import com.braintribe.model.malaclypse.cfg.preferences.ac.qi.VersionModificationAction;

public class QuickImportPreferencesCodec implements Codec<IPreferenceStore, QuickImportPreferences> {
	
	private IPreferenceStore store;

	public QuickImportPreferencesCodec( IPreferenceStore store) {
		this.store = store;		
	}

	@Override
	public IPreferenceStore decode(QuickImportPreferences quickImportPreferences) throws CodecException {		
		Boolean value = quickImportPreferences.getAlternativeUiNature(); 
		store.setValue(ArtifactContainerPreferenceConstants.PC_QI_CONDENSED_IMPORT_STYLE, Boolean.TRUE.equals(value));
		value = quickImportPreferences.getLocalOnlyNature();
		store.setValue(ArtifactContainerPreferenceConstants.PC_QI_CONDENSED_IMPORT_LOCAL, !Boolean.FALSE.equals(value));	
		value = quickImportPreferences.getAttachToCurrentProject();
		store.setValue( ArtifactContainerPreferenceConstants.PC_QI_CONDENSED_IMPORT_ATTACH, Boolean.TRUE.equals(value));
		
		int num = quickImportPreferences.getExpectedNumberOfSources();
		store.setValue(ArtifactContainerPreferenceConstants.PC_QI_NUM_OF_SOURCES, "" + num);
		
		VersionModificationAction copyMode = quickImportPreferences.getLastDependencyCopyMode();
		store.setValue( ArtifactContainerPreferenceConstants.PC_LAST_DEPENDENCY_COPY_MODE, copyMode.toString());
		
		VersionModificationAction pasteMode = quickImportPreferences.getLastDependencyPasteMode();
		store.setValue( ArtifactContainerPreferenceConstants.PC_LAST_DEPENDENCY_PASTE_MODE, pasteMode.toString());
		
		Map<String,String> archetypeMap = quickImportPreferences.getArchetypeToAssetMap();
		if (archetypeMap != null && !archetypeMap.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : archetypeMap.entrySet()) {
				if (sb.length() > 0) {
					sb.append(";");
				}
				sb.append( entry.getKey() + ":" + entry.getValue());
			}
			store.setValue( ArtifactContainerPreferenceConstants.PC_ARCHETYPE_MAP, sb.toString());
		}
		
		value = quickImportPreferences.getPrimeWithSelection();
		store.setValue(ArtifactContainerPreferenceConstants.PC_QI_PRIMEWITH_SELECTION, Boolean.TRUE.equals(value));
		return store;
	}

	@Override
	public QuickImportPreferences encode(IPreferenceStore store) throws CodecException {
		QuickImportPreferences quickImportPreferences = QuickImportPreferences.T.create();
		quickImportPreferences.setLocalOnlyNature( store.getBoolean( ArtifactContainerPreferenceConstants.PC_QI_CONDENSED_IMPORT_LOCAL));
		quickImportPreferences.setAlternativeUiNature( store.getBoolean( ArtifactContainerPreferenceConstants.PC_QI_CONDENSED_IMPORT_STYLE));
		quickImportPreferences.setAttachToCurrentProject( store.getBoolean( ArtifactContainerPreferenceConstants.PC_QI_CONDENSED_IMPORT_ATTACH));
		
		quickImportPreferences.setExpectedNumberOfSources( store.getInt(ArtifactContainerPreferenceConstants.PC_QI_NUM_OF_SOURCES));
		
		
		String copyModeAsString = store.getString( ArtifactContainerPreferenceConstants.PC_LAST_DEPENDENCY_COPY_MODE);
		if (copyModeAsString != null) {
			quickImportPreferences.setLastDependencyCopyMode( VersionModificationAction.valueOf( copyModeAsString));
		}
		
		String pasteModeAsString = store.getString( ArtifactContainerPreferenceConstants.PC_LAST_DEPENDENCY_PASTE_MODE);
		if (pasteModeAsString != null) {
			quickImportPreferences.setLastDependencyPasteMode( VersionModificationAction.valueOf( pasteModeAsString));
		}
		
		String archetypeMapAsString = store.getString( ArtifactContainerPreferenceConstants.PC_ARCHETYPE_MAP);
		if (archetypeMapAsString != null && archetypeMapAsString.length() > 0) {
			String [] tokens = archetypeMapAsString.split(",");
			for (String token : tokens) {
				int c = token.indexOf( ':');
				String archetype = token.substring(0, c);
				String tagValue = token.substring(c+1);
				quickImportPreferences.getArchetypeToAssetMap().put( archetype, tagValue);
			}
		}
		
		quickImportPreferences.setPrimeWithSelection( store.getBoolean( ArtifactContainerPreferenceConstants.PC_QI_PRIMEWITH_SELECTION));
		
		return quickImportPreferences;
	}

	@Override
	public Class<IPreferenceStore> getValueClass() {
		return IPreferenceStore.class;
	}

}
