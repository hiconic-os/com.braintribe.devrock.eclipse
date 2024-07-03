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
package com.braintribe.devrock.virtualenvironment.variable;

import java.util.Iterator;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.ui.IStartup;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

import com.braintribe.devrock.virtualenvironment.VirtualEnvironmentPlugin;
import com.braintribe.devrock.virtualenvironment.variable.resolver.ToUpperCaseTemplateResolver;
import com.braintribe.devrock.virtualenvironment.variable.resolver.UuidCodeTemplateVariableResolver;

/**
 * @author Pit
 *
 */
@SuppressWarnings("restriction")
public class VariableResolverRegisterer implements IStartup {

	static final String PLUGIN_ID = "org.eclipse.jdt.ui";
	@Override
	public void earlyStartup() {

		final Bundle bundle = Platform.getBundle(PLUGIN_ID);
		if (bundle != null && bundle.getState() == Bundle.ACTIVE) {
			// register resolvers
			registerResolvers();
		} else {
			// register listener to get informed, when plug-in becomes active
			final BundleContext bundleContext = VirtualEnvironmentPlugin.getInstance().getBundle().getBundleContext();
			bundleContext.addBundleListener(new BundleListener() {
				@Override
				public void bundleChanged(final BundleEvent pEvent) {
					final Bundle bundle2 = pEvent.getBundle();
					String symbolicName = bundle2.getSymbolicName();
					if (!symbolicName.equals(PLUGIN_ID)) {
						return;
					}
					if (bundle2.getState() == Bundle.ACTIVE) {
						registerResolvers();
						bundleContext.removeBundleListener(this);
					}
				}
			});
		}
	

	}

	/**
	   * 
	   * Internal method to register resolvers with all context types.
	   * 
	   */
	  private void registerResolvers() {
	    final ContextTypeRegistry codeTemplateContextRegistry = JavaPlugin.getDefault().getCodeTemplateContextRegistry();
	    final Iterator<TemplateContextType> ctIter = codeTemplateContextRegistry.contextTypes();
	    while (ctIter.hasNext()) {
	      final TemplateContextType contextType = ctIter.next();
	      contextType.addResolver(new UuidCodeTemplateVariableResolver());
	      contextType.addResolver( new ToUpperCaseTemplateResolver());
	    }
	  }
}
