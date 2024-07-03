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
package com.braintribe.devrock.greyface.view.tab.parameter;

import java.io.File;
import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import com.braintribe.build.artifact.name.NameParser;
import com.braintribe.build.artifact.representations.artifact.pom.ArtifactPomReader;
import com.braintribe.build.artifact.representations.artifact.pom.PomReaderException;
import com.braintribe.devrock.greyface.process.retrieval.LocalSingleDirectoryDependencyResolver;
import com.braintribe.devrock.greyface.scope.GreyfaceScope;
import com.braintribe.model.artifact.Artifact;
import com.braintribe.model.artifact.Solution;
import com.braintribe.model.artifact.processing.version.VersionProcessor;

public class LocalFileSystemScanner {	

	public class Tuple {
		public String condensedArtifactName;
		public String directory;
	}
	
	public Tuple scanLocalFileSystem(Display display) {		
		FileDialog fDialog = new FileDialog( display.getActiveShell(), SWT.OPEN | SWT.SINGLE);
		
		fDialog.setFilterExtensions( new String [] {"*.pom", "*.jar", "*.javadoc"});
		String name = fDialog.open();
		if (name == null)
			return null;
		
		Tuple tuple = new Tuple();
		File file = new File( name);
		if (file.isDirectory()) {		
			tuple.directory = file.getAbsolutePath();
			return tuple;
		}
		
		 
		File directory = file.getParentFile();
		tuple.directory = directory.getAbsolutePath();
			
		// extract data to text field.. 			
		Artifact artifact = null;
		if (name.endsWith( ".pom") == false) {
			artifact = NameParser.parseFileName( new File( name));					
			String condensedName = artifact.getArtifactId() + "-" + VersionProcessor.toString( artifact.getVersion());
			tuple.condensedArtifactName = condensedName;			
			return tuple;			
		} else {				
			try {
				ArtifactPomReader reader = GreyfaceScope.getScope().getPomReader();						
				LocalSingleDirectoryDependencyResolver localDependencyResolver = new LocalSingleDirectoryDependencyResolver();
				localDependencyResolver.setLocalDirectory( directory.getAbsolutePath());
				reader.setDependencyResolver( localDependencyResolver);									
				Solution solution = reader.read( UUID.randomUUID().toString(), name);
				String condensedName = NameParser.buildName( solution);
				tuple.condensedArtifactName = condensedName;
				return tuple;
			} catch (PomReaderException e) {
				return tuple;
			}
		}		
	}
	
	public Tuple scanForLocalMavenCompatibleRoot( Display display) {
		DirectoryDialog dialog = new DirectoryDialog(display.getActiveShell());
		String name = dialog.open();
		if (name == null)
			return null;
		
		Tuple tuple = new Tuple();
		File file = new File( name);
		if (file.isDirectory()) {		
			tuple.directory = file.getAbsolutePath();
			return tuple;
		}
		return null;
	}
}
