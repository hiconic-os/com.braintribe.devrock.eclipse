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
package com.braintribe.devrock.ac.container.plugin.listener;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;

import com.braintribe.devrock.api.storagelocker.StorageLockerSlots;
import com.braintribe.devrock.plugin.DevrockPlugin;

/**
 * a {@link IResourceChangeListener} that can choose between the two {@link IResourceChangeListener} implemented
 * 
 * @author pit
 */
public class CompoundResourceChangeListener implements IResourceChangeListener {
	private IResourceChangeListener defaultListener = new DefaultResourceChangeListener();
	private IResourceChangeListener advancedListener = new AdvancedResourceChangeListener();

	@Override
	public void resourceChanged(IResourceChangeEvent event) {		
		boolean useAdvancedListener = DevrockPlugin.instance().storageLocker().getValue( StorageLockerSlots.SLOT_ADVANCED_RC_LISTENER, false);
			
		if (useAdvancedListener) {
			advancedListener.resourceChanged(event);
		}
		else {
			defaultListener.resourceChanged(event);
		}		
	}

}
