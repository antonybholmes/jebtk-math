package org.jebtk.math.matrix;

public class MatrixReduceFunction {
	/**
	 * Apply a function to a row/column and return a statistic such as the
	 * mean.
	 * 
	 * @param index
	 * @param s
	 * @param l
	 * @param data
	 * @return
	 */
	public double apply(int index, int s, int l, double[] data) {
		return apply(index, data);
	}
	
	public double apply(int index, double[] data) {
		return Matrix.NULL_NUMBER;
	}
}
