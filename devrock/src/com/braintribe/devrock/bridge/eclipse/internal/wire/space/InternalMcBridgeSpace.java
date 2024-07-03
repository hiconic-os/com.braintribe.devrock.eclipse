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
package com.braintribe.devrock.bridge.eclipse.internal.wire.space;

import com.braintribe.devrock.bridge.eclipse.internal.wire.contract.InternalMcBridgeContract;
import com.braintribe.devrock.mc.api.classpath.ClasspathDependencyResolver;
import com.braintribe.devrock.mc.api.repository.configuration.ArtifactChangesSynchronization;
import com.braintribe.devrock.mc.api.repository.configuration.RepositoryReflection;
import com.braintribe.devrock.mc.api.resolver.ArtifactResolver;
import com.braintribe.devrock.mc.api.resolver.CompiledArtifactResolver;
import com.braintribe.devrock.mc.api.resolver.DeclaredArtifactCompiler;
import com.braintribe.devrock.mc.api.resolver.DependencyResolver;
import com.braintribe.devrock.mc.core.wirings.classpath.contract.ClasspathResolverContract;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

@Managed
public class InternalMcBridgeSpace implements InternalMcBridgeContract {
	
	@Import
	ClasspathResolverContract classpathResolver;
	
	@Override
	public ClasspathDependencyResolver classpathResolver() {
		return classpathResolver.classpathResolver();
	}

	@Override
	public DependencyResolver dependencyResolver() {
		return classpathResolver.transitiveResolverContract().dataResolverContract().dependencyResolver();
	}

	@Override
	public CompiledArtifactResolver compiledArtifactResolver() {
		return classpathResolver.transitiveResolverContract().dataResolverContract().redirectAwareCompiledArtifactResolver();
	}

	@Override
	public DeclaredArtifactCompiler declaredArtifactCompiler() {		
		return classpathResolver.transitiveResolverContract().dataResolverContract().declaredArtifactCompiler();
	}

	@Override
	public ArtifactResolver artifactResolver() {
		return classpathResolver.transitiveResolverContract().dataResolverContract().artifactResolver();
	}

	@Override
	public RepositoryReflection repositoryReflection() {	
		return classpathResolver.transitiveResolverContract().dataResolverContract().repositoryReflection();
	}
	
	@Override
	public ArtifactChangesSynchronization changesSynchronization() {
		return classpathResolver.transitiveResolverContract().dataResolverContract().changesSynchronization();
	}

}
