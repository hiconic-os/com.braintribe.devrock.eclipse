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
 * a simple structure to contain a difference as found by zed
 * @author pit
 */
public class Difference {
	private String issue;
	private String rating;
	private String relevantOwner;
	private String base;
	private String other;
	private List<String> missingInBase = new ArrayList<>();
	private List<String> surplusInOther = new ArrayList<>();
	private String labelForBaseData ="expected";
	private String labelForOtherData = "encountered";
	
	/**
	 * @return - the ComparisonIssue as reported by zed 
	 */
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	
	/**
	 * @return - the rating is determined by zed (error, warning et al)
	 */
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	
	/**
	 * @return - the owning compilation unit 
	 */
	public String getRelevantOwner() {
		return relevantOwner;
	}
	public void setRelevantOwner(String relevantOwner) {
		this.relevantOwner = relevantOwner;
	}
	
	/**
	 * @return - name/origin/location of the issue inside the base compilation unit (method, field) 
	 */
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	
	/**
	 * @return - name/origin/location of the issue inside hte other compilation unit (method, field)
	 */
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	
	/**
	 * @return - the data for the issue (missing methods/fields, also differing values)
	 */
	public List<String> getMissingInBase() {
		return missingInBase;
	}
	public void setMissingInBase(List<String> missingInBase) {
		this.missingInBase = missingInBase;
	}
	
	/**
	 * @return - the data for the issue (surplus methods/fields, also differing values)
	 */
	public List<String> getSurplusInOther() {
		return surplusInOther;
	}
	public void setSurplusInOther(List<String> surplusInOther) {
		this.surplusInOther = surplusInOther;
	}
	
	/**
	 * @return - illustrates the data collected (expected, missing etc)  
	 */
	public String getLabelForBaseData() {
		return labelForBaseData;
	}
	public void setLabelForBaseData(String labelForBaseData) {
		this.labelForBaseData = labelForBaseData;
	}
	
	/**
	 * @return - illustrates the data collected (encountered, surplus etc)  
	 */
	public String getLabelForOtherData() {
		return labelForOtherData;
	}
	public void setLabelForOtherData(String labelForOtherData) {
		this.labelForOtherData = labelForOtherData;
	}	

}
