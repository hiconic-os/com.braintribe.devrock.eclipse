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
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * represents a project that AC has taken instead of the actual dependency 
 * @author pit
 *
 */
public interface ProjectNode extends Node {
	
	EntityType<ProjectNode> T = EntityTypes.T(ProjectNode.class);
	
	String name = "name";
	String osLocation = "osLocation";
	String replacedSolution = "replacedSolution";

	/**
	 * @return - the name of the project
	 */
	String getName();
	void setName(String value);
	
	/**
	 * @return - the location of the project in the filesystem
	 */
	String getOsLocation();
	void setOsLocation(String value);

	
	/**
	 * @return - the {@link AnalysisArtifact} that the project stands for
	 */
	AnalysisArtifact getReplacedSolution();
	void setReplacedSolution(AnalysisArtifact value);

	

}
