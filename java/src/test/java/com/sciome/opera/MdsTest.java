package com.sciome.opera;

import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

import com.sciome.opera.calc.math.Mds;
import com.sciome.opera.model.MdsResult;

public class MdsTest {

	@Test
	public void test() {
		double[][] dArray = {{0.80409, 0.6449, 0.03776},{0.32608, 0.70096, 0.45571},{0.90931, 0.39327, 0.98625}};
		RealMatrix d = MatrixUtils.createRealMatrix(dArray);
		MdsResult test = Mds.calc(d, 5);
		
		double[][] positionsArray = {{-0.076698, 0.44614, -0.36944},{-0.35978, 0.12914, 0.23064},{3.6944e-09, 3.6944e-09, 3.6944e-09}};
		double[] error = {2.9172, 3.0339, 3.0339};
		double[] ev = {.34141, .19931, 4.0946e-17};
		MdsResult actual = new MdsResult(MatrixUtils.createRealMatrix(positionsArray), MatrixUtils.createRealVector(error), MatrixUtils.createRealVector(ev));
		assertTrue(test.equals(actual));
	}
	
	@Test
	public void test2() {
		double[][] dArray = {{0.25355, 0.1952, 0.58838, 0.88638, 0.69983},{0.45416, 0.40078, 0.36359, 0.73949, 0.47885},{0.12612, 0.34148, 0.28231, 0.30562,
			0.12276},{0.80915, 0.29295, 0.51454, 0.60836, 0.53379},{0.2681, 0.60211, 0.36709, 0.0059107, 0.89158}};
		RealMatrix d = MatrixUtils.createRealMatrix(dArray);
		MdsResult test = Mds.calc(d, 5);
		System.out.println(test);
		
		double[][] positionsArray = {{-0.12252, 0.22797, -0.057102, -0.40008, 0.35173},{-0.41023, -0.053784, 0.019194, 0.25719, 0.18763},{-0.12806, 0.28066, 
			0.054362, 0.0050197, -0.21198},{-0.041243, -0.077139, 0.22288, -0.082489, -0.022013},{-2.5792e-09, -2.5792e-09, -2.5792e-09, -2.5792e-09, -2.5792e-09}};
		double[] error = {3.7356, 3.0336, 3.2165, 3.3836, 3.3836};
		double[] ev = {3.5402e-1, 2.729e-1, 1.4309e-1, 6.4618e-2, 3.3261e-17};
		MdsResult actual = new MdsResult(MatrixUtils.createRealMatrix(positionsArray), MatrixUtils.createRealVector(error), MatrixUtils.createRealVector(ev));
		assertTrue(test.equals(actual));
	}

}
	