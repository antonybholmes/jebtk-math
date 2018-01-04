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

// TODO: Auto-generated Javadoc
/**
 * Matrix that can be dynamically resized to match maximum row/column.
 * 
 * @author Antony Holmes Holmes
 */
public class DynamicIntMatrix extends DynamicMatrix<Integer> {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new dynamic double matrix.
	 */
	public DynamicIntMatrix() {
		this(0, 0);
	}

	/**
	 * Instantiates a new mixed sparse matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 */
	public DynamicIntMatrix(int rows, int columns) {
		super(rows, columns);
	}

	/**
	 * Instantiates a new mixed sparse matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @param v the v
	 */
	public DynamicIntMatrix(int rows, int columns, int v) {
		super(rows, columns, v);
	}

	/**
	 * Clone a matrix optionally copying the core matrix values and the
	 * annotation.
	 *
	 * @param m the m
	 */
	public DynamicIntMatrix(Matrix m) {
		super(m);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#copy()
	 */
	@Override
	public Matrix copy() {
		return new DynamicIntMatrix(this);
	}
	
	@Override
	public Matrix ofSameType() {
		return new DynamicIntMatrix(mDim.mRows, mDim.mCols);
	}
	
	@Override
	public void update(int row, int column, double v) {
		update(row, column, (int)v);
	}
	
	@Override
	public void update(int row, int column, long v) {
		update(row, column, (int)v);
	}
	
	@Override
	public void update(int row, int column, int v) {
		mData.put(row, column, v);

		super.update(row, column, v);
	}
	
	@Override
	public void apply(final CellFunction f) {
		IterUtils.forEach(getRows(), getCols(), new ForEach2D() {
			@Override
			public void loop(int i, int j) {
				double v = f.apply(i, j, mData.get(i, j));
				
				if (isValidMatrixNum(v)) {
					mData.put(i, j, (int)v);
				} else {
					mData.put(i, j, 0);
				}
			}
		});
	}
	
	@Override
	public Matrix transpose() {
		return transpose(this);
	}
	
	public static Matrix transpose(final DynamicIntMatrix m) {
		final DynamicIntMatrix ret = 
				createDynamicIntMatrix(m.getCols(), m.getRows());

		// Swap row and column indices. We use index lookup to reduce
		// the number of number of times indices must be looked up to
		// set cell elements.

		IterUtils.forEach(m.getRows(), m.getCols(), new ForEach2D() {
			@Override
			public void loop(int i, int j) {
				ret.set(j, i, m.get(i, j));
			}
		});

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
	public static DynamicIntMatrix createDynamicIntMatrix(int rows, int columns) {
		return new DynamicIntMatrix(rows, columns);
	}
	
	/**
	 * Creates the matrix.
	 *
	 * @return the matrix
	 */
	public static Matrix createDynamicIntMatrix() {
		return new DynamicIntMatrix();
	}
}
