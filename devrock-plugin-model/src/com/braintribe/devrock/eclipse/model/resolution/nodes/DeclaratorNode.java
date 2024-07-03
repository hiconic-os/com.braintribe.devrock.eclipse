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

/**
 * represents a 'declaration' relation of a {@link AnalysisDependency} and it's declaring {@link AnalysisArtifact},
 * used for the case of parent-inherited dependencies and dept-mgt'd (at least imported) dependencies
 *  
 * @author pit
 *
 */
public interface DeclaratorNode extends Node {
	
	EntityType<DeclaratorNode> T = EntityTypes.T(DeclaratorNode.class);
	
	String declaratorArtifact = "declaratorArtifact";
	String backingDeclaratorArtifact = "backingDeclaratorArtifact";
	String dependingDependency = "dependingDependency";
	String backingDependingDependency = "backingDependingDependency";

	/**
	 * @return the {@link VersionedArtifactIdentification} of the declaring artifact (parent or import)
	 */
	VersionedArtifactIdentification getDeclaratorArtifact();
	void setDeclaratorArtifact(VersionedArtifactIdentification value);
	
	/**
	 * @return - the declaring {@link AnalysisArtifact} 
	 */
	AnalysisArtifact getBackingDeclaratorArtifact();
	void setBackingDeclaratorArtifact(AnalysisArtifact value);


	/**
	 * @return - the {@link VersionedArtifactIdentification} of the depender, a dependency 
	 */
	VersionedArtifactIdentification getDependingDependency();
	void setDependingDependency(VersionedArtifactIdentification value);
	
	/**
	 * @return - the depender {@link AnalysisDependency} 
	 */
	AnalysisDependency getBackingDependingDependency();
	void setBackingDependingDependency(AnalysisDependency value);

}
