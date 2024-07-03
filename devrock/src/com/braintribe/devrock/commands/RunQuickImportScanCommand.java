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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.braintribe.devrock.plugin.DevrockPlugin;
import com.braintribe.model.artifact.essential.VersionedArtifactIdentification;

/**
 * trigger both the 'repository import' and the 'quick import' controllers to rescan..
 * repository controller : it scans the 'remote', 'local' repositories and the sources (just to get the {@link VersionedArtifactIdentification}) 
 * quick controller : scans only the 'sources' - as it needs the .project files
 *  
 * sources   
 * 
 * @author pit
 *
 */
public class RunQuickImportScanCommand extends AbstractHandler  {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {		
		DevrockPlugin.instance().quickImportController().scheduleRescan();
		DevrockPlugin.instance().repositoryImportController().scheduleRescan();
		return null;
	}
	

}
