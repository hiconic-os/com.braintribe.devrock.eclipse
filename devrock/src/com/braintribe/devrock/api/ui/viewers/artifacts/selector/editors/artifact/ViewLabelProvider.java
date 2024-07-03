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
package com.braintribe.devrock.api.ui.viewers.artifacts.selector.editors.artifact;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.devrock.api.ui.fonts.StyledTextHandler;
import com.braintribe.devrock.api.ui.fonts.StyledTextSequenceData;
import com.braintribe.devrock.api.ui.stylers.ParametricStyler;
import com.braintribe.model.artifact.compiled.CompiledArtifactIdentification;

public class ViewLabelProvider extends CellLabelProvider implements IStyledLabelProvider  {

	private ParametricStyler idStyler;
	private ParametricStyler groupStyler;
	private ParametricStyler versionStyler;

	@Configurable  @Required
	public void setInitialFont(Font initialFont) {		
		idStyler = new ParametricStyler(initialFont, SWT.BOLD);
		groupStyler = new ParametricStyler(initialFont, null);
		versionStyler = new ParametricStyler(initialFont, null);
	}
	
	@Override
	public Image getImage(Object arg0) {	
		return null;
	}

	@Override
	public String getToolTipText(Object obj) {
		CompiledArtifactIdentification cai = (CompiledArtifactIdentification) obj;
		return cai.asString();
	}

	@Override
	public StyledString getStyledText(Object obj) {
		
		CompiledArtifactIdentification cai = (CompiledArtifactIdentification) obj;
		
		// solution identification	
		List<StyledTextSequenceData> sequences = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		
		String sgId = cai.getGroupId();

		int cS = sb.length();
		sb.append( sgId);
		sb.append( " ");		
		int cE = sb.length();
		sequences.add( new StyledTextSequenceData(cS, cE, groupStyler));		

		String saId = cai.getArtifactId();		
		cS = sb.length();
		sb.append( saId);
		cE = sb.length();
		sequences.add( new StyledTextSequenceData(cS, cE, idStyler));
		
		cS = sb.length();
		sb.append( " ");
		sb.append( cai.getVersion().asString());
		cE = sb.length();
		sequences.add( new StyledTextSequenceData(cS, cE, versionStyler));					
	
		
		String txt = sb.toString();					
		StyledString styledString = new StyledString(txt);
	
		StyledTextHandler.applyRanges(styledString, sequences);
	
		return styledString;
	}

	@Override
	public void update(ViewerCell arg0) {}

	@Override
	public void dispose() {
		
		groupStyler.dispose();
		idStyler.dispose();
		versionStyler.dispose();
		
		super.dispose();
	}
	
	
	

}
