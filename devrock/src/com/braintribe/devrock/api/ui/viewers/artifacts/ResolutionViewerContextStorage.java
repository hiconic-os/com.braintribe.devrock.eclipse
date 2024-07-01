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
package com.braintribe.devrock.api.ui.viewers.artifacts;

import com.braintribe.devrock.api.storagelocker.StorageLockerSlots;
import com.braintribe.devrock.eclipse.model.storage.TranspositionContext;
import com.braintribe.devrock.eclipse.model.storage.ViewContext;
import com.braintribe.devrock.eclipse.model.storage.ViewerContext;
import com.braintribe.devrock.plugin.DevrockPlugin;

public class ResolutionViewerContextStorage {
	/**
	 * @return - a {@link ViewContext} as stored in the storage slots 
	 */
	public static ViewerContext loadViewContextFromStorage(String key) {
		ViewContext viewContext = DevrockPlugin.instance().storageLocker().getValue( StorageLockerSlots.SLOT_ARTIFACT_VIEWER_TC_MAP_KEY, ViewContext.T.create());
		ViewerContext vc = viewContext.getViewerContexts().get( key);
		if (vc == null) {
			vc = ViewerContext.T.create();
		}								
		return vc;
	}
	
	/**
	 * @param vc - the {@link ViewContext} to store in the storage locker
	 */
	public static void storeViewContextInStorage(String key, ViewerContext vc) {
		boolean store = DevrockPlugin.instance().storageLocker().getValue( StorageLockerSlots.SLOT_ARTIFACT_VIEWER_STORE_VIEW_SETTINGS, false);
		if (store == false) 
			return;
		ViewContext viewContext = DevrockPlugin.instance().storageLocker().getValue( StorageLockerSlots.SLOT_ARTIFACT_VIEWER_TC_MAP_KEY, ViewContext.T.create());		
		viewContext.getViewerContexts().put( key, vc);
		DevrockPlugin.instance().storageLocker().setValue( StorageLockerSlots.SLOT_ARTIFACT_VIEWER_TC_MAP_KEY, viewContext);
	}
	
	/**
	 * @param context - the {@link TranspositionContext} to store
	 */
	public static void storeTranspositionContextToStorage(String key, TranspositionContext context) {		
		boolean store = DevrockPlugin.instance().storageLocker().getValue( StorageLockerSlots.SLOT_ARTIFACT_VIEWER_STORE_VIEW_SETTINGS, false);
		if (store == false) 
			return;
		context.setKey(key);
		ViewContext viewContext = DevrockPlugin.instance().storageLocker().getValue( StorageLockerSlots.SLOT_ARTIFACT_VIEWER_TC_MAP_KEY, ViewContext.T.create());
		viewContext.getTranspositionContexts().put(key, context);
		DevrockPlugin.instance().storageLocker().setValue( StorageLockerSlots.SLOT_ARTIFACT_VIEWER_TC_MAP_KEY, viewContext);
	}
	/**
	 * @return - the {@link TranspositionContext} loaded, and empty (default) one if none's stored
	 */
	public static TranspositionContext loadTranspositionContextFromStorage(String key) {		
		ViewContext viewContext = DevrockPlugin.instance().storageLocker().getValue( StorageLockerSlots.SLOT_ARTIFACT_VIEWER_TC_MAP_KEY, ViewContext.T.create());
		TranspositionContext tc = viewContext.getTranspositionContexts().get( key);
		if (tc == null) {
			tc = TranspositionContext.T.create();
		}
		return tc;
	}
	
	
}
