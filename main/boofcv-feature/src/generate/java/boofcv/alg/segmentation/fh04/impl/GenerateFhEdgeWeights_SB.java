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

package boofcv.alg.segmentation.fh04.impl;

import boofcv.generate.AutoTypeImage;
import boofcv.generate.CodeGeneratorBase;
import boofcv.struct.ConnectRule;

import java.io.FileNotFoundException;

/**
 * @author Peter Abeles
 */
public class GenerateFhEdgeWeights_SB extends CodeGeneratorBase {


	@Override
	public void generateCode() throws FileNotFoundException {
		create(AutoTypeImage.F32, ConnectRule.EIGHT);
		create(AutoTypeImage.U8, ConnectRule.EIGHT);
		create(AutoTypeImage.F32, ConnectRule.FOUR);
		create(AutoTypeImage.U8, ConnectRule.FOUR);
	}

	protected void create( AutoTypeImage imageType , ConnectRule rule ) throws FileNotFoundException {

		String name = "FhEdgeWeights"+rule.getShortName()+"_"+imageType.getAbbreviatedType();
		setOutputFile(name);
		printPreamble(imageType,rule);
		printProcess(imageType,rule);
		printCheckAround(imageType,rule);
		printCheck(imageType);
		printType(imageType);
		out.print("}\n");
	}

	private void printPreamble( AutoTypeImage imageType , ConnectRule rule ) {

		String imageName = imageType.getSingleBandName();

		int N = rule == ConnectRule.EIGHT ? 8 : 4;

		out.print("import boofcv.struct.image."+imageName+";\n" +
				"import boofcv.alg.segmentation.fh04.FhEdgeWeights;\n" +
				"import boofcv.struct.image.ImageType;\n" +
				"import org.ddogleg.struct.FastQueue;\n" +
				"\n" +
				"import static boofcv.alg.segmentation.fh04.SegmentFelzenszwalbHuttenlocher04.Edge;\n" +
				"\n" +
				"/**\n" +
				" * <p>Computes edge weight as the absolute value of the different in pixel value for single band images.\n" +
				" * A "+N+"-connect neighborhood is considered.</p>\n" +
				" *\n" +
				" * <p>\n" +
				" * WARNING: Do not modify.  Automatically generated by {@link "+getClass().getSimpleName()+"}.\n" +
				" * </p>\n" +
				" *\n" +
				" * @author Peter Abeles\n" +
				" */\n" +
				"public class "+className+" implements FhEdgeWeights<"+imageName+"> {\n\n");
	}

	private void printProcess( AutoTypeImage imageType , ConnectRule rule ) {

		String imageName = imageType.getSingleBandName();
		String sumType = imageType.getSumType();
		String bitwise = imageType.getBitWise();

		int startX = rule == ConnectRule.FOUR ? 0 : 1;

		out.print("\t@Override\n" +
				"\tpublic void process("+imageName+" input,\n" +
				"\t\t\t\t\t\tFastQueue<Edge> edges) {\n" +
				"\n" +
				"\t\tint w = input.width-1;\n" +
				"\t\tint h = input.height-1;\n" +
				"\n" +
				"\t\t// First consider the inner pixels\n" +
				"\t\tfor( int y = 0; y < h; y++ ) {\n" +
				"\t\t\tint indexSrc = input.startIndex + y*input.stride + "+startX+";\n" +
				"\t\t\tint indexDst =                  + y*input.width  + "+startX+";\n" +
				"\n" +
				"\t\t\tfor( int x = "+startX+"; x < w; x++ , indexSrc++ , indexDst++ ) {\n" +
				"\t\t\t\t"+sumType+" color0 = input.data[indexSrc]"+bitwise+";              // (x,y)\n" +
				"\t\t\t\t"+sumType+" color1 = input.data[indexSrc+1]"+bitwise+";            // (x+1,y)\n" +
				"\t\t\t\t"+sumType+" color2 = input.data[indexSrc+input.stride]"+bitwise+"; // (x,y+1)\n" +
				"\n" +
				"\t\t\t\tEdge e1 = edges.grow();\n" +
				"\t\t\t\tEdge e2 = edges.grow();\n" +
				"\n" +
				"\t\t\t\te1.sortValue = Math.abs(color1-color0);\n" +
				"\t\t\t\te1.indexA = indexDst;\n" +
				"\t\t\t\te1.indexB = indexDst+1;\n" +
				"\n" +
				"\t\t\t\te2.sortValue = Math.abs(color2-color0);\n" +
				"\t\t\t\te2.indexA = indexDst;\n" +
				"\t\t\t\te2.indexB = indexDst+input.width;\n");
		if( rule == ConnectRule.EIGHT ) {
			out.print(
				"\n" +
				"\t\t\t\t"+sumType+" color3 = input.data[indexSrc+1+input.stride]"+bitwise+"; // (x+1,y+1)\n" +
				"\t\t\t\t"+sumType+" color4 = input.data[indexSrc-1+input.stride]"+bitwise+"; // (x-1,y+1)\n" +
				"\n" +
				"\t\t\t\tEdge e3 = edges.grow();\n" +
				"\t\t\t\tEdge e4 = edges.grow();\n" +
				"\n" +
				"\t\t\t\te3.sortValue = Math.abs(color3-color0);\n" +
				"\t\t\t\te3.indexA = indexDst;\n" +
				"\t\t\t\te3.indexB = indexDst+1+input.width;\n" +
				"\n" +
				"\t\t\t\te4.sortValue = Math.abs(color4-color0);\n" +
				"\t\t\t\te4.indexA = indexDst;\n" +
				"\t\t\t\te4.indexB = indexDst-1+input.width;\n");
		}

		out.print("\t\t\t}\n" +
				"\t\t}\n"+
				"\t\t// Handle border pixels\n");

		if( rule == ConnectRule.EIGHT ) {
			out.print(
					"\t\tfor( int y = 0; y < h; y++ ) {\n" +
					"\t\t\tcheckAround(0,y,input,edges);\n" +
					"\t\t\tcheckAround(w,y,input,edges);\n" +
					"\t\t}\n" +
					"\n" +
					"\t\tfor( int x = 0; x < w; x++ ) {\n" +
					"\t\t\tcheckAround(x,h,input,edges);\n" +
					"\t\t}\n");
		} else {
			out.print(
					"\t\tfor( int y = 0; y < h; y++ ) {\n" +
					"\t\t\tcheckAround(w,y,input,edges);\n" +
					"\t\t}\n" +
					"\n" +
					"\t\tfor( int x = 0; x < w; x++ ) {\n" +
					"\t\t\tcheckAround(x,h,input,edges);\n" +
					"\t\t}\n");
		}

		out.print("\t}\n");
	}

	private void printCheckAround( AutoTypeImage imageType , ConnectRule rule ) {

		String imageName = imageType.getSingleBandName();
		String bitwise = imageType.getBitWise();
		String sumType = imageType.getSumType();

		out.print("\tprivate void checkAround( int x , int y ,\n" +
				"\t\t\t\t\t\t\t  "+imageName+" input ,\n" +
				"\t\t\t\t\t\t\t  FastQueue<Edge> edges )\n" +
				"\t{\n" +
				"\t\tint indexSrc = input.startIndex + y*input.stride + x;\n" +
				"\t\tint indexA =                      y*input.width  + x;\n" +
				"\n" +
				"\t\t"+sumType+" color0 = input.data[indexSrc]"+bitwise+";\n" +
				"\n" +
				"\t\tcheck(x+1,y  ,color0,indexA,input,edges);\n" +
				"\t\tcheck(x  ,y+1,color0,indexA,input,edges);\n");

		if( rule == ConnectRule.EIGHT ) {
			out.print(
				"\t\tcheck(x+1,y+1,color0,indexA,input,edges);\n" +
				"\t\tcheck(x-1,y+1,color0,indexA,input,edges);\n");
		}
		out.print("\t}\n\n");
	}

	private void printCheck( AutoTypeImage imageType ) {

		String imageName = imageType.getSingleBandName();
		String sumType = imageType.getSumType();
		String bitwise = imageType.getBitWise();

		out.print("\tprivate void check( int x , int y , "+sumType+" color0 , int indexA,\n" +
				"\t\t\t\t\t\t"+imageName+" input ,\n" +
				"\t\t\t\t\t\tFastQueue<Edge> edges ) {\n" +
				"\t\tif( !input.isInBounds(x,y) )\n" +
				"\t\t\treturn;\n" +
				"\n" +
				"\t\tint indexSrc = input.startIndex + y*input.stride + x;\n" +
				"\t\tint indexB   =                  + y*input.width  + x;\n" +
				"\n" +
				"\t\t"+sumType+" colorN = input.data[indexSrc]"+bitwise+";\n" +
				"\n" +
				"\t\tEdge e1 = edges.grow();\n" +
				"\n" +
				"\t\te1.sortValue = (float)Math.abs(color0-colorN);\n" +
				"\t\te1.indexA = indexA;\n" +
				"\t\te1.indexB = indexB;\n" +
				"\t}\n\n");
	}

	private void printType( AutoTypeImage imageType ) {
		String imageName = imageType.getSingleBandName();

		out.print("\t@Override\n" +
				"\tpublic ImageType<"+imageName+"> getInputType() {\n" +
				"\t\treturn ImageType.single("+imageName+".class);\n" +
				"\t}\n\n");
	}

	public static void main(String[] args) throws FileNotFoundException {
		GenerateFhEdgeWeights_SB generator = new GenerateFhEdgeWeights_SB();
		generator.generateCode();
	}
}
