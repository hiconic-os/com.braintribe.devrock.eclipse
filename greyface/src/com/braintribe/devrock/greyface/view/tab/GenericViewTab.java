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
package com.braintribe.devrock.greyface.view.tab;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.braintribe.cfg.Configurable;
import com.braintribe.devrock.greyface.view.TabItemImageListener;

public abstract class GenericViewTab {
	protected Display display;
	protected Font italicFont;
	protected Font boldFont;
	protected Font italicBoldFont;
	protected Font bigFont;
	protected String id;
	
	protected TabItemImageListener imageListener;

	protected GenericViewTab( Display display){
		this.display = display;
	}
	
	@Configurable
	public void setImageListener(TabItemImageListener imageListener) {
		this.imageListener = imageListener;
	}
	
	protected Composite createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);		  
		
		Font initialFont = parent.getFont();
		FontData [] fontDataItalic = initialFont.getFontData();
		for (FontData data : fontDataItalic) {
			data.setStyle( data.getStyle() | SWT.ITALIC);		
		}
		italicFont = new Font( display, fontDataItalic);
		
		FontData [] fontDataItalicBold = initialFont.getFontData();
		for (FontData data : fontDataItalicBold) {
			data.setStyle( data.getStyle() | SWT.BOLD | SWT.ITALIC);		
		}
		italicBoldFont = new Font( display, fontDataItalicBold);
		
		FontData [] fontDataBold = initialFont.getFontData();
		for (FontData data : fontDataBold) {
			data.setStyle( data.getStyle() | SWT.BOLD);		
		}
		boldFont = new Font( display, fontDataBold);
		
		FontData [] fontDataBig = initialFont.getFontData();
		for (FontData data : fontDataBig) {
			data.setHeight( data.getHeight() + (data.getHeight() / 5));				
		}
		bigFont = new Font( display, fontDataBig);
		
		
        return composite;
	}
	
	public abstract void adjustSize();
	public String getId() {
		return id;
	}
	
	public void dispose() {
		boldFont.dispose();
		italicFont.dispose();
		bigFont.dispose();
		italicBoldFont.dispose();
	}
	
	public void clear() {}
	
	public int getItemCount() { return 0;}
	
	public void acknowledgeActivation() {}
	public void acknowledgeDeactivation() {}
}
