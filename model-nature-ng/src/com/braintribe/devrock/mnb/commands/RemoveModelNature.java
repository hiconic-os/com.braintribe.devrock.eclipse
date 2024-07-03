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
package com.braintribe.devrock.mnb.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;

import com.braintribe.devrock.api.nature.NatureHelper;
import com.braintribe.devrock.api.selection.SelectionExtracter;
import com.braintribe.devrock.mnb.natures.ModelNature;
import com.braintribe.devrock.mnb.plugin.ModelBuilderPlugin;
import com.braintribe.devrock.mnb.plugin.ModelBuilderStatus;

/**
 * remove the model nature from the currently selected project
 * @author pit
 *
 */
public class RemoveModelNature extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IProject project = SelectionExtracter.currentProject( SelectionExtracter.currentSelection());
		if (project != null) {
			if (!NatureHelper.removeNature(project, ModelNature.NATURE_ID)) {
				String msg = "error while removing nature [" + ModelNature.NATURE_ID + "] to project [" + project.getName() + "]";
				ModelBuilderStatus status = new ModelBuilderStatus( msg, IStatus.ERROR);
				ModelBuilderPlugin.instance().log(status);				
			}
		}
		return null;
	}

}
