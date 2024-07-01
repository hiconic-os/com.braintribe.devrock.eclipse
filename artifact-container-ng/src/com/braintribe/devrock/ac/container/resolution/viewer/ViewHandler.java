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
package com.braintribe.devrock.ac.container.resolution.viewer;

import java.util.List;

import com.braintribe.devrock.eclipse.model.resolution.CapabilityKeys;
import com.braintribe.devrock.eclipse.model.resolution.nodes.Node;
import com.braintribe.devrock.eclipse.model.storage.TranspositionContext;

/**
 * handler that is instantiated per viewer, enriches and dispatches requests from the 
 * actual viewers, so that the {@link ContainerResolutionViewController} can deliver the requested data
 * @author pit
 *
 */
public class ViewHandler {
	private String key;	
	private ContainerResolutionViewController controller;
	private Node detailNode;
	
	public ViewHandler( String key, ContainerResolutionViewController controller) {
		this.key = key;
		this.controller = controller;		
	}
	
	public ViewHandler( String key, ContainerResolutionViewController controller, Node node) {
		this.key = key;
		this.controller = controller;
		this.detailNode = node;	
	}
	
	/**
	 * @param context - the current {@link TranspositionContext}
	 * @return - the corresponding {@link List} of {@link Node}s
	 */
	public List<Node> supplyNodes( TranspositionContext context) {
		if (detailNode != null) {
			return controller.supplyNodes(key, context, detailNode);	
		}
		else {
			return controller.supplyNodes(key, context, false);
		}
	}
	
	/**
	 * @param capKey - the {@link CapabilityKeys}
	 * @return - true if the view supports the capability
	 */
	public Boolean supplyCapability( CapabilityKeys capKey) {	
		return controller.supplyCapability( key, capKey);		
	}
}
