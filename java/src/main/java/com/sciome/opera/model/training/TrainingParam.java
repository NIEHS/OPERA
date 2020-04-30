package com.sciome.opera.model.training;

import java.util.List;

import org.apache.commons.math3.linear.RealVector;

import com.sciome.opera.calc.math.Utils;
import com.sciome.opera.enums.PretreatmentType;
import com.sciome.opera.model.IPretreatment;

public class TrainingParam implements IPretreatment
{
	List<Double>		mean;
	List<Double>		sd;
	List<Double>		min;
	List<Double>		max;

	String				pret_type;

	public RealVector getMean() {
		return Utils.vectorFromList(mean);
	}

	public void setMean(List<Double> mean) {
		this.mean = mean;
	}

	public RealVector getSd() {
		return Utils.vectorFromList(sd);
	}

	public void setSd(List<Double> sd) {
		this.sd = sd;
	}

	public RealVector getMin() {
		return Utils.vectorFromList(min);
	}

	public void setMin(List<Double> min) {
		this.min = min;
	}

	public RealVector getMax() {
		return Utils.vectorFromList(max);
	}

	public void setMax(List<Double> max) {
		this.max = max;
	}

	public PretreatmentType getPret_type() {
		return PretreatmentType.findByName(pret_type);
	}

	public void setPretType(String pretType) {
		this.pret_type = pretType;
	}
}
