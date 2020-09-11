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

package boofcv.alg.feature.orientation.impl;

import boofcv.generate.AutoTypeImage;
import boofcv.generate.CodeGeneratorBase;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;


/**
 * @author Peter Abeles
 */
public class GenerateImplOrientationSlidingWindow extends CodeGeneratorBase {
	AutoTypeImage imageType;

	@Override
	public void generateCode() throws FileNotFoundException {
		printClass(AutoTypeImage.F32);
		printClass(AutoTypeImage.S16);
		printClass(AutoTypeImage.S32);
	}

	private void printClass( AutoTypeImage imageType ) throws FileNotFoundException {
		this.imageType = imageType;
		className = "ImplOrientationSlidingWindow_"+imageType.getAbbreviatedType();
		out = new PrintStream(new FileOutputStream(className + ".java"));
		printPreamble();
		printComputeAngles();
		printUnweighted();
		printWeighted();

		out.print("}\n");
	}

	private void printPreamble() throws FileNotFoundException {
		setOutputFile(className);
		out.print("import boofcv.alg.feature.orientation.OrientationSlidingWindow;\n" +
				"import boofcv.struct.image."+imageType.getSingleBandName()+";\n" +
				"import georegression.metric.UtilAngle;\n" +
				"\n" +
				"/**\n" +
				" * <p>\n" +
				" * Implementation of {@link OrientationSlidingWindow} for a specific image type.\n" +
				" * </p>\n" +
				" *\n" +
				" * <p>\n" +
				" * WARNING: Do not modify.  Automatically generated by {@link GenerateImplOrientationSlidingWindow}.\n" +
				" * </p>\n" +
				" *\n" +
				" * @author Peter Abeles\n" +
				" */\n" +
				"public class "+className+" extends OrientationSlidingWindow<"+imageType.getSingleBandName()+"> {\n" +
				"\n" +
				"\tpublic "+className+"(int numAngles, double windowSize, boolean isWeighted) {\n" +
				"\t\tsuper(numAngles, windowSize, isWeighted);\n" +
				"\t}\n" +
				"\n" +
				"\t@Override\n" +
				"\tpublic Class<"+imageType.getSingleBandName()+"> getImageType() {\n" +
				"\t\treturn "+imageType.getSingleBandName()+".class;\n" +
				"\t}\n\n");
	}

	private void printComputeAngles() {
		String bitWise = imageType.getBitWise();
		String sumType = imageType.getSumType();
		out.print("\tprivate void computeAngles() {\n" +
				"\t\tint i = 0;\n" +
				"\t\tfor( int y = rect.y0; y < rect.y1; y++ ) {\n" +
				"\t\t\tint indexX = derivX.startIndex + derivX.stride*y + rect.x0;\n" +
				"\t\t\tint indexY = derivY.startIndex + derivY.stride*y + rect.x0;\n" +
				"\n" +
				"\t\t\tfor( int x = rect.x0; x < rect.x1; x++ , indexX++ , indexY++ ) {\n" +
				"\t\t\t\t"+sumType+" dx = derivX.data[indexX]"+bitWise+";\n" +
				"\t\t\t\t"+sumType+" dy = derivY.data[indexY]"+bitWise+";\n" +
				"\n" +
				"\t\t\t\tangles[i++] = Math.atan2(dy,dx);\n" +
				"\t\t\t}\n" +
				"\t\t}\n" +
				"\t}\n\n");
	}

	private void printUnweighted() {
		out.print("\t@Override\n" +
				"\tprotected double computeOrientation() {\n" +
				"\t\tcomputeAngles();\n" +
				"\n" +
				"\t\tdouble windowRadius = windowSize/2.0;\n" +
				"\t\tint w = rect.x1-rect.x0;\n" +
				"\t\tdouble bestScore = -1;\n" +
				"\t\tdouble bestAngle = 0;\n" +
				"\t\tdouble stepAngle = Math.PI*2.0/numAngles;\n" +
				"\n" +
				"\t\tint N = w*(rect.y1-rect.y0);\n" +
				"\t\tfor( double angle = -Math.PI; angle < Math.PI; angle += stepAngle ) {\n" +
				"\t\t\tdouble dx = 0;\n" +
				"\t\t\tdouble dy = 0;\n" +
				"\t\t\tfor( int i = 0; i < N; i++ ) {\n" +
				"\t\t\t\tdouble diff = UtilAngle.dist(angle,angles[i]);\n" +
				"\t\t\t\tif( diff <= windowRadius) {\n" +
				"\t\t\t\t\tint x = rect.x0 + i % w;\n" +
				"\t\t\t\t\tint y = rect.y0 + i / w;\n" +
				"\t\t\t\t\tdx += derivX.get(x,y);\n" +
				"\t\t\t\t\tdy += derivY.get(x,y);\n" +
				"\t\t\t\t}\n" +
				"\t\t\t}\n" +
				"\t\t\tdouble n = dx*dx + dy*dy;\n" +
				"\t\t\tif( n > bestScore) {\n" +
				"\t\t\t\tbestAngle = Math.atan2(dy,dx);\n" +
				"\t\t\t\tbestScore = n;\n" +
				"\t\t\t}\n" +
				"\t\t}\n" +
				"\n" +
				"\t\treturn bestAngle;\n" +
				"\t}\n\n");
	}

	private void printWeighted() {
		out.print("\t@Override\n" +
				"\tprotected double computeWeightedOrientation(int c_x, int c_y) {\n" +
				"\t\tcomputeAngles();\n" +
				"\n" +
				"\t\tdouble windowRadius = windowSize/2.0;\n" +
				"\t\tint w = rect.x1-rect.x0;\n" +
				"\t\tdouble bestScore = -1;\n" +
				"\t\tdouble bestAngle = 0;\n" +
				"\t\tdouble stepAngle = Math.PI*2.0/numAngles;\n" +
				"\t\tint N = w*(rect.y1-rect.y0);\n" +
				"\n" +
				"\t\tfor( double angle = -Math.PI; angle < Math.PI; angle += stepAngle ) {\n" +
				"\t\t\tdouble dx = 0;\n" +
				"\t\t\tdouble dy = 0;\n" +
				"\t\t\tfor( int i = 0; i < N; i++ ) {\n" +
				"\t\t\t\tdouble diff = UtilAngle.dist(angle,angles[i]);\n" +
				"\t\t\t\tif( diff <= windowRadius) {\n" +
				"\t\t\t\t\tint localX = i%w;\n" +
				"\t\t\t\t\tint localY = i/w;\n" +
				"\t\t\t\t\tdouble ww = weights.get(localX,localY);\n" +
				"\t\t\t\t\tint x = rect.x0 + i % w;\n" +
				"\t\t\t\t\tint y = rect.y0 + i / w;\n" +
				"\t\t\t\t\tdx += ww*derivX.get(x,y);\n" +
				"\t\t\t\t\tdy += ww*derivY.get(x,y);\n" +
				"\t\t\t\t}\n" +
				"\t\t\t}\n" +
				"\t\t\tdouble n = dx*dx + dy*dy;\n" +
				"\t\t\tif( n > bestScore) {\n" +
				"\t\t\t\tbestAngle = Math.atan2(dy,dx);\n" +
				"\t\t\t\tbestScore = n;\n" +
				"\t\t\t}\n" +
				"\t\t}\n" +
				"\n" +
				"\t\treturn bestAngle;\n" +
				"\t}\n\n");
	}

	public static void main( String argsp[] ) throws FileNotFoundException {
		GenerateImplOrientationSlidingWindow app = new GenerateImplOrientationSlidingWindow();
		app.generateCode();
	}
}
