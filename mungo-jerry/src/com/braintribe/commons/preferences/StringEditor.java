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
package com.braintribe.commons.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.braintribe.build.artifact.virtualenvironment.VirtualPropertyResolver;
import com.braintribe.cfg.Configurable;
import com.braintribe.devrock.virtualenvironment.listener.VirtualEnvironmentNotificationListener;


public class StringEditor extends AbstractEditor implements ModifyListener, VirtualEnvironmentNotificationListener {
	
	private String startValue;
	private boolean startEnabled = true;
	private ModifyListener startListener;
	private Text text;	
	private VirtualPropertyResolver resolver;
	
	@Configurable
	public void setResolver(VirtualPropertyResolver resolver) {
		this.resolver = resolver;
	}

	public Composite createControl( Composite parent, String tag) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		composite.setLayout(layout);
		
		Label label = new Label( composite, SWT.NONE);
		label.setText( tag);
		label.setLayoutData( new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		
		text = new Text( composite, SWT.NONE);
		text.setLayoutData( new GridData(SWT.FILL, SWT.CENTER, true, true, 3, 1));
		if (startValue != null) {
			text.setText( startValue);
		}		
		text.setEnabled(startEnabled);
		if (startListener != null)
			text.addModifyListener(startListener);
		text.addModifyListener( this);
		text.setToolTipText( resolver != null ? resolver.resolve( text.getText()) : text.getText());
		return composite;
	}
	
	public String getSelection() {
		return text.getText();
	}
	
	public void setSelection( String selection) {
		if (text == null) {
			startValue = selection;
		}
		else {
			text.setText(selection);
		}
		
	}

	public void setEnabled( boolean enabled) {
		if (text == null) {
			startEnabled = enabled;
		}
		else {
			text.setEnabled(enabled);
		}
	}
 
	public void addModifyListener( ModifyListener listener) {
		if (text == null) {
			startListener = listener;
		}
		else {			
			text.addModifyListener(listener);
		}
	}
	
	public Widget getWidget() {
		return text;
	}

	@Override
	public void modifyText(ModifyEvent event) {
		text.setToolTipText( resolver != null ? resolver.resolve( text.getText()) : text.getText());
		broadcast( text.getText());
	}

	@Override
	public void acknowledgeOverrideChange() {
		modifyText(null);
	}

	
	
}
