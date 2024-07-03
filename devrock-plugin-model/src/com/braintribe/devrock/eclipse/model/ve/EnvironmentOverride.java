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
package com.braintribe.devrock.eclipse.model.ve;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;


/**
 * a override of either an environment variable or a system property 
 * @author pit
 *
 */
public interface EnvironmentOverride extends GenericEntity {
	
	final EntityType<EnvironmentOverride> T = EntityTypes.T(EnvironmentOverride.class);
	
	String name = "name";
	String value = "value";
	String overrideNature = "overrideNature";
	String active = "active";

	/**
	 * @return - the name of the env-variable or system-property
	 */
	String getName();
	void setName( String name);
	
	/**
	 * @return - the value that should be returned 
	 */
	String getValue();
	void setValue( String value);
	
	/**
	 * @return - the {@link OverrideType}, either env-variable or system-property
	 */
	OverrideType getOverrideNature();
	void setOverrideNature( OverrideType overrideType);
	
	/**
	 * @return - whether the override is active or not
	 */
	boolean getActive();
	void setActive( boolean active);
}
