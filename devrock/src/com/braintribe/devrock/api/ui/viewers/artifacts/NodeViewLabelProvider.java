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
package com.braintribe.devrock.api.ui.viewers.artifacts;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;

import com.braintribe.devrock.api.ui.commons.UiSupport;
import com.braintribe.devrock.eclipse.model.storage.ViewerContext;

public interface NodeViewLabelProvider extends IStyledLabelProvider {

	void setUiSupport(UiSupport uiSupport);
	void setUiSupportStylersKey(String uiSupportStylersKey);
	void setViewerContext(ViewerContext viewContext);
	void setRequestHander(DetailRequestHandler requestHander);
}
