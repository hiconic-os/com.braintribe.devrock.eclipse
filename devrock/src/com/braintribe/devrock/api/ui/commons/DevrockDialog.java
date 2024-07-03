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
package com.braintribe.devrock.api.ui.commons;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeColumn;

import com.braintribe.devrock.plugin.DevrockPlugin;

/**
 * @author peter.gazdik
 */
public abstract class DevrockDialog extends Dialog {

	public DevrockDialog(IShellProvider parentShell) {
		super(parentShell);
	}

	public DevrockDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected final Point getInitialSize() {
		Rectangle shellBounds = DevrockPlugin.instance().uiSupport().getCustomValue(getWindowSizeKey());
		return shellBounds == null ? getDrInitialSize() : new Point(shellBounds.width, shellBounds.height);
	}

	protected abstract Point getDrInitialSize();

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.addListener(SWT.Resize, e -> DevrockPlugin.instance().uiSupport().setCustomValue(getWindowSizeKey(), newShell.getBounds()));
	}

	protected void makeColumnResizableAndSticky(TreeColumn column, String text, int defaultWidth) {
		String key = getWindowSizeKey() + ":" + text;
		UiSupport uiSupport = DevrockPlugin.instance().uiSupport();

		Integer width = uiSupport.getCustomValue(key);

        column.setText(text);
		column.setWidth(width == null ? defaultWidth : width);
		column.addListener(SWT.Resize, e -> uiSupport.setCustomValue(key, column.getWidth()));
        column.setResizable(true);

	}

	protected String getWindowSizeKey() {
		return getClass().getName();
	}

}
