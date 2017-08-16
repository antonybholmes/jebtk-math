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
public class SparseMixedMatrix extends SparseMatrix<Object> {

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
	public SparseMixedMatrix(int rows, int columns) {
		this(rows, columns, NULL_NUMBER);
	}

	/**
	 * Instantiates a new mixed matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @param v the v
	 */
	public SparseMixedMatrix(int rows, int columns, double v) {
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
	public SparseMixedMatrix(int rows, int columns, String v) {
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
	public SparseMixedMatrix(Matrix m) {
		super(m);
	}

	/**
	 * Instantiates a new mixed matrix.
	 *
	 * @param m the m
	 */
	public SparseMixedMatrix(IndexableMatrix m) {
		super(m);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#getType()
	 */
	@Override
	public AnnotationType getType() {
		return AnnotationType.MIXED;
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#copy()
	 */
	@Override
	public Matrix copy() {
		return new SparseMixedMatrix(this);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#getCellType(int)
	 */
	@Override
	public CellType getCellType(int index) {
		if (mData.containsKey(index)) {
			if (mData.get(index) instanceof Number) {
				return CellType.NUMBER;
			} else {
				return CellType.TEXT;
			}
		} else {
			return CellType.TEXT;
		}

		//return mData.get(index).getCellType();
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#set(int, java.lang.Object)
	 */
	@Override
	public void update(int index, Object v) {
		if (v == null) {
			return;
		}

		if (v instanceof Double || v instanceof Integer || v instanceof String) {
			mData.put(index, v);
		} else {
			mData.put(index, v.toString());
		}
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
		double v = getValue(index);

		if (Mathematics.isValidNumber(v)) {
			return v;
		} else {
			return mData.get(index);
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#getValue(int)
	 */
	@Override
	public double getValue(int index) {
		Object v = mData.get(index);
		
		if (v != null) {
			if (v instanceof Double) {
				return (Double)v;
			} else if (v instanceof Integer) {
				return (Integer)v;
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#getIntValue(int)
	 */
	@Override
	public int getIntValue(int index) {
		Object v = mData.get(index);
		
		if (v != null) {
			if (v instanceof Integer) {
				return (Integer)v;
			} else if (v instanceof Double) {
				return ((Double)v).intValue();
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}
}
