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
package com.braintribe.devrock.greyface.view.tab.result;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.braintribe.devrock.greyface.view.tab.GenericViewTab;

public abstract class CommonResultSubTab extends GenericViewTab {

	protected Image successImage;
	protected Image failImage;
	protected Image activityImage;
		
	public CommonResultSubTab(Display display) {
		super(display);
		
		ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile( GenericViewTab.class, "accept.png");
		successImage = imageDescriptor.createImage();
		
		imageDescriptor = ImageDescriptor.createFromFile( GenericViewTab.class, "exclamation--frame.png");
		failImage = imageDescriptor.createImage();
		
		imageDescriptor = ImageDescriptor.createFromFile( GenericViewTab.class, "arrow_refresh_small.png");
		activityImage = imageDescriptor.createImage();							
	}

	@Override
	public void dispose() {	
		successImage.dispose();
		failImage.dispose();
		activityImage.dispose();		
		super.dispose();
	}


}
