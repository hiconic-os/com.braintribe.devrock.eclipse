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
package com.braintribe.devrock.zed.ui.transposer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.braintribe.devrock.zarathud.model.classpath.ClasspathDuplicateNode;
import com.braintribe.devrock.zarathud.model.common.ArtifactNode;
import com.braintribe.devrock.zarathud.model.common.FingerPrintRating;
import com.braintribe.devrock.zed.forensics.fingerprint.HasFingerPrintTokens;
import com.braintribe.devrock.zed.ui.ZedViewingContext;
import com.braintribe.model.artifact.essential.VersionedArtifactIdentification;
import com.braintribe.zarathud.model.data.Artifact;
import com.braintribe.zarathud.model.data.ZedEntity;
import com.braintribe.zarathud.model.forensics.ClasspathForensicsResult;
import com.braintribe.zarathud.model.forensics.FingerPrint;
import com.braintribe.zarathud.model.forensics.ForensicsRating;
import com.braintribe.zarathud.model.forensics.data.ClasspathDuplicate;

/**
 * the transposer to get a displayable nodes from the {@link ClasspathForensicsResult}
 * 
 * @author pit
 *
 */
public class ClasspathAnalysisContentTransposer implements HasFingerPrintTokens {

	/**
	 * transpose the {@link ClasspathForensicsResult} to a {@link List} of {@link ClasspathDuplicateNode}
	 * @param context - the {@link ZedViewingContext}
	 * @param classpathForensicsResult - the {@link ClasspathForensicsResult}
	 * @return - a {@link List} of {@link ClasspathDuplicateNode}, empty if none
	 */
	public List<ClasspathDuplicateNode> transpose(ZedViewingContext context, ClasspathForensicsResult classpathForensicsResult) {
		
		List<FingerPrint> fingerPrintsOfIssues = classpathForensicsResult.getFingerPrintsOfIssues();
		ForensicsRating worstRatingOfFingerPrints = context.getRatingRegistry().getWorstRatingOfFingerPrints(fingerPrintsOfIssues);
		
		
					
		List<ClasspathDuplicate> duplicates = classpathForensicsResult.getDuplicates();
				
		List<ClasspathDuplicateNode> nodes = new ArrayList<>( duplicates.size());
		for (ClasspathDuplicate duplicate : duplicates) {
							
			nodes.addAll( transpose( context, duplicate, CommonContentTransposer.transpose(worstRatingOfFingerPrints)));
		}
		nodes.sort( new Comparator<ClasspathDuplicateNode>() {

			@Override
			public int compare(ClasspathDuplicateNode o1, ClasspathDuplicateNode o2) {
				return o1.getDuplicateType().getName().compareTo( o2.getDuplicateType().getName());				
			}
			
		});
		return nodes;
	}

	/**
	 * transpose a single {@link ClasspathDuplicate} from zed into {@link ClasspathDuplicateNode}s, several if required 
	 * @param context - the {@link ZedViewingContext}
	 * @param duplicate - the {@link ClasspathDuplicate}
	 * @param rating - the overall {@link FingerPrintRating} to assign
	 * @return - a {@link List} of {@link ClasspathDuplicateNode} (also contains all references in terminal)
	 */
	private List<ClasspathDuplicateNode> transpose(ZedViewingContext context, ClasspathDuplicate duplicate, FingerPrintRating rating) {
		List<ClasspathDuplicateNode> nodes = new ArrayList<>();
		for (ZedEntity z : duplicate.getReferencersInTerminal()) {
			ClasspathDuplicateNode node = ClasspathDuplicateNode.T.create();
			node.setDuplicateType( duplicate.getType());
			node.setReferencingType( z);			
			node.setRating( rating);
			
			List<Artifact> artifacts = duplicate.getDuplicates();
			for (Artifact artifact : artifacts) { 			
				node.getChildren().add( transpose(context, artifact));
			}		
			nodes.add(node);
		}
		return nodes;			
		
	}

	/**
	 * transpose the owning artifact of the duplicates
	 * @param context - the {@link ZedViewingContext}
	 * @param artifact - the {@link Artifact} as from zed
	 * @return - the transposed {@link ArtifactNode}
	 */
	private ArtifactNode transpose( ZedViewingContext context, Artifact artifact) {
		ArtifactNode node = ArtifactNode.T.create();		
		node.setIdentification( VersionedArtifactIdentification.create( artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion()));
		node.setIsTerminal(false);		
		return node;
				
	}
}
