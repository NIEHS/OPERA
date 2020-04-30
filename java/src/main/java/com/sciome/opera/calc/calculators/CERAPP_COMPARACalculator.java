package com.sciome.opera.calc.calculators;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import com.sciome.opera.calc.math.ClassicalLeverage;
import com.sciome.opera.calc.math.KnnPred2;
import com.sciome.opera.calc.math.TestPretreatment;
import com.sciome.opera.calc.math.Utils;
import com.sciome.opera.descriptors.OPERAChemicalDescriptors;
import com.sciome.opera.enums.DistanceType;
import com.sciome.opera.enums.Models;
import com.sciome.opera.enums.PretreatmentType;
import com.sciome.opera.model.ClassicalLeverageResult;
import com.sciome.opera.model.Prediction;
import com.sciome.opera.model.results.CERAPP_COMPARAModelResult;
import com.sciome.opera.model.results.DefaultModelResult;
import com.sciome.opera.model.results.OperaModelResult;
import com.sciome.opera.model.training.model.CERAPP_COMPARA.CERAPPTraining;
import com.sciome.opera.model.training.model.CERAPP_COMPARA.CERAPP_COMPARAModel;
import com.sciome.opera.model.training.model.CERAPP_COMPARA.CERAPP_COMPARASet;
import com.sciome.opera.model.training.model.CERAPP_COMPARA.CERAPP_COMPARATraining;
import com.sciome.opera.model.training.model.CERAPP_COMPARA.COMPARATraining;

public class CERAPP_COMPARACalculator extends ModelCalculator {
	private CERAPP_COMPARATraining cTraining;
	
	public CERAPP_COMPARACalculator(boolean cerapp) {
		if(cerapp) {
			cTraining = CERAPPTraining.getInstance();
			this.model = Models.CERAPP;
		} else {
			cTraining = COMPARATraining.getInstance();
			this.model = Models.COMPARA;
		}
	}
	
	@Override
	public List<OperaModelResult> calc(List<OPERAChemicalDescriptors> descriptors, boolean neighbors, boolean exp) {
		List<OperaModelResult> result = new ArrayList<>();
		
		double[][] XtestAGArray = new double[descriptors.size()][cTraining.getModel_ag().getDesc().size()];
		for (int i = 0; i < XtestAGArray.length; i++) {
			for (int j = 0; j < XtestAGArray[i].length; j++) {
				XtestAGArray[i][j] = Double.valueOf(descriptors.get(i).getValueWithCDK2Preference(cTraining.getModel_ag().getDesc().get(j)));
			}
		}
		RealMatrix XtestAG = MatrixUtils.createRealMatrix(XtestAGArray);
		List<DefaultModelResult> agoResult = calculateCerappResult(XtestAG, cTraining.getModel_ag(), neighbors);
		
		double[][] XtestANArray = new double[descriptors.size()][cTraining.getModel_an().getDesc().size()];
		for (int i = 0; i < XtestANArray.length; i++) {
			for (int j = 0; j < XtestANArray[i].length; j++) {
				XtestANArray[i][j] = Double.valueOf(descriptors.get(i).getValueWithCDK2Preference(cTraining.getModel_an().getDesc().get(j)));
			}
		}
		RealMatrix XtestAN = MatrixUtils.createRealMatrix(XtestANArray);
		List<DefaultModelResult> antaResult = calculateCerappResult(XtestAN, cTraining.getModel_an(), neighbors);
		
		double[][] XtestBDArray = new double[descriptors.size()][cTraining.getModel_bd().getDesc().size()];
		for (int i = 0; i < XtestBDArray.length; i++) {
			for (int j = 0; j < XtestBDArray[i].length; j++) {
				XtestBDArray[i][j] = Double.valueOf(descriptors.get(i).getValueWithCDK2Preference(cTraining.getModel_bd().getDesc().get(j)));
			}
		}
		RealMatrix XtestBD = MatrixUtils.createRealMatrix(XtestBDArray);
		List<DefaultModelResult> bindResult = calculateCerappResult(XtestBD, cTraining.getModel_bd(), neighbors);
		
		for(int i = 0; i < descriptors.size(); i++) {
			CERAPP_COMPARAModelResult cerappResult = new CERAPP_COMPARAModelResult();
			cerappResult.setChemicalDescriptors(descriptors.get(i));
			
			agoResult.get(i).setChemicalDescriptors(descriptors.get(i));
			antaResult.get(i).setChemicalDescriptors(descriptors.get(i));
			bindResult.get(i).setChemicalDescriptors(descriptors.get(i));
			
			cerappResult.setAgo(agoResult.get(i));
			cerappResult.setAnta(antaResult.get(i));
			cerappResult.setBind(bindResult.get(i));
			
			result.add(cerappResult);
		}
		
		return result;
	}

	//This function is not used because the Confidence index is calculated using much different parameters
	@Override
	public Double calculateConfIndex(Prediction pred, RealVector predNeighbor, RealVector expNeighbor,
			RealVector ADIndex, int i) {
		return null;
	}
	
	private List<DefaultModelResult> calculateCerappResult(RealMatrix Xtest, CERAPP_COMPARAModel model, boolean neighbors) {
		List<DefaultModelResult> results = new ArrayList<>();
		
		CERAPP_COMPARASet set = model.getSet();
		Prediction pred = KnnPred2.predict(Xtest, Utils.matrixFromList(set.getTrain()), Utils.vectorFromIntegerList(set.getClazz()),
				set.getK(), DistanceType.findByName(set.getDist_type()), set.getParam().getPret_type());
		RealMatrix CERAPP_pred = pred.getClassPred().scalarAdd(-1.0);
		ClassicalLeverageResult AD_AG = ClassicalLeverage.classicalLeverage(Utils.matrixFromList(set.getTrain()), Xtest, PretreatmentType.auto);
		RealVector AD_CERAPP = Utils.absVector(AD_AG.getInorout().mapSubtract(1.0));
		RealMatrix AD_index_CERAPP = TestPretreatment.test(pred.getDc().getColumnMatrix(0), set.getDc_param()).scalarMultiply(-1.0).scalarAdd(1.0);
		for(int i = 0; i < AD_index_CERAPP.getRowDimension(); i++) {
			for(int j = 0; j < AD_index_CERAPP.getColumnDimension(); j++) {
				if(AD_index_CERAPP.getEntry(i, j) < 0) {
					AD_index_CERAPP.setEntry(i, j, 1.0 / (1 + pred.getDc().getEntry(i, j)));
				}
			}
		}
		for(int i = 0; i < pred.getDc().getRowDimension(); i++) {
			for(int j = 0; j < pred.getDc().getColumnDimension(); j++) {
				if(pred.getDc().getEntry(i, j) == Double.NaN) {
					CERAPP_pred.setEntry(i, j, Double.NaN);
					AD_CERAPP.setEntry(i, 0);
					AD_index_CERAPP.setEntry(i, j, 0);
				}
			}
		}
		for(int i = 0; i < AD_index_CERAPP.getRowDimension(); i++) {
			for(int j = 0; j < AD_index_CERAPP.getColumnDimension(); j++) {
				if(AD_index_CERAPP.getEntry(i, j) > .5) {
					AD_CERAPP.setEntry(i, 1);
				}
			}
		}
		
		List<List<String>> CAS_neighbor = new ArrayList<>();
		List<List<String>> InChiKey_neighbor = new ArrayList<>();
		List<List<String>> DTXSID_neighbor = new ArrayList<>();
		List<List<String>> DSSTOXMPID_neighbor = new ArrayList<>();
		List<List<String>> Exp_neighbor = new ArrayList<>();
		List<List<String>> pred_neighbor = new ArrayList<>();
		
		for(int i = 0; i < Xtest.getRowDimension(); i++) {
			DefaultModelResult result = new DefaultModelResult();
			double[] concArray = new double[pred.getNeighbors().getColumnDimension()];
			for(int j = 0; j < pred.getNeighbors().getColumnDimension(); j++) {
				concArray[j] = model.getConc().get((int)pred.getNeighbors().getEntry(i, j) - 1);
			}
			double confIndex = MatrixUtils.createRowRealMatrix(concArray).multiply(pred.getW().getRowMatrix(i).transpose()).getEntry(0, 0);
			
			result.setPredictedValue(CERAPP_pred.getEntry(i, 0));
			result.setAdIndex(AD_index_CERAPP.getEntry(i, 0));
			result.setAdModel((int)AD_CERAPP.getEntry(i));
			result.setConfidenceIndex(confIndex);
			
			//Add neighbors to the result if asked
			if(neighbors) {
				List<String> CAS_neighbor_inner = new ArrayList<>();
				List<String> InChiKey_neighbor_inner = new ArrayList<>();
				List<String> DTXSID_neighbor_inner = new ArrayList<>();
				List<String> DSSTOXMPID_neighbor_inner = new ArrayList<>();
				List<String> Exp_neighbor_inner = new ArrayList<>();
				List<String> pred_neighbor_inner = new ArrayList<>();
				for(int j = 0; j < pred.getNeighbors().getColumnDimension(); j++) {
					if(AD_index_CERAPP.getEntry(i, 0) != 0) {
						CAS_neighbor_inner.add(model.getCas().get((int)pred.getNeighbors().getEntry(i, j) - 1));
						InChiKey_neighbor_inner.add(model.getInChiKey().get((int)pred.getNeighbors().getEntry(i, j) - 1));
						DTXSID_neighbor_inner.add(model.getDtxsid().get((int)pred.getNeighbors().getEntry(i, j) - 1));
		//				DSSTOXMPID_neighbor_inner.add(model.getDssToxMpId().get((int)pred.getNeighbors().getEntry(i, j) - 1));
						Exp_neighbor_inner.add(set.getClass_exp().get((int)pred.getNeighbors().getEntry(i, j) - 1));
						pred_neighbor_inner.add(set.getClass_s().get((int)pred.getNeighbors().getEntry(i, j) - 1));
					}
				}
				CAS_neighbor.add(CAS_neighbor_inner);
				InChiKey_neighbor.add(InChiKey_neighbor_inner);
				DTXSID_neighbor.add(DTXSID_neighbor_inner);
				DSSTOXMPID_neighbor.add(DSSTOXMPID_neighbor_inner);
				Exp_neighbor.add(Exp_neighbor_inner);
				pred_neighbor.add(pred_neighbor_inner);
				
				result.setCasNeighbor(CAS_neighbor.get(i));
				result.setInchiNeighbor(InChiKey_neighbor.get(i));
				result.setDtxsidNeighbor(DTXSID_neighbor.get(i));
				result.setExpNeighbor(Exp_neighbor.get(i));
				result.setPredNeighbor(pred_neighbor.get(i));
			}
			
			
			
			
			results.add(result);
		}
		
		return results;
	}
}
