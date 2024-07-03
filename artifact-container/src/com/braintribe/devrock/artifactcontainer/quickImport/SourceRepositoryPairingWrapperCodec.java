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
package com.braintribe.devrock.artifactcontainer.quickImport;

import com.braintribe.cc.lcd.HashSupportWrapperCodec;
import com.braintribe.model.malaclypse.cfg.preferences.svn.SourceRepositoryPairing;

public class SourceRepositoryPairingWrapperCodec extends HashSupportWrapperCodec<SourceRepositoryPairing> {
	
	public SourceRepositoryPairingWrapperCodec() {
		super(true);
	}

	@Override
	protected int entityHashCode(SourceRepositoryPairing e) {
		return e.getName().hashCode();
	}

	@Override
	protected boolean entityEquals(SourceRepositoryPairing e1, SourceRepositoryPairing e2) {
		return e1.getName().equalsIgnoreCase(e2.getName());
	}	
}
