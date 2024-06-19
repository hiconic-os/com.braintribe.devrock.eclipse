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
package com.braintribe.devrock.ac.container.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;

import com.braintribe.devrock.ac.container.ArtifactContainer;
import com.braintribe.model.artifact.analysis.AnalysisArtifactResolution;

/**
 * simple registry for artifact containers in the workspace..
 *  
 * @author pit
 *
 */
public class WorkspaceContainerRegistry {
	private Map<IProject,ArtifactContainer> projectToContainerMap = new ConcurrentHashMap<>();
	private Map<ArtifactContainer, IProject> containerMapToProjectMap = new ConcurrentHashMap<>();
	private Map<IProject, ProjectInfoContainer> projectToInfoMap = new ConcurrentHashMap<>();
	 
	
	/**
	 * called when a container is initialized
	 * @param project - the {@link IProject}
	 * @param container - the {@link ArtifactContainer}
	 */
	public void acknowledgeContainerAttachment(IProject project, ArtifactContainer container) {
		projectToContainerMap.put(project, container);
		containerMapToProjectMap.put(container, project);
	}
	/**
	 * @param project - the {@link IProject}
	 * @param container - the {@link ArtifactContainer}
	 * @return
	 */
	public boolean acknowledgeContainerDetachment(IProject project, ArtifactContainer container) {		
		boolean mapped = projectToContainerMap.remove(project, container);
		if (mapped) {
			containerMapToProjectMap.remove(container);			
		}
		return mapped;
			
	}
	/**
	 * call via copy constructor of {@link ArtifactContainer}
	 * @param project - the {@link IProject}
	 * @param oldContainer - old {@link ArtifactContainer} instance
	 * @param newContainer - new {@link ArtifactContainer} instance
	 */
	public boolean acknowledgeContainerReassignment(ArtifactContainer oldContainer, ArtifactContainer newContainer) {
		IProject project = containerMapToProjectMap.get(oldContainer);
		if (project == null)
			return false;
		projectToContainerMap.remove(project);		
		projectToContainerMap.put(project, newContainer);
		containerMapToProjectMap.remove(oldContainer);
		containerMapToProjectMap.put(newContainer, project);
		return true;
	}
	
	public boolean hasContainer(IProject project) {
		return projectToContainerMap.containsKey(project);
	}
	
	public ArtifactContainer getContainerOfProject(IProject project) {		
		return projectToContainerMap.get(project);
	}
	
	public IProject getProjectOfContainer( ArtifactContainer container) {
		return containerMapToProjectMap.get(container);
	}
	
	public Collection<IProject> getRegisteredProjects() {
		return projectToContainerMap.keySet();
	}
	
	public Collection<ArtifactContainer> getRegisteredContainers() {
		return containerMapToProjectMap.keySet();
	}
		

	public ProjectInfoContainer acknowledgeContainerRefresh(IProject project, long current) {
		ProjectInfoContainer pic = projectToInfoMap.computeIfAbsent(project, k -> new ProjectInfoContainer());
		pic.setCurrentModificationStamp(current);
		return pic;
	}
	
	public ProjectInfoContainer acknowledgePomEvent(IProject project, String md5) {
		ProjectInfoContainer pic = projectToInfoMap.computeIfAbsent(project, k -> new ProjectInfoContainer());
		pic.setCurrentMd5(md5);
		return pic;
	}
	
	public ProjectInfoContainer getProjectInfoContainer( IProject project) {
		return projectToInfoMap.get(project);
	}
		
}
