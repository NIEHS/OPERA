package com.sciome.opera;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

import com.sciome.opera.calc.math.KnnPred;
import com.sciome.opera.enums.DistanceType;
import com.sciome.opera.enums.PretreatmentType;
import com.sciome.opera.model.Prediction;

public class KnnPredTest {

	@Test
	public void test() {
		double[][] XtestArray = {{.32055, .56566, .75160}, {.37157, .38932, .35758}, {.43912, .58002, .41434}};
		double[][] XArray = {{.56073, .97098, .19255}, {.47618, .90852, .46952}, {.64974, .11409, .49138}};
		double[] classArray = {3, 4, 5};
		RealMatrix Xtest = MatrixUtils.createRealMatrix(XtestArray);
		RealMatrix X = MatrixUtils.createRealMatrix(XArray);
		RealVector clazz = MatrixUtils.createRealVector(classArray);
		Integer K = 3;
		Prediction test = KnnPred.predict(Xtest, X, clazz, K, DistanceType.euclidean, PretreatmentType.cent);
		
		double[][] neighborsArray = {{2,3,1},{3,2,1},{2,1,3}};
		double[][] wArray = {{0.40861, 0.31911, 0.27227},{0.40615, 0.31839, 0.27545},{0.41218, 0.30788, 0.27994}};
		double[][] DArray = {{0.73109, 0.47047, 0.61644},{0.63351, 0.54134, 0.41356},{0.46564, 0.33516, 0.5171}};
		double[][] dcArray = {{0.47047, 0.61644, 0.73109},{0.41356, 0.54134, 0.63351},{0.33516, 0.46564, 0.5171}};
		double[] classPredArray = {4, 5, 4};
		double[] classPredW = {4, 4, 4};
		Prediction actual = new Prediction(neighborsArray, classPredArray, classPredW, wArray, DArray, dcArray);
		
		assertEquals(test, actual);
	}

}
