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

import org.eclipse.jface.viewers.TableViewer;

import com.braintribe.model.malaclypse.cfg.repository.RemoteRepository;

public class UrlColumnEditingSupport extends AbstractColumnEditingSupport {

	private TableViewer viewer;

	public UrlColumnEditingSupport(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
	}

	@Override
	protected Object getValue(Object element) {
		RemoteRepository setting = (RemoteRepository) element;		
		return setting.getUrl();
	}

	@Override
	protected void setValue(Object element, Object value) {
		RemoteRepository setting = (RemoteRepository) element;
		setting.setUrl((String) value);
		viewer.refresh( element);
	}

}
