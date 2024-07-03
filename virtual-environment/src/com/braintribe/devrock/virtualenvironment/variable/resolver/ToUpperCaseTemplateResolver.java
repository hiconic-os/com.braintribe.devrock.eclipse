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
package com.braintribe.devrock.virtualenvironment.variable.resolver;

import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateVariable;
import org.eclipse.jface.text.templates.TemplateVariableResolver;

public class ToUpperCaseTemplateResolver extends TemplateVariableResolver {
	public ToUpperCaseTemplateResolver() {
		super( "toUpper", "converts first char to uppercase");
	}

	@Override
	public void resolve(TemplateVariable variable, TemplateContext context) {
		String [] values = variable.getValues();
		if (values != null) {
			String [] nv = new String[ values.length];
			for (int i = 0; i < values.length; i++) {
				String v = values[i];
				v = v.substring(0, 1).toUpperCase() + v.substring(1);
				nv[i] = v; 
			}
			variable.setValues(nv);
		}
		
		
		super.resolve(variable, context);
	}

	
}
	
