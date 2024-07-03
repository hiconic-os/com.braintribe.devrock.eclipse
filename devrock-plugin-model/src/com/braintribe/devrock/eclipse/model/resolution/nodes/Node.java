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
package com.braintribe.devrock.eclipse.model.resolution.nodes;

import java.util.List;

import com.braintribe.gm.model.reason.Reason;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface Node extends GenericEntity{
		
	EntityType<Node> T = EntityTypes.T(Node.class);
	
	String children = "children";
	String reason = "reason";
	String function = "function";
	String archetype = "archetype";
	String topLevel = "topLevel";
	String isAProject = "isAProject";

	/**
	 * @return - a {@link List} of {@link Node} attached
	 */
	List<Node> getChildren();
	void setChildren(List<Node> value);
	
	
	boolean getTopLevel();
	void setTopLevel(boolean value);

	/**
	 * @return - true if this node represents a project
	 */
	boolean getIsAProject();
	void setIsAProject(boolean value);

		
	/**
	 * @return - the {@link Reason} if anything's wrong, especially in the incomplete artifact tab
	 */
	Reason getReason();
	void setReason(Reason value);

	/**
	 * @return - the {@link NodeFunction} that describes the 'functional value', i.e. dependency, depender etc 
	 */
	NodeFunction getFunction();
	void setFunction(NodeFunction value);
	
	/**
	 * @return - the {@link NodeArchetype} that describes the type, i.e. jar or pom 
	 */
	NodeArchetype getArchetype();
	void setArchetype(NodeArchetype value);
	
	
}
