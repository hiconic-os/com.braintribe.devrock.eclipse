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
package com.braintribe.plugin.commons.ui.tree;

import java.util.Comparator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;


public class TreeItemComparator implements Comparator<TreeItem> {

	int direction = SWT.UP;
	int [] indices;
	int numColumns = 0;
	
	public TreeItemComparator( int mode, int index, int numColumns) {
		direction = mode;
		indices = new int[1];
		indices[0] = index;
		this.numColumns = numColumns;
	}
	
	public TreeItemComparator( int mode, int [] indices, int numColumns) {
		direction = mode;
		this.indices = indices;
		this.numColumns = numColumns;
	}
	@Override
	public int compare(TreeItem o1, TreeItem o2) {
		
		String [] values1 = TreeSortHelper.getColumnValues( o1, numColumns);
		String [] values2 = TreeSortHelper.getColumnValues( o2, numColumns);
		
		for (int index = 0; index < indices.length; index++) {
			String value1 = values1[ indices[index]];
			String value2 = values2[ indices[index]];
	
			int retval = value1.compareToIgnoreCase( value2);
			
			if (retval != 0) {
				switch (direction) {
					case SWT.UP:
						return retval;
					case SWT.DOWN:
						return retval*-1;
				}
			}
			
		}
		
		return 0;
	}
	
	
}
