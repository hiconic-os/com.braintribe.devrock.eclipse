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
package com.braintribe.devrock.ac.container.plugin.listener;

import java.io.File;
import java.util.Collections;
import java.util.function.Consumer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

import com.braintribe.devrock.ac.container.plugin.ArtifactContainerPlugin;
import com.braintribe.devrock.ac.container.plugin.ArtifactContainerStatus;
import com.braintribe.devrock.ac.container.updater.ProjectUpdater;
import com.braintribe.devrock.ac.container.updater.ProjectUpdater.Mode;
import com.braintribe.logging.Logger;

/**
 * 
 * {@link IResourceDeltaVisitor} to detect changes in the pom of a project
 * @author pit
 *
 */
public class ResourceVisitor implements IResourceDeltaVisitor {
	private static Logger log = Logger.getLogger(ResourceVisitor.class);
	private Consumer<IProject> updateRequestHandler;
	
	public ResourceVisitor(Consumer<IProject> consumer) {
		updateRequestHandler = consumer;
	}

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		IResource resource = delta.getResource();
		if (resource == null)
			return true;
			
		String resourceName = resource.getName();
		IProject project = resource.getProject();
		

		// workspace project controller probably needs to be updated ..
		if (resourceName.equalsIgnoreCase( ".project") || resourceName.equalsIgnoreCase("pom.xml")) {
			if (project != null) {
				//System.out.println("project modified : " + project.getName());
				//ArtifactContainerPlugin.getWorkspaceProjectRegistry().update( project);
			}
			else {
				//ArtifactContainerPlugin.getWorkspaceProjectRegistry().update();
			}
		}
		// standard containers listen to pom
		if (resourceName.equalsIgnoreCase( "pom.xml") ) {
			
			if (project.isAccessible() == false)
				return true;
			
			File prjDirectory = project.getLocation().toFile();
			File resourceFile = resource.getLocation().toFile();
			if (!resourceFile.getParent().equals( prjDirectory.getAbsolutePath())) {
				log.info("skippling not relevant pom resource [" + resourceFile.getAbsolutePath() + "] in project: " + project.getName());
				return true;
			}
			
			// signal listener to mark project as changed for depender update
			updateRequestHandler.accept(project);
			
			// perhaps calculate MD5 as in old mc and if changed, trigger and update?
			
			// actually update the CP as it's been changed
			ProjectUpdater updater = new ProjectUpdater( Mode.pom);
			try {
				updater.setSelectedProjects( Collections.singleton( project));
				updater.runAsJob();
			} catch (Exception e) {
				String msg = "cannot react on changes in [" + resource.getFullPath().toOSString() + "]";
				log.error( msg, e);
				ArtifactContainerStatus status = new ArtifactContainerStatus( msg, e);
				ArtifactContainerPlugin.instance().log(status);
			} 							
		}				
								
		// 
		//continue visiting.. 
		return true;
	}

}
