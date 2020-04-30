package com.sciome.opera.model.training.model;

import java.io.IOException;
import java.util.List;
import java.util.zip.GZIPInputStream;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class STRPTraining
{
	private static STRPTraining strpTraining;
	
	private List<String>	desc;
	private List<Integer>	desc_i;
	
	private STRPTraining() {
		
	}
	
	public static STRPTraining getInstance() {
		if (strpTraining == null)
		{
			strpTraining = new STRPTraining();
		}
		return strpTraining;
	}
	
	public void loadData() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		GZIPInputStream inputStream;
		try {
			inputStream = new GZIPInputStream(getClass().getClassLoader().getResourceAsStream("StrP.json.gz"));
			strpTraining = mapper.readValue(inputStream, STRPTraining.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getDesc() {
		return desc;
	}
	public void setDesc(List<String> desc) {
		this.desc = desc;
	}
	public List<Integer> getDesc_i() {
		return desc_i;
	}
	public void setDesc_i(List<Integer> desc_i) {
		this.desc_i = desc_i;
	}
}
