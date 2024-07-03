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
package com.braintribe.devrock.importer.scanner;

import com.braintribe.cc.lcd.HashingComparator;
import com.braintribe.devrock.eclipse.model.identification.EnhancedCompiledArtifactIdentification;
import com.braintribe.model.artifact.compiled.CompiledArtifactIdentification;
import com.braintribe.model.artifact.essential.ArtifactIdentification;
import com.braintribe.model.processing.core.commons.EntityHashingComparator;
import com.braintribe.model.version.Version;

public interface HashComparators {
	/**
	 * a {@link EntityHashingComparator} for {@link EnhancedCompiledArtifactIdentification} that <b>INCLUDES</b> the archetype and the origin 
	 */
	static final HashingComparator<EnhancedCompiledArtifactIdentification> enhancedCompiledArtifactIdentification = EntityHashingComparator
			.build( EnhancedCompiledArtifactIdentification.T)
			.addField( ArtifactIdentification.groupId)
			.addField( ArtifactIdentification.artifactId)
			.addPropertyPathField( CompiledArtifactIdentification.version, Version.major)
			.addPropertyPathField( CompiledArtifactIdentification.version, Version.minor)
			.addPropertyPathField( CompiledArtifactIdentification.version, Version.revision)
			.addPropertyPathField( CompiledArtifactIdentification.version, Version.qualifier)
			.addPropertyPathField( CompiledArtifactIdentification.version, Version.buildNumber)
			.addPropertyPathField( CompiledArtifactIdentification.version, Version.nonConform)
			.addField( EnhancedCompiledArtifactIdentification.archetype)
			.addField( EnhancedCompiledArtifactIdentification.origin)
			.done();
}
