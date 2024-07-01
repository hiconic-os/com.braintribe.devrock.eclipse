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
package com.braintribe.devrock.ac.commands;

import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.braintribe.devrock.ac.container.updater.DependerUpdater;
import com.braintribe.devrock.api.selection.SelectionExtracter;

public class SyncronizeDependersCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);		
		
		ISelection selection = SelectionExtracter.currentSelection( activeWorkbenchWindow);
		Set<IProject> selectedProjects = SelectionExtracter.selectedProjects(selection);
		
		DependerUpdater dependerUpdater = new DependerUpdater(activeWorkbenchWindow);
		dependerUpdater.setSelectedProjects(selectedProjects);
		
		dependerUpdater.runAsJob();

		return null;
	}

}
