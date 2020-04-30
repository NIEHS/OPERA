package com.sciome.opera.calc.math;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;

import com.sciome.opera.model.MdsResult;

public class Mds
{
	public static void main(String[] args) {
		Mds.calc(null, null);
	}
	
	/**
	 * Compute the multidimensional scaling that produces the best positions
	 * @param d two dimensional (symmetric) distance matrix
	 * @param order
	 */
	public static MdsResult calc(RealMatrix d, Integer order) {
		if(d == null) {
			double[][] temp = {{1,2,1,2},{1,1,3,4}};
			RealMatrix x = MatrixUtils.createRealMatrix(temp);
			int n = x.getColumnDimension();
			x.setRowVector(0, x.getRowVector(0).mapSubtract(Utils.vectorSum(x.getRowVector(0)) / n));
			x.setRowVector(1, x.getRowVector(1).mapSubtract(Utils.vectorSum(x.getRowVector(1)) / n));
			d = Utils.zeros(n, n);
			for(int i = 0; i < n; i++) {
				for(int j = 0; j < n; j++) {
					d.setEntry(i, j, Math.sqrt(Utils.vectorSum(Utils.expVector(x.getColumnVector(i).subtract(x.getColumnVector(j)), 2))));
				}
			}
		}
		if(order == null) {
			order = 2;
		}
		
		int n = d.getRowDimension();
		RealMatrix A = Utils.expMatrix(d, 2).scalarMultiply(-.5);
		RealMatrix ardot = Utils.ones(n, 1).multiply(MatrixUtils.createRowRealMatrix(Utils.columnSums(A).toArray())).scalarMultiply(1.0 / n);
		RealMatrix asdot = MatrixUtils.createColumnRealMatrix(Utils.columnSums(A.transpose()).toArray()).scalarMultiply(1.0 / n).multiply(Utils.ones(1, n));
		RealMatrix adotdot = Utils.ones(n, n).scalarMultiply(1.0 / n / n * Utils.vectorSum(Utils.columnSums(A)));
		RealMatrix B = A.subtract(ardot).subtract(asdot).add(adotdot);
		SingularValueDecomposition svd = new SingularValueDecomposition(B);
		RealMatrix S = svd.getS();
		RealMatrix V = svd.getV();
		
		RealVector ev = Utils.diag(S);
		
		RealMatrix positions = V.multiply(Utils.squareRootMatrix(S)).transpose();
		
		int length = Math.min(10, positions.getRowDimension());
		double[] error = new double[length];
		for(int o = 0; o < length; o++) {
			error[o] = 0.0;
			for(int i = 0; i < n; i++) {
				for(int j = 0; j < n; j++) {
					Double this_d = Math.sqrt(Utils.vectorSum(Utils.expVector(positions.getColumnVector(i).getSubVector(0, o).subtract(positions.getColumnVector(j).getSubVector(0, o)), 2)));
					error[o] =  error[o] + Math.pow(this_d - d.getEntry(i, j), 2);
				}
			}
		}
		return new MdsResult(positions, MatrixUtils.createRealVector(error), ev);
	}
}
