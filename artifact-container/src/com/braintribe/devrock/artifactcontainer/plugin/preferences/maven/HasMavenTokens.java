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
package com.braintribe.devrock.artifactcontainer.plugin.preferences.maven;

import java.io.File;

public interface HasMavenTokens {
	public static final String REMOTE_SETTINGS = "${M2_HOME}" + File.separator + "conf" + File.separator + "settings.xml";
	public static final String LOCAL_SETTINGS = "${user.home}" + File.separator + ".m2" + File.separator + "settings.xml";
}
