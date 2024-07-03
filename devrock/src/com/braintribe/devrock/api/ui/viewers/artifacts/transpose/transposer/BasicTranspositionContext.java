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
package com.braintribe.devrock.api.ui.viewers.artifacts.transpose.transposer;

import com.braintribe.devrock.eclipse.model.storage.TranspositionContext;

/**
 * transposition context - contains the different configuration switches
 * @author pit
 *
 */
public class BasicTranspositionContext implements TranspositionContextBuilder {
	private boolean includeDependencies;
	private boolean includeDependers;
	
	private boolean includeParents;
	private boolean includeParentDependers;

	private boolean includeImports;
	private boolean includeImportDependers;
		
	private boolean includeParts;
	private boolean coalesceFiltered;
	
	private boolean detectProjects;

	@Override
	public TranspositionContextBuilder includeDependencies(boolean include) {
		includeDependencies = include;
		return this;
	}

	@Override
	public TranspositionContextBuilder includeDependers(boolean include) {
		includeDependers = include;
		return this;
	}

	@Override
	public TranspositionContextBuilder includeParents(boolean include) {
		includeParents = include;
		return this;
	}
		
	@Override
	public TranspositionContextBuilder includeParentDependers(boolean include) {
		includeParentDependers = include;
		return this;
	}

	@Override
	public TranspositionContextBuilder includeImports(boolean include) {
		includeImports = include;
		return this;
	}

	@Override
	public TranspositionContextBuilder includeImportDependers(boolean include) {
		includeImportDependers = include;
		return this;
	}

	@Override
	public TranspositionContextBuilder includeParts(boolean include) {
		includeParts = include;
		return this;
	}
	
	

	@Override
	public TranspositionContextBuilder coalesceFilteredDependencies(boolean coalesce) {
		coalesceFiltered = coalesce;
		return this;
	}
	
	

	@Override
	public TranspositionContextBuilder detectProjects(boolean showProjects) {
		detectProjects = showProjects;
		return this;
	}

	@Override
	public TranspositionContext done() {
		TranspositionContext ct = TranspositionContext.T.create();
		
		ct.setShowDependencies(includeDependencies);
		ct.setShowDependers(includeDependers);
		
		ct.setShowParents( includeParents);
		ct.setShowParentDependers( includeParentDependers);
		
		ct.setShowImports( includeImports);
		ct.setShowImportDependers( includeImportDependers);
		
		
		ct.setShowParts(includeParts);
		ct.setCoalesce(coalesceFiltered);
		
		ct.setDetectProjects(detectProjects);
		return ct;
	}
	
	
	/**
	 * @return - finally build the {@link TranspositionContext}
	 */
	public static TranspositionContextBuilder build() {
		return new BasicTranspositionContext();
	}

}
