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
package com.braintribe.devrock.eclipse.model.scan;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * simple GE for the scan repository data 
 * 
 * @author pit
 *
 */
public interface SourceRepositoryEntry extends GenericEntity {
	
	EntityType<SourceRepositoryEntry> T = EntityTypes.T(SourceRepositoryEntry.class);
	
	String path = "path";
	String key = "key";
	String type = "type";
	String editable = "editable";
	String actualFile = "actualFile";
	String symbolLink = "symbolLink";

	
	/**
	 * @return - full or partial (dev-env) string representation of the directory
	 */
	String getPath();
	void setPath(String value);
	
	/**
	 * @return - the key assigned
	 */
	String getKey();
	void setKey( String key);
	
	/**
	 * @return - {@link SourceRepositoryType}, either git or svn
	 */
	SourceRepositoryType getType();
	void setType(SourceRepositoryType value);
	
	
	/**
	 * @return - true if it can be edited
	 */
	boolean getEditable();
	void setEditable(boolean value);

	/**
	 * @return - the actual directory 
	 */
	String getActualFile();
	void setActualFile(String value);

	/**
	 * @return - whether the path is a symbolic link or a real file
	 */
	boolean getSymbolLink();
	void setSymbolLink(boolean value);
	
	
}
