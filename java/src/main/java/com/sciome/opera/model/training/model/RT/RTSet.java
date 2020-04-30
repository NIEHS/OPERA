package com.sciome.opera.model.training.model.RT;

import java.util.List;

import com.sciome.opera.model.training.TrainingParam;
import com.sciome.opera.model.training.model.TrainingParamFlat;

public class RTSet {
	TrainingParam			px;
	TrainingParamFlat		py;
	String					scal;
	List<Double>			y;
	List<List<Double>>		train;
	Integer					k;
	String					dist_type;
	
	public TrainingParam getPx() {
		return px;
	}
	public void setPx(TrainingParam px) {
		this.px = px;
	}
	public TrainingParamFlat getPy() {
		return py;
	}
	public void setPy(TrainingParamFlat py) {
		this.py = py;
	}
	public String getScal() {
		return scal;
	}
	public void setScal(String scal) {
		this.scal = scal;
	}
	public List<Double> getY() {
		return y;
	}
	public void setY(List<Double> y) {
		this.y = y;
	}
	public List<List<Double>> getTrain() {
		return train;
	}
	public void setTrain(List<List<Double>> train) {
		this.train = train;
	}
	public Integer getK() {
		return k;
	}
	public void setK(Integer k) {
		this.k = k;
	}
	public String getDist_type() {
		return dist_type;
	}
	public void setDist_type(String dist_type) {
		this.dist_type = dist_type;
	}
}
