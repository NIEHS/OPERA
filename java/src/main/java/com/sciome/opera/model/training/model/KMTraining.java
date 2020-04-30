package com.sciome.opera.model.training.model;

import java.io.IOException;
import java.util.zip.GZIPInputStream;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class KMTraining extends TrainingModel
{
	private static KMTraining kmTraining;
	
	private KMTraining() {
		
	}
	
	public static KMTraining getInstance() {
		if (kmTraining == null)
		{
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			GZIPInputStream inputStream;
			try {
				inputStream = new GZIPInputStream(KMTraining.class.getClassLoader().getResourceAsStream("KM.json.gz"));
				kmTraining = mapper.readValue(inputStream, KMTraining.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return kmTraining;
	}
}
