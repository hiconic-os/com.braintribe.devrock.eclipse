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
package com.braintribe.devrock.mj.natures;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

import com.braintribe.devrock.api.nature.CommonNatureIds;


/**
 * nature for GWT libraries
 * @author pit
 *
 */
public class GwtLibraryNature implements IProjectNature {

	public static final String NATURE_ID = CommonNatureIds.NATURE_GWT_LIBRARY; //$NON-NLS-1$
	private IProject project;
	
	@Override
	public void configure() throws CoreException {		
	}
	

	@Override
	public void deconfigure() throws CoreException {
	}

	@Override
	public IProject getProject() {		
		return project;
	}

	@Override
	public void setProject(IProject project) {
		this.project = project;
		
	}

}
