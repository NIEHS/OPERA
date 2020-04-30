package com.sciome.opera.model.training.model.RT;

import java.io.IOException;
import java.util.List;
import java.util.zip.GZIPInputStream;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sciome.opera.model.training.model.TrainingModel;
 
public class RTTraining
{
	private static RTTraining rtTraining;
	
	List<String>	id;
	List<String>	desc;
	List<String>	cas;
	List<String>	dssToxCid;
	List<String>	dtxSid;
	List<Integer>	desc_i;
	RTModel			model;
	
	private RTTraining() {
		
	}
	
	public static RTTraining getInstance() {
		if (rtTraining == null)
		{
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			GZIPInputStream inputStream;
			try {
				inputStream = new GZIPInputStream(RTTraining.class.getClassLoader().getResourceAsStream("RT.json.gz"));
				rtTraining = mapper.readValue(inputStream, RTTraining.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return rtTraining;
	}

	public List<String> getId() {
		return id;
	}

	public void setId(List<String> id) {
		this.id = id;
	}

	public List<String> getDesc() {
		return desc;
	}

	public void setDesc(List<String> desc) {
		this.desc = desc;
	}

	public List<String> getCas() {
		return cas;
	}

	public void setCas(List<String> cas) {
		this.cas = cas;
	}

	public List<String> getDssToxCid() {
		return dssToxCid;
	}

	public void setDssToxCid(List<String> dssToxCid) {
		this.dssToxCid = dssToxCid;
	}

	public List<String> getDtxSid() {
		return dtxSid;
	}

	public void setDtxSid(List<String> dtxSid) {
		this.dtxSid = dtxSid;
	}

	public List<Integer> getDesc_i() {
		return desc_i;
	}

	public void setDesc_i(List<Integer> desc_i) {
		this.desc_i = desc_i;
	}

	public RTModel getModel() {
		return model;
	}

	public void setModel(RTModel model) {
		this.model = model;
	}
}
