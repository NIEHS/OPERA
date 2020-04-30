package com.sciome.opera.model;

import org.apache.commons.math3.linear.RealVector;

import com.sciome.opera.calc.math.Utils;

public class ClassicalLeverageResult {
	RealVector Ht_diag;
	RealVector inorout;
	Integer nout;
	
	public ClassicalLeverageResult(RealVector ht_diag, RealVector inorout, Integer nout) {
		super();
		Ht_diag = ht_diag;
		this.inorout = inorout;
		this.nout = nout;
	}
	
	public RealVector getHt_diag() {
		return Ht_diag;
	}
	public void setHt_diag(RealVector ht_diag) {
		Ht_diag = ht_diag;
	}
	public RealVector getInorout() {
		return inorout;
	}
	public void setInorout(RealVector inorout) {
		this.inorout = inorout;
	}
	public Integer getNout() {
		return nout;
	}
	public void setNout(Integer nout) {
		this.nout = nout;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassicalLeverageResult other = (ClassicalLeverageResult) obj;
		double tolerance = .001;
		if (Ht_diag == null) {
			if (other.Ht_diag != null)
				return false;
		} else if (!Utils.compareVector(Ht_diag, other.Ht_diag, tolerance))
			return false;
		if (inorout == null) {
			if (other.inorout != null)
				return false;
		} else if (!Utils.compareVector(inorout, other.inorout, tolerance))
			return false;
		if (nout == null) {
			if (other.nout != null)
				return false;
		} else if (!nout.equals(other.nout))
			return false;
		return true;
	}
	
	
}
