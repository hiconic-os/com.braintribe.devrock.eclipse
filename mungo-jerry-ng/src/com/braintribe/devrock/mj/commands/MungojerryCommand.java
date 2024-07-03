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
package com.braintribe.devrock.mj.commands;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ISelection;

import com.braintribe.devrock.api.commands.SingleDropdownHandler;
import com.braintribe.devrock.api.selection.SelectionExtracter;
import com.braintribe.devrock.mj.plugin.MungoJerryPlugin;
import com.braintribe.devrock.mj.plugin.MungoJerryStatus;

/**
 * command for the artifact cloner / copier
 * @author pit
 *
 */
public class MungojerryCommand extends SingleDropdownHandler implements GwtAnalysisTrait {
	
	@Override
	public void process(String parameter) {		
		ISelection selection = SelectionExtracter.currentSelection();
		final IProject project = SelectionExtracter.currentProject( selection);
		
		if (project == null) {
			MungoJerryStatus status = new MungoJerryStatus( "No project selected, no dice", IStatus.INFO);
			MungoJerryPlugin.instance().log(status);			
			return;
		}		 
		process( project);				
	}	
}
