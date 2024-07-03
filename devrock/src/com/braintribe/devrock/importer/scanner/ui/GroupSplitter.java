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
package com.braintribe.devrock.importer.scanner.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * a helper class to support navigation in the group logic 
 * @author pit
 *
 */
public class GroupSplitter {
	private List<String> tokens;
	
	/**
	 * @param group - the full groupId
	 */
	public GroupSplitter(String group) {
		String [] values = group.split("\\.");
		tokens = new ArrayList<String>( values.length);
		for (String value : values) {
			tokens.add( value);
		}
	}
	
	/**
	 * @return - the {@link List} of the elements within the group
	 */
	public List<String> getGroupTokens() {
		return tokens;
	}
	
	/**
	 * @param index - the index up to which the elements should be returned
	 * @return - combine the elements up to the index in 
	 */
	public String getTokensUpTo( int index) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < index && i < tokens.size(); i++) {
			if (builder.length() > 0)
				builder.append(".");
			builder.append( tokens.get(i));
		}
		return builder.toString();
	}
	
	/**
	 * @param index - the index from which elements should be returned
	 * @return - the group elements after the index
	 */
	public List<String> getTokensAfter( int index) {
		return tokens.subList(index, tokens.size());
	}
	
	/**
	 * @return - number of elements in the full group
	 */
	public int getLength() {
		return tokens.size();
	}

}
