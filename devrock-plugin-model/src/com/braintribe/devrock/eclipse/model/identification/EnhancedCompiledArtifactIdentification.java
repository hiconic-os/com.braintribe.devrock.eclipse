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

import com.braintribe.model.artifact.compiled.CompiledArtifact;
import com.braintribe.model.artifact.compiled.CompiledArtifactIdentification;
import com.braintribe.model.artifact.essential.VersionedArtifactIdentification;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.version.Version;

/**
 * an extended {@link CompiledArtifactIdentification} which also includes the 'archetype' and the 
 * 'origin' folder. Used by the devrock-plugin.
 * @author pit
 *
 */
public interface EnhancedCompiledArtifactIdentification extends CompiledArtifactIdentification, HasArchetype {
	
	EntityType<EnhancedCompiledArtifactIdentification> T = EntityTypes.T(EnhancedCompiledArtifactIdentification.class);
		
	String origin = "origin";
	
	/**
	 * @return - path to the directory of where the .project was (and where the pom.xml should be)
	 */
	String getOrigin();
	void setOrigin(String directory);

	
	/**
	 * creates a new {@link EnhancedCompiledArtifactIdentification}
	 * @param groupId  - the group id
	 * @param artifactId - the artifact id
	 * @param version - the {@link Version} 
	 * @param archetype - the archetype 
	 * @return - a new {@link EnhancedCompiledArtifactIdentification}
	 */
	static EnhancedCompiledArtifactIdentification create( String groupId, String artifactId, Version version, String archetype) {
		EnhancedCompiledArtifactIdentification ai = EnhancedCompiledArtifactIdentification.T.create();
		ai.setGroupId(groupId);
		ai.setArtifactId(artifactId);
		ai.setVersion( version);
		ai.setArchetype(archetype);		
		return ai;
	}
	
	static EnhancedCompiledArtifactIdentification from( CompiledArtifact cai) {
		EnhancedCompiledArtifactIdentification ai = EnhancedCompiledArtifactIdentification.T.create();
		ai.setGroupId( cai.getGroupId());
		ai.setArtifactId( cai.getArtifactId());
		ai.setVersion( cai.getVersion());
		String archetype = cai.getArchetype();
		if (archetype == null) {
			archetype = cai.getProperties().get("archetype");
		}
		ai.setArchetype( archetype);		
		return ai;
	}
	/**
	 * @return - a new {@link VersionedArtifactIdentification} parametrized by this {@link EnhancedCompiledArtifactIdentification}
	 */
	default VersionedArtifactIdentification asVersionedArtifactIdentification() {
		return VersionedArtifactIdentification.create( this.getGroupId(), this.getArtifactId(), this.getVersion().asString());
	}
	
}
