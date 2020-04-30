package com.sciome.opera.model.training.model.pKa;

import java.util.List;

import com.sciome.opera.model.training.TrainingParam;

public class PKASet {
	List<List<Double>>	train;
	List<Integer>		clazz;
	TrainingParam		param;
	Integer				K;

	String				dist_type;

	List<Double>		y;
	List<Double>		yc;
	
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
		return K;
	}
	public void setK(Integer k) {
		K = k;
	}
	public String getDist_type() {
		return dist_type;
	}
	public void setDist_type(String dist_type) {
		this.dist_type = dist_type;
	}
	public List<Double> getY() {
		return y;
	}
	public void setY(List<Double> y) {
		this.y = y;
	}
	public List<Double> getYc() {
		return yc;
	}
	public void setYc(List<Double> yc) {
		this.yc = yc;
	}
}
