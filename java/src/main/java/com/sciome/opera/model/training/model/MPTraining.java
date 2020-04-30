package com.sciome.opera.model.training.model;

import java.io.IOException;
import java.util.List;
import java.util.zip.GZIPInputStream;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sciome.opera.model.training.ModelData;

public class MPTraining extends TrainingModel
{
	private static MPTraining mpTraining;
	
	List<Double>		epiMp;
	List<Integer>		saltId;
	ModelData			model_s;
	
	private MPTraining() {
		
	}
	
	public static MPTraining getInstance() {
		if (mpTraining == null)
		{
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			GZIPInputStream inputStream;
			try {
				inputStream = new GZIPInputStream(MPTraining.class.getClassLoader().getResourceAsStream("MP.json.gz"));
				mpTraining = mapper.readValue(inputStream, MPTraining.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return mpTraining;
	}
	
	public List<Double> getEpiMp() {
		return epiMp;
	}
	public void setEpiMp(List<Double> epiMp) {
		this.epiMp = epiMp;
	}
	public List<Integer> getSaltId() {
		return saltId;
	}
	public void setSaltId(List<Integer> saltId) {
		this.saltId = saltId;
	}
	public ModelData getModel_s() {
		return model_s;
	}
	public void setModel_s(ModelData model_s) {
		this.model_s = model_s;
	}
}
