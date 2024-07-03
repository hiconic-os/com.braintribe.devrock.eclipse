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
package com.braintribe.devrock.greyface.commands.upload;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.IViewDescriptor;
import org.eclipse.ui.views.IViewRegistry;

import com.braintribe.devrock.greyface.GreyfacePlugin;
import com.braintribe.devrock.greyface.GreyfaceStatus;
import com.braintribe.devrock.greyface.HasGreyfaceToken;

/**
 * command implementation to start the greyface view
 * 
 * @author pit
 *
 */
public class GreyfaceViewStarter extends AbstractHandler implements HasGreyfaceToken {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbench wb = PlatformUI.getWorkbench();
		IViewRegistry viewRegistry = wb.getViewRegistry();
		IViewDescriptor desc = viewRegistry.find( GreyfacePlugin.VIEW_ID);
		if (desc != null) {
			try {
				IViewPart viewPart = wb.getActiveWorkbenchWindow().getActivePage().showView( VIEW_ID);
				if (viewPart == null) {					
					String msg = "no view part returned for Greyface view with ID [" + VIEW_ID + "]";
					GreyfaceStatus status = new GreyfaceStatus( msg, IStatus.ERROR);
					GreyfacePlugin.getInstance().getLog().log(status);	
				}
			} catch (PartInitException e) {
				String msg = "cannot activate Greyface view with ID [" + VIEW_ID + "] as [" + e.getLocalizedMessage() + "]";
				GreyfaceStatus status = new GreyfaceStatus( msg, e);
				GreyfacePlugin.getInstance().getLog().log(status);
			}
		}		
		else {
			String msg = "cannot activate Greyface view with ID [" + VIEW_ID + "] as it's not present in the view registry";
			GreyfaceStatus status = new GreyfaceStatus( msg, IStatus.ERROR);
			GreyfacePlugin.getInstance().getLog().log(status);
		}
        return null;
	}
	
	
}
