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
package com.braintribe.devrock.artifactcontainer.plugin.preferences.maven;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.model.maven.settings.Profile;
import com.braintribe.model.maven.settings.Repository;

public class MavenPreferencesTreeContentProvider implements ITreeContentProvider {

	private MavenPreferencesTreeRegistry registry;
	
	@Configurable @Required
	public void setRegistry(MavenPreferencesTreeRegistry registry) {
		this.registry = registry;
	}
	
	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	}

	@Override
	public Object[] getChildren(Object arg0) {
		Profile profile = (Profile) arg0;		
		List<Repository> repositories= registry.getRepositoriesOfProfile(profile);
		if (repositories == null)
			return null;		
		return repositories.toArray();				
	}

	@Override
	public Object[] getElements(Object arg0) {
		@SuppressWarnings("unchecked")
		List<Profile> profiles = (List<Profile>) arg0;
		return profiles.toArray();
	}

	@Override
	public Object getParent(Object arg0) {
		if (arg0 instanceof Repository) {
			Repository repository = (Repository) arg0;
			return registry.getProfileOfRepository(repository);
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object arg0) {
		if (arg0 instanceof Profile) {
			Profile profile = (Profile) arg0;
			if (getChildren(profile) != null) {
				return true;			
			}
		}
		return false;
	}

	
}
