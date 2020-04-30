package com.sciome.opera.model.training.model;

import java.io.IOException;
import java.util.zip.GZIPInputStream;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class KOATraining extends TrainingModel
{
	private static KOATraining koaTraining;
	
	private KOATraining() {
		
	}
	
	public static KOATraining getInstance() {
		if (koaTraining == null)
		{
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			GZIPInputStream inputStream;
			try {
				inputStream = new GZIPInputStream(KOATraining.class.getClassLoader().getResourceAsStream("KOA.json.gz"));
				koaTraining = mapper.readValue(inputStream, KOATraining.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return koaTraining;
	}
}
