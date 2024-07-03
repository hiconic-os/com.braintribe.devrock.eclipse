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
package com.braintribe.devrock.artifactcontainer.plugin.preferences.bias;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

public class RepositoryActivityColumnLabelProvider extends ColumnLabelProvider {
	
	private Image activeImage;
	private Image inactiveImage;
	
	public RepositoryActivityColumnLabelProvider() {
		ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(RepositoryActivityColumnLabelProvider.class, "include_obj.gif");
		activeImage = imageDescriptor.createImage();
		
		imageDescriptor = ImageDescriptor.createFromFile(RepositoryActivityColumnLabelProvider.class, "exclude_obj.gif");
		inactiveImage = imageDescriptor.createImage();		
	}

	@Override
	public Image getImage(Object element) {
		RepositoryExpression expression = (RepositoryExpression) element;		
		return expression.getIsActive() ? activeImage : inactiveImage;
	}

	@Override
	public String getText(Object element) {
		RepositoryExpression expression = (RepositoryExpression) element;		
		return expression.getIsActive() ? "included" : "excluded"; 
	}

	@Override
	public void dispose() {	
		activeImage.dispose();
		inactiveImage.dispose();
		super.dispose();
	}

}
