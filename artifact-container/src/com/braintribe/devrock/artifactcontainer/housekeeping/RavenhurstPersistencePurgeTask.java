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
package com.braintribe.devrock.artifactcontainer.housekeeping;

import java.util.List;

import org.eclipse.core.runtime.IStatus;

import com.braintribe.build.artifact.retrieval.multi.ravenhurst.persistence.registry.RavenhurstPersistenceHelper;
import com.braintribe.build.artifacts.mc.wire.classwalk.contract.ClasspathResolverContract;
import com.braintribe.devrock.artifactcontainer.ArtifactContainerPlugin;
import com.braintribe.devrock.artifactcontainer.ArtifactContainerStatus;
import com.braintribe.devrock.artifactcontainer.plugin.malaclypse.scope.wirings.MalaclypseWirings;
import com.braintribe.model.ravenhurst.interrogation.RavenhurstBundle;

/**
 * a {@link HouseKeepingTask} that cleans the .interrogation files from the updateinfo directories  
 * 
 * @author pit
 *
 */
public class RavenhurstPersistencePurgeTask implements HouseKeepingTask {

	@Override
	public void execute() {
	
		// do not purge if debug's active 
		if (ArtifactContainerPlugin.isDebugActive()) {
			return;
		}
		// get the days
		int days = ArtifactContainerPlugin.getInstance().getArtifactContainerPreferences(false).getRavenhurstPreferences().getPrunePeriod();
		// currently configured contract (a full one, even if not really required)
		ClasspathResolverContract contract = MalaclypseWirings.fullClasspathResolverContract().contract();
		try {
			List<RavenhurstBundle> bundles = contract.ravenhurstScope().getRavenhurstBundles();
			// purge for each bundle 
			for (RavenhurstBundle bundle : bundles) {
				List<String> msgs = RavenhurstPersistenceHelper.purge( contract.settingsReader(), bundle, days);
				// process errors 
				if (!msgs.isEmpty()) {
					for (String msg : msgs) {
						ArtifactContainerStatus status = new ArtifactContainerStatus( msg, IStatus.ERROR);
						ArtifactContainerPlugin.getInstance().log(status);
					}
				}
				else {
					ArtifactContainerStatus status = new ArtifactContainerStatus( "sucessfully pruned interrogation data of repository [" + bundle.getRepositoryId() + "@" + bundle.getRepositoryUrl() + "] of [" + bundle.getProfileId() + "]", IStatus.INFO);
					ArtifactContainerPlugin.getInstance().log(status);
				}
			}
		} catch (Exception e) {
			String msg = "cannot run ravenhurst house keeping task";
			ArtifactContainerStatus status = new ArtifactContainerStatus( msg, e);
			ArtifactContainerPlugin.getInstance().log(status);
		} 
	}
	
}
