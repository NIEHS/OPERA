package com.sciome.opera.calc.math;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import com.sciome.opera.enums.PretreatmentType;
import com.sciome.opera.model.PretreatmentResult;

public class DataPretreatment
{
	public static PretreatmentResult calculate(RealMatrix matrix, PretreatmentType type)
	{
		PretreatmentResult result = new PretreatmentResult();
		result.setPretType(type);
		List<Statistics> mStats = Utils.columnStatsOfMatrix(matrix);
		
		RealMatrix matrixScale;
		int rows = matrix.getRowDimension();
		int cols = matrix.getColumnDimension();

		if (type.equals(PretreatmentType.cent))
		{
			RealMatrix amat = createRealMatrixFromVectorStats(mStats, rows, cols, "mean");
			matrixScale = matrix.subtract(amat);
		}
		else if (type.equals(PretreatmentType.scal))
		{
			matrixScale = Utils.zeros(rows, cols);
			// loop through column stats
			for (int j = 0; j < mStats.size(); j++)
			{
				if (mStats.get(j).getStandardDeviation() <= 0.0)
					continue;
				for (int i = 0; i < rows; i++)
					matrixScale.setEntry(i, j, matrix.getEntry(i, j)
							/ mStats.get(j).getStandardDeviation());
			}

		}
		else if (type.equals(PretreatmentType.auto))
		{

			matrixScale = Utils.zeros(rows, cols);
			// loop through column stats
			for (int j = 0; j < mStats.size(); j++)
			{
				if (mStats.get(j).getStandardDeviation() <= 0.0)
					continue;
				for (int i = 0; i < rows; i++)
					matrixScale.setEntry(i, j, (matrix.getEntry(i, j) - mStats.get(j).getMean())
							/ mStats.get(j).getStandardDeviation());
			}
		}
		else if (type.equals(PretreatmentType.rang))
		{
			matrixScale = Utils.zeros(rows, cols);

			// loop through column stats
			for (int j = 0; j < mStats.size(); j++)
			{
				double diff = mStats.get(j).getMax() - mStats.get(j).getMin();
				if (diff <= 0.0)
					continue;
				// for each row set value for scaled matrix
				for (int i = 0; i < rows; i++)
					matrixScale.setEntry(i, j, (matrix.getData()[i][j] - mStats.get(j).getMin()) / diff);
			}
		}
		else
			matrixScale = matrix;

		result.setMatrix(matrixScale);
		result.setDescriptiveStatistics(mStats);
		return result;
	}

	private static RealMatrix createRealMatrixFromVectorStats(List<Statistics> mStats, int rows,
			int cols, String stat)
	{
		RealMatrix mat = MatrixUtils.createRealMatrix(rows, cols);
		// create a list of averages
		List<Double> rowList = new ArrayList<>();
		mStats.forEach(descStats ->
		{
			if (stat.equals("mean"))
				rowList.add(descStats.getMean());
			else if (stat.equals("std"))
				rowList.add(descStats.getStandardDeviation());
			else if (stat.equals("min"))
				rowList.add(descStats.getMin());
			else if (stat.equals("max"))
				rowList.add(descStats.getMax());
		});
		for (int i = 0; i < rows; i++)
		{
			// create real vector of the averages
			RealVector rv = MatrixUtils.createRealVector(Utils.convertCollectionToDoubleArray(rowList));
			mat.setRowVector(i, rv);

		}
		return mat;
	}
}
