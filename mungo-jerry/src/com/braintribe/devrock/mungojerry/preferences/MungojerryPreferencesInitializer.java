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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.preference.IPreferenceStore;

import com.braintribe.codec.CodecException;
import com.braintribe.devrock.mungojerry.plugin.Mungojerry;
import com.braintribe.logging.Logger;
import com.braintribe.model.malaclypse.cfg.preferences.gwt.GwtPreferences;
import com.braintribe.model.malaclypse.cfg.preferences.mj.MungojerryPreferences;
import com.braintribe.model.malaclypse.cfg.preferences.mv.MavenPreferences;

public class MungojerryPreferencesInitializer {
	private static Logger log = Logger.getLogger(MungojerryPreferencesInitializer.class);
	
	public static void initializeDefaultPreferences(IPreferenceStore store) {		
		MungojerryPreferences preferences = initializeMungojerryPreferences();
		MungojerryPreferencesCodec codec = new MungojerryPreferencesCodec(store);
		try {
			codec.decode(preferences);
		} catch (CodecException e) {
			String msg="cannot decode initial preferences";
			log.error( msg, e);
			Mungojerry.log( IStatus.ERROR, msg + " as " + e.getMessage());
		}
	}
	
	public static GwtPreferences initializeGwtPreferences() {
		GwtPreferences preferences = GwtPreferences.T.create();
		preferences.setAutoInjectLibrary( "com.google.gwt:gwt-user#2.8.0.jar");
		return preferences;
	}
	
	public static MavenPreferences initializeMavenPreferences() {
		return MavenPreferences.T.create();
	}
	
	
	public static MungojerryPreferences initializeMungojerryPreferences() {
		MungojerryPreferences preferences = MungojerryPreferences.T.create();
		GwtPreferences gwtPreferences = initializeGwtPreferences();
		preferences.setGwtPreferences(gwtPreferences);
		MavenPreferences mvPreferences = initializeMavenPreferences();
		preferences.setMavenPreferences(mvPreferences);		
		return preferences;
	}
}
