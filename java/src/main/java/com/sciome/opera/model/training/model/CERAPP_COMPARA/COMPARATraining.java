package com.sciome.opera.model.training.model.CERAPP_COMPARA;

import java.io.IOException;
import java.util.zip.GZIPInputStream;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class COMPARATraining extends CERAPP_COMPARATraining
{
	private static COMPARATraining 		comparaTraining;
	
	private COMPARATraining() {
		
	}
	
	public static COMPARATraining getInstance() {
		if (comparaTraining == null)
		{
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			GZIPInputStream inputStream;
			try {
				inputStream = new GZIPInputStream(COMPARATraining.class.getClassLoader().getResourceAsStream("CoMPARA.json.gz"));
				comparaTraining = mapper.readValue(inputStream, COMPARATraining.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return comparaTraining;
	}
}
