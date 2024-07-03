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
package com.braintribe.devrock.mungojerry.dialog.tab;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import com.braintribe.build.gwt.Monitor;

public class GwtModuleScannerMonitor implements Monitor {
	
	private SubMonitor monitor;	
	
	public void setMonitor(IProgressMonitor monitor) {
		this.monitor = SubMonitor.convert(monitor);
	}

	@Override
	public void acknowledgeModule(String arg0) {
		monitor.setTaskName( arg0.toString());		
	}

	@Override
	public boolean isCancelled() {
		return monitor.isCanceled();
	}

	@Override
	public void acknowledgeTask(String msg, int expectedSteps) {	
		monitor.setTaskName( msg);
		monitor.setWorkRemaining(expectedSteps);		
		
	}

	@Override
	public void acknowledgeStep(String msg, int step) {
		monitor.subTask( msg);	
		monitor.split( 1);		
	}


	@Override
	public void acknowledgeSubStep(String msg) {
		monitor.subTask( msg);		
		monitor.split( 1);
	}

	@Override
	public void done() {
		monitor.done();
		
	}
	
	

}
