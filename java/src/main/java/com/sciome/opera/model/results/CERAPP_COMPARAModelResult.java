package com.sciome.opera.model.results;

import com.sciome.opera.enums.Models;

public class CERAPP_COMPARAModelResult extends OperaModelResult {
	DefaultModelResult ago;
	DefaultModelResult anta;
	DefaultModelResult bind;
	public DefaultModelResult getAgo() {
		return ago;
	}
	public void setAgo(DefaultModelResult ago) {
		this.ago = ago;
	}
	public DefaultModelResult getAnta() {
		return anta;
	}
	public void setAnta(DefaultModelResult anta) {
		this.anta = anta;
	}
	public DefaultModelResult getBind() {
		return bind;
	}
	public void setBind(DefaultModelResult bind) {
		this.bind = bind;
	}
	
	public String getHeaderWithModel(Models model) {
		StringBuilder header = new StringBuilder();
		
		header.append(this.getAgo().getHeaderWithModel(model.name() + " Ago"));
		header.append(this.getAnta().getHeaderWithModelWithoutChem(model.name() + " Anta"));
		header.append(this.getBind().getHeaderWithModelWithoutChem(model.name() + " Bind"));
		
		return header.toString();
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(this.getAgo().toString());
		builder.append(this.getAnta().toStringWithoutChem());
		builder.append(this.getBind().toStringWithoutChem());
		
		return builder.toString();
	}
}