package com.sciome.opera.model.training.model;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;

import com.sciome.opera.enums.PretreatmentType;
import com.sciome.opera.model.IPretreatment;

public class TrainingParamFlat implements IPretreatment {
	private Double 		mean;
	private Double		sd;
	private Double		min;
	private Double		max;
	
	private String 		pret_type;

	public RealVector getMean() {
		return MatrixUtils.createRealVector(new double[]{mean});
	}

	public void setMean(Double mean) {
		this.mean = mean;
	}

	public RealVector getSd() {
		return MatrixUtils.createRealVector(new double[]{sd});
	}

	public void setSd(Double sd) {
		this.sd = sd;
	}

	public RealVector getMin() {
		return MatrixUtils.createRealVector(new double[]{min});
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public RealVector getMax() {
		return MatrixUtils.createRealVector(new double[]{max});
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public PretreatmentType getPret_type() {
		return PretreatmentType.findByName(pret_type);
	}

	public void setPretType(String pretType) {
		this.pret_type = pretType;
	}
}
