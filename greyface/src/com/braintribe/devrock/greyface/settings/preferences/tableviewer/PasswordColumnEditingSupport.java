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
package com.braintribe.devrock.greyface.settings.preferences.tableviewer;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import com.braintribe.model.malaclypse.cfg.repository.RemoteRepository;

public class PasswordColumnEditingSupport extends EditingSupport {
	private TableViewer viewer;

	public PasswordColumnEditingSupport(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
	}

	@Override
	protected boolean canEdit(Object arg0) {	
		return true;
	}

	@Override
	protected CellEditor getCellEditor(Object arg0) {		
		return new TextCellEditor( viewer.getTable());
	}

	@Override
	protected Object getValue(Object element) {
		RemoteRepository setting = (RemoteRepository) element;	
		return setting.getPassword();
	}

	@Override
	protected void setValue(Object element, Object value) {
		RemoteRepository setting = (RemoteRepository) element;
		setting.setPassword( (String) value);
		viewer.refresh(element);

	}

}
