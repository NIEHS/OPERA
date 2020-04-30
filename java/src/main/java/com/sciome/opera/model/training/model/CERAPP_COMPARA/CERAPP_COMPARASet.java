package com.sciome.opera.model.training.model.CERAPP_COMPARA;

import java.util.List;

import com.sciome.opera.model.training.TrainingParam;
import com.sciome.opera.model.training.model.TrainingParamFlat;

public class CERAPP_COMPARASet {
	private List<List<Double>>		train;
	private List<Integer>			clazz;
	private TrainingParam			param;
	private Integer					k;
	private String					dist_type;
	private List<Double>			dc1;
	private TrainingParamFlat		dc_param;
	private List<Integer>			class_exp_n;
	private List<String>			class_exp;
	private List<String>			class_s;
	
	
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
	public List<Integer> getClass_exp_n() {
		return class_exp_n;
	}
	public void setClass_exp_n(List<Integer> class_exp_n) {
		this.class_exp_n = class_exp_n;
	}
	public List<String> getClass_exp() {
		return class_exp;
	}
	public void setClass_exp(List<String> class_exp) {
		this.class_exp = class_exp;
	}
	public List<String> getClass_s() {
		return class_s;
	}
	public void setClass_s(List<String> class_s) {
		this.class_s = class_s;
	}
}
