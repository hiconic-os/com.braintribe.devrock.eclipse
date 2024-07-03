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
package com.braintribe.plugin.commons.ui.tree;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;

public class DefaultTreeItemPainter implements TreeItemPainter {

	private String [] keys;
	
	@Configurable @Required
	public void setKeys(String[] keys) {
		this.keys = keys;
	}
	@Override
	public void paint(TreeItem newItem, TreeItem oldItem) {
		for (String key : keys) {
			Object value = oldItem.getData(key);
			if (value == null)
				continue;
			newItem.setData(key, value);
			
			if (value instanceof Color) {
				newItem.setForeground((Color) value);
				continue;
			}
			if (value instanceof Image) {
				newItem.setImage( (Image) value);
			}
			if (value instanceof Font) {
				newItem.setFont((Font) value);
			}			
		}
	}

}
