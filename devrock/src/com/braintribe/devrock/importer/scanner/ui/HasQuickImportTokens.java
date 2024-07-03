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
package com.braintribe.devrock.importer.scanner.ui;

public interface HasQuickImportTokens {
	static final String MARKER_ARTIFACT = "artifact";
	static final String MARKER_AVAILABLE = "available";
	static final String DATA_AVAILABLITY_STATE = "availablity";
	static final String MARKER_TOOLTIP = "tooltip";
	static final String NO_IMPORT = "cannot be imported as it exists in the workspace or in the current working set";
	static final String AVAILABLE_IMPORT = "can be imported as it doesn't exists in the workspace or in the current working set";

}
