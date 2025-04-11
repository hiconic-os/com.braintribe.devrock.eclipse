package com.braintribe.devrock.api.ui.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class DoubleEditor extends AbstractEditor<Double> {

	private Double value;
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
		if (value != null) {
			text.setText( Double.toString(value));
		}
		if (editToolTip != null) {
			text.setToolTipText(editToolTip);
		}
		return composite;
	}
	
	
	@Override
	public Double getSelection() {
		return Double.valueOf(text.getText());
	}

	@Override
	public void setSelection(Double selection) {
		value = selection;
		if (text != null) {
			text.setText( "" + selection);
		}	
	}

	@Override
	public void setEnabled(boolean value) {
		text.setEnabled(value);
	}

}
