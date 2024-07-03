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

import java.util.List;

import com.braintribe.gm.model.reason.Reason;

public class ReasonFormatter {
	public static final String HTML_SUFFIX = "</p></body></html>";
	public static final String HTML_PREFIX = "<!DOCTYPE html><html><body><p style=\"color:block;font-size:12px;\">";
	
	public static String toHtmlFormattedText( Reason reason, int offset) {
		if (reason == null)
			return "<null>";
		StringBuilder builder = new StringBuilder();
		if (offset != 0) {
			builder.append("<br/>");
		}
		for ( int i = 0; i < offset; i++) { 
			builder.append("&nbsp;");
		}
		
		builder.append( reason.getText());
		List<Reason> attachedReasons = reason.getReasons();
		if (!attachedReasons.isEmpty()) {
			for (Reason child : attachedReasons) {
				builder.append( toHtmlFormattedText( child, offset+1));
			}
		}
		return builder.toString();
	}
	
	public static String toHtmlSnippet( Reason reason) {
		StringBuilder embedder = new StringBuilder();
		embedder.append( HTML_PREFIX);
		embedder.append( ReasonFormatter.toHtmlFormattedText( reason, 0));
		embedder.append( HTML_SUFFIX);		     	
		return embedder.toString();
	}
		
}
