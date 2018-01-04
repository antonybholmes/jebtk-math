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

import org.jebtk.core.text.TextUtils;

// TODO: Auto-generated Javadoc
/**
 * Matrix that can be dynamically resized to match maximum row/column.
 * 
 * @author Antony Holmes Holmes
 */
public class ExpandTextMatrix extends ExpandMatrix<String> {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new dynamic double matrix.
	 */
	public ExpandTextMatrix() {
		this(0, 0);
	}

	/**
	 * Instantiates a new mixed sparse matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 */
	public ExpandTextMatrix(int rows, int columns) {
		super(rows, columns, TextUtils.EMPTY_STRING);
	}

	/**
	 * Instantiates a new mixed sparse matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @param v the v
	 */
	public ExpandTextMatrix(int rows, int columns, String v) {
		super(rows, columns, v);
	}

	/**
	 * Clone a matrix optionally copying the core matrix values and the
	 * annotation.
	 *
	 * @param m the m
	 */
	public ExpandTextMatrix(Matrix m) {
		super(m, TextUtils.EMPTY_STRING);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#copy()
	 */
	@Override
	public Matrix copy() {
		return new ExpandTextMatrix(this);
	}
	
	@Override
	public Matrix ofSameType() {
		return new ExpandTextMatrix(mDim.mRows, mDim.mCols);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DynamicMatrix#update(double)
	 */
	@Override
	public void update(double v) {
		update(Double.toString(v));
	}
	
	@Override
	public void update(String v) {
		for (int i = 0; i < mDim.mRows; ++i) {
			for (int j = 0; j < mDim.mCols; ++j) {
				mData.get(i).set(j, v);
			}
		}
	}

	@Override
	public void update(int row, int column, double v) {
		update(row, column, Double.toString(v));
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DynamicMatrix#update(int, int, double)
	 */
	@Override
	public void update(int row, int column, String v) {
		mData.get(row).set(column, v);

		super.update(row, column, v);
	}
	
	@Override
	public Matrix transpose() {
		return transpose(this);
	}
	
	public static Matrix transpose(ExpandTextMatrix m) {
		ExpandTextMatrix ret = 
				createExpandTextMatrix(m.getCols(), m.getRows());

		// Swap row and column indices. We use index lookup to reduce
		// the number of number of times indices must be looked up to
		// set cell elements.

		for (int i = 0; i < m.getRows(); ++i) {
			for (int j = 0; j < m.getCols(); ++j) {
				ret.set(j, i, m.get(i, j));
			}
		}

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
	public static ExpandTextMatrix createExpandTextMatrix(int rows, int columns) {
		return new ExpandTextMatrix(rows, columns);
	}
	
	/**
	 * Creates the matrix.
	 *
	 * @return the matrix
	 */
	public static Matrix createExpandTextMatrix() {
		return new ExpandTextMatrix();
	}
}
