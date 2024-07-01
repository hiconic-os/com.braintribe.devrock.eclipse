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
package com.braintribe.plugin.commons.ui.tree;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public class TreeSortSelectionListener implements SelectionListener {

	private IWizardContainer iWizardContainer = null;
	private TreeItemPainter painter = null;
	

	
	public TreeSortSelectionListener( IWizardContainer container) {
		iWizardContainer = container;
	}
			
	public void setPainter(TreeItemPainter painter) {
		this.painter = painter;
	}
	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void widgetSelected(SelectionEvent event) {
		sortTree( event);
	}

	private void sortTree( final SelectionEvent event) {
		
		IRunnableWithProgress job = new IRunnableWithProgress() {
			
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				
				monitor.beginTask( "sorting", IProgressMonitor.UNKNOWN);
				
				TreeColumn column = (TreeColumn) event.widget;
				Tree tree = column.getParent();
		
				TreeColumn sortColumn = tree.getSortColumn();
				TreeColumn columns[] = tree.getColumns();
				tree.setSortColumn(column);
				int numOfColumns = columns.length;
				int columnIndex = findColumnIndex(columns, column, numOfColumns);
		
				
				 if (
						 (column.equals(sortColumn)) && 				 
						 (tree.getSortDirection() == SWT.UP)
					) {
					 tree.setSortDirection(SWT.DOWN);
					 sortItems( tree, columnIndex, SWT.UP, numOfColumns);
				 } else {
					 tree.setSortDirection(SWT.UP);
					 sortItems( tree, columnIndex, SWT.DOWN, numOfColumns);
				 }

				 monitor.done();
			}
		};
		try {
			iWizardContainer.run( false, false, job);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


	}
	
	private int findColumnIndex(TreeColumn[] columns, TreeColumn column, int numOfColumns) {
			int index = 0;
			for (int i = 0; i < numOfColumns; i++) {
				if (column.equals(columns[i])) {
					index = i;
					break;
				}
			}
	    return index;
	}
	
	
	 
	 private void sortItems( Tree tree, int index, int mode, int numColumns) {
		 TreeItem [] items = tree.getItems();
		
		 List<TreeItem> itemCollection = new ArrayList<TreeItem>( items.length);
		 for (TreeItem item : items)
			 itemCollection.add( item);
		 TreeItemComparator comparator = new TreeItemComparator( mode, index, numColumns);
		 Collections.sort( itemCollection, comparator);
		 
		
		 // now rearrange 
		 for (TreeItem item : itemCollection) {
			
		 	 TreeItem [] subItems = item.getItems();
			 TreeItem newItem = new TreeItem( tree, SWT.NONE);
			 if (painter != null)
				 painter.paint( newItem, item);
			 newItem.setText( TreeSortHelper.getColumnValues( item, numColumns));			 
			 if (subItems.length > 0) {
				 sortSubItems( subItems, newItem, index, mode, numColumns);	
			 }				 
			 
			item.dispose();
		 }
		 
	 }

	 private void sortSubItems( TreeItem[] items, TreeItem parent, int index, int mode, int numColumns) {
		 
		 // sort list of subitems 
		 List<TreeItem> itemCollection = new ArrayList<TreeItem>( items.length);
		 for (TreeItem item : items)
			 itemCollection.add( item);
		 
		 TreeItemComparator comparator = new TreeItemComparator( mode, index, numColumns);
		 Collections.sort( itemCollection, comparator);
		 
		
		 // now add subitems in the correct order 
		 for (TreeItem item : itemCollection) {
			 TreeItem [] subitems = item.getItems();
			 TreeItem newItem = new TreeItem( parent, SWT.NONE);
			 if (painter != null)
				 painter.paint( newItem, item);
			 newItem.setText( TreeSortHelper.getColumnValues( item, numColumns));
			 if (subitems.length > 0) {
				 sortSubItems( subitems, newItem, index, mode, numColumns);	
			 }
			 item.dispose();			
		 }
		
	 }
	 
	 

}
