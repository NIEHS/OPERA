package com.sciome.opera.model.training.model.CATMoS;

import java.util.List;

import com.sciome.opera.model.training.TrainingParam;
import com.sciome.opera.model.training.model.TrainingParamFlat;

public class CATMOSLD50Set {
	List<List<Double>>		train;
	List<Double>			y;
	TrainingParam			param;
	Integer					k;
	String					dist_type;
	TrainingParamFlat		dc_param;
	List<Double>			dc1;
	List<Double>			y_exp_n;
	List<String>			y_exp;
	
	
	public List<List<Double>> getTrain() {
		return train;
	}
	public void setTrain(List<List<Double>> train) {
		this.train = train;
	}
	public List<Double> getY() {
		return y;
	}
	public void setY(List<Double> y) {
		this.y = y;
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
	public TrainingParamFlat getDc_param() {
		return dc_param;
	}
	public void setDc_param(TrainingParamFlat dc_param) {
		this.dc_param = dc_param;
	}
	public List<Double> getDc1() {
		return dc1;
	}
	public void setDc1(List<Double> dc1) {
		this.dc1 = dc1;
	}
	public List<Double> getY_exp_n() {
		return y_exp_n;
	}
	public void setY_exp_n(List<Double> y_exp_n) {
		this.y_exp_n = y_exp_n;
	}
	public List<String> getY_exp() {
		return y_exp;
	}
	public void setY_exp(List<String> y_exp) {
		this.y_exp = y_exp;
	}
}
