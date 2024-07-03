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
package com.braintribe.devrock.greyface.views.dependency.tabs.capability.clipboard;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import com.braintribe.devrock.greyface.view.tab.GenericViewTab;
import com.braintribe.devrock.greyface.views.dependency.tabs.capability.AbstractDependencyViewActionContainer;
import com.braintribe.plugin.commons.views.actions.ViewActionContainer;
import com.braintribe.plugin.commons.views.actions.ViewActionController;

public class ClipboardCopyActionContainer extends AbstractDependencyViewActionContainer implements ViewActionContainer<GenericViewTab>, ViewActionController<GenericViewTab> {

	private Action copyToClipboardAction;
	private Clipboard clipboard;
	
	@Override
	public void controlAvailablity(GenericViewTab tab) {
		if (tab instanceof ClipboardCopyCapable) {
			copyToClipboardAction.setEnabled(true);
		}
		else {
			copyToClipboardAction.setEnabled(false);
		}		
	}

	@Override
	public ViewActionController<GenericViewTab> create() {
		clipboard = new Clipboard(display);
		
		ImageDescriptor clipboardCopyImageDescriptor = ImageDescriptor.createFromFile( ClipboardCopyActionContainer.class, "copy_edit.gif");
	
		copyToClipboardAction = new Action("copy contents to clipboard", clipboardCopyImageDescriptor) {		
			@Override
			public void run() {				
				GenericViewTab viewTab = activeTabProvider.provideActiveTab();
				if (viewTab instanceof ClipboardCopyCapable) {					
					ClipboardCopyCapable clipboardCapable = (ClipboardCopyCapable) viewTab; 
					String contents = clipboardCapable.copyContents();
					if (contents == null) {
						return;
					}
					TextTransfer textTransfer = TextTransfer.getInstance();			
					clipboard.setContents( new String[] { contents}, new Transfer[] { textTransfer});
				}
			}
			
		};
		toolbarManager.add(copyToClipboardAction);
		menuManager.add( copyToClipboardAction);
		return this;
	}

}
