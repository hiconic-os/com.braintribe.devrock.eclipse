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

import org.eclipse.core.resources.IProject;

/**
 * information container for {@link IProject} in the workspace 
 * 
 * currently overkill (no other info than the {@link IProject#getModificationStamp()}
 * 
 * @author pit
 */
public class ProjectInfoContainer {

	private long currentModificationStamp;
	private String currentMd5;
	
	/**
	 * @return - the modification stamp as AC has seen while building the CP
	 */
	public long getCurrentModificationStamp() {
		return currentModificationStamp;
	}
	public void setCurrentModificationStamp(long currentModificationStamp) {
		this.currentModificationStamp = currentModificationStamp;
	}
	public String getCurrentMd5() {
		return currentMd5;
	}
	public void setCurrentMd5(String currentMd5) {
		this.currentMd5 = currentMd5;
	}		
	
	
}
