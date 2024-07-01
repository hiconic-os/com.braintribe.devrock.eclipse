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
package com.braintribe.devrock.artifactcontainer.control.project;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

/**
 * @author pit
 *
 */
public class AccessibleProjectsController {
	
	private Set<IProject> projects = null;
	
	private Set<IProject> buildList() {
		Set<IProject> result = new HashSet<IProject>();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();	
		for (IProject project : root.getProjects()) {
			if (project.isAccessible())
				result.add( project);
		}
		return result;
	}
	
	private boolean compareSets( Set<IProject> one, Set<IProject> two) {
		if (one.size() != two.size())
			return false;
		
		if (two.containsAll( one))
			return true;
		
		return false;
	}
	
	public boolean accessibleProjectsChanged() {
		Set<IProject> projectlist = buildList();
		if (projects == null) {
			projects = projectlist;
			return true;
		}
		boolean retval = compareSets( projects, projectlist);
		projects = projectlist;
		
		return !retval;
	}
	
}
