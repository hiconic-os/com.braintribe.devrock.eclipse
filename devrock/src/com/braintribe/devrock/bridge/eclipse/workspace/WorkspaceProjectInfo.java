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
package com.braintribe.devrock.bridge.eclipse.workspace;

import java.io.File;

import org.eclipse.core.resources.IProject;

import com.braintribe.model.artifact.compiled.CompiledArtifactIdentification;
import com.braintribe.model.artifact.essential.VersionedArtifactIdentification;

public interface WorkspaceProjectInfo {

	/**
	 * @return - the {@link IProject} of the project
	 */
	IProject getProject();

	/**
	 * @return - the project's artifact identification as a {@link CompiledArtifactIdentification}
	 */
	CompiledArtifactIdentification getCompiledArtifactIdentification();
	
	/**
	 * @return - the project's artifact identification as a {@link VersionedArtifactIdentification}
	 */
	VersionedArtifactIdentification getVersionedArtifactIdentification();

	/**
	 * @return - the main folder of the project, i.e. where the .project file lies
	 */
	File getProjectFolder();
	
	/**
	 * @return - the folder that the K_SOURCE package fragment points to 
	 */
	File getSourceFolder();
	
	/**
	 * @return - the folder that stands as output folder for the project
	 */
	File getBinariesFolder();

}
