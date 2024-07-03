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

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import com.braintribe.build.artifact.name.NameParser;
import com.braintribe.build.artifact.representations.artifact.pom.PomReaderException;
import com.braintribe.build.artifact.retrieval.multi.resolving.ResolvingException;
import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.devrock.greyface.scope.GreyfaceScope;
import com.braintribe.model.artifact.Dependency;
import com.braintribe.model.artifact.Part;
import com.braintribe.model.artifact.Solution;
import com.braintribe.model.artifact.processing.artifact.ArtifactProcessor;
import com.braintribe.model.artifact.processing.part.PartTupleProcessor;

public class LocalSingleDirectoryDependencyResolver extends AbstractDependencyResolver {
	private String localDirectory;
	
	@Configurable @Required
	public void setLocalDirectory(String localDirectory) {
		this.localDirectory = localDirectory;
	}

	@Override
	public Part resolvePomPart(String contextId, Part part) throws ResolvingException {
		try {
			Part pomPart = ArtifactProcessor.createPartFromIdentification(part, part.getVersion(), PartTupleProcessor.createPomPartTuple());			
			File file = new File( localDirectory, NameParser.buildFileName(pomPart));
			if (file.exists()) {
				pomPart.setLocation(file.getAbsolutePath());
				return pomPart;
			}
			return null;
		} catch (Exception e) {
			String msg="cannot resolve local pom file";
			throw new ResolvingException( msg, e);
		}
	}

	@Override
	public Set<Solution> resolveTopDependency(String contextId, Dependency dependency) throws ResolvingException {
		Set<Solution> result = new HashSet<Solution>();
		File scanDirectory = new File( localDirectory);
		String prefix = dependency.getArtifactId() + "-";
		String suffix = ".pom";
		for (File file : scanDirectory.listFiles()) {
			String name = file.getName();
			if (name.startsWith( prefix) && name.endsWith(suffix)) {
				Solution solution = Solution.T.create();
				ArtifactProcessor.transferIdentification(solution, dependency);
				result.add( solution);
				// version?? 
				Solution readSolution;
				try {
					readSolution = GreyfaceScope.getScope().getPomReader().readPom(contextId, file);
				} catch (PomReaderException e) {
					throw new ResolvingException("cannot read pom file [" + file.getAbsolutePath() + "]", e);
				}
				
				Part part = Part.T.create();
				ArtifactProcessor.transferIdentification(part, readSolution);
				part.setVersion( readSolution.getVersion());
				part.setType( PartTupleProcessor.createPomPartTuple());
				part.setLocation( file.getAbsolutePath());
				solution.getParts().add(part);
			}
		}
		return result;
		
	}
	
	

}
