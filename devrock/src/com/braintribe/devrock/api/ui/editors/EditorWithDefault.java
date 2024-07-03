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
package com.braintribe.devrock.api.ui.editors;

import java.util.function.Function;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;

public class EditorWithDefault<T> extends AbstractEditor<T>{
		
	private Function<Shell, AbstractEditor<T>> editorSupplier;
	private Font bigFont;
	private BooleanEditor useDefaultEditor;

	private String useDefaultLabel;
	private String useCustomLabel;
	
	private AbstractEditor<T> customValueEditor;
	private boolean useDefaultValue;
		
	@Configurable @Required
	public void setEditorSupplier(Function<Shell, AbstractEditor<T>> editorSupplier) {
		this.editorSupplier = editorSupplier;
	}

	@Configurable @Required
	public void setBigFont(Font bigFont) {
		this.bigFont = bigFont;
	}

	@Override
	public T getSelection() {	
		return customValueEditor.getSelection();
	}

	@Override
	public void setSelection(T value) {		
		customValueEditor.setSelection(value);
	}
	
	public void setDefaultSelection( boolean value) {
		useDefaultValue = value;
		if (useDefaultEditor != null) {
			useDefaultEditor.setSelection( useDefaultValue);
		}		
		customValueEditor.setEnabled(!useDefaultValue);		
	}
	
	public boolean getDefaultSelection() {
		return useDefaultEditor.getSelection();
	}
	
	@Configurable @Required
	public void setUseDefaultLabel(String useDefaultLabel) {
		this.useDefaultLabel = useDefaultLabel;
	}

	@Configurable @Required
	public void setUseCustomLabel(String useCustomLabel) {
		this.useCustomLabel = useCustomLabel;
	}

	public boolean isDefaultSelected() {
		return Boolean.TRUE.equals( useDefaultEditor.getSelection());
	}

	@Override
	public Composite createControl(Composite parent, String tag) {		
			final Composite composite = new Composite(parent, SWT.NONE);					
			
			int nColumns= 4;
	        GridLayout layout= new GridLayout();
	        layout.numColumns = nColumns;
	        composite.setLayout( layout);	        
	        layout.verticalSpacing=2;        
	        
	        Label titleLabelLabel = new Label( composite, SWT.NONE);	        
			titleLabelLabel.setText( tag);
	        titleLabelLabel.setFont(bigFont);	        
	        titleLabelLabel.setLayoutData( new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
	        
	        
	        useDefaultEditor = new BooleanEditor();	        
			useDefaultEditor.setLabelToolTip( labelToolTip);
			useDefaultEditor.setEditToolTip( editToolTip);
			useDefaultEditor.setSelection( useDefaultValue);
			useDefaultEditor.setSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					broadcast("changed");
					if (!useDefaultEditor.getSelection()) {
						customValueEditor.setEnabled(true);
					}
					else {
						customValueEditor.setEnabled(false);
					}
				}
				
			});

			Composite control = useDefaultEditor.createControl(composite, useDefaultLabel);		
			control.setLayoutData(new GridData( SWT.FILL, SWT.CENTER, true, false, 4, 1));
			
	        
	        customValueEditor = editorSupplier.apply( parent.getShell());
			control = customValueEditor.createControl(composite, useCustomLabel);
			control.setLayoutData(new GridData( SWT.FILL, SWT.CENTER, true, false, 4, 1));
			
			customValueEditor.addListener( this);
						
			customValueEditor.setEnabled(!useDefaultValue);
				        
	        return composite;		
	}

	@Override
	public void setEnabled(boolean value) {
		customValueEditor.setEnabled(value);		
	}

	@Override
	public void acknowledgeChange(Object sender, String value) {
		super.acknowledgeChange(sender, value);
	}

	
	

	
}
