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
package com.braintribe.devrock.api.ui.viewers.reason;

import java.util.Collections;

import org.eclipse.jface.viewers.ITreeContentProvider;

import com.braintribe.gm.model.reason.Reason;

/**
 * content provider for the tree viewer in the page
 * 
 * @author pit
 *
 */
public class ContentProvider implements ITreeContentProvider {
	
	private Reason reason;
	
	public void setupFrom(Reason reason) {
		this.reason = reason;	
	}
	

	@Override
	public Object[] getChildren(Object obj) {
		Reason r = (Reason) obj;
		return r.getReasons().toArray();
	}

	@Override
	public Object[] getElements(Object obj) {		
		return Collections.singletonList( reason).toArray();
		//return getChildren(obj);
		
	}

	private Reason findParent( Reason current, Reason suspect) {
		if (current == suspect)
			return null;
		if (current.getReasons().contains(suspect))
			return current;
		for (Reason child : current.getReasons()) {
			Reason p = findParent(child, suspect);
			if (p == null)
				return p;
		}
		return null;
	}
	
	@Override
	public Object getParent(Object obj) {
		Reason r = (Reason) obj;
		return findParent( reason, r);
	}

	@Override
	public boolean hasChildren(Object obj) {
		Reason r = (Reason) obj;
		return r.getReasons().size() > 0;
	}
	

}
