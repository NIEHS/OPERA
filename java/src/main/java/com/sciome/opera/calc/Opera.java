package com.sciome.opera.calc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sciome.opera.calc.calculators.LogDCalculator;
import com.sciome.opera.calc.calculators.ModelCalculator;
import com.sciome.opera.calc.calculators.ModelFactory;
import com.sciome.opera.descriptors.ChemicalDescriptorTool;
import com.sciome.opera.descriptors.OPERAChemicalDescriptors;
import com.sciome.opera.enums.DescriptorCalculateEnum;
import com.sciome.opera.enums.Models;
import com.sciome.opera.model.results.CATMOSModelResult;
import com.sciome.opera.model.results.CERAPP_COMPARAModelResult;
import com.sciome.opera.model.results.DefaultModelResult;
import com.sciome.opera.model.results.OperaModelResult;
import com.sciome.opera.model.results.OperaResult;
import com.sciome.opera.model.results.PKAModelResult;

public class Opera
{
	/**
	 * Main calculation method
	 * 
	 * @param smile
	 *            String text of smiles data from either a .smi file or
	 * @param models
	 *            A set of models that you want to calculate
	 * @param neighbors
	 *            Include neighbor output in the output or not
	 * @param exp
	 *            Include experimental values in the output or not (not implemented)
	 * @return A map from models to result objects
	 */
	public OperaResult calc(String smile, Set<Models> models, boolean neighbors)
	{
		DescriptorCalculateEnum descriptorMethod = howToCalculateDescriptors(models);
		ChemicalDescriptorTool tool = new ChemicalDescriptorTool();
		List<OPERAChemicalDescriptors> descriptors = null;
		try
		{
			if (descriptorMethod.equals(DescriptorCalculateEnum.PADEL))
				descriptors = tool.getDescriptors(smile, false, false);
			else if (descriptorMethod.equals(DescriptorCalculateEnum.PADEL_FINGERPRINT))
				descriptors = tool.getDescriptors(smile, true, false);
			else if (descriptorMethod.equals(DescriptorCalculateEnum.PADEL_CDK))
				descriptors = tool.getDescriptors(smile, false, true);
			else if (descriptorMethod.equals(DescriptorCalculateEnum.PADEL_FINGERPRINT_CDK))
				descriptors = tool.getDescriptors(smile, true, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ModelFactory factory = new ModelFactory();
		Set<ModelCalculator> calculators = factory.getModelCalculator(models);
		Map<Models, List<OperaModelResult>> combinedOutput = new HashMap<>();
		for (ModelCalculator calculator : calculators)
		{
			if(!(calculator instanceof LogDCalculator)) {
				List<OperaModelResult> output = calculator.calc(descriptors, neighbors, false);
				combinedOutput.put(calculator.getModel(), output);
			}
		}
		if(models.contains(Models.LOGD)) {
			LogDCalculator calculator = new LogDCalculator();
			calculator.setpKaResults(combinedOutput.get(Models.PKA));
			calculator.setlogPResults(combinedOutput.get(Models.LOGP));
			List<OperaModelResult> output = calculator.calc(descriptors, neighbors, false);
			combinedOutput.put(calculator.getModel(), output);
		}
		return new OperaResult(combinedOutput);
	}

	private DescriptorCalculateEnum howToCalculateDescriptors(Set<Models> models)
	{
		boolean requiresCDK = false;
		boolean requiresFingerprint = false;
		for (Models model : models)
		{
			if (model.equals(Models.PKA) || model.equals(Models.LOGD))
				requiresFingerprint = true;
			if(model.equals(Models.CERAPP) || model.equals(Models.COMPARA) || model.equals(Models.CATMOS)) {
				requiresCDK = true;
			}
		}
		
		if(requiresFingerprint && !requiresCDK)
			return DescriptorCalculateEnum.PADEL_FINGERPRINT;
		else if(requiresCDK && !requiresFingerprint)
			return DescriptorCalculateEnum.PADEL_CDK;
		else if(requiresCDK && requiresFingerprint)
			return DescriptorCalculateEnum.PADEL_FINGERPRINT_CDK;
		else
			return DescriptorCalculateEnum.PADEL;
	}
	
	public static void writeOuputTofile(Map<Models, List<OperaModelResult>> output, File file)
	{
		StringBuilder full = new StringBuilder();
		for (Models model : output.keySet())
		{
			StringBuilder header = new StringBuilder();
			StringBuilder data = new StringBuilder();
			boolean headerString = true;
			for (OperaModelResult result : output.get(model))
			{
				if (result instanceof DefaultModelResult)
				{
					DefaultModelResult defaultResult = (DefaultModelResult) result;
				}
				else if (result instanceof PKAModelResult)
				{
					PKAModelResult defaultResult = (PKAModelResult) result;
				}
				else if (result instanceof CERAPP_COMPARAModelResult)
				{
					CERAPP_COMPARAModelResult cerappResult = (CERAPP_COMPARAModelResult) result;
					if (headerString)
					{
						header.append(cerappResult.getHeaderWithModel(model));
						header.append("\n");
						headerString = false;
					}
					data.append(cerappResult.toString());
				}
				else if (result instanceof CATMOSModelResult)
				{
					CATMOSModelResult catmosResult = (CATMOSModelResult) result;
					if (headerString)
					{
						header.append(catmosResult.getHeaderWithModel(model));
						header.append("\n");
						headerString = false;
					}
					data.append(catmosResult.toString());
				}
				data.append("\n");
			}
			full.append(header.toString());
			full.append(data.toString());
			full.append("\n\n");
		}

		// Write data to file
		try
		{
			FileWriter writer = new FileWriter(file);
			writer.write(full.toString());
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void printOutputToConsole(Map<Models, List<OperaModelResult>> output)
	{
		for (Models model : output.keySet())
		{
			for (OperaModelResult result : output.get(model))
			{
				if (result instanceof DefaultModelResult)
				{
					DefaultModelResult defaultResult = (DefaultModelResult) result;
					System.out.println(result.getChemicalDescriptors().getChemID());
					System.out.println(defaultResult.getPredictedValue());
					System.out.println("ad model: " + defaultResult.getAdModel());
					System.out.println("ad index: " + defaultResult.getAdIndex());
					System.out.println("conf index: " + defaultResult.getConfidenceIndex());
				}
				else if (result instanceof PKAModelResult)
				{
					PKAModelResult defaultResult = (PKAModelResult) result;
					System.out.println(result.getChemicalDescriptors().getChemID());
					System.out.println("pKa pred a: " + defaultResult.getpKa_a_pred());
					System.out.println("pKa pred b: " + defaultResult.getpKa_b_pred());
					System.out.println("ionization: " + defaultResult.getIonization());
					System.out.println("ad model: " + defaultResult.getAdModel());
					System.out.println("ad index: " + defaultResult.getAdIndex());
					System.out.println("conf index: " + defaultResult.getConfidenceIndex());
				}
				else if (result instanceof CERAPP_COMPARAModelResult)
				{
					CERAPP_COMPARAModelResult cerappResult = (CERAPP_COMPARAModelResult) result;
					System.out.println("Ago Conf Index: " + cerappResult.getAgo().getConfidenceIndex());
					System.out.println("Anta Conf Index: " + cerappResult.getAnta().getConfidenceIndex());
					System.out.println("Bind Conf Index: " + cerappResult.getBind().getConfidenceIndex());
				}
				else if (result instanceof CATMOSModelResult)
				{
					CATMOSModelResult catmosResult = (CATMOSModelResult) result;
					System.out.println("VT Conf Index: " + catmosResult.getVt().getConfidenceIndex());
					System.out.println("NT Conf Index: " + catmosResult.getNt().getConfidenceIndex());
					System.out.println("EPA Conf Index: " + catmosResult.getEpa().getConfidenceIndex());
					System.out.println("GHS Conf Index: " + catmosResult.getGhs().getConfidenceIndex());
					System.out.println("LD50 Conf Index: " + catmosResult.getLd50().getConfidenceIndex());
				}
			}
		}
	}
}
