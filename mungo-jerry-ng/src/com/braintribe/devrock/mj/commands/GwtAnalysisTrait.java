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
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.braintribe.devrock.mj.plugin.MungoJerryPlugin;
import com.braintribe.devrock.mj.plugin.MungoJerryStatus;
import com.braintribe.devrock.mj.ui.dialog.MungojerryDialog;
import com.braintribe.devrock.mj.ui.dialog.analyzer.Analyzer;
import com.braintribe.devrock.mj.ui.dialog.analyzer.AnalyzerException;
import com.braintribe.devrock.mj.ui.dialog.experts.AnalysisControllerImpl;
import com.braintribe.devrock.mj.ui.dialog.experts.ModuleDeclarationWriter;
import com.braintribe.devrock.mj.ui.dialog.tab.AnalysisController;

/**
 * common runner for the GWT analysis
 * @author pit
 *
 */
public interface GwtAnalysisTrait {

	default void process(IProject project)  {
		Display display = PlatformUI.getWorkbench().getDisplay();
		final Shell shell = display.getActiveShell();		

		Job job = new Job("Collecting data for Mungojerry dialog launch") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				AnalysisController analysisController = new AnalysisControllerImpl();
				analysisController.setProject(project);
				Analyzer analyzer = new Analyzer();
				
				analyzer.setGwtArtifactToBeInjected( null);
				try {
					analyzer.extractProtocols( analysisController, monitor);
				} catch (AnalyzerException e1) {
					MungoJerryStatus status = new MungoJerryStatus( "Extracting the protocols failed", e1);
					MungoJerryPlugin.instance().log(status);
					return Status.CANCEL_STATUS;
				}
				
				ModuleDeclarationWriter moduleDeclarationWriter = new ModuleDeclarationWriter();
				moduleDeclarationWriter.setAnalysisController(analysisController);
				
				try {						
					//												
					System.out.println("Start Dialog");
					shell.getDisplay().asyncExec( new Runnable() {
						
						@Override
						public void run() {
							MungojerryDialog mungoJerryDialog = new MungojerryDialog(shell);
							mungoJerryDialog.setAnalyisController(analysisController);
							moduleDeclarationWriter.setParentPage(mungoJerryDialog);
							mungoJerryDialog.setModuleDeclarationWriter( moduleDeclarationWriter);
							mungoJerryDialog.open();									
						}
					});
					
			
					return Status.OK_STATUS;
				} catch (Exception e) {					
					MungoJerryStatus status = new MungoJerryStatus( "Launching the Artifact Wizard failed", e);
					MungoJerryPlugin.instance().log( status);
				}
				return Status.CANCEL_STATUS;
			}
			
		};
		
		try {
			job.schedule();
		} catch (Exception e) {
			MungoJerryStatus status = new MungoJerryStatus( "Running the Artifact Wizard failed", e);
			MungoJerryPlugin.instance().log( status);
		}
	}
}
