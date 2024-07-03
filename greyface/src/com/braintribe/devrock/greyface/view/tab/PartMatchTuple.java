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
package com.braintribe.devrock.greyface.view.tab;

import com.braintribe.model.artifact.Part;

/**
 * tuple to be used as a container for the information required to distinguish parts, especially parts reflecting maven-metadata.xml files
 * 
 * @author Pit
 *
 */
public class PartMatchTuple {
	public Part part;
	public String location;
	public String extractedLocation;
	public boolean isTempFile;
	public boolean isMetaData;
	public boolean isGroupMetaData;
	public String partVersionAsString;
	public String partFileName;
}
