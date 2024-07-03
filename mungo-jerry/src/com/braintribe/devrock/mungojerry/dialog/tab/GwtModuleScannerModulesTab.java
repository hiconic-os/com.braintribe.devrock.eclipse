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
package com.braintribe.devrock.mungojerry.dialog.tab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.braintribe.build.gwt.GwtDependencyCollector;
import com.braintribe.build.gwt.GwtModule;

public class GwtModuleScannerModulesTab extends GenericGwtViewerTab {
	private static final String TOKEN_COLLECTING_DATA_FOR_MUNGOJERRY_DIALOG_LAUNCH = "Collecting data for Mungojerry launch";
	private static final String MARKER_MODULE = "module";
	private static final String MARKER_LOADING = "loading...";	
	
	public GwtModuleScannerModulesTab(Display display) {
		super(display);	
		setColumnNames(new String [] { "Modules", });
		setColumnWeights( new int [] {300,});			
	}

	@Override
	public Composite createControl(Composite parent) {
		return super.createControl(parent);
	}
	
	private void buildItem( Object parent, GwtModule module) {			
		
		TreeItem item = null;
		if (parent instanceof Tree)
			item = new TreeItem( (Tree) parent, SWT.NONE);
		else
			item = new TreeItem( (TreeItem) parent, SWT.NONE);
		
		List<String> texts = new ArrayList<String>();
		texts.add( module.getModuleName());
		texts.add( module.getSourcePackage());		
		item.setText( texts.toArray( new String[0]));
		
		item.setData( MARKER_MODULE, module);
		
		if (module.getInheritedModules().size() > 0) {
			TreeItem loader = new TreeItem( item, SWT.NONE);
			loader.setText(  new String[] {MARKER_LOADING});
		}
		
		
	}
	
	public void setup(final String moduleName) {
		
		// runn
		
		Job job = new Job( TOKEN_COLLECTING_DATA_FOR_MUNGOJERRY_DIALOG_LAUNCH) {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					GwtModuleScannerMonitor moduleMonitor = new GwtModuleScannerMonitor();
					moduleMonitor.setMonitor( monitor);
					GwtDependencyCollector collector = new GwtDependencyCollector();
					collector.setMonitor( moduleMonitor);
					collector.setArtifactClassesFolder( analysisController.getOutputFolder());
					collector.setArtifactSourceFolder( analysisController.getSourceFolder());
					collector.setClasspath( analysisController.getClasspathAsFiles());
					collector.setModuleName( moduleName);
					collector.scanForModules();
					// 
					final List<GwtModule> modules = new ArrayList<GwtModule>();
					modules.addAll( collector.getRequiredModules());
					// sort..
					//
					Collections.sort( modules, new Comparator<GwtModule>() {
							@Override
							public int compare(GwtModule m1, GwtModule m2) {
								return m1.getModuleName().compareToIgnoreCase( m2.getModuleName());
							}
					});
					analysisController.setModules( modules);

					display.asyncExec( new Runnable() {						
						@Override
						public void run() {					
							tree.removeAll();
							for (GwtModule gwtModule : modules) {
								buildItem(tree, gwtModule);
							}
						}
					});
					
				} catch (Exception e) {
					return Status.CANCEL_STATUS;
				}
				return Status.OK_STATUS;
			}
		};
		job.schedule();				
				
	}
	
	@Override
	public void handleEvent(Event event) {		
		final TreeItem solutionItem = (TreeItem) event.item;
		TreeItem [] children = solutionItem.getItems();
		if (
			(children != null) &&
			(children.length == 1) 
		   ) {
			final TreeItem suspect = children[0];
			String name = suspect.getText(0);
			if (name.equalsIgnoreCase( MARKER_LOADING) == false)
				return;
			//
			// 
			display.asyncExec( new Runnable() {
				
				@Override
				public void run() {
					suspect.dispose();
					
					GwtModule parentModule = (GwtModule) solutionItem.getData( MARKER_MODULE);
					for (GwtModule module : parentModule.getInheritedModules()) {
						buildItem( solutionItem, module);
					}
				}
			});
		}
		
	}	
}
