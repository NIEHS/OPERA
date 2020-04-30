package com.sciome.opera.calc.calculators;

import java.util.List;

import org.apache.commons.math3.linear.RealVector;

import com.sciome.opera.calc.math.Utils;
import com.sciome.opera.descriptors.OPERAChemicalDescriptors;
import com.sciome.opera.enums.Models;
import com.sciome.opera.model.Prediction;
import com.sciome.opera.model.results.OperaModelResult;
import com.sciome.opera.model.training.model.HLTraining;

public class HLCalculator extends ModelCalculator {
	public HLCalculator()
	{
		this.training = HLTraining.getInstance();
		this.model = Models.HL;
	}
	
	@Override
	public List<OperaModelResult> calc(List<OPERAChemicalDescriptors> descriptors, boolean neighbors, boolean exp) {
		return this.genericCalc(descriptors, neighbors, exp);
	}

	@Override
	public Double calculateConfIndex(Prediction pred, RealVector predNeighbor, RealVector expNeighbor,
			RealVector ADIndex, int i) {
		return (1.0 / (1 + Math.sqrt(Utils.vectorSum(Utils.expVector(expNeighbor.subtract(predNeighbor), 2)
				.ebeMultiply(pred.getW().getRowVector(i))))) + ADIndex.getEntry(i)) / 2.0;
	}
}
