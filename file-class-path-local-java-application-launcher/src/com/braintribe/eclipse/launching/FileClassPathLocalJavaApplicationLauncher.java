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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.launching.JavaLaunchDelegate;

public class FileClassPathLocalJavaApplicationLauncher extends
		JavaLaunchDelegate {
	public FileClassPathLocalJavaApplicationLauncher() {
	}

	public String getVMArguments(ILaunchConfiguration configuration)
			throws CoreException {
		StringBuilder builder = new StringBuilder(
				super.getVMArguments(configuration));
		builder.append(" -Djava.system.class.loader=com.braintribe.utils.classloader.FileClassPathClassLoader");
		builder.append(" -Dcom.braintribe.classpath.file=\"");
		builder.append(getClasspathFileLocation(configuration).getPath());
		builder.append("\"");
		String extendedVMArguments = builder.toString();
		return extendedVMArguments;
	}

	protected File getClasspathFileLocation(ILaunchConfiguration configuration) {
		String name = configuration.getName();
		File directory = Activator.getDefault().getStateLocation().toFile();
		File classpathFile = new File(directory, (new StringBuilder(
				String.valueOf(name))).append(".classpath").toString());
		return classpathFile;
	}

	public String[] getClasspath(ILaunchConfiguration configuration)
			throws CoreException {
		String originalClasspath[];
		File classpathFile;
		BufferedWriter writer;
		originalClasspath = super.getClasspath(configuration);
		classpathFile = getClasspathFileLocation(configuration);
		writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(classpathFile), "UTF-8"));
			int i = 0;
			String as[];
			int k = (as = originalClasspath).length;
			for (int j = 0; j < k; j++) {
				String line = as[j];
				if (i++ > 0)
					writer.newLine();
				writer.append(line);
			}

			writer.flush();

			return (new String[] { ClassloaderJarFile.getJarFile().toString() });
		} catch (IOException e) {
			Status status = new Status(
					4,
					"Launcher",
					"error while relocating classpath from commandline to file",
					e);
			throw new CoreException(status);
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

}
