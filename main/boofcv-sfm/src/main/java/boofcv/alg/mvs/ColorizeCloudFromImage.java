/*
 * Copyright (c) 2020, Peter Abeles. All Rights Reserved.
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

package boofcv.alg.mvs;

import boofcv.core.image.LookUpColorRgb;
import boofcv.misc.BoofMiscOps;
import boofcv.struct.distort.Point2Transform2_F64;
import boofcv.struct.image.ImageBase;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Point4D_F64;
import georegression.struct.se.Se3_F64;
import georegression.transform.se.SePointOps_F64;
import lombok.Getter;

import java.util.List;

/**
 * Given a set of 3D points and the image they are from, extract the RGB value. Since only a single image is used
 * there is the potential for more noise. However this is much simpler and faster.
 *
 * @author Peter Abeles
 */
public class ColorizeCloudFromImage<T extends ImageBase<T>> {
	protected final @Getter LookUpColorRgb<T> colorLookup;

	protected final Point3D_F64 viewPt = new Point3D_F64();
	protected final Point4D_F64 viewPt4 = new Point4D_F64();
	protected final Point2D_F64 pixel = new Point2D_F64();

	public ColorizeCloudFromImage( LookUpColorRgb<T> colorLookup ) {
		this.colorLookup = colorLookup;
	}

	/**
	 * Colorizes all the points in the specified range using the specified image.
	 *
	 * @param image (Input) Which image is being considered
	 * @param cloud (Input) The point cloud
	 * @param idx0 (Input) The first point in the point cloud that's inside this image. Inclusive.
	 * @param idx1 (Input) The last point in the point cloud that's inside this image. Exclusive.
	 * @param world_to_view (Input) Transform from world (cloud) into this image/view.
	 * @param norm_to_pixel (Input) Normalized image coordinates into pixel coordinates.
	 * @param colorizer (Output) As the color of each point becomes known this function is invoked.
	 */
	public void process3( T image, List<Point3D_F64> cloud, int idx0, int idx1, Se3_F64 world_to_view,
						  Point2Transform2_F64 norm_to_pixel, IndexColor colorizer ) {
		colorLookup.setImage(image);
		for (int i = idx0; i < idx1; i++) {
			world_to_view.transform(cloud.get(i), viewPt);

			// See if the point is behind the camera
			if (viewPt.z <= 0.0)
				continue;

			norm_to_pixel.compute(viewPt.x/viewPt.z, viewPt.y/viewPt.z, pixel);

			if (!BoofMiscOps.isInside(image, pixel.x, pixel.y))
				continue;

			int xx = (int)(pixel.x+0.5);
			int yy = (int)(pixel.y+0.5);

			int rgb = colorLookup.lookupRgb(xx, yy);
			colorizer.setRgb(i, (rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
		}
	}

	/**
	 * Colorizes all the points in the specified range using the specified image.
	 *
	 * @param image (Input) Which image is being considered
	 * @param cloud (Input) The point cloud in homogenous coordinates
	 * @param idx0 (Input) The first point in the point cloud that's inside this image. Inclusive.
	 * @param idx1 (Input) The last point in the point cloud that's inside this image. Exclusive.
	 * @param world_to_view (Input) Transform from world (cloud) into this image/view.
	 * @param norm_to_pixel (Input) Normalized image coordinates into pixel coordinates.
	 * @param colorizer (Output) As the color of each point becomes known this function is invoked.
	 */
	public void process4( T image, List<Point4D_F64> cloud, int idx0, int idx1, Se3_F64 world_to_view,
						  Point2Transform2_F64 norm_to_pixel, IndexColor colorizer ) {
		colorLookup.setImage(image);
		for (int i = idx0; i < idx1; i++) {
			SePointOps_F64.transform(world_to_view, cloud.get(i), viewPt4);

			// See if the point is behind the camera
			if (Math.signum(viewPt4.z)*Math.signum(viewPt4.w) < 0)
				continue;

			// w component is ignored. x = [I(3) 0]*X
			norm_to_pixel.compute(viewPt.x/viewPt.z, viewPt.y/viewPt.z, pixel);

			if (!BoofMiscOps.isInside(image, pixel.x, pixel.y))
				continue;

			int xx = (int)(pixel.x+0.5);
			int yy = (int)(pixel.y+0.5);

			int rgb = colorLookup.lookupRgb(xx, yy);
			colorizer.setRgb(i, (rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
		}
	}
}