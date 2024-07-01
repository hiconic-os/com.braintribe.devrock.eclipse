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
package com.braintribe.devrock.artifactcontainer.commands;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.braintribe.devrock.artifactcontainer.ArtifactContainerPlugin;
import com.braintribe.devrock.artifactcontainer.ArtifactContainerStatus;
import com.braintribe.devrock.artifactcontainer.ui.ant.AntWizardDialog;
import com.braintribe.model.artifact.Artifact;
import com.braintribe.plugin.commons.commands.SingleDropdownHandler;
import com.braintribe.plugin.commons.selection.SelectionExtractor;

/**
 * command for the artifact cloner / copier
 * @author pit
 *
 */
public class AntWizardCommand extends SingleDropdownHandler {
	
	@Override
	public void process(String parameter) {	
		Display display = PlatformUI.getWorkbench().getDisplay();
		final Shell shell = display.getActiveShell();		
		ISelection selection = SelectionExtractor.getCurrentPackageExplorerSelection();
		final List<Artifact> selectedArtifacts = SelectionExtractor.extractSelectedProjectsArtifact(selection);		
		
		
		Job job = new Job("Running Ant Wizard") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {						
					//																					
					shell.getDisplay().asyncExec( new Runnable() {
						
						@Override
						public void run() {
							AntWizardDialog antWizard = new AntWizardDialog( shell);
							if (selectedArtifacts != null) {
								antWizard.setSelectedTargetArtifacts(selectedArtifacts);								
							}
							antWizard.open();									
						}
					});
					
			
					return Status.OK_STATUS;
				} catch (Exception e) {		
					ArtifactContainerStatus status = new ArtifactContainerStatus( "Launching the Ant Wizard failed", e);
					ArtifactContainerPlugin.getInstance().log(status);									
				}
				return Status.CANCEL_STATUS;
			}
			
		};
		
		try {
			job.schedule();
		} catch (Exception e) {
			ArtifactContainerStatus status = new ArtifactContainerStatus( "Running the Ant Wizard failed", e);
			ArtifactContainerPlugin.getInstance().log(status);	
		}
	}	
}
