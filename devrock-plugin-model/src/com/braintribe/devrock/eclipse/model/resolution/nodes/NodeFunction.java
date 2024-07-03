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

import com.braintribe.model.generic.base.EnumBase;
import com.braintribe.model.generic.reflection.EnumType;
import com.braintribe.model.generic.reflection.EnumTypes;

/**
 * simple way to classify the node via its function, to assign icons for instance
 * @author pit
 *
 */
public enum NodeFunction implements EnumBase{
	standard, imports, import_owning_parent, parent, depender, terminal, clash, clashWinner, clashLoser, dependency;
	
	
	final EnumType T = EnumTypes.T(NodeFunction.class);

	@Override
	public EnumType type() {	
		return T;
	}
	
	
}
