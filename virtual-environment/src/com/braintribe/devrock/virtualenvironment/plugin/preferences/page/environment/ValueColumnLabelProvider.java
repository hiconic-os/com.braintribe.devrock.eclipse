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
package com.braintribe.devrock.virtualenvironment.plugin.preferences.page.environment;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.braintribe.model.malaclypse.cfg.preferences.ve.EnvironmentOverride;

public class ValueColumnLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		EnvironmentOverride override = (EnvironmentOverride) element;
		switch ( override.getOverrideNature()) {
			case environment:
				return System.getenv( override.getName());
			case property:
				return System.getProperty( override.getName());
			default:
				return null;		
		}	
	}

	@Override
	public String getToolTipText(Object element) {
		EnvironmentOverride override = (EnvironmentOverride) element;
		switch ( override.getOverrideNature()) {
			case environment:
				return "current system value for environment variable [" + override.getName() + "] is [" + System.getenv( override.getName()) + "]";
			case property:
				return "current system value for system property [" + override.getName() + "] is [" + System.getenv( override.getName()) + "]";
			default:
				return null;		
		}
	}

}
