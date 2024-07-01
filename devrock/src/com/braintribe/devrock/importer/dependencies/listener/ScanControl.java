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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.devrock.importer.dependencies.listener;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.braintribe.devrock.eclipse.model.identification.RemoteCompiledDependencyIdentification;

public interface ScanControl {
	
	/**
	 * @return - true if a scan is currently running, false otherwise
	 */
	boolean isScanActive();

	/**
	 * starts a scan 
	 * @param monitor - the {@link IProgressMonitor}, may be null
	 */
	void rescan(IProgressMonitor monitor);
	
	/**
	 * stops a currently running scan - not implemented yet 
	 */
	void stop();
	
	
	/**
	 * @param listener - {@link RemoteRepositoryScanListener} to add 
	 */
	void addListener( RemoteRepositoryScanListener listener);
	
	/**
	 * @param listener - {@link RemoteRepositoryScanListener} to remove
	 */
	void removeListener( RemoteRepositoryScanListener listener);
		
	/**
	 * run a CamelCase-style query 
	 * @param txt - the expression
	 * @return - a {@link List} of matching {@link RemoteCompiledDependencyIdentification}
	 */
	List<RemoteCompiledDependencyIdentification> runQuery(String txt);
	
	/**
	 * run a 'contains'-query
	 * @param txt - the expression
	 * @return - a {@link List} of matching {@link RemoteCompiledDependencyIdentification}
	 */
	List<RemoteCompiledDependencyIdentification> runContainsQuery( String txt);
	
	default void scheduleRescan() {
		Job.create("Scanning source repositories", (IProgressMonitor monitor) -> {
			rescan( monitor);
			return Status.OK_STATUS;
		}).schedule();
	}
}
