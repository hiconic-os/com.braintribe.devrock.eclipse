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
package com.braintribe.devrock.greyface.generics.tree;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class TreeExpander {
	
	private List<Listener> expansionListeners = new ArrayList<Listener>();
	
	public static void expand( Tree tree) {
		TreeExpander expander = new TreeExpander();
		expander.expandTree(tree, true);
	}
	
	public static void collapse( Tree tree) {
		TreeExpander expander = new TreeExpander();
		expander.expandTree(tree, false);
	}
	
	

	public  void expandTree( Tree tree, boolean expand) {
		for (TreeItem item : tree.getItems()) {
			expandTree( item, expand);
		}
	}
	
	private  void expandTree( TreeItem item, boolean expand) {
		if (expand == true) {
			// 
			if (expansionListeners.isEmpty() == false) {
				Event event = new Event();
				event.item = item;				
				event.type = SWT.Expand;
				for (Listener listener : expansionListeners) {
					listener.handleEvent( event);
				}
			}				
		}
		item.setExpanded( expand);
		for (TreeItem child : item.getItems()) {
			expandTree( child, expand);
		}
	}
	
	public void addListener( Listener listener) {
		expansionListeners.add( listener);
	}
	
	public void removeListener( Listener listener) {
		expansionListeners.remove( listener);
	}
}
