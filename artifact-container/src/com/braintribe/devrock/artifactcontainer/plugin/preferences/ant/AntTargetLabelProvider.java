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
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.braintribe.model.malaclypse.cfg.AntTarget;

public class AntTargetLabelProvider extends LabelProvider implements ITableLabelProvider {
	ImageDescriptor yes_imageDescriptor = ImageDescriptor.createFromFile( AntTargetLabelProvider.class, "task-active.gif");
	ImageDescriptor no_imageDescriptor = ImageDescriptor.createFromFile( AntTargetLabelProvider.class, "task-inactive.gif");
	
	@Override
	public Image getColumnImage(Object object, int columnIndex) {
		AntTarget setting = (AntTarget) object;		
		
		switch (columnIndex) {
			case 0:
				return null;
			case 1: 				
				return null;			
			case 2:		
				return setting.getTransitiveNature() ? yes_imageDescriptor.createImage() : no_imageDescriptor.createImage();			
		}		
		return null;
	}

	@Override
	public String getColumnText(Object object, int columnIndex) {
		AntTarget setting = (AntTarget) object;	
		switch (columnIndex) {
			case 0:
				return setting.getName();
			case 1:
				return setting.getTarget();
			case 2:
				return setting.getTransitiveNature() ? "transitive" : "single artifact";
			default:
				return null;		
		}
	}



}
