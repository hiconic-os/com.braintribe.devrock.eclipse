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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.braintribe.build.artifact.name.NameParser;
import com.braintribe.build.artifact.name.NameParserException;
import com.braintribe.devrock.greyface.GreyfacePlugin;
import com.braintribe.devrock.greyface.GreyfaceStatus;
import com.braintribe.devrock.greyface.process.notification.ScanContext;
import com.braintribe.devrock.greyface.process.notification.ScanProcessListener;
import com.braintribe.devrock.greyface.process.notification.ScanProcessNotificator;
import com.braintribe.model.artifact.Dependency;
import com.braintribe.model.artifact.processing.version.VersionProcessingException;
import com.braintribe.model.artifact.processing.version.VersionRangeProcessor;

public abstract class AbstractScannerImpl implements ScanProcessNotificator, Scanner {
	protected List<ScanProcessListener> listeners = new ArrayList<ScanProcessListener>();
	protected DependencyCache dependencyCache = new DependencyCache();
	
	
	@Override
	public void addScanProcessListener(ScanProcessListener listener) {			
		listeners.add(listener);
	}
	
	@Override
	public void removeScanProcessListener(ScanProcessListener listener) {
		listeners.remove(listener);		
	}
	
	
	protected List<Dependency> extractDependenciesFromNameList( List<String> condensedNames) {
		List<Dependency> result = new ArrayList<Dependency>( condensedNames.size());
		for (String name : condensedNames) {
			Dependency dependency = null;
			try {
				if (name.contains("#")) {
					 dependency = NameParser.parseCondensedDependencyName( name);
				}
				else if (name.contains( ":")){
					String [] tokens = name.split(":");
					dependency = Dependency.T.create();
					dependency.setGroupId( tokens[0]);
					dependency.setArtifactId( tokens[1]);
					dependency.setType( tokens[2]);
					dependency.setVersionRange( VersionRangeProcessor.createFromString( tokens[3]));					
				}
				else {
					name = name.replace('\\', '/');
					String [] tokens = name.split("/");
					dependency = Dependency.T.create();
					dependency.setVersionRange( VersionRangeProcessor.createFromString( tokens[tokens.length-1]));
					dependency.setArtifactId( tokens[ tokens.length-2]);
					// 
					StringBuffer group = new StringBuffer();
					for (int i = 0; i < tokens.length-2; i++) {
						if (group.length() > 0)
							group.append('.');
						group.append( tokens[i]);
					}
					dependency.setGroupId( group.toString());					
				}
			} catch (NameParserException e) {				
				String msg = "cannot parse dependency from [" + name + "] as [" + e.getMessage() +"]";
				GreyfaceStatus status = new GreyfaceStatus( msg, e);
				GreyfacePlugin.getInstance().getLog().log(status);
				continue;			
			} catch (VersionProcessingException e) {
				String msg = "cannot parse version range from [" + name + "] as [" + e.getMessage() +"]";
				GreyfaceStatus status = new GreyfaceStatus( msg, e);
				GreyfacePlugin.getInstance().getLog().log(status);
				continue;	
			}
			catch (Exception e) {
				String msg =  "cannot parse tokens from [" + name + "] as [" + e.getMessage() +"]";
				GreyfaceStatus status = new GreyfaceStatus( msg, e);
				GreyfacePlugin.getInstance().getLog().log(status);
				continue;	
			}
			
			result.add(dependency);
		}		
		return result;
	}

	@Override
	public void scan(IProgressMonitor progressMonitor, ScanContext context) {	 
		RemoteRepositoryExpert expert = new RemoteRepositoryExpert();
		runScan(progressMonitor, context);
		expert.closeHttpAccessContext();
	}
	
	protected abstract void runScan( IProgressMonitor progressMonitor, ScanContext context);
}
