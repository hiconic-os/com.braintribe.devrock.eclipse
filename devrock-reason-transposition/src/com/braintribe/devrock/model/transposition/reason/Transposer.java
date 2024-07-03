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
package com.braintribe.devrock.model.transposition.reason;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.braintribe.devrock.eclipse.model.resolution.nodes.ReasonNode;
import com.braintribe.devrock.model.mc.cfg.origination.Origination;
import com.braintribe.devrock.model.mc.reason.McReason;
import com.braintribe.gm.model.reason.Reason;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.Property;

public class Transposer {

	public ReasonNode transpose( Origination origination) {
		return transpose(origination, true);
	}
	
	public ReasonNode transpose( Reason reason) {
		return transpose(reason, false);
	}

	private ReasonNode transpose( Reason reason, boolean asOrigination) {
		ReasonNode node = ReasonNode.T.create();
		
		node.setBackingReason(reason);
		EntityType<GenericEntity> entityType = reason.entityType();
		node.setType( entityType.getShortName());
		node.setText( reason.getText());
		
		Set<String> standardReasonProperties = null;
		if (asOrigination) {
			standardReasonProperties = Origination.T.getProperties().stream().map( p -> p.getName()).collect(Collectors.toSet());
		}
		else {
			standardReasonProperties = McReason.T.getProperties().stream().map( p -> p.getName()).collect(Collectors.toSet());	
		}
		
		// properties
		List<Property> properties = entityType.getProperties();
		for (Property property : properties) {
			String name = property.getName();
			
			// filter any irrelevant - any standard properties of the base class is ignored
			if (standardReasonProperties.contains( name)) {
				continue;
			}
			// 
			Object value = property.get(reason);
			// transpose property value?? how?? toString()?					
		}
				
		// children 
		for (Reason child : reason.getReasons()) {
			node.getChildren().add( transpose(child, asOrigination));
		}
	
		
		return node;
	}
}
