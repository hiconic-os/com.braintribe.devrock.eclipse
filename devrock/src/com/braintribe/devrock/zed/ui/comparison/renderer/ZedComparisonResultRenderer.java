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
package com.braintribe.devrock.zed.ui.comparison.renderer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.braintribe.common.lcd.Pair;
import com.braintribe.devrock.api.storagelocker.StorageLockerSlots;
import com.braintribe.devrock.plugin.DevrockPlugin;
import com.braintribe.devrock.zarathud.model.reasons.comparison.renderer.RendererError;
import com.braintribe.devrock.zed.api.comparison.ComparisonIssueClassification;
import com.braintribe.devrock.zed.ui.comparison.ZedComparisonViewerContext;
import com.braintribe.devrock.zed.ui.comparison.renderer.data.Difference;
import com.braintribe.devrock.zed.ui.comparison.renderer.data.Differences;
import com.braintribe.devrock.zed.ui.comparison.renderer.data.Report;
import com.braintribe.devrock.zed.ui.transposer.CommonContentTransposer;
import com.braintribe.devrock.zed.ui.transposer.TopologyExpert;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.Reasons;
import com.braintribe.gm.model.reason.essential.IoError;
import com.braintribe.gm.model.reason.essential.NotFound;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.web.velocity.renderer.VelocityTemplateRenderer;
import com.braintribe.web.velocity.renderer.VelocityTemplateRendererException;
import com.braintribe.zarathud.model.data.AccessModifier;
import com.braintribe.zarathud.model.data.AnnotationEntity;
import com.braintribe.zarathud.model.data.AnnotationValueContainer;
import com.braintribe.zarathud.model.data.AnnotationValueContainerType;
import com.braintribe.zarathud.model.data.ClassEntity;
import com.braintribe.zarathud.model.data.EnumEntity;
import com.braintribe.zarathud.model.data.FieldEntity;
import com.braintribe.zarathud.model.data.InterfaceEntity;
import com.braintribe.zarathud.model.data.MethodEntity;
import com.braintribe.zarathud.model.data.ScopeModifier;
import com.braintribe.zarathud.model.data.TypeReferenceEntity;
import com.braintribe.zarathud.model.data.ZedEntity;
import com.braintribe.zarathud.model.forensics.FingerPrint;
import com.braintribe.zarathud.model.forensics.ForensicsRating;
import com.braintribe.zarathud.model.forensics.findings.ComparisonIssueType;

/**
 * renders the resulting comparison fingerprints to a velocity template to obtain a textual output
 * @author pit
 */
public class ZedComparisonResultRenderer{
	public final String template = "templates/report.vm";
	public final String templateKey = "default";
	private Map<String, Supplier<String>> providerMap;		
	
	private VelocityTemplateRenderer renderer;
	
	public ZedComparisonResultRenderer() {
		renderer = new VelocityTemplateRenderer();
		providerMap = new HashMap<String, Supplier<String>>();
		renderer.setKeyToProviderMap( providerMap);
		InputStream stream = this.getClass().getResourceAsStream( template);
		providerMap.put( templateKey, new StreambasedTemplateProvider( stream));		
	}
	
	/**
	 * @param context - the {@link ZedComparisonViewerContext}
	 * @return - a {@link Maybe} containing the rendered template 
	 */
	public Maybe<String> renderReport(ZedComparisonViewerContext context) {
		// 
		renderer.setContextValue("context", "baseArtifact", context.getBaseArtifact().asString());
		renderer.setContextValue("context", "otherArtifact", context.getOtherArtifact().asString());
		renderer.setContextValue("context", "comparisonDate", new Date().toString());
		renderer.setContextValue("context", "renderer", new ExtractionRenderer());
		
		Report report = buildReport(context);
		

		
		//		
		renderer.setContextValue("context", "report", report);
		
		// 
		String templateToUse = templateKey;
		if (DevrockPlugin.instance().storageLocker().getValue(StorageLockerSlots.SLOT_ZED_USE_INTERAL_CMP_TEMPLATE, true) == false) {
			String templateFileName = DevrockPlugin.instance().storageLocker().getValue( StorageLockerSlots.SLOT_ZED_EXTERNAL_CMP_TEMPLATE, null);
			if (templateFileName == null) {
				return Maybe.empty( Reasons.build(NotFound.T).assign( NotFound::setText, "no template file specified").toReason());
			}
			File templateFile = new java.io.File( templateFileName);
			if (!templateFile.exists()) {
				return Maybe.empty( Reasons.build(NotFound.T).assign( NotFound::setText, "template file not found:" + templateFileName).toReason());
			}
			templateToUse = templateFile.getName();
			
			try (InputStream in = new FileInputStream( templateFile)) {
				providerMap.put( templateToUse, new StreambasedTemplateProvider( in));
			} catch (Exception e) {
				return Maybe.empty( Reasons.build(IoError.T).assign( IoError::setText, "cannot read template file :" + templateFileName).toReason());
			}			
		}			
		
		try {
			return Maybe.complete(renderer.renderTemplate( templateToUse, "context"));
		} catch (VelocityTemplateRendererException e) {
			return Maybe.empty( Reasons.build(RendererError.T).assign( RendererError::setRenderFailure, e.getMessage()).toReason());
		}						
	}
	
	 



	
	/**
	 * actually build the instances for the template 
	 * @param context - the {@link ZedComparisonViewerContext}
	 * @return - the {@link Report}
	 */
	private Report buildReport(ZedComparisonViewerContext context) {
		
		Report report = new Report();
		report.setBaseArtifact( context.getBaseArtifact().asString());
		report.setComparedArtifact( context.getOtherArtifact().asString());
		report.setComparisonDate( new Date());
		report.setSemanticVersioningLevel(context.getSemanticComparisonLevel().name());
		
		List<FingerPrint> fingerPrints = context.getFingerPrints();
		Map<ZedEntity, List<FingerPrint>> entityToFingerPrint = new HashMap<>();
		
		for (FingerPrint fingerPrint : fingerPrints) {
			ZedEntity owner = TopologyExpert.findBaseTopOwner(fingerPrint);
			List<FingerPrint> assignedFingerPrints = entityToFingerPrint.computeIfAbsent(owner, z -> new ArrayList<>());
			assignedFingerPrints.add(fingerPrint);
		}
		
		
		for (Map.Entry<ZedEntity, List<FingerPrint>> entry : entityToFingerPrint.entrySet()) {

			ZedEntity zedEntity = entry.getKey();
			Differences differences = new Differences();
			differences.setRelevantOwner( zedEntity.getName());
			report.getOwnedDifferences().add(differences);
			
			List<FingerPrint> associatedFingerPrint = entry.getValue();
			for (FingerPrint fp : associatedFingerPrint) {						
				Difference difference = translateToDifference(context, zedEntity, fp);
				report.getRawDifferences().add(difference);				
				differences.getDifferences().add(difference);
			}			
			differences.setRating( ForensicsRating.getWorstRating( associatedFingerPrint.stream().map( fp -> ComparisonIssueClassification.rateComparisonIssue( fp.getComparisonIssueType(), context.getSemanticComparisonLevel())).collect(Collectors.toList())).name());
		}			
		
		report.getOwnedDifferences().sort( new Comparator<Differences>() {
			@Override
			public int compare(Differences o1, Differences o2) {				
				return o1.getRelevantOwner().compareTo( o2.getRelevantOwner());
			}			
		});
		
		return report;
		
	}

	/**
	 * @param context - {@link ZedComparisonViewerContext} 
	 * @param ownerEntity - the'owning' entity, aka the compilation unit
	 * @param fp - the {@link FingerPrint} to render
	 * @return
	 */
	private Difference translateToDifference(ZedComparisonViewerContext context, ZedEntity ownerEntity, FingerPrint fp) {
		Difference difference = new Difference();	
		difference.setRelevantOwner( extractRelevantString(ownerEntity).get(0));
		difference.setIssue( fp.getSlots().get("issue"));
		ForensicsRating rating = ComparisonIssueClassification.rateComparisonIssue( fp.getComparisonIssueType(), context.getSemanticComparisonLevel());
		difference.setRating( CommonContentTransposer.transpose(rating).name());		
		
		GenericEntity entitySource = fp.getEntitySource();
		if (ownerEntity != entitySource) {
			difference.setBase( extractRelevantString(entitySource).get(0));
		}				
		GenericEntity entityComparisonTarget = fp.getEntityComparisonTarget();
		if (ownerEntity != entityComparisonTarget) {
			difference.setOther( extractRelevantString(entityComparisonTarget).get(0));
		}
		
		ComparisonIssueType cit = fp.getComparisonIssueType();
		// complex 				
		List<GenericEntity> comparisonIssueData = fp.getComparisonIssueData();
		List<String> issueData = fp.getIssueData();
		
		// 
		if (cit == ComparisonIssueType.annotationValueMismatch) {
			// determine difference in annotation ? 
			enrichByAnnotationValueMismatch( difference, entitySource, entityComparisonTarget);		
		}
		
		if (comparisonIssueData.size() == 0 && issueData.size() == 0) {
			return difference;
		}
				
		boolean relatedToSurplus = ComparisonIssueClassification.isCollectionComparisonIssue( cit.name()) && ComparisonIssueClassification.isSurplus( cit.name());
		boolean relatedToMissing = ComparisonIssueClassification.isCollectionComparisonIssue( cit.name()) && !ComparisonIssueClassification.isSurplus( cit.name());
		
		
		
		// might contain raw data if comparisonIssueData is set 
		if (comparisonIssueData.size() == 0) {
			for (String s : issueData) {
				if (relatedToMissing) {
					difference.getMissingInBase().add(s);
				}
				else if (relatedToSurplus) {
					difference.getSurplusInOther().add(s);
				}
				else {
					System.out.println("unrelated : " + s);
				}
			}
		}
										
		for (GenericEntity ge : comparisonIssueData) {
			GenericEntity se;
			if (ge instanceof TypeReferenceEntity) {
				TypeReferenceEntity tre = (TypeReferenceEntity) ge;
				se = tre.getReferencedType();
			}
			else {
				se = ge;
			}
			String comparisonEntity = extractRelevantString(se).get(0);
			if (relatedToMissing) {
				difference.getMissingInBase().add(comparisonEntity);
			} else if (relatedToSurplus) {
				difference.getSurplusInOther().add(comparisonEntity);
			}
			else {
				System.out.println("Unrelated : " + comparisonEntity.getClass().getName());
			}
		}
		
		return difference;
	}
	
	/**
	 * somewhat out of order - a annotation value mismatch (initializer value different for instance)
	 * @param difference - the {@link Difference} as prepared 
	 * @param entitySource - the {@link GenericEntity} in base (actually, it's a {@link AnnotationEntity})
	 * @param entityComparisonTarget - the {@link GenericEntity} in other (actually, it's a {@link AnnotationEntity})
	 */
	private void enrichByAnnotationValueMismatch(Difference difference, GenericEntity entitySource, GenericEntity entityComparisonTarget) {
		AnnotationEntity aeb = (AnnotationEntity) entitySource;
		AnnotationEntity aeo = (AnnotationEntity) entityComparisonTarget;
		Map<String, AnnotationValueContainer> aebMembers = aeb.getMembers();	
		Map<String, AnnotationValueContainer> aeoMembers = aeo.getMembers();

		Pair<List<String>, List<String>> cp = compareAnnotationContainers(aebMembers, aeoMembers);
		List<String> missing = cp.first;
		List<String> surplus = cp.second;
		
		difference.getMissingInBase().addAll(missing);
		difference.getSurplusInOther().addAll(surplus);				
	}
	
	/**
	 * finding the difference 
	 * @param baseMembers
	 * @param otherMembers
	 * @return - a {@link Pair} of the two differing values as String
	 */ 
	private Pair<List<String>, List<String>> compareAnnotationContainers(Map<String, AnnotationValueContainer> baseMembers, Map<String, AnnotationValueContainer> otherMembers) {
		List<String> missing = new ArrayList<>();
		List<String> surplus = new ArrayList<>();
		
		for (Map.Entry<String, AnnotationValueContainer> baseEntry : baseMembers.entrySet()) {
			AnnotationValueContainer baseAvc = baseEntry.getValue();
			
			AnnotationValueContainer otherAvc = otherMembers.get( baseEntry.getKey());
			if (otherAvc == null) {
				missing.add( baseEntry.getKey());
				continue;
			}
			//
			Pair<String,String> dif = compare(baseAvc, otherAvc);
			if (dif != null) {
				missing.add( dif.first);
				surplus.add(dif.second);
			}						
		}
				
		return Pair.of( missing, surplus);
	}	
	
	private Pair<String,String> compare(AnnotationValueContainer baseAvc, AnnotationValueContainer otherAvc) {
		AnnotationValueContainerType bContainerType = baseAvc.getContainerType();
		AnnotationValueContainerType oContainerType = otherAvc.getContainerType();
		
		// compare type of annotation value 
		if (bContainerType != oContainerType) {
			return null;
		}
		
		// compare values
		switch (bContainerType) {
			case annotation:
				//new StatefulAnnotationComparator(context).compare( baseAvc.getOwner(), otherAvc.getOwner());
				System.out.println("Unsupported yet : collection of containers" );
				break;
			case collection:			
				System.out.println("Unsupported yet : collection of values" );
				break;				
			case s_boolean:
				return Pair.of( Boolean.toString(baseAvc.getSimpleBooleanValue()), Boolean.toString(otherAvc.getSimpleBooleanValue())); 											
			case s_date:
				return Pair.of(baseAvc.getSimpleDateValue().toString(), otherAvc.getSimpleDateValue().toString());																
			case s_double:
				return Pair.of( Double.toString( baseAvc.getSimpleDoubleValue()), Double.toString(otherAvc.getSimpleDoubleValue()));															
			case s_float:
				return Pair.of( Float.toString(baseAvc.getSimpleFloatValue()), Float.toString(otherAvc.getSimpleFloatValue()));								
			case s_int:
				return Pair.of( Integer.toString(baseAvc.getSimpleIntegerValue()), Integer.toString( otherAvc.getSimpleIntegerValue()));								
			case s_long:
				return Pair.of( Long.toString( baseAvc.getSimpleLongValue()), Long.toString(otherAvc.getSimpleLongValue()));								
			case s_string:
				return Pair.of( baseAvc.getSimpleStringValue(), otherAvc.getSimpleStringValue());								
			default:
				break;		
		}
		return null;
		
		
	}
	

	public List<String> extractRelevantString (GenericEntity ge) {
		
		if (ge == null) {
			return Collections.singletonList("<null>");
		}
		
		if (ge instanceof AnnotationEntity) {			
			return render( (AnnotationEntity) ge);
		}
		else if (ge instanceof ClassEntity) {			
			return render( (ClassEntity) ge);			
		}
		else if (ge instanceof InterfaceEntity) {			
			return render( (InterfaceEntity) ge);			
		}
		else if (ge instanceof EnumEntity) {			
			return render( (EnumEntity) ge);			
		}
		else if (ge instanceof MethodEntity) {		
			return render( (MethodEntity) ge);
		}
		else if (ge instanceof FieldEntity) {		
			return render( (FieldEntity) ge);
		}		
		
		return Collections.singletonList( ge.getClass().getName());
	}

	private List<String> render(AnnotationEntity annotationEntity) {
		String name = annotationEntity.getDeclaringInterface().getReferencedType().getName();				
		return Collections.singletonList(name);
	}

	private List<String> render(ClassEntity classEntity) {
		StringBuilder sb = new StringBuilder();
		AccessModifier accessModifier = classEntity.getAccessModifier();
		if (accessModifier != null) {
		sb.append(accessModifier.name().toLowerCase());
		sb.append(" ");
		}
		
		boolean abstractNature = classEntity.getAbstractNature();
		if (abstractNature) {
			sb.append( "abstract ");
		}
		
		boolean staticNature = classEntity.getStaticNature();		
		if (staticNature) {
			sb.append( "static");
			sb.append( " ");
		}
		
		sb.append( classEntity.getName());
		
		return Collections.singletonList(sb.toString());
	}
	
	private List<String> render( InterfaceEntity interfaceEntity) {
		return Collections.singletonList( interfaceEntity.getName());
	}
	
	private List<String> render( EnumEntity enumEntity) {
		StringBuilder sb = new StringBuilder();
		AccessModifier accessModifier = enumEntity.getAccessModifier();
		if (accessModifier != null) {
		sb.append(accessModifier.name().toLowerCase());
		sb.append(" ");
		}
		
		boolean abstractNature = enumEntity.getAbstractNature();
		if (abstractNature) {
			sb.append( "abstract ");
		}
		
		boolean staticNature = enumEntity.getStaticNature();		
		if (staticNature) {
			sb.append( "static");
			sb.append( " ");
		}
		
		sb.append( enumEntity.getName());
		
		return Collections.singletonList(sb.toString());
	}
	

	private List<String> render( MethodEntity methodEntity) {
		StringBuilder sb = new StringBuilder();
		AccessModifier accessModifier = methodEntity.getAccessModifier();
		sb.append(accessModifier.name().toLowerCase());
		sb.append(" ");
		
		boolean abstractNature = methodEntity.getAbstractNature();
		if (abstractNature) {
			sb.append( "abstract ");
		}
		
		boolean staticNature = methodEntity.getStaticNature();		
		if (staticNature) {
			sb.append( "static");
			sb.append( " ");
		}
		
		
		boolean synchronizedNature = methodEntity.getSynchronizedNature();
		if (synchronizedNature) {
			sb.append( "synchronized");
			sb.append( " ");
		}
		
		TypeReferenceEntity returnType = methodEntity.getReturnType();
		sb.append( returnType.getReferencedType().getName());
		sb.append( " ");
		
		sb.append( methodEntity.getName());
		
		sb.append("(");
		
		List<TypeReferenceEntity> argumentTypes = methodEntity.getArgumentTypes();
		boolean first = true;
		for (TypeReferenceEntity tre : argumentTypes) {
			if (!first) {
				sb.append( ",");
				first = false;
			}
			sb.append(tre.getReferencedType().getName());
		}		
		sb.append( ")");
		return Collections.singletonList(sb.toString());
	}
	
	private List<String> render(FieldEntity fieldEntity) {
		StringBuilder sb = new StringBuilder();
		
		AccessModifier accessModifier = fieldEntity.getAccessModifier();
		ScopeModifier scopeModifier = fieldEntity.getScopeModifier();
		boolean staticNature = fieldEntity.getStaticNature();
		TypeReferenceEntity type = fieldEntity.getType();
		
		sb.append(accessModifier.name().toLowerCase());
		sb.append( " ");
		
		if (staticNature) {
			sb.append( "static");
			sb.append( " ");
		}
		
		if (scopeModifier != ScopeModifier.DEFAULT) {
			sb.append( scopeModifier.name().toLowerCase());		
			sb.append( " ");
		}
		sb.append( type.getReferencedType().getName());
		sb.append( " ");
		
		sb.append( fieldEntity.getName());
		
		return Collections.singletonList(sb.toString());
	}
}
