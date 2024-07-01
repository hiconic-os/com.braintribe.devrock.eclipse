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
package com.braintribe.devrock.greyface.process.notification;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.braintribe.model.artifact.Artifact;
import com.braintribe.model.artifact.Dependency;
import com.braintribe.model.artifact.Solution;
import com.braintribe.model.malaclypse.cfg.preferences.gf.RepositorySetting;

/**
 * listeners for the scan process must implement this 
 * @author pit
 *
 */
public interface ScanProcessListener {

	void acknowledgeStartScan();
	void acknowledgeStopScan();

	void acknowledgeScanAbortedAsArtifactIsPresentInTarget( RepositorySetting target, Solution artifact, Set<Artifact> parents);
	
	void acknowledgeScannedArtifact( RepositorySetting setting, Solution artifact, Set<Artifact> parents, boolean presentInTarget);
	void acknowledgeScannedParentArtifact( RepositorySetting setting, Solution artifact, Artifact child, boolean presentInTarget);	
	void acknowledgeScannedRootArtifact( RepositorySetting setting, Solution artifact, boolean presentInTarget);
	
	void acknowledgeUnresolvedArtifact(List<RepositorySetting> sources, Dependency dependency, Collection<Artifact> requestors);	
}
