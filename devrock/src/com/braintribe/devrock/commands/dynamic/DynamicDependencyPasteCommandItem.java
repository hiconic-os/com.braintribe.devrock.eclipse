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
package com.braintribe.devrock.commands.dynamic;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.braintribe.devrock.api.selection.SelectionExtracter;
import com.braintribe.devrock.api.storagelocker.StorageLockerSlots;
import com.braintribe.devrock.api.ui.commons.UiSupport;
import com.braintribe.devrock.commands.DependencyFromClipboardPasteCommand;
import com.braintribe.devrock.eclipse.model.actions.VersionModificationAction;
import com.braintribe.devrock.plugin.DevrockPlugin;
import com.braintribe.devrock.plugin.DevrockPluginStatus;
import com.braintribe.logging.Logger;

public class DynamicDependencyPasteCommandItem extends ContributionItem {
	private static Logger log = Logger.getLogger(DynamicDependencyPasteCommandItem.class);
	private Image image;
	private UiSupport uisupport = DevrockPlugin.instance().uiSupport();
	
	public DynamicDependencyPasteCommandItem() {
		//ImageDescriptor dsc = org.eclipse.jface.resource.ImageDescriptor.createFromFile( DynamicDependencyPasteCommandItem.class, "pasteFromClipboard.png");
		//image = dsc.createImage();
		image = uisupport.images().addImage( "cmd-dep-paste", DynamicDependencyPasteCommandItem.class, "pasteFromClipboard.png");
	}
	
	public DynamicDependencyPasteCommandItem(String id) {
		super( id);
	}
	

	@Override
	public void fill(Menu menu, int index) {
		long before = System.currentTimeMillis();
		VersionModificationAction pasteMode   = DevrockPlugin.envBridge().storageLocker().getValue( DependencyClipboardRelatedHelper.STORAGE_SLOT_PASTE_MODE, VersionModificationAction.referenced);
				
		IProject target = SelectionExtracter.currentProject();
		if (target == null) {			
			return;
		}
		
		MenuItem menuItem = new MenuItem(menu, SWT.CHECK, index);
	    menuItem.setText("Paste dependency declaration(s) to " + target.getName() + " " + DependencyClipboardRelatedHelper.getAppropriateActionLabelRepresentation(pasteMode));
	    menuItem.setToolTipText( "Paste the dependency declaration(s) from the clipboard into the pom of " +  target.getName() + " " + DependencyClipboardRelatedHelper.getAppropriateActionTooltipRepresentation(pasteMode));
	    menuItem.setImage(  image);
	    
	    menuItem.addSelectionListener(new SelectionAdapter() {
	            public void widgetSelected(SelectionEvent e) {	            	
	            	DependencyFromClipboardPasteCommand cmd = new DependencyFromClipboardPasteCommand( target);
	            	cmd.process( pasteMode);	            		            	
	            }
	        });
	    
	    long after = System.currentTimeMillis();
	    long delay = after - before;

	    if (log.isDebugEnabled()) {
			log.debug( getClass().getName() + " : " + delay  + "ms");
	    }

	    long maxDelay = DevrockPlugin.envBridge().storageLocker().getValue( StorageLockerSlots.SLOT_DYNAMIC_COMMAND_MAX_DELAY, StorageLockerSlots.DEFAULT_DYNAMIC_COMMAND_MAX_DELAY);
	    if (delay > maxDelay) {
			DevrockPluginStatus status = new DevrockPluginStatus( "dynamic command took too long to setup [" + delay + " ms] :" + getClass().getName(), IStatus.WARNING);
			DevrockPlugin.instance().log(status);	
	    }
	}

	@Override
	public void dispose() {
		//image.dispose();
		super.dispose();
	}
	
	
	
	

}
