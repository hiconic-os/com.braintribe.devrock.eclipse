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
package com.braintribe.devrock.tbrunner;

import org.eclipse.core.runtime.IProgressMonitor;

import com.braintribe.devrock.api.console.ConsoleLogger;
import com.braintribe.devrock.api.process.listener.MessageType;
import com.braintribe.devrock.api.process.listener.ProcessNotificationListener;

public class MessageMonitorBridge implements ProcessNotificationListener {
	private IProgressMonitor monitor = null;
	private ConsoleLogger consoleLogger = null;
			
	public IProgressMonitor getMonitor() {
		return monitor;
	}
	
	public void setMonitor(IProgressMonitor monitor) {
		this.monitor = monitor;
	}
	
	public MessageMonitorBridge( String consoleName, IProgressMonitor monitor) {
		this.monitor = monitor;
		consoleLogger = new ConsoleLogger( consoleName);		
	}	
	
	@Override
	public void acknowledgeProcessNotification(MessageType arg0, String msg) {
		monitor.beginTask( msg, IProgressMonitor.UNKNOWN);
		if (consoleLogger != null) {
			consoleLogger.log(msg);	
		}
	}
}
