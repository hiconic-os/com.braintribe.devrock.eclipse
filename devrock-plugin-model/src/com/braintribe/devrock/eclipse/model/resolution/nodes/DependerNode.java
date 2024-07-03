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
package com.braintribe.devrock.eclipse.model.resolution.nodes;

import com.braintribe.model.artifact.analysis.AnalysisArtifact;
import com.braintribe.model.artifact.analysis.AnalysisDependency;
import com.braintribe.model.artifact.essential.VersionedArtifactIdentification;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface DependerNode extends Node {
	
	String dependerArtifact = "dependerArtifact";
	String backingArtifact = "backingArtifact";
	String dependency = "dependency";
	String backingDependency = "backingDependency";
	String isParentDepender = "isParentDepender";
	String isTerminal = "isTerminal";
	
	EntityType<DependerNode> T = EntityTypes.T(DependerNode.class);

	
	VersionedArtifactIdentification getDependerArtifact();
	void setDependerArtifact(VersionedArtifactIdentification value);
	
	AnalysisArtifact getBackingArtifact();
	void setBackingArtifact(AnalysisArtifact value);


	VersionedArtifactIdentification getDependency();
	void setDependency(VersionedArtifactIdentification value);
	
	AnalysisDependency getBackingDependency();
	void setBackingDependency(AnalysisDependency value);
 
	boolean getIsParentDepender();
	void setIsParentDepender(boolean value);

	boolean getIsTerminal();
	void setIsTerminal(boolean value);


}
