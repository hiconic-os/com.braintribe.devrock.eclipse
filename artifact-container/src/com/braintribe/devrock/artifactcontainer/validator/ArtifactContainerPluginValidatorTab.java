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
package com.braintribe.devrock.artifactcontainer.validator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.braintribe.devrock.artifactcontainer.views.dependency.ValidationResultContentProvider;
import com.braintribe.plugin.commons.preferences.validator.ValidationResult;
import com.braintribe.plugin.commons.tableviewer.CommonTableColumnData;
import com.braintribe.plugin.commons.tableviewer.CommonTableViewer;

public class ArtifactContainerPluginValidatorTab {
	private ValidationResult result;
	private CommonTableViewer commonTableViewer;
	private Table repositoryTable;
	
	public ArtifactContainerPluginValidatorTab(ValidationResult result) {
		super();
		this.result = result;
	}


	public Composite createControl( Composite parent) {		
		
		Composite tableComposite = new Composite( parent, SWT.BORDER);	
		tableComposite.setLayout( new GridLayout());
		//tableComposite.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 1, 1));
		
    	commonTableViewer = new CommonTableViewer(tableComposite, SWT.V_SCROLL | SWT.SINGLE);
		CommonTableColumnData [] columnData = new CommonTableColumnData[1];
		columnData[0] = new CommonTableColumnData("message", 100, 100, result.getTooltip(), new NameColumnLabelProvider());
		
		commonTableViewer.setup(columnData);
		
		repositoryTable = commonTableViewer.getTable();
		repositoryTable.setHeaderVisible(false);
		repositoryTable.setLinesVisible(true);
		
	
		ValidationResultContentProvider contentProvider = new ValidationResultContentProvider();    
		contentProvider.setResults( result.getMessages());
		commonTableViewer.setContentProvider(contentProvider);
		commonTableViewer.setInput( result.getMessages());
	
		return tableComposite;			
	}
}
