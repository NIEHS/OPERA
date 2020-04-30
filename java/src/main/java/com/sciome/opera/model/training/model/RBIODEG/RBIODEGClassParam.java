package com.sciome.opera.model.training.model.RBIODEG;

import java.util.List;

public class RBIODEGClassParam {
	List<List<Integer>>		conf_mat;
	Double					ner;
	Double					er;
	Integer					not_ass;
	List<Double>			precision;
	List<Double>			sensitivity;
	List<Double>			specificity;
	List<Double>			class_error;
	public List<List<Integer>> getConf_mat() {
		return conf_mat;
	}
	public void setConf_mat(List<List<Integer>> conf_mat) {
		this.conf_mat = conf_mat;
	}
	public Double getNer() {
		return ner;
	}
	public void setNer(Double ner) {
		this.ner = ner;
	}
	public Double getEr() {
		return er;
	}
	public void setEr(Double er) {
		this.er = er;
	}
	public Integer getNot_ass() {
		return not_ass;
	}
	public void setNot_ass(Integer not_ass) {
		this.not_ass = not_ass;
	}
	public List<Double> getPrecision() {
		return precision;
	}
	public void setPrecision(List<Double> precision) {
		this.precision = precision;
	}
	public List<Double> getSensitivity() {
		return sensitivity;
	}
	public void setSensitivity(List<Double> sensitivity) {
		this.sensitivity = sensitivity;
	}
	public List<Double> getSpecificity() {
		return specificity;
	}
	public void setSpecificity(List<Double> specificity) {
		this.specificity = specificity;
	}
	public List<Double> getClass_error() {
		return class_error;
	}
	public void setClass_error(List<Double> class_error) {
		this.class_error = class_error;
	}
}
