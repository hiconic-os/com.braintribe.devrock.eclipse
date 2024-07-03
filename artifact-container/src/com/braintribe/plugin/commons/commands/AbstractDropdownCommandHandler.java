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
package com.braintribe.plugin.commons.commands;

import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IWorkingSet;

import com.braintribe.plugin.commons.selection.SelectionTuple;
import com.braintribe.plugin.commons.selection.TargetProvider;
import com.braintribe.plugin.commons.selection.TargetProviderImpl;

public abstract class AbstractDropdownCommandHandler extends AbstractHandler implements TargetProvider {
	
	private TargetProviderImpl targetProvider = new TargetProviderImpl();
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		refresh();
		String parameter = event.getParameter( getParamKey());
		process( parameter);
		return null;	    
	}
  
	public abstract void process( String parameter);  
	public void executeSingle(IProject project){}
	public void executeSingle( IProject project, IProgressMonitor monitor){}
	
	protected abstract String getParamKey();
			

	
	// target provider delegation 
	@Override
	public SelectionTuple getSelectionTuple() {	
		return targetProvider.getSelectionTuple();
	}
	@Override
	public IWorkingSet getTargetWorkingSet() {		
		return targetProvider.getTargetWorkingSet();
	}	
	@Override
	public IProject getTargetProject() {
		return targetProvider.getTargetProject();
	}
	@Override
	public Set<IProject> getTargetProjects() {
		return targetProvider.getTargetProjects();
	}
	@Override
	public void refresh() {
		targetProvider.refresh();	
	}
		
	
}
