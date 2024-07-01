// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.devrock.artifactcontainer.control.container;

import java.io.File;
import java.io.IOException;

import com.braintribe.logging.Logger;

public class TemporaryFilestorage {
	private static Logger log = Logger.getLogger(TemporaryFilestorage.class);
	private static File tmpDirectory;
	
	private static File getTmpDirectory() {
		if (tmpDirectory != null) {
			return tmpDirectory;
		}
		File javaTmpDirectory = new File( System.getProperty( "java.io.tmpdir"));
		tmpDirectory = new File( javaTmpDirectory, "devrock.ac");
		tmpDirectory.mkdirs();
		return tmpDirectory;
	}
	
	public static File createTempFile( String prefix, String suffix) throws IOException {
		File tf = File.createTempFile(prefix, suffix, getTmpDirectory());
		return tf;
	}
	
	public static void purge() {
		File [] files = getTmpDirectory().listFiles();
		int i = 0;
		if (files == null)
			return;
		
		for (File file : files) {
			boolean deleted = file.delete();
			if (deleted) {
				i++;
			}
		}
		
		log.debug("purged [" + i + "] files from [" + tmpDirectory + "]");
	}
}
