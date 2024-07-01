// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.devrock.ac.container.updater;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;

import com.braintribe.cfg.DestructionAware;
import com.braintribe.devrock.plugin.DevrockPlugin;
import com.braintribe.logging.Logger;

/**
 * an intermediate between the IResourceChangeListener and actual Projectupdater..
 * waits a while before it starts processing to make sure it gets all of the data ready to process  
 */
public class DeferredProjectUpdater implements DestructionAware {
	private static Logger log = Logger.getLogger(DeferredProjectUpdater.class);

	private Set<IProject> projectsToUpdate = new HashSet<>();
	
	private Dispatcher dispatcher = new Dispatcher(this::getProjects, this::clearProjects);
	
	private Object monitor = new Object();
	
	private long currentStamp;
	
	private long delta = 2000;
	
	
	public DeferredProjectUpdater() {
		dispatcher.start();
	}
	
	private Set<IProject> getProjects(){
		return projectsToUpdate;
	}
	
	private void clearProjects( boolean cleared) {
		projectsToUpdate.clear();
	}
	
	/**
	 * @param project - adds one project and resets the timestamp
	 */
	public void addProjectToUpdate(IProject project) {
		
		synchronized (monitor) {
			projectsToUpdate.add(project);
			long oldStamp = currentStamp;
			currentStamp = System.currentTimeMillis();
			log.debug("Updated from [" + oldStamp + "] to [" + currentStamp + "] to: " + project.getName());
		}
	}
	
	/**
	 * @param projects - adds all passed projects and resets the timestamp
	 */
	public void addProjectToUpdate(Collection<IProject> projects) {
		
		synchronized (monitor) {
			projectsToUpdate.addAll(projects);
			long oldStamp = currentStamp;
			currentStamp = System.currentTimeMillis();
			log.debug("Updated from [" + oldStamp + "] to [" + currentStamp + "] to: " + projects.stream().map( p -> p.getName()).collect( Collectors.joining(",")));
		}
	}
	
	
	/**
	 * the actual dispatcher thread
	 */
	private class Dispatcher extends Thread  {
		private Supplier<Set<IProject>> supplier;
		private Consumer<Boolean> consumer;
		
		Dispatcher( Supplier<Set<IProject>> supplier, Consumer<Boolean> consumer) {
			this.supplier = supplier;
			this.consumer = consumer;
		}

		@Override
		public void run() {
			while (!Thread.interrupted()) {
				try {
					Thread.sleep( delta / 2);
					synchronized (monitor) {
						Set<IProject> prjs = supplier.get();
						if (!prjs.isEmpty()) {
							long time = System.currentTimeMillis();
							if ((time - currentStamp) > delta) {
								DevrockPlugin.instance().forceRefreshOnProjectView();
								
								DevrockPlugin.mcBridge().close();
								
								log.debug("passing projects to updater :" + projectsToUpdate.stream().map( p -> p.getName()).collect( Collectors.joining(",")));
								DependerUpdater dependerUpdater = new DependerUpdater(null);
								dependerUpdater.setSelectedProjects( projectsToUpdate);																
								consumer.accept(true);
								// launch the workspace job
								dependerUpdater.runAsJob();
							}
						}						
					}
				} catch (InterruptedException e) {
					break;
				}
			}
		}
		
	}
	
	@Override
	public void preDestroy() {
		dispatcher.interrupt();
		try {
			dispatcher.join();
		}
		catch (InterruptedException e) {
			// NOP
		}
	}
}
