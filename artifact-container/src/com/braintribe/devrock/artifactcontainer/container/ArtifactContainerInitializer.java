// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.devrock.artifactcontainer.container;

import java.util.UUID;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import com.braintribe.devrock.artifactcontainer.ArtifactContainerPlugin;
import com.braintribe.devrock.artifactcontainer.control.container.ArtifactContainerRegistry;
import com.braintribe.model.malaclypse.cfg.container.ArtifactContainerConfiguration;
import com.braintribe.model.malaclypse.cfg.container.ArtifactKind;
import com.braintribe.plugin.commons.container.ContainerNatureExpert;

/**
 * @author pit
 *
 */
public class ArtifactContainerInitializer extends ClasspathContainerInitializer {

	@Override
	public void initialize(IPath iPath, IJavaProject iJavaProject) throws CoreException {	
		
		IProject project = iJavaProject.getProject();
		ArtifactContainerRegistry artifactContainerRegistry = ArtifactContainerPlugin.getArtifactContainerRegistry();
		ArtifactContainer container = artifactContainerRegistry.getContainerOfProject( project);
		if (container == null) {
			container = new ArtifactContainer( iPath, iJavaProject, UUID.randomUUID().toString());
			artifactContainerRegistry.addContainer( project, container);
			// see whether we have a pre configured configuration from Eclipse
			ArtifactContainerConfiguration configuration = artifactContainerRegistry.getPreconfiguredConfiguration(iJavaProject);
			if (configuration == null) {
				configuration = artifactContainerRegistry.getPersistedConfigurationOfProject( iJavaProject.getProject());
			}
			if (configuration != null) {
				container.setConfiguration(configuration);
			}
			
			// check if the project's tagged as a model via the nature, if so, set it's cfg to model
			if (ContainerNatureExpert.hasModelNature(project)) {
				container.getConfiguration().setArtifactKind( ArtifactKind.model);
			}
		}
		JavaCore.setClasspathContainer(iPath, new IJavaProject[] {iJavaProject}, new IClasspathContainer[] {container}, null);
	}

}
