/*
 * Copyright (c) 2011-2019, Peter Abeles. All Rights Reserved.
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

package boofcv.alg.border;

import boofcv.struct.border.*;
import boofcv.struct.image.*;

import javax.annotation.Generated;

/**
 * Implementations of {@link GrowBorder} for single band images.
 *
 * <p>
 * DO NOT MODIFY. This code was automatically generated by GenerateGrowBorderSB.
 * <p>
 *
 * @author Peter Abeles
 */
@Generated("boofcv.alg.border.GenerateGrowBorderSB")
public interface GrowBorderSB<T extends ImageGray<T>,PixelArray> extends  GrowBorder<T,PixelArray>
{
	abstract class SB_I_S32<T extends GrayI<T>,PixelArray> implements GrowBorderSB<T,PixelArray> {
		T image;
		ImageBorder_S32<T> border;
		ImageType<T> imageType;

		public SB_I_S32(ImageType<T> imageType) {
			this.imageType = imageType;
		}

		@Override
		public void setBorder(ImageBorder<T> _border) {
			border = (ImageBorder_S32<T>)_border;
		}

		@Override
		public void setImage(T image) {
			this.image = image;
			this.border.setImage(image);
		}

		@Override
		public ImageType<T> getImageType() {
			return imageType;
		}
	}

	class SB_I8<T extends GrayI8<T>> extends SB_I_S32<T,byte[]> {

		public SB_I8(ImageType<T> imageType) {
			super(imageType);
		}

		@Override
		public void growRow(int y, int borderLower , int borderUpper, byte[] output, int offset) {
			int idxDst = offset;
			if( y < 0 || y >= image.height ) {
				int end = image.width+borderUpper;
				for (int i = -borderLower; i < end; i++) {
					output[idxDst++] = (byte)border.getOutside(i, y);
				}
			} else {
				for (int i = borderLower; i > 0; i--) {
					output[idxDst++] = (byte)border.getOutside(-i, y);
				}
				System.arraycopy(image.data, image.getIndex(0, y), output, idxDst, image.width);
				idxDst += image.width;
				for (int i = 0; i < borderUpper; i++) {
					output[idxDst++] = (byte)border.getOutside(image.width + i, y);
				}
			}
		}

		@Override
		public void growCol(int x, int borderLower , int borderUpper, byte[] output, int offset) {
			int idxDst = offset;

			if( x < 0 || x >= image.width ) {
				int end = image.height+borderUpper;
				for (int i = -borderLower; i < end; i++) {
					output[idxDst++] = (byte)border.getOutside(x, i);
				}
			} else {
				int idxSrc = image.startIndex + x;
				for (int i = borderLower; i > 0; i--) {
					output[idxDst++] = (byte)border.getOutside(x, -i);
				}
				for (int y = 0; y < image.height; y++, idxSrc += image.stride) {
					output[idxDst++] = image.data[idxSrc];
				}
				for (int i = 0; i < borderUpper; i++) {
					output[idxDst++] = (byte)border.getOutside(x, image.height + i);
				}
			}		}
	}

	class SB_I16<T extends GrayI16<T>> extends SB_I_S32<T,short[]> {

		public SB_I16(ImageType<T> imageType) {
			super(imageType);
		}

		@Override
		public void growRow(int y, int borderLower , int borderUpper, short[] output, int offset) {
			int idxDst = offset;
			if( y < 0 || y >= image.height ) {
				int end = image.width+borderUpper;
				for (int i = -borderLower; i < end; i++) {
					output[idxDst++] = (short)border.getOutside(i, y);
				}
			} else {
				for (int i = borderLower; i > 0; i--) {
					output[idxDst++] = (short)border.getOutside(-i, y);
				}
				System.arraycopy(image.data, image.getIndex(0, y), output, idxDst, image.width);
				idxDst += image.width;
				for (int i = 0; i < borderUpper; i++) {
					output[idxDst++] = (short)border.getOutside(image.width + i, y);
				}
			}
		}

		@Override
		public void growCol(int x, int borderLower , int borderUpper, short[] output, int offset) {
			int idxDst = offset;

			if( x < 0 || x >= image.width ) {
				int end = image.height+borderUpper;
				for (int i = -borderLower; i < end; i++) {
					output[idxDst++] = (short)border.getOutside(x, i);
				}
			} else {
				int idxSrc = image.startIndex + x;
				for (int i = borderLower; i > 0; i--) {
					output[idxDst++] = (short)border.getOutside(x, -i);
				}
				for (int y = 0; y < image.height; y++, idxSrc += image.stride) {
					output[idxDst++] = image.data[idxSrc];
				}
				for (int i = 0; i < borderUpper; i++) {
					output[idxDst++] = (short)border.getOutside(x, image.height + i);
				}
			}		}
	}

	class SB_S32 implements GrowBorderSB<GrayS32,int[]> {
		GrayS32 image;
		ImageBorder_S32<GrayS32> border;

		@Override
		public void setBorder(ImageBorder<GrayS32> border) {
			this.border = (ImageBorder_S32<GrayS32>)border;
		}

		@Override
		public void setImage(GrayS32 image) {
			this.image = image;
			this.border.setImage(image);
		}

		@Override
		public ImageType<GrayS32> getImageType() {
			return ImageType.SB_S32;
		}
		@Override
		public void growRow(int y, int borderLower , int borderUpper, int[] output, int offset) {
			int idxDst = offset;
			if( y < 0 || y >= image.height ) {
				int end = image.width+borderUpper;
				for (int i = -borderLower; i < end; i++) {
					output[idxDst++] = border.getOutside(i, y);
				}
			} else {
				for (int i = borderLower; i > 0; i--) {
					output[idxDst++] = border.getOutside(-i, y);
				}
				System.arraycopy(image.data, image.getIndex(0, y), output, idxDst, image.width);
				idxDst += image.width;
				for (int i = 0; i < borderUpper; i++) {
					output[idxDst++] = border.getOutside(image.width + i, y);
				}
			}
		}

		@Override
		public void growCol(int x, int borderLower , int borderUpper, int[] output, int offset) {
			int idxDst = offset;

			if( x < 0 || x >= image.width ) {
				int end = image.height+borderUpper;
				for (int i = -borderLower; i < end; i++) {
					output[idxDst++] = border.getOutside(x, i);
				}
			} else {
				int idxSrc = image.startIndex + x;
				for (int i = borderLower; i > 0; i--) {
					output[idxDst++] = border.getOutside(x, -i);
				}
				for (int y = 0; y < image.height; y++, idxSrc += image.stride) {
					output[idxDst++] = image.data[idxSrc];
				}
				for (int i = 0; i < borderUpper; i++) {
					output[idxDst++] = border.getOutside(x, image.height + i);
				}
			}		}
	}

	class SB_S64 implements GrowBorderSB<GrayS64,long[]> {
		GrayS64 image;
		ImageBorder_S64 border;

		@Override
		public void setBorder(ImageBorder<GrayS64> border) {
			this.border = (ImageBorder_S64)border;
		}

		@Override
		public void setImage(GrayS64 image) {
			this.image = image;
			this.border.setImage(image);
		}

		@Override
		public ImageType<GrayS64> getImageType() {
			return ImageType.SB_S64;
		}
		@Override
		public void growRow(int y, int borderLower , int borderUpper, long[] output, int offset) {
			int idxDst = offset;
			if( y < 0 || y >= image.height ) {
				int end = image.width+borderUpper;
				for (int i = -borderLower; i < end; i++) {
					output[idxDst++] = border.getOutside(i, y);
				}
			} else {
				for (int i = borderLower; i > 0; i--) {
					output[idxDst++] = border.getOutside(-i, y);
				}
				System.arraycopy(image.data, image.getIndex(0, y), output, idxDst, image.width);
				idxDst += image.width;
				for (int i = 0; i < borderUpper; i++) {
					output[idxDst++] = border.getOutside(image.width + i, y);
				}
			}
		}

		@Override
		public void growCol(int x, int borderLower , int borderUpper, long[] output, int offset) {
			int idxDst = offset;

			if( x < 0 || x >= image.width ) {
				int end = image.height+borderUpper;
				for (int i = -borderLower; i < end; i++) {
					output[idxDst++] = border.getOutside(x, i);
				}
			} else {
				int idxSrc = image.startIndex + x;
				for (int i = borderLower; i > 0; i--) {
					output[idxDst++] = border.getOutside(x, -i);
				}
				for (int y = 0; y < image.height; y++, idxSrc += image.stride) {
					output[idxDst++] = image.data[idxSrc];
				}
				for (int i = 0; i < borderUpper; i++) {
					output[idxDst++] = border.getOutside(x, image.height + i);
				}
			}		}
	}

	class SB_F32 implements GrowBorderSB<GrayF32,float[]> {
		GrayF32 image;
		ImageBorder_F32 border;

		@Override
		public void setBorder(ImageBorder<GrayF32> border) {
			this.border = (ImageBorder_F32)border;
		}

		@Override
		public void setImage(GrayF32 image) {
			this.image = image;
			this.border.setImage(image);
		}

		@Override
		public ImageType<GrayF32> getImageType() {
			return ImageType.SB_F32;
		}
		@Override
		public void growRow(int y, int borderLower , int borderUpper, float[] output, int offset) {
			int idxDst = offset;
			if( y < 0 || y >= image.height ) {
				int end = image.width+borderUpper;
				for (int i = -borderLower; i < end; i++) {
					output[idxDst++] = border.getOutside(i, y);
				}
			} else {
				for (int i = borderLower; i > 0; i--) {
					output[idxDst++] = border.getOutside(-i, y);
				}
				System.arraycopy(image.data, image.getIndex(0, y), output, idxDst, image.width);
				idxDst += image.width;
				for (int i = 0; i < borderUpper; i++) {
					output[idxDst++] = border.getOutside(image.width + i, y);
				}
			}
		}

		@Override
		public void growCol(int x, int borderLower , int borderUpper, float[] output, int offset) {
			int idxDst = offset;

			if( x < 0 || x >= image.width ) {
				int end = image.height+borderUpper;
				for (int i = -borderLower; i < end; i++) {
					output[idxDst++] = border.getOutside(x, i);
				}
			} else {
				int idxSrc = image.startIndex + x;
				for (int i = borderLower; i > 0; i--) {
					output[idxDst++] = border.getOutside(x, -i);
				}
				for (int y = 0; y < image.height; y++, idxSrc += image.stride) {
					output[idxDst++] = image.data[idxSrc];
				}
				for (int i = 0; i < borderUpper; i++) {
					output[idxDst++] = border.getOutside(x, image.height + i);
				}
			}		}
	}

	class SB_F64 implements GrowBorderSB<GrayF64,double[]> {
		GrayF64 image;
		ImageBorder_F64 border;

		@Override
		public void setBorder(ImageBorder<GrayF64> border) {
			this.border = (ImageBorder_F64)border;
		}

		@Override
		public void setImage(GrayF64 image) {
			this.image = image;
			this.border.setImage(image);
		}

		@Override
		public ImageType<GrayF64> getImageType() {
			return ImageType.SB_F64;
		}
		@Override
		public void growRow(int y, int borderLower , int borderUpper, double[] output, int offset) {
			int idxDst = offset;
			if( y < 0 || y >= image.height ) {
				int end = image.width+borderUpper;
				for (int i = -borderLower; i < end; i++) {
					output[idxDst++] = border.getOutside(i, y);
				}
			} else {
				for (int i = borderLower; i > 0; i--) {
					output[idxDst++] = border.getOutside(-i, y);
				}
				System.arraycopy(image.data, image.getIndex(0, y), output, idxDst, image.width);
				idxDst += image.width;
				for (int i = 0; i < borderUpper; i++) {
					output[idxDst++] = border.getOutside(image.width + i, y);
				}
			}
		}

		@Override
		public void growCol(int x, int borderLower , int borderUpper, double[] output, int offset) {
			int idxDst = offset;

			if( x < 0 || x >= image.width ) {
				int end = image.height+borderUpper;
				for (int i = -borderLower; i < end; i++) {
					output[idxDst++] = border.getOutside(x, i);
				}
			} else {
				int idxSrc = image.startIndex + x;
				for (int i = borderLower; i > 0; i--) {
					output[idxDst++] = border.getOutside(x, -i);
				}
				for (int y = 0; y < image.height; y++, idxSrc += image.stride) {
					output[idxDst++] = image.data[idxSrc];
				}
				for (int i = 0; i < borderUpper; i++) {
					output[idxDst++] = border.getOutside(x, image.height + i);
				}
			}		}
	}

}
