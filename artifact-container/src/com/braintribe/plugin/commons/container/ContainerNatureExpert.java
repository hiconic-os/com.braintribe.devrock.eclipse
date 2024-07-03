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
package com.braintribe.plugin.commons.container;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;

import com.braintribe.devrock.artifactcontainer.ArtifactContainerPlugin;
import com.braintribe.devrock.artifactcontainer.ArtifactContainerStatus;
import com.braintribe.devrock.artifactcontainer.natures.TribefireServicesNature;
import com.braintribe.logging.Logger;
import com.braintribe.model.artifact.Artifact;
import com.braintribe.plugin.commons.selection.SelectionExtractor;


public class ContainerNatureExpert {
	private static Logger log = Logger.getLogger(ContainerNatureExpert.class);
	
	private static String [] tomcatNatures = new String [] {"com.sysdeo.eclipse.tomcat.tomcatnature", "net.sf.eclipse.tomcat.tomcatnature"};
	private static String modelNature = "com.braintribe.eclipse.model.nature.ModelNature";
	

	public static String getPackaging( IProject project) {
		String packaging = null;
		IResource pomResource = project.findMember( "pom.xml");
		if (pomResource != null) {
		
			File pom = new File(pomResource.getLocation().toOSString());
			try {
				Artifact artifact = SelectionExtractor.extractArtifactFromPom(pom);
				if (artifact != null) {
					packaging = artifact.getPackaging();
				}
				else {
					String msg = "cannot read artifact from pom [" + pom.getAbsolutePath() + "]";				
					ArtifactContainerStatus status = new ArtifactContainerStatus(msg, IStatus.WARNING);
					ArtifactContainerPlugin.getInstance().log(status);	
				}
			} catch (Exception e) {
				log.error("cannot read pom to identify pom nature ", e);
			}
		}
		if (packaging == null)
			packaging = "jar";
		return packaging;
	}
	
	public static boolean hasAggregateNature( IProject project) {
		if (getPackaging( project).equalsIgnoreCase("pom")) {
			return true;
		}
		return false;
	}

	public static boolean hasTomcatNature( IProject project) {
		Map<String, Boolean> tomcatNatureMap = hasNatures(project, tomcatNatures);
		if (tomcatNatureMap.values().contains( Boolean.TRUE)) {
			return true;
		}
		return false;
	}
	
	public static boolean hasModelNature( IProject project) {
		Map<String, Boolean> natureMap = hasNatures(project, modelNature);
		if (natureMap.values().contains( Boolean.TRUE)) {
			return true;
		}
		return false;
	}
	public static boolean hasTribefireServicesNature( IProject project) {	
		Map<String, Boolean> natureMap = hasNatures(project, TribefireServicesNature.NATURE_ID);
		if (natureMap.values().contains( Boolean.TRUE)) {
			return true;
		}
		return false;
	}
	
		
	
	public static Map<String, Boolean> hasNatures( IProject project, String ... natures) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		if (natures == null)
			return result;
		for (String nature : natures) {
			try {
				result.put(nature, project.getNature(nature) != null);
			} catch (CoreException e) {
				result.put(nature, false);
			}				
		}
		return result;
	}
}
