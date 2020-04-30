package com.sciome.opera.model.results;

import com.sciome.opera.enums.Models;

public class CATMOSModelResult extends OperaModelResult {
	DefaultModelResult vt;
	DefaultModelResult nt;
	DefaultModelResult epa;
	DefaultModelResult ghs;
	DefaultModelResult ld50;
	
	public DefaultModelResult getVt() {
		return vt;
	}
	public void setVt(DefaultModelResult vt) {
		this.vt = vt;
	}
	public DefaultModelResult getNt() {
		return nt;
	}
	public void setNt(DefaultModelResult nt) {
		this.nt = nt;
	}
	public DefaultModelResult getEpa() {
		return epa;
	}
	public void setEpa(DefaultModelResult epa) {
		this.epa = epa;
	}
	public DefaultModelResult getGhs() {
		return ghs;
	}
	public void setGhs(DefaultModelResult ghs) {
		this.ghs = ghs;
	}
	public DefaultModelResult getLd50() {
		return ld50;
	}
	public void setLd50(DefaultModelResult ld50) {
		this.ld50 = ld50;
	}
	
	public String getHeaderWithModel(Models model) {
		StringBuilder header = new StringBuilder();
		
		header.append(this.getVt().getHeaderWithModel(model.name() + " VT"));
		header.append(this.getNt().getHeaderWithModelWithoutChem(model.name() + " NT"));
		header.append(this.getEpa().getHeaderWithModelWithoutChem(model.name() + " EPA"));
		header.append(this.getGhs().getHeaderWithModelWithoutChem(model.name() + " GHS"));
		header.append(this.getLd50().getHeaderWithModelWithoutChem(model.name() + " LD50"));
		
		return header.toString();
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(this.getVt().toString());
		builder.append(this.getNt().toStringWithoutChem());
		builder.append(this.getEpa().toStringWithoutChem());
		builder.append(this.getGhs().toStringWithoutChem());
		builder.append(this.getLd50().toStringWithoutChem());
		
		return builder.toString();
	}
}
