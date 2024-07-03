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
package com.braintribe.plugin.commons.selection;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;

/**
 * an expert to extract the currently selected project and/or the currently active working set in the package explorer
 * @author pit
 *
 */
public class TargetProviderImpl implements TargetProvider {
	IWorkingSet activeWorkingSet;
	IProject activeProject;
	
	
	private ISelection extractSelection(){
		IWorkbench iworkbench = PlatformUI.getWorkbench();
		IWorkbenchWindow iworkbenchwindow = iworkbench.getActiveWorkbenchWindow();
		if (iworkbenchwindow == null)
			return null;
		IWorkbenchPage page =  iworkbenchwindow.getActivePage();
		ISelection selection = page.getSelection();
		return selection;
	}
	
	private void extractTargets() {		
		  ISelection selection = extractSelection();
		  if (selection != null) {
			  activeProject = SelectionExtractor.extractSelectedProject(selection);
			  activeWorkingSet = SelectionExtractor.extractSelectedWorkingSet(selection);
		  }
	}
	
	

	@Override
	public void refresh() {
		activeProject = null;
		activeWorkingSet = null;
	}



	@Override
	public IWorkingSet getTargetWorkingSet() {
		if (activeWorkingSet == null) { 
			extractTargets();
		}
		return activeWorkingSet;
	}

	@Override
	public IProject getTargetProject() {
		if (activeProject == null) {
			extractTargets();
		}				
		return activeProject;
	}

	@Override
	public SelectionTuple getSelectionTuple() {
		ISelection selection = extractSelection();
		if (selection == null)
			return null;
		return SelectionExtractor.extractSelectionTuple(selection);
	}
	@Override
	public Set<IProject> getTargetProjects() {
		ISelection selection = extractSelection();
		if (selection == null)
			return new HashSet<IProject>();		
		return SelectionExtractor.extractSelectedProjects(selection);
	}
	
	

}
