package com.sciome.opera.calc.math;

public class Statistics {
	private double mean;
	private double standardDeviation;
	private double min;
	private double max;
	
	public Statistics(double[] data) {
		double n = data.length;
		
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		double mean = 0;
		double variance = 0;
		
		for(int i = 0; i < n; i++) {
			double x = data[i];
			if(x > max)
				max = data[i];
			if(x < min)
				min = data[i];
			
			double oldM = mean;
			mean += (x - mean) / (i + 1);
			variance += (x- mean) * (x - oldM);
		}
		variance = variance / (n - 1);
		double sd = Math.sqrt(variance);
		
		this.mean = mean;
		this.standardDeviation = sd;
		this.max = max;
		this.min = min;
	}
	
	public double getMean() {
		return mean;
	}
	public void setMean(double mean) {
		this.mean = mean;
	}
	public double getStandardDeviation() {
		return standardDeviation;
	}
	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
}
