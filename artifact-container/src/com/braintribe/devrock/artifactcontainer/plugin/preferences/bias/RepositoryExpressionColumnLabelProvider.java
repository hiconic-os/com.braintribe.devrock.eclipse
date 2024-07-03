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
package com.braintribe.devrock.artifactcontainer.plugin.preferences.bias;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.braintribe.build.artifact.retrieval.multi.repository.reflection.impl.bias.ArtifactBias;

public class RepositoryExpressionColumnLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		ArtifactBias pairing = (ArtifactBias) element;
		StringBuilder builder = new StringBuilder();
		
		for (String repo : pairing.getActiveRepositories()) {
			if (builder.length() > 0) {
				builder.append(',');
			}
			builder.append( repo);
		}
		for (String repo : pairing.getInactiveRepositories()) {
			if (builder.length() > 0) {
				builder.append(',');
			}
			builder.append( "!"+ repo);
		}
		
		return builder.toString();
	}

	@Override
	public String getToolTipText(Object element) {		
		return "display the repository information"; 
	}

}
