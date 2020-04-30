package com.sciome.opera.model.training.model;

import java.io.IOException;
import java.util.zip.GZIPInputStream;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HLTraining extends TrainingModel
{
	private static HLTraining hlTraining;
	
	private HLTraining() {
		
	}
	
	public static HLTraining getInstance() {
		if (hlTraining == null)
		{
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			GZIPInputStream inputStream;
			try {
				inputStream = new GZIPInputStream(HLTraining.class.getClassLoader().getResourceAsStream("HL.json.gz"));
				hlTraining = mapper.readValue(inputStream, HLTraining.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return hlTraining;
	}
}
