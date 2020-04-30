package com.sciome.opera.model;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import com.sciome.opera.calc.math.Utils;

public class Prediction {
	RealMatrix neighbors;
	RealMatrix classPred;
	RealMatrix classPredW;
	RealMatrix w;
	RealMatrix D;
	RealMatrix dc;
	
	public Prediction(double[][] neighborsArray, double[] classPredArray, double[] classPredWArray, double[][] wArray, double[][] DArray, double[][] dcArray) {
		this.neighbors = MatrixUtils.createRealMatrix(neighborsArray);
		this.classPred = MatrixUtils.createRowRealMatrix(classPredArray);
		this.classPredW = MatrixUtils.createRowRealMatrix(classPredWArray);
		this.w = MatrixUtils.createRealMatrix(wArray);
		this.D = MatrixUtils.createRealMatrix(DArray);
		this.dc = MatrixUtils.createRealMatrix(dcArray);
	}
	
	public Prediction() {
		
	}
	
	public RealMatrix getNeighbors() {
		return neighbors;
	}
	public void setNeighbors(RealMatrix neighbors) {
		this.neighbors = neighbors;
	}
	public RealMatrix getClassPred() {
		return classPred;
	}
	public void setClassPred(RealMatrix classPred) {
		this.classPred = classPred;
	}
	public RealMatrix getClassPredW() {
		return classPredW;
	}
	public void setClassPredW(RealMatrix classPredW) {
		this.classPredW = classPredW;
	}
	public RealMatrix getW() {
		return w;
	}
	public void setW(RealMatrix w) {
		this.w = w;
	}
	public RealMatrix getD() {
		return D;
	}
	public void setD(RealMatrix d) {
		D = d;
	}
	public RealMatrix getDc() {
		return dc;
	}
	public void setDc(RealMatrix dc) {
		this.dc = dc;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Prediction other = (Prediction) obj;
		double tolerance = .0001;
		if (D == null) {
			if (other.D != null)
				return false;
		} else if (!Utils.compareMatrix(D, other.D, tolerance))
			return false;
		if (classPred == null) {
			if (other.classPred != null)
				return false;
		} else if (!Utils.compareMatrix(classPred, other.classPred, tolerance))
			return false;
		if (classPredW == null) {
			if (other.classPredW != null)
				return false;
		} else if (!Utils.compareMatrix(classPredW, other.classPredW, tolerance))
			return false;
		if (dc == null) {
			if (other.dc != null)
				return false;
		} else if (!Utils.compareMatrix(dc, other.dc, tolerance))
			return false;
		if (neighbors == null) {
			if (other.neighbors != null)
				return false;
		} else if (!Utils.compareMatrix(neighbors, neighbors, tolerance))
			return false;
		if (w == null) {
			if (other.w != null)
				return false;
		} else if (!Utils.compareMatrix(w, other.w, tolerance))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Prediction [neighbors=" + neighbors + ", classPred=" + classPred + ", classPredW=" + classPredW + ", w="
				+ w + ", D=" + D + ", dc=" + dc + "]";
	}
}
