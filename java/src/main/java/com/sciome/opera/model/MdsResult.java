package com.sciome.opera.model;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import com.sciome.opera.calc.math.Utils;

public class MdsResult {
	private RealMatrix positions;
	private RealVector error;
	private RealVector ev;
	
	public MdsResult(RealMatrix positions, RealVector error, RealVector ev) {
		this.positions = positions;
		this.error = error;
		this.ev = ev;
	}

	public RealMatrix getPositions() {
		return positions;
	}

	public void setPositions(RealMatrix positions) {
		this.positions = positions;
	}

	public RealVector getError() {
		return error;
	}

	public void setError(RealVector error) {
		this.error = error;
	}

	public RealVector getEv() {
		return ev;
	}

	public void setEv(RealVector ev) {
		this.ev = ev;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MdsResult other = (MdsResult) obj;
		double tolerance = .001;
		if (error == null) {
			if (other.error != null)
				return false;
		} else if (!Utils.compareVector(error, other.error, tolerance))
			return false;
		if (ev == null) {
			if (other.ev != null)
				return false;
		} else if (!Utils.compareVector(ev, other.ev, tolerance))
			return false;
		if (positions == null) {
			if (other.positions != null)
				return false;
		} else if (!Utils.compareMatrix(positions, other.positions, tolerance))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MdsResult [positions=" + positions + ", error=" + error + ", ev=" + ev + "]";
	}
}
