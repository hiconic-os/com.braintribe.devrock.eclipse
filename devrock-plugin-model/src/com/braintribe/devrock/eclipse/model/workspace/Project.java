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
package com.braintribe.devrock.eclipse.model.workspace;

import java.util.List;

import com.braintribe.devrock.eclipse.model.identification.EnhancedCompiledArtifactIdentification;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * a GE representing a IProject of Eclipse
 * @author pit
 *
 */
public interface Project extends GenericEntity{
	
	final EntityType<Project> T = EntityTypes.T(Project.class);

	String projectName = "projectName";
	String natures= "natures";
	String identification = "identification";
	
	String getProjectName();
	void setProjectName(String  projectName);
	
	EnhancedCompiledArtifactIdentification getIdentification();
	void setIdentification(EnhancedCompiledArtifactIdentification  ecai);

	List<String> getNatures();
	void setNatures(List<String>  name);
	
}
