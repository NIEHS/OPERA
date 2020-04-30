package com.sciome.opera.model.training;

import java.util.List;

public class TrainingSet
{
	List<List<Double>>	train;
	List<Double>		y;
	TrainingParam		param;
	Integer				K;

	String				dist_type;

	public List<List<Double>> getTrain()
	{
		return train;
	}

	public void setTrain(List<List<Double>> train)
	{
		this.train = train;
	}

	public List<Double> getY()
	{
		return y;
	}

	public void setY(List<Double> y)
	{
		this.y = y;
	}

	public TrainingParam getParam()
	{
		return param;
	}

	public void setParam(TrainingParam param)
	{
		this.param = param;
	}

	public Integer getK()
	{
		return K;
	}

	public void setK(Integer k)
	{
		K = k;
	}

	public String getDist_type()
	{
		return dist_type;
	}

	public void setDist_type(String dist_type)
	{
		this.dist_type = dist_type;
	}

}
