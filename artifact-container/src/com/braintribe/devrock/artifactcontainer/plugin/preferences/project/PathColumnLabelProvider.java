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
package com.braintribe.devrock.artifactcontainer.plugin.preferences.project;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.braintribe.build.artifact.virtualenvironment.VirtualPropertyResolver;
import com.braintribe.devrock.artifactcontainer.ArtifactContainerPlugin;
import com.braintribe.model.malaclypse.cfg.preferences.svn.SourceRepositoryPairing;
import com.braintribe.model.panther.SourceRepository;

public class PathColumnLabelProvider extends ColumnLabelProvider {
	

	private boolean local;
	private VirtualPropertyResolver virtualPropertyResolver;

	public PathColumnLabelProvider(boolean local) {
		super();
		this.local = local;
		virtualPropertyResolver = ArtifactContainerPlugin.getInstance().getVirtualPropertyResolver();
	}

	@Override
	public String getText(Object element) {
		SourceRepositoryPairing pairing = (SourceRepositoryPairing) element;
		
		SourceRepository repository;
		if (local) {
			repository = pairing.getLocalRepresentation();
			return repository.getRepoUrl();
		} else {
			repository = pairing.getRemoteRepresentation();
			return repository.getRepoUrl();						
		}
		
	}

	@Override
	public String getToolTipText(Object element) {
		SourceRepositoryPairing pairing = (SourceRepositoryPairing) element;
		SourceRepository repository;
		if (local) {
			repository = pairing.getLocalRepresentation();
			return "path [" + repository.getRepoUrl() + "]";
		} else {
			repository = pairing.getRemoteRepresentation();
			return "path [" + repository.getRepoUrl() + "]";
			
		}
	
	}

}
