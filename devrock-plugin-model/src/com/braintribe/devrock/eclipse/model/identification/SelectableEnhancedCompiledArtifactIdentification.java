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
package com.braintribe.devrock.eclipse.model.identification;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface SelectableEnhancedCompiledArtifactIdentification extends EnhancedCompiledArtifactIdentification {
	EntityType<SelectableEnhancedCompiledArtifactIdentification> T = EntityTypes.T(SelectableEnhancedCompiledArtifactIdentification.class);

	String existInWorkspace = "existInWorkspace";
	String existInCurrentWorkingSet = "existInCurrentWorkingSet";
	String canBeImportedWithCurrentChoices = "canBeImportedWithCurrentChoices";
	
	
	boolean getExistsInWorkspace();
	void setExistsInWorkspace(boolean value);

	boolean getExistsInCurrentWorkingSet();
	void setExistsInCurrentWorkingSet(boolean value);
	
	boolean getCanBeImportedWithCurrentChoices();
	void setCanBeImportedWithCurrentChoices(boolean value);



	static SelectableEnhancedCompiledArtifactIdentification from( EnhancedCompiledArtifactIdentification ecai) {
		SelectableEnhancedCompiledArtifactIdentification secai = SelectableEnhancedCompiledArtifactIdentification.T.create();
		secai.setGroupId( ecai.getGroupId());
		secai.setArtifactId( ecai.getArtifactId());
		secai.setVersion( ecai.getVersion());
		secai.setArchetype( ecai.getArchetype());
		secai.setOrigin( ecai.getOrigin());
		
		return secai;
	}
}
