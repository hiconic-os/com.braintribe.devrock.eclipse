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
package com.braintribe.devrock.arb.builder;

import java.io.File;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import com.braintribe.devrock.api.storagelocker.StorageLockerSlots;
import com.braintribe.devrock.arb.plugin.ArtifactReflectionBuilderPlugin;
import com.braintribe.devrock.arb.plugin.ArtifactReflectionBuilderStatus;
import com.braintribe.devrock.artifact.ArtifactReflectionGenerator;
import com.braintribe.devrock.plugin.DevrockPlugin;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.logging.Logger;


/**
 * @author pit
 *
 */
public class ArtifactReflectionBuilder extends IncrementalProjectBuilder {
	private static Logger log = Logger.getLogger(ArtifactReflectionBuilder.class);
	public static final String ID = "com.braintribe.devrock.arb.builder.ArtifactReflectionBuilder";
	
	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
		IProject project = getProject();
		
		boolean useIncrementalBuilds = DevrockPlugin.instance().storageLocker().getValue( StorageLockerSlots.SLOT_BUILDER_AR_INCREMENTAL_ALSO, false);
		
				
		// from Ralf : only full builds are to be executed..
		if (!useIncrementalBuilds && kind != IncrementalProjectBuilder.FULL_BUILD) {
			return null;
		}
		log.debug("called via builder integration: " + ((kind == IncrementalProjectBuilder.FULL_BUILD) ? "full build" : "incremental build"));
		
		build( project);
		
		return null;
	}
	

	/**
	 * actual build run
	 * @param project - the {@link IProject} to run the build for
	 */
	public static void build(IProject project) {
		File projectFolder = project.getLocation().makeAbsolute().toFile();
		
		IJavaProject javaProject;
		try {
			javaProject = JavaCore.create(project);
		} catch (Exception e) {			
			String msg = "not a valid Java project :" + project.getName();
			ArtifactReflectionBuilderStatus status = new ArtifactReflectionBuilderStatus(msg, e);
			ArtifactReflectionBuilderPlugin.instance().log(status);
			return;
		}
		
		// arb output folder is a sibling of the java project's output folder..
		File binaryOutputFolder;
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		try {
			binaryOutputFolder = workspace.getRoot().getFile( javaProject.getOutputLocation()).getRawLocation().toFile();
		} catch (JavaModelException e) {
			String msg = "cannot access binary output folder of :" + project.getName();
			ArtifactReflectionBuilderStatus status = new ArtifactReflectionBuilderStatus(msg, e);
			ArtifactReflectionBuilderPlugin.instance().log(status);
			return;
		}	 		
		
		String arbOutputDirName = DevrockPlugin.instance().storageLocker().getValue(StorageLockerSlots.SLOT_ARB_OUTPUT_DIR, StorageLockerSlots.DEFAULT_ARB_OUTPUT_DIRNAME);	
		File arbOutputFolder = new File( binaryOutputFolder.getParentFile(), arbOutputDirName);
		arbOutputFolder.mkdir();
		
		ArtifactReflectionGenerator generator = new ArtifactReflectionGenerator();	
		Maybe<Void> maybe = generator.generate(projectFolder, arbOutputFolder);
		if (maybe.isSatisfied()) {
			// check.. 
			boolean refreshAfterBuilds = DevrockPlugin.instance().storageLocker().getValue( StorageLockerSlots.SLOT_BUILDER_AR_REFRESH_POST_BUILD, false);
			if ( refreshAfterBuilds) {
				IPath path = IPath.fromFile(arbOutputFolder);
				IFile ifile= workspace.getRoot().getFileForLocation( path);
				try {
					ifile.refreshLocal(IResource.DEPTH_INFINITE, null);
				} catch (CoreException e) {
					String msg = "error broadcasting changes on project [" + project.getName() + "] (" + projectFolder.getAbsolutePath() + "," + binaryOutputFolder.getAbsolutePath() +") : " + e.getLocalizedMessage();
					ArtifactReflectionBuilderStatus status = new ArtifactReflectionBuilderStatus(msg, IStatus.ERROR);
					ArtifactReflectionBuilderPlugin.instance().log(status);
				}
			}		
			return;
		}
		
		String msg = "error while running artifact reflection generator on project [" + project.getName() + "] (" + projectFolder.getAbsolutePath() + "," + binaryOutputFolder.getAbsolutePath() +") : " + maybe.whyUnsatisfied().stringify();
		ArtifactReflectionBuilderStatus status = new ArtifactReflectionBuilderStatus(msg, IStatus.ERROR);
		ArtifactReflectionBuilderPlugin.instance().log(status);
		
	}
}
