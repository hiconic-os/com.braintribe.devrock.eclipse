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
package com.braintribe.plugin.commons.clipboard;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;

import com.braintribe.codec.marshaller.bin.BinMarshaller;
import com.braintribe.model.generic.GenericEntity;

/**
 * an generic {@link Transfer} for {@link GenericEntity} based structures - currently root must be a generic entity itself!! 
 * @author pit
 *
 */
public class AssemblyTransfer extends ByteArrayTransfer {
	 private static final String ASSEMBLY_TYPE_NAME = "gm_assembly";
     private static final int ASSEMBLY_TYPE_ID = registerType(ASSEMBLY_TYPE_NAME);
     private static AssemblyTransfer _instance = new AssemblyTransfer();
     
     private static BinMarshaller marshaller = new BinMarshaller();
     
     private AssemblyTransfer() {}
     
     public static AssemblyTransfer getInstance() {
    	 return _instance;
     }
     
	@Override
	protected int[] getTypeIds() {		
		return new int[] {ASSEMBLY_TYPE_ID};
	}

	@Override
	protected String[] getTypeNames() {
		return new String[] {ASSEMBLY_TYPE_NAME};
	}

	 public void javaToNative (Object object, TransferData transferData) {
	        if (object == null || !(object instanceof GenericEntity)) return;
	        	        
	        try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				marshaller.marshall(out, object);
				byte [] buffer = out.toByteArray();
				out.close();
				
				super.javaToNative(buffer, transferData);				
			} catch (Exception e) {
				throw new TransferException("cannot marshall passed data", e);
			} 
	        
	        
	 }
	 
	 public Object nativeToJava(TransferData transferData) {
		 if (isSupportedType(transferData)) {
             
             try {
				byte[] buffer = (byte[])super.nativeToJava(transferData);
				 if (buffer == null) {
					 return null;
				 }
				 ByteArrayInputStream in = new ByteArrayInputStream(buffer);
				 Object result = marshaller.unmarshall(in);
				 in.close();
				 return result;
			} catch (Exception e) {
				throw new TransferException( "cannot unmarshall passed data", e);
			}
		 }
		 return null;

	 }
	 
}
