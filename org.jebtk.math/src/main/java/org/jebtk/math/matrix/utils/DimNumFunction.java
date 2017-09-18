package org.jebtk.math.matrix.utils;

public interface DimNumFunction {
	/**
	 * Apply a function to the data in a matrix dimension (e.g. a row or
	 * a column). For speed data may, but is not guaranteed to, reference
	 * data blocks from the underlying matrix so if manipulated could 
	 * modify the underlying matrix.
	 * 
	 * @param data
	 * @param offset
	 * @param l
	 * @param index
	 */
	public void apply(double[] data, int offset, int l, int index);
}
