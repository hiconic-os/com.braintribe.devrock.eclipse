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
package com.braintribe.devrock.preferences.pages.scan;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.braintribe.devrock.eclipse.model.scan.SourceRepositoryEntry;

public class PathColumnLabelProvider extends ColumnLabelProvider {	
	private Display display;

	public PathColumnLabelProvider(Display display) {
		super();
		this.display = display;

	}

	@Override
	public String getText(Object element) {
		SourceRepositoryEntry pairing = (SourceRepositoryEntry) element;		
		return pairing.getActualFile();
	}

	@Override
	public String getToolTipText(Object element) {
		SourceRepositoryEntry pairing = (SourceRepositoryEntry) element;
		String tooltipMsg = "";
		if (pairing.getSymbolLink()) {
			File file = new File(pairing.getActualFile());			
			try {
				Path path = Files.readSymbolicLink( file.toPath());
				tooltipMsg = "target : " + path.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tooltipMsg.length() > 0 ? tooltipMsg : pairing.getPath();	
	}

	@Override
	public Color getForeground(Object element) {
		SourceRepositoryEntry pairing = (SourceRepositoryEntry) element;
		if (pairing.getSymbolLink()) {
			return display.getSystemColor( SWT.COLOR_DARK_GRAY);
		}
		return super.getForeground(element);
	}

}
