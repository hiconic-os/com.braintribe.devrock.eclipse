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
package com.braintribe.devrock.bridge.eclipse.api;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.braintribe.common.lcd.Pair;
import com.braintribe.devrock.bridge.eclipse.environment.BasicStorageLocker;
import com.braintribe.devrock.eclipse.model.scan.SourceRepositoryEntry;
import com.braintribe.ve.api.VirtualEnvironment;
import com.braintribe.ve.impl.OverridingEnvironment;

public interface EnvironmentBridge {
	/**
	 * @return - the folder where workspace-specific data should be stored
	 */
	File workspaceSpecificStorageLocation(); // passive, derived from dev-environment, remains workspace location
	
	/**
	 * @return - the currently configured map of 'archetype' to 'tag' - required by the dependency injectors 
	 */
	Map<String,String> archetypeToTagMap();  // passive, derived from dev-environment (or even hard-coded)
	
	
	/**
	 * @return - the current data container for data that should be persisted 
	 */
	BasicStorageLocker storageLocker(); // active
	
	/**
	 * @return - the root {@link File} of the dev-environment 
	 */
	Optional<File> getDevEnvironmentRoot(); // passive, derived from dev-envionment
	
	
	
	/**
	 * @return - the root of the scan directories (the'git')
	 */
	Optional<List<Pair<String, File>>> getDevEnvScanRoots(); // passive, derived from dev-environment
	
	
	Optional<File> getDevEnvBuildRoot(); // active, managed by user
		
	/**
	 * @return - the roots of the scan directories as added by the user 
	 */
	Optional<List<File>> getWorkspaceScanDirectories(); // active, managed by user

	
	List<SourceRepositoryEntry> getScanRepositories(); // combination of getDevEnvScanDirectories & getWorkspaceScanDirectories
	
	List<File> getScanDirectories(); // combination of getDevEnvScanDirectories & getWorkspaceScanDirectories
	
		
	/**
	 * @return - the devenv's configuration
	 */
	Optional<File> getRepositoryConfiguration(); // passive, derived from dev-envionment
	
	/**
	 * @return - the local cache aka the 'local repository'
	 */
	Optional<File> getLocalCache(); // passive, derived from dev-envionment

	/**
	 * @return - the current {@link OverridingEnvironment}
	 */
	VirtualEnvironment virtualEnviroment(); // active, managed by user
	
	
	
	
}
