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

import java.util.HashMap;
import java.util.Map;

import org.jebtk.core.text.TextUtils;


// TODO: Auto-generated Javadoc
/**
 * Number/String matrix using sparse representation.
 *
 * @author Antony Holmes Holmes
 * @param <T> the generic type
 */
public abstract class SparseMatrix<T> extends IndexableMatrix {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Represents cells are strings. Unused cells are set to null.
	 */
	protected Map<Integer, T> mData = null;
	
	/**
	 * Instantiates a new mixed matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 */
	public SparseMatrix(int rows, int columns) {
		super(rows, columns);
	}
	
	/**
	 * Instantiates a new sparse matrix.
	 *
	 * @param m the m
	 */
	public SparseMatrix(Matrix m) {
		super(m);
	}
	
	
	/**
	 * Instantiates a new sparse matrix.
	 *
	 * @param m the m
	 */
	public SparseMatrix(IndexableMatrix m) {
		super(m);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#createData(int, int, int)
	 */
	@Override
	protected void createData(int rows, int columns, int n) {
		mData = new HashMap<Integer, T>(n);
	}
	
	/**
	 * Update.
	 *
	 * @param m the m
	 */
	public void update(SparseMatrix<T> m) {
		mData = new HashMap<Integer, T>(m.mData);
	}

	/**
	 * Set a cell to null so that neither text nor number are valid.
	 *
	 * @param index the index
	 */
	@Override
	public void updateToNull(int index) {
		mData.remove(index);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#update(int, double)
	 */
	@Override
	public void update(int index, double v) {
		// Do nothing
	}
	
	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#updateText(int, java.lang.String)
	 */
	@Override
	public void update(int index, String v) {
		// Do nothing
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#get(int)
	 */
	@Override
	public Object get(int index) {
		Object v = mData.get(index);

		if (v != null && v instanceof Number) {
			return ((Number)v).doubleValue();
		} else {
			return Matrix.NULL_NUMBER;
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#getText(int)
	 */
	@Override
	public String getText(int index) {
		Object v = mData.get(index);

		if (v != null) {
			if (v instanceof String) {
				return (String)v;
			} else {
				return v.toString();
			}
		} else {
			return TextUtils.EMPTY_STRING;
		}
		
		/*
		Object o = mData.get(index);
		
		if (o == null) {
			return null;
		}
		
		if (o instanceof Number) {
			double v = ((Number)o).doubleValue();
			
			if (Mathematics.isInt(v)) {
				return Integer.toString((int)v);
			} else {
				return Double.toString(v);
			}
		} else if (o instanceof String) {
			return (String)o;
		} else {
			return o.toString();
		}
		*/
	}
}
