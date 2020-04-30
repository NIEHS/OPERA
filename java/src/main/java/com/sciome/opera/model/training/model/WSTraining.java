package com.sciome.opera.model.training.model;

import java.io.IOException;
import java.util.zip.GZIPInputStream;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WSTraining extends TrainingModel
{
	private static WSTraining wsTraining;
	
	private WSTraining() {
		
	}
	
	public static WSTraining getInstance() {
		if (wsTraining == null)
		{
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			GZIPInputStream inputStream;
			try {
				inputStream = new GZIPInputStream(WSTraining.class.getClassLoader().getResourceAsStream("WS.json.gz"));
				wsTraining = mapper.readValue(inputStream, WSTraining.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return wsTraining;
	}
}
