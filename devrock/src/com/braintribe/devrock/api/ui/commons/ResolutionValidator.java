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
import com.braintribe.model.artifact.analysis.AnalysisArtifact;
import com.braintribe.model.artifact.analysis.AnalysisArtifactResolution;
import com.braintribe.model.artifact.analysis.AnalysisDependency;
import com.braintribe.model.artifact.analysis.AnalysisTerminal;

/**
 * temporary helper - required as long as failure reasons on terminals are not reflected in the resolution
 * @author pit
 *
 */
public class ResolutionValidator {

	/**
	 * @param resolution - the {@link AnalysisArtifactResolution}
	 * @return - true if the resolution or the terminals have failed
	 */
	public static boolean isResolutionInvalid( AnalysisArtifactResolution resolution) {
		if (resolution.hasFailed())
			return true;
		
		List<AnalysisTerminal> terminals = resolution.getTerminals();
		for (AnalysisTerminal terminal : terminals) {
			if (terminal instanceof AnalysisArtifact) {
				AnalysisArtifact aa = (AnalysisArtifact) terminal;
				if (aa.hasFailed()) {
					return true;
				}
			}
			else if (terminal instanceof AnalysisDependency) {
				continue;
			}
		}
		return false;
	}
	
	/**
	 * @param resolution - the {@link AnalysisArtifactResolution}
	 * @return - a collator {@link Reason} with all reasons attached
	 */
	public static Reason getReasonForFailure( AnalysisArtifactResolution resolution) {
		
		// no longer required as resolution is now properly flagged if a terminal has failed
		/*
		List<Reason> terminalFailureReasons = new ArrayList<>( resolution.getTerminals().size());
		
		for (AnalysisTerminal terminal : resolution.getTerminals()) {
			if (terminal instanceof AnalysisArtifact) {
				AnalysisArtifact analysisArtifact = (AnalysisArtifact) terminal;
				if (analysisArtifact.hasFailed()) {
					terminalFailureReasons.add( analysisArtifact.getFailure());
				}
			}
		}
		Reason resolutionFailureReason = resolution.getFailure();
		
		if (terminalFailureReasons.size() == 0 && resolutionFailureReason == null) 
			return null;
		
		if (terminalFailureReasons.size() == 0) {
			return resolutionFailureReason;
		}
		
		if (resolutionFailureReason != null) {
			resolutionFailureReason.getReasons().addAll(terminalFailureReasons);
			return resolutionFailureReason;
		} 
		resolutionFailureReason = Reasons.build( IncompleteResolution.T).text("invalid resolution due to invalid terminals").causes( terminalFailureReasons.toArray( new Reason[0])).toReason();
		return resolutionFailureReason;
		*/
		
		return resolution.getFailure();
		
	}
}
