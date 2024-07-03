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
package com.braintribe.devrock.api.identification;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;

import com.braintribe.devrock.mc.core.declared.DeclaredArtifactIdentificationExtractor;
import com.braintribe.devrock.model.mc.reason.PomCompileError;
import com.braintribe.devrock.plugin.DevrockPlugin;
import com.braintribe.devrock.plugin.DevrockPluginStatus;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.Reasons;
import com.braintribe.gm.model.reason.essential.NotFound;
import com.braintribe.logging.Logger;
import com.braintribe.model.artifact.compiled.CompiledArtifactIdentification;

/**
 * lill' helper to identify a project be looking at its pom file
 * @author pit
 *
 */
public class PomIdentificationHelper {
	private static Logger log = Logger.getLogger(PomIdentificationHelper.class);

	/**
	 * @param project - the {@link IProject} 
	 * @param pomFile - the pom file 
	 * @return - a {@link Maybe} of a {@link CompiledArtifactIdentification} 
	 */
	public static Maybe<CompiledArtifactIdentification> identifyPom(IProject project, File pomFile) {
		Maybe<CompiledArtifactIdentification> extractedIdentificationPotential = DeclaredArtifactIdentificationExtractor.extractIdentification(pomFile);
		if (extractedIdentificationPotential.isEmpty()) {
			String msg = "cannot read pom [" + pomFile.getAbsolutePath() + "] associated with project [" + project.getName() + "] as [" + extractedIdentificationPotential.whyUnsatisfied().stringify();
			DevrockPluginStatus status = new DevrockPluginStatus(msg, IStatus.WARNING);
			DevrockPlugin.instance().log(status);
			return Maybe.empty( Reasons.build(PomCompileError.T).text(msg).toReason());
		}
		CompiledArtifactIdentification cai = extractedIdentificationPotential.get();
		return Maybe.complete( cai);
	}
	
	/**
	 * @param project - the {@link IProject}
	 * @return - a {@link Maybe} of a {@link CompiledArtifactIdentification}
	 */
	public static Maybe<CompiledArtifactIdentification> identifyProject(IProject project) {
		File projectDir = project.getLocation().toFile();
		File pomFile = new File( projectDir, "pom.xml");
		if (!pomFile.exists()) {
			String msg = "project [" + project.getName() + "] has no associated pom in [" + projectDir.getAbsolutePath() + "] and is therefore transparent for the view";
			log.info( msg);			
			return Maybe.empty( Reasons.build(NotFound.T).text( msg).toReason());
		}
		return identifyPom(project, pomFile);
	}
}
