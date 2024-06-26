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
 	
