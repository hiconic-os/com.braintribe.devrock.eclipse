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
package com.braintribe.devrock.api.ui.viewers.artifacts.transpose.transposer;

import java.util.Comparator;

import com.braintribe.devrock.eclipse.model.resolution.nodes.AnalysisNode;
import com.braintribe.devrock.eclipse.model.resolution.nodes.Node;
import com.braintribe.model.artifact.essential.VersionedArtifactIdentification;

public class NodeComparator implements Comparator<Node> {
	@Override
	public int compare(Node o1, Node o2) {
		if (o1 instanceof AnalysisNode && o2 instanceof AnalysisNode) {
			// compare on artifactId, make sure they're reachable
			
			// first node
			AnalysisNode a1 = (AnalysisNode) o1;										
			VersionedArtifactIdentification identification1 = a1.getSolutionIdentification();
			if (identification1 == null) {
				return 0;
			}
			String aa1 = identification1.getArtifactId();
			if (aa1 == null) {
				return 0;
			}
			// second node
			AnalysisNode a2 = (AnalysisNode) o2;
			VersionedArtifactIdentification identification2 = a2.getSolutionIdentification();
			if (identification2 == null) {
				return 0;
			}
			String aa2 = identification2.getArtifactId();
			if (aa2 == null) {
				return 0;
			}
			// compare artifactId 
			return aa1.compareTo(aa2);
		}
		return 0;
	}
}