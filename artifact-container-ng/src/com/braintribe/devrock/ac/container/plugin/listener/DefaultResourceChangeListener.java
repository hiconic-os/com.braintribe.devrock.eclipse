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

import java.util.Collections;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;

import com.braintribe.devrock.ac.container.plugin.ArtifactContainerPlugin;
import com.braintribe.devrock.ac.container.plugin.ArtifactContainerStatus;
import com.braintribe.devrock.ac.container.updater.ProjectUpdater;
import com.braintribe.devrock.ac.container.updater.ProjectUpdater.Mode;
import com.braintribe.devrock.ac.container.updater.WorkspaceUpdater;
import com.braintribe.devrock.api.storagelocker.StorageLockerSlots;
import com.braintribe.devrock.plugin.DevrockPlugin;
import com.braintribe.logging.Logger;

/**
 * standard {@link IResourceChangeListener}
 * 
 * @author pit
 */
public class DefaultResourceChangeListener implements IResourceChangeListener {
	private static Logger log = Logger.getLogger(DefaultResourceChangeListener.class);

	private ResourceVisitor resourceVisitor = null;
			
	
	public DefaultResourceChangeListener() {	
		resourceVisitor = new ResourceVisitor( this::acceptUpdateRequest);
	}
	
	public void acceptUpdateRequest(IProject project) {
		ProjectUpdater updater = new ProjectUpdater( Mode.pom);
		try {
			updater.setSelectedProjects( Collections.singleton( project));
			updater.runAsJob();
		} catch (Exception e) {
			String msg = "cannot react on changes in pom of [" + project.getName() + "]";
			log.error( msg, e);
			ArtifactContainerStatus status = new ArtifactContainerStatus( msg, e);
			ArtifactContainerPlugin.instance().log(status);
		} 
	}
	
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		try {
			if (
					(event.getType() == IResourceChangeEvent.PRE_DELETE) &&
					(event.getResource() instanceof IProject)
				) {
				IProject project = (IProject) event.getResource();
				System.out.println("Pre deleting project [" +  project.getName() + "]");				
			}
			IResourceDelta delta = event.getDelta();
			// no changes? 
			if (delta == null)
				return;
			
			int kind = delta.getKind();
			switch( kind ) {
				case IResourceDelta.CHANGED : {				
					int flags = delta.getFlags();
					if ((flags & IResourceDelta.MARKERS) != 0)
						return;
				}
			}
			
			// install visitor - to detect changes in the pom, see ResourceVisitor
			try {
				delta.accept( resourceVisitor);
			} catch (CoreException e1) {		
				e1.printStackTrace();
			}
			

			IResource resource = delta.getResource();
			if (resource != null) {

				// update entries as the workspace has been changed (projects opened/closed/loaded/deleted)			
				if (
						resource instanceof IWorkspaceRoot &&						
						event.getType() == IResourceChangeEvent.POST_CHANGE ||
						event.getType() == IResourceChangeEvent.PRE_REFRESH
					) {
					// trigger update ?
					boolean autoUpdate = DevrockPlugin.envBridge().storageLocker().getValue( StorageLockerSlots.SLOT_AUTO_UPDATE_WS, true);
					if (autoUpdate) {
						
						if (DevrockPlugin.instance().isWorkspaceDirty()) {
							// close scoped bridge to make sure we get a new scope  
							DevrockPlugin.mcBridge().close();
							// update the containers in the WS
							WorkspaceUpdater updater = new WorkspaceUpdater();
							updater.runAsJob();
						}
					}
					else {
						log.debug("auto update on workspace inhibited by user choice");
					}
																											
				}			
			}
		} catch (Exception e) {			
			ArtifactContainerStatus status = new ArtifactContainerStatus("error while reacting to resource change", e);
			ArtifactContainerPlugin.instance().log(status);	
		}
	}
	
}
