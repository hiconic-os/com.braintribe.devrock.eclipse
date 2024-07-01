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
package com.braintribe.devrock.arb.listener;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;

import com.braintribe.devrock.arb.plugin.ArtifactReflectionBuilderPlugin;
import com.braintribe.devrock.arb.plugin.ArtifactReflectionBuilderStatus;

public class ResourceChangeListener implements IResourceChangeListener {
	private ResourceVisitor resourceVisitor = null;
			
	
	public ResourceChangeListener() {	
		resourceVisitor = new ResourceVisitor();
	}
	
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		try {			
			
			IResourceDelta delta = event.getDelta();
			// no changes? 
			if (delta == null)
				return;
			
			int kind = delta.getKind();
			switch( kind ) {
				case IResourceDelta.CHANGED : {				
					int flags = delta.getFlags();
					if ((flags & IResourceDelta.MARKERS) != 0)
						return;
				}
			}
			
			// install visitor - to detect changes in the pom, see ResourceVisitor
			try {
				delta.accept( resourceVisitor);
			} catch (CoreException e1) {		
				e1.printStackTrace();
			}
					
		} catch (Exception e) {			
			ArtifactReflectionBuilderStatus status = new ArtifactReflectionBuilderStatus("error while reacting to resource change", e);
			ArtifactReflectionBuilderPlugin.instance().log(status);	
		}
	}
	
}
