package com.sciome.opera.model.training.model.RBIODEG;

import java.util.List;

import com.sciome.opera.model.training.TrainingParam;

public class RBIODEGSet {
	List<List<Double>>		train;
	List<Integer>			clazz;
	TrainingParam			param;
	Integer					k;
	String					dist_type;
	
	public List<List<Double>> getTrain() {
		return train;
	}
	public void setTrain(List<List<Double>> train) {
		this.train = train;
	}
	public List<Integer> getClazz() {
		return clazz;
	}
	public void setClazz(List<Integer> clazz) {
		this.clazz = clazz;
	}
	public TrainingParam getParam() {
		return param;
	}
	public void setParam(TrainingParam param) {
		this.param = param;
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
