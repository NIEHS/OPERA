package com.sciome.opera.model.results;

import java.util.List;

public class PKAModelResult extends OperaModelResult {
	private Integer 					ionization;
	private Double 						pKa_a_pred;
	private Double						pKa_b_pred;
	private Double						pKa_ac_ba_amp;
//	private Double 						pKa_a_exp;
//	private Double 						pKa_b_exp;
	private Double						confidenceIndex;
	private Double						adIndex;
	private Integer						adModel;
	private List<String>				casNeighbor;
	private List<String>				inchiNeighbor;
	private List<String>				dtxsidNeighbor;
	private List<String>				dsstoxmpidNeighbor;
	private List<String>				expNeighbor;
	private List<String>				predNeighbor;
	
	public Integer getIonization() {
		return ionization;
	}
	public void setIonization(Integer ionization) {
		this.ionization = ionization;
	}
	public Double getpKa_a_pred() {
		return pKa_a_pred;
	}
	public void setpKa_a_pred(Double pKa_a_pred) {
		this.pKa_a_pred = pKa_a_pred;
	}
	public Double getpKa_b_pred() {
		return pKa_b_pred;
	}
	public void setpKa_b_pred(Double pKa_b_pred) {
		this.pKa_b_pred = pKa_b_pred;
	}
	public Double getpKa_ac_ba_amp() {
		return pKa_ac_ba_amp;
	}
	public void setpKa_ac_ba_amp(Double pKa_ac_ba_amp) {
		this.pKa_ac_ba_amp = pKa_ac_ba_amp;
	}
	//	public Double getpKa_a_exp() {
//		return pKa_a_exp;
//	}
//	public void setpKa_a_exp(Double pKa_a_exp) {
//		this.pKa_a_exp = pKa_a_exp;
//	}
//	public Double getpKa_b_exp() {
//		return pKa_b_exp;
//	}
//	public void setpKa_b_exp(Double pKa_b_exp) {
//		this.pKa_b_exp = pKa_b_exp;
//	}
	public Double getConfidenceIndex() {
		return confidenceIndex;
	}
	public void setConfidenceIndex(Double confidenceIndex) {
		this.confidenceIndex = confidenceIndex;
	}
	public Double getAdIndex() {
		return adIndex;
	}
	public void setAdIndex(Double adIndex) {
		this.adIndex = adIndex;
	}
	public Integer getAdModel() {
		return adModel;
	}
	public void setAdModel(Integer adModel) {
		this.adModel = adModel;
	}
	public List<String> getCasNeighbor() {
		return casNeighbor;
	}
	public void setCasNeighbor(List<String> casNeighbor) {
		this.casNeighbor = casNeighbor;
	}
	public List<String> getInchiNeighbor() {
		return inchiNeighbor;
	}
	public void setInchiNeighbor(List<String> inchiNeighbor) {
		this.inchiNeighbor = inchiNeighbor;
	}
	public List<String> getDtxsidNeighbor() {
		return dtxsidNeighbor;
	}
	public void setDtxsidNeighbor(List<String> dtxsidNeighbor) {
		this.dtxsidNeighbor = dtxsidNeighbor;
	}
	public List<String> getDsstoxmpidNeighbor() {
		return dsstoxmpidNeighbor;
	}
	public void setDsstoxmpidNeighbor(List<String> dsstoxmpidNeighbor) {
		this.dsstoxmpidNeighbor = dsstoxmpidNeighbor;
	}
	public List<String> getExpNeighbor() {
		return expNeighbor;
	}
	public void setExpNeighbor(List<String> expNeighbor) {
		this.expNeighbor = expNeighbor;
	}
	public List<String> getPredNeighbor() {
		return predNeighbor;
	}
	public void setPredNeighbor(List<String> predNeighbor) {
		this.predNeighbor = predNeighbor;
	}
}
