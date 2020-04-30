package com.sciome.opera.model.training.model.pKa;

import java.io.IOException;
import java.util.List;
import java.util.zip.GZIPInputStream;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sciome.opera.libSVM_java.libsvm.svm;
import com.sciome.opera.libSVM_java.libsvm.svm_model;
import com.sciome.opera.libSVM_java.libsvm.svm_node;
import com.sciome.opera.libSVM_java.libsvm.svm_parameter;

public class PKATraining
{
	private static PKATraining	pkaTraining;

	private PKAModel			model;
	private PKASecondModel		model_a;
	private PKASecondModel		model_b;

	private List<String>		desc;
	private List<String>		desc_a;
	private List<String>		desc_b;
	private List<Double>		ya;
	private List<Double>		yb;
	private List<Integer>		iD;
	private List<String>		inChiKey;
	private List<Integer>		desc_ai;
	private List<Integer>		desc_bi;
	private List<Integer>		desc_i;
	private List<String>		dtxsid;
	private List<String>		cas;
	private List<Integer>		chemID;

	private PKATraining()
	{

	}

	public static PKATraining getInstance()
	{
		if (pkaTraining == null)
		{
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			GZIPInputStream inputStream;
			try
			{
				inputStream = new GZIPInputStream(
						PKATraining.class.getClassLoader().getResourceAsStream("pKa.json.gz"));
				pkaTraining = mapper.readValue(inputStream, PKATraining.class);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return pkaTraining;
	}

	public static PKATraining getPkaTraining()
	{
		return pkaTraining;
	}

	public static void setPkaTraining(PKATraining pkaTraining)
	{
		PKATraining.pkaTraining = pkaTraining;
	}

	public PKAModel getModel()
	{
		return model;
	}

	public void setModel(PKAModel model)
	{
		this.model = model;
	}

	public PKASecondModel getModel_a()
	{
		return model_a;
	}

	public void setModel_a(PKASecondModel model_a)
	{
		this.model_a = model_a;
	}

	public PKASecondModel getModel_b()
	{
		return model_b;
	}

	public void setModel_b(PKASecondModel model_b)
	{
		this.model_b = model_b;
	}

	public List<String> getDesc()
	{
		return desc;
	}

	public void setDesc(List<String> desc)
	{
		this.desc = desc;
	}

	public List<String> getDesc_a()
	{
		return desc_a;
	}

	public void setDesc_a(List<String> desc_a)
	{
		this.desc_a = desc_a;
	}

	public List<String> getDesc_b()
	{
		return desc_b;
	}

	public void setDesc_b(List<String> desc_b)
	{
		this.desc_b = desc_b;
	}

	public List<Double> getYa()
	{
		return ya;
	}

	public void setYa(List<Double> ya)
	{
		this.ya = ya;
	}

	public List<Double> getYb()
	{
		return yb;
	}

	public void setYb(List<Double> yb)
	{
		this.yb = yb;
	}

	public List<Integer> getiD()
	{
		return iD;
	}

	public void setiD(List<Integer> iD)
	{
		this.iD = iD;
	}

	public List<String> getInChiKey()
	{
		return inChiKey;
	}

	public void setInChiKey(List<String> inChiKey)
	{
		this.inChiKey = inChiKey;
	}

	public List<Integer> getDesc_ai()
	{
		return desc_ai;
	}

	public void setDesc_ai(List<Integer> desc_ai)
	{
		this.desc_ai = desc_ai;
	}

	public List<Integer> getDesc_bi()
	{
		return desc_bi;
	}

	public void setDesc_bi(List<Integer> desc_bi)
	{
		this.desc_bi = desc_bi;
	}

	public List<Integer> getDesc_i()
	{
		return desc_i;
	}

	public void setDesc_i(List<Integer> desc_i)
	{
		this.desc_i = desc_i;
	}

	public List<String> getDtxsid()
	{
		return dtxsid;
	}

	public void setDtxsid(List<String> dtxsid)
	{
		this.dtxsid = dtxsid;
	}

	public List<String> getCas()
	{
		return cas;
	}

	public void setCas(List<String> cas)
	{
		this.cas = cas;
	}

	public List<Integer> getChemID()
	{
		return chemID;
	}

	public void setChemID(List<Integer> chemID)
	{
		this.chemID = chemID;
	}

	public svm_model createModelA()
	{
		svm_model model = addModelFields(this.getModel_a());

		svm_parameter param = new svm_parameter();
		param.svm_type = 3;
		param.kernel_type = 2;
		param.degree = 3;
		param.gamma = 0.0006329113924050633;
		param.coef0 = 0.0;
		param.nu = 0.5;
		param.cache_size = 100;
		param.C = 1;
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 1;
		param.probability = 0;
		param.nr_weight = 0;
		param.weight_label = new int[0];
		param.weight = new double[0];

		model.param = param;

		return model;
	}

	public svm_model createModelB()
	{
		svm_model model = addModelFields(this.getModel_b());

		svm_parameter param = new svm_parameter();
		param.svm_type = 3;
		param.kernel_type = 2;
		param.degree = 3;
		param.gamma = 0.0006480881399870824;
		param.coef0 = 0.0;
		param.nu = 0.5;
		param.cache_size = 100;
		param.C = 1;
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 1;
		param.probability = 0;
		param.nr_weight = 0;
		param.weight_label = new int[0];
		param.weight = new double[0];

		model.param = param;

		return model;
	}

	private svm_model addModelFields(PKASecondModel secondModel)
	{
		svm_model model = new svm_model();

		model.nr_class = secondModel.getNr_class();
		model.l = secondModel.totalSV;
		model.rho = new double[] { secondModel.getRho() };
		double[][] sv_coef = new double[1][secondModel.getSv_coef().size()];
		for (int i = 0; i < secondModel.getSv_coef().size(); i++)
		{
			sv_coef[0][i] = secondModel.getSv_coef().get(i);
		}
		int[] sv_indices = new int[secondModel.getSv_indices().size()];
		for (int i = 0; i < secondModel.getSv_indices().size(); i++)
		{
			sv_indices[i] = secondModel.getSv_indices().get(i);
		}
		model.sv_indices = sv_indices;
		model.sv_coef = sv_coef;

		model.SV = new svm_node[secondModel.getSVsArray().length][secondModel.getSVsArray()[0].length];
		for (int i = 0; i < model.SV.length; i++)
		{
			for (int j = 0; j < model.SV[0].length; j++)
			{
				svm_node node = new svm_node();
				node.value = secondModel.getSVsArray()[i][j];
				node.index = j;
				model.SV[i][j] = node;
			}
		}

		return model;
	}

}
