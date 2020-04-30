package com.sciome.opera.model.training.model.CATMoS;

import java.util.List;

public class CATMOSLD50Model {
	CATMOSLD50Set			set;
	List<Double>			conc;
	List<String>			desc;
	List<String>			desc_i;
	List<String>			CAS;
	List<String>			inChiKey;
	List<String>			dtxSid;
	List<Integer>			chemId;
	List<Integer>			rm;
	List<Integer>			rm2;
	
	
	public CATMOSLD50Set getSet() {
		return set;
	}
	public void setSet(CATMOSLD50Set set) {
		this.set = set;
	}
	public List<Double> getConc() {
		return conc;
	}
	public void setConc(List<Double> conc) {
		this.conc = conc;
	}
	public List<String> getDesc() {
		return desc;
	}
	public void setDesc(List<String> desc) {
		this.desc = desc;
	}
	public List<String> getDesc_i() {
		return desc_i;
	}
	public void setDesc_i(List<String> desc_i) {
		this.desc_i = desc_i;
	}
	public List<String> getCAS() {
		return CAS;
	}
	public void setCAS(List<String> cAS) {
		CAS = cAS;
	}
	public List<String> getInChiKey() {
		return inChiKey;
	}
	public void setInChiKey(List<String> inChiKey) {
		this.inChiKey = inChiKey;
	}
	public List<String> getDtxSid() {
		return dtxSid;
	}
	public void setDtxSid(List<String> dtxSid) {
		this.dtxSid = dtxSid;
	}
	public List<Integer> getChemId() {
		return chemId;
	}
	public void setChemId(List<Integer> chemId) {
		this.chemId = chemId;
	}
	public List<Integer> getRm() {
		return rm;
	}
	public void setRm(List<Integer> rm) {
		this.rm = rm;
	}
	public List<Integer> getRm2() {
		return rm2;
	}
	public void setRm2(List<Integer> rm2) {
		this.rm2 = rm2;
	}
}
