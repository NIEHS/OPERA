package com.sciome.opera.calc.calculators;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import com.sciome.opera.calc.math.ClassicalLeverage;
import com.sciome.opera.calc.math.NNRPred;
import com.sciome.opera.calc.math.PlsTest;
import com.sciome.opera.calc.math.Utils;
import com.sciome.opera.descriptors.OPERAChemicalDescriptors;
import com.sciome.opera.enums.DistanceType;
import com.sciome.opera.enums.Models;
import com.sciome.opera.enums.PretreatmentType;
import com.sciome.opera.model.ClassicalLeverageResult;
import com.sciome.opera.model.Prediction;
import com.sciome.opera.model.results.DefaultModelResult;
import com.sciome.opera.model.results.OperaModelResult;
import com.sciome.opera.model.training.model.RT.RTSet;
import com.sciome.opera.model.training.model.RT.RTTraining;

public class RTCalculator extends ModelCalculator {
	private RTTraining rtTraining;
	public RTCalculator()
	{
		this.rtTraining = RTTraining.getInstance();
		this.model = Models.RT;
	}
	
	@Override
	public List<OperaModelResult> calc(List<OPERAChemicalDescriptors> descriptors, boolean neighbors, boolean exp) {
		List<OperaModelResult> results = new ArrayList<>();
		
		double[][] XtestArray = new double[descriptors.size()][rtTraining.getDesc().size()];
		for (int i = 0; i < XtestArray.length; i++) {
			for (int j = 0; j < XtestArray[i].length; j++) {
				XtestArray[i][j] = Double.valueOf(descriptors.get(i).getPadelValue(rtTraining.getDesc().get(j).toLowerCase()));
			}
		}
		RealMatrix Xtest = MatrixUtils.createRealMatrix(XtestArray);
		
		RTSet set = rtTraining.getModel().getSet();
		Prediction pred = NNRPred.predict(Xtest, Utils.matrixFromList(set.getTrain()), Utils.vectorFromList(set.getY()), set.getK(),
				DistanceType.findByName(set.getDist_type()), PretreatmentType.findByName(set.getScal()));
		RealMatrix pls = PlsTest.pls(Xtest, rtTraining.getModel());
		
		ClassicalLeverageResult AD = ClassicalLeverage.classicalLeverage(Utils.matrixFromList(set.getTrain()), Xtest, PretreatmentType.auto);
		
		RealVector ADRT = Utils.absVector(AD.getInorout().mapSubtractToSelf(1.0));
		
		//Add back in for 2.4
//		for(int i = 0; i < pred.getDc().getRowDimension(); i++) {
//			if(pred.getDc().getEntry(i, 0) == 0)
//				ADRT.setEntry(i, 1);
//		}
		
		RealVector Exp = Utils.zeros(Xtest.getRowDimension());
		RealVector AD_index_RT = Utils.zeros(Xtest.getRowDimension());
		RealVector Conf_index_RT = Utils.zeros(Xtest.getRowDimension());
		
		List<List<String>> CAS_neighbor = new ArrayList<>();
		List<List<String>> DTXSID_neighbor = new ArrayList<>();
		List<List<String>> Exp_neighbor = new ArrayList<>();
		List<List<String>> pred_neighbor = new ArrayList<>();
		
		for(int i = 0; i < descriptors.size(); i++) {
			//Add back in for 2.4
//			boolean li = false;
//			int lo = 0;
//			String moleculeName = descriptors.get(i).getChemID();
//			if(exp && !descriptors.get(i).getChemID().contains("AUTOGEN_")) {
//				if(moleculeName.matches("[0-9]+-[0-9]+-[0-9]")) {
//					li = rtTraining.getCas().contains(moleculeName);
//					lo = rtTraining.getCas().indexOf(moleculeName);
//				} else if(moleculeName.matches("DTXSID[0-9]+")) {
//					li = rtTraining.getDtxSid().contains(moleculeName);
//					lo = rtTraining.getDtxSid().indexOf(moleculeName);
//				}
//			}
//			if(li) {
//				if(lo > rtTraining.getDtxSid().size())
//					lo = lo % rtTraining.getDtxSid().size();
//				Exp.setEntry(i, set.getY().get(lo));
//			}
			
			List<String> CAS_neighbor_inner = new ArrayList<>();
			List<String> DTXSID_neighbor_inner = new ArrayList<>();
			List<String> Exp_neighbor_inner = new ArrayList<>();
			List<String> pred_neighbor_inner = new ArrayList<>();
			for(int j = 0; j < pred.getNeighbors().getColumnDimension(); j++) {
				CAS_neighbor_inner.add(rtTraining.getCas().get((int)pred.getNeighbors().getEntry(i, j) - 1));
				DTXSID_neighbor_inner.add(rtTraining.getDtxSid().get((int)pred.getNeighbors().getEntry(i, j) - 1));
				Exp_neighbor_inner.add("" + set.getY().get((int)pred.getNeighbors().getEntry(i, j) - 1));
				pred_neighbor_inner.add("" + rtTraining.getModel().getYc().get((int)pred.getNeighbors().getEntry(i, j) - 1));
			}
			CAS_neighbor.add(CAS_neighbor_inner);
			DTXSID_neighbor.add(DTXSID_neighbor_inner);
			Exp_neighbor.add(Exp_neighbor_inner);
			pred_neighbor.add(pred_neighbor_inner);

			boolean nanFound = false;
			List<Double> pred_dc_array = new ArrayList<>();
			List<Double> pred_w_array = new ArrayList<>();
			for(int j = 0; j < pred.getDc().getColumnDimension(); j++) {
				if(!Double.isNaN(pred.getDc().getEntry(i, j))) {
					pred_dc_array.add(pred.getDc().getEntry(i, j) + 1);
					pred_w_array.add(pred.getW().getEntry(i, j));
				} else {
					nanFound = true;
				}
			}
			
			if(nanFound) {
				pred.getClassPredW().setEntry(i, 0, Double.NaN);
				ADRT.setEntry(i, 0.0);
				AD_index_RT.setEntry(i, 0.0);
				Conf_index_RT.setEntry(i, 0.0);
			} else {
				AD_index_RT.setEntry(i, 1.0 / Utils.vectorSum(Utils.vectorFromList(pred_dc_array).ebeMultiply(Utils.vectorFromList(pred_w_array))));
				Conf_index_RT.setEntry(i, calculateConfIndex(pred, Utils.vectorFromStringList(pred_neighbor.get(i)), Utils.vectorFromStringList(Exp_neighbor.get(i)), AD_index_RT, i));
			}
			//Add back in for 2.4
//			if(pls.getEntry(i, 0) < 0) {
//				pls.setEntry(i, 0, 0.0);
//				ADRT.setEntry(i, 0.0);
//			}
			
			//Populate results
			DefaultModelResult result = new DefaultModelResult();
			result.setChemicalDescriptors(descriptors.get(i));
			result.setPredictedValue(pls.getEntry(i, 0));
			result.setAdModel((int)ADRT.getEntry(i));
			result.setAdIndex(AD_index_RT.getEntry(i));
			result.setConfidenceIndex(Conf_index_RT.getEntry(i));
			if(neighbors) {
				result.setCasNeighbor(CAS_neighbor.get(i));
				result.setDtxsidNeighbor(DTXSID_neighbor.get(i));
				result.setExpNeighbor(Exp_neighbor.get(i));
				result.setPredNeighbor(pred_neighbor.get(i));
			}
			
			results.add(result);
		}
		
		return results;
	}

	@Override
	public Double calculateConfIndex(Prediction pred, RealVector predNeighbor, RealVector expNeighbor, RealVector ADIndex, int i) {
		return (1.0 / (1 + Math.sqrt(Utils.vectorSum(Utils.expVector(expNeighbor.subtract(predNeighbor), 2)
				.ebeMultiply(pred.getW().getRowVector(i)))) / 4.5) + ADIndex.getEntry(i)) / 2.0;
	}
}
