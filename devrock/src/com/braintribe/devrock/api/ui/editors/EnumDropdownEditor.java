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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;

public class EnumDropdownEditor<T extends Enum<T>> extends AbstractEditor<T> {

		private Map<String, T> choicesMap = new HashMap<>();
		private Map<T, String> toolTipMap = new HashMap<>();
		private T startValue;
		private Combo combo;
		
		/**
		 * set the choices, a map of declarative texts to enum value 
		 * @param choices - a {@link Map} of {@link String} to Enum value
		 */
		@Required @Configurable
		public void setChoices( Map<String, T> choices) {
			choicesMap.putAll(choices);
		}

		@Configurable
		public void setToolTips( Map<T, String> tips) {
			toolTipMap = tips;
		}
		
		public void setSelection( T selection) {
			int i = 0;
			for (Map.Entry<String, T> entry : choicesMap.entrySet()) {
				if (entry.getValue() == startValue) {
					combo.select( i);
					break;
				}
				i++;				
			}
		}
		
		public T getSelection() {
			int selectedIndex = combo.getSelectionIndex();
			int i = 0;
			for (Map.Entry<String, T> entry : choicesMap.entrySet()) {
				if (i == selectedIndex) {
					return entry.getValue();
				}
				i++;
			}
			return null;			 
		}
		
		public Composite createControl( Composite parent, String tag, Font font) {
			Composite composite = new Composite(parent, SWT.NONE);
			GridLayout layout = new GridLayout();
			layout.numColumns = choicesMap.size();
			composite.setLayout(layout);
			if (tag != null) {
				Label label = new Label( composite, SWT.NONE);
				label.setText( tag);
				label.setLayoutData( new GridData(SWT.LEFT, SWT.CENTER, true, false, choicesMap.size(), 1));
				if (font != null) {
					label.setFont(font);
				}			
			}
			combo = new Combo(composite, SWT.NONE);
			combo.setItems( choicesMap.keySet().toArray(new String[0]));
						
			return composite;
		}

		@Override
		public void setEnabled(boolean value) {
			combo.setEnabled(value);	
		}
		
		
}
