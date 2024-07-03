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
package com.braintribe.devrock.api.process;

@SuppressWarnings("serial")
public class ProcessException extends Exception {

	public ProcessException() {
	}

	public ProcessException(String arg0) {
		super(arg0);
	}

	public ProcessException(Throwable arg0) {
		super(arg0);
	}

	public ProcessException(String arg0, Throwable arg1) {
		super(arg0, arg1);	
	}

}
