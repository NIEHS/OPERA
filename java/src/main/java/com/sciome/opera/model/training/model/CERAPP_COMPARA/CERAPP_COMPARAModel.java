package com.sciome.opera.model.training.model.CERAPP_COMPARA;

import java.util.List;

public class CERAPP_COMPARAModel {
	private CERAPP_COMPARASet set;
	private List<String> desc;
	private List<Integer> desc_i;
	private List<Double> conc;
	private List<Integer> chemId;
	private List<String> cas;
	private List<String> inChiKey;
	private List<String> dtxsid;
	private List<Integer> rm;
	
	public CERAPP_COMPARASet getSet() {
		return set;
	}
	public void setSet(CERAPP_COMPARASet set) {
		this.set = set;
	}
	public List<String> getDesc() {
		return desc;
	}
	public void setDesc(List<String> desc) {
		this.desc = desc;
	}
	public List<Integer> getDesc_i() {
		return desc_i;
	}
	public void setDesc_i(List<Integer> desc_i) {
		this.desc_i = desc_i;
	}
	public List<Double> getConc() {
		return conc;
	}
	public void setConc(List<Double> conc) {
		this.conc = conc;
	}
	public List<Integer> getChemId() {
		return chemId;
	}
	public void setChemId(List<Integer> chemId) {
		this.chemId = chemId;
	}
	public List<String> getCas() {
		return cas;
	}
	public void setCas(List<String> cas) {
		this.cas = cas;
	}
	public List<String> getInChiKey() {
		return inChiKey;
	}
	public void setInChiKey(List<String> inChiKey) {
		this.inChiKey = inChiKey;
	}
	public List<String> getDtxsid() {
		return dtxsid;
	}
	public void setDtxsid(List<String> dtxsid) {
		this.dtxsid = dtxsid;
	}
	public List<Integer> getRm() {
		return rm;
	}
	public void setRm(List<Integer> rm) {
		this.rm = rm;
	}
}
