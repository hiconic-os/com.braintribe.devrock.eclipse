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
package com.braintribe.devrock.api.ui.viewers.artifacts.transpose.context;

import com.braintribe.devrock.eclipse.model.storage.ViewerContext;

public interface ViewContextBuilder {
	
	/**
	 * @param shortRanges - true to show ranges in 'js-style' notation rather than 'maven-style'
	 * @return - itself
	 */
	ViewContextBuilder shortRanges( boolean shortRanges);
	/**
	 * @param showGroups - true to show at least one groupid per node
	 * @return
	 */
	ViewContextBuilder showGroups( boolean showGroups);
	/**
	 * @param showDependencies - true to show dependency information or to concentrate on artifacts only
	 * @return - itself
	 */
	ViewContextBuilder showDependencies( boolean showDependencies);

	/**
	 * @param showNatures - true to show artifact natures (as images for artifact/project/parent)
	 * @return - itself
	 */
	ViewContextBuilder showNatures( boolean showNatures);
	
	/**
	 * @return - close and produce a {@link ViewerContext}
	 */
	
	ViewerContext done();
}
