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
package com.braintribe.eclipse.model.nature.wire.pomreader.space;

import java.util.function.Function;

import com.braintribe.build.artifact.representations.artifact.maven.settings.OverrideableVirtualEnvironment;
import com.braintribe.build.artifact.representations.artifact.pom.ArtifactPomReader;
import com.braintribe.build.artifact.retrieval.multi.resolving.DependencyResolver;
import com.braintribe.build.artifacts.mc.wire.pomreader.contract.PomReaderContract;
import com.braintribe.build.artifacts.mc.wire.pomreader.external.contract.PomReaderExternalContract;
import com.braintribe.devrock.virtualenvironment.VirtualEnvironmentPlugin;
import com.braintribe.eclipse.model.nature.EclipseVirtualEnvironment;
import com.braintribe.eclipse.model.nature.WorkspaceAwareDependencyResolver;
import com.braintribe.ve.api.VirtualEnvironment;
import com.braintribe.wire.api.annotation.Bean;
import com.braintribe.wire.api.annotation.Beans;
import com.braintribe.wire.api.annotation.Import;

@Beans
public class PomReaderExternalSpace implements PomReaderExternalContract {

	@Import
	private PomReaderContract pomReader;
	
	@Override
	public Function<DependencyResolver, DependencyResolver> dependencyResolverEnricher() {
		return r -> workspaceAwareDependencyResolver();
	}
	
	@Bean
	private WorkspaceAwareDependencyResolver workspaceAwareDependencyResolver() {
		WorkspaceAwareDependencyResolver bean = new WorkspaceAwareDependencyResolver();
		
		bean.setPomReader(pomReader.pomReader());
		bean.setDelegate(pomReader.standardDependencyResolver());
		
		return bean;
	}
	
	@Bean
	private ArtifactPomReader leanPomReader() {
		ArtifactPomReader bean = new ArtifactPomReader();
		return bean;
	}


	@Override
	public VirtualEnvironment virtualEnvironment() {
		if (VirtualEnvironmentPlugin.getOverrideActivation()) {
			OverrideableVirtualEnvironment ove = new OverrideableVirtualEnvironment();
			ove.setEnvironmentOverrides( VirtualEnvironmentPlugin.getEnvironmentOverrides());
			ove.setPropertyOverrides( VirtualEnvironmentPlugin.getPropertyOverrides());
			return ove;
		}				
		return EclipseVirtualEnvironment.INSTANCE;
	}

}
