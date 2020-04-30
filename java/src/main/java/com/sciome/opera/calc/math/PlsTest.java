package com.sciome.opera.calc.math;

import org.apache.commons.math3.linear.RealMatrix;

import com.sciome.opera.model.training.model.RT.RTModel;

public class PlsTest {
	public static RealMatrix pls(RealMatrix x, RTModel model) {
		RealMatrix W = Utils.matrixFromList(model.getW());
		RealMatrix Q = Utils.rowRealMatrixFromList(model.getQ());
		RealMatrix P = Utils.matrixFromList(model.getP());
		int nF = Utils.matrixFromList(model.getT()).getColumnDimension();
		RealMatrix xscal = TestPretreatment.test(x, model.getSet().getPx());
		
		RealMatrix yscal_c = Utils.zeros(xscal.getRowDimension(), 1);
		for(int k = 0; k < nF; k++) {
			RealMatrix Tnew = xscal.multiply(W.getColumnMatrix(k)).scalarMultiply(1 / (W.getColumnMatrix(k).transpose().multiply(W.getColumnMatrix(k)).getEntry(0, 0)));
			yscal_c = yscal_c.add(Tnew.multiply(Q.getColumnMatrix(k).transpose()));
			xscal = xscal.subtract(Tnew.multiply(P.getColumnMatrix(k).transpose()));
		}
		return RedoScaling.scale(yscal_c, model.getSet().getPy());
	}
}
