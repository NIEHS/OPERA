package com.sciome.opera.model;

import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import com.sciome.opera.calc.math.Statistics;
import com.sciome.opera.enums.PretreatmentType;

public class PretreatmentResult implements IPretreatment
{
	private RealMatrix matrix;
	private List<Statistics> descriptiveStatistics;
	private PretreatmentType pretType;
	
	//Only used for testing purposes
	private RealVector mean;
	private RealVector min;
	private RealVector max;
	private RealVector std;

	public RealMatrix getMatrix()
	{
		return matrix;
	}

	public void setMatrix(RealMatrix matrix)
	{
		this.matrix = matrix;
	}

	public List<Statistics> getDescriptiveStatistics()
	{
		return descriptiveStatistics;
	}

	public void setDescriptiveStatistics(List<Statistics>  descriptiveStatistics)
	{
		this.descriptiveStatistics = descriptiveStatistics;
	}

	public PretreatmentType getPret_type() {
		return pretType;
	}

	public void setPretType(PretreatmentType pretType) {
		this.pretType = pretType;
	}

	public RealVector getMean() {
		if(mean != null) {
			return mean;
		} else {
			double[] mean = new double[descriptiveStatistics.size()];
			for(int i = 0; i < descriptiveStatistics.size(); i++) {
				mean[i] = descriptiveStatistics.get(i).getMean();
			}
			return MatrixUtils.createRealVector(mean);
		}
	}
	
	public RealVector getSd() {
		if(std != null) {
			return std;
		} else {
			double[] std = new double[descriptiveStatistics.size()];
			for(int i = 0; i < descriptiveStatistics.size(); i++) {
				std[i] = descriptiveStatistics.get(i).getStandardDeviation();
			}
			return MatrixUtils.createRealVector(std);
		}
	}
	
	public RealVector getMin() {
		if(min != null) {
			return min;
		} else {
			double[] min = new double[descriptiveStatistics.size()];
			for(int i = 0; i < descriptiveStatistics.size(); i++) {
				min[i] = descriptiveStatistics.get(i).getMin();
			}
			return MatrixUtils.createRealVector(min);
		}
	}
	
	public RealVector getMax() {
		if(max != null) {
			return max;
		} else {
			double[] max = new double[descriptiveStatistics.size()];
			for(int i = 0; i < descriptiveStatistics.size(); i++) {
				max[i] = descriptiveStatistics.get(i).getMax();
			}
			return MatrixUtils.createRealVector(max);
		}
	}

	public void setMean(RealVector mean) {
		this.mean = mean;
	}

	public void setMin(RealVector min) {
		this.min = min;
	}

	public void setMax(RealVector max) {
		this.max = max;
	}

	public void setStd(RealVector std) {
		this.std = std;
	}
}
