package com.sciome.opera.calc.math;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import com.sciome.opera.model.NNRResult;

public class NNRCalcy2 {
	public static NNRResult calc(RealVector neighbors_y, RealVector neighbors_distance, Integer K) {
		double sum = 0;
		for(int i = 0; i < neighbors_y.getDimension(); i++) {
			sum += neighbors_y.getEntry(i);
		}
		double yc = sum / neighbors_y.getDimension();
		
		RealMatrix w = Utils.zeros(1, K);
		

		
		boolean hasZero = false;
		for(int i = 0; i < neighbors_distance.getDimension(); i++) {
			if(neighbors_distance.getEntry(i) < 1e-6) {
				hasZero = true;
				break;
			}
		}
		
		double f = .05;
		if(hasZero)
			f = 1e-10;
		
		int nan = 0;
		for(int i = 0; i < neighbors_distance.getDimension(); i++) {
			if(Double.isNaN(neighbors_distance.getEntry(i)))
				nan++;
		}
		
		if(nan < K - 1) {
			for(int i = 0; i < neighbors_distance.getDimension(); i++) {
				if(!Double.isNaN(neighbors_distance.getEntry(i))) {
					w.setEntry(0, i, 1.0 / (f + neighbors_distance.getEntry(i)));
				}
			}
			w = w.scalarMultiply(1.0 / Utils.matrixSum(w));
		} else {
			w = Utils.ones(1, K).scalarMultiply(1.0 / K);
		}
		
		RealMatrix yc_weighted_matrix = MatrixUtils.createRowRealMatrix(neighbors_y.toArray()).multiply(w.transpose());
		double yc_weighted;
		if(yc_weighted_matrix.getColumnDimension() == 1 && yc_weighted_matrix.getRowDimension() == 1) {
			yc_weighted = yc_weighted_matrix.getEntry(0, 0);
		} else {
			throw new IllegalArgumentException("Input dimensions don't match");
		}
		
		return new NNRResult(yc, yc_weighted, w);
		
	}
}
