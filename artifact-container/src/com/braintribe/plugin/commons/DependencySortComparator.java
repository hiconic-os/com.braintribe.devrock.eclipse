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
package com.braintribe.plugin.commons;

import java.util.Comparator;

import com.braintribe.model.artifact.Dependency;
import com.braintribe.model.artifact.processing.version.VersionRangeProcessor;

public class DependencySortComparator implements Comparator<Dependency> {

	@Override
	public int compare(Dependency d1, Dependency d2) {
		int r = d1.getArtifactId().compareToIgnoreCase( d2.getArtifactId());
		if (r == 0) {
			r = d1.getGroupId().compareToIgnoreCase(d2.getGroupId());
			if (r == 0) {				
				r = VersionRangeProcessor.toString( d1.getVersionRange()).compareTo( VersionRangeProcessor.toString( d2.getVersionRange()));				
			}
		}
		return r;
	}


}
