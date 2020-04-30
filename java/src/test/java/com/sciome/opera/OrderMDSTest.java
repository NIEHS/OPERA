package com.sciome.opera;

import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

import com.sciome.opera.calc.math.OrderMDS;
import com.sciome.opera.model.OrderMDSResult;

public class OrderMDSTest {
	public void testCalc() {
		double[][] gDistArray = {{0.25355, 0.1952, 0.58838, 0.88638, 0.69983},{0.45416, 0.40078, 0.36359, 0.73949, 0.47885},{0.12612, 0.34148, 0.28231, 0.30562
			, 0.12276},{0.80915, 0.29295, 0.51454, 0.60836, 0.53379},{0.2681, 0.60211, 0.36709, 0.0059107, 0.89158}};
		RealMatrix gdist = MatrixUtils.createRealMatrix(gDistArray);
		OrderMDSResult test = OrderMDS.calc(gdist, 5);
		System.out.println(test);
		
		double[][] xArray = {{-1.1111, -0.12482, 0.38122, -0.054313, -8.5931e-24},{-0.52447, 0.77198, -0.30339, -0.10877, -8.9244e-24},{0.019043, -0.28177,
			 -0.14053, 0.32455, 3.0852e-23},{1.2137, 0.22419, 0.45906, -0.018552, -9.0484e-24},{0.40284, -0.58958, -0.39637, -0.14292, -4.2861e-24}};
		OrderMDSResult actual = new OrderMDSResult(MatrixUtils.createRealMatrix(xArray), 0.0);
		assertTrue(test.equals(actual));
	}

}
