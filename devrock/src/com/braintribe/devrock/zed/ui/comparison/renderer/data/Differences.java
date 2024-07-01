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
package com.braintribe.devrock.zed.ui.comparison.renderer.data;

import java.util.ArrayList;
import java.util.List;

/**
 * a simple structure that contains all differences found within a compilation unit (class/interface/enum/annotation)
 * @author pit
 */
public class Differences {
	
	String relevantOwner;
	String rating;
	List<Difference> differences = new ArrayList<>();
	
	/**
	 * @return - the name of the compilation unit 
	 */
	public String getRelevantOwner() {
		return relevantOwner;
	}
	public void setRelevantOwner(String relevantOwner) {
		this.relevantOwner = relevantOwner;
	}
	
	/**
	 * @return - the *worst* rating found amongst all differences 
	 */
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	
	/**
	 * @return - a list of {@link Difference}s attached to the compilation unit
	 */
	public List<Difference> getDifferences() {
		return differences;
	}
	public void setDifferences(List<Difference> differences) {
		this.differences = differences;
	}
	
	
	
	
}
