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

import com.braintribe.model.artifact.Solution;
import com.braintribe.model.artifact.processing.version.VersionProcessor;

public class SolutionSortComparator implements Comparator<Solution> {

	@Override
	public int compare(Solution s1, Solution s2) {
		int r = s1.getArtifactId().compareToIgnoreCase(s2.getArtifactId());
		if (r == 0) {
			r = s1.getGroupId().compareToIgnoreCase( s2.getGroupId());
			if (r == 0) {			
				r = VersionProcessor.toString(s1.getVersion()).compareTo( VersionProcessor.toString(s2.getVersion()));
			}
		}
		return r;
	}


}
