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
package com.braintribe.devrock.artifactcontainer.views.dependency.tabs;

import java.util.Set;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;

import com.braintribe.model.artifact.Dependency;
import com.braintribe.model.artifact.Solution;

public class DependencyTab extends AbstractDependencyTab {
	
	public DependencyTab(Display display) {
		super(display);
		setColumnNames( new String [] { "Artifact", "Assigned Version", "Requested Version",  "Group", "Classifier", "Scope"});
		setColumnWeights( new int [] { 150, 50, 50, 150, 50, 50 });
	}
						
	@Override
	protected void buildContents(boolean interactive) {
		super.buildContents(interactive);
		buildEntry( terminalSolution, tree, interactive);		
	}
			
	@Override
	protected void buildEntriesForDependencyItem( TreeItem parent, Dependency dependency, boolean interactive) {
		Set<Solution> solutions = dependency.getSolutions();
		for (Solution solution : solutions) {
			buildEntry( solution, parent, interactive);
		}
	}

	@Override
	protected void broadcastTabState() {
		if (!ensureMonitorData()) {
			super.broadcastTabState();
			return;
		}
		if (terminalSolution.getDependencies().size() > 0) {
			setTabState( DependencyViewTabState.validState);
		}
		else {
			super.broadcastTabState();
		}
	}	
	
}
