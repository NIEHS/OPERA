package com.sciome.opera;

import static com.sciome.opera.CustomMatcher.matches;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.sciome.opera.calc.Opera;
import com.sciome.opera.enums.Models;
import com.sciome.opera.model.results.CATMOSModelResult;
import com.sciome.opera.model.results.CERAPP_COMPARAModelResult;
import com.sciome.opera.model.results.DefaultModelResult;
import com.sciome.opera.model.results.LogDModelResult;
import com.sciome.opera.model.results.OperaModelResult;
import com.sciome.opera.model.results.PKAModelResult;

public class OperaTest {
	public static final double epsilon = .0001;
	public static final double catmos_conf_index_epsilon = .01;
	
	@Rule
	public ErrorCollector collector = new ErrorCollector();
	
	@SuppressWarnings("unchecked")
	@Test
	public void test() {
		URL url = Resources.getResource("Sample_50.smi");
		String text = "";
		try
		{
			text = Resources.toString(url, Charsets.UTF_8);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		Opera opera = new Opera();

		Set<Models> models = new HashSet<Models>(Arrays.asList(Models.values()));

//		Set<Models> models = new HashSet<Models>();
//		models.add(Models.LOGD);
		
		Map<Models, List<OperaModelResult>> output = opera.calc(text, models, true).getOperaResults();
		InputStream operaStream = getClass().getClassLoader().getResourceAsStream("opera_results_2.csv");
		CSVParser parser;
		try
		{
			parser = CSVParser.parse(operaStream, Charset.defaultCharset(), CSVFormat.DEFAULT.withFirstRecordAsHeader());
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		
		for (CSVRecord record : parser)
		{
			String name = record.get("Molecule Name");
			System.out.println(record.getRecordNumber());
			
			for(Models model : output.keySet()) {
				OperaModelResult result = null;
				for(OperaModelResult operaResult : output.get(model)) {
					if(operaResult.getChemicalDescriptors().getChemID().equals(name))
						result = operaResult;
				}
				switch(model) {
					case CERAPP:
						CERAPP_COMPARAModelResult cerappResult = (CERAPP_COMPARAModelResult)result;
						//Ago Models
						collector.checkThat("AD_index " + model.getName() + " Ago at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_index_" + model.getName() + "_Ago")), matches(cerappResult.getAgo().getAdIndex()));
						collector.checkThat("Conf_index " + model.getName() + " Ago at index " + record.getRecordNumber(),
								Double.valueOf(record.get("Conf_index_" + model.getName() + "_Ago")), matches(cerappResult.getAgo().getConfidenceIndex()));
						collector.checkThat(model.getName() + "_Ago_pred at index " + record.getRecordNumber(),
								Double.valueOf(record.get(model.getName() + "_Ago_pred")), matches(cerappResult.getAgo().getPredictedValue()));
						collector.checkThat("AD_" + model.getName() + "_Ago at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_" + model.getName() + "_Ago")), matches(Double.valueOf(cerappResult.getAgo().getAdModel())));
						
						//Anta Models
						collector.checkThat("AD_index " + model.getName() + " Anta at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_index_" + model.getName() + "_Anta")), matches(cerappResult.getAnta().getAdIndex()));
						collector.checkThat("Conf_index " + model.getName() + " Anta at index " + record.getRecordNumber(),
								Double.valueOf(record.get("Conf_index_" + model.getName() + "_Anta")), matches(cerappResult.getAnta().getConfidenceIndex()));
						collector.checkThat(model.getName() + "_Anta_pred at index " + record.getRecordNumber(),
								Double.valueOf(record.get(model.getName() + "_Anta_pred")), matches(cerappResult.getAnta().getPredictedValue()));
						collector.checkThat("AD_" + model.getName() + "_Anta at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_" + model.getName() + "_Anta")), matches(Double.valueOf(cerappResult.getAnta().getAdModel())));
						
						//Bind Models
						collector.checkThat("AD_index " + model.getName() + " Bind at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_index_" + model.getName() + "_Bind")), matches(cerappResult.getBind().getAdIndex()));
						collector.checkThat("Conf_index " + model.getName() + " Bind at index " + record.getRecordNumber(),
								Double.valueOf(record.get("Conf_index_" + model.getName() + "_Bind")), matches(cerappResult.getBind().getConfidenceIndex()));
						collector.checkThat(model.getName() + "_Bind_pred at index " + record.getRecordNumber(),
								Double.valueOf(record.get(model.getName() + "_Bind_pred")), matches(cerappResult.getBind().getPredictedValue()));
						collector.checkThat("AD_" + model.getName() + "_BinD at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_" + model.getName() + "_BinD")), matches(Double.valueOf(cerappResult.getBind().getAdModel())));
						break;
					case CATMOS:
						CATMOSModelResult catmosResult = (CATMOSModelResult)result;
						//VT Models
						collector.checkThat("AD_index VT " + model.getName() + " at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_index_VT")), matches(catmosResult.getVt().getAdIndex()));
						collector.checkThat("Conf_index VT" + model.getName() + " at index " + record.getRecordNumber(),
								Double.valueOf(record.get("Conf_index_VT")), matches(catmosResult.getVt().getConfidenceIndex()));
						collector.checkThat(model.getName() + "_CATMoS_VT_pred at index " + record.getRecordNumber(),
								Double.valueOf(record.get("CATMoS_VT_pred")), matches(catmosResult.getVt().getPredictedValue()));
						collector.checkThat("AD_" + model.getName() + "_VT at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_VT")), matches(Double.valueOf(catmosResult.getVt().getAdModel())));
						
						//NT Models
						collector.checkThat("AD_index NT " + model.getName() + " at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_index_NT")), matches(catmosResult.getNt().getAdIndex()));
						collector.checkThat("Conf_index NT" + model.getName() + " at index " + record.getRecordNumber(),
								Double.valueOf(record.get("Conf_index_NT")), matches(catmosResult.getNt().getConfidenceIndex()));
						collector.checkThat(model.getName() + "_CATMoS_NT_pred at index " + record.getRecordNumber(),
								Double.valueOf(record.get("CATMoS_NT_pred")), matches(catmosResult.getNt().getPredictedValue()));
						collector.checkThat("AD_" + model.getName() + "_NT at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_NT")), matches(Double.valueOf(catmosResult.getNt().getAdModel())));
						
						//GHS Models
						collector.checkThat("AD_index GHS " + model.getName() + " at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_index_GHS")), matches(catmosResult.getGhs().getAdIndex()));
						collector.checkThat("Conf_index GHS" + model.getName() + " at index " + record.getRecordNumber(),
								Double.valueOf(record.get("Conf_index_GHS")), matches(catmosResult.getGhs().getConfidenceIndex()));
						collector.checkThat(model.getName() + "_CATMoS_GHS_pred at index " + record.getRecordNumber(),
								Double.valueOf(record.get("CATMoS_GHS_pred")), matches(catmosResult.getGhs().getPredictedValue()));
						collector.checkThat("AD_" + model.getName() + "_GHS at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_GHS")), matches(Double.valueOf(catmosResult.getGhs().getAdModel())));
						
						//EPA Models
						collector.checkThat("AD_index EPA " + model.getName() + " at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_index_EPA")), matches(catmosResult.getEpa().getAdIndex()));
						collector.checkThat("Conf_index EPA" + model.getName() + " at index " + record.getRecordNumber(),
								Double.valueOf(record.get("Conf_index_EPA")), matches(catmosResult.getEpa().getConfidenceIndex()));
						collector.checkThat(model.getName() + "_CATMoS_EPA_pred at index " + record.getRecordNumber(),
								Double.valueOf(record.get("CATMoS_EPA_pred")), matches(catmosResult.getEpa().getPredictedValue()));
						collector.checkThat("AD_" + model.getName() + "_EPA at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_EPA")), matches(Double.valueOf(catmosResult.getEpa().getAdModel())));
						
						//LD50 Models
						collector.checkThat("AD_index LD50 " + model.getName() + " at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_index_LD50")), matches(catmosResult.getLd50().getAdIndex()));
						collector.checkThat("Conf_index LD50" + model.getName() + " at index " + record.getRecordNumber(),
								Double.valueOf(record.get("Conf_index_LD50")), matches(catmosResult.getLd50().getConfidenceIndex()));
						collector.checkThat(model.getName() + "_CATMoS_LD50_pred at index " + record.getRecordNumber(),
								Double.valueOf(record.get("CATMoS_LD50_pred")), matches(catmosResult.getLd50().getPredictedValue()));
						collector.checkThat("AD_" + model.getName() + "_LD50 at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_LD50")), matches(Double.valueOf(catmosResult.getLd50().getAdModel())));
						break;
					case COMPARA:
						CERAPP_COMPARAModelResult comparaResult = (CERAPP_COMPARAModelResult)result;
						//Ago Models
						collector.checkThat("AD_index " + model.getName() + " Ago at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_index_" + model.getName() + "_Ago")), matches(comparaResult.getAgo().getAdIndex()));
						collector.checkThat("Conf_index " + model.getName() + " Ago at index " + record.getRecordNumber(),
								Double.valueOf(record.get("Conf_index_" + model.getName() + "_Ago")), matches(comparaResult.getAgo().getConfidenceIndex()));
						collector.checkThat(model.getName() + "_Ago_pred at index " + record.getRecordNumber(),
								Double.valueOf(record.get(model.getName() + "_Ago_pred")), matches(comparaResult.getAgo().getPredictedValue()));
						collector.checkThat("AD_" + model.getName() + " at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_" + model.getName() + "_Ago")), matches(Double.valueOf(comparaResult.getAgo().getAdModel())));
						
						//Anta Models
						collector.checkThat("AD_index " + model.getName() + " Anta at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_index_" + model.getName() + "_Anta")), matches(comparaResult.getAnta().getAdIndex()));
						collector.checkThat("Conf_index " + model.getName() + " Anta at index " + record.getRecordNumber(),
								Double.valueOf(record.get("Conf_index_" + model.getName() + "_Anta")), matches(comparaResult.getAnta().getConfidenceIndex()));
						collector.checkThat(model.getName() + "_Anta_pred at index " + record.getRecordNumber(),
								Double.valueOf(record.get(model.getName() + "_Anta_pred")), matches(comparaResult.getAnta().getPredictedValue()));
						collector.checkThat("AD_" + model.getName() + "_Anta at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_" + model.getName() + "_Anta")), matches(Double.valueOf(comparaResult.getAnta().getAdModel())));
						
						//Bind Models
						collector.checkThat("AD_index " + model.getName() + " Bind at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_index_" + model.getName() + "_Bind")), matches(comparaResult.getBind().getAdIndex()));
						collector.checkThat("Conf_index " + model.getName() + " Bind at index " + record.getRecordNumber(),
								Double.valueOf(record.get("Conf_index_" + model.getName() + "_Bind")), matches(comparaResult.getBind().getConfidenceIndex()));
						collector.checkThat(model.getName() + "_Bind_pred at index " + record.getRecordNumber(),
								Double.valueOf(record.get(model.getName() + "_Bind_pred")), matches(comparaResult.getBind().getPredictedValue()));
						collector.checkThat("AD_" + model.getName() + "_BinD at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_" + model.getName() + "_BinD")), matches(Double.valueOf(comparaResult.getBind().getAdModel())));
						break;
					case PKA:
						PKAModelResult pkaResult = (PKAModelResult)result;

						collector.checkThat("AD_index " + model.getName() + " at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_index_" + model.getName())), matches(pkaResult.getAdIndex()));
						collector.checkThat("Conf_index " + model.getName() + " at index " + record.getRecordNumber(),
								Double.valueOf(record.get("Conf_index_" + model.getName())), matches(pkaResult.getConfidenceIndex()));
						collector.checkThat("pKa_pred_a at index " + record.getRecordNumber(),
								Double.valueOf(record.get("pKa_a_pred")), matches(pkaResult.getpKa_a_pred()));
						collector.checkThat("pKa_pred_a at index " + record.getRecordNumber(),
								Double.valueOf(record.get("pKa_b_pred")), matches(pkaResult.getpKa_b_pred()));
						collector.checkThat("pKa ionization at index " + record.getRecordNumber(),
								Double.valueOf(record.get("ionization")), matches(Double.valueOf(pkaResult.getIonization())));
						collector.checkThat("AD_" + model.getName() + " at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_" + model.getName())), matches(Double.valueOf(pkaResult.getAdModel())));
						break;
					case LOGD:
						LogDModelResult logDResult = (LogDModelResult)result;
						collector.checkThat("AD_index " + model.getName() + " at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_index_" + model.getName())), matches(logDResult.getAdIndex()));
						collector.checkThat("Conf_index " + model.getName() + " at index " + record.getRecordNumber(),
								Double.valueOf(record.get("Conf_index_" + model.getName())), matches(logDResult.getConfidenceIndex()));
						collector.checkThat(model.getName() + "55_pred at index " + record.getRecordNumber(),
								Double.valueOf(record.get(model.getName() + "55_pred")), matches(logDResult.getLogD55_pred()));
						collector.checkThat(model.getName() + "74_pred at index " + record.getRecordNumber(),
								Double.valueOf(record.get(model.getName() + "74_pred")), matches(logDResult.getLogD74_pred()));
						collector.checkThat("AD_" + model.getName() + " at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_" + model.getName())), matches(Double.valueOf(logDResult.getAdModel())));
						break;
					default:
						DefaultModelResult defaultResult = (DefaultModelResult)result;
						collector.checkThat("AD_index " + model.getName() + " at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_index_" + model.getName())), matches(defaultResult.getAdIndex()));
						collector.checkThat("Conf_index " + model.getName() + " at index " + record.getRecordNumber(),
								Double.valueOf(record.get("Conf_index_" + model.getName())), matches(defaultResult.getConfidenceIndex()));
						collector.checkThat(model.getName() + "_pred at index " + record.getRecordNumber(),
								Double.valueOf(record.get(model.getName() + "_pred")), matches(defaultResult.getPredictedValue()));
						collector.checkThat("AD_" + model.getName() + " at index " + record.getRecordNumber(),
								Double.valueOf(record.get("AD_" + model.getName())), matches(Double.valueOf(defaultResult.getAdModel())));
						break;
				}
			}
		}
	}
	
}
