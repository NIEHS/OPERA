package com.sciome.opera.calc.math;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.stat.correlation.Covariance;

import com.sciome.opera.enums.DistanceType;
import com.sciome.opera.enums.PretreatmentType;

public class KnnCalcDist
{
	public static RealMatrix calculate(RealMatrix X, RealMatrix Xnew, DistanceType type, PretreatmentType pret) {
		double[][] matrix = new double[Xnew.getRowDimension()][X.getRowDimension()];
		RealMatrix D = MatrixUtils.createRealMatrix(matrix);
		RealMatrix inv_covX = null;
		if(type.equals(DistanceType.mahalanobis)) {
			Covariance cov = new Covariance(X);
			SingularValueDecomposition decomp = new SingularValueDecomposition(cov.getCovarianceMatrix());
			inv_covX = decomp.getSolver().getInverse();
		}

		for(int i = 0; i < Xnew.getRowDimension(); i++) {
			if(type.equals(DistanceType.euclidean)) {
				RealVector x_in = Xnew.getRowVector(i);
				//1 x X[0] vector 
				RealVector D_squares_x = Utils.ones(1, X.getRowDimension()).scalarMultiply(Utils.vectorSum(Utils.expVector(x_in, 2))).getRowVector(0);
				RealVector D_squares_w = Utils.columnSums(Utils.expMatrix(X.transpose(), 2));
				RealVector D_product = MatrixUtils.createRowRealMatrix(x_in.toArray()).multiply(X.transpose()).scalarMultiply(-2.0).getRowVector(0);
				RealVector vec = D_squares_x.add(D_squares_w).add(D_product);
				double[] temp = new double[vec.getDimension()];
				for(int j = 0; j < vec.getDimension(); j++) {
					Complex num = new Complex(vec.getEntry(j));
					temp[j] = num.sqrt().getReal();
				}
				D.setRowVector(i, MatrixUtils.createRealVector(temp));
			} else {
				for(int j = 0; j < X.getRowDimension(); j++) {
					RealVector x = Xnew.getRowVector(i);
					RealVector y = Xnew.getRowVector(j);
					if(type.equals(DistanceType.mahalanobis)) {
						RealMatrix vec = MatrixUtils.createRowRealMatrix(x.subtract(y).toArray());
						D.setEntry(i, j, Math.sqrt(vec.multiply(inv_covX).multiply(vec.transpose()).getEntry(0, 0)));
					} else if(type.equals(DistanceType.cityblock)) {
						D.setEntry(i, j, Utils.vectorSum(Utils.absVector(x.subtract(y))));
					} else if(type.equals(DistanceType.minkowski)) {
						double p = 2;
						D.setEntry(i, j, Math.pow(Utils.vectorSum(Utils.expVector(Utils.absVector(x.subtract(y)), p)), (1.0/p)));
					} else if(pret.equals(PretreatmentType.fp)) {
						double a = 0;
						double bc = 0;
						double d = 0;
						double p = x.getDimension();
						RealVector s = x.add(y);
						for(int k = 0; k < s.getDimension(); k++) {
							if(s.getEntry(k) == 0.0) {
								d++;
							} else if(s.getEntry(k) == 1.0) {
								bc++;
							} else if(s.getEntry(k) == 2.0) {
								a++;
							}
						}
						if(type.equals(DistanceType.sm)) {
							D.setEntry(i, j, 1-((a+d)/p));
						} else if(type.equals(DistanceType.rt)) {
							D.setEntry(i, j, 1-((a+d)/(p+bc)));
						} else if(type.equals(DistanceType.jt)) {
							D.setEntry(i, j, 1-(a/(a+bc)));
						} else if(type.equals(DistanceType.gle)) {
							D.setEntry(i, j, 1-(2*a/(2*a+bc)));
						} else if(type.equals(DistanceType.ct4)) {
							D.setEntry(i, j, 1-((Math.log(1+a)/Math.log(2))/(Math.log(1+a+bc)/Math.log(2))));
						} else if(type.equals(DistanceType.ac)) {
							D.setEntry(i, j, 1-((2/Math.PI)*Math.asin(Math.sqrt((a+d)/p))));
						}
					}
				}
			}
		}
		return D;
	}
}
