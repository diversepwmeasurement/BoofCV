/*
 * Copyright (c) 2023, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://boofcv.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package boofcv.alg.feature.orientation.impl;

import boofcv.alg.feature.orientation.GenericOrientationGradientTests;
import boofcv.struct.image.GrayF32;
import boofcv.testing.BoofStandardJUnit;
import org.junit.jupiter.api.Nested;


public class TestImplOrientationHistogram_F32 extends BoofStandardJUnit {

	protected static final int N = 10;

	class Base extends GenericOrientationGradientTests {
		Base( boolean weighted ) {
			super(2.0*Math.PI/N,r*2+1,GrayF32.class);
			ImplOrientationHistogram_F32 alg = new ImplOrientationHistogram_F32(1.0/2.0,N,weighted);
			alg.setObjectToSample(r);
			setRegionOrientation(alg);
		}
	}

	@Nested
	class Unweighted extends Base {
		Unweighted() {
			super(false);
		}
	}

	@Nested
	class Weighted extends Base {
		Weighted() {
			super(true);
		}
	}

}
