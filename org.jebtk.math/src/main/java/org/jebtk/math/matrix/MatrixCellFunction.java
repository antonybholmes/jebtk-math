package org.jebtk.math.matrix;

public interface MatrixCellFunction {
	/**
	 * Apply a function to a matrix cell.
	 * 
	 * @param row
	 * @param col
	 * @param value
	 * @return
	 */
	public double apply(int row, int col, double value);
}
