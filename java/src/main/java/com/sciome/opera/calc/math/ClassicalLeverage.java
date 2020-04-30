package com.sciome.opera.calc.math;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;

import com.sciome.opera.enums.PretreatmentType;
import com.sciome.opera.model.ClassicalLeverageResult;

public class ClassicalLeverage
{
	public static ClassicalLeverageResult classicalLeverage(RealMatrix train_row, RealMatrix test_row, PretreatmentType method) {
		double[][] train_rowArray = new double[train_row.getRowDimension()][train_row.getColumnDimension() + 1];
		int k = train_row.getColumnDimension();
		for(int i = 0; i < train_row.getRowDimension(); i++) {
			for(int j = 0; j < k + 1; j++) {
				if(j == k) {
					train_rowArray[i][j] = 1;
				} else {
					train_rowArray[i][j] = train_row.getEntry(i, j);
				}
			}
		}
		train_row = MatrixUtils.createRealMatrix(train_rowArray);
		
		double[][] test_rowArray = new double[test_row.getRowDimension()][test_row.getColumnDimension() + 1];
		int l = test_row.getColumnDimension();
		for(int i = 0; i < test_row.getRowDimension(); i++) {
			for(int j = 0; j < l + 1; j++) {
				if(j == k) {
					test_rowArray[i][j] = 1;
				} else {
					test_rowArray[i][j] = test_row.getEntry(i, j);
				}
			}
		}
		test_row = MatrixUtils.createRealMatrix(test_rowArray);
		
		RealVector Ht_diag = calc_lev(train_row, test_row);
		double p = train_row.getColumnDimension();
		double n = train_row.getRowDimension();
		double threshold = 3 * (p / n);
		
		RealVector inorout = outputMatrix(Ht_diag, threshold);
		
		return new ClassicalLeverageResult(Ht_diag, inorout, (int)Utils.vectorSum(inorout));
	}
	
	private static RealVector calc_lev(RealMatrix train, RealMatrix test) {
		RealMatrix ht;
		try {
			ht = (test.multiply(MatrixUtils.inverse(train.transpose().multiply(train)))).multiply(test.transpose());
		} catch(SingularMatrixException e) {
			double[][] temp = new double[train.getRowDimension()][train.getRowDimension()];
			for(int i = 0; i < temp.length; i++) {
				for(int j = 0; j < temp[i].length; j++) {
					temp[i][j] = Double.POSITIVE_INFINITY;
				}
			}
			ht = MatrixUtils.createRealMatrix(temp);
		}
		
		return Utils.diag(ht);
	}
	
	private static RealVector outputMatrix(RealVector Ht_diag, double threshold) {
		RealVector inorout = Utils.zeros(Ht_diag.getDimension());
		for(int i = 0; i < Ht_diag.getDimension(); i++) {
			if(Ht_diag.getEntry(i) > threshold) {
				inorout.setEntry(i, 1.0);
			}
		}
		return inorout;
	}
}
