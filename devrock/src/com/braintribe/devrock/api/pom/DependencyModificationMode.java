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
package com.braintribe.devrock.api.pom;

import com.braintribe.devrock.eclipse.model.actions.VersionModificationAction;

public enum DependencyModificationMode {
	delete,
	insert_untouched, // take the version expression as the selected source has 
	insert_rangified, // rangify the version expression using the standard logic : lowerbounds = major.minor, upperbounds = major.minor+1
	insert_referenced; // derive a variable-name from the groupId of the selected source and inject that
	
	
	public static DependencyModificationMode from( VersionModificationAction action) {
		switch (action) {
		case rangified:
			return insert_rangified;				
		case untouched:
			return insert_untouched;			
		case referenced:
		default:
			return insert_referenced;			
		}
	}
	
	
}
