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
package com.braintribe.devrock.dmb.decorators;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;

import com.braintribe.devrock.artifactcontainer.natures.TribefireServicesNature;

/**
 * the decorator for the module carrier nature 
 * 
 * @author pit
 *
 */
public class TribefireServicesNatureDecorator implements ILightweightLabelDecorator {
	private ImageDescriptor image;

	public TribefireServicesNatureDecorator() {
		image = ImageDescriptor.createFromFile(TribefireServicesNatureDecorator.class, "carrier.png");
	}

	@Override
	public void addListener(ILabelProviderListener arg0) {
	}

	@Override
	public void dispose() {		
	}

	@Override
	public boolean isLabelProperty(Object element, String arg1) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {
	}

	@Override
	public void decorate(Object element, IDecoration decoration) {
		if (element instanceof IProject == false)
			return;
		IProject project = (IProject) element;
		if (hasTribefireServicesNature(project)) {
			decoration.addOverlay(image, IDecoration.TOP_LEFT);
		}
	}

	private boolean hasTribefireServicesNature(IProject project) {
		try {
			IProjectDescription descriptions = project.getDescription();
			String[] natureIds = descriptions.getNatureIds();
			if (natureIds == null || natureIds.length == 0)
				return false;
			for (String id : natureIds) {
				if (id.equalsIgnoreCase(TribefireServicesNature.NATURE_ID))
					return true;
			}

		} catch (CoreException e) {
		}
		return false;
	}

}
