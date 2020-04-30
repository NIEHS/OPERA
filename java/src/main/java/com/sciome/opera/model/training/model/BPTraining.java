package com.sciome.opera.model.training.model;

import java.io.IOException;
import java.util.List;
import java.util.zip.GZIPInputStream;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BPTraining extends TrainingModel
{
	private static BPTraining bpTraining;
	List<Double> epiBP;
	List<Integer> dssToxCid;
	
	private BPTraining() {
		
	}
	
	public static BPTraining getInstance() {
		if (bpTraining == null)
		{
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			GZIPInputStream inputStream;
			try {
				inputStream = new GZIPInputStream(BPTraining.class.getClassLoader().getResourceAsStream("BP.json.gz"));
				bpTraining = mapper.readValue(inputStream, BPTraining.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bpTraining;
	}
	
	public List<Double> getEpiBP() {
		return epiBP;
	}
	public void setEpiBP(List<Double> epiBP) {
		this.epiBP = epiBP;
	}
	public List<Integer> getDssToxCid() {
		return dssToxCid;
	}
	public void setDssToxCid(List<Integer> dssToxCid) {
		this.dssToxCid = dssToxCid;
	}
}
