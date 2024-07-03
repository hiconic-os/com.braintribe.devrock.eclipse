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
package com.braintribe.eclipse.launching;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class ClassloaderJarFile {
	private static File jarFile;
	private static final String jarName = "FileClasspathClassloader-1.0.jar";
	
	protected static synchronized File getJarFile() throws IOException {
		if (jarFile == null) {
			File directory = Activator.getDefault().getStateLocation().toFile();
			File newJarFile = new File(directory, jarName);

			if (!newJarFile.exists()) {
				URL url = ClassloaderJarFile.class
						.getResource(jarName);
				InputStream in = url.openStream();
				OutputStream out = new FileOutputStream(newJarFile);
				try {
					byte buffer[] = new byte[32768];
					for (int len = 0; (len = in.read(buffer)) != -1;)
						out.write(buffer, 0, len);

					out.flush();
				} finally {
					in.close();
					out.close();
				}
			}

			jarFile = newJarFile;
		}

		return jarFile;
	}


}
