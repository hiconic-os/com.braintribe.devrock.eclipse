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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubMonitor;

import com.braintribe.build.process.ProcessAbortSignaller;
import com.braintribe.build.quickscan.notification.QuickImportScanPhaseListener;
import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.devrock.artifactcontainer.ArtifactContainerPlugin;
import com.braintribe.devrock.artifactcontainer.ArtifactContainerStatus;
import com.braintribe.model.malaclypse.cfg.preferences.svn.SourceRepositoryPairing;
import com.braintribe.model.panther.SourceArtifact;

public class QuickImportScanMonitorListener implements QuickImportScanPhaseListener, ProcessAbortSignaller{	
	private SubMonitor monitor;
	private IProgressMonitor progressMonitor;
	private ProcessAbortSignaller abortSignaller;
	private SourceRepositoryPairing pairing;
	
	@Configurable
	public void setAbortSignaller(ProcessAbortSignaller abortSignaller) {
		this.abortSignaller = abortSignaller;
	}
	
	@Configurable @Required
	public void setPairing(SourceRepositoryPairing pairing) {
		this.pairing = pairing;
	}
	
	public QuickImportScanMonitorListener(IProgressMonitor progressMonitor) {
		this.progressMonitor = progressMonitor;		
	}
	
	@Override
	public void acknowledgeDetected(String arg0) {
		if (abortScan())
			return;
		
		monitor.subTask( "source artifact [" + arg0.toString() + "] detected in repository [" + pairing.getName() + "]");
		monitor.split(1);
	}

	@Override
	public void acknowledgeEnumerationPhase() {
		
		int expectedNumberOnSources = pairing.getNumberOfSourcesFound();
		if (expectedNumberOnSources > 0) {
			monitor = SubMonitor.convert(progressMonitor, expectedNumberOnSources);
		}
		else {
			monitor = SubMonitor.convert(progressMonitor);
		}
		monitor.setTaskName("Scanning ");	
			
	}

	@Override
	public void acknowledgeResolved(SourceArtifact sourceArtifact) {	
		if (abortScan())
			return;
		
		monitor.subTask( "resolved [" + sourceArtifact.getGroupId() + ":" + sourceArtifact.getArtifactId() + "#" + sourceArtifact.getVersion() + "]");
		monitor.split(1);
	}

	@Override
	public void acknowledgeScanError(String msg, String path) {
		if (abortScan())
			return;
		ArtifactContainerStatus status = new ArtifactContainerStatus("scanner reports error : " + msg + " in ["+ path + "] in repository [" + pairing.getName() + "]", IStatus.WARNING);
		ArtifactContainerPlugin.getInstance().log(status);	
	}
	

	@Override
	public void acknowledgeUnresolved(int phases, String path) {
		if (abortScan())
			return;
		ArtifactContainerStatus status = new ArtifactContainerStatus("remaining unresolved after : " + phases + " phases : ["+ path + "] in repository [" + pairing.getName() + "]", IStatus.WARNING);
		ArtifactContainerPlugin.getInstance().log(status);	
		
	}

	@Override
	public void acknowledgeScanPhase(int numPhase, int remaining) {
		if (abortScan())
			return;
		monitor = SubMonitor.convert(progressMonitor);
	}


	@Override
	public boolean abortScan() {
		if (abortSignaller != null && abortSignaller.abortScan())
			return true;
		return progressMonitor.isCanceled();
	}

	
	
	
}
