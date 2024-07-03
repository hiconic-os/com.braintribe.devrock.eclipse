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
package com.braintribe.devrock.preferences.pages.scan;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.braintribe.devrock.eclipse.model.scan.SourceRepositoryEntry;

public class NameColumnLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		SourceRepositoryEntry pairing = (SourceRepositoryEntry) element;
		return pairing.getKey();
	}

	@Override
	public String getToolTipText(Object element) {
		SourceRepositoryEntry pairing = (SourceRepositoryEntry) element;
		return "display name of source repository pairing [" + pairing.getActualFile() + "]"; 
	}

}
