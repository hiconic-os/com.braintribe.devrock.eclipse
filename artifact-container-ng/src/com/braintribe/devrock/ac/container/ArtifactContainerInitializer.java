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
package com.braintribe.devrock.ac.container;

import java.io.File;
import java.util.UUID;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import com.braintribe.devrock.ac.container.plugin.ArtifactContainerPlugin;
import com.braintribe.devrock.ac.container.plugin.ArtifactContainerStatus;
import com.braintribe.devrock.mc.core.declared.DeclaredArtifactIdentificationExtractor;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.logging.Logger;
import com.braintribe.model.artifact.compiled.CompiledArtifactIdentification;
import com.braintribe.model.artifact.essential.VersionedArtifactIdentification;

/**
 * initializer for the container - called when Eclipse is assigning the container
 * @author pit
 *
 */
//TODO : should a check for duplicates happen? Can there be duplicates?  
public class ArtifactContainerInitializer extends ClasspathContainerInitializer {
	private static Logger log = Logger.getLogger(ArtifactContainerInitializer.class);

	@Override
	public void initialize(IPath iPath, IJavaProject iJavaProject) throws CoreException {	
		
		ArtifactContainer container = new ArtifactContainer( iPath, iJavaProject, UUID.randomUUID().toString());
		
		IProject project = iJavaProject.getProject();
		File projectDir = project.getLocation().toFile();
		File pomFile = new File(projectDir, "pom.xml");
		if (!pomFile.exists()) {
			log.info("project [" + project.getName() + "] has no associated pom in [" + projectDir.getAbsolutePath() + "] and is not a candidate for a container");
			return;
		}
		Maybe<CompiledArtifactIdentification> extractedIdentificationPotential = DeclaredArtifactIdentificationExtractor.extractIdentification(pomFile);
		
	
		if (extractedIdentificationPotential.isUnsatisfied()) {
			ArtifactContainerStatus status = new ArtifactContainerStatus(" project [" + project.getName() + "] isn't a suitable candidate for a container : cannot read pom [" + pomFile.getAbsolutePath() + "] associated with project [" + project.getName() + "] as [" + extractedIdentificationPotential.whyUnsatisfied().stringify() + "]", IStatus.WARNING);
			ArtifactContainerPlugin.instance().log(status);
			return;
		}
		CompiledArtifactIdentification cai = extractedIdentificationPotential.get();							
		container.setVersionedArtifactIdentification( VersionedArtifactIdentification.create( cai.getGroupId(), cai.getArtifactId(), cai.getVersion().asString()));
		
	
		
		JavaCore.setClasspathContainer(iPath, new IJavaProject[] {iJavaProject}, new IClasspathContainer[] {container}, null);
				
	}

}
