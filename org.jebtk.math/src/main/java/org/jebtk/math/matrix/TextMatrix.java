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

import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.text.TextUtils;

// TODO: Auto-generated Javadoc
/**
 * Concrete implementation of the annotation matrix which
 * stores n * m String elements. Each cell must contain a string where
 * the empty string is used in place of null so that calls to getText() always
 * return a string object so that null checks are not required.
 * 
 * @author Antony Holmes Holmes
 */
public class TextMatrix extends IndexableMatrix {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member data.
	 */
	public String[] mData = null;

	/**
	 * Instantiates a new text matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 */
	public TextMatrix(int rows, int columns) {
		this(rows, columns, TextUtils.EMPTY_STRING);
	}

	/**
	 * Instantiates a new text matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @param v the v
	 */
	public TextMatrix(int rows, int columns, String v) {
		super(rows, columns);

		// We use a 1d array to store a 2d matrix for speed.
		mData = new String[mSize];

		//CollectionUtils.fill(TextUtils.EMPTY_STRING, mData);
		
		// Set the default value
		set(v);
	}
	
	/**
	 * Instantiates a new text matrix.
	 *
	 * @param m the m
	 */
	public TextMatrix(Matrix m) {
		this(m.getRowCount(), m.getColumnCount());
		
		update(m);
	}
	
	/**
	 * Instantiates a new text matrix.
	 *
	 * @param m the m
	 */
	public TextMatrix(TextMatrix m) {
		this(m.getRowCount(), m.getColumnCount());

		update(m);
	}
	
	/**
	 * Instantiates a new text matrix.
	 *
	 * @param m the m
	 */
	public TextMatrix(IndexableMatrix m) {
		this(m.getRowCount(), m.getColumnCount());

		update(m);
	}
	
	/**
	 * Copy the values from a matrix to this matrix.
	 *
	 * @param m the m
	 */
	public void set(TextMatrix m) {
		update(m);

		fireMatrixChanged();
	}
	
	/**
	 * Copy the values from a matrix to this matrix.
	 *
	 * @param m the m
	 */
	public void update(TextMatrix m) {
		System.arraycopy(m.mData, 0, mData, 0, Math.min(m.mData.length, mData.length));
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#getType()
	 */
	@Override
	public AnnotationType getType() {
		return AnnotationType.TEXT;
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#copy()
	 */
	@Override
	public Matrix copy() {
		return new TextMatrix(this);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#getCellType(int)
	 */
	@Override
	public CellType getCellType(int index) {
		return CellType.TEXT;
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.Matrix#getNumCells()
	 */
	@Override
	public int getNumCells() {
		return mData.length;
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.Matrix#updateValue(double)
	 */
	@Override
	public void update(double v) {
		update(Double.toString(v));
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#updateValue(int, double)
	 */
	@Override
	public void update(int index, double v) {
		update(index, Double.toString(v));
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#updateToNull(int)
	 */
	@Override
	public void updateToNull(int index) {
		mData[index] = TextUtils.EMPTY_STRING;
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.Matrix#updateText(java.lang.String)
	 */
	@Override
	public void update(String v) {
		//for (int i = 0; i < mData.length; ++i) {
		////	updateText(i, v);
		//}
		
		CollectionUtils.fill(v, mData);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#updateText(int, java.lang.String)
	 */
	@Override
	public void update(int index, String v) {
		if (v != null) {
			mData[index] = v;
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#getText(int)
	 */
	@Override
	public String getText(int index) {
		return mData[index];
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#get(int)
	 */
	@Override
	public Object get(int index) {
		return getText(index);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#copyColumn(org.abh.common.math.matrix.Matrix, int, int)
	 */
	@Override
	public void copyColumn(final Matrix from, 
			int column,
			int toColumn) {
		if (from instanceof DoubleMatrix) {
			copyColumn((DoubleMatrix)from, column, toColumn);
		} else if (from instanceof TextMatrix) { 
			copyColumn((TextMatrix)from, column, toColumn);
		} else {
			int i1 = getIndex(0, toColumn);

			int r = Math.min(from.getRowCount(), getRowCount());

			for (int i = 0; i < r; ++i) {
				mData[i1] = from.getText(i, column);

				i1 += mColumns;
			}
		}
	}
	
	/**
	 * Specialized instance of column copying for numerical matrices.
	 *
	 * @param from the from
	 * @param column the column
	 * @param toColumn the to column
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
			
			i1 += from.mColumns;
			i2 += mColumns;
		}
		
		fireMatrixChanged();
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
		
		fireMatrixChanged();
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#setTextColumn(int, java.util.List)
	 */
	@Override
	public void setTextColumn(int column, List<String> values) {
		int r = Math.min(getRowCount(), values.size());
		
		int ix = getIndex(0, column);
		
		for (int i = 0; i < r; ++i) {
			mData[ix] = values.get(i);
			
			ix += mColumns;
		}
		
		fireMatrixChanged();
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#columnAsText(int)
	 */
	@Override
	public List<String> columnAsText(int column) {
		int r = getRowCount();
		
		List<String> values = new ArrayList<String>(r);
		
		int i1 = getIndex(0, column);
		
		for (int row = 0; row < r; ++row) {
			values.add(mData[i1]);
			
			i1 += mColumns;
		}

		return values;
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#rowAsText(int)
	 */
	@Override
	public List<String> rowAsText(int row) {
		int c = getColumnCount();
		
		List<String> values = new ArrayList<String>(c);
		
		int i1 = getIndex(row, 0);
		
		for (int col = 0; col < c; ++col) {
			values.add(mData[i1++]);
		}

		return values;
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#transpose()
	 */
	@Override
	public Matrix transpose() { 
		TextMatrix ret = new TextMatrix(mColumns, mRows);

		int c = 0;
		int i2 = 0;
		
		for (int i = 0; i < mData.length; ++i) {
			if (i % mColumns == 0) {
				i2 = c++;
			}
			
			ret.mData[i2] = mData[i];
			
			i2 += mRows;
		}
		
		return ret;
	}
	
	//
	// Static methods
	//
	
	/**
	 * Creates the text matrix.
	 *
	 * @param m the m
	 * @return the text matrix
	 */
	public static TextMatrix createTextMatrix(Matrix m) {
		return createTextMatrix(m.getRowCount(), m.getColumnCount());
	}
	
	/**
	 * Creates the text matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @return the text matrix
	 */
	public static TextMatrix createTextMatrix(int rows, int columns) {
		return new TextMatrix(rows, columns);
	}
}
