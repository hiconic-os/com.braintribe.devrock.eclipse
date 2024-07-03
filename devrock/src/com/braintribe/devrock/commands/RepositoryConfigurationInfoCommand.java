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
package com.braintribe.devrock.commands;

import java.util.Date;

import com.braintribe.devrock.mc.api.repository.configuration.RepositoryReflection;
import com.braintribe.devrock.model.repository.RepositoryConfiguration;
import com.braintribe.devrock.plugin.DevrockPlugin;
import com.braintribe.gm.model.reason.Maybe;


/**
 * retrieves the current repository configuration and displays it 
 * 
 * @author pit
 *
 */
public class RepositoryConfigurationInfoCommand extends AbstractRepositoryConfigurationViewCommand {

	@Override
	protected Maybe<Container> retrieveRepositoryMaybe() {
				
		long before = System.nanoTime();
		Maybe<RepositoryReflection> repositoryReflectionMaybe = DevrockPlugin.mcBridge().reflectRepositoryConfiguration();
		long after = System.nanoTime();
		double lastProcessingTime = (after - before) / 1E6;	
		if (repositoryReflectionMaybe.isSatisfied()) {
			RepositoryReflection reflection = repositoryReflectionMaybe.get();
			RepositoryConfiguration repositoryConfiguration = reflection.getRepositoryConfiguration();
			
			Container container = new Container();
			container.rfcg = repositoryConfiguration;
			container.processingTime = lastProcessingTime;
			container.timestamp = new Date();
			container.file = null; // no file 
			
			return Maybe.complete( container);
		}
		else {
			return repositoryReflectionMaybe.cast();
		}		
	}
}
