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
package com.braintribe.devrock.eclipse.model.storage;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.annotation.Transient;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * simple container for the parameters of the transposition
 * @author pit
 *
 */
public interface TranspositionContext extends GenericEntity {
		
	EntityType<TranspositionContext> T = EntityTypes.T(TranspositionContext.class);
	
	String showDependencies = "showDependencies";
	String showDependers = "showDependers";
	String showParents = "showParents";
	String showParentDependers = "showParentDependers";
	String showImports = "showImports";
	String showImportDependers = "showImportDependers";
	String showParts = "showParts";
	String coalesce = "coalesce";
	String key = "key";
	String assignedKey = "assignedKey";
	String detectProjects = "detectProjects";

	/**
	 * @return - whether to transpose dependencies of artifacts
	 */
	boolean getShowDependencies();
	void setShowDependencies(boolean value);
	
	/**
	 * @return - whether to transpose dependers of artifacts / dependencies
	 */
	boolean getShowDependers();
	void setShowDependers(boolean value);
	
	/**
	 * @return - whether to show parents
	 */
	boolean getShowParents();
	void setShowParents(boolean value);
	
	/**
	 * @return - whether to show dependers of parents
	 */
	boolean getShowParentDependers();
	void setShowParentDependers(boolean value);
	
	/**
	 * @return - whether to show imports of parents
	 */
	boolean getShowImports();
	void setShowImports(boolean value);
	
	/**
	 * @return - whether to show dependers of imports (the parents)
	 */
	boolean getShowImportDependers();
	void setShowImportDependers(boolean value);

	
	/**
	 * @return - whether to show parts
	 */
	boolean getShowParts();
	void setShowParts(boolean value);
	
	/**
	 * @return - true if filtered dependencies should be coalesced
	 */
	boolean getCoalesce();
	void setCoalesce(boolean value);
	
	/**
	 * @return - the key as assigned/read 
	 */
	String getKey();
	void setKey(String value);

	@Transient
	String getAssignedKey();
	void setAssignedKey(String value);
	
	/**
	 * @return - true if projects should be shown
	 */
	boolean getDetectProjects();
	void setDetectProjects(boolean value);


	
}
