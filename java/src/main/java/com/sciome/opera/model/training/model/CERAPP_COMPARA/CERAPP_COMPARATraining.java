package com.sciome.opera.model.training.model.CERAPP_COMPARA;

import java.util.List;

public class CERAPP_COMPARATraining {
	private CERAPP_COMPARAModel				model_ag;
	private CERAPP_COMPARAModel				model_an;
	private CERAPP_COMPARAModel				model_bd;
	private List<String>					desc;
	private List<String>					padelDesc_in;
	private List<Integer>					padel_in;
	private List<String>					cdkDesc_in;
	private List<Integer>					cdk_in;
	private List<String>					descIn;
	
	
	public CERAPP_COMPARAModel getModel_ag() {
		return model_ag;
	}
	public void setModel_ag(CERAPP_COMPARAModel model_ag) {
		this.model_ag = model_ag;
	}
	public CERAPP_COMPARAModel getModel_an() {
		return model_an;
	}
	public void setModel_an(CERAPP_COMPARAModel model_an) {
		this.model_an = model_an;
	}
	public CERAPP_COMPARAModel getModel_bd() {
		return model_bd;
	}
	public void setModel_bd(CERAPP_COMPARAModel model_bd) {
		this.model_bd = model_bd;
	}
	public List<String> getDesc() {
		return desc;
	}
	public void setDesc(List<String> desc) {
		this.desc = desc;
	}
	public List<String> getPadelDesc_in() {
		return padelDesc_in;
	}
	public void setPadelDesc_in(List<String> padelDesc_in) {
		this.padelDesc_in = padelDesc_in;
	}
	public List<Integer> getPadel_in() {
		return padel_in;
	}
	public void setPadel_in(List<Integer> padel_in) {
		this.padel_in = padel_in;
	}
	public List<String> getCdkDesc_in() {
		return cdkDesc_in;
	}
	public void setCdkDesc_in(List<String> cdkDesc_in) {
		this.cdkDesc_in = cdkDesc_in;
	}
	public List<Integer> getCdk_in() {
		return cdk_in;
	}
	public void setCdk_in(List<Integer> cdk_in) {
		this.cdk_in = cdk_in;
	}
	public List<String> getDescIn() {
		return descIn;
	}
	public void setDescIn(List<String> descIn) {
		this.descIn = descIn;
	}
}
