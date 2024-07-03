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
package com.braintribe.devrock.mungojerry.preferences.maven;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

import com.braintribe.model.maven.settings.Repository;

public class MavenPreferencesTreePolicyLabelProvider extends LabelProvider implements IStyledLabelProvider, ToolTipProvider {
	private Image dynamicUpdatePolicyImage; 
	private Image nonDynamicUpdatePolicyImage;
	private MavenPreferencesTreeRegistry registry;
	

	public MavenPreferencesTreePolicyLabelProvider(MavenPreferencesTreeRegistry registry) {
		this.registry = registry;
		ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(getClass(), "arrow_refresh_small.png");
		dynamicUpdatePolicyImage = imageDescriptor.createImage();
		imageDescriptor = ImageDescriptor.createFromFile( getClass(), "find.png");
		nonDynamicUpdatePolicyImage = imageDescriptor.createImage();
	}

	@Override
	public StyledString getStyledText(Object element) {
		if (element instanceof Repository) {
			Repository repository = (Repository) element;
			if (registry.getDynamicUpdatePolicySupport(repository)) {
				StyledString styledString = new StyledString( "dynamic");
				return styledString;
			}
			else {
				StyledString styledString = new StyledString( "maven");
				return styledString;
			}
		}
		return new StyledString();
			
	}

	@Override
	public Image getImage(Object element) {	
		if (element instanceof Repository) {
			Repository repository = (Repository) element;
			if (registry.getDynamicUpdatePolicySupport(repository)) {
				return dynamicUpdatePolicyImage;				
			}
			else {
				return nonDynamicUpdatePolicyImage;
			}
		}
		return super.getImage(element);
	}

	@Override
	public void dispose() {
		if (dynamicUpdatePolicyImage != null)
			dynamicUpdatePolicyImage.dispose();
		if (nonDynamicUpdatePolicyImage != null)
			nonDynamicUpdatePolicyImage.dispose();
		super.dispose();
	}

	@Override
	public String getToolTipText(Object element) {
		if (element instanceof Repository) {
			Repository repository = (Repository) element;
			if (registry.getDynamicUpdatePolicySupport(repository)) {
				return new String("dynamic update policy via ravenhurst");
			}
			else {
				return new String("maven update policy " + repository.getReleases().getUpdatePolicy());
			}
		}
		return null;
	}
	

	
}
