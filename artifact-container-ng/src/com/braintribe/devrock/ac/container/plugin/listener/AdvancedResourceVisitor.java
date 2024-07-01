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

import java.util.function.Consumer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

import com.braintribe.logging.Logger;

/**
 * a {@link IResourceDeltaVisitor} to handle {@link IResourceDelta} impacting the containers 
 */
public class AdvancedResourceVisitor implements IResourceDeltaVisitor {
	private static Logger log = Logger.getLogger(AdvancedResourceVisitor.class);
	
	private Consumer<IProject> projectToBuildConsumer;
	private Consumer<IProject> dependersToBuildConsumer;
	
	/**
	 * @param buildProjectConsumer - a {@link Consumer} for projects that need to be refreshed
	 * @param buildDependersConsumer - a {@link Consumer} for projects of which their dependers need to be refreshed
	 */
	public AdvancedResourceVisitor(Consumer<IProject> buildProjectConsumer, Consumer<IProject> buildDependersConsumer) {
		projectToBuildConsumer = buildProjectConsumer;
		dependersToBuildConsumer = buildDependersConsumer;
	}

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
					
		IResource resource = delta.getResource();
		if (resource == null)
			return true;
		
		if (resource instanceof IProject) {
			if (isProjectRelevant(delta)) {
				IProject project = (IProject) resource;
				boolean isAccessible = project.isAccessible();
				boolean isOpen = project.isOpen();
				log.debug("Visitor: project [" + project.getName() + "]: " + ( isAccessible ? "access " : "no acc ") + (isOpen ? "open" : "closed"));
				dependersToBuildConsumer.accept( project);
			}
		}
		else {
			String name = resource.getName();
			if (
					(name.compareTo( "pom.xml") == 0) && 
					isPomChangeRelevant(delta)
				) {
				IProject project = resource.getProject();
				projectToBuildConsumer.accept( project);
			}
		}		
		return true;
	}

	
	/**
	 * checks the delta flags for relevant changes to the POM
	 * @param delta - the {@link IResourceDelta}
	 * @return - true if the change was relevant, false otherwise 
	 */
	private boolean isPomChangeRelevant(IResourceDelta delta) {
		int flags = delta.getFlags();		
		
		if (flags == 0)
			return false;
		
		if ( (flags & IResourceDelta.CONTENT) == IResourceDelta.CONTENT) {
			log.debug("Pom change: CONTENT");
			return true;
		}
			
		return false;
	}
	
	
	/**
	 * checks the delta & kind flags for relevant changes to the project
	 * @param delta - the {@link IResourceDelta}
	 * @return - true if changes in the project are relevant for us 
	 */
	private boolean isProjectRelevant(IResourceDelta delta) {
		int kind = delta.getKind();
		if (kind != 0) {
		
			if ( (kind & IResourceDelta.ADDED) == IResourceDelta.ADDED) {
				log.debug("Project change: ADDED");
				return true;
			}
			
			if ( (kind & IResourceDelta.REMOVED) == IResourceDelta.REMOVED) {
				log.debug("Project change: REMOVED");
				return true;
			}
		}
		
		int flags = delta.getFlags();		
		
		if (flags == 0)
			return false;
		
		if ( (flags & IResourceDelta.OPEN) == IResourceDelta.OPEN) {
			log.debug("Project change: OPEN_CHANGED");
			return true;
		}
		
		return false;
	}
	
	

}
