package com.braintribe.devrock.zed.ui.comparison.renderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.braintribe.devrock.zarathud.model.common.Node;
import com.braintribe.devrock.zarathud.model.extraction.AnnotationNode;
import com.braintribe.devrock.zarathud.model.extraction.ClassNode;
import com.braintribe.devrock.zarathud.model.extraction.EnumNode;
import com.braintribe.devrock.zarathud.model.extraction.ExtractionNode;
import com.braintribe.devrock.zarathud.model.extraction.FieldNode;
import com.braintribe.devrock.zarathud.model.extraction.InterfaceNode;
import com.braintribe.devrock.zarathud.model.extraction.MethodNode;
import com.braintribe.devrock.zarathud.model.extraction.subs.ContainerNode;
import com.braintribe.zarathud.model.data.AccessModifier;
import com.braintribe.zarathud.model.data.AnnotationEntity;
import com.braintribe.zarathud.model.data.ClassEntity;
import com.braintribe.zarathud.model.data.EnumEntity;
import com.braintribe.zarathud.model.data.FieldEntity;
import com.braintribe.zarathud.model.data.InterfaceEntity;
import com.braintribe.zarathud.model.data.MethodEntity;
import com.braintribe.zarathud.model.data.ScopeModifier;
import com.braintribe.zarathud.model.data.TypeReferenceEntity;

public class ExtractionRenderer {

	public String render(List<ExtractionNode> enodes) {
		StringBuilder sb = new StringBuilder();
		for (ExtractionNode enode : enodes) {
			if (sb.length() > 0) {
				sb.append( "\n");
			}
			sb.append( render(enode));
		}
		return sb.toString();
	}
	
	public List<String> render(ExtractionNode enode) {
		
		if (enode instanceof AnnotationNode) {
			AnnotationNode an = (AnnotationNode) enode;
			return render( an.getAnnotationEntity());
		}
		else if (enode instanceof ClassNode) {
			ClassNode cn = (ClassNode) enode;
			return render( cn.getEntity());
			
		}
		else if (enode instanceof InterfaceNode) {
			InterfaceNode cn = (InterfaceNode) enode;
			return render( cn.getInterfaceEntity());			
		}
		else if (enode instanceof EnumNode) {
			EnumNode cn = (EnumNode) enode;
			return render( cn.getEnumEntity());
			
		}
		else if (enode instanceof MethodNode) {
			MethodNode mn = (MethodNode) enode;
			return render(mn.getMethodEntity());
		}
		else if (enode instanceof FieldNode) {
			FieldNode fn = (FieldNode) enode;
			return render( fn.getFieldEntity());
		}		
		else if (enode instanceof ContainerNode) {
			// container..
			ContainerNode cn = (ContainerNode) enode;
			List<String> result = new ArrayList<>(cn.getChildren().size());
			for (Node en : cn.getChildren()) {
				result.addAll( render( (ExtractionNode) en));
			}
			return result;
		}
		return Collections.singletonList( enode.getClass().getName());
	}
	

	private List<String> render(AnnotationEntity annotationEntity) {
		// TODO Auto-generated method stub
		return Collections.emptyList();
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
