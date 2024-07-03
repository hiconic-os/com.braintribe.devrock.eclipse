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
package com.braintribe.devrock.bridge.eclipse.api;

import java.io.File;
import java.util.Collection;
import java.util.List;

import com.braintribe.common.potential.Potential;
import com.braintribe.devrock.eclipse.model.identification.RemoteCompiledDependencyIdentification;
import com.braintribe.devrock.mc.api.classpath.ClasspathResolutionScope;
import com.braintribe.devrock.mc.api.repository.configuration.RepositoryReflection;
import com.braintribe.devrock.model.repository.RepositoryConfiguration;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.Reason;
import com.braintribe.model.artifact.analysis.AnalysisArtifactResolution;
import com.braintribe.model.artifact.compiled.CompiledArtifact;
import com.braintribe.model.artifact.compiled.CompiledArtifactIdentification;
import com.braintribe.model.artifact.compiled.CompiledDependencyIdentification;
import com.braintribe.model.artifact.compiled.CompiledPartIdentification;
import com.braintribe.model.artifact.compiled.CompiledTerminal;

/**
 * devrock's API bridge, i.e. the plugins API passed to the other plugins
 * @author pit
 *
 */
public interface McBridge extends AutoCloseable {
	
	
	/**
	 * @param ct - the {@link CompiledTerminal}
	 * @param resolutionScope - the {@link ClasspathResolutionScope}
	 * @return - the resulting {@link AnalysisArtifactResolution}
	 */
	Maybe<AnalysisArtifactResolution> resolveClasspath( CompiledTerminal ct, ClasspathResolutionScope resolutionScope);
	
	/**
	 * @param cts - a {@link Collection} of {@link CompiledTerminal} as starting points
	 * @param resolutionScope - the {@link ClasspathResolutionScope}
	 * @return - the resulting {@link AnalysisArtifactResolution}
	 */
	Maybe<AnalysisArtifactResolution> resolveClasspath( Collection<CompiledTerminal> cts, ClasspathResolutionScope resolutionScope);
	
	/**
	 * @param pomFile - the pomfile to read
	 * @return - a {@link Potential} of the {@link CompiledArtifact} and a {@link Reason} non-null if failed
	 */
	Maybe<CompiledArtifact> readPomFile( File pomFile);
	
	/**
	 * @param cai - the {@link CompiledArtifactIdentification}
	 * @return - a {@link Potential} of the {@link CompiledArtifact} or a {@link Reason}  non-null if failed
	 */
	Maybe<CompiledArtifact> resolve( CompiledArtifactIdentification cai);
	
	/**
	 * resolve an artifact 
	 * @param cdi - the {@link CompiledDependencyIdentification}
	 * @return - a {@link Potential} of the {@link CompiledArtifactIdentification}
	 */
	Maybe<CompiledArtifactIdentification> resolve( CompiledDependencyIdentification cdi);
	
	
	/**
	 * resolve a specific part 
	 * @param cpi - the {@link CompiledPartIdentification}
	 * @return - a {@link Potential} of the {@link File}
	 */
	Maybe<File> resolve( CompiledPartIdentification cpi);
	
	
	/**
	 * @return - the {@link RepositoryReflection}
	 */
	Maybe<RepositoryReflection> reflectRepositoryConfiguration();

	
	/**
	 * will return a list of {@link CompiledArtifactIdentification}, derived from the versions available
	 * for a {@link CompiledDependencyIdentification}. The {@link CompiledArtifactIdentification} returned may not be
	 * available (it uses the versions from the repository indices)
	 * @param cdi - the {@link CompiledDependencyIdentification}
	 * @return - a list of all {@link CompiledArtifactIdentification} matching the {@link CompiledDependencyIdentification}
	 */
	List<CompiledArtifactIdentification> matchesFor(CompiledDependencyIdentification cdi);
	
	/**
	 * returns a NEW McBridge with the {@link RepositoryConfiguration} passed. It is not scoped by the Devrock plugin,
	 * so it will not refresh between the calls and you need to close it after use. 
	 * @param repositoryConfiguration - the {@link RepositoryConfiguration} to use for the {@link McBridge}
	 * @return - the custom {@link McBridge}
	 */
	McBridge customBridge( RepositoryConfiguration repositoryConfiguration);
	
	
	/**
	 * access the current repository-configuration and uses the RH functionality to 
	 * get the content of each repository that can reflect on its content
	 * @return - a {@link List} of {@link RemoteCompiledDependencyIdentification} 
	 */
	List<RemoteCompiledDependencyIdentification> retrieveCurrentRemoteArtifactPopulation();
		
	/**
	 * scans the currently configured local repository to extract all artifacts
	 * @return - a {@link List} of {@link RemoteCompiledDependencyIdentification}
	 */
	List<RemoteCompiledDependencyIdentification> retrieveCurrentLocalArtifactPopulation();
	
	@Override
	void close();

		
}
