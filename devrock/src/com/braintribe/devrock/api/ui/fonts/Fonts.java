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
package com.braintribe.devrock.api.ui.fonts;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.services.IDisposable;

public class Fonts implements IDisposable {
	private Map<String,Font> keyToFontMap = new ConcurrentHashMap<>();
	private Font initialFont;
	private Display display;
	
	
	public Fonts() {
		display = Display.getCurrent();
	}
	public void setInitialFont(Font initialFont) {
		this.initialFont = initialFont;
	}
	
	public Font getInitialFont() {
		return initialFont;
	}
	
	public Font addBigFont(String key) {
		return keyToFontMap.computeIfAbsent(key, k -> {
			FontData [] fontDataBig = initialFont.getFontData();
			for (FontData data : fontDataBig) {
				data.setHeight( data.getHeight() + (data.getHeight() / 5));				
			}
			Font bigFont = new Font( display, fontDataBig);		
			return bigFont;
		});		
	}
	
	public Font addBigFont(String key, FontData[] data) {
		return keyToFontMap.computeIfAbsent(key, k -> {			
			Font bigFont = new Font( display, data);		
			return bigFont;
		});	
	}
	
	@Override
	public void dispose() {
		keyToFontMap.values().stream().forEach( f -> f.dispose());
		
	}
	
	}
