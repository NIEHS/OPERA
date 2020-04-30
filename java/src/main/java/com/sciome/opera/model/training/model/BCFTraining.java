package com.sciome.opera.model.training.model;

import java.io.IOException;
import java.util.zip.GZIPInputStream;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BCFTraining extends TrainingModel
{
	private static BCFTraining bcfTraining;
	
	private BCFTraining() {
		
	}
	
	public static BCFTraining getInstance() {
		if (bcfTraining == null)
		{
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			GZIPInputStream inputStream;
			try {
				inputStream = new GZIPInputStream(BCFTraining.class.getClassLoader().getResourceAsStream("BCF.json.gz"));
				bcfTraining = mapper.readValue(inputStream, BCFTraining.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bcfTraining;
	}
}
