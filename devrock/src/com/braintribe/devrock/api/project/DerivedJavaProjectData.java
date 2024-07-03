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
package com.braintribe.devrock.api.project;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IClasspathEntry;

/**
 * a simple triplet to reflect the data extracted from a {@link IClasspathEntry}
 * @author pit
 *
 */
public class DerivedJavaProjectData {
	// the project
	public IProject project;
	// the main output folder
	public File outputFolder;
	// the exported folders, ARB stuff et al
	public List<File> exportedFolders;
}
