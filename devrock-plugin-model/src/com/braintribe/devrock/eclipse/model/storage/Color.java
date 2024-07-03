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
package com.braintribe.devrock.eclipse.model.storage;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface Color extends GenericEntity{
	
	EntityType<Color> T = EntityTypes.T(Color.class);
	
	String red = "red";
	String green = "green";
	String blue = "blue";
	
	int getRed();
	void setRed(int value);

	int getGreen();
	void setGreen(int value);

	int getBlue();
	void setBlue(int value);

	
	static Color create( int r, int g, int b) {
		Color color = Color.T.create();
		color.setRed(r);
		color.setGreen(g);
		color.setBlue(b);
		return color;
	}
	
	default int [] asArray() {
		int [] retval = new int[3];
		retval[0] = getRed();
		retval[1] = getGreen();
		retval[2] = getBlue();
		return retval;
	}
}
