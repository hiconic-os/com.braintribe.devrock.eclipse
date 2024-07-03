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
package com.braintribe.devrock.greyface.process.retrieval;

import java.util.Set;

import com.braintribe.build.artifact.retrieval.multi.resolving.DependencyResolver;
import com.braintribe.build.artifact.retrieval.multi.resolving.ResolvingException;
import com.braintribe.build.artifact.retrieval.multi.resolving.listener.DependencyResolverNotificationListener;
import com.braintribe.model.artifact.Dependency;
import com.braintribe.model.artifact.Identification;
import com.braintribe.model.artifact.Part;
import com.braintribe.model.artifact.Solution;
import com.braintribe.model.artifact.version.Version;

public abstract class AbstractDependencyResolver implements DependencyResolver {

	@Override
	public void addListener(DependencyResolverNotificationListener arg0) {		
	}

	@Override
	public void removeListener(DependencyResolverNotificationListener arg0) {		
	}

	@Override
	public Set<Solution> resolveMatchingDependency(String arg0, Dependency arg1) throws ResolvingException {
		return null;
	}

	@Override
	public Part resolvePom(String contextId, Identification arg1, Version arg2) throws ResolvingException {
		return null;
	}

	@Override
	public Part resolvePomPart(String contextId, Part part) throws ResolvingException {	
		return null;
	}

		

	
}
