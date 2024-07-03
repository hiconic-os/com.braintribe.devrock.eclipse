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
package com.braintribe.devrock.greyface.process.retrieval;

import java.io.File;
import java.io.IOException;

import com.braintribe.devrock.greyface.GreyfacePlugin;
import com.braintribe.logging.Logger;

/**
 * helper for temporary file creation 
 * @author pit
 *
 */
public class TempFileHelper {
	private static Logger log = Logger.getLogger(TempFileHelper.class);
	public static final String gf_tempfile_marker = ".gf.";
	private static File tmpDirectory;
	
	/**
	 * create a temporary file in the directory specified in the preferences
	 * @param name - the of the file to generate a respective temporary file 
	 * @return - the temporary {@link File}
	 * @throws IOException - arrgh
	 */
	public static File createTempFileFromFilename( String name) throws IOException {		
		File gfDirectory = getTempDirectory();		
		return File.createTempFile(name + gf_tempfile_marker, null, gfDirectory);
	}

	private static File getTempDirectory() {
		if (tmpDirectory != null)
			return tmpDirectory;
		
		GreyfacePlugin plugin = GreyfacePlugin.getInstance();
		String tempVar = plugin.getGreyfacePreferences(false).getTempDirectory();
		String tempDir = plugin.getVirtualPropertyResolver().resolve( tempVar);
		File directory = new File( tempDir);		
		directory.mkdirs();
		
		// 
		File gfDirectory = new File( directory, "devrock.gf");
		gfDirectory.mkdirs();
		
		tmpDirectory = gfDirectory;
		return tmpDirectory;
	}
	
	public static void purge() {
		File [] files = getTempDirectory().listFiles();
		int i = 0;
		if (files == null)
			return;
		
		for (File file : files) {
			boolean deleted = file.delete();
			if (deleted) {
				i++;
			}
		}		
		log.debug("purged [" + i + "] files from [" + getTempDirectory() + "]");
	}
	
	/**
	 * extracts the nucleus name of the temporary file (inverse to the wrapping of the {@link #createTempFileFromFilename(String)} function 
	 * @param tempfile - the name of the temporary file 
	 * @return - the original name 
	 */
	public static String extractFilenameFromTempFile( String tempfile){
		int p = tempfile.lastIndexOf( gf_tempfile_marker);
		if (p < 0)
			return tempfile;
		return tempfile.substring(0, p);
	}
	
	public static boolean isATempFile( String tempfile){
		if (tempfile.contains( gf_tempfile_marker) && tempfile.endsWith( ".tmp")) {
			return true;
		}
		return false;
	}
}
