package com.sciome.opera.calc.math;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class KnnClass {
	public static Integer calc(RealVector neighborsClass, RealVector neighborsDistance, int numClass, int K) {
		double[] freqTemp = new double[numClass];
		for(int i = 1; i <= numClass; i++) {
			int sum = 0;
			for(int j = 0; j < neighborsClass.getDimension(); j++) {
				if(neighborsClass.getEntry(j) == i)
					sum++;
			}
			freqTemp[i - 1] = sum;
		}
		RealVector freq = MatrixUtils.createRealVector(freqTemp);
		
		List<Integer> uniqueClass = new ArrayList<Integer>();
		for(int i = 0; i < freq.getDimension(); i++) {
			if(freq.getEntry(i) == freq.getMaxValue()) {
				uniqueClass.add(i);
			}
		}
		
		Integer classCalc = null;
		if(uniqueClass.size() == 1) {
			classCalc = freq.getMaxIndex() + 1;
		} else {
			RealVector meanDist = Utils.ones(numClass).mapMultiply(neighborsDistance.getMaxValue());
			for(int i = 0; i < uniqueClass.size(); i++) {
				SummaryStatistics stat = new SummaryStatistics();
				for(int j = 0; j < neighborsClass.getDimension(); j++) {
					if(uniqueClass.get(i) + 1 == neighborsClass.getEntry(j)) {
						stat.addValue(neighborsDistance.getEntry(j));
					}
				}
				meanDist.setEntry(uniqueClass.get(i), stat.getMean());
			}
			classCalc = meanDist.getMinIndex() + 1;
		}
		return classCalc;
	}
}