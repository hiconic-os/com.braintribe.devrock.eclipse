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
