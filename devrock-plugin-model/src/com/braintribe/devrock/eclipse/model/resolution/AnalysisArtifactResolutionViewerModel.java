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

import java.util.List;

import com.braintribe.devrock.eclipse.model.resolution.nodes.Node;
import com.braintribe.model.artifact.analysis.AnalysisArtifact;
import com.braintribe.model.artifact.analysis.AnalysisArtifactResolution;
import com.braintribe.model.artifact.analysis.AnalysisDependency;
import com.braintribe.model.artifact.analysis.AnalysisTerminal;
import com.braintribe.model.artifact.compiled.CompiledTerminal;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;


/**
 * a model that contains a transcribed {@link AnalysisArtifactResolution}
 * @author pit
 *
 */
public interface AnalysisArtifactResolutionViewerModel extends GenericEntity {
	
	String terminals = "terminals";
	
	EntityType<AnalysisArtifactResolutionViewerModel> T = EntityTypes.T(AnalysisArtifactResolutionViewerModel.class);

	/**
	 * @return - a list of the  {@link CompiledTerminal} / {@link AnalysisTerminal} as {@link AnalysisNode}
	 */
	List<Node> getTerminals();
	void setTerminals(List<Node>  Terminals);
	
	/**
	 * @return - a list of the {@link AnalysisArtifact} in the 'solutions' list of the {@link AnalysisArtifactResolution}, as {@link Node}
	 */
	List<Node> getSolutions();
	void setSolutions(List<Node> solutions);
	
	/**
	 * @return - a {@link List} of ALL involved {@link AnalysisArtifact}, inclusive parents/imports,  as {@link Node}
	 */
	List<Node> getPopulation();
	void setPopulation(List<Node> value);

	/**
	 * @return - a {@link List} of clashes, as {@link Node}
	 */
	List<Node> getClashes();
	void setClashes(List<Node> value);
	
	/**
	 * @return - a {@link List} of the {@link AnalysisArtifact} if the 'incomplete artifacts' list of the {@link AnalysisArtifactResolution}
	 */
	List<Node> getIncompleteArtifacts();
	void setIncompleteArtifacts(List<Node> value);
	
	/**
	 * @return - a {@link List} of the {@link AnalysisDependency} if the 'unresolved dependencies' list of the {@link AnalysisArtifactResolution}
	 */
	List<Node> getUnresolvedDependencies();
	void setUnresolvedDependencies(List<Node> value);
	
	/**
	 * @return - a {@link List} of the {@link AnalysisDependency} if the 'filtered artifacts' list of the {@link AnalysisArtifactResolution}
	 */
	List<Node> getFilteredDependencies();
	void setFilteredDependencies(List<Node> value);
	
	
	/**
	 * @return
	 */
	List<Node> getParents();
	void setParents(List<Node> value);

		
}


