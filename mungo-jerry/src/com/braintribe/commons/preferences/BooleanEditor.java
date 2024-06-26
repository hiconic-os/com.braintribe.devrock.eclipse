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
