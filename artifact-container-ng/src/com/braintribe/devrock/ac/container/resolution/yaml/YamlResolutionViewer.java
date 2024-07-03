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
package com.braintribe.devrock.ac.container.resolution.yaml;


import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.braintribe.devrock.api.ui.viewers.yaml.YamlViewer;
import com.braintribe.model.artifact.analysis.AnalysisArtifactResolution;

public class YamlResolutionViewer extends Dialog {
	public static final int SHELL_STYLE = SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.APPLICATION_MODAL;	

	private YamlViewer yamlViewer;
	private AnalysisArtifactResolution resolution;
	
	public void setResolution(AnalysisArtifactResolution resolution) {
		this.resolution = resolution;
	}
	
	public YamlResolutionViewer(Shell parentShell) {
		super(parentShell);	
		setShellStyle(SHELL_STYLE);
	}
	
	@Override
	protected Point getInitialSize() {		
		return new Point( 800, 600);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {	
		
		initializeDialogUnits(parent);
		final Composite composite = new Composite(parent, SWT.NONE);
		int nColumns= 4;
        GridLayout layout= new GridLayout();
        layout.numColumns = nColumns;
        composite.setLayout( layout);
        composite.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true));
        
		yamlViewer = new YamlViewer();
        yamlViewer.setResolution(resolution);
        
        Composite yamlViewerComposite = yamlViewer.createControl(composite, "yaml");
        yamlViewerComposite.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true));
       
		return composite;
	}
	
	
}
