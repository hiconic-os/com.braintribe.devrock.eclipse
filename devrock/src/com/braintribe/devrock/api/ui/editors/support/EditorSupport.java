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
package com.braintribe.devrock.api.ui.editors.support;

import java.io.File;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;

import com.braintribe.devrock.plugin.DevrockPlugin;
import com.braintribe.devrock.plugin.DevrockPluginStatus;


/**
 * helper class to load either {@link IResource} (from a {@link IProject}) or a {@link File} into an associated editor in Eclipse. 
 * The choice what editor it is, is completely left to Eclipse, no choices here.
 * 
 * @author pit
 *
 */
public class EditorSupport {

	/**
	 * load a resource into the associated editor
	 * @param resource - the {@link IResource}
	 * @return - true if no errors occurred, false if anything went wrong 
	 */
	public static boolean load( IResource resource) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (resource instanceof IFile) {
			IFile iFile = (IFile) resource;
			IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(iFile.getName());
			try {
				page.openEditor(new FileEditorInput(iFile), desc.getId());
				return true;
			} catch (PartInitException e) {
		    	String msg = "cannot open resource [" + resource.getName() + "]";
		    	DevrockPluginStatus status = new DevrockPluginStatus( msg, e);
		    	DevrockPlugin.instance().log(status);
			}
		}		
		return false;
	}
	
	/**
	 * load a file into the associated editor 
	 * @param file - the {@link File} 
	 * @return - true if no errors occurred, false if anything went wrong 
	 */
	public static boolean load( File file) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		
		IFileStore fileStore = EFS.getLocalFileSystem().getStore( file.toURI());		   		    
	    try {
	        IDE.openEditorOnFileStore( page, fileStore );
	        return true;
	    } catch ( PartInitException e ) {
	    	String msg = "cannot open file [" + file.getAbsolutePath() + "]";
	    	DevrockPluginStatus status = new DevrockPluginStatus( msg, e);
	    	DevrockPlugin.instance().log(status);
	    	return false;
	    }
	}
}
