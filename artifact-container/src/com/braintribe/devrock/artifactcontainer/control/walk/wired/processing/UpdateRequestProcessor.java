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
package com.braintribe.devrock.artifactcontainer.control.walk.wired.processing;


import com.braintribe.devrock.artifactcontainer.container.ArtifactContainer;
import com.braintribe.devrock.artifactcontainer.control.container.ArtifactContainerRegistry;
import com.braintribe.devrock.artifactcontainer.control.walk.ArtifactContainerUpdateRequestType;
import com.braintribe.devrock.artifactcontainer.control.walk.wired.WiredArtifactContainerUpdateRequest;
import com.braintribe.devrock.artifactcontainer.control.walk.wired.notification.ContainerProcessingNotificationBroadcaster;


public interface UpdateRequestProcessor extends ContainerProcessingNotificationBroadcaster {
	void processRequest(final WiredArtifactContainerUpdateRequest updateRequest, final ArtifactContainer container, ArtifactContainerUpdateRequestType walkMode);
	void setArtifactContainerRegistry( ArtifactContainerRegistry registry);
}
