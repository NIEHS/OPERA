package com.sciome.opera.model.training.model.CERAPP_COMPARA;

import java.io.IOException;
import java.util.zip.GZIPInputStream;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CERAPPTraining extends CERAPP_COMPARATraining
{
	private static CERAPPTraining 			cerappTraining;
	
	private CERAPPTraining() {
		
	}
	
	public static CERAPPTraining getInstance() {
		if (cerappTraining == null)
		{
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			GZIPInputStream inputStream;
			try {
				inputStream = new GZIPInputStream(CERAPPTraining.class.getClassLoader().getResourceAsStream("CERAPP.json.gz"));
				cerappTraining = mapper.readValue(inputStream, CERAPPTraining.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return cerappTraining;
	}
	
	public static CERAPPTraining getCerappTraining() {
		return cerappTraining;
	}

	public static void setCerappTraining(CERAPPTraining cerappTraining) {
		CERAPPTraining.cerappTraining = cerappTraining;
	}
}
