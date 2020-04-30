package com.sciome.opera.model.training.model.CATMoS;

import java.io.IOException;
import java.util.List;
import java.util.zip.GZIPInputStream;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sciome.opera.model.training.model.TrainingModel;

public class CATMOSTraining extends TrainingModel
{
	private static CATMOSTraining catmosTraining;
	
	CATMOSModel			model_VT;
	CATMOSModel			model_NT;
	CATMOSModel			model_EPA;
	CATMOSModel			model_GHS;
	CATMOSLD50Model		model_LD50;
	List<String>		Desc;
	List<Integer>		cdk_in;
	List<String>		padelDesc_in;
	List<String>		cdkDesc_in;
	List<String>		descIn;
	List<Integer>		padel_in;
	
	
	private CATMOSTraining() {
		
	}
	
	public static CATMOSTraining getInstance() {
		if (catmosTraining == null)
		{
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			GZIPInputStream inputStream;
			try {
				inputStream = new GZIPInputStream(CATMOSTraining.class.getClassLoader().getResourceAsStream("CATMoS.json.gz"));
				catmosTraining = mapper.readValue(inputStream, CATMOSTraining.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return catmosTraining;
	}
	
	public CATMOSModel getModel_VT() {
		return model_VT;
	}
	public void setModel_VT(CATMOSModel model_VT) {
		this.model_VT = model_VT;
	}
	public CATMOSModel getModel_NT() {
		return model_NT;
	}
	public void setModel_NT(CATMOSModel model_NT) {
		this.model_NT = model_NT;
	}
	public CATMOSModel getModel_EPA() {
		return model_EPA;
	}
	public void setModel_EPA(CATMOSModel model_EPA) {
		this.model_EPA = model_EPA;
	}
	public CATMOSModel getModel_GHS() {
		return model_GHS;
	}
	public void setModel_GHS(CATMOSModel model_GHS) {
		this.model_GHS = model_GHS;
	}
	public CATMOSLD50Model getModel_LD50() {
		return model_LD50;
	}

	public void setModel_LD50(CATMOSLD50Model model_LD50) {
		this.model_LD50 = model_LD50;
	}

	public List<String> getDesc() {
		return Desc;
	}
	public void setDesc(List<String> desc) {
		Desc = desc;
	}
	public List<Integer> getCdk_in() {
		return cdk_in;
	}
	public void setCdk_in(List<Integer> cdk_in) {
		this.cdk_in = cdk_in;
	}
	public List<String> getPadelDesc_in() {
		return padelDesc_in;
	}
	public void setPadelDesc_in(List<String> padelDesc_in) {
		this.padelDesc_in = padelDesc_in;
	}
	public List<String> getCdkDesc_in() {
		return cdkDesc_in;
	}
	public void setCdkDesc_in(List<String> cdkDesc_in) {
		this.cdkDesc_in = cdkDesc_in;
	}
	public List<String> getDescIn() {
		return descIn;
	}
	public void setDescIn(List<String> descIn) {
		this.descIn = descIn;
	}
	public List<Integer> getPadel_in() {
		return padel_in;
	}
	public void setPadel_in(List<Integer> padel_in) {
		this.padel_in = padel_in;
	}
}
