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
package com.braintribe.devrock.greyface.process.notification;

import java.util.List;
import java.util.Set;

import com.braintribe.model.artifact.Part;
import com.braintribe.model.artifact.Solution;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.malaclypse.cfg.preferences.gf.RepositorySetting;


public interface UploadContext extends GenericEntity {
	
	
	final EntityType<UploadContext> T = EntityTypes.T(UploadContext.class);

	List<Solution> getSolutions();
	void setSolutions( List<Solution> solutions);
	
	Set<Solution> getRootSolutions();
	void setRootSolutions( Set<Solution> solutions);
	
	List<Part> getParts();
	void setParts( List<Part> parts);
	
	RepositorySetting getTarget();
	void setTarget( RepositorySetting target);
	
	Set<RepositorySetting> getSources();
	void setSources( Set<RepositorySetting> sources);
	
	boolean getPrunePom();
	void setPrunePom( boolean prune);

}
