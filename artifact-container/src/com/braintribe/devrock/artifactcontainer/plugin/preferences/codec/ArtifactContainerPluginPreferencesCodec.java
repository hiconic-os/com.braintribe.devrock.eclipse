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
import com.braintribe.model.malaclypse.cfg.preferences.ac.ArtifactContainerPreferences;

public class ArtifactContainerPluginPreferencesCodec implements Codec<IPreferenceStore, ArtifactContainerPreferences> {
	
	private AntPreferencesCodec antPreferencesCodec;
	private SvnPreferencesCodec svnPreferencesCodec;
	private QuickImportPreferencesCodec quickImportPreferencesCodec;
	private RavenhurstPreferencesCodec ravenhurstPreferencesCodec;
	private ProfilePreferencesCodec profilePreferencesCodec;
	private DependencyViewFilerPreferencesCodec dependencyFilterPreferencesCodec;
	private GwtPreferencesCodec gwtPreferencesCodec;
	private ClasspathPreferencesCodec classpathPreferencesCodec;
	private MavenPreferencesCodec mavenPreferencesCodec;
	private DynamicContainerPreferencesCodec dynamicContainerCodec;
	private TbRunnerPreferencesCodec tbRunnerCodec;

	
	private IPreferenceStore iPreferenceStore; 
	
	public ArtifactContainerPluginPreferencesCodec( IPreferenceStore store) {
		this.iPreferenceStore = store;
		
		antPreferencesCodec = new AntPreferencesCodec(store);
		svnPreferencesCodec = new SvnPreferencesCodec(store);
		quickImportPreferencesCodec = new QuickImportPreferencesCodec(store);
		ravenhurstPreferencesCodec = new RavenhurstPreferencesCodec(store);
		profilePreferencesCodec = new ProfilePreferencesCodec(store);
		dependencyFilterPreferencesCodec = new DependencyViewFilerPreferencesCodec(store);
		gwtPreferencesCodec = new GwtPreferencesCodec(store);
		classpathPreferencesCodec = new ClasspathPreferencesCodec(store);
		mavenPreferencesCodec = new MavenPreferencesCodec(store);
		dynamicContainerCodec = new DynamicContainerPreferencesCodec(store);
		tbRunnerCodec = new TbRunnerPreferencesCodec(store);
		
	}

	@Override
	public IPreferenceStore decode(ArtifactContainerPreferences preferences) throws CodecException {
		
		// do our stuff directly 
		dynamicContainerCodec.decode(preferences.getDynamicContainerPreferences());
		
		// static maven 
		classpathPreferencesCodec.decode( preferences.getClasspathPreferences());
		// gwt		
		gwtPreferencesCodec.decode( preferences.getGwtPreferences());

		// svn 
		svnPreferencesCodec.decode( preferences.getSvnPreferences());
		
		// ant
		antPreferencesCodec.decode( preferences.getAntRunnerPreferences());
				
		// quick import 
		quickImportPreferencesCodec.decode(preferences.getQuickImportPreferences());
		
		// ravenhurst.. 
		ravenhurstPreferencesCodec.decode(preferences.getRavenhurstPreferences());		
 		
 		// dependency view filter
 		dependencyFilterPreferencesCodec.decode(preferences.getDependencyViewPreferences());
 		
 		// profile settings
 		profilePreferencesCodec.decode(preferences.getProfilePreferences());
 		
 		//maven 
 		mavenPreferencesCodec.decode( preferences.getMavenPreferences());
 		
 		// tb 
 		tbRunnerCodec.decode(preferences.getTbRunnerPreferences());
 		
		return iPreferenceStore;
	}

	@Override
	public ArtifactContainerPreferences encode(IPreferenceStore store) throws CodecException {
		//
		// read the store data
		//
		ArtifactContainerPreferences preferences = ArtifactContainerPreferences.T.create();
		
		// only direct settings here 
		preferences.setDynamicContainerPreferences( dynamicContainerCodec.encode(store));
		
		// static maven 		
		preferences.setClasspathPreferences( classpathPreferencesCodec.encode(store));
		
		// gwt
		preferences.setGwtPreferences( gwtPreferencesCodec.encode(store));
		// svn		
		preferences.setSvnPreferences(svnPreferencesCodec.encode(store));
				
		// ant 
		preferences.setAntRunnerPreferences(antPreferencesCodec.encode(store));
		
		// quick import				
		preferences.setQuickImportPreferences( quickImportPreferencesCodec.encode(store));
		
		// ravenhurst
		preferences.setRavenhurstPreferences( ravenhurstPreferencesCodec.encode(store));
		
		// dependency view filter		
		preferences.setDependencyViewPreferences( dependencyFilterPreferencesCodec.encode(store));

		// profile 	
		preferences.setProfilePreferences( profilePreferencesCodec.encode(store));
		
		// maven
		preferences.setMavenPreferences( mavenPreferencesCodec.encode(store));
		
		//tb
		preferences.setTbRunnerPreferences( tbRunnerCodec.encode(store));
		return preferences;
	}

	@Override
	public Class<IPreferenceStore> getValueClass() {
		return IPreferenceStore.class;
	}

	
}
