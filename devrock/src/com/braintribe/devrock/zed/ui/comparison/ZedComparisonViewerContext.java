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
package com.braintribe.devrock.zed.ui.comparison;

import java.util.List;

import com.braintribe.devrock.zed.api.comparison.SemanticVersioningLevel;
import com.braintribe.zarathud.model.data.Artifact;
import com.braintribe.zarathud.model.forensics.FingerPrint;

public class ZedComparisonViewerContext {

	private Artifact baseArtifact;
	private Artifact otherArtifact;
	private List<FingerPrint> fingerPrints;
	private SemanticVersioningLevel semanticComparisonLevel;
	private String usedTemplateExtension;
	
	public Artifact getBaseArtifact() {
		return baseArtifact;
	}
	public void setBaseArtifact(Artifact baseArtifact) {
		this.baseArtifact = baseArtifact;
	}
	public Artifact getOtherArtifact() {
		return otherArtifact;
	}
	public void setOtherArtifact(Artifact otherArtifact) {
		this.otherArtifact = otherArtifact;
	}
	public List<FingerPrint> getFingerPrints() {
		return fingerPrints;
	}
	public void setFingerPrints(List<FingerPrint> fingerPrints) {
		this.fingerPrints = fingerPrints;
	}
	public SemanticVersioningLevel getSemanticComparisonLevel() {
		return semanticComparisonLevel;
	}
	public void setSemanticComparisonLevel(SemanticVersioningLevel semanticComparisonLevel) {
		this.semanticComparisonLevel = semanticComparisonLevel;
	}
	public String getUsedTemplateExtension() {
		return usedTemplateExtension;
	}
	public void setUsedTemplateExtension(String usedTemplateExtension) {
		this.usedTemplateExtension = usedTemplateExtension;
	}
	
	
	
	
}
