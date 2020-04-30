package com.sciome.opera.model.training.model;

import java.io.IOException;
import java.util.List;
import java.util.zip.GZIPInputStream;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VPTraining extends TrainingModel
{
	private static VPTraining vpTraining;
	
	private List<Double>	epiLogVP;

	private VPTraining() {
		
	}
	
	public static VPTraining getInstance() {
		if (vpTraining == null)
		{
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			GZIPInputStream inputStream;
			try {
				inputStream = new GZIPInputStream(VPTraining.class.getClassLoader().getResourceAsStream("VP.json.gz"));
				vpTraining = mapper.readValue(inputStream, VPTraining.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return vpTraining;
	}
	
	public List<Double> getEpiLogVP() {
		return epiLogVP;
	}

	public void setEpiLogVP(List<Double> epiLogVP) {
		this.epiLogVP = epiLogVP;
	}
}
