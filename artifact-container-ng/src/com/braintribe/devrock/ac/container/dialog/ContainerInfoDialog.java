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
package com.braintribe.devrock.ac.container.dialog;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.devrock.ac.container.ArtifactContainer;
import com.braintribe.devrock.ac.container.properties.component.ContainerPropertiesComponent;

public class ContainerInfoDialog extends Dialog  {
	public static final int SHELL_STYLE = SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.APPLICATION_MODAL;


	private ArtifactContainer container;	
	private IProject iProject;
	private Shell parentShell;
	private ContainerPropertiesComponent cpc;

	
	public ContainerInfoDialog(Shell parentShell) {
		super(parentShell);
		this.parentShell = parentShell;		
		
		setShellStyle(SHELL_STYLE);
		
		
	}
	
	@Configurable @Required
	public void setContainer(ArtifactContainer container) {
		this.container = container;		
	}
	
	@Configurable @Required
	public void setProject(IProject selectedProject) {
		iProject = selectedProject;			
	}

	@Override
	protected Control createDialogArea(Composite parent) {	
		initializeDialogUnits(parent);
		
		parentShell.layout(true);
	
		final Composite composite = new Composite(parent, SWT.NONE);
		int nColumns= 4;
        GridLayout layout= new GridLayout();
        layout.numColumns = nColumns;
        composite.setLayout( layout);
        composite.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true));
    
		
		cpc = new ContainerPropertiesComponent(parentShell, container, null);
		cpc.createControl(parent);
			
			
		return composite;
	}
	

	@Override
	protected Point getInitialSize() {		
		return new Point( 500, 450);
	}
	
	

	@Override
	protected Control createButtonBar(Composite parent) {
		Composite buttonComposite = new Composite(parent, SWT.NONE);
		buttonComposite.setLayoutData(  new GridData( SWT.LEFT, SWT.CENTER, true, false, 4, 1));
		GridLayout layout= new GridLayout();
	    layout.numColumns = 3;
		buttonComposite.setLayout( layout);
		
		Button closeButton = new Button( buttonComposite, SWT.NONE);
		closeButton.setText("OK");
		closeButton.addSelectionListener( new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				if (event.widget == closeButton) 
					close();				
			}
			
		});
		buttonComposite.setLayoutData(  new GridData( SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		return closeButton;
	}

	
	
	@Override
	protected void configureShell(Shell newShell) {		
		super.configureShell(newShell);
		newShell.setText("Container properties of project " + iProject.getName());
	}
	
	@Override
	public boolean close() {
		return super.close();
	}
	
	

}
