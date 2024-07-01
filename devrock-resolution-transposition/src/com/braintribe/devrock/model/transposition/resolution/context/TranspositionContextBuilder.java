// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.devrock.model.transposition.resolution.context;

import com.braintribe.devrock.eclipse.model.storage.TranspositionContext;

/**
 * builder interface to build a {@link TranspositionContext}
 * @author pit
 *
 */
public interface TranspositionContextBuilder {
	/**
	 * whether to include dependencies 
	 * @param include - true if to include 
	 * @return - the {@link TranspositionContextBuilder}
	 */
	TranspositionContextBuilder includeDependencies( boolean include);
	/**
	 * whether to include dependers 
	 * @param include - true if to include 
	 * @return - the {@link TranspositionContextBuilder}
	 */
	TranspositionContextBuilder includeDependers( boolean include);
	
	/**
	 * whether to include parents 
	 * @param include - true if to include 
	 * @return - the {@link TranspositionContextBuilder}
	 */
	TranspositionContextBuilder includeParents( boolean include);
	
	/**
	 * whether to include dependers of parents
	 * @param include - true if to include 
	 * @return - the {@link TranspositionContextBuilder}
	 */
	TranspositionContextBuilder includeParentDependers( boolean include);
	
	/**
	 * whether to include imports 
	 * @param include - true if to include 
	 * @return - the {@link TranspositionContextBuilder}
	 */
	TranspositionContextBuilder includeImports( boolean include);
	
	/**
	 * whether to include dependers of imports
	 * @param include - true if to include 
	 * @return - the {@link TranspositionContextBuilder}
	 */
	TranspositionContextBuilder includeImportDependers( boolean include);
	
	
	/**
	 * whether to include parts 
	 * @param include - true if to include 
	 * @return - the {@link TranspositionContextBuilder}
	 */
	TranspositionContextBuilder includeParts( boolean include);
	/**
	 * whether to coalesce filtered dependencies  
	 * @param include - true if to include 
	 * @return - the {@link TranspositionContextBuilder}
	 */
	TranspositionContextBuilder coalesceFilteredDependencies( boolean coalesce);
	
	/**
	 * @return - the resulting {@link TranspositionContext}
	 */
	TranspositionContext done();
}
