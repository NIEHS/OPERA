package com.sciome.opera.model.training.model.RBIODEG;

import java.io.IOException;
import java.util.List;
import java.util.zip.GZIPInputStream;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sciome.opera.model.training.model.TrainingModel;

public class RBIODEGTraining
{
	private static RBIODEGTraining rBioDegTraining;
	List<String>	inChiKey;
	List<String>	id;
	List<String>	desc;
	List<String>	cas;
	List<String>	dssToxMpId;
	List<String>	dtxSid;
	List<Integer>	desc_i;
	RBIODEGModel	model;
	
	private RBIODEGTraining() {
		
	}
	
	public static RBIODEGTraining getInstance() {
		if (rBioDegTraining == null)
		{
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			GZIPInputStream inputStream;
			try {
				inputStream = new GZIPInputStream(RBIODEGTraining.class.getClassLoader().getResourceAsStream("RBioDeg.json.gz"));
				rBioDegTraining = mapper.readValue(inputStream, RBIODEGTraining.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return rBioDegTraining;
	}
	
	public List<String> getInChiKey() {
		return inChiKey;
	}

	public void setInChiKey(List<String> inChiKey) {
		this.inChiKey = inChiKey;
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

	public List<String> getDssToxMpId() {
		return dssToxMpId;
	}

	public void setDssToxMpId(List<String> dssToxMpId) {
		this.dssToxMpId = dssToxMpId;
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

	public RBIODEGModel getModel() {
		return model;
	}

	public void setModel(RBIODEGModel model) {
		this.model = model;
	}
}
