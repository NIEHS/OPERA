package com.sciome.opera.model;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class NNRResult {
	private double yc;
	private double yc_weighted;
	private RealMatrix w;
	
	public NNRResult(double yc, double yc_weighted, RealMatrix w) {
		this.yc = yc;
		this.yc_weighted = yc_weighted;
		this.w = w;
	}
	
	public NNRResult(double yc, double yc_weighted, double[] w) {
		this.yc = yc;
		this.yc_weighted = yc_weighted;
		this.w = MatrixUtils.createRowRealMatrix(w);
	}
	
	public double getYc() {
		return yc;
	}
	public void setYc(double yc) {
		this.yc = yc;
	}
	public double getYc_weighted() {
		return yc_weighted;
	}
	public void setYc_weighted(double yc_weighted) {
		this.yc_weighted = yc_weighted;
	}
	public RealMatrix getW() {
		return w;
	}
	public void setW(RealMatrix w) {
		this.w = w;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NNRResult other = (NNRResult) obj;
		double tolerance = .0001;
		if (w == null) {
			if (other.w != null)
				return false;
		} else {
			if(w.getRowDimension() != other.w.getRowDimension() || w.getColumnDimension() != other.w.getColumnDimension())
				return false;
			
			for(int i = 0; i < w.getRowDimension(); i++) {
				for(int j = 0; j < w.getColumnDimension(); j++) {
					if(w.getEntry(i, j) - other.w.getEntry(i, j) > tolerance) {
						return false;
					}
				}
			}
		}
		if (yc - other.yc > tolerance)
			return false;
		if (yc_weighted - other.yc_weighted > tolerance)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NNRResult [yc=" + yc + ", yc_weighted=" + yc_weighted + ", w=" + w + "]";
	}
}
