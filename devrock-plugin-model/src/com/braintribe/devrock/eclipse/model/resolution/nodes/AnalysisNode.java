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
 * common GE to be used in the transposed viewers 
 * @author pit
 *
 */
public interface AnalysisNode extends Node {
		
	EntityType<AnalysisNode> T = EntityTypes.T(AnalysisNode.class);
	
	String dependencyIdentification = "dependencyIdentification";
	String backingDependency = "backingDependency";
	String dependerIdentification = "dependerIdentification";
	String solutionIdentification = "solutionIdentification";
	String backingSolution = "backingSolution";
	String imports = "imports";
	String parentNode = "parentNode";	
	String relevantResourceOrigin = "relevantResourceOrigin";

	/**
	 * @return - the {@link VersionedArtifactIdentification} of the involved dependency (if any)
	 */
	VersionedArtifactIdentification getDependencyIdentification();
	void setDependencyIdentification(VersionedArtifactIdentification value);
	
	/**
	 * @return - the {@link AnalysisDependency} backing the node (if any)
	 */
	AnalysisDependency getBackingDependency();
	void setBackingDependency(AnalysisDependency value);

	/**
	 * @return - the {@link VersionedArtifactIdentification} of the depender 
	 */
	VersionedArtifactIdentification getDependerIdentification();
	void setDependerIdentification(VersionedArtifactIdentification value);
	
	/**
	 * @return - the {@link VersionedArtifactIdentification} of the solution (if any)
	 */
	VersionedArtifactIdentification getSolutionIdentification();
	void setSolutionIdentification(VersionedArtifactIdentification value);
	
	/**
	 * @return - the {@link AnalysisArtifact} backing the node (if any)
	 */
	AnalysisArtifact getBackingSolution();
	void setBackingSolution(AnalysisArtifact value);

	
	/**
	 * @return - imports
	 */
	AnalysisNode getImports();
	void setImports(AnalysisNode value);
 
	
	/**
	 * @return - the parent node 
	 */
	AnalysisNode getParentNode();
	void setParentNode(AnalysisNode value);
 

	/**
	 * @return - the name of repository of the relevants parts of this artifact are located (pom/jar)
	 */
	String getRelevantResourceOrigin();
	void setRelevantResourceOrigin(String value);

	
	/**
	 * @return - true if the node has been purged from the 'file system repo' and that it cannot be 
	 * purged again (it's a ghost anyway)
	 */
	boolean getIsPurged();
	void setIsPurged(boolean value);

	
	
}
