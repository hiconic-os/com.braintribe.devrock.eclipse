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
package com.braintribe.devrock.artifactcontainer.plugin.preferences.project;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.braintribe.model.malaclypse.cfg.preferences.svn.SourceRepositoryPairing;

public class RepositoryKindColumnLabelProvider extends ColumnLabelProvider {
	private Image svnImage;
	private Image gitImage;
	
	public RepositoryKindColumnLabelProvider() {
		ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile( RepositoryKindColumnLabelProvider.class, "repo_svn.gif");
		svnImage = imageDescriptor.createImage();
		
		imageDescriptor = ImageDescriptor.createFromFile( RepositoryKindColumnLabelProvider.class, "repo_git.gif");
		gitImage = imageDescriptor.createImage();
	}

	@Override
	public Image getImage(Object element) {
		SourceRepositoryPairing pairing = (SourceRepositoryPairing) element;
		switch (pairing.getRemoteRepresentationKind()) {
		case git:
			return gitImage;		
		case svn:			
		default:
			return svnImage;		
		}			
	}

	@Override
	public String getText(Object element) {
		return null;
	}

	@Override
	public String getToolTipText(Object element) {	
		SourceRepositoryPairing pairing = (SourceRepositoryPairing) element;
		switch (pairing.getRemoteRepresentationKind()) {
		case git:
			return "GIT repository";		
		case svn:			
		default:
			return "SVN repository";		
		}		
	}

	@Override
	public void dispose() {
		svnImage.dispose();
		gitImage.dispose();
		super.dispose();
	}

	
}
