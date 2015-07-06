/*
 * Copyright (c) 2011-2015, Peter Abeles. All Rights Reserved.
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

package boofcv.struct.convolve;

/**
 * Floating point 1D convolution kernel that extends {@link Kernel1D}.
 *
 * <p>
 * WARNING: Do not modify.  Automatically generated by {@link boofcv.struct.convolve.GenerateKernel1D}.
 * </p>
 *
 * @author Peter Abeles
 */
public class Kernel1D_F32 extends Kernel1D {

	public float data[];

	/**
	 * Creates a new kernel whose initial values are specified by "data" and length is "width". 
	 * The offset will be set to width/2
	 *
	 * @param data  The value of the kernel. Not modified.  Reference is not saved.
	 * @param width The kernels width.
	 */
	public Kernel1D_F32(float data[], int width) {
		this(data,width,width/2);
	}

	/**
	 * Creates a kernel with elements equal to 'data' and with the specified 'width' plus 'offset'
	 *
	 * @param data  The value of the kernel. Not modified.  Reference is not saved.
	 * @param width The kernels width.
	 * @param offset Location of the origin in the array
	 */
	public Kernel1D_F32(float data[], int width , int offset) {
		super(width,offset);

		this.data = new float[width];
		System.arraycopy(data, 0, this.data, 0, width);
	}

	/**
	 * Create a kernel with elements initialized to zero.  Offset is automatically
	 * set to width/2.
	 *
	 * @param width How wide the kernel is. 
	 */
	public Kernel1D_F32(int width) {
		this(width,width/2);
	}

	/**
	 * Create a kernel whose elements initialized to zero.
	 *
	 * @param width How wide the kernel is.
	 * @param offset Location of the origin in the array
	 */
	public Kernel1D_F32(int width , int offset) {
		super(width,offset);
		data = new float[width];
	}

	protected Kernel1D_F32() {
	}

	@Override
	public double getDouble(int index) {
		return data[index];
	}

	/**
	 * Creates a kernel whose elements are the specified data array and has
	 * the specified width.
	 *
	 * @param data  The array who will be the kernel's data.  Reference is saved.
	 * @param width The kernel's width.
	 * @param offset Location of the origin in the array
	 * @return A new kernel.
	 */
	public static Kernel1D_F32 wrap(float data[], int width, int offset ) {
		Kernel1D_F32 ret = new Kernel1D_F32();
		ret.data = data;
		ret.width = width;
		ret.offset = offset;

		return ret;
	}

	@Override
	public Kernel1D_F32 copy() {
		Kernel1D_F32 ret = new Kernel1D_F32(width,offset);
		System.arraycopy(data,0,ret.data,0,ret.width);
		return ret;
	}

	@Override
	public boolean isInteger() {
		return false;
	}

	public float get(int i) {
		return data[i];
	}

	public float computeSum() {
		float sum = 0;
		for( int i = 0; i < data.length; i++ ) {
			sum += data[i];
		}

		return sum;
	}

	public float[] getData() {
		return data;
	}

	public void print() {
		for (int i = 0; i < width; i++) {
			System.out.printf("%6.3f ", data[i]);
		}
		System.out.println();
	}
}

