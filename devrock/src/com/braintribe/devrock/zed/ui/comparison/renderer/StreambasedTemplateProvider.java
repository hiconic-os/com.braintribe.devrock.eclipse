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
package com.braintribe.devrock.zed.ui.comparison.renderer;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

import com.braintribe.utils.IOTools;

/**
 * retrieve the content of a file via its {@link InputStream}
 * @author pit
 *
 */
public class StreambasedTemplateProvider implements Supplier<String> {
	String content;

	public StreambasedTemplateProvider( InputStream stream) {	
		try {
			content = IOTools.slurp(stream, "UTF-8");
		} catch (IOException e) {
			throw new IllegalStateException("cannot read template", e);
		}
	}
	@Override
	public String get() {		
		return content;
	}

}
