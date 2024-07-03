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
package com.braintribe.devrock.api.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.braintribe.devrock.api.process.listener.MessageType;
import com.braintribe.devrock.api.process.listener.ProcessNotificationListener;

public class ProcessExecution {
	
	private static class ProcessStreamReader extends Thread {
		private InputStream in;
		private StringBuilder buffer = new StringBuilder();
		private ProcessNotificationListener listener;
		
		public ProcessStreamReader(InputStream in) {
			this.in = in;
		}
			
		public void setListener(ProcessNotificationListener listener) {
			this.listener = listener;
		}


		@Override
		public void run() {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(in, getConsoleEncoding()));
				String line = null;				
				while ((line = reader.readLine()) != null) {
					if (listener != null) {
						listener.acknowledgeProcessNotification(MessageType.info, line);
					}
					if (buffer.length() > 0) buffer.append('\n');
					buffer.append(line);
					
				}
			}
			catch (InterruptedIOException e) {
			}
			catch (IOException e) {
				
			}
		}
		
		public String getStreamResults() {
			return buffer.toString();
		}
		
		public void cancel() {
			if (isAlive()) {
				interrupt();
				try {
					join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static ProcessResults runCommand( ProcessNotificationListener listener, Map<String, String> environment, String ... cmd) throws ProcessException {
		return runCommand(Arrays.asList( cmd), null, environment, listener);
	}
	
	public static ProcessResults runCommand( ProcessNotificationListener listener, File workingCopy, Map<String, String> environment, String ... cmd) throws ProcessException {
		return runCommand(Arrays.asList( cmd), workingCopy, environment, listener);
	}
	public static ProcessResults runCommand(List<String> cmd, File workingDirectory, Map<String, String> environment, ProcessNotificationListener monitor) throws ProcessException {
		try {
			
			ProcessBuilder processBuilder = new ProcessBuilder( cmd);
			
			if (workingDirectory != null) {
				processBuilder.directory( workingDirectory);
			}
						
			
			if (environment != null) {
				Map<String, String> processBuilderEnvironment = processBuilder.environment();
				processBuilderEnvironment.putAll(environment);
			}
			
								
			Process process = processBuilder.start();
			

			ProcessStreamReader errorReader = new ProcessStreamReader(process.getErrorStream());
			ProcessStreamReader inputReader = new ProcessStreamReader(process.getInputStream());
			
			inputReader.setListener(monitor);
			errorReader.setListener(monitor);
			
			errorReader.start();
			inputReader.start();
			
			process.waitFor();
			int retVal = process.exitValue();

			inputReader.cancel();
			errorReader.cancel();

			return new ProcessResults(retVal, inputReader.getStreamResults(), errorReader.getStreamResults());
		}
		catch (Exception e) {
			throw new ProcessException(e);
		}
	}
	
	public static String getConsoleEncoding() {
		return "Cp850";
	}


}
