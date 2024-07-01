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
import java.util.Date;
import java.util.List;

/**
 * the basic report as produced by Zed 
 * 
 * @author pit
 */
public class Report  {		
	
	private String  baseArtifact;
	private String comparedArtifact;
	private Date comparisonDate;
	private List<Difference> rawDifferences = new ArrayList<>();
	private List<Differences> ownedDifferences = new ArrayList<>();
	private String semanticVersioningLevel; 
	

	/**
	 * @return - the qualified name of the artifact that is the base of the comparison
	 */
	public String getBaseArtifact() {
		return baseArtifact;
	}
	public void setBaseArtifact(String baseArtifact) {
		this.baseArtifact = baseArtifact;
	}

	/**
	 * @return - the qualified name of the artifact that the base is compared against
	 */
	public String getComparedArtifact() {
		return comparedArtifact;
	}
	public void setComparedArtifact(String comparedArtifact) {
		this.comparedArtifact = comparedArtifact;
	}

	/**
	 * @return - the {@link Date} of the comparison
	 */
	public Date getComparisonDate() {
		return comparisonDate;
	}
	public void setComparisonDate(Date comparisonDate) {
		this.comparisonDate = comparisonDate;
	}
	
	/**
	 * @return -  the number of found differences 
	 */
	public int getNumberOfDifferences() {
		return rawDifferences.size();
	}
	/**
	 * @return - the number of offending compilation units
	 */
	public int getNumberOfOwners() {
		return ownedDifferences.size();
	}
	
	/**
	 * @return - a list of all {@link Difference}s
	 */
	public List<Difference> getRawDifferences() {
		return rawDifferences;
	}
	public void setRawDifferences(List<Difference> rawDifferences) {
		this.rawDifferences = rawDifferences;
	}
	
	/**
	 * @return - a list of the compilation units as {@link Differences}, with their {@link Difference} attached
	 */
	public List<Differences> getOwnedDifferences() {
		return ownedDifferences;
	}
	public void setOwnedDifferences(List<Differences> ownedDifferences) {
		this.ownedDifferences = ownedDifferences;
	}
	
	/**
	 * @return - the current rating rules depending on the semanatic versioning (major, minor, revision)
	 */
	public String getSemanticVersioningLevel() {
		return semanticVersioningLevel;
	}
	public void setSemanticVersioningLevel(String semanticVersioningLevel) {
		this.semanticVersioningLevel = semanticVersioningLevel;
	}
    
	
}
