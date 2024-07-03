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
package com.braintribe.devrock.greyface.settings.preferences.tableviewer;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.braintribe.model.malaclypse.cfg.repository.RemoteRepository;

public class UserColumnLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		RemoteRepository setting = (RemoteRepository) element;			
		return setting.getUser();
	}

	@Override
	public String getToolTipText(Object element) {
		RemoteRepository setting = (RemoteRepository) element;	
		return "the user name required to access the remote repository [" + setting.getName() +"] @[" + setting.getUrl() + "] if required";
	}

}
