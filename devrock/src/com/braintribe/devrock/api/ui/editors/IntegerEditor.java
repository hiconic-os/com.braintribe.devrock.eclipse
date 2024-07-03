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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

public class IntegerEditor extends AbstractEditor<Integer> {
	private Integer start;
	private Text text;
	


	public Composite createControl( Composite parent, String tag) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		composite.setLayout(layout);
		
		Label label = new Label( composite, SWT.NONE);
		label.setText( tag);
		label.setLayoutData( new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
		if (labelToolTip != null) {
			label.setToolTipText(labelToolTip);
		}
		
		text = new Text( composite, SWT.NONE);
		text.setLayoutData( new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		if (start != null) {
			text.setText( start.toString());
		}
		if (editToolTip != null) {
			text.setToolTipText(editToolTip);
		}
		return composite;
	}
	
	public Integer getSelection() {
		return Integer.valueOf(text.getText());
	}
	
	public void setSelection( Integer selection){
		start = selection;
		if (text != null) {
			text.setText( "" + selection);
		}	
	}
	
	public Widget getWidget() {
		return text;
	}

	@Override
	public void setEnabled(boolean value) {	
		text.setEnabled(value);
	}
	
	
}
