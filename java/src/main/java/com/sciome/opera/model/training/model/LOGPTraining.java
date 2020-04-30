package com.sciome.opera.model.training.model;

import java.io.IOException;
import java.util.zip.GZIPInputStream;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LOGPTraining extends TrainingModel
{
	private static LOGPTraining logPTraining;
	
	private LOGPTraining() {
		
	}
	
	public static LOGPTraining getInstance() {
		if (logPTraining == null)
		{
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			GZIPInputStream inputStream;
			try {
				inputStream = new GZIPInputStream(LOGPTraining.class.getClassLoader().getResourceAsStream("LogP.json.gz"));
				logPTraining = mapper.readValue(inputStream, LOGPTraining.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return logPTraining;
	}
}
