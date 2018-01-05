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

// TODO: Auto-generated Javadoc
/**
 * Number/String matrix using sparse representation.
 * 
 * @author Antony Holmes Holmes
 */
public class SparseTextMatrix extends SparseMatrix<String> {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new matrix defaulting to being entirely numeric.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 */
	public SparseTextMatrix(int rows, int columns) {
		super(rows, columns);
	}

	/**
	 * Instantiates a new mixed matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @param v the v
	 */
	public SparseTextMatrix(int rows, int columns, double v) {
		this(rows, columns);

		// Set the default value
		set(v);
	}

	/**
	 * Instantiates a new mixed matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @param v the v
	 */
	public SparseTextMatrix(int rows, int columns, String v) {
		this(rows, columns);

		// Set the default value
		set(v);
	}

	/**
	 * Clone a matrix optionally copying the core matrix values and the
	 * annotation.
	 *
	 * @param m the m
	 */
	public SparseTextMatrix(Matrix m) {
		super(m);
	}

	/**
	 * Instantiates a new mixed matrix.
	 *
	 * @param m the m
	 */
	public SparseTextMatrix(IndexRowMatrix m) {
		super(m);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#copy()
	 */
	@Override
	public Matrix copy() {
		return new SparseTextMatrix(this);
	}
	
	@Override
	public Matrix ofSameType(int rows, int cols) {
		return new SparseTextMatrix(rows, cols);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#getCellType(int)
	 */
	@Override
	public CellType getCellType(int index) {
		return CellType.TEXT;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#update(int, java.lang.Object)
	 */
	@Override
	public void update(int index, Object v) {
		if (v != null) {
			if (v instanceof String) {
				mData.put(index, (String)v);
			} else {
				mData.put(index, v.toString());
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#updateValue(int, double)
	 */
	@Override
	public void update(int index, double v) {
		update(index, Double.toString(v));
	}

	@Override
	public void update(int index, long v) {
		update(index, Long.toString(v));
	}

	@Override
	public void update(int index, int v) {
		update(index, Integer.toString(v));
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#updateText(int, java.lang.String)
	 */
	@Override
	public void update(int index, String v) {
		mData.put(index, v);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#get(int)
	 */
	@Override
	public Object get(int index) {
		return mData.get(index);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#getText(int)
	 */
	@Override
	public String getText(int index) {
		return mData.get(index);
	}

	@Override
	public Matrix transpose() { 
		return transpose(this);
	}

	public static Matrix transpose(final SparseTextMatrix m) { 
		SparseTextMatrix ret = new SparseTextMatrix(m.mDim.mCols, m.mDim.mRows);

		int i2 = 0;
		int c = 0;

		for (int i : m.mData.keySet()) {
			// Each time we end a row, reset i2 back to the next column
			if (i % m.mDim.mCols == 0) {
				i2 = c++;
			}

			ret.mData.put(i2, m.mData.get(i));

			// Skip blocks
			i2 += m.mDim.mRows;
		}

		return ret;
	}
}
