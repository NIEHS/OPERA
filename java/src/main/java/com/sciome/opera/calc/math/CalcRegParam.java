package com.sciome.opera.calc.math;

import org.apache.commons.math3.linear.RealVector;

import com.sciome.opera.model.Res;

public class CalcRegParam
{
	public static Res calculate(RealVector yReal, RealVector yCalc) throws Exception
	{
		Res res = new Res();
		int n = yReal.getDimension();
		double[] yRealArray = yReal.toArray();
		double[] yCalcArray = yCalc.toArray();
		if (yRealArray.length != yCalcArray.length)
			throw new Exception("yReal must be same lenght as yCalc");
		Statistics sYReal = new Statistics(yReal.toArray());
		double yRealMean = sYReal.getMean();
		double TSS = 0.0;
		for (double d : yRealArray)
			TSS += Math.pow(d - yRealMean, 2.0);

		double RSS = 0.0;
		for (int i = 0; i < yRealArray.length; i++)
			RSS += Math.pow(yRealArray[i] - yCalcArray[i], 2.0);
		res.setR2(1.0 - RSS / TSS);
		res.setR(Math.pow(res.getR2(), 0.5));
		res.setRMSEC(Math.pow(RSS / n, 0.5));
		return res;
	}
}
