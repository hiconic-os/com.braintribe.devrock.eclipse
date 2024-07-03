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
package com.braintribe.devrock.artifactcontainer.plugin.preferences.ant;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.braintribe.model.malaclypse.cfg.AntTarget;

public class TransitivityColumnLabelProvider extends ColumnLabelProvider {
	private Image transitiveImage;
	private Image intransitiveImage;
	
	public TransitivityColumnLabelProvider() {
		ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile( TransitivityColumnLabelProvider.class, "task-active.gif");
		transitiveImage = imageDescriptor.createImage();
		
		imageDescriptor = ImageDescriptor.createFromFile( TransitivityColumnLabelProvider.class, "task-inactive.gif");
		intransitiveImage = imageDescriptor.createImage();
	}

	@Override
	public Image getImage(Object element) {
		AntTarget setting = (AntTarget) element;
		return setting.getTransitiveNature() ? transitiveImage : intransitiveImage;		
	}

	@Override
	public String getText(Object element) {
		return null;
	}

	@Override
	public String getToolTipText(Object element) {
		AntTarget setting = (AntTarget) element;
		if (setting.getTransitiveNature()) {
			return "target is a transitive target (runs in main artifact directory)"; 			
		}
		else {
			return "target is an intransitive target (runs in local artifact directory)";
		}
	}

	@Override
	public void dispose() {
		transitiveImage.dispose();
		intransitiveImage.dispose();
		super.dispose();
	}

	
}
