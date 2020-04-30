package com.sciome.opera.model.training.model;

import java.io.IOException;
import java.util.zip.GZIPInputStream;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AOHTraining extends TrainingModel
{
	private static AOHTraining aohTraining;
	
	private AOHTraining() {
		
	}
	
	public static AOHTraining getInstance() {
		if (aohTraining == null)
		{
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			GZIPInputStream inputStream;
			try {
				inputStream = new GZIPInputStream(AOHTraining.class.getClassLoader().getResourceAsStream("AOH.json.gz"));
				aohTraining = mapper.readValue(inputStream, AOHTraining.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return aohTraining;
	}
}
