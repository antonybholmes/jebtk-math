/**
 * Copyright (C) 2016, Antony Holmes
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. Neither the name of copyright holder nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jebtk.math.matrix;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jebtk.core.Indexed;
import org.jebtk.core.Mathematics;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.text.TextUtils;


// TODO: Auto-generated Javadoc
/**
 * Basis for a numerical matrix. Note that Double.NaN is used
 * to indicate an unset/absent value, so valid matrices should not contain
 * this value (it is ignored by matrix operations).
 *
 * @author Antony Holmes Holmes
 */
public abstract class Matrix extends MatrixEventListeners {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;


	/** The Constant NULL_NUMBER. */
	public static final double NULL_NUMBER = Double.NaN;

	/** The Constant NULL_INT_NUMBER. */
	public static final int NULL_INT_NUMBER = Integer.MIN_VALUE;

	public static final long NULL_LONG_NUMBER = Long.MIN_VALUE;


	/** The Constant NUMBER_MATRIX_TYPES. */
	private static final Set<CellType> NUMBER_MATRIX_TYPES = 
			CollectionUtils.toSet(CellType.NUMBER);

	/** The Constant TEXT_MATRIX_TYPES. */
	private static final Set<CellType> TEXT_MATRIX_TYPES = 
			CollectionUtils.toSet(CellType.TEXT);




	/** The m size. */
	protected int mSize;


	/**
	 * Gets the row count.
	 *
	 * @return the row count
	 */
	public abstract int getRowCount();

	/**
	 * Gets the column count.
	 *
	 * @return the column count
	 */
	public abstract int getColumnCount();


	/**
	 * Instantiates a new matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 */
	public Matrix(int rows, int columns) {
		mSize = rows * columns;

		createData(rows, columns, mSize);
	}

	/**
	 * Guaranteed to be called first so can be used to initialize
	 * internal data structures before trying to set values etc.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @param size 		The number of elements (rows x columns).
	 */
	protected void createData(int rows, int columns, int size) {
		// Do nothing
	}

	/**
	 * Returns the type of the matrix indicating whether it store numbers,
	 * text or both.
	 *
	 * @return the type
	 */
	public MatrixType getType() {
		return MatrixType.NUMBER;
	}

	/**
	 * Returns the total number of elements in the matrix.
	 *
	 * @return the num cells
	 */
	public int getNumCells() {
		return mSize;
	}

	/**
	 * Returns a copy the matrix.
	 *
	 * @return the matrix
	 */
	public Matrix copy() {
		return this;
	}

	/**
	 * Sets the value.
	 *
	 * @param row the row
	 * @param column the column
	 * @param v the v
	 */
	public void set(int row, int column, double v) {
		update(row, column, v);

		fireMatrixChanged();
	}

	/**
	 * Update a cell with a number value, but does not trigger any events.
	 *
	 * @param row the row
	 * @param column the column
	 * @param value the value
	 */
	public void update(int row, int column, double v) {
		fireMatrixChanged();
	}

	/**
	 * Update value.
	 *
	 * @param value the value
	 */
	public void update(double value) {
		for (int i = 0; i < getRowCount(); ++i) {
			for (int j = 0; j < getColumnCount(); ++j) {
				update(i, j, value);
			}
		}
		
		fireMatrixChanged();
	}

	/**
	 * Sets the all values in the matrix to a given number and triggers
	 * a change event.
	 *
	 * @param v the new value
	 */
	public void set(double v) {
		update(v);

		fireMatrixChanged();
	}

	/**
	 * Sets the text.
	 *
	 * @param row the row
	 * @param column the column
	 * @param v the v
	 */
	public void set(int row, int column, String v) {
		update(row, column, v);

		fireMatrixChanged();
	}

	/**
	 * Update a cell with a string.
	 *
	 * @param row the row
	 * @param column the column
	 * @param value the value
	 */
	public void update(int row, int column, String value) {
		fireMatrixChanged();
	}

	/**
	 * Update text.
	 *
	 * @param value the value
	 */
	public void update(String value) {
		for (int i = 0; i < getRowCount(); ++i) {
			for (int j = 0; j < getColumnCount(); ++j) {
				update(i, j, value);
			}
		}
		
		fireMatrixChanged();
	}


	/**
	 * Sets the text.
	 *
	 * @param v the new text
	 */
	public void set(String v) {
		update(v);

		fireMatrixChanged();
	}

	/**
	 * Set a cell element by analyzing the object type.
	 *
	 * @param row the row
	 * @param column the column
	 * @param value the value
	 */
	public void set(int row, int column, Object value) {
		update(row, column, value);

		fireMatrixChanged();
	}



	/**
	 * Update cell with a value. If the value can be converted to a number
	 * update the cell as a number, else try to update as a string
	 *
	 * @param row the row
	 * @param column the column
	 * @param value the value
	 */
	public void update(int row, int column, Object value) {
		if (value != null) {
			if (value instanceof Number) {
				update(row, column, ((Number)value).doubleValue());
			} else {
				String s = value.toString();

				try {
					// First try to parse the string as number.
					update(row, column, TextUtils.parseDouble(s));
				} catch (ParseException e) {
					// If that fails, use the string value for the cell value.
					update(row, column, s);
				}
			}
		}
		
		fireMatrixChanged();
	}

	/**
	 * Copy the values from a matrix to this matrix.
	 *
	 * @param m the m
	 */
	public void update(Matrix m) {
		int r = Math.min(getRowCount(), m.getRowCount());
		int c = Math.min(getColumnCount(), m.getColumnCount());

		for (int i = 0; i < r; ++i) {
			for (int j = 0; j < c; ++j) {
				set(i, j, m.get(i, j));
			}
		}
		
		fireMatrixChanged();
	}

	/**
	 * Copy the values from a matrix to this matrix.
	 *
	 * @param m the m
	 */
	public void set(Matrix m) {
		update(m);

		fireMatrixChanged();
	}


	/**
	 * Sets the column.
	 *
	 * @param column the column
	 * @param values the values
	 */
	public void setColumn(int column, List<? extends Object> values) {
		for (int i = 0; i < Math.min(getRowCount(), values.size()); ++i) {
			set(i, column, values.get(i));
		}
		
		fireMatrixChanged();
	}
	
	public void setColumn(int column, Object[] values) {
		for (int i = 0; i < Math.min(getRowCount(), values.length); ++i) {
			set(i, column, values[i]);
		}
		
		fireMatrixChanged();
	}
	
	public void setColumn(int column, double[] values) {
		for (int i = 0; i < Math.min(getRowCount(), values.length); ++i) {
			set(i, column, values[i]);
		}
		
		fireMatrixChanged();
	}

	/**
	 * Sets the value column.
	 *
	 * @param column the column
	 * @param values the values
	 */
	public void setValueColumn(int column, List<Double> values) {
		for (int i = 0; i < Math.min(getRowCount(), values.size()); ++i) {
			set(i, column, values.get(i));
		}
		
		fireMatrixChanged();
	}

	/**
	 * Sets the text column.
	 *
	 * @param column the column
	 * @param values the values
	 */
	public void setTextColumn(int column, List<String> values) {
		for (int i = 0; i < Math.min(getRowCount(), values.size()); ++i) {
			set(i, column, values.get(i));
		}
		
		fireMatrixChanged();
	}

	/**
	 * Sets the row.
	 *
	 * @param row the row
	 * @param values the values
	 */
	public void setRow(int row, List<? extends Object> values) {
		for (int i = 0; i < Math.min(getColumnCount(), values.size()); ++i) {
			set(row, i, values.get(i));
		}
		
		fireMatrixChanged();
	}
	
	public void setRow(int row, Object[] values) {
		for (int i = 0; i < Math.min(getColumnCount(), values.length); ++i) {
			set(row, i, values[i]);
		}
		
		fireMatrixChanged();
	}
	
	public void setRow(int row, double[] values) {
		for (int i = 0; i < Math.min(getRowCount(), values.length); ++i) {
			set(row, i, values[i]);
		}
		
		fireMatrixChanged();
	}

	/**
	 * Sets the value row.
	 *
	 * @param row the row
	 * @param values the values
	 */
	public void setValueRow(int row, List<Double> values) {
		for (int i = 0; i < Math.min(getColumnCount(), values.size()); ++i) {
			set(row, i, values.get(i));
		}
		
		fireMatrixChanged();
	}

	/**
	 * Sets the text row.
	 *
	 * @param row the row
	 * @param values the values
	 */
	public void setTextRow(int row, List<String> values) {
		for (int i = 0; i < Math.min(getColumnCount(), values.size()); ++i) {
			set(row, i, values.get(i));
		}
		
		fireMatrixChanged();
	}

	/**
	 * Copy the row from the "from" matrix to this matrix.
	 *
	 * @param from the from
	 * @param row the row
	 * @param toRow the to row
	 */
	public void copyRow(final Matrix from, 
			int row,
			int toRow) {
		int c = Math.min(from.getColumnCount(), getColumnCount());

		for (int i = 0; i < c; ++i) {
			set(toRow, i, from.get(row, i));
		}
	}

	/**
	 * Copy row.
	 *
	 * @param from the from
	 * @param row the row
	 * @param toRow the to row
	 */
	public void copyRow(final DoubleMatrix from, 
			int row,
			int toRow) {
		int c = Math.min(from.getColumnCount(), getColumnCount());

		for (int i = 0; i < c; ++i) {
			set(toRow, i, from.getValue(row, i));
		}
	}

	/**
	 * Copy row.
	 *
	 * @param from the from
	 * @param row the row
	 * @param toRow the to row
	 */
	public void copyRow(final TextMatrix from, 
			int row,
			int toRow) {
		int c = Math.min(from.getColumnCount(), getColumnCount());

		for (int i = 0; i < c; ++i) {
			set(toRow, i, from.getText(row, i));
		}
	}

	/**
	 * Copy row.
	 *
	 * @param from the from
	 * @param row the row
	 * @param toRow the to row
	 */
	public void copyRow(final MixedMatrix from, 
			int row,
			int toRow) {
		int c = Math.min(from.getColumnCount(), getColumnCount());

		for (int i = 0; i < c; ++i) {
			set(toRow, i, from.getText(row, i));
		}

		for (int i = 0; i < c; ++i) {
			set(toRow, i, from.getValue(row, i));
		}
	}

	/**
	 * Copy a column from one matrix to another.
	 *
	 * @param from the from
	 * @param column the column
	 */
	public void copyColumn(final Matrix from, int column) {
		copyColumn(from, column, column);
	}

	/**
	 * Copy column.
	 *
	 * @param from the from
	 * @param column the column
	 * @param toColumn the to column
	 */
	public void copyColumn(final Matrix from, 
			int column,
			int toColumn) {
		if (from instanceof DoubleMatrix) {
			copyColumn((DoubleMatrix)from, column, toColumn);
		} else if (from instanceof TextMatrix) { 
			copyColumn((TextMatrix)from, column, toColumn);
		} else {
			int r = Math.min(from.getRowCount(), getRowCount());

			for (int i = 0; i < r; ++i) {
				set(i, toColumn, from.get(i, column));
			}
		}
	}

	/**
	 * Copy column.
	 *
	 * @param from the from
	 * @param column the column
	 * @param toColumn the to column
	 */
	public void copyColumn(final DoubleMatrix from, 
			int column,
			int toColumn) {
		int r = Math.min(from.getRowCount(), getRowCount());

		for (int i = 0; i < r; ++i) {
			set(i, toColumn, from.getValue(i, column));
		}
	}

	/**
	 * Copy column.
	 *
	 * @param from the from
	 * @param column the column
	 * @param toColumn the to column
	 */
	public void copyColumn(final TextMatrix from, 
			int column,
			int toColumn) {
		int r = Math.min(from.getRowCount(), getRowCount());

		for (int i = 0; i < r; ++i) {
			set(i, toColumn, from.getText(i, column));
		}
	}

	/**
	 * Copy column.
	 *
	 * @param from the from
	 * @param column the column
	 * @param toColumn the to column
	 */
	public void copyColumn(final MixedMatrix from, 
			int column,
			int toColumn) {
		int r = Math.min(from.getRowCount(), getRowCount());

		for (int i = 0; i < r; ++i) {
			set(i, toColumn, from.getText(i, column));
		}

		for (int i = 0; i < r; ++i) {
			set(i, toColumn, from.getValue(i, column));
		}
	}

	/**
	 * Should return the numerical value at a cell location or
	 * Double.NaN if the cell is not in use.
	 *
	 * @param row the row
	 * @param column the column
	 * @return the value
	 */
	public double getValue(int row, int column) {
		return Double.NaN;
	}

	/**
	 * Gets the text.
	 *
	 * @param row the row
	 * @param column the column
	 * @return the text
	 */
	public String getText(int row, int column) {
		return TextUtils.EMPTY_STRING;
	}

	/**
	 * Gets the.
	 *
	 * @param row the row
	 * @param column the column
	 * @return the object
	 */
	public Object get(int row, int column) {
		if (getCellType(row, column) == CellType.NUMBER) {
			return getValue(row, column);
		} else {
			return getText(row, column);
		}
	}

	/**
	 * Should return whether the cell contains a  number or string.
	 *
	 * @param row the row
	 * @param column the column
	 * @return the cell type
	 */
	public CellType getCellType(int row, int column) {
		return CellType.NUMBER;
	}

	/**
	 * Should reset a cell to its null value.
	 *
	 * @param row the row
	 * @param column the column
	 */
	public final void setToNull(int row, int column) {
		updateToNull(row, column);

		fireMatrixChanged();
	}

	/**
	 * Should reset a cell to its null value.
	 *
	 * @param row the row
	 * @param column the column
	 */
	public void updateToNull(int row, int column) {
		// Do nothing
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder("[").append(getRowCount()).append(" x ").append(getColumnCount()).append("]");

		return buffer.toString();
	}

	/**
	 * Converts a matrix column into a list.
	 *
	 * @param column the column
	 * @return the list
	 */
	public Object[] columnAsList(int column) {
		Object[] values = new Object[getRowCount()];

		for (int row = 0; row < getRowCount(); ++row) {
			values[row] = get(row, column);
		}

		return values;
	}

	/**
	 * Converts a matrix column to a list of strings.
	 *
	 * @param column the column
	 * @return the list
	 */
	public List<String> columnAsText(int column) {
		List<String> values = new ArrayList<String>(getRowCount());

		for (int row = 0; row < getRowCount(); ++row) {
			values.add(getText(row, column));
		}

		return values;
	}

	/**
	 * Converts a matrix column to a list of doubles. If a cell is unset/NaN
	 * this will be included. Recommended for use on columns containing only
	 * numbers.
	 *
	 * @param column the column
	 * @return the double[]
	 */
	public double[] columnAsDouble(int column) {
		int r = getRowCount();

		double[] ret = new double[r];

		columnAsDouble(column, ret);

		return ret;
	}
	
	public void columnAsDouble(int column, double[] data) {
		int r = getRowCount();

		for (int row = 0; row < r; ++row) {
			double v = getValue(row, column);

			//if (Mathematics.isValidNumber(v)) {
			data[row] = v;
			//}
		}
	}

	/**
	 * Converts a matrix row into a list.
	 *
	 * @param row the row
	 * @return the list
	 */
	public Object[] rowAsList(int row) {
		int n = getColumnCount();

		Object[] ret = new Object[n];

		for (int c = 0; c < n; ++c) {
			ret[c] = get(row, c);
		}

		return ret;
	}

	/**
	 * Row as double.
	 *
	 * @param row the row
	 * @return the double[]
	 */
	public double[] rowAsDouble(int row) {
		int n = getColumnCount();

		double[] ret = new double[n];

		rowAsDouble(row, ret);

		return ret;
	}
	
	public void rowAsDouble(int row, double[] data) {
		int n = getColumnCount();

		for (int c = 0; c < n; ++c) {
			data[c] = getValue(row, c);
		}
	}

	/**
	 * Row to string.
	 *
	 * @param row the row
	 * @return the list
	 */
	public List<String> rowAsText(int row) {
		List<String> values = new ArrayList<String>(getColumnCount());

		for (int c = 0; c < getColumnCount(); ++c) {
			values.add(getText(row, c));
		}

		return values;
	}

	public abstract Matrix transpose();

	/**
	 * Dot.
	 *
	 * @param m the m
	 * @return the matrix
	 */
	public Matrix dot(Matrix m) {
		dot(this, m);

		fireMatrixChanged();

		return this;
	}

	public static void dot(Matrix m1, Matrix m2) {
		for (int i = 0; i < m1.getRowCount(); ++i) {
			for (int j = 0; j < m1.getColumnCount(); ++j) {
				m1.set(i, j, m1.getValue(i, j) * m2.getValue(i, j));
			}
		}
	}

	public Matrix applied(MatrixCellFunction f) {
		// Copy the matrix
		Matrix ret = copy();

		ret.apply(f);

		return ret;
	}
	
	public Matrix rowApplied(MatrixCellFunction f, int row) {
		// Copy the matrix
		Matrix ret = copy();

		ret.rowApply(f, row);

		return ret;
	}
	
	public Matrix colApplied(MatrixCellFunction f, int col) {
		// Copy the matrix
		Matrix ret = copy();

		ret.colApply(f, col);

		return ret;
	}

	public void apply(MatrixCellFunction f) {
		for (int i = 0; i < getRowCount(); ++i) {
			for (int j = 0; j < getColumnCount(); ++j) {
				double v = getValue(i, j);

				if (Mathematics.isValidNumber(v)) {
					set(i, j, f.apply(i, j, v));
				}
			}
		}

		fireMatrixChanged();
	}
	
	public void rowApply(MatrixCellFunction f) {
		for (int i = 0; i < getRowCount(); ++i) {
			rowApply(f, i);
		}
	}

	public void rowApply(MatrixCellFunction f, int row) {
		for (int i = 0; i < getColumnCount(); ++i) {
			double v = getValue(row, i);

			if (Mathematics.isValidNumber(v)) {
				set(row, i, f.apply(row, i, v));
			}
		}

		fireMatrixChanged();
	}
	
	public void rowApply(MatrixDimFunction f) {
		int c = getColumnCount();
		
		double[] data = new double[c];
		double[] ret = new double[c];
		
		for (int i = 0; i < getRowCount(); ++i) {
			rowAsDouble(i, data);
			f.apply(i, data, ret);
			setRow(i, ret);
		}
	}
	
	public void rowApply(MatrixDimFunction f, int row) {
		double[] data = rowAsDouble(row);
		double[] ret = new double[data.length];
		
		f.apply(row, data, ret);
		
		setRow(row, ret);
	}
	
	public void colApply(MatrixCellFunction f, int col) {
		for (int i = 0; i < getRowCount(); ++i) {
			double v = getValue(i, col);

			if (Mathematics.isValidNumber(v)) {
				set(i, col, f.apply(i, col, v));
			}
		}

		fireMatrixChanged();
	}
	
	public void colApply(MatrixDimFunction f) {
		int r = getRowCount();
		
		double[] data = new double[r];
		double[] ret = new double[r];
		
		for (int i = 0; i < getColumnCount(); ++i) {
			columnAsDouble(i, data);
			f.apply(i, data, ret);
			setColumn(i, ret);
		}
		
		fireMatrixChanged();
	}
	
	public void colApply(MatrixDimFunction f, int col) {
		double[] data = columnAsDouble(col);
		double[] ret = new double[data.length];
		
		f.apply(col, data, ret);
		
		setColumn(col, ret);
		
		fireMatrixChanged();
	}
	
	public void rowEval(MatrixReduceFunction f, double[] ret) {
		for (int i = 0; i < getRowCount(); ++i) {
			double[] data = rowAsDouble(i);

			ret[i] = f.apply(i, data);
		}
	}
	
	public void rowEval(MatrixDimFunction f, int row, double[] ret) {
		double[] data = rowAsDouble(row);
		
		f.apply(row, data, ret);
	}
	
	public void colEval(MatrixDimFunction f, int col, double[] ret) {
		double[] data = rowAsDouble(col);
		
		f.apply(col, data, ret);
	}
	
	/**
	 * Evaluate the a function across each column.
	 * 
	 * @param f
	 * @param ret
	 */
	public void colEval(MatrixDimFunction f, double[] ret) {
		for (int i = 0; i < getColumnCount(); ++i) {
			double[] data = columnAsDouble(i);
			
			f.apply(i, data, ret);
		}
	}

	/**
	 * Apply a stat function over a matrix.
	 * 
	 * @param f
	 * @return 
	 */
	public double stat(MatrixStatFunction f) {
		f.init();

		for (int i = 0; i < getRowCount(); ++i) {
			for (int j = 0; j < getColumnCount(); ++j) {
				f.apply(i, j, getValue(i, j));
			}
		}

		return f.getStat();
	}

	public double rowStat(MatrixStatFunction f, int row) {
		f.init();

		for (int i = 0; i < getColumnCount(); ++i) {
			f.apply(row, i, getValue(row, i));
		}

		return f.getStat();
	}

	public double colStat(MatrixStatFunction f, int col) {
		f.init();

		for (int i = 0; i < getRowCount(); ++i) {
			f.apply(i, col, getValue(i, col));
		}

		return f.getStat();
	}


	public double[] toDouble() {
		return toDouble(this);
	}


	//
	// Static methods
	//

	/**
	 * Creates a submatrix containing only the text cells in the matrix.
	 *
	 * @param m the m
	 * @param rows 		Will be populated with the rows containing text.
	 * @param columns 	Will be populated with the columns containing text.
	 * @return the matrix
	 */
	public static Matrix extractText(Matrix m, 
			List<Integer> rows, 
			List<Integer> columns) {
		if (m instanceof MixedMatrix) {
			return extractText((MixedMatrix)m, rows, columns);
		}

		if (columns == null || rows == null) {
			return null;
		}

		int n = m.getColumnCount();

		for (int i = 0; i < n; ++i) {
			if (m.getCellType(0, i) == CellType.TEXT) {
				columns.add(i);
			}
		}

		if (columns.size() == 0) {
			return null;
		}

		int rn = m.getRowCount();


		for (int i = 0; i < rn; ++i) {
			if (m.getCellType(i, columns.get(0)) == CellType.TEXT) {
				rows.add(i);
			}
		}

		if (rows.size() == 0) {
			return null;
		}

		TextMatrix ret = TextMatrix.createTextMatrix(rows.size(), columns.size());

		for (int i = 0; i < rows.size(); ++i) {
			int r = rows.get(i);

			for (int j = 0; j < columns.size(); ++j) {
				int c = columns.get(j);

				ret.set(i, j, m.getText(r, c));
			}
		}

		return ret;
	}

	/**
	 * Extract text.
	 *
	 * @param m the m
	 * @param rows the rows
	 * @param columns the columns
	 * @return the matrix
	 */
	public static Matrix extractText(MixedMatrix m, 
			List<Integer> rows, 
			List<Integer> columns) {
		return extractData(m, CellType.TEXT, rows, columns);
	}

	/**
	 * Extract the numerical portion of a matrix. The supplied rows and
	 * columns lists will be populated by the rows and indices of the matrix
	 * where numerical data is found.
	 *
	 * @param m the m
	 * @param rows the rows
	 * @param columns the columns
	 * @return the matrix
	 */
	public static Matrix extractNumbers(Matrix m, 
			List<Integer> rows, 
			List<Integer> columns) {
		if (m instanceof MixedMatrix) {
			return extractNumbers((MixedMatrix)m, rows, columns);
		}

		if (columns == null || rows == null) {
			return null;
		}

		int n = m.getColumnCount();

		for (int i = 0; i < n; ++i) {
			if (m.getCellType(0, i) == CellType.NUMBER) {
				columns.add(i);
			}
		}

		if (columns.size() == 0) {
			return null;
		}

		int rn = m.getRowCount();

		for (int i = 0; i < rn; ++i) {
			if (m.getCellType(i, columns.get(0)) == CellType.NUMBER) {
				rows.add(i);
			}
		}

		if (rows.size() == 0) {
			return null;
		}

		DoubleMatrix ret = DoubleMatrix.createDoubleMatrix(rows.size(), columns.size());

		for (int i = 0; i < rows.size(); ++i) {
			int r = rows.get(i);

			for (int j = 0; j < columns.size(); ++j) {
				int c = columns.get(j);

				ret.set(i, j, m.getValue(r, c));
			}
		}

		return ret;
	}

	/**
	 * Extract numbers from a matrix and create a new matrix from them.
	 *
	 * @param m the m				An input matrix.
	 * @param rows					This list will be populated with the rows
	 * 								containing numbers.
	 * @param columns the columns	This list will be populated with the 
	 * 								columns containing the numbers.
	 * @return the matrix			A matrix containing just the numbers from
	 * 								the input matrix. Its dimensions will
	 * 								the size of the rows list x the size of
	 * 								columns list.
	 */
	public static Matrix extractNumbers(MixedMatrix m, 
			List<Integer> rows, 
			List<Integer> columns) {
		return extractData(m, CellType.NUMBER, rows, columns);
	}


	/**
	 * Extract data.
	 *
	 * @param m the m
	 * @param cellType the cell type
	 * @param rows the rows
	 * @param columns the columns
	 * @return the matrix
	 */
	public static Matrix extractData(MixedMatrix m,
			CellType cellType,
			List<Integer> rows, 
			List<Integer> columns) {
		if (columns == null || rows == null) {
			return null;
		}

		int cn = m.mCols;

		for (int i = 0; i < cn; ++i) {
			if (m.mCellType[i] == cellType) {
				columns.add(i);
			}
		}

		if (columns.size() == 0) {
			return null;
		}

		int rn = m.mRows;

		int c = columns.get(0);

		for (int i = 0; i < rn; ++i) {
			if (m.mCellType[c] == cellType) {
				rows.add(i);
			}

			c += cn;
		}

		if (cellType == CellType.NUMBER) {
			return extractDoubleData(m,rows, columns);
		} else {
			return extractTextData(m,rows, columns);
		}
	}

	/**
	 * Extract double data.
	 *
	 * @param m the m
	 * @param rows the rows
	 * @param columns the columns
	 * @return the matrix
	 */
	public static Matrix extractDoubleData(MixedMatrix m,
			List<Integer> rows, 
			List<Integer> columns) {
		int rn = m.mRows;
		int cn = m.mCols;
		int cn2 = columns.size();

		DoubleMatrix ret = DoubleMatrix.createDoubleMatrix(rows.size(), cn2);

		for (int i = 0; i < cn2; ++i) {
			int origColIndex = columns.get(i);
			int newIndex = i;

			for (int j = 0; j < rn; ++j) {
				ret.mData[newIndex] = ((Number)m.mData[origColIndex]).doubleValue();

				origColIndex += cn;
				newIndex += cn2;
			}
		}

		return ret;
	}

	/**
	 * Extract the text data from the rows and columns specified.
	 *
	 * @param m the m
	 * @param rows the rows
	 * @param columns the columns
	 * @return the matrix
	 */
	public static Matrix extractTextData(MixedMatrix m,
			List<Integer> rows, 
			List<Integer> columns) {
		int rn = m.mRows;
		int cn = m.mCols;
		int cn2 = columns.size();

		TextMatrix ret = TextMatrix.createTextMatrix(rows.size(), cn2);

		for (int i = 0; i < cn2; ++i) {
			int origColIndex = columns.get(i);
			int newIndex = i;

			for (int j = 0; j < rn; ++j) {
				ret.mData[newIndex] = (String)m.mData[origColIndex];

				origColIndex += cn;
				newIndex += cn2;
			}
		}

		return ret;
	}

	/**
	 * Copy columns from one matrix to another.
	 *
	 * @param from the from
	 * @param to the to
	 */
	public static void copyColumns(Matrix from, 
			Matrix to) {
		int cols = Math.min(from.getColumnCount(), to.getColumnCount());

		for (int i = 0; i < cols; ++i) {
			to.copyColumn(from, i);
		}
	}

	/**
	 * Copy columns.
	 *
	 * @param from the from
	 * @param to the to
	 * @param column the column
	 * @param columns the columns
	 */
	public static void copyColumns(Matrix from, 
			Matrix to,
			int column,
			int... columns) {
		copyColumns(from, to, 0, column, columns);
	}

	/**
	 * Copy columns.
	 *
	 * @param from the from
	 * @param to the to
	 * @param toOffset the to offset
	 * @param column the column
	 * @param columns the columns
	 */
	public static void copyColumns(Matrix from, 
			Matrix to,
			int toOffset,
			int column,
			int... columns) {
		int tc = toOffset;

		to.copyColumn(from, column, tc++);

		for (int c : columns) {
			to.copyColumn(from, c, tc++);
		}
	}

	/**
	 * Copy columns.
	 *
	 * @param from the from
	 * @param to the to
	 * @param columns the columns
	 */
	public static void copyColumns(Matrix from, 
			Matrix to,
			Collection<Integer> columns) {
		copyColumns(from, to, 0, columns);
	}

	/**
	 * Copy columns.
	 *
	 * @param from the from
	 * @param to the to
	 * @param toOffset the to offset
	 * @param columns the columns
	 */
	public static void copyColumns(Matrix from, 
			Matrix to,
			int toOffset,
			Collection<Integer> columns) {
		int tc = toOffset;

		for (int c : columns) {
			to.copyColumn(from, c, tc++);
		}
	}

	/**
	 * Copy columns indexed.
	 *
	 * @param <V> the value type
	 * @param from the from
	 * @param to the to
	 * @param cols the cols
	 */
	public static <V extends Comparable<? super V>> void copyColumnsIndexed(Matrix from, 
			Matrix to,
			Collection<Indexed<Integer, V>> cols) {
		copyColumnsIndexed(from, to, 0, cols);
	}

	/**
	 * Copy columns indexed.
	 *
	 * @param <V> the value type
	 * @param from the from
	 * @param to the to
	 * @param toOffset the to offset
	 * @param cols the cols
	 */
	public static <V extends Comparable<? super V>> void copyColumnsIndexed(Matrix from, 
			Matrix to,
			int toOffset,
			Collection<Indexed<Integer, V>> cols) {
		int tc = toOffset;

		for (Indexed<Integer, ?> c : cols) {
			to.copyColumn(from, c.getIndex(), tc++);
		}
	}

	/**
	 * Copy rows.
	 *
	 * @param from the from
	 * @param to the to
	 * @param rows the rows
	 */
	public static void copyRows(Matrix from, 
			Matrix to,
			int... rows) {
		copyRows(from, to, 0, rows);
	}

	/**
	 * Copy rows from one matrix to another.
	 *
	 * @param from 		The matrix to copy from
	 * @param to 		The matrix to copy to.
	 * @param toOffset 	The offset where to begin copying rows into the 
	 * 					target matrix. Thus 2 would start copying row 0
	 * 					of the source matrix to row 2 of the target and
	 * 					row 1 to row 3 etc.
	 * @param rows 		A list of the rows to copy.
	 */
	public static void copyRows(Matrix from, 
			Matrix to,
			int toOffset,
			int... rows) {
		int tr = toOffset;

		for (int r : rows) {
			to.copyRow(from, r, tr++);
		}
	}

	/**
	 * Copy rows.
	 *
	 * @param from the from
	 * @param to the to
	 * @param rows the rows
	 */
	public static void copyRows(final Matrix from, 
			Matrix to,
			Collection<Integer> rows) {
		copyRows(from, to, 0, rows);
	}

	/**
	 * Copy rows.
	 *
	 * @param from the from
	 * @param to the to
	 * @param toOffset the to offset
	 * @param rows the rows
	 */
	public static void copyRows(final Matrix from, 
			Matrix to,
			int toOffset,
			Collection<Integer> rows) {
		int tc = toOffset;

		for (int c : rows) {
			to.copyRow(from, c, tc++);
		}
	}

	/**
	 * Copy rows indexed.
	 *
	 * @param <V> the value type
	 * @param from the from
	 * @param to the to
	 * @param rows the rows
	 */
	public static <V extends Comparable<? super V>> void copyRowsIndexed(final Matrix from, 
			Matrix to,
			Collection<Indexed<Integer, V>> rows) {
		copyRowsIndexed(from, to, 0, rows);
	}

	/**
	 * Copy rows indexed.
	 *
	 * @param <V> the value type
	 * @param from the from
	 * @param to the to
	 * @param toOffset the to offset
	 * @param rows the rows
	 */
	public static <V extends Comparable<? super V>> void copyRowsIndexed(final Matrix from, 
			Matrix to,
			int toOffset,
			Collection<Indexed<Integer, V>> rows) {
		int tc = toOffset;

		for (Indexed<Integer, ?> c : rows) {
			to.copyRow(from, c.getIndex(), tc++);
		}
	}

	/**
	 * Count how many rows in a column have a number in them.
	 *
	 * @param m the m
	 * @param c the c
	 * @return the int
	 */
	public static int countNumericalRows(final Matrix m, int c) {
		int ret = 0;

		for (int i = 0; i < m.getRowCount(); ++i) {
			if (Mathematics.isValidNumber(m.getValue(i, c))) {
				++ret;
			}
		}

		return ret;
	}

	/**
	 * Set the values in a column. If the values are too long, the outside
	 * portion will be omitted, if the values are too short, the column will
	 * be updated to the length of the values.
	 *
	 * @param <T> the generic type
	 * @param column the column
	 * @param values the values
	 * @param m the m
	 */
	public static <T> void setColumn(int column, List<T> values, Matrix m) {
		for (int i = 0; i < Math.min(m.getRowCount(), values.size()); ++i) {
			m.set(i, column, values.get(i));
		}
	}

	/**
	 * Sets the row NA.
	 *
	 * @param from the from
	 * @param to the to
	 * @param ret the ret
	 */
	public static void setRowNA(int from, int to, Matrix ret) {
		setRowValue(from, to, TextUtils.NA, ret);
	}

	/**
	 * Sets the row value.
	 *
	 * @param from the from
	 * @param to the to
	 * @param v the v
	 * @param ret the ret
	 */
	public static void setRowValue(int from, int to, String v, Matrix ret) {
		for (int row = from; row <= to; ++row) {
			for (int column = 0; column < ret.getColumnCount(); ++column) {
				ret.set(row, column, v);
			}
		}
	}

	/**
	 * Sets the row value.
	 *
	 * @param from the from
	 * @param to the to
	 * @param v the v
	 * @param ret the ret
	 */
	public static void setRowValue(int from, int to, double v, Matrix ret) {
		for (int row = from; row <= to; ++row) {
			for (int column = 0; column < ret.getColumnCount(); ++column) {
				ret.set(row, column, v);
			}
		}
	}

	/**
	 * Returns true if the number is a valid double.
	 *
	 * @param v the v
	 * @return true, if is valid matrix num
	 */
	public static boolean isValidMatrixNum(double v) {
		return !Double.isNaN(v);
	}

	public static boolean isValidMatrixNum(int v) {
		return v != NULL_INT_NUMBER;
	}

	public static boolean isValidMatrixNum(long v) {
		return v != NULL_LONG_NUMBER;
	}

	/**
	 * Convert all of the values in a matrix to a list.
	 *
	 * @param m the m
	 * @return the list
	 */
	public static double[] toDouble(Matrix m) {
		double[] ret = new double[m.getNumCells()];

		int v = 0;
		
		for (int i = 0; i < m.getRowCount(); ++i) {
			for (int j = 0; j < m.getColumnCount(); ++j) {
				ret[v++] = m.getValue(i, j);
			}
		}

		return ret;
	}

	/**
	 * Create a new matrix of the same type as given with the specified
	 * rows and columns.
	 *
	 * @param m the m
	 * @return the matrix
	 */
	public static Matrix ofSameType(final Matrix m) {
		if (m instanceof DoubleMatrix) {
			return DoubleMatrix.createDoubleMatrix(m);
		} else if (m instanceof IntMatrix) {
			return IntMatrix.createIntMatrix(m);
		} else if (m instanceof TextMatrix) {
			return TextMatrix.createTextMatrix(m);
		} else {
			return MixedMatrix.createMixedMatrix(m);
		}

		//return ofSameType(m, m.getRowCount(), m.getColumnCount());
	}

	/**
	 * Create a new matrix of the same type as given with the specified
	 * rows and columns.
	 *
	 * @param m the m
	 * @param rows the rows
	 * @param columns the columns
	 * @return the matrix
	 */
	public static Matrix ofSameType(final Matrix m, int rows, int columns) {
		//return ofSameType(m.getType(), rows, columns);

		if (m instanceof DoubleMatrix) {
			return DoubleMatrix.createDoubleMatrix(rows, columns);
		} else if (m instanceof IntMatrix) {
			return IntMatrix.createIntMatrix(rows, columns);
		} else if (m instanceof TextMatrix) {
			return TextMatrix.createTextMatrix(rows, columns);
		} else {
			return MixedMatrix.createMixedMatrix(rows, columns);
		}
	}

	/**
	 * Create a new matrix of the same type as given with the specified
	 * rows and columns.
	 *
	 * @param type the type
	 * @param rows the rows
	 * @param columns the columns
	 * @return the matrix
	 */
	public static Matrix ofSameType(MatrixType type, int rows, int columns) {
		switch (type) {
		case NUMBER:
			return new DoubleMatrix(rows, columns);
		case TEXT:
			return new TextMatrix(rows, columns);
		default:
			return new MixedMatrix(rows, columns);
		}
	}

	/**
	 * Returns true if the matrix is a TextMatrix, false otherwise.
	 *
	 * @param m the m
	 * @return true, if is text
	 */
	public static boolean isText(Matrix m) {
		return m.getType() == MatrixType.TEXT;
	}

	/**
	 * Returns the cell types in the matrix. This is predominately for use
	 * with mixed matrices to check whether they contain a mixture of text
	 * or numbers. If a matrix contains only numbers, we might treat it as
	 * a numerical matrix for future calculations.
	 *
	 * @param m the m
	 * @return the sets the
	 */
	public static Set<CellType> cellTypes(Matrix m) {
		if (m instanceof DoubleMatrix) {
			return cellTypes((DoubleMatrix)m);
		} else if (m instanceof TextMatrix) {
			return cellTypes((TextMatrix)m);
		} else if (m instanceof MixedMatrix) {
			return cellTypes((MixedMatrix)m);
		} else {
			Set<CellType> ret = new HashSet<CellType>();

			int n = CellType.values().length;

			for (int i = 0; i < m.getRowCount(); ++i) {
				for (int j = 0; j < m.getColumnCount(); ++j) {
					ret.add(m.getCellType(i, j));

					if (ret.size() == n) {
						// Contains all types so no point checking further
						break;
					}
				}

				if (ret.size() == n) {
					// Contains all types so no point checking further
					break;
				}
			}

			return ret;
		}
	}

	/**
	 * Cell types.
	 *
	 * @param m the m
	 * @return the sets the
	 */
	public static Set<CellType> cellTypes(DoubleMatrix m) {
		return NUMBER_MATRIX_TYPES;
	}

	/**
	 * Cell types.
	 *
	 * @param m the m
	 * @return the sets the
	 */
	public static Set<CellType> cellTypes(TextMatrix m) {
		return TEXT_MATRIX_TYPES;
	}

	/**
	 * Cell types.
	 *
	 * @param m the m
	 * @return the sets the
	 */
	public static Set<CellType> cellTypes(MixedMatrix m) {
		Set<CellType> ret = new HashSet<CellType>();

		int n = CellType.values().length;

		for (int i = 0; i < m.mData.length; ++i) {
			ret.add(m.mCellType[i]);

			if (ret.size() == n) {
				break;
			}
		}

		return ret;
	}
}
