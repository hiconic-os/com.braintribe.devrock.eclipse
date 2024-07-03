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
package com.braintribe.devrock.greyface.process.scan;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.braintribe.build.artifact.name.NameParser;
import com.braintribe.build.artifact.retrieval.multi.resolving.ResolvingException;
import com.braintribe.devrock.greyface.GreyfacePlugin;
import com.braintribe.devrock.greyface.GreyfaceStatus;
import com.braintribe.devrock.greyface.process.notification.ScanContext;
import com.braintribe.devrock.greyface.process.notification.ScanProcessListener;
import com.braintribe.devrock.greyface.process.retrieval.CompoundDependencyResolver;
import com.braintribe.devrock.greyface.scope.GreyfaceScope;
import com.braintribe.model.artifact.Dependency;
import com.braintribe.model.malaclypse.cfg.preferences.gf.RepositorySetting;

public class SynchronousScannerImpl extends AbstractScannerImpl {
	
	@Override
	protected void runScan(IProgressMonitor monitor, ScanContext context) {				
		List<RepositorySetting> sources = context.getSourceRepositories();
		List<Dependency> dependencies = extractDependenciesFromNameList(context.getCondensedNames());			
		SynchronousScanExpert scanExpert = new SynchronousScanExpert();
		
		CompoundDependencyResolver resolver = new CompoundDependencyResolver();
		resolver.setSources(sources);
		//scanExpert.setCompoundDependencyResolver(resolver);
		scanExpert.setSources(sources);
		scanExpert.setContext(context);		

		scanExpert.setProgressMonitor(monitor);
		scanExpert.setDependencyCache(dependencyCache);
		GreyfaceScope.getScope().resetScope( resolver);
		
		
		for (ScanProcessListener listener : listeners) {
			listener.acknowledgeStartScan();
			scanExpert.addScanProcessListener(listener);
		}
	
		for (Dependency dependency : dependencies) {
			if (monitor.isCanceled())
				break;
			// 
			try {
				scanExpert.scanDependency( dependency, 0, 0, null);
			} catch (ResolvingException e) {
				String msg = "cannot scan [" + NameParser.buildName(dependency) + "] as [" + e.getMessage() + "]";
				GreyfaceStatus status = new GreyfaceStatus( msg, e);
				GreyfacePlugin.getInstance().getLog().log(status);
			}
		}				
		
		for (ScanProcessListener listener : listeners) {
			listener.acknowledgeStopScan();
		}							
	}
}
