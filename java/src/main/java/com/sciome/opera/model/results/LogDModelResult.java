package com.sciome.opera.model.results;

import java.util.List;

public class LogDModelResult extends OperaModelResult {
	private Double 						LogD55_pred;
	private Double 						LogD74_pred;
	private Double						confidenceIndex;
	private Double						adIndex;
	private Integer						adModel;
	private List<String>				casNeighbor;
	private List<String>				inchiNeighbor;
	private List<String>				dtxsidNeighbor;
	private List<String>				dsstoxmpidNeighbor;
	
	public Double getLogD55_pred() {
		return LogD55_pred;
	}
	public void setLogD55_pred(Double logD55_pred) {
		LogD55_pred = logD55_pred;
	}
	public Double getLogD74_pred() {
		return LogD74_pred;
	}
	public void setLogD74_pred(Double logD74_pred) {
		LogD74_pred = logD74_pred;
	}
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
}
