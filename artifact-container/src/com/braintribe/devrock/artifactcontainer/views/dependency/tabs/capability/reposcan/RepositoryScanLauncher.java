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
package com.braintribe.devrock.artifactcontainer.views.dependency.tabs.capability.reposcan;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;

import com.braintribe.build.artifact.name.NameParser;
import com.braintribe.devrock.greyface.GreyfacePlugin;
import com.braintribe.devrock.greyface.GreyfaceViewLauncher;
import com.braintribe.model.artifact.Dependency;

/**
 * 
 * @author pit
 *
 */
public class RepositoryScanLauncher implements HasRepositoryScanTokens {

	public static void initiateRepositoryScan( Display display, TreeItem ...items) {
		if (items == null || items.length == 0)
			return;
		final GreyfaceViewLauncher launcher = GreyfacePlugin.getInstance();
		for (TreeItem item : items){
			Dependency dependency = (Dependency) item.getData( DATAKEY_DEPENDENCY);
			launcher.addDependency( NameParser.buildName(dependency));			
		}
		display.asyncExec( new Runnable() {
			
			@Override
			public void run() {
				launcher.activateGreyface();				
			}
		});
	}
}
