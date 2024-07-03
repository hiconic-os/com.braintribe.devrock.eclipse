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
package com.braintribe.devrock.importer.scanner;

import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.braintribe.devrock.eclipse.model.identification.EnhancedCompiledArtifactIdentification;
import com.braintribe.devrock.eclipse.model.scan.SourceRepositoryEntry;
import com.braintribe.devrock.importer.scanner.listener.QuickImportScanListener;


public interface QuickImportControl {

	boolean isScanActive();

	void rescan();
	void rescan(List<SourceRepositoryEntry> entries);

	void stop();
	
	
	void addListener(QuickImportScanListener listener);
	void removeListener( QuickImportScanListener listener);
	
	/**
	 * runs a query using the 'coarse' method (i.e. with some lee way due to automatic wildcards). Depending on how the artifact is declared in the expression:
	 * groupId, artifactId, version present -> full match required
	 * groupId, artifactId present -> match only on groupId and artifactId
	 * artifactId, version present -> match only on artifactId and version
	 * @param expression - the {@link String} that contains the expression 
	 * @return - a {@link List} of matching {@link EnhancedCompiledArtifactIdentification}
	 */
	List<EnhancedCompiledArtifactIdentification> runQuery( String expression);
	
	/**
	 * runs an EXACT query on the expression passed
	 * @param expression
	 * @return
	 */
	List<EnhancedCompiledArtifactIdentification> runProjectToSourceQuery( String condensedArtifact);
	
	
	/**
	 * runs a query with groupId and artifactId only, i.e. {@code <groupId>:<artifactId>} 
	 * @param expression - the {@link String} that contains the matching {@link EnhancedCompiledArtifactIdentification}
	 * @return
	 */
	List<EnhancedCompiledArtifactIdentification> runPartialSourceArtifactQuery( String expression);
	
	/**
	 * basically identical to {@link QuickImportControl#runPartialSourceArtifactQuery(String)}
	 * @see runPartialSourceArtifactQuery
	 */
	List<EnhancedCompiledArtifactIdentification> runSourceArtifactQuery( String expression);
	
	/**
	 * @param txt - text string for 'contains' check
	 * @return - a list of matching {@link EnhancedCompiledArtifactIdentification}
	 */
	List<EnhancedCompiledArtifactIdentification> runContainsQuery(String txt);
	
	Set<EnhancedCompiledArtifactIdentification> acquirePopulation();
	
	/*
	List<EnhancedCompiledArtifactIdentification> runPomFileToSourceArtifactQuery( File pomFile);
	 */

	default void scheduleRescan() {
		Job.create("Scanning source repositories", (IProgressMonitor monitor) -> {
			rescan();
			return Status.OK_STATUS;
		}).schedule();
	}
	
}