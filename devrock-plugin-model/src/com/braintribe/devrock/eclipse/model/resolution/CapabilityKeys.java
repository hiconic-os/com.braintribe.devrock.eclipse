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
package com.braintribe.devrock.eclipse.model.resolution;

import com.braintribe.model.generic.base.EnumBase;
import com.braintribe.model.generic.reflection.EnumType;
import com.braintribe.model.generic.reflection.EnumTypes;

public enum CapabilityKeys implements EnumBase {
	identifyProjects, // identify projects and show data 
	saveResolution, // adds a 'save resolution button' to tab
	shortNotation,  // show 'js-style' short notations for ranges 
	visibleGroups, // show groups
	visibleArtifactNature, // show artifact nature as icons
	visibleDependencies, // show dependencies as structural element (hide dep-info in node)
	parents, parentDependers, // show parents and their dependers 
	imports, importDependers, // show imports and their imports and users 
	dependencies, dependers,  // show dependencies (top down) and dependers (bottom up)
	filter, // allow filtering the contents
	search,  // add search feature (currently ignored by the viewer)
	detail, // allow double-clicking to create a new tab 
	parts, // show parts of the artifact
	coalesce, // combine duplicates
	purge, // purge pc-versions
	open, // open pom files
	copy; // copy VAI to clipboard
		
	final EnumType T = EnumTypes.T(CapabilityKeys.class);
	
	@Override
	public EnumType type() {
		return T;
	}
}
