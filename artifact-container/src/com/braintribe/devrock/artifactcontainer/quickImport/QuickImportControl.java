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

import java.io.File;
import java.util.List;

import com.braintribe.devrock.artifactcontainer.quickImport.notification.QuickImportScanResultBroadcaster;
import com.braintribe.model.malaclypse.cfg.preferences.svn.SourceRepositoryPairing;
import com.braintribe.model.panther.SourceArtifact;

public interface QuickImportControl extends QuickImportScanResultBroadcaster{

	boolean isScanActive();
	void rescan();
	void rescan( SourceRepositoryPairing parring);
	void stop();
	void setup();
	List<SourceArtifact> runCoarseSourceArtifactQuery( String expression);
	List<SourceArtifact> runSourceArtifactQuery( String expression);
	List<SourceArtifact> runPartialSourceArtifactQuery( String expression);
	List<SourceArtifact> runPomFileToSourceArtifactQuery( File pomFile);
}
