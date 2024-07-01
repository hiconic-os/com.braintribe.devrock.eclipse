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
package com.braintribe.devrock.artifactcontainer.quickImport;

import java.net.URL;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.braintribe.build.quickscan.agnostic.LocationAgnosticQuickImportScanner;
import com.braintribe.devrock.artifactcontainer.ArtifactContainerPlugin;
import com.braintribe.devrock.artifactcontainer.ArtifactContainerStatus;
import com.braintribe.devrock.artifactcontainer.quickImport.notification.QuickImportScanResultListener;
import com.braintribe.model.malaclypse.cfg.preferences.svn.SourceRepositoryPairing;
import com.braintribe.model.panther.SourceArtifact;
import com.braintribe.model.panther.SourceRepository;

public class MultipleRepositoryQuickImportScanner {
	private ArtifactContainerPlugin plugin = ArtifactContainerPlugin.getInstance();
	private boolean active;
	private QuickImportScanResultListener resultListener;
	
	public void setResultListener(QuickImportScanResultListener resultListener) {
		this.resultListener = resultListener;
	}
	
	public IStatus run(IProgressMonitor monitor) {
		
		LocationAgnosticQuickImportScanner scanner = new LocationAgnosticQuickImportScanner();
		QuickImportScanMonitorListener listener = null;
		listener = new QuickImportScanMonitorListener(monitor);
		scanner.addListener(listener);
		scanner.setScanAbortSignaller(listener);
		List<SourceRepositoryPairing> sourceRepositoriesToScan = plugin.getArtifactContainerPreferences(false).getSvnPreferences().getSourceRepositoryPairings();
		
		for (SourceRepositoryPairing sourceRepositoryPairing : sourceRepositoriesToScan) {
			try {
				listener.setPairing( sourceRepositoryPairing);
				SourceRepository sourceRepository = sourceRepositoryPairing.getLocalRepresentation();
				scanner.setSourceRepository( sourceRepository);
				URL url = new URL( sourceRepository.getRepoUrl());			
				active = true;
				List<SourceArtifact> result  = scanner.scanLocalWorkingCopy( url.getFile());
				// only store if scan wasn't aborted 
				
				if ( listener != null && !listener.abortScan()) {
					resultListener.acknowledgeScanResult( sourceRepositoryPairing, result);
				}
				else {
					// abort's been signaled, so don't scan anything else
					break;
				}
			} catch (Exception e) {
				String msg="cannot scan source repository [" + sourceRepositoryPairing.getName() + "]'s working copy, [" + e.getMessage() + "]";
				ArtifactContainerStatus status = new ArtifactContainerStatus( msg, e);
				plugin.log(status);	
				return status;
			}
		}					
		active = false;
		monitor.done();
		return Status.OK_STATUS;
	}
	
	
}
