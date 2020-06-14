/*
 * Copyright (c) 2011-2020, Peter Abeles. All Rights Reserved.
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

package boofcv.abst.feature.detdesc;

import boofcv.factory.feature.detdesc.FactoryDetectDescribe;
import boofcv.struct.feature.TupleDesc_F64;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.ImageType;
import boofcv.struct.image.Planar;

/**
 * @author Peter Abeles
 */
public class TestSurfPlanar_to_DetectDescribePoint extends
		GenericTestsDetectDescribePoint<Planar<GrayF32>,TupleDesc_F64>
{

	public TestSurfPlanar_to_DetectDescribePoint() {
		super(true, true, ImageType.pl(3, GrayF32.class), TupleDesc_F64.class);
	}

	@Override
	public DetectDescribePoint<Planar<GrayF32>, TupleDesc_F64> createDetDesc() {
		return FactoryDetectDescribe.surfColorStable(null, null, null, imageType);
	}
}
