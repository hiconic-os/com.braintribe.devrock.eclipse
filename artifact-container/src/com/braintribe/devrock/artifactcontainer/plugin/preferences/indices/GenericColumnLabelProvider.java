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
package com.braintribe.devrock.artifactcontainer.plugin.preferences.indices;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.braintribe.model.ravenhurst.interrogation.RavenhurstBundle;

public class GenericColumnLabelProvider extends ColumnLabelProvider {

	private int columnId;
	
	public GenericColumnLabelProvider(int id) {
		this.columnId = id;
	}

	@Override
	public String getText(Object element) {
		RavenhurstBundle bundle = (RavenhurstBundle) element;
		switch ( columnId) {
			case 0: // name
				return bundle.getRepositoryId();
			case 1: // last updated 
				return bundle.getDate().toString();
			case 2: // url
				return bundle.getRepositoryUrl();
			case 3:
				return bundle.getDynamicRepository() ? "dynamic" : "maven";								
		}
		return super.getText(element);
	}

	@Override
	public String getToolTipText(Object element) {		
		switch ( columnId) {
			case 0: // name
				return "the id of the repository";
			case 1: // last updated 
				return "last date as sent by RH";
			case 2: // url
				return "the url the repository is connected to";
			case 3:
				return "what kind of update policy the repository supports";								
		}
		return super.getToolTipText(element);
	}
	
	
}
