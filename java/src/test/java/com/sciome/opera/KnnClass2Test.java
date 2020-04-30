package com.sciome.opera;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

import com.sciome.opera.calc.math.KnnClass2;

public class KnnClass2Test {

	@Test
	public void test() {
		double[] tempClass = new double[] {0, 1, 2, 3, 4, 5};
		double[] tempDistance = new double[] {.1, .2, .3, .4};
		RealVector neighborsClass = MatrixUtils.createRealVector(tempClass);
		RealVector neighborsDistance = MatrixUtils.createRealVector(tempDistance);
		assertEquals(KnnClass2.calc(neighborsClass, neighborsDistance, 3, 2), new Integer(1));
	}
	
	@Test
	public void test2() {
		double[] tempClass = new double[] {5, 3, 4};
		double[] tempDistance = new double[] {0.47047083108307575, 0.6164381310399285, 0.7311002922308265};
		RealVector neighborsClass = MatrixUtils.createRealVector(tempClass);
		RealVector neighborsDistance = MatrixUtils.createRealVector(tempDistance);
		assertEquals(KnnClass2.calc(neighborsClass, neighborsDistance, 5, 3), new Integer(5));
	}

}
