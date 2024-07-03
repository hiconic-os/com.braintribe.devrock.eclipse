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
package com.braintribe.devrock.plugin;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.braintribe.gm.model.reason.Reason;

/**
 * simple helper class to generate 'custom error messages' tied to the devrock plugin
 * @author pit
 *
 */
public class DevrockPluginStatus extends Status implements IStatus {

	public DevrockPluginStatus(String msg, int severity, Exception exception) {
		super( severity, DevrockPlugin.PLUGIN_ID, msg, exception);		
	}
	
	public DevrockPluginStatus(String msg, Exception exception) {
		super( IStatus.ERROR, DevrockPlugin.PLUGIN_ID, msg, exception);		
	}

	public DevrockPluginStatus(String msg, int severity) {
		super( severity, DevrockPlugin.PLUGIN_ID, msg);		
	}
	
	public DevrockPluginStatus( Reason reason) {
		super(IStatus.ERROR, DevrockPlugin.PLUGIN_ID, reason.stringify());
	}
	
	public DevrockPluginStatus( String text, Reason reason) {
		super(IStatus.ERROR, DevrockPlugin.PLUGIN_ID, text  + reason.stringify());
	}
	
}
