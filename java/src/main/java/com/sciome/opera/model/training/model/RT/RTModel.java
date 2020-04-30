package com.sciome.opera.model.training.model.RT;

import java.util.List;

public class RTModel {
	List<Double>			yc;
	List<Double>			r;
	List<List<Double>>		t;
	List<List<Double>>		p;
	List<List<Double>>		u;
	List<Double>			q;
	List<List<Double>>		w;
	List<List<Double>>		expvar;
	List<List<Double>>		cumvar;
	RTSet					set;
	List<String>			id;
	
	
	public List<Double> getYc() {
		return yc;
	}
	public void setYc(List<Double> yc) {
		this.yc = yc;
	}
	public List<Double> getR() {
		return r;
	}
	public void setR(List<Double> r) {
		this.r = r;
	}
	public List<List<Double>> getT() {
		return t;
	}
	public void setT(List<List<Double>> t) {
		this.t = t;
	}
	public List<List<Double>> getP() {
		return p;
	}
	public void setP(List<List<Double>> p) {
		this.p = p;
	}
	public List<List<Double>> getU() {
		return u;
	}
	public void setU(List<List<Double>> u) {
		this.u = u;
	}
	public List<Double> getQ() {
		return q;
	}
	public void setQ(List<Double> q) {
		this.q = q;
	}
	public List<List<Double>> getW() {
		return w;
	}
	public void setW(List<List<Double>> w) {
		this.w = w;
	}
	public List<List<Double>> getExpvar() {
		return expvar;
	}
	public void setExpvar(List<List<Double>> expvar) {
		this.expvar = expvar;
	}
	public List<List<Double>> getCumvar() {
		return cumvar;
	}
	public void setCumvar(List<List<Double>> cumvar) {
		this.cumvar = cumvar;
	}
	public RTSet getSet() {
		return set;
	}
	public void setSet(RTSet set) {
		this.set = set;
	}
	public List<String> getId() {
		return id;
	}
	public void setId(List<String> id) {
		this.id = id;
	}
}
