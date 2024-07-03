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

import java.net.URL;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.braintribe.build.process.ProcessAbortSignaller;
import com.braintribe.build.quickscan.agnostic.LocationAgnosticQuickImportScanner;
import com.braintribe.devrock.artifactcontainer.ArtifactContainerPlugin;
import com.braintribe.devrock.artifactcontainer.ArtifactContainerStatus;
import com.braintribe.devrock.artifactcontainer.quickImport.notification.QuickImportScanResultListener;
import com.braintribe.model.malaclypse.cfg.preferences.svn.SourceRepositoryPairing;
import com.braintribe.model.panther.SourceArtifact;
import com.braintribe.model.panther.SourceRepository;

public class SingleRepositoryQuickImportScanRunner {
	private SourceRepositoryPairing sourceRepositoryPairing;
	private QuickImportScanResultListener resultListener;
	private ProcessAbortSignaller abortSignaller;
	boolean active;
	
	public void setSourceRepositoryPairing(SourceRepositoryPairing sourceRepositoryPairing) {
		this.sourceRepositoryPairing = sourceRepositoryPairing;
	}
	
	public void setResultListener(QuickImportScanResultListener resultListener) {
		this.resultListener = resultListener;
	}
	public void setAbortSignaller(ProcessAbortSignaller abortSignaller) {
		this.abortSignaller = abortSignaller;
	}



	public IStatus run(IProgressMonitor monitor) {					
		QuickImportScanMonitorListener monitorListener = new QuickImportScanMonitorListener(monitor);
		LocationAgnosticQuickImportScanner scanner = new LocationAgnosticQuickImportScanner();
		scanner.addListener(monitorListener);
		scanner.setScanAbortSignaller(abortSignaller);
		try {
			monitorListener.setPairing(sourceRepositoryPairing);
			SourceRepository sourceRepository = sourceRepositoryPairing.getLocalRepresentation();
			scanner.setSourceRepository( sourceRepository);
			
			URL url = new URL( sourceRepository.getRepoUrl());			
			active = true;
			
			List<SourceArtifact> result = scanner.scanLocalWorkingCopy( url.getFile());
			resultListener.acknowledgeScanResult( sourceRepositoryPairing, result);
		} catch (Exception e) {
			String msg="cannot scan source repository [" + sourceRepositoryPairing.getName() + "]'s working copy";
			ArtifactContainerStatus status = new ArtifactContainerStatus( msg, e);
			ArtifactContainerPlugin.getInstance().log(status);
			return status;	
		}							
		active = false;
		monitor.done();
		return Status.OK_STATUS;
	}
}
