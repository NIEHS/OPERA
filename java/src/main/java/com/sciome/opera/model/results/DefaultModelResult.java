package com.sciome.opera.model.results;

import java.util.List;

public class DefaultModelResult extends OperaModelResult {
	Double						predictedValue;
	Double						confidenceIndex;
	Double						adIndex;
	Integer						adModel;
	List<String>				casNeighbor;
	List<String>				inchiNeighbor;
	List<String>				dtxsidNeighbor;
	List<String>				dsstoxmpidNeighbor;
	List<String>				expNeighbor;
	List<String>				predNeighbor;
	
	public Double getPredictedValue() {
		return predictedValue;
	}
	public void setPredictedValue(Double predictedValue) {
		this.predictedValue = predictedValue;
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
	
	public String getHeaderWithModel(String model) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Molecule ID" + "\t");
		builder.append(model + "_predicted value" + "\t");
		builder.append(model + "_confidence index" + "\t");
		builder.append(model + "_ad index" + "\t");
		builder.append(model + "_ad model" + "\t");
//		if(casNeighbor != null) {
//			for(int i = 0; i < casNeighbor.size(); i++) {
//				builder.append(model + "_casNeighbor_" + (i + 1) + "\t");
//			}
//		}
//		if(inchiNeighbor != null) {
//			for(int i = 0; i < inchiNeighbor.size(); i++) {
//				builder.append(model + "_inchiNeighbor_" + (i + 1) + "\t");
//			}
//		}
//		if(dtxsidNeighbor != null) {
//			for(int i = 0; i < dtxsidNeighbor.size(); i++) {
//				builder.append(model + "_dtxsidNeighbor_" + (i + 1) + "\t");
//			}
//		}
//		if(dsstoxmpidNeighbor != null) {
//			for(int i = 0; i < dsstoxmpidNeighbor.size(); i++) {
//				builder.append(model + "_dsstoxmpidNeighbor_" + (i + 1) + "\t");
//			}
//		}
//		if(expNeighbor != null) {
//			for(int i = 0; i < expNeighbor.size(); i++) {
//				builder.append(model + "_expNeighbor_" + (i + 1) + "\t");
//			}
//		}
//		if(predNeighbor != null) {
//			for(int i = 0; i < predNeighbor.size(); i++) {
//				builder.append(model + "_predNeighbor_" + (i + 1) + "\t");
//			}
//		}
		
		return builder.toString();
	}
	
	public String getHeaderWithModelWithoutChem(String model) {
		StringBuilder builder = new StringBuilder();
		
		builder.append(model + "_predicted value" + "\t");
		builder.append(model + "_confidence index" + "\t");
		builder.append(model + "_ad index" + "\t");
		builder.append(model + "_ad model" + "\t");
		
		return builder.toString();
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(chemicalDescriptors.getChemID() + "\t");
		builder.append(predictedValue + "\t");
		builder.append(confidenceIndex + "\t");
		builder.append(adIndex + "\t");
		builder.append(adModel + "\t");
		
//		if(casNeighbor != null) {
//			for(int i = 0; i < casNeighbor.size(); i++) {
//				builder.append(casNeighbor.get(i) + "\t");
//			}
//		}
//		if(inchiNeighbor != null) {
//			for(int i = 0; i < inchiNeighbor.size(); i++) {
//				builder.append(inchiNeighbor.get(i) + "\t");
//			}
//		}
//		if(dtxsidNeighbor != null) {
//			for(int i = 0; i < dtxsidNeighbor.size(); i++) {
//				builder.append(dtxsidNeighbor.get(i) + "\t");
//			}
//		}
//		if(dsstoxmpidNeighbor != null) {
//			for(int i = 0; i < dsstoxmpidNeighbor.size(); i++) {
//				builder.append(dsstoxmpidNeighbor.get(i) + "\t");
//			}
//		}
//		if(expNeighbor != null) {
//			for(int i = 0; i < expNeighbor.size(); i++) {
//				builder.append(expNeighbor.get(i) + "\t");
//			}
//		}
//		if(predNeighbor != null) {
//			for(int i = 0; i < predNeighbor.size(); i++) {
//				builder.append(predNeighbor.get(i) + "\t");
//			}
//		}
		
		return builder.toString();
	}
	
	public String toStringWithoutChem() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(predictedValue + "\t");
		builder.append(confidenceIndex + "\t");
		builder.append(adIndex + "\t");
		builder.append(adModel + "\t");
		
//		if(casNeighbor != null) {
//			for(int i = 0; i < casNeighbor.size(); i++) {
//				builder.append(casNeighbor.get(i) + "\t");
//			}
//		}
//		if(inchiNeighbor != null) {
//			for(int i = 0; i < inchiNeighbor.size(); i++) {
//				builder.append(inchiNeighbor.get(i) + "\t");
//			}
//		}
//		if(dtxsidNeighbor != null) {
//			for(int i = 0; i < dtxsidNeighbor.size(); i++) {
//				builder.append(dtxsidNeighbor.get(i) + "\t");
//			}
//		}
//		if(dsstoxmpidNeighbor != null) {
//			for(int i = 0; i < dsstoxmpidNeighbor.size(); i++) {
//				builder.append(dsstoxmpidNeighbor.get(i) + "\t");
//			}
//		}
//		if(expNeighbor != null) {
//			for(int i = 0; i < expNeighbor.size(); i++) {
//				builder.append(expNeighbor.get(i) + "\t");
//			}
//		}
//		if(predNeighbor != null) {
//			for(int i = 0; i < predNeighbor.size(); i++) {
//				builder.append(predNeighbor.get(i) + "\t");
//			}
//		}
		
		return builder.toString();
	}
}
