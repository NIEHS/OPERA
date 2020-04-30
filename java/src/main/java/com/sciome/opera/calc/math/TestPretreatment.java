package com.sciome.opera.calc.math;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import com.sciome.opera.enums.PretreatmentType;
import com.sciome.opera.model.IPretreatment;

public class TestPretreatment
{
	public static RealMatrix test(RealMatrix X, IPretreatment param) {
		RealMatrix X_scal = null;
		
		RealVector a = param.getMean();
		RealVector s = param.getSd();
		RealVector m = param.getMin();
		RealVector M = param.getMax();
		PretreatmentType pret = param.getPret_type();
		
		if(pret.equals(PretreatmentType.cent)) {
			X_scal = X.subtract(Utils.repmat(MatrixUtils.createRowRealMatrix(a.toArray()), X.getRowDimension(), 1));
		} else if(pret.equals(PretreatmentType.scal)) {
			RealMatrix smat = Utils.repmat(MatrixUtils.createRowRealMatrix(s.toArray()), X.getRowDimension(), 1);
			X_scal = Utils.zeros(X.getRowDimension(), X.getColumnDimension());
			for(int i = 0; i < s.getDimension(); i++) {
				if(s.getEntry(i) > 0) {
					X_scal.setColumnVector(i, X.getColumnVector(i).ebeDivide(smat.getColumnVector(i)));
				}
			}
		} else if(pret.equals(PretreatmentType.auto)) {
			RealMatrix amat = Utils.repmat(MatrixUtils.createRowRealMatrix(a.toArray()), X.getRowDimension(), 1);
			RealMatrix smat = Utils.repmat(MatrixUtils.createRowRealMatrix(s.toArray()), X.getRowDimension(), 1);
			X_scal = Utils.zeros(X.getRowDimension(), X.getColumnDimension());
			for(int i = 0; i < s.getDimension(); i++) {
				if(s.getEntry(i) > 0) {
					X_scal.setColumnVector(i, X.getColumnVector(i).subtract(amat.getColumnVector(i)).ebeDivide(smat.getColumnVector(i)));
				}
			}
		} else if(pret.equals(PretreatmentType.rang)) {
			RealMatrix mmat = Utils.repmat(MatrixUtils.createRowRealMatrix(m.toArray()), X.getRowDimension(), 1);
			RealMatrix Mmat = Utils.repmat(MatrixUtils.createRowRealMatrix(M.toArray()), X.getRowDimension(), 1);
			X_scal = Utils.zeros(X.getRowDimension(), X.getColumnDimension());
			RealVector temp = M.subtract(m);
			for(int i = 0; i < temp.getDimension(); i++) {
				if(temp.getEntry(i) > 0) {
					X_scal.setColumnVector(i, X.getColumnVector(i).subtract(mmat.getColumnVector(i)).ebeDivide(Mmat.getColumnVector(i).subtract(mmat.getColumnVector(i))));
				}
			}
		}
		
		return X_scal;
	}
}
