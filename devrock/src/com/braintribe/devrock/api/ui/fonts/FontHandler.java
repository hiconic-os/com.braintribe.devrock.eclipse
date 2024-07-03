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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * simple handler for fonts.. 
 * @author pit
 *
 */
public class FontHandler {
	
	private Font bigFont;
	private Font boldFont;
	private Font italicFont;

	public void buildCommonFonts(Composite parent) {
		Font initialFont = parent.getFont();		
		// big font 
		bigFont = buildBigFont(parent.getDisplay(), initialFont);		
		boldFont = buildBoldFont(parent.getDisplay(), initialFont);		
		italicFont = buildItalicFont(parent.getDisplay(), initialFont);						
	}
	public void buildCommonFonts(Display display, Font initialFont) {		
		// big font 
		bigFont = buildBigFont(display, initialFont);		
		boldFont = buildBoldFont( display, initialFont);		
		italicFont = buildItalicFont( display, initialFont);						
	}

	public static Font buildItalicFont(Display display, Font initialFont) {
		FontData [] fontDataItalic = initialFont.getFontData();
		for (FontData data : fontDataItalic) {
			data.setStyle( data.getStyle() | SWT.ITALIC);				
		}
		return new Font( display, fontDataItalic);
	}

	public static Font buildBoldFont(Display display, Font initialFont) {
		FontData [] fontDataBold = initialFont.getFontData();
		for (FontData data : fontDataBold) {
			data.setStyle( data.getStyle() | SWT.BOLD);				
		}
		return new Font( display, fontDataBold);
	}

	public static Font buildBigFont(Display display, Font initialFont) {
		FontData [] fontDataBig = initialFont.getFontData();
		for (FontData data : fontDataBig) {
			data.setHeight( data.getHeight() + (data.getHeight() / 5));				
		}
		return new Font( display, fontDataBig);
	}
	
	public void disposeCommonFonts() {
		if (bigFont != null)
			bigFont.dispose();
		
		if (boldFont != null)
			boldFont.dispose();
		
		if (italicFont != null) {
			italicFont.dispose();
		}
	}
	
}
