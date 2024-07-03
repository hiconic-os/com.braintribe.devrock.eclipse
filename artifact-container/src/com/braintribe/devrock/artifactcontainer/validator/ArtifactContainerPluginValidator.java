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
package com.braintribe.devrock.artifactcontainer.validator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.braintribe.devrock.artifactcontainer.ArtifactContainerPlugin;
import com.braintribe.devrock.artifactcontainer.ArtifactContainerStatus;
import com.braintribe.plugin.commons.preferences.validator.CompoundValidator;
import com.braintribe.plugin.commons.preferences.validator.SettingsValidator;
import com.braintribe.plugin.commons.preferences.validator.ValidationResult;

public class ArtifactContainerPluginValidator implements CompoundValidator {	
	private List<SettingsValidator> validators = new ArrayList<SettingsValidator>();
	private SubMonitor monitor;
	
	@Override
	public void addValidator(SettingsValidator validator) {
		validators.add(validator);

	}

	@Override
	public boolean validate( IProgressMonitor progressMonitor) {
		boolean overallResult = true;
		List<ValidationResult> results = new ArrayList<ValidationResult>();
		monitor = SubMonitor.convert(progressMonitor, validators.size());
		for (SettingsValidator validator : validators) {
			monitor.subTask("calling validator ");
			ValidationResult validationResult = validator.validate();
			if (!validationResult.getValidationState()) {
				overallResult = false;		
				results.add(validationResult);
			}
			monitor.split(1);
		}
		if (!overallResult) {
			Display display = PlatformUI.getWorkbench().getDisplay();
			display.asyncExec( new Runnable() {			
				@Override
				public void run() {
					Shell shell = display.getActiveShell();					
					ArtifactContainerPluginValidatorDialog dlg = new ArtifactContainerPluginValidatorDialog(shell);
					dlg.setResultsToDisplay(results);
					dlg.open();									
				}
			});
			ArtifactContainerStatus status = new ArtifactContainerStatus( "Validation has found critical errors in setup", IStatus.ERROR);
			ArtifactContainerPlugin.getInstance().log(status);	
		}
		else {
			ArtifactContainerStatus status = new ArtifactContainerStatus( "Validation has succeeded without any problems", IStatus.INFO);
			ArtifactContainerPlugin.getInstance().log(status);	
		}
		
		return overallResult;
	}

}
