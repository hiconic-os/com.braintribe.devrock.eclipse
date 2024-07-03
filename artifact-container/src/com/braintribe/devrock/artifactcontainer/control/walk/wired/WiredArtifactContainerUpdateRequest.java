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
package com.braintribe.devrock.artifactcontainer.control.walk.wired;

import com.braintribe.build.artifacts.mc.wire.classwalk.contract.ClasspathResolverContract;
import com.braintribe.devrock.artifactcontainer.container.ArtifactContainer;
import com.braintribe.devrock.artifactcontainer.control.walk.ArtifactContainerUpdateRequestType;
import com.braintribe.devrock.artifactcontainer.plugin.malaclypse.listener.MalaclypseAnalysisMonitor;

/**
 * an update request for the {@link ThreadedArtifactContainerWalkProcessor}
 * @author pit
 *
 */
public class WiredArtifactContainerUpdateRequest {
	private ArtifactContainer container;
	private ArtifactContainerUpdateRequestType walkMode;
	private ClasspathResolverContract classpathResolverContract;
	
	
	private boolean updateEclipse;
	private String requestId;
	private MalaclypseAnalysisMonitor analysisMonitor;
	
	public WiredArtifactContainerUpdateRequest(String id, ArtifactContainer container, ArtifactContainerUpdateRequestType walkMode, boolean updateEclipse) {
		super();
		this.requestId = id;
		this.container = container;
		this.walkMode = walkMode;
		this.updateEclipse = updateEclipse;
		this.analysisMonitor = new MalaclypseAnalysisMonitor(id);		
	}
	public WiredArtifactContainerUpdateRequest(String id, ArtifactContainer container, ArtifactContainerUpdateRequestType walkMode, boolean updateEclipse, ClasspathResolverContract contract) {
		super();
		this.requestId = id;
		this.container = container;		
		this.walkMode = walkMode;
		this.updateEclipse = updateEclipse;
		this.classpathResolverContract = contract;
		this.analysisMonitor = new MalaclypseAnalysisMonitor( id);		
	}
	
	public WiredArtifactContainerUpdateRequest( WiredArtifactContainerUpdateRequest sibling) {
		super();		
		this.container = sibling.container;
		this.walkMode = sibling.walkMode;
		this.updateEclipse = sibling.updateEclipse;
		this.classpathResolverContract = sibling.classpathResolverContract;
		this.analysisMonitor = new MalaclypseAnalysisMonitor( sibling.getAnalysisMonitor());
		this.requestId = sibling.getRequestId();
	}
	
	public String getRequestId() {
		return requestId;
	}
	
	public ArtifactContainer getContainer() {
		return container;
	}
	public void setContainer(ArtifactContainer container) {
		this.container = container;
	}
	public ArtifactContainerUpdateRequestType getWalkMode() {
		return walkMode;
	}
	public void setWalkMode(ArtifactContainerUpdateRequestType walkMode) {
		this.walkMode = walkMode;
	}		
		
	public boolean getUpdateEclipse() {
		return updateEclipse;
	}
	public void setUpdateEclipse(boolean updateEclipse) {
		this.updateEclipse = updateEclipse;
	}
			
	public MalaclypseAnalysisMonitor getAnalysisMonitor() {
		return analysisMonitor;
	}
	public void setAnalysisMonitor(MalaclypseAnalysisMonitor analysisMonitor) {
		this.analysisMonitor = analysisMonitor;
	}
	public ClasspathResolverContract getClasspathResolverContract() {
		return classpathResolverContract;
	}
	public void setClasspathResolverContract(ClasspathResolverContract classpathResolverContract) {
		this.classpathResolverContract = classpathResolverContract;
	}
}
