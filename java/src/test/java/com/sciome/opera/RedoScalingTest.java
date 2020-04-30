package com.sciome.opera;

import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

import com.sciome.opera.calc.math.RedoScaling;
import com.sciome.opera.calc.math.Utils;
import com.sciome.opera.enums.PretreatmentType;
import com.sciome.opera.model.PretreatmentResult;

public class RedoScalingTest {

	@Test
	public void testCent() {
		double[][] XscalArray =  {{0.62731, 0.20239, 0.29427},{0.5315, 0.5781, 0.064238},{0.52355, 0.96232, 0.061263}};
		double[] mean =  {0.24589, 0.13726, 0.28941};
		double[] min = {0.011992, 0.82017, 0.43386};
		double[] max = {0.27098, 0.92946, 0.66704};
		double[] std = {0.29756, 0.77522, 0.28136};
		RealMatrix Xscal = MatrixUtils.createRealMatrix(XscalArray);
		RealVector meanVec = MatrixUtils.createRealVector(mean);
		RealVector minVec = MatrixUtils.createRealVector(min);
		RealVector maxVec = MatrixUtils.createRealVector(max);
		RealVector stdVec = MatrixUtils.createRealVector(std);
		PretreatmentResult pretResult = new PretreatmentResult();
		pretResult.setMean(meanVec);
		pretResult.setMax(maxVec);
		pretResult.setMin(minVec);
		pretResult.setStd(stdVec);
		pretResult.setPretType(PretreatmentType.cent);
		
		RealMatrix test = RedoScaling.scale(Xscal, pretResult);
		
		double[][] actualArray = {{0.8732, 0.33965, 0.58368},{0.77739, 0.71536, 0.35365},{0.76944, 1.0996, 0.35067}};
		RealMatrix actual = MatrixUtils.createRealMatrix(actualArray);
		
		assertTrue(Utils.compareMatrix(test, actual, .001));
	}

	@Test
	public void testScal() {
		double[][] XscalArray =  {{0.62731, 0.20239, 0.29427},{0.5315, 0.5781, 0.064238},{0.52355, 0.96232, 0.061263}};
		double[] mean =  {0.24589, 0.13726, 0.28941};
		double[] min = {0.011992, 0.82017, 0.43386};
		double[] max = {0.27098, 0.92946, 0.66704};
		double[] std = {0.29756, 0.77522, 0.28136};
		RealMatrix Xscal = MatrixUtils.createRealMatrix(XscalArray);
		RealVector meanVec = MatrixUtils.createRealVector(mean);
		RealVector minVec = MatrixUtils.createRealVector(min);
		RealVector maxVec = MatrixUtils.createRealVector(max);
		RealVector stdVec = MatrixUtils.createRealVector(std);
		PretreatmentResult pretResult = new PretreatmentResult();
		pretResult.setMean(meanVec);
		pretResult.setMax(maxVec);
		pretResult.setMin(minVec);
		pretResult.setStd(stdVec);
		pretResult.setPretType(PretreatmentType.scal);
		
		RealMatrix test = RedoScaling.scale(Xscal, pretResult);
		
		double[][] actualArray = {{0.18666, 0.1569, 0.082796},{0.15815, 0.44815, 0.018074},{0.15579, 0.74601, 0.017237}};
		RealMatrix actual = MatrixUtils.createRealMatrix(actualArray);
		
		assertTrue(Utils.compareMatrix(test, actual, .001));
	}
	
	@Test
	public void testAuto() {
		double[][] XscalArray =  {{0.62731, 0.20239, 0.29427},{0.5315, 0.5781, 0.064238},{0.52355, 0.96232, 0.061263}};
		double[] mean =  {0.24589, 0.13726, 0.28941};
		double[] min = {0.011992, 0.82017, 0.43386};
		double[] max = {0.27098, 0.92946, 0.66704};
		double[] std = {0.29756, 0.77522, 0.28136};
		RealMatrix Xscal = MatrixUtils.createRealMatrix(XscalArray);
		RealVector meanVec = MatrixUtils.createRealVector(mean);
		RealVector minVec = MatrixUtils.createRealVector(min);
		RealVector maxVec = MatrixUtils.createRealVector(max);
		RealVector stdVec = MatrixUtils.createRealVector(std);
		PretreatmentResult pretResult = new PretreatmentResult();
		pretResult.setMean(meanVec);
		pretResult.setMax(maxVec);
		pretResult.setMin(minVec);
		pretResult.setStd(stdVec);
		pretResult.setPretType(PretreatmentType.auto);
		
		RealMatrix test = RedoScaling.scale(Xscal, pretResult);
		
		double[][] actualArray = {{0.43255, 0.29416, 0.37221},{0.40404, 0.58541, 0.30748},{0.40168, 0.88327, 0.30665}};
		RealMatrix actual = MatrixUtils.createRealMatrix(actualArray);
		
		assertTrue(Utils.compareMatrix(test, actual, .001));
	}
	
	@Test
	public void testRang() {
		double[][] XscalArray =  {{0.62731, 0.20239, 0.29427},{0.5315, 0.5781, 0.064238},{0.52355, 0.96232, 0.061263}};
		double[] mean =  {0.24589, 0.13726, 0.28941};
		double[] min = {0.011992, 0.82017, 0.43386};
		double[] max = {0.27098, 0.92946, 0.66704};
		double[] std = {0.29756, 0.77522, 0.28136};
		RealMatrix Xscal = MatrixUtils.createRealMatrix(XscalArray);
		RealVector meanVec = MatrixUtils.createRealVector(mean);
		RealVector minVec = MatrixUtils.createRealVector(min);
		RealVector maxVec = MatrixUtils.createRealVector(max);
		RealVector stdVec = MatrixUtils.createRealVector(std);
		PretreatmentResult pretResult = new PretreatmentResult();
		pretResult.setMean(meanVec);
		pretResult.setMax(maxVec);
		pretResult.setMin(minVec);
		pretResult.setStd(stdVec);
		pretResult.setPretType(PretreatmentType.rang);
		
		RealMatrix test = RedoScaling.scale(Xscal, pretResult);
		
		double[][] actualArray = {{0.17446, 0.84229, 0.50248},{0.14964, 0.88335, 0.44884},{0.14759, 0.92534, 0.44815}};
		RealMatrix actual = MatrixUtils.createRealMatrix(actualArray);
		
		assertTrue(Utils.compareMatrix(test, actual, .001));
	}
	
}
