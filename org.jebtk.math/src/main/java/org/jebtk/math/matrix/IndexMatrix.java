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

import java.util.ArrayList;
import java.util.List;

import org.jebtk.core.text.TextUtils;

// TODO: Auto-generated Javadoc
/**
 * For matrices that intending to store data in a 1D array format. This
 * class provides some of the required implementation.
 * 
 * @author Antony Holmes Holmes
 */
public abstract class IndexMatrix extends RegularMatrix {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new index matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 */
	public IndexMatrix(int rows, int columns) {
		super(rows, columns);
	}

	/**
	 * Instantiates a new index matrix.
	 *
	 * @param m the m
	 */
	public IndexMatrix(Matrix m) {
		this(m.getRowCount(), m.getColumnCount());

		update(m);
	}


	/**
	 * Instantiates a new index matrix.
	 *
	 * @param m the m
	 */
	public IndexMatrix(IndexMatrix m) {
		this(m.getRowCount(), m.getColumnCount());

		update(m);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#add(double)
	 */
	@Override
	public Matrix add(double v) {
		for (int i = 0; i < mSize; ++i) {
			set(i, getValue(i) + v);
		}

		fireMatrixChanged();

		return this;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#subtract(double)
	 */
	@Override
	public Matrix subtract(double v) {
		for (int i = 0; i < mSize; ++i) {
			set(i, getValue(i) - v);
		}

		fireMatrixChanged();

		return this;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#multiply(double)
	 */
	@Override
	public Matrix multiply(double v) {
		for (int i = 0; i < mSize; ++i) {
			set(i, getValue(i) * v);
		}

		fireMatrixChanged();

		return this;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#divide(double)
	 */
	@Override
	public Matrix divide(double v) {
		for (int i = 0; i < mSize; ++i) {
			set(i, getValue(i) / v);
		}

		fireMatrixChanged();

		return this;
	}


	/**
	 * Copy the values from a matrix to this matrix.
	 *
	 * @param m the m
	 */
	public void set(IndexMatrix m) {
		update(m);

		fireMatrixChanged();
	}

	/**
	 * Copy the values from a matrix to this matrix.
	 *
	 * @param m the m
	 */
	public void update(IndexMatrix m) {
		int r = Math.min(getNumCells(), m.getNumCells());

		for (int i = 0; i < r; ++i) {
			set(i, m.get(i));
		}
	}

	/**
	 * Returns the lookup index associated with the row and column.
	 *
	 * @param row the row
	 * @param column the column
	 * @return the index
	 */
	public abstract int getIndex(int row, int column);

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#update(double)
	 */
	@Override
	public void update(double value) {
		for (int i = 0; i < mSize; ++i) {
			update(i, value);
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.Matrix#updateValue(int, int, double)
	 */
	@Override
	public void update(int row, int column, double v) {
		update(getIndex(row, column), v);
	}

	/**
	 * Update.
	 *
	 * @param index the index
	 * @param v the v
	 */
	public void update(int index, double v) {
		// Do nothing
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#update(int, int, int)
	 */
	@Override
	public void update(int row, int column, int v) {
		update(getIndex(row, column), v);
	}

	/**
	 * Update.
	 *
	 * @param index the index
	 * @param v the v
	 */
	public void update(int index, int v) {
		update(index, (double)v);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#updateToNull(int, int)
	 */
	@Override
	public void updateToNull(int row, int column) {
		updateToNull(getIndex(row, column));
	}

	/**
	 * Set the cell to its default null value.
	 *
	 * @param index the new to null
	 */
	public void setToNull(int index) {
		updateToNull(index);

		fireMatrixChanged();
	}

	/**
	 * Update to null.
	 *
	 * @param index the index
	 */
	public abstract void updateToNull(int index);



	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#update(java.lang.String)
	 */
	@Override
	public void update(String value) {
		for (int i = 0; i < mSize; ++i) {
			update(i, value);
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.Matrix#updateText(int, int, java.lang.String)
	 */
	@Override
	public void update(int row, int column, String v) {
		update(getIndex(row, column), v);
	}

	/**
	 * Update text.
	 *
	 * @param index the index
	 * @param v the v
	 */
	public void update(int index, String v) {
		// Do nothing
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.Matrix#setText(int, int, java.lang.String)
	 */
	@Override
	public void set(int row, int column, String v) {
		set(getIndex(row, column), v);
	}

	/**
	 * Sets the text.
	 *
	 * @param index the index
	 * @param v the v
	 */
	public void set(int index, String v) {
		update(index, v);

		fireMatrixChanged();
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.Matrix#setValue(int, int, double)
	 */
	@Override
	public void set(int row, int column, double v) {
		set(getIndex(row, column), v);
	}

	/**
	 * Sets the.
	 *
	 * @param index the index
	 * @param v the v
	 */
	public void set(int index, Object v) {
		update(index, v);

		fireMatrixChanged();
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#update(int, int, java.lang.Object)
	 */
	@Override
	public void update(int row, int column, Object v) {
		update(getIndex(row, column), v);
	}

	/**
	 * Sets the.
	 *
	 * @param index the index
	 * @param v the v
	 */
	public void set(int index, double v) {
		update(index, v);

		fireMatrixChanged();
	}

	/**
	 * Sets the.
	 *
	 * @param index the index
	 * @param v the v
	 */
	public void set(int index, int v) {
		update(index, v);

		fireMatrixChanged();
	}

	/**
	 * Update.
	 *
	 * @param index the index
	 * @param v the v
	 */
	public void update(int index, Object v) {
		if (v == null) {
			return;
		}

		if (v instanceof Number) {
			update(index, ((Number)v).doubleValue());
		} else {
			String s = v.toString();

			if (TextUtils.isNumber(s)) {
				update(index, Double.parseDouble(s));
			} else {
				update(index, s);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.Matrix#get(int, int)
	 */
	@Override
	public Object get(int row, int column) {
		return get(getIndex(row, column));
	}

	/**
	 * Gets the.
	 *
	 * @param index the index
	 * @return the object
	 */
	public Object get(int index) {
		if (getCellType(index) == CellType.NUMBER) {
			return getValue(index);
		} else {
			return getText(index);
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.Matrix#getValue(int, int)
	 */
	@Override
	public double getValue(int row, int column) {
		return getValue(getIndex(row, column));
	}

	/**
	 * Gets the value.
	 *
	 * @param index the index
	 * @return the value
	 */
	public double getValue(int index) {
		return NULL_NUMBER;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#getIntValue(int, int)
	 */
	@Override
	public int getIntValue(int row, int column) {
		return getIntValue(getIndex(row, column));
	}

	/**
	 * Gets the int value of a cell.
	 *
	 * @param index the index
	 * @return the value
	 */
	public int getIntValue(int index) {
		return (int)getValue(index);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#getLongValue(int, int)
	 */
	@Override
	public long getLongValue(int row, int column) {
		return getLongValue(getIndex(row, column));
	}

	/**
	 * Gets the long value.
	 *
	 * @param index the index
	 * @return the long value
	 */
	public long getLongValue(int index) {
		return (long)getValue(index);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.Matrix#getText(int, int)
	 */
	@Override
	public String getText(int row, int column) {
		return getText(getIndex(row, column));
	}

	/**
	 * Gets the text.
	 *
	 * @param index the index
	 * @return the text
	 */
	public String getText(int index) {
		return TextUtils.EMPTY_STRING;
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.Matrix#getCellType(int, int)
	 */
	@Override
	public CellType getCellType(int row, int column) {
		return getCellType(getIndex(row, column));
	}

	/**
	 * Gets the cell type.
	 *
	 * @param index the index
	 * @return the cell type
	 */
	public CellType getCellType(int index) {
		return CellType.NUMBER;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#setColumn(int, java.util.List)
	 */
	@Override
	public void setColumn(int column, List<? extends Object> values) {
		int r = Math.min(getRowCount(), values.size());

		int ix = getIndex(0, column);

		for (int i = 0; i < r; ++i) {
			set(ix, values.get(i));

			ix += mCols;
		}

		fireMatrixChanged();
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#columnAsList(int)
	 */
	@Override
	public List<Object> columnAsList(int column) {
		int r = getRowCount();

		List<Object> values = new ArrayList<Object>(r);

		int i1 = getIndex(0, column);

		for (int row = 0; row < r; ++row) {
			values.add(get(i1));

			i1 += mCols;
		}

		return values;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#columnAsDouble(int)
	 */
	@Override
	public double[] columnAsDouble(int column) {
		int r = getRowCount();

		double[] values = new double[r];

		int i1 = column;

		for (int row = 0; row < r; ++row) {
			values[row] = getValue(i1);

			i1 += mCols;
		}

		return values;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#columnAsText(int)
	 */
	@Override
	public List<String> columnAsText(int column) {
		int r = getRowCount();

		List<String> values = new ArrayList<String>(r);

		int i1 = getIndex(0, column);

		for (int row = 0; row < r; ++row) {
			values.add(getText(i1));

			i1 += mCols;
		}

		return values;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#rowAsList(int)
	 */
	@Override
	public List<Object> rowAsList(int row) {
		int r = getColumnCount();

		List<Object> values = new ArrayList<Object>(r);

		int i1 = getIndex(row, 0);

		for (int col = 0; col < r; ++col) {
			values.add(get(i1++));
		}

		return values;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#rowAsDouble(int)
	 */
	@Override
	public double[] rowAsDouble(int row) {
		int c = getColumnCount();

		double[] ret = new double[c];

		int i1 = getIndex(row, 0);

		for (int col = 0; col < c; ++col) {
			ret[col] = getValue(i1++);
		}

		return ret;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#rowAsText(int)
	 */
	@Override
	public List<String> rowAsText(int row) {
		int c = getColumnCount();

		List<String> values = new ArrayList<String>(c);

		int i1 = getIndex(row, 0);

		for (int col = 0; col < c; ++col) {
			values.add(getText(i1++));
		}

		return values;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#copyColumn(org.abh.common.math.matrix.Matrix, int, int)
	 */
	@Override
	public void copyColumn(final Matrix from, 
			int column,
			int toColumn) {
		int r = Math.min(from.getRowCount(), getRowCount());

		int i = getIndex(0, toColumn);

		for (int row = 0; row < r; ++row) {
			update(i, from.get(row, column));

			i += mCols;
		}

		fireMatrixChanged();
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#copyRow(org.abh.common.math.matrix.Matrix, int, int)
	 */
	@Override
	public void copyRow(final Matrix from, 
			int row,
			int toRow) {
		int c = Math.min(from.getColumnCount(), getColumnCount());


		int i = getIndex(toRow, 0);

		for (int col = 0; col < c; ++col) {
			update(i++, from.get(row, col));
		}

		fireMatrixChanged();
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#transpose()
	 */
	@Override
	public Matrix transpose() { 
		MixedMatrix ret = MixedMatrix.createMixedMatrix(mCols, mRows);

		int i2 = 0;
		int c = 0;

		for (int i = 0; i < getNumCells(); ++i) {
			// Each time we end a row, reset i2 back to the next column
			if (i % mCols == 0) {
				i2 = c++;
			}

			ret.set(i2, get(i));;

			// Skip blocks
			i2 += mRows;
		}

		return ret;
	}
}
