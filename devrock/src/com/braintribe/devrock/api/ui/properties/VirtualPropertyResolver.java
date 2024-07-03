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
package com.braintribe.devrock.api.ui.properties;

import com.braintribe.devrock.api.ui.editors.DirectoryEditor;

/**
 * a specialized interface for virtual env support for the editors,
 * notably only the {@link DirectoryEditor} for now
 * 
 * @author pit
 *
 */
public interface VirtualPropertyResolver {
	/**
	 * @param key
	 * @return
	 */
	String getSystemProperty(String key);
	/**
	 * @param key
	 * @return
	 */
	String getEnvironmentProperty( String key);
	
	/**
	 * parses an expression and resolves all variables within it 
	 * @param expression - the expression to parse
	 * @return - the parsed result
	 */
	String resolve(String expression);
}
