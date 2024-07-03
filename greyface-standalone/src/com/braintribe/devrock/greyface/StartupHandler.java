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
package com.braintribe.devrock.greyface;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IStartup;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

/**
 * used to register early contribution to VE.. 
 * @author Pit
 *
 */
public class StartupHandler implements IStartup {
	private static final String PLUGIN_ID = "com.braintribe.devrock.VirtualEnvironment";
	
	@Override
	public void earlyStartup() {
		/*
		final Bundle bundle = Platform.getBundle(PLUGIN_ID);
		if (bundle != null && bundle.getState() == Bundle.ACTIVE) {
			// register resolvers
			registerContribution();
		} else {
			// register listener to get informed, when plug-in becomes active
			final BundleContext bundleContext = GreyfacePlugin.getInstance().getBundle().getBundleContext();
			bundleContext.addBundleListener(new BundleListener() {
				@Override
				public void bundleChanged(final BundleEvent pEvent) {
					final Bundle notifyingBundle = pEvent.getBundle();
					String symbolicName = notifyingBundle.getSymbolicName();
					if (!symbolicName.equals(PLUGIN_ID)) {
						return;
					}
					if (notifyingBundle.getState() == Bundle.ACTIVE) {
						registerContribution();
						bundleContext.removeBundleListener(this);
					}
				}
			});
		}
		*/
			
	}
	
	private void registerContribution() {
		//VirtualEnvironmentPlugin.getInstance().addContributionDeclaration( GreyfacePlugin.getInstance());
	}
	

}
