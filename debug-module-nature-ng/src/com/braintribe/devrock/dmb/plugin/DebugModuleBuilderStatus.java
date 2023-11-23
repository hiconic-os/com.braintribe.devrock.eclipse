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
package com.braintribe.devrock.dmb.plugin;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * simple helper class to generate 'custom error messages' tied to the devrock plugin
 * @author pit
 *
 */
public class DebugModuleBuilderStatus extends Status implements IStatus {

	public DebugModuleBuilderStatus(String msg, int severity, Exception exception) {
		super( severity, DebugModuleBuilderPlugin.PLUGIN_ID, msg, exception);		
	}
	
	public DebugModuleBuilderStatus(String msg, Exception exception) {
		super( IStatus.ERROR, DebugModuleBuilderPlugin.PLUGIN_ID, msg, exception);		
	}

	public DebugModuleBuilderStatus(String msg, int severity) {
		super( severity, DebugModuleBuilderPlugin.PLUGIN_ID, msg);		
	}
	
	
}
