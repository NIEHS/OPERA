package com.sciome.opera.calc.calculators;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import com.sciome.opera.calc.math.ClassicalLeverage;
import com.sciome.opera.calc.math.KnnPred;
import com.sciome.opera.calc.math.Utils;
import com.sciome.opera.descriptors.OPERAChemicalDescriptors;
import com.sciome.opera.descriptors.OPERADescriptorValue;
import com.sciome.opera.enums.DistanceType;
import com.sciome.opera.enums.Models;
import com.sciome.opera.enums.PretreatmentType;
import com.sciome.opera.libSVM_java.libsvm.svm;
import com.sciome.opera.libSVM_java.libsvm.svm_node;
import com.sciome.opera.model.ClassicalLeverageResult;
import com.sciome.opera.model.Prediction;
import com.sciome.opera.model.results.OperaModelResult;
import com.sciome.opera.model.results.PKAModelResult;
import com.sciome.opera.model.training.model.pKa.PKASet;
import com.sciome.opera.model.training.model.pKa.PKATraining;

public class PKACalculator extends ModelCalculator {
	private PKATraining pkaTraining;
	
	public PKACalculator() {
		this.pkaTraining = PKATraining.getInstance();
		this.model = Models.PKA;
	}
	
	@Override
	public List<OperaModelResult> calc(List<OPERAChemicalDescriptors> descriptors, boolean neighbors, boolean exp) {
		List<OperaModelResult> resultList = new ArrayList<>();
		double[][] XtestArray = new double[descriptors.size()][pkaTraining.getDesc().size()];
		for (int i = 0; i < XtestArray.length; i++) {
			for (int j = 0; j < XtestArray[i].length; j++) {
				XtestArray[i][j] = Double.valueOf(descriptors.get(i).getValueWithPadelPreference(pkaTraining.getDesc().get(j)));
			}
		}
		RealMatrix Xtest = MatrixUtils.createRealMatrix(XtestArray);
		
		double[][] XtestaArray = new double[descriptors.size()][pkaTraining.getDesc_a().size()];
		for (int i = 0; i < XtestaArray.length; i++) {
			for (int j = 0; j < XtestaArray[i].length; j++) {
				XtestaArray[i][j] = Double.valueOf(descriptors.get(i).getValueWithPadelPreference(pkaTraining.getDesc_a().get(j)));
			}
		}
		RealMatrix XtestA= MatrixUtils.createRealMatrix(XtestaArray);
		
		double[][] XtestbArray = new double[descriptors.size()][pkaTraining.getDesc_b().size()];
		for (int i = 0; i < XtestbArray.length; i++) {
			for (int j = 0; j < XtestbArray[i].length; j++) {
				XtestbArray[i][j] = Double.valueOf(descriptors.get(i).getValueWithPadelPreference(pkaTraining.getDesc_b().get(j)));
			}
		}
		RealMatrix XtestB = MatrixUtils.createRealMatrix(XtestbArray);
		
		PKASet set = pkaTraining.getModel().getSet();
		Prediction pred = KnnPred.predict(Xtest, Utils.matrixFromList(set.getTrain()), Utils.vectorFromIntegerList(set.getClazz()), set.getK(), 
											DistanceType.findByName(set.getDist_type()), set.getParam().getPret_type());
		
		ClassicalLeverageResult AD = ClassicalLeverage.classicalLeverage(Utils.matrixFromList(set.getTrain()), Xtest, PretreatmentType.auto);
		
		RealVector AD_Model = Utils.absVector(AD.getInorout().mapSubtractToSelf(1.0));
		
		//Add back in for 2.4
//		for(int i = 0; i < pred.getDc().getRowDimension(); i++) {
//			if(pred.getDc().getEntry(i, 0) == 0)
//				AD_Model.setEntry(i, 1);
//		}
		
		RealVector AD_index = Utils.zeros(Xtest.getRowDimension());
		RealVector Conf_index = Utils.zeros(Xtest.getRowDimension());
		
		List<List<String>> CAS_neighbor = new ArrayList<>();
		List<List<String>> InChiKey_neighbor = new ArrayList<>();
		List<List<String>> DTXSID_neighbor = new ArrayList<>();
		List<List<String>> DSSTOXMPID_neighbor = new ArrayList<>();
		List<List<String>> Exp_neighbor = new ArrayList<>();
		List<List<String>> pred_neighbor = new ArrayList<>();
		
		for(int i = 0; i < descriptors.size(); i++) {
			double pKa_ac_ba_amp = pred.getClassPred().getEntry(0, i);
			
			svm_node[] node_a = new svm_node[XtestA.getColumnDimension()];
			for(int j = 0; j < node_a.length; j++) {
				svm_node node = new svm_node();
				node.index = j;
				node.value = XtestA.getEntry(i, j);
				node_a[j] = node;
			}
			svm_node[] node_b = new svm_node[XtestB.getColumnDimension()];
			for(int j = 0; j < node_b.length; j++) {
				svm_node node = new svm_node();
				node.index = j;
				node.value = XtestB.getEntry(i, j);
				node_b[j] = node;
			}
			
			double pKa_a = svm.svm_predict(pkaTraining.createModelA(), node_a);
			double pKa_b = svm.svm_predict(pkaTraining.createModelB(), node_b);
			int ionization = 0;
			
			List<OPERADescriptorValue> list = descriptors.get(i).getPadelDescriptorValues();
			if(Double.parseDouble(list.get(12).getValue()) + Double.parseDouble(list.get(13).getValue()) - 
					Double.parseDouble(list.get(730).getValue()) - Double.parseDouble(list.get(731).getValue()) == 0) {
				ionization = 0;
				pKa_ac_ba_amp = Double.NaN;
				pKa_a = Double.NaN;
				pKa_b = Double.NaN;
			} else {
				if(pred.getClassPred().getEntry(0, i) == 1) {
					pKa_b = Double.NaN;
					ionization = 1;
				} else if(pred.getClassPred().getEntry(0, i) == 2) {
					pKa_a = Double.NaN;
					ionization = 1;
				} if(pred.getClassPred().getEntry(0, i) == 3) {
					ionization = 2;
				}
			}
			
			List<String> CAS_neighbor_inner = new ArrayList<>();
			List<String> InChiKey_neighbor_inner = new ArrayList<>();
			List<String> DTXSID_neighbor_inner = new ArrayList<>();
			List<String> DSSTOXMPID_neighbor_inner = new ArrayList<>();
			List<String> Exp_neighbor_inner = new ArrayList<>();
			List<String> pred_neighbor_inner = new ArrayList<>();
			for(int j = 0; j < pred.getNeighbors().getColumnDimension(); j++) {
				CAS_neighbor_inner.add(pkaTraining.getCas().get((int)pred.getNeighbors().getEntry(i, j) - 1));
				InChiKey_neighbor_inner.add(pkaTraining.getInChiKey().get((int)pred.getNeighbors().getEntry(i, j) - 1));
				DTXSID_neighbor_inner.add(pkaTraining.getDtxsid().get((int)pred.getNeighbors().getEntry(i, j) - 1));
				Exp_neighbor_inner.add("" + set.getY().get((int)pred.getNeighbors().getEntry(i, j) - 1));
				pred_neighbor_inner.add("" + set.getYc().get((int)pred.getNeighbors().getEntry(i, j) - 1));
			}
			CAS_neighbor.add(CAS_neighbor_inner);
			InChiKey_neighbor.add(InChiKey_neighbor_inner);
			DTXSID_neighbor.add(DTXSID_neighbor_inner);
			DSSTOXMPID_neighbor.add(DSSTOXMPID_neighbor_inner);
			Exp_neighbor.add(Exp_neighbor_inner);
			pred_neighbor.add(pred_neighbor_inner);

			List<Double> pred_dc_array = new ArrayList<>();
			List<Double> pred_w_array = new ArrayList<>();
			boolean nanFound = false;
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
				AD_Model.setEntry(i, 0.0);
				AD_index.setEntry(i, 0.0);
			} else {
				AD_index.setEntry(i, 1.0 / Utils.vectorSum(Utils.vectorFromList(pred_dc_array).ebeMultiply(Utils.vectorFromList(pred_w_array))));
			}
			
			Conf_index.setEntry(i, calculateConfIndex(pred, Utils.vectorFromStringList(pred_neighbor.get(i)), Utils.vectorFromStringList(Exp_neighbor.get(i)), AD_index, i));
			
			//Populate results
			PKAModelResult result = new PKAModelResult();
			result.setChemicalDescriptors(descriptors.get(i));
			result.setpKa_ac_ba_amp(pKa_ac_ba_amp);
			result.setpKa_a_pred(pKa_a);
			result.setpKa_b_pred(pKa_b);
			result.setIonization(ionization);
			result.setAdModel((int)AD_Model.getEntry(i));
			result.setAdIndex(AD_index.getEntry(i));
			result.setConfidenceIndex(Conf_index.getEntry(i));
			if(neighbors) {
				result.setCasNeighbor(CAS_neighbor.get(i));
				result.setInchiNeighbor(InChiKey_neighbor.get(i));
				result.setDtxsidNeighbor(DTXSID_neighbor.get(i));
				result.setDsstoxmpidNeighbor(DSSTOXMPID_neighbor.get(i));
				result.setExpNeighbor(Exp_neighbor.get(i));
				result.setPredNeighbor(pred_neighbor.get(i));
			}
//			if(exp) {
//				result.setpKa_a_exp(pKa_a_exp.getEntry(i));
//				result.setpKa_b_exp(pKa_b_exp.getEntry(i));
//			}
			
			resultList.add(result);
		}
		
		return resultList;
	}

	@Override
	public Double calculateConfIndex(Prediction pred, RealVector predNeighbor, RealVector expNeighbor,
			RealVector ADIndex, int i) {
		return (1.0 / (1 + Math.sqrt(Utils.vectorSum(Utils.expVector(expNeighbor.subtract(predNeighbor), 2)
				.ebeMultiply(pred.getW().getRowVector(i))))) + ADIndex.getEntry(i)) / 2.0;
	}
}
