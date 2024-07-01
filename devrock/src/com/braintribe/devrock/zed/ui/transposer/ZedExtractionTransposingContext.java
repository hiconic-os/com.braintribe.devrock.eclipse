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
package com.braintribe.devrock.zed.ui.transposer;

import java.util.Map;

import com.braintribe.devrock.zarathud.model.extraction.subs.ContainerNode;
import com.braintribe.zarathud.model.forensics.findings.ComparisonIssueType;

public class ZedExtractionTransposingContext implements HasContainerTokens {

	private boolean detailed = true;
	
	private ExtractionTransposer baseExtractionTransposer;
	private ExtractionTransposer otherExtractionTransposer;
	private ComparisonTransposer comparisonExtractionTransposer; 
	
	public boolean isDetailed() {
		return detailed;
	}

	public void setDetailed(boolean detailed) {
		this.detailed = detailed;
	}

	public ExtractionTransposer getBaseExtractionTransposer() {
		return baseExtractionTransposer;
	}

	public void setBaseExtractionTransposer(ExtractionTransposer baseExtractionTransposer) {
		this.baseExtractionTransposer = baseExtractionTransposer;
	}

	public ExtractionTransposer getOtherExtractionTransposer() {
		return otherExtractionTransposer;
	}

	public void setOtherExtractionTransposer(ExtractionTransposer otherExtractionTransposer) {
		this.otherExtractionTransposer = otherExtractionTransposer;
	}

	public ComparisonTransposer getComparisonExtractionTransposer() {
		return comparisonExtractionTransposer;
	}

	public void setComparisonExtractionTransposer(ComparisonTransposer comparisonExtractionTransposer) {
		this.comparisonExtractionTransposer = comparisonExtractionTransposer;
	}
	
	public ContainerNode identifyNode( Map<String,ContainerNode> map, ComparisonIssueType cit) {
		switch (cit) {
		
		case missingAnnotations:
		case surplusAnnotations:
			return map.get( ANNOTATIONS);
		case missingEnumValues:
		case surplusEnumValues:
			return map.get( VALUES);
		case missingFields:
		case surplusFields:
			return map.get( FIELDS);
		case missingImplementedInterfaces:
		case surplusImplementedInterfaces:
			return map.get( IMPLEMENTED_INTERFACES);
		case missingImplementingClasses:
		case surplusImplementingClasses:
			return map.get( IMPLEMENTING_TYPES);
		case missingInCollection:
		case surplusInCollection:
			break;
		case missingMethodArguments:
		case surplusMethodArguments:
			return map.get( ARGUMENT_TYPES);		
		case missingMethodExceptions:
		case surplusMethodExceptions:
			return map.get(THROWN_EXCEPTIONS);
		case missingMethods:
		case surplusMethods:
			return map.get( METHODS);
		case missingSubInterfaces:
		case surplusSubInterfaces:
			break;			
		case missingSubTypes:
		case surplusSubTypes:
			return map.get( DERIVED_TYPES);
		case missingSuperInterfaces:		
		case surplusSuperInterfaces:
			return map.get( SUPER_INTERFACES);			
		default:
			break;
		
		}
		return null;
	}
	
	
	
}
