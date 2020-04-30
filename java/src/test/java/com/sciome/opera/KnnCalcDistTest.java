package com.sciome.opera;

import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

import com.sciome.opera.calc.math.KnnCalcDist;
import com.sciome.opera.calc.math.Utils;
import com.sciome.opera.enums.DistanceType;
import com.sciome.opera.enums.PretreatmentType;

public class KnnCalcDistTest {

	@Test
	public void testEuclidean() {
		double[][] XArray = {{0.18827, 0.98788, 0.00017015},{0.19305, 0.91994, 0.45028},{0.35528, 0.32526, 0.043356}};
		double[][] XtestArray = {{0.035121, 0.55492, 0.65605},{0.99968, 0.34005, 0.25262},{0.6403, 0.69381, 0.17084}};
		RealMatrix X = MatrixUtils.createRealMatrix(XArray);
		RealMatrix Xnew = MatrixUtils.createRealMatrix(XtestArray);
		RealMatrix test = KnnCalcDist.calculate(X, Xnew, DistanceType.euclidean, PretreatmentType.cent);
		
		double[][] actualArray =  {{0.80067, 0.44779, 0.72845},{1.0685, 1.0129, 0.67769},{0.56563, 0.57381, 0.48303}};
		RealMatrix actual = MatrixUtils.createRealMatrix(actualArray);
		assertTrue(Utils.compareMatrix(test, actual, .0001));
	}
}
