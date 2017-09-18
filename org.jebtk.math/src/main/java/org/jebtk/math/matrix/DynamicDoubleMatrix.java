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

// TODO: Auto-generated Javadoc
/**
 * Matrix that can be dynamically resized to match maximum row/column.
 * 
 * @author Antony Holmes Holmes
 */
public class DynamicDoubleMatrix extends DynamicMatrix<Double> {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new dynamic double matrix.
	 */
	public DynamicDoubleMatrix() {
		this(0, 0);
	}

	/**
	 * Instantiates a new mixed sparse matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 */
	public DynamicDoubleMatrix(int rows, int columns) {
		super(rows, columns);
	}

	/**
	 * Instantiates a new mixed sparse matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @param v the v
	 */
	public DynamicDoubleMatrix(int rows, int columns, double v) {
		super(rows, columns, v);
	}

	/**
	 * Clone a matrix optionally copying the core matrix values and the
	 * annotation.
	 *
	 * @param m the m
	 */
	public DynamicDoubleMatrix(Matrix m) {
		super(m);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#copy()
	 */
	@Override
	public Matrix copy() {
		return new DynamicDoubleMatrix(this);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DynamicMatrix#update(double)
	 */
	@Override
	public void update(double v) {
		for (int i = 0; i < mRows; ++i) {
			for (int j = 0; j < mColumns; ++j) {
				mData.put(i, j, v);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DynamicMatrix#update(int, int, double)
	 */
	@Override
	public void update(int row, int column, double v) {
		mData.put(row, column, v);

		super.update(row, column, v);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DynamicMatrix#updateToNull(int, int)
	 */
	@Override
	public void updateToNull(int row, int column) {
		mData.put(row, column, Matrix.NULL_NUMBER);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DynamicMatrix#getValue(int, int)
	 */
	@Override
	public double getValue(int row, int column) {
		Object v = mData.get(row, column);

		if (v != null) {
			return ((Double)v).doubleValue();
		} else {
			return Matrix.NULL_NUMBER;
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
	public Matrix transpose() {
		return transpose(this);
	}
	
	public static Matrix transpose(DynamicDoubleMatrix m) {
		DynamicDoubleMatrix ret = 
				createDynamicDoubleMatrix(m.getColumnCount(), m.getRowCount());

		// Swap row and column indices. We use index lookup to reduce
		// the number of number of times indices must be looked up to
		// set cell elements.

		for (int i = 0; i < m.getRowCount(); ++i) {
			for (int j = 0; j < m.getColumnCount(); ++j) {
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
	public static DynamicDoubleMatrix createDynamicDoubleMatrix(int rows, int columns) {
		return new DynamicDoubleMatrix(rows, columns);
	}
	
	/**
	 * Creates the matrix.
	 *
	 * @return the matrix
	 */
	public static Matrix createDynamicDoubleMatrix() {
		return new DynamicDoubleMatrix();
	}
}
