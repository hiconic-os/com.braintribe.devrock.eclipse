// ============================================================================
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
package com.braintribe.devrock.commands.zed;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.braintribe.devrock.zed.ui.ViewingContextStorageHandler;
import com.braintribe.devrock.zed.ui.ZedResultViewer;
import com.braintribe.devrock.zed.ui.ZedViewingContext;

public class LoadAndDisplayAnalysisCommand extends AbstractHandler implements ZedRunnerTrait {
		
	private ViewingContextStorageHandler storagedHandler = new ViewingContextStorageHandler();

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException { 
		
		ZedViewingContext viewingContext = storagedHandler.load();
		
		if (viewingContext != null) {			
			// open dialog  
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			ZedResultViewer resultViewer = new ZedResultViewer( shell);
			resultViewer.setContext(viewingContext);
			resultViewer.open();
		}
		
		return null;
		
	}
	
	

}
