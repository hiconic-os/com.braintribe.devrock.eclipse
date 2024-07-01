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
package com.braintribe.devrock.greyface.generics.commons;

import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.malaclypse.cfg.preferences.gf.RepositorySetting;
import com.braintribe.model.malaclypse.cfg.repository.RemoteRepository;
import com.braintribe.model.maven.settings.Server;

public class CommonConversions {

	
	public static Server serverFrom(RepositorySetting setting) {
		Server server = Server.T.create();
		server.setUsername( setting.getUser());
		server.setPassword( setting.getPassword());
		server.setId( setting.getName());
		return server;
	}

	/**
	 * upcasts a {@link RepositorySetting} from a {@link RemoteRepository}<br/>
	 * only works because {@link RepositorySetting} is a sub type of {@link RemoteRepository}
	 * @param remoteRepository - {@link RemoteRepository} as base 
	 * @return - the {@link RepositorySetting} returned, other values are default. 
	 */
	public static RepositorySetting repositorySettingFrom( RemoteRepository remoteRepository) {
		RepositorySetting setting = RepositorySetting.T.create();
		for (Property property : RemoteRepository.T.getDeclaredProperties()) {
			
			Property targetProperty = RepositorySetting.T.getProperty( property.getName());
			if (targetProperty != null) {
				targetProperty.set(setting, property.get(remoteRepository));
			}
		}
		return setting;
	}
	
	
}
