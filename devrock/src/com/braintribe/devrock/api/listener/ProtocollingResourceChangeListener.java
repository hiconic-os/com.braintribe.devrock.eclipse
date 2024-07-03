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
package com.braintribe.devrock.api.listener;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;

/**
 * helper to identify changes in the workspace as an additional {@link IResourceChangeListener}
 * 
 * @author pit
 */
public class ProtocollingResourceChangeListener implements IResourceChangeListener {
	private ProtocollingResourceVisitor prv = new ProtocollingResourceVisitor();

	@Override
	public void resourceChanged(IResourceChangeEvent arg0) {
		protocolEvent(arg0);
		
	}

	/**
	 * @param event - the {@link IResourceChangeEvent} as sent by Eclipse
	 */
	/**
	 * @param event
	 */
	private void protocolEvent(IResourceChangeEvent event) {
		
		System.out.println("**** Protocolling event START ****");
		int type = event.getType();
		String key;
		switch( type) {
			case 1: 
				key = "POST_CHANGE";
				break;
			case 2: 
				key = "PRE_CLOSE";
				break;
			case 4: 
				key = "PRE_DELETE";
				break;
			case 8: 
				key = "PRE_BUILD";
				break;
			case 16: 
				key = "POST_BUILD";
				break;
			case 32: 
				key = "PRE_REFRESH";
				break;
			default: 
				key = "<n/a>";
				break;
		}		
		System.out.println("type: [" + key + "]");
		
		// check delta 
		IResourceDelta delta = event.getDelta();
		if (delta != null) {
			
			// call visitor
			try {
				delta.accept( prv);
			} catch (CoreException e) {			
				e.printStackTrace();
			}			
			
			IResource resource = delta.getResource();
			
			// interpret the workspace resource 
			if (resource instanceof IWorkspaceRoot) {
				System.out.println("WORKSPACE delta");
				IWorkspaceRoot wroot = (IWorkspaceRoot) resource;
				IProject[] projects = wroot.getProjects();
				System.out.println("num workspace project: [" + projects.length  + "]");
				for (IProject project : projects) {
					long modificationStamp = project.getModificationStamp();
					if (modificationStamp == IResource.NULL_STAMP) {
						System.out.println("Project : " + project.getName() + "[accessible: " + project.isAccessible() + ", unchanged]");
					}
					else {
						System.out.println("Project : " + project.getName() + "[accessible: " + project.isAccessible() + ", value : " + modificationStamp + "]");
					}
				}
				
			}
			
			String resourceName = resource.getName();
			System.out.println("delta resource name: [" + resourceName + "]");
		}
		else {
			System.out.println("No delta");
		}
		
		Object source = event.getSource();
		System.out.println( "event source: [" + (source != null ? source.getClass().getName() : "null") + "]");
		
		IResource resource = event.getResource();
		if (resource != null) {
			String name = resource.getName();			
			System.out.println("event resource: [" + (name != null ? name : resource.getClass()) + "]");
		}
		else {
			System.err.println("No event resource");
		}
		
		int buildKind = event.getBuildKind();
		System.out.println( "build: [" + buildKind + "]");
		
		System.out.println("**** Protocolling event END ****");
		
	}
	
}
