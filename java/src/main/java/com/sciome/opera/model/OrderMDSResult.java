package com.sciome.opera.model;

import org.apache.commons.math3.linear.RealMatrix;

import com.sciome.opera.calc.math.Utils;

public class OrderMDSResult {
	private RealMatrix x;
	private Double S;
	
	public OrderMDSResult(RealMatrix x, Double S) {
		this.x = x;
		this.S = S; 
	}
	
	public RealMatrix getX() {
		return x;
	}
	public void setX(RealMatrix x) {
		this.x = x;
	}
	public Double getS() {
		return S;
	}
	public void setS(Double s) {
		S = s;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderMDSResult other = (OrderMDSResult) obj;
		double tolerance = .0001;
		if (S == null) {
			if (other.S != null)
				return false;
		} else if (!S.equals(other.S))
			return false;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!Utils.compareMatrix(x, other.x, tolerance))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrderMDSResult [x=" + x + ", S=" + S + "]";
	}
}
