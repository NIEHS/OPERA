package com.sciome.opera.model.training.model.pKa;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PKASecondModel
{
	List<Integer>	parameters;
	Integer			nr_class;
	Integer			totalSV;
	Double			rho;
	List<String>	label;
	List<Integer>	sv_indices;
	List<Double>	probA;
	List<Double>	probB;
	List<Double>	nSV;
	List<Double>	sv_coef;
	String			SVs;
	int[][]			SVsArray;

	public List<Integer> getParameters()
	{
		return parameters;
	}

	public void setParameters(List<Integer> parameters)
	{
		this.parameters = parameters;
	}

	public Integer getNr_class()
	{
		return nr_class;
	}

	public void setNr_class(Integer nr_class)
	{
		this.nr_class = nr_class;
	}

	public Integer getTotalSV()
	{
		return totalSV;
	}

	public void setTotalSV(Integer totalSV)
	{
		this.totalSV = totalSV;
	}

	public Double getRho()
	{
		return rho;
	}

	public void setRho(Double rho)
	{
		this.rho = rho;
	}

	public List<String> getLabel()
	{
		return label;
	}

	public void setLabel(List<String> label)
	{
		this.label = label;
	}

	public List<Integer> getSv_indices()
	{
		return sv_indices;
	}

	public void setSv_indices(List<Integer> sv_indices)
	{
		this.sv_indices = sv_indices;
	}

	public List<Double> getProbA()
	{
		return probA;
	}

	public void setProbA(List<Double> probA)
	{
		this.probA = probA;
	}

	public List<Double> getProbB()
	{
		return probB;
	}

	public void setProbB(List<Double> probB)
	{
		this.probB = probB;
	}

	public List<Double> getnSV()
	{
		return nSV;
	}

	public void setnSV(List<Double> nSV)
	{
		this.nSV = nSV;
	}

	public List<Double> getSv_coef()
	{
		return sv_coef;
	}

	public void setSv_coef(List<Double> sv_coef)
	{
		this.sv_coef = sv_coef;
	}

	public String getSVs()
	{
		return SVs;
	}

	public void setSVs(String sVs)
	{
		SVs = sVs;
	}

	public int[][] getSVsArray()
	{
		if (SVsArray == null)
		{
			ObjectMapper mapper = new ObjectMapper();
			SVModel sv = null;
			try
			{
				sv = mapper.readValue(SVs, SVModel.class);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			if (sv != null && sv.get_ArraySize_() != null && sv.get_ArrayData_() != null)
			{
				SVsArray = new int[(int) sv.get_ArraySize_()[0]][(int) sv.get_ArraySize_()[1]];
				for (int i = 0; i < sv.get_ArrayData_().length; i++)
				{
					SVsArray[(int) sv.get_ArrayData_()[i][0] - 1][(int) sv.get_ArrayData_()[i][1]
							- 1] = (int) sv.get_ArrayData_()[i][2];
				}
			}
			// SVsArray = MatrixUtils.createRealMatrix(SVsArray).transpose().getData();
		}
		return SVsArray;
	}
}
