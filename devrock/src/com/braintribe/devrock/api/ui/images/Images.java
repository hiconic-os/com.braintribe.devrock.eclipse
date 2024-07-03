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
package com.braintribe.devrock.api.ui.images;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.services.IDisposable;

public class Images implements IDisposable {
	private Map<String, Image> images = new ConcurrentHashMap<>();

	public Image addImage( String key, Class<?> owner, String name) {
		Image image = images.computeIfAbsent( key, k -> {		
			ImageDescriptor descriptor = ImageDescriptor.createFromFile(owner, name);
			return descriptor.createImage();
		});			
		return image;
	}
	
	public Image getImage(String key) {
		return images.get(key);
	}

	@Override
	public void dispose() {
		images.values().stream().forEach( i -> i.dispose());
		images.clear();
	}

	
	
}
