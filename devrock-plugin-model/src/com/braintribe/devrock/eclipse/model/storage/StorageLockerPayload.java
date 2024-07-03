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

import java.util.Map;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * simple entity to store some stuff for the workspace 
 * NOTE that in slotData, even though Object is the type of the value, only GE compatible objects 
 * can be serialized.
 * 
 * @author pit
 *
 */
public interface StorageLockerPayload extends GenericEntity {
	
	EntityType<StorageLockerPayload> T = EntityTypes.T(StorageLockerPayload.class);
	
	String slotData = "slotData";
	
	/** 
	 * @return - a simple map of 'slot'-name to 'slot'-value
	 */
	Map<String,Object> getSlotData();
	void setSlotData(Map<String,Object> value);


	default boolean isEmtpy() {
		return getSlotData().size() == 0;
	}
	
}
