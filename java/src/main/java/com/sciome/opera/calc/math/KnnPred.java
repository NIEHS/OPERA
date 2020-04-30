package com.sciome.opera.calc.math;

import java.util.Arrays;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import com.sciome.opera.enums.DistanceType;
import com.sciome.opera.enums.PretreatmentType;
import com.sciome.opera.model.NNRResult;
import com.sciome.opera.model.Prediction;
import com.sciome.opera.model.PretreatmentResult;

public class KnnPred
{
	public static Prediction predict(RealMatrix Xtest, RealMatrix X, RealVector clazz, int K, DistanceType distType, PretreatmentType pret) {
		if(clazz.getDimension() != X.getRowDimension()) {
			throw new IllegalArgumentException("The class input should be for the training set");
		}
		int n = Xtest.getRowDimension();
		int p = Xtest.getColumnDimension();
		PretreatmentResult pretreatment = DataPretreatment.calculate(X, pret);
		RealMatrix X_scal_train = pretreatment.getMatrix();
		RealMatrix X_scal = TestPretreatment.test(Xtest, pretreatment);
		RealMatrix D = KnnCalcDist.calculate(X_scal_train, X_scal, distType, pret);
		RealMatrix neighbors = Utils.zeros(n, K);
		RealMatrix dc = Utils.zeros(n, K);
		RealMatrix classCalc = Utils.zeros(n, 1);
		RealMatrix classCalcWeighted = Utils.zeros(n, 1);
		RealMatrix w = Utils.zeros(n, K);
		for(int i = 0; i < n; i++) {
			RealVector D_in = D.getRowVector(i);
			RealVector n_tmp = Utils.sortVectorIndices(D_in, true);
			double[] temp = D_in.toArray();
			Arrays.sort(temp);
			RealVector d_tmp = MatrixUtils.createRealVector(temp);
			neighbors.setRowVector(i, n_tmp.getSubVector(0, K));
			RealVector d_neighbors = d_tmp.getSubVector(0, K);
			
			double[] classTemp = new double[K];
			for(int j = 0; j < neighbors.getRowVector(i).getDimension(); j++) {
				classTemp[j] = clazz.getEntry((int)neighbors.getRowVector(i).getEntry(j) - 1);
			}
			RealVector classVector = MatrixUtils.createRealVector(classTemp);
			classCalc.setEntry(i, 0, KnnClass.calc(classVector, d_neighbors, (int)clazz.getMaxValue(), K));
			NNRResult result = NNRCalcy.calc(classVector, d_neighbors, K);
			classCalcWeighted.setEntry(i, 0, Math.round(result.getYc_weighted()));
			w.setRowVector(i, result.getW().getRowVector(0));
			dc.setRowVector(i, d_neighbors);
		}
		Prediction pred = new Prediction();
		pred.setNeighbors(neighbors);
		pred.setClassPred(classCalc.transpose());
		pred.setClassPredW(classCalcWeighted.transpose());
		pred.setW(w);
		pred.setD(D);
		pred.setDc(dc);
		return pred;
	}
}
