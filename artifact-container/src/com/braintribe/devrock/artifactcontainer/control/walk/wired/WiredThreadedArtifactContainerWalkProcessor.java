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
package com.braintribe.devrock.artifactcontainer.control.walk.wired;

import java.util.concurrent.BlockingQueue;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.devrock.artifactcontainer.ArtifactContainerPlugin;
import com.braintribe.devrock.artifactcontainer.container.ArtifactContainer;
import com.braintribe.devrock.artifactcontainer.control.walk.ArtifactContainerUpdateRequestType;
import com.braintribe.devrock.artifactcontainer.control.walk.wired.notification.ContainerProcessingNotificationBroadcaster;
import com.braintribe.devrock.artifactcontainer.control.walk.wired.notification.ContainerProcessingNotificationListener;
import com.braintribe.devrock.artifactcontainer.control.walk.wired.processing.JobBasedUpdateRequestProcessor;
import com.braintribe.devrock.artifactcontainer.control.walk.wired.processing.UpdateRequestProcessor;

/**
 * the deferring processor for {@link WiredArtifactContainerUpdateRequest} sent form the {@link WiredArtifactContainerWalkController}
 * @author pit
 *
 */
public class WiredThreadedArtifactContainerWalkProcessor extends Thread implements ContainerProcessingNotificationBroadcaster {
	private BlockingQueue<WiredArtifactContainerUpdateRequest> queue;
	private boolean done;		

	private UpdateRequestProcessor requestProcessor;
	
	public WiredThreadedArtifactContainerWalkProcessor() {
		requestProcessor = new JobBasedUpdateRequestProcessor();
		requestProcessor.setArtifactContainerRegistry( ArtifactContainerPlugin.getArtifactContainerRegistry());
	}
		
	@Configurable @Required
	public void setQueue(BlockingQueue<WiredArtifactContainerUpdateRequest> queue) {
		this.queue = queue;
	}
	
	public void signalDone() {
		done = true;
	}
	
	@Override
	public void run() {		
		do {		
			final WiredArtifactContainerUpdateRequest updateRequest  = queue.poll();			
			if (updateRequest == null) {
				try {
					sleep( 1000);
				} catch (InterruptedException e) {				
					e.printStackTrace();
				}				
			}		
			else {
				final ArtifactContainer container = updateRequest.getContainer();			
				ArtifactContainerUpdateRequestType walkMode = updateRequest.getWalkMode();							
				requestProcessor.processRequest(updateRequest, container, walkMode);
			}
		} while (! done);
		
	}

	
	@Override
	public void addListener(ContainerProcessingNotificationListener listener) {
		requestProcessor.addListener(listener);		
	}

	@Override
	public void removeListener(ContainerProcessingNotificationListener listener) {
		requestProcessor.removeListener(listener);		
	}
	
}
