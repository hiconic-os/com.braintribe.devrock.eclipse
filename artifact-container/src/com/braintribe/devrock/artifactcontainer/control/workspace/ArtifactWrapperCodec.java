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
package com.braintribe.devrock.artifactcontainer.control.workspace;

import com.braintribe.build.artifact.name.NameParser;
import com.braintribe.cc.lcd.HashSupportWrapperCodec;
import com.braintribe.model.artifact.Artifact;
import com.braintribe.model.artifact.processing.version.VersionProcessor;

public class ArtifactWrapperCodec extends HashSupportWrapperCodec<Artifact> {
	public ArtifactWrapperCodec() {
		super( true);
	}

	@Override
	protected int entityHashCode(Artifact e) {
		return NameParser.buildName(e).hashCode();
	}

	@Override
	protected boolean entityEquals(Artifact e1, Artifact e2) {
		
		if (!e1.getGroupId().equalsIgnoreCase(e2.getGroupId()))
			return false;
		if (!e1.getArtifactId().equalsIgnoreCase(e2.getArtifactId()))
			return false;
		
		if (!VersionProcessor.matches(e1.getVersion(), e2.getVersion()))
			return false;
		
		return true;
	}
}
