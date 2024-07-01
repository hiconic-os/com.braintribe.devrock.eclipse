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
package com.braintribe.devrock.artifactcontainer.control.workspace;

import java.util.Set;

import org.eclipse.core.resources.IProject;

import com.braintribe.build.artifact.name.NameParser;
import com.braintribe.model.artifact.Artifact;

public class ProjectInfoTuple {

	private IProject project;
	private Artifact artifact;
	private boolean lastWalkFailed;
	private Set<ProjectInfoTuple> dependencies;
	
	public ProjectInfoTuple() {
	}
	
	public ProjectInfoTuple( IProject iProject, Artifact artifact) {
		this.project = iProject;
		this.artifact = artifact;
	}
	
	public IProject getProject() {
		return project;
	}
	public void setProject(IProject project) {
		this.project = project;
	}
	public Artifact getArtifact() {
		return artifact;
	}
	public void setArtifact(Artifact artifact) {
		this.artifact = artifact;
	}

	public Set<ProjectInfoTuple> getDependencies() {
		return dependencies;
	}

	public void setDependencies(Set<ProjectInfoTuple> dependencies) {
		this.dependencies = dependencies;
	}

	public boolean getHasLastWalkFailed() {
		return lastWalkFailed;
	}
	public void setHasLastWalkFailed(boolean lastWalkFailed) {
		this.lastWalkFailed = lastWalkFailed;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ProjectInfoTuple) {
			ProjectInfoTuple other = (ProjectInfoTuple) obj;
			return project.getName().equals( other.getProject().getName());
		}
		return false;
	}

	@Override
	public int hashCode() {	
		return project.getName().hashCode();
	}

	@Override
	public String toString() {	
		return project.getName() + "->" + NameParser.buildName( artifact);
	}
	
	
	
	
}
