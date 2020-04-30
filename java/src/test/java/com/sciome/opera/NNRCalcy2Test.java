package com.sciome.opera;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

import com.sciome.opera.calc.math.NNRCalcy2;
import com.sciome.opera.model.NNRResult;

public class NNRCalcy2Test {

	@Test
	public void testCalc() {
		double[] testY = {0, 1, 2, 3, 4};
		double[] testDistance = {.1, .2, .3, .4, .5};
		RealVector neighbors_y = MatrixUtils.createRealVector(testY);
		RealVector neighbors_distance = MatrixUtils.createRealVector(testDistance);
		Integer K = 5;
		NNRResult test = NNRCalcy2.calc(neighbors_y, neighbors_distance, K);
		
		double actualYc = 2;
		double actualYcWeighted = 1.3467;
		double[] actualWArray = {0.37956, 0.22774, 0.16267,0.12652, 0.10352};
		NNRResult actual = new NNRResult(actualYc, actualYcWeighted, actualWArray);
		
		assertEquals(test, actual);
	}

}
