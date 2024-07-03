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
package com.braintribe.devrock.api.nature;

/**
 * a container for natures that multiple plugins need to agree on
 * @author pit
 *
 */
public class CommonNatureIds {
	// gwt library nature from MungoJerry 
	public static final String NATURE_GWT_LIBRARY = "com.braintribe.devrock.mj.natures.GwtLibraryNature";

	// gwt terminal nature from MungoJerry
	public static final String NATURE_GWT_TERMINAL = "com.braintribe.devrock.mj.natures.GwtTerminalNature";
	
	// model nature from ModelBuilder
	public static final String NATURE_MODEL = "com.braintribe.eclipse.model.nature.ModelNature";
	
	// debug module nature (tribefire services)
	public static final String NATURE_DEBUG_MODULE = "com.braintribe.devrock.artifactcontainer.natures.TribefireServicesNature";
	
	public static final String NATURE_ARTIFACT_REFLECTION = "com.braintribe.devrock.arb.nature.ArtifactReflectionNature";
	
	// Tomcat nature of old SysDeo plugin
	public static final String NATURE_TOMCAT_SYSDEO = "com.sysdeo.eclipse.tomcat.tomcatnature";
	
	// Tomcat nature of current Eclipse Tomcat Plugin
	public static final String NATURE_TOMCAT_NETSF = "net.sf.eclipse.tomcat.tomcatnature";

	// jdt core java nature
	public static final String NATURE_JAVA = "org.eclipse.jdt.core.javanature";
	
	
	
}
