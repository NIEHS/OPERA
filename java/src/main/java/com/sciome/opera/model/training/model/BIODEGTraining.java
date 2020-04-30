package com.sciome.opera.model.training.model;

import java.io.IOException;
import java.util.zip.GZIPInputStream;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BIODEGTraining extends TrainingModel
{
	private static BIODEGTraining biodegTraining;
	
	private BIODEGTraining() {
		
	}
	
	public static BIODEGTraining getInstance() {
		if (biodegTraining == null)
		{
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			GZIPInputStream inputStream;
			try {
				inputStream = new GZIPInputStream(BIODEGTraining.class.getClassLoader().getResourceAsStream("Biodeg.json.gz"));
				biodegTraining = mapper.readValue(inputStream, BIODEGTraining.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return biodegTraining;
	}
}
