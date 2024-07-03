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
package com.braintribe.devrock.artifactcontainer.control.container.decorator;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;

import com.braintribe.devrock.artifactcontainer.ArtifactContainerPlugin;
import com.braintribe.devrock.artifactcontainer.control.container.ArtifactContainerRegistry;
import com.braintribe.devrock.artifactcontainer.views.dependency.diagnostics.ContainerState;
import com.braintribe.devrock.artifactcontainer.views.dependency.diagnostics.ProjectDiagnostics;
import com.braintribe.model.malaclypse.WalkMonitoringResult;

public class ProjectDecorator implements ILightweightLabelDecorator {
	private ImageDescriptor errorImageDescriptor;
	private ImageDescriptor warningImageDescriptor;
	private ImageDescriptor deadImageDescriptor;
	private ArtifactContainerRegistry artifactContainerRegistry = ArtifactContainerPlugin.getArtifactContainerRegistry();
	
	
	public ProjectDecorator()  {
		errorImageDescriptor = ImageDescriptor.createFromFile( ProjectDecorator.class, "error_ovr.gif");
		warningImageDescriptor = ImageDescriptor.createFromFile( ProjectDecorator.class, "warning_small.png");
		deadImageDescriptor = ImageDescriptor.createFromFile( ProjectDecorator.class, "skull.png");
		
	}

	@Override
	public void addListener(ILabelProviderListener arg0){}
	@Override
	public void removeListener(ILabelProviderListener arg0){}
	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}

	@Override
	public void dispose(){}


	@Override
	public void decorate(Object element, IDecoration iDecoration) {
		if (element instanceof IProject == false)
			return;		
		IProject project = (IProject) element;
		
		// check if project has a container, if not, none of our business
		if (ArtifactContainerPlugin.getArtifactContainerRegistry().getContainerOfProject(project) == null) {
			return;
		}
		// dead check 
		if (ArtifactContainerPlugin.getWorkspaceProjectRegistry().hasLastWalkFailed( project)) {
			iDecoration.addOverlay(deadImageDescriptor, IDecoration.TOP_LEFT);
			return;
		}
		
		WalkMonitoringResult walkResult = artifactContainerRegistry.getCompileWalkResult( project);
		
		
		// no result at all - something's fishy 
		if (walkResult == null ) {
			if (artifactContainerRegistry.getContainerOfProject(project) != null) {
				iDecoration.addOverlay(deadImageDescriptor, IDecoration.TOP_LEFT);
			}
			return;
		}
		
		ContainerState containerState = ProjectDiagnostics.isProjectHealhty(walkResult);
		switch (containerState) {
			case error:
				iDecoration.addOverlay(errorImageDescriptor, IDecoration.TOP_LEFT);
				break;
			case warning:
				iDecoration.addOverlay(warningImageDescriptor, IDecoration.TOP_LEFT);
				break;
			case dead:
				iDecoration.addOverlay(deadImageDescriptor, IDecoration.TOP_LEFT);
				break;
			default:
				break;
		}
	}
	

}
