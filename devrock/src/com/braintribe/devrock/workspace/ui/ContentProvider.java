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
package com.braintribe.devrock.workspace.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;

import com.braintribe.cfg.Configurable;
import com.braintribe.devrock.eclipse.model.workspace.Project;
import com.braintribe.devrock.eclipse.model.workspace.WorkingSet;
import com.braintribe.devrock.eclipse.model.workspace.Workspace;

/**
 * simple {@link ITreeContentProvider} for the selective workspace importer UI
 * @author pit
 *
 */
public class ContentProvider implements ITreeContentProvider {
	
	private Workspace workspace;
	private Workspace[] elements = new Workspace[1];

	public ContentProvider() {	
	}
	
	@Configurable
	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
		this.elements[0] = workspace;
	}

	@Override
	public Object[] getChildren(Object arg0) {

		if (arg0 instanceof Workspace) {
			Workspace workspace = (Workspace) arg0;			 
			List<WorkingSet> workingSets = workspace.getWorkingSets();
			List<Project> projects = workspace.getProjects();
			
			List<Object> result  = new ArrayList<>( workingSets.size() + projects.size());
			result.addAll( workingSets);
			result.addAll(projects);
			return result.toArray();
		}
		else if (arg0 instanceof WorkingSet) {
			WorkingSet workingSet = (WorkingSet) arg0;
			return workingSet.getProjects().toArray();			
		}
		else if (arg0 instanceof Project) {
			return new Object[0]; 
		}
		else {
			return new Object[0];
		}
	}

	@Override
	public Object[] getElements(Object arg0) {
		if (arg0 != null) {
			return getChildren(arg0);
		}
		return null;
	}

	@Override
	public Object getParent(Object arg0) {
		return null;
	}

	@Override
	public boolean hasChildren(Object arg0) {
		if (arg0 instanceof Workspace) {
			Workspace workspace = (Workspace) arg0;
			return workspace.getWorkingSets().size() > 0 || workspace.getProjects().size() > 0; 
		}
		else if (arg0 instanceof WorkingSet) {
			WorkingSet workingSet = (WorkingSet) arg0;
			return workingSet.getProjects().size() > 0;
		}
		return false;
	}

}
