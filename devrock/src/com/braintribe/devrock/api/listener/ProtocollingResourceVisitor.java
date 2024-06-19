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
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

/**
 * a {@link IResourceDeltaVisitor} that protocolls the {@link IResourceDelta}
 */
public class ProtocollingResourceVisitor implements IResourceDeltaVisitor {

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {

		 
		
		IResource resource = delta.getResource();
		
		if (resource instanceof IProject) {
			System.out.println("*** VISTOR EVENT START ***");
			IProject prj = (IProject) resource;
			System.out.println("Project: " + prj.getName());
			System.out.println("stamped: " + prj.getModificationStamp());				
			logFlags(delta);		
			logKind(delta);
			System.out.println("*** VISTOR END ***");
		}
		else {	
			String name = resource.getName();
			if (name.compareTo( "pom.xml") == 0) {
				System.out.println("*** VISTOR START ***");
				System.out.println("resource: " + name);
				logFlags(delta);		
				logKind(delta);
				System.out.println("*** VISITOR END ***");
			}
		}
		
			
		return true;
	}
	
	/**
	 * @param delta - the flags to interpret
	 */
	private void logFlags(IResourceDelta delta) {	
		int flags = delta.getFlags();
		
		if (flags == 0)
			return;
		
		System.out.print("Delta flags : ");
		
		if ( (flags & IResourceDelta.CONTENT) == IResourceDelta.CONTENT) {
			System.out.println("CONTENT");
		}
		
		if ( (flags & IResourceDelta.DERIVED_CHANGED) == IResourceDelta.DERIVED_CHANGED) {
			System.out.println("DERIVED_CHANGED");
		}
		
		if ( (flags & IResourceDelta.ENCODING ) == IResourceDelta.ENCODING ) {
			System.out.println("ENCODING_CHANGED");
		}
		
		if ( (flags & IResourceDelta.DESCRIPTION) == IResourceDelta.DESCRIPTION) {
			System.out.println("DESCRIPTION_CHANGED");
		}
		
		if ( (flags & IResourceDelta.OPEN) == IResourceDelta.OPEN) {
			System.out.println("OPEN_CHANGED");
		}
		
		if ( (flags & IResourceDelta.TYPE ) == IResourceDelta.TYPE ) {
			System.out.println("TYPE_CHANGED");
		}
		
		if ( (flags & IResourceDelta.SYNC ) == IResourceDelta.SYNC ) {
			System.out.println("SYNC_CHANGED");
		}
		
		if ( (flags & IResourceDelta.MARKERS) == IResourceDelta.MARKERS) {
			System.out.println("MARKERS_CHANGED");
		}
		
		if ( (flags & IResourceDelta.REPLACED) == IResourceDelta.REPLACED) {
			System.out.println("REPLACED_CHANGED");
		}
		if ( (flags & IResourceDelta.LOCAL_CHANGED) == IResourceDelta.LOCAL_CHANGED) {
			System.out.println("LOCAL_CHANGED");
		}
		
		if ( (flags & IResourceDelta.MOVED_TO) == IResourceDelta.MOVED_TO) {
			System.out.println("MOVED_TO");
		}
	
		if ( (flags & IResourceDelta.MOVED_FROM) == IResourceDelta.MOVED_FROM) {
			System.out.println("MOVED_FROM");
		}
		
		if ( (flags & IResourceDelta.COPIED_FROM) == IResourceDelta.COPIED_FROM) {
			System.out.println("COPIED_FROM");
		}
	}
	
	/**
	 * @param delta - the kind to interpret
	 */
	private void logKind( IResourceDelta delta) {
		int kind = delta.getKind();
		if (kind == 0)
			return;
		
		System.out.print("Delta kind: ");
		
		if ( (kind & IResourceDelta.ADDED) == IResourceDelta.ADDED) {
			System.out.println("ADDED");
		}
		
		if ( (kind & IResourceDelta.REMOVED) == IResourceDelta.REMOVED) {
			System.out.println("REMOVED");
		}
		
		if ( (kind & IResourceDelta.CHANGED) == IResourceDelta.CHANGED) {
			System.out.println("CHANGED");
		}
	}
	

}
