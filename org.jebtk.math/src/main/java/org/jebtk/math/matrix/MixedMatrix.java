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

import java.util.Arrays;
import java.util.List;

import org.jebtk.core.text.TextUtils;


// TODO: Auto-generated Javadoc
/**
 * Allows strings and numbers to exist in same matrix.
 * 
 * @author Antony Holmes Holmes
 */
public class MixedMatrix extends IndexableMatrix {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** Stores the matrix data in a row format. */
	public Object[] mData;
	
	/**  Storest the cell types in a row format. */
	public CellType[] mCellType;

	/** The m default number. */
	private double mDefaultNumber;

	/**
	 * Create a new matrix defaulting to being entirely numeric.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 */
	public MixedMatrix(int rows, int columns) {
		this(rows, columns, NULL_NUMBER);
	}

	/**
	 * Instantiates a new mixed matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @param v the v
	 */
	public MixedMatrix(int rows, int columns, double v) {
		super(rows, columns);

		mDefaultNumber = v;
	}

	/**
	 * Instantiates a new mixed matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @param v the v
	 */
	public MixedMatrix(int rows, int columns, String v) {
		super(rows, columns);

		// Set the default value
		set(v);
	}

	/**
	 * Clone a matrix optionally copying the core matrix values and the
	 * annotation.
	 *
	 * @param m the m
	 */
	public MixedMatrix(Matrix m) {
		super(m);
	}

	/**
	 * Instantiates a new mixed matrix.
	 *
	 * @param m the m
	 */
	public MixedMatrix(IndexableMatrix m) {
		super(m);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#createData(int, int, int)
	 */
	@Override
	protected void createData(int rows, int columns, int n) {
		mData = new Object[n];
		mCellType = new CellType[n];
		
		update(CellType.TEXT);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#getType()
	 */
	@Override
	public MatrixType getType() {
		return MatrixType.MIXED;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#copy()
	 */
	@Override
	public Matrix copy() {
		return new MixedMatrix(this);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#getCellType(int)
	 */
	@Override
	public CellType getCellType(int index) {
		return mCellType[index];
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#get(int)
	 */
	@Override
	public Object get(int index) {
		Object v = mData[index];
		
		if (v != null) {
			return v;
		} else {
			return TextUtils.EMPTY_STRING;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#getValue(int)
	 */
	@Override
	public double getValue(int index) {
		Object v = mData[index];
		
		if (v != null) {
			if (v instanceof Double) {
				return (Double)v;
			} else if (v instanceof Integer) {
				return (Integer)v;
			} else {
				return mDefaultNumber;
			}
		} else {
			return mDefaultNumber;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#getText(int)
	 */
	@Override
	public String getText(int index) {
		Object v = mData[index];
		
		//System.err.println("mixed " + index + " " + v);
		
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

	/**
	 * Set a cell to null so that neither text nor number are valid.
	 *
	 * @param index the index
	 */
	@Override
	public void updateToNull(int index) {
		mData[index] = null;
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.Matrix#update(double)
	 */
	@Override
	public void update(double v) {
		Arrays.fill(mData, v);
		update(CellType.NUMBER);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#update(int, java.lang.Object)
	 */
	@Override
	public void update(int index, Object v) {
		if (v == null) {
			return;
		}

		if (v instanceof Double) {
			update(index, (double)v);
		} else if (v instanceof Integer) {
			update(index, (int)v);
		} else if (v instanceof Number) {
			update(index, ((Number)v).doubleValue());
		} else if (v instanceof String) {
			update(index, (String)v);
		} else {
			update(index, v.toString());
		}
	}
	
	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#update(int, java.lang.String)
	 */
	@Override
	public void update(int index, String v) {
		mData[index] = v;
		mCellType[index] = CellType.TEXT;
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#update(int, double)
	 */
	@Override
	public void update(int index, double v) {
		mData[index] = v;
		mCellType[index] = CellType.NUMBER;
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.Matrix#update(java.lang.String)
	 */
	@Override
	public void update(String v) {
		Arrays.fill(mData, v);
		update(CellType.TEXT);
	}
	
	/**
	 * Update.
	 *
	 * @param v the v
	 */
	public void update(CellType v) {
		Arrays.fill(mCellType, v);
	}
	
	/**
	 * Specialized instance of column copying for numerical matrices.
	 *
	 * @param from the from
	 * @param column the column
	 * @param toColumn the to column
	 */
	@Override
	public void copyColumn(final DoubleMatrix from, 
			int column,
			int toColumn) {
		int i1 = from.getIndex(0, column);
		int i2 = getIndex(0, toColumn);

		int r = Math.min(from.getRowCount(), getRowCount());

		for (int i = 0; i < r; ++i) {
			mData[i2] = from.mData[i1];

			i1 += from.mCols;
			i2 += mCols;
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#copyColumn(org.abh.common.math.matrix.TextMatrix, int, int)
	 */
	@Override
	public void copyColumn(final TextMatrix from, 
			int column,
			int toColumn) {
		int i1 = from.getIndex(0, column);
		int i2 = getIndex(0, toColumn);

		int r = Math.min(from.getRowCount(), getRowCount());

		for (int i = 0; i < r; ++i) {
			mData[i2] = from.mData[i1];

			i1 += from.mCols;
			i2 += mCols;
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#copyColumn(org.abh.common.math.matrix.MixedMatrix, int, int)
	 */
	@Override
	public void copyColumn(final MixedMatrix from, 
			int column,
			int toColumn) {
		int i1 = from.getIndex(0, column);
		int i2 = getIndex(0, toColumn);

		int r = Math.min(from.getRowCount(), getRowCount());

		for (int i = 0; i < r; ++i) {
			mData[i2] = from.mData[i1];

			i1 += from.mCols;
			i2 += mCols;
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#copyRow(org.abh.common.math.matrix.DoubleMatrix, int, int)
	 */
	@Override
	public void copyRow(final DoubleMatrix from, 
			int row,
			int toRow) {
		int c = Math.min(from.getColumnCount(), getColumnCount());

		System.arraycopy(from.mData, from.mRowOffsets[row], mData, mRowOffsets[toRow], c);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#copyRow(org.abh.common.math.matrix.TextMatrix, int, int)
	 */
	@Override
	public void copyRow(final TextMatrix from, 
			int row,
			int toRow) {

		int c = Math.min(from.getColumnCount(), getColumnCount());

		System.arraycopy(from.mData, from.mRowOffsets[row], mData, mRowOffsets[toRow], c);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#copyRow(org.abh.common.math.matrix.MixedMatrix, int, int)
	 */
	@Override
	public void copyRow(final MixedMatrix from, 
			int row,
			int toRow) {
		int c = Math.min(from.getColumnCount(), getColumnCount());

		//SysUtils.err().println("copy row", from.getColumnCount(), getColumnCount());

		System.arraycopy(from.mData, from.mRowOffsets[row], mData, mRowOffsets[toRow], c);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#setValueColumn(int, java.util.List)
	 */
	@Override
	public void setValueColumn(int column, List<Double> values) {
		int r = Math.min(getRowCount(), values.size());

		int ix = getIndex(0, column);

		for (int i = 0; i < r; ++i) {
			mData[ix] = values.get(i);

			ix += mCols;
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#setTextColumn(int, java.util.List)
	 */
	@Override
	public void setTextColumn(int column, List<String> values) {
		int r = Math.min(getRowCount(), values.size());

		int ix = getIndex(0, column);

		for (int row = 0; row < r; ++row) {
			mData[ix] = values.get(row);

			ix += mCols;
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#transpose()
	 */
	@Override
	public Matrix transpose() {
		return transpose(this);
	}

	public static Matrix transpose(MixedMatrix m) {
		MixedMatrix ret = createMixedMatrix(m.mCols, m.mRows);

		int i2 = 0;
		int c = 0;

		for (int i = 0; i < m.mData.length; ++i) {
			// Each time we end a row, reset i2 back to the next column
			if (i % m.mCols == 0) {
				i2 = c++;
			}

			ret.mData[i2] = m.mData[i];
			
			// Skip blocks
			i2 += m.mRows;
		}

		return ret;
	}
	
	

	//
	// Static methods
	//

	/**
	 * Returns a new empty matrix the same dimensions as the input matrix.
	 *
	 * @param m the m
	 * @return the mixed matrix
	 */
	public static MixedMatrix createMixedMatrix(Matrix m) {
		return createMixedMatrix(m.getRowCount(), m.getColumnCount());
	}

	/**
	 * Creates the mixed matrix.
	 *
	 * @param rows the rows
	 * @param cols the cols
	 * @return the mixed matrix
	 */
	public static MixedMatrix createMixedMatrix(int rows, int cols) {
		return new MixedMatrix(rows, cols);
	}
}
