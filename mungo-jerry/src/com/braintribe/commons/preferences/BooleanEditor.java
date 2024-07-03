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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class BooleanEditor {
	private boolean startValue;
	private boolean startEnabled = true;
	private Button check;

	public Composite createControl( Composite parent, String tag) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		composite.setLayout(layout);
		
		Label label = new Label( composite, SWT.NONE);
		label.setText( tag);
		label.setLayoutData( new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
		
		check = new Button( composite, SWT.CHECK);
		check.setLayoutData( new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		check.setSelection(startValue);
		check.setEnabled(startEnabled);
		return composite;
	}
	
	public Boolean getSelection() {
		return check.getSelection();
	}
	
	public void setSelection( boolean selection){
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
	
}
