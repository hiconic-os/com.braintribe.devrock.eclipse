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

import com.braintribe.devrock.api.storagelocker.StorageLockerSlots;
import com.braintribe.devrock.eclipse.model.actions.VersionModificationAction;

public abstract class DependencyClipboardRelatedHelper implements StorageLockerSlots{
	
	public static final String STORAGE_SLOT_COPY_MODE = SLOT_CLIP_COPY_MODE;
	public static final String STORAGE_SLOT_PASTE_MODE = SLOT_CLIP_PASTE_MODE;

	public static String getAppropriateActionLabelRepresentation( VersionModificationAction action) {
		switch (action) {
		case rangified:
			return "and rangify ...";
		case referenced:
			return "and replace ...";
		case untouched:
		default:
			return "";
		}
	}
	public static String getAppropriateActionTooltipRepresentation( VersionModificationAction action) {
		switch (action) {
		case rangified:
			return "and rangify the version";
		case referenced:
			return "and replace the version with a variable";
		case untouched:
		default:
			return "and keep the version as is";
		}
	}
	
	
}
