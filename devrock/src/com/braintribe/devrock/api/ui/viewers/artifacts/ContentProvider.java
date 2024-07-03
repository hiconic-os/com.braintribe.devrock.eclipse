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
package com.braintribe.devrock.api.ui.viewers.artifacts;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.devrock.eclipse.model.resolution.nodes.Node;


/**
 * {@link ITreeContentProvider} for the {@link TransposedAnalysisArtifactViewer}
 * @author pit
 *
 */
public class ContentProvider implements ITreeContentProvider {

	private List<Node> nodes;
	
	@Configurable @Required
	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}
	
	List<Node> getCurrentNodes() {
		return nodes;
	}
			
	@Override
	public Object[] getChildren(Object arg0) {
		Node node = (Node) arg0;
		
		return node.getChildren().toArray();
		
	}

	@Override
	public Object[] getElements(Object arg0) {
		return nodes.toArray();
	}

	@Override
	public Object getParent(Object arg0) {
		return null;
	}

	@Override
	public boolean hasChildren(Object arg0) {		
		Object[] children = getChildren(arg0);
		return children != null && children.length > 0;
	}
	

}
