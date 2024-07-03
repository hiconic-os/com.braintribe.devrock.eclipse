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
package com.braintribe.devrock.ac.container.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.braintribe.codec.marshaller.yaml.YamlMarshaller;
import com.braintribe.devrock.ac.container.plugin.ArtifactContainerPlugin;
import com.braintribe.devrock.ac.container.plugin.ArtifactContainerStatus;
import com.braintribe.model.artifact.analysis.AnalysisArtifactResolution;

/**
 * helper class to load/save {@link AnalysisArtifactResolution} from and to YAML formatted files
 * 
 * @author pit
 *
 */
public class ResolutionYamlMarshaller {
	private static YamlMarshaller marshaller;
	static {
		 marshaller = new YamlMarshaller();
		 marshaller.setWritePooled( true);
	}
	

	/**
	 * load the {@link AnalysisArtifactResolution} from an YAML file 
	 * @param file - the {@link File} to load from 
	 * @return - the 
	 */
	public static AnalysisArtifactResolution fromYamlFile( File file) {
		try (InputStream in = new FileInputStream( file)) {
			return (AnalysisArtifactResolution) marshaller.unmarshall( in);
		}
		catch( Exception e) {
			ArtifactContainerStatus status = new ArtifactContainerStatus( "Cannot unmarshall the resolution from the file [" + file.getAbsolutePath() + "]", e);
			ArtifactContainerPlugin.instance().log(status);
		}
		return null;
	}
	
	/**
	 * write the resolution as YAML to disk
	 * @param resolution - the {@link AnalysisArtifactResolution}
	 * @param file - the {@link File} to write to 
	 */
	public static void toYamlFile( AnalysisArtifactResolution resolution, File file) {
		try (OutputStream out = new FileOutputStream( file)) {
			marshaller.marshall(out, resolution);			
		} catch (Exception e) {
			ArtifactContainerStatus status = new ArtifactContainerStatus( "Cannot marshall the resolution to the file [" + file.getAbsolutePath() + "]", e);
			ArtifactContainerPlugin.instance().log(status);
		}
	}
}
