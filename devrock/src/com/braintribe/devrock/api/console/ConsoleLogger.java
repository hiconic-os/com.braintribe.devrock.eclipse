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
package com.braintribe.devrock.api.console;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * 
 * @author pit
 *
 */
public class ConsoleLogger {
	
	// the message console stream used to output to the console view
	private MessageConsoleStream mConsoleStream = null;
	
	 /**
	 * @param consoleName - the name of the console to create a stream to 
	 */
	public ConsoleLogger(String consoleName) {
		 super(); 
		 MessageConsole myConsole = findConsole( consoleName);
		 mConsoleStream = myConsole.newMessageStream(); 
	}
	 
	/**
	 * log a message to the console 
	 * @param message - the message to log, what else? 
	 */
	public void log(String message) {
		 mConsoleStream.println(message);
	}	
	
	public void close() {		
		// the fuck - might do something later
	}
	 /**
	 * @param name - find a console with a given name or create one  
	 * @return - the {@link MessageConsole} requested 
	 */
	public static MessageConsole findConsole(String name) {
		 IConsoleManager conMan = ConsolePlugin.getDefault().getConsoleManager();
		// scan for console 
		 IConsole[] existing = conMan.getConsoles();
		 for (int i = 0; i < existing.length; i++) {
			 if (name.equals( existing[i].getName())) {
				 return (MessageConsole) existing[i];
			 }
		 }
		 
		 //no console found, so create a new one
		 MessageConsole myConsole = new MessageConsole( name, null);
		 conMan.addConsoles( new IConsole[]{ myConsole});
		 return myConsole;
	 }
}
 	
