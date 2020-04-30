package com.sciome.opera.calc.calculators;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import com.sciome.opera.calc.math.ClassicalLeverage;
import com.sciome.opera.calc.math.KnnPred2;
import com.sciome.opera.calc.math.NNRPred2;
import com.sciome.opera.calc.math.TestPretreatment;
import com.sciome.opera.calc.math.Utils;
import com.sciome.opera.descriptors.OPERAChemicalDescriptors;
import com.sciome.opera.enums.DistanceType;
import com.sciome.opera.enums.Models;
import com.sciome.opera.enums.PretreatmentType;
import com.sciome.opera.model.ClassicalLeverageResult;
import com.sciome.opera.model.Prediction;
import com.sciome.opera.model.results.CATMOSModelResult;
import com.sciome.opera.model.results.DefaultModelResult;
import com.sciome.opera.model.results.OperaModelResult;
import com.sciome.opera.model.training.model.CATMoS.CATMOSLD50Model;
import com.sciome.opera.model.training.model.CATMoS.CATMOSLD50Set;
import com.sciome.opera.model.training.model.CATMoS.CATMOSModel;
import com.sciome.opera.model.training.model.CATMoS.CATMOSSet;
import com.sciome.opera.model.training.model.CATMoS.CATMOSTraining;

public class CATMOSCalculator extends ModelCalculator {
	private CATMOSTraining catmosTraining;
	
	public CATMOSCalculator() {
		this.catmosTraining = CATMOSTraining.getInstance();
		this.model = Models.CATMOS;
	}
	
	@Override
	public List<OperaModelResult> calc(List<OPERAChemicalDescriptors> descriptors, boolean neighbors, boolean exp) {
		List<OperaModelResult> result = new ArrayList<>();
		
		double[][] XtestVTArray = new double[descriptors.size()][catmosTraining.getModel_VT().getDesc().size()];
		for (int i = 0; i < XtestVTArray.length; i++) {
			for (int j = 0; j < XtestVTArray[i].length; j++) {
				XtestVTArray[i][j] = Double.valueOf(descriptors.get(i).getValueWithCDK2Preference(catmosTraining.getModel_VT().getDesc().get(j)));
			}
		}
		RealMatrix XtestVT = MatrixUtils.createRealMatrix(XtestVTArray);
		List<DefaultModelResult> vtResult = calculateCatmosResult(XtestVT, catmosTraining.getModel_VT(), neighbors, true);
		
		double[][] XtestNTArray = new double[descriptors.size()][catmosTraining.getModel_NT().getDesc().size()];
		for (int i = 0; i < XtestNTArray.length; i++) {
			for (int j = 0; j < XtestNTArray[i].length; j++) {
				XtestNTArray[i][j] = Double.valueOf(descriptors.get(i).getValueWithCDK2Preference(catmosTraining.getModel_NT().getDesc().get(j)));
			}
		}
		RealMatrix XtestNT = MatrixUtils.createRealMatrix(XtestNTArray);
		List<DefaultModelResult> ntResult = calculateCatmosResult(XtestNT, catmosTraining.getModel_NT(), neighbors, true);
		
		double[][] XtestEPAArray = new double[descriptors.size()][catmosTraining.getModel_EPA().getDesc().size()];
		for (int i = 0; i < XtestEPAArray.length; i++) {
			for (int j = 0; j < XtestEPAArray[i].length; j++) {
				XtestEPAArray[i][j] = Double.valueOf(descriptors.get(i).getValueWithCDK2Preference(catmosTraining.getModel_EPA().getDesc().get(j)));
			}
		}
		RealMatrix XtestEPA = MatrixUtils.createRealMatrix(XtestEPAArray);
		List<DefaultModelResult> epaResult = calculateCatmosResult(XtestEPA, catmosTraining.getModel_EPA(), neighbors, false);
		
		double[][] XtestGHSArray = new double[descriptors.size()][catmosTraining.getModel_GHS().getDesc().size()];
		for (int i = 0; i < XtestGHSArray.length; i++) {
			for (int j = 0; j < XtestGHSArray[i].length; j++) {
				XtestGHSArray[i][j] = Double.valueOf(descriptors.get(i).getValueWithCDK2Preference(catmosTraining.getModel_GHS().getDesc().get(j)));
			}
		}
		RealMatrix XtestGHS = MatrixUtils.createRealMatrix(XtestGHSArray);
		List<DefaultModelResult> ghsResult = calculateCatmosResult(XtestGHS, catmosTraining.getModel_GHS(), neighbors, false);
		
		double[][] XtestLD50Array = new double[descriptors.size()][catmosTraining.getModel_LD50().getDesc().size()];
		for (int i = 0; i < XtestLD50Array.length; i++) {
			for (int j = 0; j < XtestLD50Array[i].length; j++) {
				XtestLD50Array[i][j] = Double.valueOf(
						descriptors.get(i).getValueWithCDK2Preference(catmosTraining.getModel_LD50().getDesc().get(j)));
			}
		}
		RealMatrix XtestLD50 = MatrixUtils.createRealMatrix(XtestLD50Array);
		List<DefaultModelResult> ld50Result = calculateLD50Result(XtestLD50, catmosTraining.getModel_LD50(), neighbors);
		
		for(int i = 0; i < descriptors.size(); i++) {
			CATMOSModelResult catmosResult = new CATMOSModelResult();
			catmosResult.setChemicalDescriptors(descriptors.get(i));

			vtResult.get(i).setChemicalDescriptors(descriptors.get(i));
			ntResult.get(i).setChemicalDescriptors(descriptors.get(i));
			epaResult.get(i).setChemicalDescriptors(descriptors.get(i));
			ghsResult.get(i).setChemicalDescriptors(descriptors.get(i));
			ld50Result.get(i).setChemicalDescriptors(descriptors.get(i));
			
			catmosResult.setVt(vtResult.get(i));
			catmosResult.setNt(ntResult.get(i));
			catmosResult.setEpa(epaResult.get(i));
			catmosResult.setGhs(ghsResult.get(i));
			catmosResult.setLd50(ld50Result.get(i));
			
			result.add(catmosResult);
		}
		
		return result;
	}

	@Override
	public Double calculateConfIndex(Prediction pred, RealVector predNeighbor, RealVector expNeighbor,
			RealVector ADIndex, int i) {
		return null;
	}
	
	private List<DefaultModelResult> calculateCatmosResult(RealMatrix Xtest, CATMOSModel model, boolean neighbors, boolean subtract) {
		List<DefaultModelResult> results = new ArrayList<>();
		
		CATMOSSet set = model.getSet();
		Prediction pred = KnnPred2.predict(Xtest, Utils.matrixFromList(set.getTrain()), Utils.vectorFromIntegerList(set.getClazz()),
				set.getK(), DistanceType.findByName(set.getDist_type()), set.getParam().getPret_type());
		RealMatrix CATMOS_pred = pred.getClassPred();
		if(subtract)
			CATMOS_pred = CATMOS_pred.scalarAdd(-1.0);
		ClassicalLeverageResult AD_AG = ClassicalLeverage.classicalLeverage(Utils.matrixFromList(set.getTrain()), Xtest, PretreatmentType.auto);
		RealVector AD_CATMOS = Utils.absVector(AD_AG.getInorout().mapSubtract(1.0));
		RealMatrix AD_index_CATMOS = TestPretreatment.test(pred.getDc().getColumnMatrix(0), set.getDc_param()).scalarMultiply(-1.0).scalarAdd(1.0);
		for(int i = 0; i < AD_index_CATMOS.getRowDimension(); i++) {
			for(int j = 0; j < AD_index_CATMOS.getColumnDimension(); j++) {
				if(AD_index_CATMOS.getEntry(i, j) < 0) {
					AD_index_CATMOS.setEntry(i, j, 1 / (1 + pred.getDc().getEntry(i, j)));
				}
			}
		}
		for(int i = 0; i < pred.getDc().getRowDimension(); i++) {
			for(int j = 0; j < pred.getDc().getColumnDimension(); j++) {
				if(pred.getDc().getEntry(i, j) == Double.NaN) {
					CATMOS_pred.setEntry(i, j, Double.NaN);
					AD_CATMOS.setEntry(i, 0);
					AD_index_CATMOS.setEntry(i, j, 0);
				}
			}
		}
		for(int i = 0; i < AD_index_CATMOS.getRowDimension(); i++) {
			for(int j = 0; j < AD_index_CATMOS.getColumnDimension(); j++) {
				if(AD_index_CATMOS.getEntry(i, j) > .5) {
					AD_CATMOS.setEntry(i, 1);
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
			
			result.setPredictedValue(CATMOS_pred.getEntry(i, 0));
			result.setAdIndex(AD_index_CATMOS.getEntry(i, 0));
			result.setAdModel((int)AD_CATMOS.getEntry(i));
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
					if(AD_index_CATMOS.getEntry(i, 0) != 0) {
						CAS_neighbor_inner.add(model.getCAS().get((int)pred.getNeighbors().getEntry(i, j) - 1));
						InChiKey_neighbor_inner.add(model.getInChiKey().get((int)pred.getNeighbors().getEntry(i, j) - 1));
						DTXSID_neighbor_inner.add(model.getDtxSid().get((int)pred.getNeighbors().getEntry(i, j) - 1));
						Exp_neighbor_inner.add("" + set.getClazz().get((int)pred.getNeighbors().getEntry(i, j) - 1));
						pred_neighbor_inner.add("" + set.getClazz().get((int)pred.getNeighbors().getEntry(i, j) - 1));
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
	
	private List<DefaultModelResult> calculateLD50Result(RealMatrix Xtest, CATMOSLD50Model model, boolean neighbors) {
		List<DefaultModelResult> results = new ArrayList<>();
		
		CATMOSLD50Set set = model.getSet();
		Prediction pred = NNRPred2.predict(Xtest, Utils.matrixFromList(set.getTrain()), Utils.vectorFromList(set.getY()),
				set.getK(), DistanceType.findByName(set.getDist_type()), set.getParam().getPret_type());
		RealMatrix CATMOS_pred = pred.getClassPredW();
		ClassicalLeverageResult AD_AG = ClassicalLeverage.classicalLeverage(Utils.matrixFromList(set.getTrain()), Xtest, PretreatmentType.auto);
		RealVector AD_CATMOS = Utils.absVector(AD_AG.getInorout().mapSubtract(1.0));
		RealMatrix AD_index_CATMOS = TestPretreatment.test(pred.getDc().getColumnMatrix(0), set.getDc_param()).scalarMultiply(-1.0).scalarAdd(1.0);
		for(int i = 0; i < AD_index_CATMOS.getRowDimension(); i++) {
			for(int j = 0; j < AD_index_CATMOS.getColumnDimension(); j++) {
				if(AD_index_CATMOS.getEntry(i, j) < 0) {
					AD_index_CATMOS.setEntry(i, j, 1 / (1 + pred.getDc().getEntry(i, j)));
				}
			}
		}
		for(int i = 0; i < pred.getDc().getRowDimension(); i++) {
			for(int j = 0; j < pred.getDc().getColumnDimension(); j++) {
				if(pred.getDc().getEntry(i, j) == Double.NaN) {
					CATMOS_pred.setEntry(i, j, Double.NaN);
					AD_CATMOS.setEntry(i, 0);
					AD_index_CATMOS.setEntry(i, j, 0);
				}
			}
		}
		for(int i = 0; i < AD_index_CATMOS.getRowDimension(); i++) {
			for(int j = 0; j < AD_index_CATMOS.getColumnDimension(); j++) {
				if(AD_index_CATMOS.getEntry(i, j) > .5) {
					AD_CATMOS.setEntry(i, 1);
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
			
			result.setPredictedValue(CATMOS_pred.getEntry(i, 0));
			result.setAdIndex(AD_index_CATMOS.getEntry(i, 0));
			result.setAdModel((int)AD_CATMOS.getEntry(i));
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
					if(AD_index_CATMOS.getEntry(i, 0) != 0) {
						CAS_neighbor_inner.add(model.getCAS().get((int)pred.getNeighbors().getEntry(i, j) - 1));
						InChiKey_neighbor_inner.add(model.getInChiKey().get((int)pred.getNeighbors().getEntry(i, j) - 1));
						DTXSID_neighbor_inner.add(model.getDtxSid().get((int)pred.getNeighbors().getEntry(i, j) - 1));
						Exp_neighbor_inner.add(set.getY_exp().get((int)pred.getNeighbors().getEntry(i, j) - 1));
						pred_neighbor_inner.add("" + set.getY().get((int)pred.getNeighbors().getEntry(i, j) - 1));
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
