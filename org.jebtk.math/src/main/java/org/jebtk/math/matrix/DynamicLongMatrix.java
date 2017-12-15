/**
IntMatrix * Copyright (C) 2016, Antony Holmes
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

import org.jebtk.core.ForEach2D;
import org.jebtk.core.IterUtils;
import org.jebtk.core.Range;

// TODO: Auto-generated Javadoc
/**
 * Matrix that can be dynamically resized to match maximum row/column.
 * 
 * @author Antony Holmes Holmes
 */
public class DynamicLongMatrix extends DynamicMatrix<Long> {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new dynamic double matrix.
	 */
	public DynamicLongMatrix() {
		this(0, 0);
	}

	/**
	 * Instantiates a new mixed sparse matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 */
	public DynamicLongMatrix(int rows, int columns) {
		super(rows, columns);
	}

	/**
	 * Instantiates a new mixed sparse matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @param v the v
	 */
	public DynamicLongMatrix(int rows, int columns, long v) {
		super(rows, columns, v);
	}

	/**
	 * Clone a matrix optionally copying the core matrix values and the
	 * annotation.
	 *
	 * @param m the m
	 */
	public DynamicLongMatrix(Matrix m) {
		super(m);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#copy()
	 */
	@Override
	public Matrix copy() {
		return new DynamicLongMatrix(this);
	}
	
	@Override
	public Matrix ofSameType() {
		return new DynamicLongMatrix(mDim.mRows, mDim.mCols);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DynamicMatrix#updateToNull(int, int)
	 */
	@Override
	public void updateToNull(int row, int column) {
		mData.put(row, column, NULL_LONG_NUMBER);
	}
	
	@Override
	public void update(int row, int column, double v) {
		update(row, column, (long)v);
	}
	
	@Override
	public void update(int row, int column, int v) {
		update(row, column, (long)v);
	}
	
	@Override
	public void update(int row, int column, long v) {
		mData.put(row, column, v);

		super.update(row, column, v);
	}

	@Override
	public long getLong(int row, int column) {
		Object v = mData.get(row, column);

		if (v != null) {
			return (long)v;
		} else {
			return NULL_LONG_NUMBER;
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DynamicMatrix#getText(int, int)
	 */
	@Override
	public String getText(int row, int column) {
		Object v = mData.get(row, column);

		if (v != null) {
			return v.toString();
		} else {
			return null;
		}
	}

	@Override
	public void apply(CellFunction f) {
		for (int i = 0; i < getRows(); ++i) {
			for (int j = 0; j < getCols(); ++j) {
				double v = f.apply(i, j, mData.get(i, j));
				
				if (isValidMatrixNum(v)) {
					mData.put(i, j, (long)v);
				} else {
					mData.put(i, j, NULL_LONG_NUMBER);
				}
			}
		}
	}
	
	@Override
	public Matrix transpose() {
		return transpose(this);
	}
	
	public static Matrix transpose(final DynamicLongMatrix m) {
		final DynamicLongMatrix ret = 
				createDynamicLongMatrix(m.getCols(), m.getRows());

		// Swap row and column indices. We use index lookup to reduce
		// the number of number of times indices must be looked up to
		// set cell elements.

		IterUtils.forEach(m.getRows(), m.getCols(), new ForEach2D() {
			@Override
			public void loop(int i, int j) {
				ret.set(j, i, m.get(i, j));
			}});

		return ret;
	}

	//
	// Static methods
	//

	/**
	 * Creates the.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @return the dynamic double matrix
	 */
	public static DynamicLongMatrix createDynamicLongMatrix(int rows, int columns) {
		return new DynamicLongMatrix(rows, columns);
	}

	/**
	 * Creates the matrix.
	 *
	 * @return the matrix
	 */
	public static Matrix createDynamicLongMatrix() {
		return new DynamicLongMatrix();
	}
}
