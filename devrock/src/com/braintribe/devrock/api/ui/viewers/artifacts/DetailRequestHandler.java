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
package com.braintribe.devrock.api.ui.viewers.artifacts;

import java.util.List;

import com.braintribe.devrock.eclipse.model.resolution.nodes.AnalysisNode;
import com.braintribe.devrock.eclipse.model.resolution.nodes.Node;
import com.braintribe.model.artifact.compiled.CompiledDependencyIdentification;
import com.braintribe.model.artifact.essential.VersionedArtifactIdentification;

/**
 * interface exposed by the {@link TransposedAnalysisArtifactViewer}, so that the external users 
 * can receive / handle detail requests 
 * @author pit
 *
 */
public interface DetailRequestHandler {

	/**
	 * accepts a request to show the passed {@link Node} in a separate viewer tab 
	 * @param node - the {@link Node}
	 */
	void acknowledgeOpenDetailRequest( Node node);
	
	/**
	 * accepts a request to close the viewer's tab. Currently not sent by the 
	 * viewer, as its tab can/is made to be auto-closeable
	 * @param viewer - the {@link TransposedAnalysisArtifactViewer} sending the request
	 */
	void acknowledgeCloseDetailRequest( TransposedAnalysisArtifactViewer viewer);
	
	/**
	 * accepts a request to open the {@link Node}'s attached pom file in an editor (if any)
	 * @param node - the {@link Node} to retrieve the pom from 
	 */
	void acknowledgeOpenPomRequest( Node node);

	/**
	 * accepts a request to open the {@link Node}'s attached pom file in a tab (if any)  
	 * @param node - the {@link Node} to retrieve the pom from 
	 */
	void acknowledgeViewPomRequest(Node node);
	
	
	/**
	 * accepts a request to copy the dependency related to a {@link Node} to the clipboard
	 * @param node - the {@link Node} to retrieve the {@link CompiledDependencyIdentification} from 
	 */
	void acknowledgeCopyDependencyToClipboardRequest( Node node);
	
	
	/**
	 * accepts a request to dump the currently displayed resolution as YAML file to disk
	 */
	void acknowledgeResolutionDumpRequest();
	
	
	/**
	 * accepts a request to remove a pc-versioned artifact from the install-repository 
	 * @param node - the {@link AnalysisNode} who's artifact should be removed
	 */
	void acknowledgeRemovalFromPcRepositoryRequest( List<AnalysisNode> node);
	
	/**
	 * accepts a request to identify all 'removable pc-versioned artifacts of the current resolution from the install-repository 
	 */
	void acknowledgeRemovalFromPcRepositoryRequest();
	
	/**
	 * requests whether the artifact has been marked as 'obsolete', i.e. the resolution doesn't reflect the artifact anymore 
	 * @param vai - the {@link VersionedArtifactIdentification} that identifies the artifact
	 * @return - true if it's obsolete (and should be marked accordingly), false if it's still ok
	 */
	boolean acknowledgeObsoleteCheckRequest( VersionedArtifactIdentification vai);
			
}
