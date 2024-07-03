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
package com.braintribe.devrock.greyface.process.scan;

import java.util.concurrent.BlockingQueue;

import com.braintribe.build.artifact.retrieval.multi.resolving.ResolvingException;

public class AsynchronousScanExpert extends AbstractScanExpert implements Runnable {

	private ScanTuple scanTuple;
	private BlockingQueue<ScanTuple> queue;
	
	public void setQueue(BlockingQueue<ScanTuple> queue) {
		this.queue = queue;
	}
	
	public void setScanTuple(ScanTuple scanTuple) {
		this.scanTuple = scanTuple;
	}
	
	
	@Override
	protected void handleFoundDependency(ScanTuple scanTuple) throws ResolvingException {
		queue.offer(scanTuple);
	}

	@Override
	public void run() {	
		try {
			scanDependency(scanTuple.dependency, scanTuple.level, scanTuple.index, scanTuple.importParent);
		} catch (ResolvingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
