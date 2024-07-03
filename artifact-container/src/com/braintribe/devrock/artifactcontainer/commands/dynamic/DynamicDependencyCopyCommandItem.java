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
package com.braintribe.devrock.artifactcontainer.commands.dynamic;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.braintribe.devrock.artifactcontainer.ArtifactContainerPlugin;
import com.braintribe.devrock.artifactcontainer.commands.DependencyToClipboardCopyCommand;
import com.braintribe.model.malaclypse.cfg.preferences.ac.qi.VersionModificationAction;


public class DynamicDependencyCopyCommandItem extends ContributionItem {
	private Image image;
	
	public DynamicDependencyCopyCommandItem() {
		ImageDescriptor dsc = org.eclipse.jface.resource.ImageDescriptor.createFromFile( DynamicDependencyCopyCommandItem.class, "copyToClipboard.png");
		image = dsc.createImage();
	}
	
	public DynamicDependencyCopyCommandItem(String id) {
		super( id);
	}
	

	@Override
	public void fill(Menu menu, int index) {
		VersionModificationAction copyMode = ArtifactContainerPlugin.getInstance().getArtifactContainerPreferences(false).getQuickImportPreferences().getLastDependencyCopyMode();
		MenuItem menuItem = new MenuItem(menu, SWT.CHECK, index);
	    menuItem.setText("Copy dependency declaration(s) " + DependencyClipboardRelatedHelper.getAppropriateActionLabelRepresentation(copyMode));
	    menuItem.setToolTipText( "Copy the selected dependency declaration(s) " +  DependencyClipboardRelatedHelper.getAppropriateActionTooltipRepresentation(copyMode));
	    menuItem.setImage(  image);
	    
	    menuItem.addSelectionListener(new SelectionAdapter() {
	            public void widgetSelected(SelectionEvent e) {
	            	DependencyToClipboardCopyCommand cmd = new DependencyToClipboardCopyCommand();
	            	cmd.process( null);
	            }
	        });
		
	}

	@Override
	public void dispose() {
		image.dispose();
		super.dispose();
	}
	
	
	
	

}
