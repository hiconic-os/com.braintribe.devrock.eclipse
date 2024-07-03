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
package com.braintribe.devrock.api.ui.viewers.pom;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.braintribe.cfg.Configurable;
import com.braintribe.model.artifact.declared.DeclaredArtifact;
import com.braintribe.utils.IOTools;

/**
 * simple viewer for XML (or rather a {@link DeclaredArtifact} or an pom XML directly 
 * 
 * @author pit
 *
 */
public class PomViewer {
	private String payload = "<nothing/>";
		
	@Configurable
	public void setPom( String pomAsString) {
		this.payload = pomAsString;		
	}	
	
	@Configurable
	public void setPom( File file) {
		try {
			payload = IOTools.slurp( file, "UTF-8");
		} catch (IOException e) {
			payload = e.getMessage();
		}		
	}
		
	public Composite createControl( Composite parent, String tag) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		composite.setLayout(layout);
	
		// label 
		Composite treeLabelComposite = new Composite( composite, SWT.NONE);
        treeLabelComposite.setLayout( layout);
        treeLabelComposite.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 4,1));
        
        Label treeLabel = new Label( treeLabelComposite, SWT.NONE);
        treeLabel.setText( tag);
        treeLabel.setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, true, false, 3, 1));
			
        Text reasonText = new Text( composite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		reasonText.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 4, 10));
		reasonText.setText( payload);
		
		composite.pack();	
		return composite;
	}
	
	public void dispose() {		
	}
}
