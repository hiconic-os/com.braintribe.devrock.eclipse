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
package com.braintribe.devrock.mnb.marker;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import com.braintribe.devrock.plugin.DevrockPlugin;
import com.braintribe.devrock.plugin.DevrockPluginStatus;
import com.braintribe.gm.model.reason.Reason;
import com.braintribe.logging.Logger;

/**
 * helper to attach 'failed resolution markers' to an {@link IProject}
 * @author pit
 *
 */
public class ReasonedMarkerHandler {
	private static Logger log = Logger.getLogger(ReasonedMarkerHandler.class);
	
	public static final String MRK_REASON = "com.braintribe.devrock.mnb.failed";

	/**
	 * attaches a reasoned marker to a project
	 * @param project - the {@link IProject}
	 * @param reason - the {@link Reason}
	 */
	public static void addFailedResolutionMarkerToProject(IProject project, Reason reason) {
		try {
			IMarker marker = project.createMarker(MRK_REASON);	
			marker.setAttribute(IMarker.MESSAGE, reason.getText());
	        marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		} catch (CoreException e) {
			String msg = "cannot create a marker of type [" + MRK_REASON + "] to project [" + project.getName() + "]";
			log.error( msg);
			DevrockPluginStatus status = new DevrockPluginStatus( msg, e);
			DevrockPlugin.instance().log(status);	
		}		
	}
}
