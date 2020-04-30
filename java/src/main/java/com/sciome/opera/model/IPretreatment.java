package com.sciome.opera.model;

import org.apache.commons.math3.linear.RealVector;

import com.sciome.opera.enums.PretreatmentType;

public interface IPretreatment {
	public RealVector getMean();
	public RealVector getSd();
	public RealVector getMin();
	public RealVector getMax();
	public PretreatmentType getPret_type();
}
