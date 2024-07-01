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
package com.braintribe.devrock.arb.listener;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

/**
 * @author pit
 *
 */
public class ResourceVisitor implements IResourceDeltaVisitor {
	
	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		IResource resource = delta.getResource();
		if (resource == null)
			return true;
			
		String resourceName = resource.getName();
		IProject project = resource.getProject();
		
		// check if the changed resource's a pom and if the content of the pom has changed..		
		if ( resourceName.equalsIgnoreCase( "pom.xml") &&  
			 (delta.getFlags() & IResourceDelta.CONTENT) == IResourceDelta.CONTENT) {
			
			if (project.isAccessible() == false)
				return true;
			
			// make sure it's a pom directly in the main directory of the project and not a fake (testing purposes for instance)
			File prjDirectory = project.getLocation().toFile();
			File resourceFile = resource.getLocation().toFile();
			if (!resourceFile.getParent().equals( prjDirectory.getAbsolutePath())) {
				return true;
			}		
			
			// actually call the runner
			BuilderRunner runner = new BuilderRunner();
			runner.setProject(project);
			runner.runAsJob();
		}				
								
		//continue visiting.. 
		return true;
	}

}
