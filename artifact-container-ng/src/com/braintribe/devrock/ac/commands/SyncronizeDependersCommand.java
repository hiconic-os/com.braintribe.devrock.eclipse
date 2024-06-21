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
