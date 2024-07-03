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
package com.braintribe.devrock.artifactcontainer.container.diagnostics;

import com.braintribe.devrock.artifactcontainer.container.ArtifactContainer;
import com.braintribe.devrock.artifactcontainer.control.walk.ArtifactContainerUpdateRequestType;
import com.braintribe.model.artifact.Solution;

/**
 * listener for the {@link ContainerDiagnostics} - 
 * 
 * @author Pit
 *
 */
public interface ContainerClasspathDiagnosticsListener {
	
	/**
	 * acknowledges start of classpath processing. 
	 * @param container - the {@link ArtifactContainer} that is processed 
	 * @param mode - the {@link ArtifactContainerUpdateRequestType} of the current process 
	 */
	void acknowledgeContainerProcessingStart( ArtifactContainer container, ArtifactContainerUpdateRequestType mode);
	void acknowledgeContainerProcessingEnd( ArtifactContainer container, ArtifactContainerUpdateRequestType mode);
	
	/**
	 * acknowledges that a pom-packaged solution which is also only referenced as pom dependency
	 * is not relevant for the classpath  - this is ok, as intended
	 * @param container - the {@link ArtifactContainer} that is processed 
	 * @param requestType - the {@link ArtifactContainerUpdateRequestType} of the current process
	 * @param solution - the {@link Solution} that is not relevant
	 */
	void acknowledgeSolutionPomPackagedAndReferencedAsPom( ArtifactContainer container, ArtifactContainerUpdateRequestType requestType, Solution solution);
	/**
	 * acknowledges that a pom-packaged solution which is at least once referenced as non-pom dependency
	 * is relevant for the classpath  - this is ok, but discouraged (sloppy actually), and needs to be reported as warning
	 * @param container - the {@link ArtifactContainer} that is processed 
	 * @param requestType - the {@link ArtifactContainerUpdateRequestType} of the current process 
	 * @param solution - the {@link Solution} that is relevant
	 */ 
	void acknowledgeSolutionPomPackagedAndReferencedAsJarSolution( ArtifactContainer container, ArtifactContainerUpdateRequestType requestType, Solution solution);
	
	/**
	 * acknowledge that a non-jar packaged solution which is at least once referenced as a classes dependency 
	 * @param container - the {@link ArtifactContainer} that is processed 
	 * @param requestType - the {@link ArtifactContainerUpdateRequestType} of the current process
	 * @param solution - the {@link Solution} that is relevant
	 */
	void acknowledgeSolutionNonJarPackagedAndReferencedAsClassesJarSolution( ArtifactContainer container, ArtifactContainerUpdateRequestType requestType, Solution solution);
	
	/**
	 * acknowledges that a jar-packaged solution which is only referenced as pom dependency
	 * is not relevant for the classpath - this is not ok, and needs to be reported as a problem 
	 * @param container - the {@link ArtifactContainer} that is processed 
	 * @param requestType - the {@link ArtifactContainerUpdateRequestType} of the current process
	 * @param solution - the {@link Solution} that is irrelevant 
	 */
	void acknowledgeSolutionJarPackagedAndReferencedAsPom( ArtifactContainer container, ArtifactContainerUpdateRequestType requestType, Solution solution);
	
}
