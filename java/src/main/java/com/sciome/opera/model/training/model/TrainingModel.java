package com.sciome.opera.model.training.model;

import java.util.List;

import com.sciome.opera.model.training.ModelData;

public class TrainingModel
{
	List<String>	inChiKey;

	List<String>	id;
	List<String>	desc;

	List<String>	cas;

	List<String>	dssToxMpId;

	List<String>	dtxSid;
	List<Integer>	desc_i;

	ModelData		model;

	public List<String> getInChiKey()
	{
		return inChiKey;
	}

	public void setInChiKey(List<String> inChiKey)
	{
		this.inChiKey = inChiKey;
	}

	public List<String> getId()
	{
		return id;
	}

	public void setId(List<String> id)
	{
		this.id = id;
	}

	public List<String> getDesc()
	{
		return desc;
	}

	public void setDesc(List<String> desc)
	{
		this.desc = desc;
	}

	public List<String> getCas()
	{
		return cas;
	}

	public void setCas(List<String> cas)
	{
		this.cas = cas;
	}

	public List<String> getDssToxMpId()
	{
		return dssToxMpId;
	}

	public void setDssToxMpId(List<String> dssToxMpId)
	{
		this.dssToxMpId = dssToxMpId;
	}

	public List<String> getDtxSid()
	{
		return dtxSid;
	}

	public void setDtxSid(List<String> dtxSid)
	{
		this.dtxSid = dtxSid;
	}

	public List<Integer> getDesc_i()
	{
		return desc_i;
	}

	public void setDesc_i(List<Integer> desc_i)
	{
		this.desc_i = desc_i;
	}

	public ModelData getModel()
	{
		return model;
	}

	public void setModel(ModelData model)
	{
		this.model = model;
	}

}
