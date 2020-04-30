package com.sciome.opera.model.training.model.CATMoS;

import java.util.List;

import com.sciome.opera.model.training.TrainingParam;
import com.sciome.opera.model.training.model.TrainingParamFlat;

public class CATMOSSet {
	List<List<Double>>		train;
	List<Integer>			clazz;
	TrainingParam			param;
	Integer					K;
	String					dist_type;
	List<Double>			dc1;
	TrainingParamFlat		dc_param;
	List<Integer>			class_Exp;
	
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
	public List<Double> getDc1() {
		return dc1;
	}
	public void setDc1(List<Double> dc1) {
		this.dc1 = dc1;
	}
	public TrainingParamFlat getDc_param() {
		return dc_param;
	}
	public void setDc_param(TrainingParamFlat dc_param) {
		this.dc_param = dc_param;
	}
	public List<Integer> getClass_Exp() {
		return class_Exp;
	}
	public void setClass_Exp(List<Integer> class_Exp) {
		this.class_Exp = class_Exp;
	}
}
