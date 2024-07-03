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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;

import com.braintribe.cfg.Configurable;

/**
 * lil' helper to create an editor that handles a boolean choice
 * @author pit
 *
 */
public class BooleanEditor extends AbstractEditor<Boolean> {
	private boolean startValue;
	private boolean startEnabled = true;
	private Button check;
		
	private SelectionListener selectionListener;
	
	@Configurable
	public void setSelectionListener(SelectionListener selectionListener) {
		this.selectionListener = selectionListener;
	}

	public Composite createControl( Composite parent, String tag) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		composite.setLayout(layout);
		
		Label label = new Label( composite, SWT.NONE);
		label.setText( tag);
		label.setLayoutData( new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
		if (labelToolTip != null) 
			label.setToolTipText(labelToolTip);
		
		check = new Button( composite, SWT.CHECK);
		check.setLayoutData( new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		check.setSelection(startValue);
		check.setEnabled(startEnabled);
		if (editToolTip != null) {
			check.setToolTipText(editToolTip);
		}
		if (selectionListener != null) {
			check.addSelectionListener(selectionListener);
		}
	 		
		return composite;
	}
	
	public Boolean getSelection() {
		return check.getSelection();
	}
	
	public void setSelection( Boolean selection){
		if (check == null) {
			startValue = selection;
		} else {
			check.setSelection( selection);
		}
	}
	
	public void setEnabled( boolean enabled) {
		if (check == null) {
			startEnabled = enabled;
		}
		else {
			check.setEnabled(enabled);
		}
	}	
	
	public Widget getWidget() {
		return check;
	}
	
	public Button getCheck() {
		return check;
	}
	
	
}
