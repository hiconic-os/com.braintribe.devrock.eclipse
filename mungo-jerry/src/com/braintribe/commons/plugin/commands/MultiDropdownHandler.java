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
package com.braintribe.commons.plugin.commands;

import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

public abstract class MultiDropdownHandler extends AbstractDropdownCommandHandler  {

	protected String paramForGlobalExecutions = "ALL";
	protected String paramForMultipleExecutions = "SELECTED";
	
	@Override
	public void process(String parameter) {
				
		if (parameter != null) { 
			if (parameter.equalsIgnoreCase( paramForGlobalExecutions)) {			
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();			
				IProject [] projects = root.getProjects();
				executeMultipleCommand( projects);						
			} 
			else if (parameter.equalsIgnoreCase(paramForMultipleExecutions)) {
				Set<IProject> projects = getTargetProjects();
				if (projects != null) {
					executeMultipleCommand( projects.toArray( new IProject[0]));
				}
			}
		}
		else {							
			executeSingle( getTargetProject());
		}
	}		
	protected abstract void executeMultipleCommand(IProject ... projects); 			
}
