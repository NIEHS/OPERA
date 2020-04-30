package com.sciome.opera.model.training.model;

import java.io.IOException;
import java.util.zip.GZIPInputStream;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class KOCTraining extends TrainingModel
{
	private static KOCTraining kocTraining;
	
	private KOCTraining() {
		
	}
	
	public static KOCTraining getInstance() {
		if (kocTraining == null)
		{
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			GZIPInputStream inputStream;
			try {
				inputStream = new GZIPInputStream(KOCTraining.class.getClassLoader().getResourceAsStream("KOC.json.gz"));
				kocTraining = mapper.readValue(inputStream, KOCTraining.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return kocTraining;
	}
}
