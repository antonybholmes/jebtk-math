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

import org.jebtk.core.collections.DefaultHashMapMap;
import org.jebtk.core.collections.MapMap;
import org.jebtk.core.text.Parser;
import org.jebtk.core.text.TextUtils;


// TODO: Auto-generated Javadoc
/**
 * Matrix that can be dynamically resized to match maximum row/column.
 * This matrix uses lazy instantiation so cells are only created as they
 * are referenced. Thus there will be a performance loss at the expense
 * of flexibility. This matrix has both the properties of a matrix and
 * a map. The matrix's nominal dimensions are increased to match the largest
 * row and column encountered.
 *
 * @author Antony Holmes Holmes
 * @param <T> the generic type
 */
public abstract class DynamicMatrix<T> extends Matrix {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Dynamically resizing map of maps to store text for each cell.
	 */
	protected MapMap<Integer, Integer, T> mData = null;

	/**
	 * The member cells.
	 */
	protected int mSize = 0;

	/** The m rows. */
	protected int mRows = 0;

	/** The m columns. */
	protected int mColumns = 0;


	/**
	 * Instantiates a new dynamic matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 */
	public DynamicMatrix(int rows, int columns) {
		super(rows, columns);

		mData = DefaultHashMapMap.create(rows, columns);
	}

	/**
	 * Instantiates a new mixed sparse matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @param v the v
	 */
	public DynamicMatrix(int rows, int columns, T v) {
		super(rows, columns);

		mData = DefaultHashMapMap.create(rows, columns, v);

		// update size uses the zero based row and column to calculate size
		// so we need to adjust for this
		//updateSize(rows - 1, columns - 1);
	}

	/**
	 * Clone a matrix optionally copying the core matrix values and the
	 * annotation.
	 *
	 * @param m the m
	 */
	public DynamicMatrix(Matrix m) {
		this(m.getRowCount(), m.getColumnCount());

		update(m);
	}

	/**
	 * Instantiates a new dynamic matrix.
	 *
	 * @param m the m
	 */
	public DynamicMatrix(DynamicMatrix<T> m) {
		super(m.getRowCount(), m.getColumnCount());

		mData = DefaultHashMapMap.create(m.getRowCount(), m.getColumnCount());

		update(m);
	}

	/**
	 * Specialized update method for dynamic matrices to quickly copy data
	 * from one matrix to another.
	 *
	 * @param m the m
	 */
	public void update(DynamicMatrix<T> m) {
		for (int row : m.mData.keySet()) {
			mData.put(row, m.mData.get(row));
		}
	}


	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#getValue(int)
	 */
	@Override
	public double getValue(int row, int column) {
		Object v = mData.get(row, column);

		if (v != null && v instanceof Number) {
			return ((Double)v).doubleValue();
		} else {
			return NULL_NUMBER;
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#getText(int)
	 */
	@Override
	public String getText(int row, int column) {
		Object v = mData.get(row, column);

		if (v != null) {
			if (v instanceof String) {
				return (String)v;
			} else {
				return v.toString();
			}
		} else {
			return TextUtils.EMPTY_STRING;
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#updateToNull(int, int)
	 */
	@Override
	public void updateToNull(int row, int column) {
		mData.put(row, column, null);
	}

	/**
	 * Keep track of the maximum row and column requested so far. This is
	 * so the matrix can dynamically resize if necessary.
	 *
	 * @param row the row
	 * @param column the column
	 */
	protected void updateSize(int row, int column) {
		boolean changed = false;

		if (row >= mRows) {
			mRows = row + 1;
			changed = true;
		}

		if (column >= mColumns) {
			mColumns = column + 1;
			changed = true;
		}

		if (changed) {
			mSize = mRows * mColumns;

			//fireMatrixChanged();
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#update(int, int, java.lang.Object)
	 */
	@Override
	public void update(int row, int column, Object o) {
		if (o == null) {
			return;
		}

		if (o instanceof Double) {
			update(row, column, (Double)o);
		} else if (o instanceof Integer) {
			update(row, column, (Integer)o);
		} else if (o instanceof Number) {
			update(row, column, ((Number)o).doubleValue());
		} else {
			String s = o.toString();

			try {
				update(row, column, Parser.toDouble(s));
			} catch (ParseException e) {
				update(row, column, s);
			}
		}
	}



	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.Matrix#update(double)
	 */
	@Override
	public void update(double v) {
		for (int i = 0; i < mRows; ++i) {
			for (int j = 0; j < mColumns; ++j) {
				update(i, j, v);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.Matrix#update(java.lang.String)
	 */
	@Override
	public void update(String v) {
		for (int i = 0; i < mRows; ++i) {
			for (int j = 0; j < mColumns; ++j) {
				update(i, j, v);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#update(int, int, double)
	 */
	@Override
	public void update(int row, int column, double v) {
		updateSize(row, column);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#update(int, int, int)
	 */
	@Override
	public void update(int row, int column, int v) {
		updateSize(row, column);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#update(int, int, java.lang.String)
	 */
	@Override
	public void update(int row, int column, String v) {
		updateSize(row, column);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.Matrix#getNumCells()
	 */
	@Override
	public int getNumCells() {
		return mSize;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return mRows;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return mColumns;
	}
}
