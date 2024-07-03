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
package com.braintribe.devrock.commands;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.braintribe.devrock.api.commands.SingleDropdownHandler;
import com.braintribe.devrock.api.selection.EnhancedSelectionExtracter;
import com.braintribe.devrock.api.selection.SelectionExtracter;
import com.braintribe.devrock.eclipse.model.identification.EnhancedCompiledArtifactIdentification;
import com.braintribe.devrock.plugin.DevrockPlugin;
import com.braintribe.devrock.plugin.DevrockPluginStatus;
import com.braintribe.devrock.tbrunner.TbWizardDialog;



/**
 * command for TB runner
 * @author pit
 *
 */
public class TbWizardCommand extends SingleDropdownHandler {
	
	@Override
	public void process(String parameter) {	
		Display display = PlatformUI.getWorkbench().getDisplay();
		final Shell shell = display.getActiveShell();		
		ISelection selection = SelectionExtracter.currentSelection();
		final List<EnhancedCompiledArtifactIdentification> selectedArtifacts = EnhancedSelectionExtracter.extractSelectedArtifacts(selection);		
		
		
		Job job = new Job("Running TB Wizard") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {						
					//																					
					shell.getDisplay().asyncExec( new Runnable() {
						
						@Override
						public void run() {
							TbWizardDialog antWizard = new TbWizardDialog( shell);
							if (selectedArtifacts != null) {
								antWizard.setSelectedTargetArtifacts(selectedArtifacts);								
							}
							antWizard.open();									
						}
					});
					
			
					return Status.OK_STATUS;
				} catch (Exception e) {		
					DevrockPluginStatus status = new DevrockPluginStatus( "Launching the TB Wizard failed", e);
					DevrockPlugin.instance().log(status);									
				}
				return Status.CANCEL_STATUS;
			}
			
		};
		
		try {
			job.schedule();
		} catch (Exception e) {
			DevrockPluginStatus status = new DevrockPluginStatus( "Running the Ant Wizard failed", e);
			DevrockPlugin.instance().log(status);	
		}
	}	
}
