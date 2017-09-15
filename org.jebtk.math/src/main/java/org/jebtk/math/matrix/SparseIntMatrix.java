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

import org.jebtk.core.Mathematics;


// TODO: Auto-generated Javadoc
/**
 * Number/String matrix using sparse representation.
 * 
 * @author Antony Holmes Holmes
 */
public class SparseIntMatrix extends SparseMatrix<Integer>  {

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
	public SparseIntMatrix(int rows, int columns) {
		super(rows, columns);
	}

	/**
	 * Instantiates a new mixed matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @param v the v
	 */
	public SparseIntMatrix(int rows, int columns, double v) {
		super(rows, columns);

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
	public SparseIntMatrix(int rows, int columns, String v) {
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
	public SparseIntMatrix(Matrix m) {
		super(m);
	}

	/**
	 * Instantiates a new mixed matrix.
	 *
	 * @param m the m
	 */
	public SparseIntMatrix(IndexableMatrix m) {
		super(m);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#copy()
	 */
	@Override
	public Matrix copy() {
		return new SparseIntMatrix(this);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#updateValue(int, double)
	 */
	@Override
	public void update(int index, double v) {
		update(index, (int)v);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#update(int, int)
	 */
	@Override
	public void update(int index, int v) {
		mData.put(index, v);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#get(int)
	 */
	@Override
	public Object get(int index) {
		double v = getValue(index);

		if (Mathematics.isValidNumber(v)) {
			return v;
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#getValue(int)
	 */
	@Override
	public double getValue(int index) {
		return getIntValue(index);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#getIntValue(int)
	 */
	@Override
	public int getIntValue(int index) {
		if (mData.containsKey(index)) {
			return mData.get(index);
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#getText(int)
	 */
	@Override
	public String getText(int index) {
		return Double.toString(getValue(index));
	}
}
