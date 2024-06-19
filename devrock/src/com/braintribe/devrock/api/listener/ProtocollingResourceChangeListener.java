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