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
package com.braintribe.devrock.arb.plugin;

import java.util.Date;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.braintribe.devrock.api.logging.LoggingCommons;
import com.braintribe.devrock.arb.listener.ResourceChangeListener;
import com.braintribe.logging.Logger;

public class ArtifactReflectionBuilderPlugin extends AbstractUIPlugin {

	private static Logger log = Logger.getLogger(ArtifactReflectionBuilderPlugin.class);

	public static final String PLUGIN_ID = "com.braintribe.devrock.ArtifactReflectionBuilderPlugin"; //$NON-NLS-1$
	public static final String PLUGIN_RESOURCE_PREFIX = "platform:/plugin/" + PLUGIN_ID;
	
	private static ArtifactReflectionBuilderPlugin instance;
	private ResourceChangeListener resourceChangeListener = new ResourceChangeListener();

	public static ArtifactReflectionBuilderPlugin instance() {
		return instance;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {	
		super.start(context);
		instance = this;
		
		LoggingCommons.initializeWithFallback( PLUGIN_ID);
		long startTime = System.nanoTime();		
		log.info("ArtifactReflectionBuilderPlugin: starting : " + new Date());

		// initialization tasks here 
		ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener, IResourceChangeEvent.POST_CHANGE );
		
		long endTime = System.nanoTime();
		String msg = "ArtifactReflectionBuilderPlugin : started after " + (endTime - startTime) / 1E6 + " ms";

		log.info(msg);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		long startTime = System.nanoTime();		
		log.info("ArtifactReflectionBuilderPlugin: stopping : " + new Date());
		
		// de-initialization tasks here 
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceChangeListener);		
		
		long endTime = System.nanoTime();
		String msg = "ArtifactReflectionBuilderPlugin : stopped after " + (endTime - startTime) / 1E6 + " ms";
		log.info(msg);
		
	}

	/**
	 * @param status - the {@link IStatus} to log
	 */
	public void log( IStatus status) {
		getLog().log( status);
		
		// redirect to logs 
		switch (status.getSeverity()) {
			case 0 : // OK
				log.trace(status.getMessage());
				break;
			case 1 : // INFO
				log.info(status.getMessage());
				break;
			case 2 : // WARN
				log.warn(status.getMessage());
				break;
			case 4: // ERROR
				log.error(status.getMessage(), status.getException());
				break;
			case 8:	// CANCEL ?? 	
				log.warn(status.getMessage());
				break;
				
			
		}
	}
	
}
