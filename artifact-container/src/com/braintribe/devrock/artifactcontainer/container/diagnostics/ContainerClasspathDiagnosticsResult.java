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

import java.util.Set;

import com.braintribe.build.artifact.retrieval.multi.coding.SolutionWrapperCodec;
import com.braintribe.cc.lcd.CodingSet;
import com.braintribe.model.artifact.Solution;

/**
 * a container for solutions that are special for classpath handling, i.e. pom aggregates or jars treated as aggregates are 
 * referenced in here.
 * 
 * @author Pit
 *
 */
public class ContainerClasspathDiagnosticsResult {

	private Set<Solution> pomAggregates = CodingSet.createHashSetBased( new SolutionWrapperCodec());
	private Set<Solution> pomAggregatesReferencedAsJars = CodingSet.createHashSetBased( new SolutionWrapperCodec());
	private Set<Solution> jarReferencedAsPomAggregates = CodingSet.createHashSetBased( new SolutionWrapperCodec());
	private Set<Solution> nonjarReferencedAsClassesJar = CodingSet.createHashSetBased( new SolutionWrapperCodec());
	
	public void addToPomAggregates( Solution solution) {
		pomAggregates.add(solution);
	}
	
	public void addToPomAggregatesReferencedAsJars( Solution solution) {
		pomAggregatesReferencedAsJars.add(solution);
	}
	
	public void addtoJarReferencedAsPomAggregates( Solution solution) {
		jarReferencedAsPomAggregates.add(solution);
	}
	
	public void addToNonJarReferencedAsClassesJar( Solution solution) {
		nonjarReferencedAsClassesJar.add( solution);
	}
	
	/**
	 * returns the {@link ClasspathDiagnosticsClassification} of the given {@link Solution} 
	 * @param solution - the {@link Solution} to check for 
	 * @return - the {@link ClasspathDiagnosticsClassification} for the solution
	 */
	public ClasspathDiagnosticsClassification getDiagnosticsClassificationOfSolution( Solution solution) {
		if (pomAggregates.contains(solution)) { 
			return ClasspathDiagnosticsClassification.pomAsPom;
		}
		if (pomAggregatesReferencedAsJars.contains(solution)) {
			return ClasspathDiagnosticsClassification.pomAsJar;
		}
		if (jarReferencedAsPomAggregates.contains(solution)) { 
			return ClasspathDiagnosticsClassification.jarAsPom;
		}
		if (nonjarReferencedAsClassesJar.contains(solution)) {
			return ClasspathDiagnosticsClassification.nonJarAsClassesJar;
		}
		return ClasspathDiagnosticsClassification.standard;
	}

	/**
	 * checks if anything "improper" is stored within the container's data<br/>
	 * if neither poms referenced as jars or jars referenced as poms, it will return {@link ClasspathDiagnosticsClassification#standard}  
	 * @return - the {@link ClasspathDiagnosticsClassification} overall 
	 */
	public ClasspathDiagnosticsClassification getDiagnosticsClassification() {
		if (jarReferencedAsPomAggregates.size()> 0) {
			return ClasspathDiagnosticsClassification.jarAsPom;
		}
		if (pomAggregatesReferencedAsJars.size() > 0) {
			return ClasspathDiagnosticsClassification.pomAsJar;
		}
		return ClasspathDiagnosticsClassification.standard;
	}
}
