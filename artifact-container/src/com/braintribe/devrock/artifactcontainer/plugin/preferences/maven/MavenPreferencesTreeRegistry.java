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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.braintribe.build.artifact.representations.RepresentationException;
import com.braintribe.build.artifact.representations.artifact.maven.settings.MavenSettingsReader;
import com.braintribe.devrock.artifactcontainer.ArtifactContainerPlugin;
import com.braintribe.devrock.artifactcontainer.ArtifactContainerStatus;
import com.braintribe.model.maven.settings.Mirror;
import com.braintribe.model.maven.settings.Profile;
import com.braintribe.model.maven.settings.Repository;
import com.braintribe.model.maven.settings.Server;

public class MavenPreferencesTreeRegistry {

	private MavenSettingsReader reader;
	private Map<Repository, Profile> repositoryOwnerMap;
		
	public void setup(MavenSettingsReader reader) {
		this.reader = reader;
		repositoryOwnerMap = new HashMap<Repository, Profile>();
		try {
			List<Profile> profiles = reader.getAllProfiles();
			for (Profile profile : profiles) {
				List<Repository> repositories = getRepositoriesOfProfile(profile);
				for (Repository repository : repositories) {
					repositoryOwnerMap.put(repository, profile);
				}
			}
		} catch (RepresentationException e) {
			String msg = "cannot retrieve repositories";
			ArtifactContainerStatus status = new ArtifactContainerStatus(msg, e);
			ArtifactContainerPlugin.getInstance().log(status);
		}		
	}
		
	public boolean isProfileActive( Profile profile) {
		try {
			return reader.isProfileActive( profile);
		} catch (RepresentationException e) {
			return false;
		}
	}
	public Profile getProfileOfRepository( Repository repository) {
		return repositoryOwnerMap.get(repository);
	}

	public String getOriginOfProfile( Profile profile) {
		try {
			return reader.getProperty( profile.getId(), "MC_ORIGIN");
		} catch (RepresentationException e) {
			return null;
		}
	}
	public List<Repository> getRepositoriesOfProfile( Profile profile) {		
		return profile.getRepositories();		
	}
	
	public boolean getDynamicUpdatePolicySupport( Repository repository) {
		Profile profile = getProfileOfRepository(repository);
		if (profile == null)
			return false;
		try {
			return reader.isDynamicRepository(profile, repository);
		} catch (RepresentationException e) {
			return false;
		}	
	}
	
	public Mirror getMirror( Repository repository) {
		try {
			return reader.getMirror( repository.getId(), repository.getUrl());
		} catch (RepresentationException e) {
			return null;
		}
	}
	
	public Server getServer( Repository repository){
		Mirror mirror = getMirror(repository);
		if (mirror != null) {
			try {
				return reader.getServerById( mirror.getId());
			} catch (RepresentationException e) {
				return null;
			}
		}
		else {
			return reader.getServerById( repository.getId());
		}
	
	}
}
