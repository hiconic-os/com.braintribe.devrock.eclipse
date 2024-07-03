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
package com.braintribe.devrock.greyface.views.dependency.tabs.capability.pomloading;

import java.io.File;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.braintribe.devrock.greyface.GreyfacePlugin;
import com.braintribe.devrock.greyface.GreyfaceStatus;
import com.braintribe.devrock.greyface.view.tab.HasTreeTokens;
import com.braintribe.model.artifact.Part;
import com.braintribe.model.artifact.PartTuple;
import com.braintribe.model.artifact.processing.part.PartTupleProcessor;

public class PomLoader implements HasTreeTokens{

	public static void loadPoms( TreeItem [] items) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		PartTuple pomTuple = PartTupleProcessor.createPomPartTuple();
		for (TreeItem item : items) {
			Part part = (Part) item.getData( KEY_PART);
			if (part == null) {
				continue;
			}
			if (PartTupleProcessor.equals(pomTuple, part.getType())) {
				File file = new File( part.getLocation());
				if (file.exists()) {
					IFileStore fileStore = EFS.getLocalFileSystem().getStore( file.toURI());		   		    
				    try {
				        IDE.openEditorOnFileStore( page, fileStore );
				    } catch ( PartInitException e ) {
				    	String msg = "cannot open pom [" + file.getAbsolutePath() + "]";
				    	GreyfaceStatus status = new GreyfaceStatus( msg, e);
				    	GreyfacePlugin.getInstance().getLog().log(status);			
				    }
				}
			}
		}

	}
}
