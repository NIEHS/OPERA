package com.sciome.opera.model.training;

import java.util.List;

public class ModelData
{
	List<Double>	yc;
	List<Double>	yc_weighted;
	RegParam		reg_param;
	RegParam		reg_param_weighted;

	TrainingSet		set;

	public List<Double> getYc()
	{
		return yc;
	}

	public void setYc(List<Double> yc)
	{
		this.yc = yc;
	}

	public List<Double> getYc_weighted()
	{
		return yc_weighted;
	}

	public void setYc_weighted(List<Double> yc_weighted)
	{
		this.yc_weighted = yc_weighted;
	}

	public RegParam getReg_param()
	{
		return reg_param;
	}

	public void setReg_param(RegParam reg_param)
	{
		this.reg_param = reg_param;
	}

	public RegParam getReg_param_weighted()
	{
		return reg_param_weighted;
	}

	public void setReg_param_weighted(RegParam reg_param_weighted)
	{
		this.reg_param_weighted = reg_param_weighted;
	}

	public TrainingSet getSet()
	{
		return set;
	}

	public void setSet(TrainingSet set)
	{
		this.set = set;
	}

}
