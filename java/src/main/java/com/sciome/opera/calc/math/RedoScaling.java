package com.sciome.opera.calc.math;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import com.sciome.opera.enums.PretreatmentType;
import com.sciome.opera.model.IPretreatment;

public class RedoScaling {
	public static RealMatrix scale(RealMatrix X_scal, IPretreatment param) {
		RealVector a = param.getMean();
		RealVector s = param.getSd();
		RealVector m = param.getMin();
		RealVector M = param.getMax();
		PretreatmentType pret = param.getPret_type();
		
		RealMatrix X = Utils.zeros(X_scal.getRowDimension(), X_scal.getColumnDimension());
		
		if(pret.equals(PretreatmentType.cent)) {
			for(int j = 0; j < X_scal.getColumnDimension(); j++) {
				X.setColumnVector(j, X_scal.getColumnVector(j).mapAdd(a.getEntry(j)));
			}
		} else if(pret.equals(PretreatmentType.scal)) {
			for(int j = 0; j < X_scal.getColumnDimension(); j++) {
				X.setColumnVector(j, X_scal.getColumnVector(j).mapMultiply(s.getEntry(j)));
			}
		} else if(pret.equals(PretreatmentType.auto)) {
			for(int j = 0; j < X_scal.getColumnDimension(); j++) {
				X.setColumnVector(j, X_scal.getColumnVector(j).mapMultiply(s.getEntry(j)).mapAdd(a.getEntry(j)));
			}
		} else if(pret.equals(PretreatmentType.rang)) {
			for(int j = 0; j < X_scal.getColumnDimension(); j++) {
				X.setColumnVector(j, X_scal.getColumnVector(j).mapMultiply(M.getEntry(j) - m.getEntry(j)).mapAdd(m.getEntry(j)));
			}
		} else {
			X = X_scal;
		}
		
		return X;
	}
}
