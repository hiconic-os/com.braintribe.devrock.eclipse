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
package com.braintribe.devrock.api.ui.viewers.reason.transpose;

import java.util.Collections;

import org.eclipse.jface.viewers.ITreeContentProvider;

import com.braintribe.devrock.eclipse.model.resolution.nodes.Node;
import com.braintribe.devrock.eclipse.model.resolution.nodes.ReasonNode;

/**
 * content provider for the tree viewer in the page
 * 
 * @author pit
 *
 */
public class ContentProvider implements ITreeContentProvider {
	
	private ReasonNode reasonNode;
	
	public void setupFrom(ReasonNode reasonNode) {
		this.reasonNode = reasonNode;	
	}
	

	@Override
	public Object[] getChildren(Object obj) {
		ReasonNode r = (ReasonNode) obj;
		return r.getChildren().toArray();
	}

	@Override
	public Object[] getElements(Object obj) {		
		return Collections.singletonList( reasonNode).toArray();				
	}

	private ReasonNode findParent( ReasonNode current, ReasonNode suspect) {
		if (current == suspect)
			return null;
		if (current.getChildren().contains(suspect))
			return current;
		for (Node child : current.getChildren()) {
			if (child instanceof ReasonNode) {
				ReasonNode p = findParent((ReasonNode) child, suspect);
				if (p == null)
					return p;
				}
		}
		return null;
	}
	
	@Override
	public Object getParent(Object obj) {
		ReasonNode r = (ReasonNode) obj;
		return findParent( reasonNode, r);
	}

	@Override
	public boolean hasChildren(Object obj) {
		ReasonNode r = (ReasonNode) obj;
		return r.getChildren().size() > 0;
	}
	

}
