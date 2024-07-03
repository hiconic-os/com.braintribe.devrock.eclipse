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
package com.braintribe.devrock.zed.ui.transposer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.braintribe.devrock.zarathud.model.module.ImportedModuleNode;
import com.braintribe.devrock.zarathud.model.module.PackageNode;
import com.braintribe.devrock.zed.ui.ZedViewingContext;
import com.braintribe.zarathud.model.forensics.ModuleForensicsResult;
import com.braintribe.zarathud.model.forensics.data.ImportedModule;
import com.braintribe.zarathud.model.forensics.data.ModuleReference;

/**
 * transposer the {@link ModuleForensicsResult}
 * @author pit
 *
 */
public class ModuleAnalysisContentTransposer {

	private Map<String, ImportedModuleNode> importedToArtifactNodes = new HashMap<>();
	private Map<String, PackageNode> packageToPackageNodes = new HashMap<>();
	private Map<String, ImportedModuleNode> packageToArtifactNodes = new HashMap<>();
	
	 				
	public List<PackageNode> transpose(ZedViewingContext context, ModuleForensicsResult moduleForensicsResult) {
		List<PackageNode> result = new ArrayList<>();
		List<ImportedModule> requiredImportModules = moduleForensicsResult.getRequiredImportModules();
		for (ImportedModule im : requiredImportModules) {
			importedToArtifactNodes.put( im.getArtifactName(), transpose( context, im));
		}
					
		List<ModuleReference> moduleImports = moduleForensicsResult.getModuleImports();
		for (ModuleReference mr : moduleImports) {				
			for (String pkg : mr.getRequiredPackages()) {
				PackageNode pn = PackageNode.T.create();
				pn.setImportedPackageName(pkg);
				pn.getChildren().add( packageToArtifactNodes.get( pkg));
				result.add(pn);
			}				
		}
		
		result.sort( new Comparator<PackageNode>() {

			@Override
			public int compare(PackageNode o1, PackageNode o2) {			
				return o1.getImportedPackageName().compareToIgnoreCase(o2.getImportedPackageName());
			}
			
		});
		return result;
	}
	
	

	private ImportedModuleNode transpose(ZedViewingContext context, ImportedModule im) {
		ImportedModuleNode node = ImportedModuleNode.T.create();
		node.setArtifactName( im.getArtifactName());
		node.setModuleName( im.getModuleName());
		for (String re : im.getRequiredExports()) {			
			PackageNode pn = PackageNode.T.create();
			pn.setProvidingModule(node);
			pn.setImportedPackageName( re);
			node.getChildren().add(pn);				
			packageToPackageNodes.put( re, pn);
			packageToArtifactNodes.put(re, node);
		}
		return node;						
	}

}
