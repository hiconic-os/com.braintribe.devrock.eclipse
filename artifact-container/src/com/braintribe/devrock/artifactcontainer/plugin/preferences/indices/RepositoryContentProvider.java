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
package com.braintribe.devrock.artifactcontainer.plugin.preferences.indices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.braintribe.model.ravenhurst.interrogation.RavenhurstBundle;

public class RepositoryContentProvider implements IStructuredContentProvider {

	private List<RavenhurstBundle> bundles = null;

	
	public List<RavenhurstBundle> getBundles() {
		return bundles;
	}

	public void setBundles(List<RavenhurstBundle> pairings) {
		this.bundles = pairings;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {	
	}

	@Override
	public Object[] getElements(Object arg0) {	
		return bundles.toArray();
	}

	
	public List<RavenhurstBundle> getBundlesAtPositions( int ... positions) {
		if (positions == null) {
			return Collections.emptyList();
		}
		List<RavenhurstBundle> result = new ArrayList<>();
		for (int i : positions) {
			result.add( bundles.get(i));
		}		
		return result;
	}

}
