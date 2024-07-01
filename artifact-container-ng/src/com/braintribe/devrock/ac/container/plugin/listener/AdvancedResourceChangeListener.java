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
package com.braintribe.devrock.ac.container.plugin.listener;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;

import com.braintribe.devrock.ac.container.ArtifactContainer;
import com.braintribe.devrock.ac.container.plugin.ArtifactContainerPlugin;
import com.braintribe.devrock.ac.container.updater.ProjectUpdater.Mode;
import com.braintribe.logging.Logger;

/**
 * a {@link IResourceChangeListener} to listener for relevant changes to the projects that AC manages
 */
public class AdvancedResourceChangeListener  implements IResourceChangeListener {
	private static Logger log = Logger.getLogger(AdvancedResourceChangeListener.class);
	
	private AdvancedResourceVisitor resourceVisitor = new AdvancedResourceVisitor(this::acceptProjectBuildRequest, this::acceptDependersBuildRequest);
	private IProject projectToBuild;
	private Set<IProject> projectsToBuildDependers = new HashSet<>();

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		
		IResourceDelta delta = event.getDelta();
		if (delta == null)
			return;				
		try {
			delta.accept( resourceVisitor);
		} catch (CoreException e1) {		
			e1.printStackTrace();
		}
		
		IResource resource = delta.getResource();
		if (resource == null || resource instanceof IWorkspaceRoot == false || event.getType() != IResourceChangeEvent.POST_CHANGE) {
			return;
		}			
		
		// post change only to trigger?
		
		if (event.getType() != IResourceChangeEvent.POST_CHANGE) {
			return;
		}
				
		// if a pom has been changed - notified by the resource visitor, directly update it 
		if (projectToBuild != null) {
					
			// update it 
			ArtifactContainer container = ArtifactContainerPlugin.instance().containerRegistry().getContainerOfProject(projectToBuild);
			if (container != null) {
				// reassign - and perhaps update the VAI in the container
				container.reinitialize( Mode.standard);
				// add it to the projects of whose dependers are to be built
				projectsToBuildDependers.add(projectToBuild);
			}
			else {
				log.warn("no container for [" + projectToBuild.getName() + "]");
			}								
			projectToBuild = null;
		}
	
		// if several projects have been modified 
		if (projectsToBuildDependers.size() > 0) {				
			ArtifactContainerPlugin.instance().projectUpdater().addProjectToUpdate(projectsToBuildDependers);
			projectsToBuildDependers.clear();
		}				
	}
	
	/**
	 * @param project - a {@link IProject} that needs to be built
	 */
	private void acceptProjectBuildRequest(IProject project) {	
		projectToBuild = project;
	}
	
	/**
	 * @param project - an {@link IProject} that needs to have its dependers built
	 */ 
	private void acceptDependersBuildRequest(IProject project) {
		projectsToBuildDependers.add(project);
	}
		

}
