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
package com.braintribe.commons.environment.properties;

import com.braintribe.build.artifact.virtualenvironment.VirtualPropertyResolver;
import com.braintribe.devrock.virtualenvironment.VirtualEnvironmentPlugin;


public class PropertyResolver implements VirtualPropertyResolver {	
	@Override
	public String getEnvironmentProperty(String key) {
		
		
		String value = null;
		if (isActive()) {
			value  = VirtualEnvironmentPlugin.getEnvironmentOverrides().get(key);			
		}
		if (value == null) {
			value = System.getenv(key);
		}
		return value;
	}

	@Override
	public String getSystemProperty(String key) {
		String value = null;
		if (isActive()) {
			value = VirtualEnvironmentPlugin.getPropertyOverrides().get(key);			
		}
		if (value == null) {
			value = System.getProperty(key);
		}
		return value;
	}

	@Override
	public boolean isActive() {
		return VirtualEnvironmentPlugin.getOverrideActivation();
	}
	
	@Override
	public String resolve(String expression) {
		if (!requiresEvaluation(expression))
			return expression;
		String value = expression;
		do {
			String rawKey = extract(value);
			// strip points.. here			
			String [] keys = rawKey.split( "\\.");
			String key = keys[0];
			if (keys.length > 1) {
				if (keys[0].equalsIgnoreCase("env"))
					key = keys[1];
				else
					key = rawKey;				
			}
			String v = getEnvironmentProperty(key);
			if (v == null) {
				v = getSystemProperty(key);
			}
			value = replace( rawKey, v, value);			
		} while ( requiresEvaluation( value));
		return value;
	}
	
	/**
	 * just checks if the expression contains ${..} somehow
	 * @param expression - the string to check 
	 * @return - true if a variable reference is in the string and false otherwise 
	 */
	protected boolean requiresEvaluation(String expression) {
		String extract = extract(expression);
		return !extract.equalsIgnoreCase(expression);
	}

	/**
	 * extracts the first variable in the expression 
	 * @param expression - the {@link String} to extract the variable from 
	 * @return - the first variable (minus the ${..} stuff)
	 */
	protected String extract(String expression) {
		int p = expression.indexOf( "${");
		if (p < 0)
			return expression;
		int q = expression.indexOf( "}", p+1);
		return expression.substring(p+2, q);
	}

	/**
	 * replaces any occurrence of the variable by its value 
	 * @param variable - without ${..}, it will be added 
	 * @param value - the value of the variable
	 * @param expression - the expression to replace in
	 * @return - the expression after the replace
	 */
	protected String replace(String variable, String value, String expression) {
		if (value != null) 
			return expression.replace( "${" + variable + "}", value);
		else
			return expression.replace( "${" + variable + "}", "<n/a>");
	}

}
