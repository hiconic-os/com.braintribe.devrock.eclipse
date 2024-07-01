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
package com.braintribe.plugin.commons.commands;

import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

public abstract class MultiDropdownHandler extends AbstractDropdownCommandHandler  {

	protected String paramForGlobalExecutions = "ALL";
	protected String paramForMultipleExecutions = "SELECTED";
	protected String paramForAtomicExecution = "ATOMIC";
	
	@Override
	public void process(String parameter) {
				
		if (parameter != null) { 
			if (parameter.equalsIgnoreCase( paramForGlobalExecutions)) {			
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();			
				IProject [] projects = root.getProjects();
				executeMultipleCommand(false, projects);						
			} 
			else if (parameter.equalsIgnoreCase(paramForMultipleExecutions)) {
				Set<IProject> projects = getTargetProjects();
				if (projects != null) {
					executeMultipleCommand( true, projects.toArray( new IProject[0]));
				}
			}
			else if (parameter.equalsIgnoreCase( paramForAtomicExecution)) {
				Set<IProject> projects = getTargetProjects();
				executeMultipleCommand( false, projects.toArray( new IProject[0]));
			}
		}
		else {								
			Set<IProject> projects = getTargetProjects();
			if (projects != null && projects.size() > 0) {
				executeMultipleCommand( true, projects.toArray( new IProject[0]));
			} 
		}
	}		
	protected abstract void executeMultipleCommand(boolean chain, IProject ... projects); 			
}
