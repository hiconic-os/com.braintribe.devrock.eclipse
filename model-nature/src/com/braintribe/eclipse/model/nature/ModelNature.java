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
package com.braintribe.eclipse.model.nature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class ModelNature implements IProjectNature {

	public static final String NATURE_ID = "com.braintribe.eclipse.model.nature.ModelNature"; //$NON-NLS-1$
	private IProject project;
	
	@Override
	public void configure() throws CoreException {
		IProjectDescription description = project.getDescription();
		ICommand commands[] = description.getBuildSpec();
		
		if (findCommand(ModelBuilder.ID, commands) == -1) {
			List<ICommand> commandList = new ArrayList<ICommand>(Arrays.asList(commands));
			ICommand builderCommand = description.newCommand();
			builderCommand.setBuilderName(ModelBuilder.ID);
			commandList.add(builderCommand);
			ICommand manipulatedCommands[] = commandList.toArray(new ICommand[commandList.size()]);
			description.setBuildSpec(manipulatedCommands);
			project.setDescription(description, null);
		}
	}
	
	private static int findCommand(String name, ICommand[] commands) {
		int i = 0;
		for (ICommand iCommand : commands) {
			if (iCommand.getBuilderName().equals(name))
				return i;
			i++;
		}
		return -1;
	}

	@Override
	public void deconfigure() throws CoreException {
		IProjectDescription description = project.getDescription();
		ICommand commands[] = description.getBuildSpec();
		
		int pos = findCommand(ModelBuilder.ID, commands);
		if (pos != -1) {
			List<ICommand> commandList = new ArrayList<ICommand>(Arrays.asList(commands));
			commandList.remove(pos);
			ICommand manipulatedCommands[] = commandList.toArray(new ICommand[commandList.size()]);
			description.setBuildSpec(manipulatedCommands);
			project.setDescription(description, null);
		}
	}

	@Override
	public IProject getProject() {		
		return project;
	}

	@Override
	public void setProject(IProject project) {
		this.project = project;
		
	}

}
