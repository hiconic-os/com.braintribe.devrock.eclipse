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
package com.braintribe.plugin.commons.selection;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class SelectionServiceListener implements ISelectionListener {
	private ISelection currentSelection;
	private boolean attached;

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		//System.out.println( selection.getClass().getName());
		if (selection instanceof TreeSelection) {
			currentSelection = selection;
		}
	}
	
	public ISelection getSelection() {
		if (!attached) {
			attach();
			ISelectionService selectionService = getSelectionService();
			if (selectionService != null) {
				ISelection iSelection = selectionService.getSelection();
				if (iSelection instanceof TreeSelection) {
					currentSelection = iSelection;
				}
			}
		}
		return currentSelection;
	}
	
	private void attach() {
		if (attached)
			return;
		ISelectionService selectionService = getSelectionService();
		if (selectionService != null) {
			selectionService.addSelectionListener( this);
			attached = true;
		}		
	}

	private ISelectionService getSelectionService() {
		IWorkbench iworkbench = PlatformUI.getWorkbench();
		IWorkbenchWindow iworkbenchwindow = iworkbench.getActiveWorkbenchWindow();
		if (iworkbenchwindow != null) {
			ISelectionService selectionService = iworkbenchwindow.getSelectionService();
			return selectionService;
		}
		return null;
	}
	
	public void detach() {
		if (!attached) {
			return;
		}
		ISelectionService selectionService = getSelectionService();
		if (selectionService != null) {
			selectionService.removeSelectionListener(this);
		}
		attached = false;
	}

}
