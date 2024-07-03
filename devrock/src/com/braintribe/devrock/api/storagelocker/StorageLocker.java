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
package com.braintribe.devrock.api.storagelocker;

import java.util.Optional;

import com.braintribe.devrock.eclipse.model.storage.StorageLockerPayload;
import com.braintribe.devrock.plugin.DevrockPlugin;

/**
 * interface to the internal 'settings storage system' as implemented by the {@link DevrockPlugin} 
 * @author pit
 *
 */
public interface StorageLocker {

	
	/**
	 * @param <T> - the type of the slot's value
	 * @param slot - the name of the slot
	 * @return - an {@link Optional} of T, whatever that is  
	 */
	<T> Optional<T> getValue(String slot);
	
	
	/**
	 * @param <T> - the type of the slot's value
	 * @param slot - the name of the slot
	 * @param value - the default value to be returned if no such slot exists 
	 * @return - the T, whatever that is 
	 */
	<T> T getValue( String slot, T value);

	/**
	 * @param <T> - the type of the slot's value
	 * @param slot - the name of the slot
	 * @param value - an instance of T whatever that is  
	 */
	<T> void setValue(String slot, T value);

	
	/**
	 * @return - the full data currently stored
	 */
	StorageLockerPayload content();
}