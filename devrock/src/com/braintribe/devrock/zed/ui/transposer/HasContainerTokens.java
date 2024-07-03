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
package com.braintribe.devrock.zed.ui.transposer;

/**
 * containers are named, and as they need to be found by their associated issues 
 * (surplusMethods et al), they must be tokenized to allow for association via
 * their names
 * @author pit
 */
public interface HasContainerTokens {
	String ANNOTATIONS = "annotations";
	String SUPER_TYPES = "super types";
	String DERIVED_TYPES = "derived types";	
	String IMPLEMENTED_INTERFACES = "implemented interfaces";
	String FIELDS = "fields";
	String METHODS = "methods";
	String SUPER_INTERFACES = "super interfaces";
	String IMPLEMENTING_TYPES = "implementing types";
	String DERIVING_TYPES = "deriving types";
	String TEMPLATE_PARAMETERS = "template parameters";
	String THROWN_EXCEPTIONS = "thrown exceptions";
	String TYPE_REFERENCES_IN_BODY = "type references in body";
	String RETURN_TYPE = "return type";
	String ARGUMENT_TYPES = "argument types";
	String VALUES = "values";
	
}
