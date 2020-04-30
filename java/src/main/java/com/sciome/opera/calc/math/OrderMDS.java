package com.sciome.opera.calc.math;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import com.sciome.opera.model.OrderMDSResult;

public class OrderMDS
{
	public static OrderMDSResult calc(RealMatrix gdist, Integer L) {
		double epsilon = 1e-2;
		if(gdist == null && L == null) {
			Integer N = 30;
			L = 2;
			RealMatrix x_true = Utils.createRandomMatrix(N, L, 222);
		} else if(gdist == null || L == null) {
			throw new IllegalArgumentException("Too few arguments");
		}

		int N = gdist.getRowDimension();
		if(N != gdist.getColumnDimension()) {
			throw new IllegalArgumentException("Excepted gdist to be square");
		}
		
		gdist = gdist.add(gdist.transpose()).scalarMultiply(.5);
		gdist = gdist.subtract(MatrixUtils.createRealDiagonalMatrix(Utils.diag(gdist).toArray()));
		RealMatrix x = Mds.calc(gdist, L).getPositions().transpose();
		double[][] temp = new double[N * gdist.getColumnDimension()][3];
		for(int i = 1; i < gdist.getRowDimension(); i++) {
			for(int j = 0; j < i; j++) {
				temp[i - 1][0] = gdist.getEntry(i, j);
				temp[i - 1][1] = i;
				temp[i - 1][2] = j;
			}
		}
		gdist = MatrixUtils.createRealMatrix(temp);
		int M = gdist.getRowDimension();
		
		int iter_count = 0;
		double len_dS_dx = epsilon + 1;
		int len_update = 1;

		RealMatrix dS_dx_prev = null;
		RealVector S_prev = null;
		double S = 0;
		while(len_dS_dx > epsilon) {
			x = x.subtract(Utils.ones(N, 1).multiply(MatrixUtils.createRowRealMatrix(Utils.columnSums(x).mapMultiply(1.0 / x.getRowDimension()).toArray())));
			x = x.scalarMultiply(1.0 / (Utils.matrixSum(Utils.expMatrix(x, 2)) / N));
			int[] row1 = copyFromDoubleArray(gdist.getColumnVector(1).toArray());
			int[] row2 = copyFromDoubleArray(gdist.getColumnVector(2).toArray());
			int[] columns = new int[x.getColumnDimension()];
			for(int i = 0; i < x.getColumnDimension(); i++) {
				columns[i] = i;
			}
			RealVector dist_x = Utils.squareRootVector(Utils.rowSums(Utils.expMatrix(x.getSubMatrix(row1, columns).subtract(x.getSubMatrix(row2, columns)), 2)));
			RealVector cavg_dist_x = MatrixUtils.createRealVector(Utils.filter(new double[]{1.0}, new double[]{1.0, -1.0}, dist_x.toArray()));
			double[] divisor = new double[M];
			for(int i = 0; i < M; i++) {
				divisor[i] = i / (double)M;
			}
			cavg_dist_x.ebeMultiply(MatrixUtils.createRealVector(divisor));
			RealVector s = Utils.sortVectorIndices(cavg_dist_x, true);
			List<Integer> iList = new ArrayList<Integer>();
			for(int i = 0; i < s.getDimension() - 1; i++) {
				if(s.getEntry(i) < s.getEntry(i + 1))
					iList.add(i);
			}
			int j = M + 2;
			while(iList.size() > 0) {
				double min = Double.MAX_VALUE;
				for(int i = 0; i < iList.size(); i++) {
					if(s.getEntry(iList.get(i) + 1) < min) {
						min = s.getEntry(iList.get(i) + 1);
					}
				}
				j = (int)Math.min(j, min);
				iList = new ArrayList<Integer>();
				for(int i = 0; i < s.getDimension() - 1; i++) {
					if(s.getEntry(i + 1) < s.getEntry(i))
						iList.add(i);
				}
			}
			
			RealVector hat_dist_x = dist_x.copy();
			
			if(j == 1) {
				for(int i = 0; i < s.getEntry(0); i++) {
					hat_dist_x.setEntry(i, cavg_dist_x.getEntry((int)s.getEntry(0)) / s.getEntry(0));
				}
			} else if(j > 2) {
				for(int i = 0; i < j - 2; i++) {
					s.setEntry(i, 0);
				}
			}
			
			if(s.getDimension() > 1) {
				RealMatrix s1 = Utils.zeros(s.getDimension() - 1, 2);
				s1.setColumnVector(0, s.getSubVector(0, s.getDimension() - 1));
				s1.setColumnVector(1, s.getSubVector(1, s.getDimension() - 1));
				for(int i = 0; i < s1.getRowDimension(); i++) {
					if(s1.getEntry(i, 0) == s1.getEntry(i, 1)) {
						s1.setEntry(i, 0, 0);
						s1.setEntry(i, 1, 0);
					}
					double t = (cavg_dist_x.getEntry((int)s1.getEntry(i, 1)) - cavg_dist_x.getEntry((int)s1.getEntry(i, 0))) / (s1.getEntry(i, 1) - s1.getEntry(i, 0));
					for(int k = (int)s1.getEntry(i, 0) + 1; k < (int)s1.getEntry(i, 1); k++) {
						hat_dist_x.setEntry(k, t);
					}
				}
			}
			
			double sstar = Math.pow(Utils.vectorSum(dist_x.subtract(hat_dist_x)), 2);
			double tstar = Utils.vectorSum(Utils.expVector(dist_x, 2));
			S = Math.sqrt(sstar / tstar);

			RealMatrix dS_dx = Utils.zeros(N, L);
			
			if(sstar <= Math.pow(epsilon, 2)) {
				len_dS_dx = 0;
			} else {
				for(int i = 0; i < dist_x.getDimension(); i++) {
					dist_x.setEntry(i, 1.0 / Math.max(dist_x.getEntry(i), Math.pow(epsilon, 2)));
				}
				hat_dist_x = hat_dist_x.ebeMultiply(dist_x);
				hat_dist_x = Utils.ones(hat_dist_x.getDimension()).subtract(hat_dist_x).mapMultiply(1.0 / sstar).mapSubtract(1.0/tstar);
				for(int m = 0; m < M; m++) {
					int r = (int)gdist.getEntry(m, 1);
					int s2 = (int)gdist.getEntry(m, 2);
					RealVector v = x.getRowVector(r).subtract(x.getRowVector(s2)).mapMultiply(hat_dist_x.getEntry(m));
					dS_dx.setRowVector(r, dS_dx.getRowVector(r).add(v));
					dS_dx.setRowVector(s2, dS_dx.getRowVector(s2).subtract(v));
				}
				dS_dx = dS_dx.scalarMultiply(S);
				len_dS_dx = Math.sqrt(Utils.matrixSum(Utils.expMatrix(dS_dx, 2)));
			}
			
			if(len_dS_dx > epsilon) {
				dS_dx = dS_dx.scalarMultiply(1.0 / len_dS_dx);
				
				if(iter_count < 1) {
					dS_dx_prev = dS_dx.copy();
					S_prev = Utils.ones(5).mapMultiply(S);
					len_update = (int) Math.min(1, S / (2.6 * len_dS_dx));
				}
				
				double f1 = Math.pow(Utils.matrixSum(Utils.ebeMultiply(dS_dx, dS_dx_prev)), 3);
				f1 = Math.pow(4, f1);
				double f2 = 1.3 / (1 + Math.min(1, Math.pow(S / S_prev.getEntry(4), 5)));
				double f3 = Math.min(1, S / S_prev.getEntry(0));
				len_update *= f1*f2*f3;
				x = x.subtract(dS_dx.scalarMultiply(len_update));
				dS_dx_prev = dS_dx.copy();
				RealVector S_temp = Utils.zeros(1);
				S_temp.addToEntry(0, S);
				S_prev = S_temp.append(S_prev.getSubVector(0, 4));
			}
			
			iter_count++;
		}
		return new OrderMDSResult(x, S);
	}
	
	private static int[] copyFromDoubleArray(double[] source) {
	    int[] dest = new int[source.length];
	    for(int i=0; i<source.length; i++) {
	        dest[i] = (int)source[i];
	    }
	    return dest;
	}
}
