package com.sciome.opera.calc.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class Utils
{
	public static List<Statistics> columnStatsOfMatrix(RealMatrix matrix)
	{
		List<Statistics> results = new ArrayList<Statistics>();
		for (int i = 0; i < matrix.getColumnDimension(); i++)
		{
			double[] col = matrix.getColumn(i);
			results.add(new Statistics(col));
		}

		return results;
	}

	public static List<Statistics> rowStatsOfMatrix(RealMatrix matrix)
	{
		List<Statistics> results = new ArrayList<Statistics>();

		for (int i = 0; i < matrix.getRowDimension(); i++)
		{
			double[] row = matrix.getRow(i);
			results.add(new Statistics(row));
		}

		return results;
	}
	
	public static double medianOfVector(RealVector vector) {
		double[] data = vector.toArray();
		Arrays.sort(data); 
		
		int n = data.length;
        // check for even case 
        if (n % 2 != 0) 
        	return (double)data[n / 2]; 
      
        return (double)(data[(n - 1) / 2] + data[n / 2]) / 2.0; 
	}

	public static double[] convertCollectionToDoubleArray(Collection<Double> list)
	{
		double[] returnArray = new double[list.size()];
		int index = 0;
		for (Double item : list)
			returnArray[index++] = item.doubleValue();

		return returnArray;

	}

	//It might be a good idea to move these into sciome commons math eventually
	public static RealVector vectorFromList(List<Double> list) {
		if(list.size() == 0)
			return null;
		
		double[] array = new double[list.size()];
		for(int i = 0; i < array.length; i++) {
			array[i] = list.get(i);
		}
		
		return MatrixUtils.createRealVector(array);
	}
	
	public static RealVector vectorFromStringList(List<String> list) {
		if(list.size() == 0)
			return null;
		
		double[] array = new double[list.size()];
		for(int i = 0; i < array.length; i++) {
			array[i] = Double.parseDouble(list.get(i));
		}
		
		return MatrixUtils.createRealVector(array);
	}
	
	public static RealVector vectorFromIntegerList(List<Integer> list) {
		if(list.size() == 0)
			return null;
		
		double[] array = new double[list.size()];
		for(int i = 0; i < array.length; i++) {
			array[i] = list.get(i);
		}
		
		return MatrixUtils.createRealVector(array);
	}
	
	public static RealMatrix matrixFromList(List<List<Double>> list) {
		if(list.size() == 0)
			return null;
		
		double[][] array = new double[list.size()][list.get(0).size()];
		for(int i = 0; i < array.length; i++) {
			for(int j = 0; j < array[i].length; j++) {
				array[i][j] = list.get(i).get(j);
			}
		}
		
		return MatrixUtils.createRealMatrix(array);
	}
	
	public static RealMatrix rowRealMatrixFromList(List<Double> list) {
		if(list.size() == 0)
			return null;
		
		double[] array = new double[list.size()];
		for(int i = 0; i < array.length; i++) {
			array[i] = list.get(i);
		}
		
		return MatrixUtils.createRowRealMatrix(array);
	}
	
	public static RealMatrix rowRealMatrixFromIntegerList(List<Integer> list) {
		if(list.size() == 0)
			return null;
		
		double[] array = new double[list.size()];
		for(int i = 0; i < array.length; i++) {
			array[i] = list.get(i);
		}
		
		return MatrixUtils.createRowRealMatrix(array);
	}
	
	/**
	 * Creates a matrix with uniformly distributed random values between 0.0 and 1.0
	 * @param rows The number of rows in the matrix
	 * @param cols The number of columns in the matrix
	 * @param seed The seed to generate random values from
	 * @return The randomly generated matrix
	 */
	public static RealMatrix createRandomMatrix(int rows, int cols, int seed) {
		RealMatrix perms = MatrixUtils.createRealMatrix(rows, cols);
		Random rand = new Random(seed);
		
		//Randomize the values in the matrix
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				perms.addToEntry(i, j, rand.nextDouble());
			}
		}
		return perms;
	}
	
	/**
	 * Ranks each row in the matrix based on the value of increasing and returns the resulting matrix
	 * Ties are broken by their position in the matrix
	 * Example: {{0.1, 0.2, 0.3}, {0.3, 0.2, 0.1}} -> {{1, 2, 3}, {3, 2, 1}}
	 * @param matrix The matrix to rank
	 * @param increasing Whether to rank the matrix in increasing or decreasing order
	 * @return The ranked matrix
	 */
	public static RealMatrix rankMatrix(RealMatrix matrix, boolean increasing) {
		RealMatrix rankedMatrix = MatrixUtils.createRealMatrix(matrix.getRowDimension(), matrix.getColumnDimension());
		for(int i = 0; i < matrix.getRowDimension(); i++) {
			rankedMatrix.setRow(i, rankVector(MatrixUtils.createRealVector(matrix.getRow(i)), increasing).toArray());
		}
		return rankedMatrix;
	}
	
	/**
	 * Ranks a vector in each row 
	 * @param vector 
	 * @param increasing Whether to rank the vector in increasing or decreasing order
	 * @return The ranked vector
	 */
	public static RealVector rankVector(RealVector vector, boolean increasing) {
		double[] currentRow = vector.toArray();
		double[] newRow = new double[vector.getDimension()];
		if(increasing) {
			for(int j = 0; j < currentRow.length; j++) {
				double currValue = currentRow[j];
				int counter = 1;
				for(int k = 0; k < currentRow.length; k++) {
					if(k != j && (currValue > currentRow[k] || (currValue == currentRow[k] && j < k))) {
						counter++;
					}
				}
				newRow[j] = counter;
			}
		} else {
			for(int j = 0; j < currentRow.length; j++) {
				double currValue = currentRow[j];
				int counter = 1;
				for(int k = 0; k < currentRow.length; k++) {
					if(k != j && (currValue < currentRow[k] || (currValue == currentRow[k] && j < k))) {
						counter++;
					}
				}
				newRow[j] = counter;
			}
		}
		return MatrixUtils.createRealVector(newRow);
	}
	
	/**
	 * Returns a collection of index vectors that are produced from sorting. Assumes starting with index of 1.
	 * e.g. (3.4, 1.9, 5.3) -> (2, 1, 3) if in increasing order
	 * @param vector The vector you want to get sort indices of
	 * @param increasing Whether to rank the vector in increasing or decreasing order
	 * @return The sorted index vector
	 */
	public static RealVector sortVectorIndices(RealVector vector, boolean increasing) {
		double[] currentRow = vector.toArray();
		
		//Initialize index array
		double[] indexArray = new double[currentRow.length];
		for(int i = 0; i < indexArray.length; i++) {
			indexArray[i] = i + 1;
		}
		
		int n = currentRow.length; 
    	for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n-i-1; j++) {
                if (currentRow[j] > currentRow[j+1])  { 
                    // swap arr[j+1] and arr[i] 
                	double indexTemp = indexArray[j];
                    double temp = currentRow[j]; 
                    currentRow[j] = currentRow[j+1]; 
                    currentRow[j+1] = temp;
                    indexArray[j] = indexArray[j + 1];
                    indexArray[j + 1] = indexTemp;
                } 
            }
    	}
		return MatrixUtils.createRealVector(indexArray);
	}
	
	/**
	 * Takes a vector and removes all duplicate values and returns the sorted vector back
	 * @param vector The vector to remove duplicate values from
	 * @return The new vector with duplicates removed
	 */
	public static RealVector removeDuplicates(RealVector vector) {
		//Remove duplicate values from y and put them into a new vector
		Set<Double> uniqueSet = new HashSet<Double>();
		for(int i = 0; i < vector.getDimension(); i++) {
			uniqueSet.add(vector.getEntry(i));
		}
		
		//Create a double array with the set values
		double[] uniqueData = new double[uniqueSet.size()];
		Iterator<Double> it = uniqueSet.iterator();
		int counter = 0;
		while(it.hasNext()) {
			uniqueData[counter] = it.next().doubleValue();
			counter++;
		}
		Arrays.sort(uniqueData);
		return MatrixUtils.createRealVector(uniqueData);
	}
	
	/**
	 * Calculates the sum of all the rows in the matrix and returns a vector with the results
	 * @param matrix The matrix to do the calculations on
	 * @return A Vector with the sum of each row
	 */
	public static RealVector rowSums(RealMatrix matrix) {
		double[] vector = new double[matrix.getRowDimension()];
		for(int i = 0; i < matrix.getRowDimension(); i++) {
			double sum = 0;
			for(int j = 0; j < matrix.getColumnDimension(); j++) {
				sum += matrix.getEntry(i, j);
			}
			vector[i] = sum;
		}
		return MatrixUtils.createRealVector(vector);
	}
	
	/**
	 * Calculates the sum of all the columns in the matrix and returns a vector with the results
	 * @param matrix The matrix to do the calculations on
	 * @return A Vector with the sum of each column
	 */
	public static RealVector columnSums(RealMatrix matrix) {
		double[] vector = new double[matrix.getColumnDimension()];
		for(int i = 0; i < matrix.getColumnDimension(); i++) {
			double sum = 0;
			for(int j = 0; j < matrix.getRowDimension(); j++) {
				sum += matrix.getEntry(j, i);
			}
			vector[i] = sum;
		}
		return MatrixUtils.createRealVector(vector);
	}
	
	/**
	 * Finds the sum of all the values in the vector
	 * @param vector The vector to find the sum of
	 * @return The sum of the vector
	 */
	public static double vectorSum(RealVector vector) {
		double sum = 0;
		for(int i = 0; i < vector.getDimension(); i++) {
			sum += vector.getEntry(i);
		}
		return sum;
	}
	
	/**
	 * Finds the sum of all the values in the matrix
	 * @param matrix The matrix to find the sum of
	 * @return The sum of the matrix
	 */
	public static double matrixSum(RealMatrix matrix) {
		double sum = 0;
		for(int i = 0; i < matrix.getRowDimension(); i++) {
			for(int j = 0; j < matrix.getColumnDimension(); j++) {
				sum += matrix.getEntry(i, j);
			}
		}
		return sum;
	}
	
	/**
	 * Compares two matrices given a tolerance to see if they are equal
	 * @param Vector The Vector to format
	 * @param tolerance The amount of difference between the two is allowed per value
	 */
	public static boolean compareVector(RealVector a, RealVector b, double tolerance) {
		for(int i = 0; i < a.getDimension(); i++) {
			if(Math.abs(a.getEntry(i) - b.getEntry(i)) > tolerance)
				return false;
		}
		return true;
	}
	
	/**
	 * Compares two matrices given a tolerance to see if they are equal
	 * @param matrix The matrix to format
	 * @param tolerance The amount of difference between the two is allowed per value
	 */
	public static boolean compareMatrix(RealMatrix a, RealMatrix b, double tolerance) {
		for(int i = 0; i < a.getRowDimension(); i++) {
			for(int j = 0; j < a.getColumnDimension(); j++) {
				if(Math.abs(a.getEntry(i, j) - b.getEntry(i, j)) > tolerance)
					return false;
			}
		}
		return true;
	}
	
	/**
	 * Takes the square root of every element in the vector and returns the result
	 * @param vector The vector to square root
	 * @return The vector after the square root is taken
	 */
	public static RealVector squareRootVector(RealVector vector) {
		double[] data = new double[vector.getDimension()];
		for(int i = 0; i < vector.getDimension(); i++) {
			data[i] = Math.sqrt(vector.getEntry(i));
		}
		return MatrixUtils.createRealVector(data);
	}
	
	/**
	 * Takes the square root of every element in the matrix and returns the result
	 * @param vector The matrix to square root
	 * @return The matrix after the square root is taken
	 */
	public static RealMatrix squareRootMatrix(RealMatrix matrix) {
		double[][] data = new double[matrix.getRowDimension()][matrix.getColumnDimension()];
		for(int i = 0; i < matrix.getRowDimension(); i++) {
			for(int j = 0; j < matrix.getColumnDimension(); j++) {
				data[i][j] = Math.sqrt(matrix.getEntry(i, j));
			}
		}
		return MatrixUtils.createRealMatrix(data);
	}
	
	/**
	 * Takes the absolute value of every element in the vector and returns the result
	 * @param vector The vector to take the absolute value of
	 * @return The vector after the absolute value is taken
	 */
	public static RealVector absVector(RealVector vector) {
		double[] data = new double[vector.getDimension()];
		for(int i = 0; i < vector.getDimension(); i++) {
			data[i] = Math.abs(vector.getEntry(i));
		}
		return MatrixUtils.createRealVector(data);
	}
	
	/**
	 * Takes each element in the vector to a power
	 * @param vector The vector to do the exponentiation of
	 * @param exp The power to exponentiate the vector by
	 * @return The vector after the exponentiation is done
	 */
	public static RealVector expVector(RealVector vector, double exp) {
		double[] data = new double[vector.getDimension()];
		for(int i = 0; i < vector.getDimension(); i++) {
			data[i] = Math.pow(vector.getEntry(i), exp);
		}
		return MatrixUtils.createRealVector(data);
	}
	
	/**
	 * Takes each element in the vector to a power
	 * @param vector The vector to do the exponentiation of
	 * @param exp The power to exponentiate the vector by
	 * @return The vector after the exponentiation is done
	 */
	public static RealMatrix expMatrix(RealMatrix matrix, double exp) {
		double[][] data = new double[matrix.getRowDimension()][matrix.getColumnDimension()];
		for(int i = 0; i < matrix.getRowDimension(); i++) {
			for(int j = 0; j < matrix.getColumnDimension(); j++) {
				data[i][j] = Math.pow(matrix.getEntry(i, j), exp);
			}
		}
		return MatrixUtils.createRealMatrix(data);
	}
	
	/**
	 * Takes the inverse of every element in the vector and returns the result
	 * @param vector The vector to inverse
	 * @return The vector after the inverse is taken
	 */
	public static RealVector inverseVector(RealVector vector) {
		double[] data = new double[vector.getDimension()];
		for(int i = 0; i < vector.getDimension(); i++) {
			data[i] = 1 / vector.getEntry(i);
		}
		return MatrixUtils.createRealVector(data);
	}
	
	/**
	 * Returns the diagonal elements of the matrix in a vector
	 * @param matrix The matrix to get the diagonal elements from
	 * @return A vector with the diagonal elements
	 */
	public static RealVector diag(RealMatrix matrix) {
		int length = matrix.getColumnDimension();
		if(matrix.getRowDimension() < length) {
			length = matrix.getRowDimension();
		}
		double[] temp = new double[length];
		for(int i = 0 ; i < length; i++) {
			temp[i] = matrix.getEntry(i, i);
		}
		return MatrixUtils.createRealVector(temp);
	}
	
	/**
	 * Element by element multiplication of 2 matrices
	 * @param a The first matrix
	 * @param b The second matrix
	 * @return The result of the multiplication
	 * @throws IllegalArgumentException if the two matrices are not the same dimension
	 */
	public static RealMatrix ebeMultiply(RealMatrix a, RealMatrix b) throws IllegalArgumentException {
		if(a.getColumnDimension() != b.getColumnDimension() ||
		   a.getRowDimension() != b.getRowDimension()) {
			throw new IllegalArgumentException("Matrices must be of the same dimension");
		}
		double[][] result = new double[a.getRowDimension()][a.getColumnDimension()];
		for(int i = 0; i < result.length; i++) {
			for(int j = 0; j < result[i].length; j++) {
				result[i][j] = a.getEntry(i, j) * b.getEntry(i, j);
			}
		}
		return MatrixUtils.createRealMatrix(result);
	}
	
	/**
	 * Takes a vector and returns the cumulative minimum of it (same as R cummin)
	 * Ex: {4, 3, 5, 2, 1, 6} -> {4, 3, 3, 2, 1, 1}
	 * @param vec The vector to take the cumulative miniumum of
	 * @return The cumulative minimum of the vector
	 */
	public static RealVector cummin(RealVector vec) {
		int length = vec.getDimension();
		double[] data = new double[length];
		double min = Double.MAX_VALUE;
		for(int i = 0; i < length; i++) {
			double value = vec.getEntry(i);
			if(value < min)
				min = value;
			data[i] = min;
		}
		return MatrixUtils.createRealVector(data);
	}
	
	/**
	 * Creates a matrix filled with ones with given dimensions (similar to matlab function)
	 * @param row The number of rows in the matrix
	 * @param columns The number of columns in the matrix
	 * @return A matrix with the given size filled with ones
	 */
	public static RealMatrix ones(int row, int columns) {
		double[][] mat = new double[row][columns];
		for(int i = 0; i < row; i++) {
			for(int j = 0; j < columns; j++) {
				mat[i][j] = 1;
			}
		}
		return MatrixUtils.createRealMatrix(mat);
	}
	
	/**
	 * Creates a vector filled with ones with given dimensions (similar to matlab function)
	 * @param size The size of the vector
	 * @return A vector with the given size filled with ones
	 */
	public static RealVector ones(int size) {
		double[] mat = new double[size];
		for(int i = 0; i < size; i++) {
			mat[i] = 1;
		}
		return MatrixUtils.createRealVector(mat);
	}
	
	/**
	 * Creates a matrix filled with ones with given dimensions (similar to matlab function)
	 * @param row The number of rows in the matrix
	 * @param columns The number of columns in the matrix
	 * @return A matrix with the given size filled with zeros
	 */
	public static RealMatrix zeros(int row, int columns) {
		double[][] mat = new double[row][columns];
		return MatrixUtils.createRealMatrix(mat);
	}
	
	/**
	 * Creates a vector filled with ones with given dimensions (similar to matlab function)
	 * @param size The size of the vector
	 * @return A vector with the given size filled with zeros
	 */
	public static RealVector zeros(int size) {
		double[] mat = new double[size];
		return MatrixUtils.createRealVector(mat);
	}
	
	public static RealMatrix repmat(RealMatrix matrix, int rowDimension, int colDimension) {
		double[][] temp = new double[matrix.getRowDimension() * rowDimension][matrix.getColumnDimension() * colDimension];
		for(int i = 0; i < temp.length; i++) {
			for(int j = 0; j < temp[i].length; j++) {
				temp[i][j] = matrix.getEntry(i % matrix.getRowDimension(), j % matrix.getColumnDimension());
			}
		}
		return MatrixUtils.createRealMatrix(temp);
	}
	
	public static double[] filter(double[] b, double[] a, double[] signal) {
        double[] result = new double[signal.length];
        for (int i = 0; i < signal.length; ++i) {
            double tmp = 0.0;
            for (int j = 0; j < b.length; ++j) {
                if (i - j < 0) continue;
                tmp += b[j] * signal[i - j];
            }
            for (int j = 1; j < a.length; ++j) {
                if (i - j < 0) continue;
                tmp -= a[j] * result[i - j];
            }
            tmp /= a[0];
            result[i] = tmp;
        }
        return result;
    }
	
	/**
	 * Counts the number of unique values in a vector
	 * @param vector The input vector
	 * @return The number of unique values in the vector
	 */
	public static int uniqueValues(RealVector vector) {
		Set<Double> set = new HashSet<Double>();
		for(int i = 0; i < vector.getDimension(); i++) {
			set.add(vector.getEntry(i));
		}
		return set.size();
	}
	
	/**
	 * Re-scales the vector such that the sum of the components in the vector is equal to 1
	 * @param vector
	 * @return The standardized vector
	 */
	public static RealVector scaleVector(RealVector vector) {
		double sum = vectorSum(vector);
		return vector.mapDivide(sum);
	}
}
