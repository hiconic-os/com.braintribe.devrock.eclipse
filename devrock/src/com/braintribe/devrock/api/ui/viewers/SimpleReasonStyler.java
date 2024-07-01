// ============================================================================
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
package com.braintribe.devrock.api.ui.viewers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;

import com.braintribe.devrock.api.ui.fonts.StyledTextHandler;
import com.braintribe.devrock.api.ui.fonts.StyledTextSequenceData;
import com.braintribe.gm.model.reason.Reason;

/**
 * old style text styler
 * @author pit
 *
 */
@Deprecated
public class SimpleReasonStyler {

	public static StyledString createStyledStringFromReason(Reason reason, Styler typeStyler, Styler messageStyler) {
		String text = reason.getText();
		List<StyledTextSequenceData> sequences = new ArrayList<>();
		boolean inside = false;
		int s=0,e = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c == '\'') {
				if (inside) {
					inside = false;
					e = i;
					// 
					sequences.add( new StyledTextSequenceData(s, e, typeStyler));
					System.out.println("bold:" + text.substring(s, e));
					s = i+1;
				}
				else {
					inside = true;
					e = i;
					sequences.add( new StyledTextSequenceData(s, e, messageStyler));
					System.out.println("normal:" + text.substring(s, e));
					s = i+1;
					
				}
			}				
		}			
		if (s < text.length()) {
			if (inside) {
				sequences.add( new StyledTextSequenceData(s, text.length()-1, typeStyler));
			}
			else {
				sequences.add( new StyledTextSequenceData(s, text.length()-1, messageStyler));
			}
		}
		StyledString styledString = new StyledString( text);
		if (sequences.size() > 0) {
			StyledTextHandler.applyRanges(styledString, sequences);
		}
		return styledString;
	}
}
