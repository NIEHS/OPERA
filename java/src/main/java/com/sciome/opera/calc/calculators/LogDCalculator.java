package com.sciome.opera.calc.calculators;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.RealVector;

import com.sciome.opera.descriptors.OPERAChemicalDescriptors;
import com.sciome.opera.enums.Models;
import com.sciome.opera.model.Prediction;
import com.sciome.opera.model.results.DefaultModelResult;
import com.sciome.opera.model.results.LogDModelResult;
import com.sciome.opera.model.results.OperaModelResult;
import com.sciome.opera.model.results.PKAModelResult;

public class LogDCalculator extends ModelCalculator{
	List<OperaModelResult> pKaResults;
	List<OperaModelResult> logPResults;
	
	public LogDCalculator() {
		this.model = Models.LOGD;
	}
	
	@Override
	public List<OperaModelResult> calc(List<OPERAChemicalDescriptors> descriptors, boolean neighbors, boolean exp) {
		List<OperaModelResult> results = new ArrayList<OperaModelResult>();
		
		for(int i = 0; i < descriptors.size(); i++) {
			LogDModelResult result = new LogDModelResult();
			DefaultModelResult logPResult = (DefaultModelResult)logPResults.get(i);
			PKAModelResult pKaResult = (PKAModelResult)pKaResults.get(i);
			
			Double logD55 = logPResult.getPredictedValue();
			Double logD74 = logPResult.getPredictedValue();
			Integer logD_AD = logPResult.getAdModel() + pKaResult.getAdModel();
			if(logD_AD == 1)
				logD_AD = 0 ;
			if(logD_AD == 2)
				logD_AD = 1;
			
			Double AD_index = 0.5* pKaResult.getAdIndex() + 0.5*logPResult.getAdIndex();
			Double Conf_index = 0.5* pKaResult.getConfidenceIndex() + 0.5*logPResult.getConfidenceIndex();
			
			if(pKaResult.getpKa_ac_ba_amp() == 1) {
				logD55 = logPResult.getPredictedValue() - Math.log10(1 + Math.pow(10, 5.5 - pKaResult.getpKa_a_pred()));
				logD74 = logPResult.getPredictedValue() - Math.log10(1 + Math.pow(10, 7.4 - pKaResult.getpKa_a_pred()));
			} else if(pKaResult.getpKa_ac_ba_amp() == 2) {
				logD55 = logPResult.getPredictedValue() - Math.log10(1 + Math.pow(10, pKaResult.getpKa_b_pred() - 5.5));
				logD74 = logPResult.getPredictedValue() - Math.log10(1 + Math.pow(10, pKaResult.getpKa_b_pred() - 7.4));
			} else if(pKaResult.getpKa_ac_ba_amp() == 3) {
				logD55 = logPResult.getPredictedValue() - Math.log10(1 + Math.pow(10, Math.abs(0.5 * pKaResult.getpKa_a_pred() + 0.5 * pKaResult.getpKa_b_pred() - 5.5)));
				logD74 = logPResult.getPredictedValue() - Math.log10(1 + Math.pow(10, Math.abs(0.5 * pKaResult.getpKa_a_pred() + 0.5 * pKaResult.getpKa_b_pred() - 7.4)));
			}
			
			result.setChemicalDescriptors(descriptors.get(i));
			result.setLogD55_pred(logD55);
			result.setLogD74_pred(logD74);
			result.setAdModel(logD_AD);
			result.setAdIndex(AD_index);
			result.setConfidenceIndex(Conf_index);
			if(neighbors) {
				result.setCasNeighbor(logPResult.getCasNeighbor());
				result.setInchiNeighbor(logPResult.getInchiNeighbor());
				result.setDtxsidNeighbor(logPResult.getDtxsidNeighbor());
				result.setDsstoxmpidNeighbor(logPResult.getDsstoxmpidNeighbor());
			}
			
			results.add(result);
		}
		
		return results;
	}

	@Override
	public Double calculateConfIndex(Prediction pred, RealVector predNeighbor, RealVector expNeighbor,
			RealVector ADIndex, int i) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setpKaResults(List<OperaModelResult> pKaResults) {
		this.pKaResults = pKaResults;
	}
	
	public void setlogPResults(List<OperaModelResult> logPResults) {
		this.logPResults = logPResults;
	}
	
}
