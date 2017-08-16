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
import java.util.Collection;
import java.util.List;

import org.jebtk.core.Indexed;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.collections.UniqueArrayList;
import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;

// TODO: Auto-generated Javadoc
/**
 * Implementation of the annotation component of a matrix.
 * This allows annotations to be added a matrix, but does not specify
 * cell type etc.
 * 
 * @author Antony Holmes Holmes
 *
 */
public class AnnotatableMatrix extends AnnotationMatrix {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member row annotation.
	 */
	protected Annotation mRowAnnotation;

	/**
	 * The member column annotation.
	 */
	protected Annotation mColumnAnnotation;

	/** The m M. */
	private Matrix mM;

	/** The m rows. */
	private int mRows;

	/** The m cols. */
	private int mCols;

	/**
	 * Instantiates a new annotable matrix.
	 *
	 * @param m the m
	 */
	public AnnotatableMatrix(Matrix m) {
		this(m, false);
	}

	/**
	 * Create a new Annotatable matrix wrapping m.
	 *
	 * @param m the m
	 * @param copy If true, causes the underlying matrix to be copied rather
	 * 				than referenced.
	 */
	public AnnotatableMatrix(Matrix m, boolean copy) {
		if (copy) {
			mM = m.copy();
		} else {
			mM = m;
		}
		
		mRowAnnotation = new Annotation(m.getRowCount());
		mColumnAnnotation = new Annotation(m.getColumnCount());

		mRowAnnotation.addChangeListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent e) {
				refresh();
			}});

		mColumnAnnotation.addChangeListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent e) {
				refresh();
			}});
		
		mM.addMatrixListener(new MatrixEventListener() {

			@Override
			public void matrixChanged(ChangeEvent e) {
				changed();
			}});
		
		changed();
	}

	/**
	 * Instantiates a new annotatable matrix.
	 *
	 * @param matrix the matrix
	 */
	public AnnotatableMatrix(AnnotationMatrix matrix) {
		this(matrix, false);
	}

	/**
	 * Create a new annotation matrix by copying another.
	 *
	 * @param matrix the matrix
	 * @param copy 	If true causes both the annotatable and its underlying
	 * 					matrix to be copied. This deep copy should only be
	 * 					used when there is a need to manipulate the matrix
	 * 					itself.
	 */
	public AnnotatableMatrix(AnnotationMatrix matrix, boolean copy) {
		this(matrix.getInnerMatrix(), copy);

		copyRowAnnotations(matrix, this);
		
		
		copyColumnAnnotations(matrix, this);

		refresh();
	}

	/**
	 * Clone the annotations from an annotation matrix, but replace the
	 * inner matrix with a different one. Useful for when creating a
	 * copy of another matrix with updated values.
	 *
	 * @param matrix the matrix
	 * @param m the m
	 */
	public AnnotatableMatrix(AnnotationMatrix matrix, Matrix m) {
		this(m);

		copyRowAnnotations(matrix, this);
		copyColumnAnnotations(matrix, this);

		//SysUtils.err().println("rtr c name", getColumnName(2));
		
		refresh();
	}
	
	/**
	 * Changed.
	 */
	private void changed() {
		refresh();
	}

	/**
	 * Refresh.
	 */
	private void refresh() {
		mRows = mM.getRowCount() + getColumnAnnotationNames().size();
		mCols = mM.getColumnCount() + getRowAnnotationNames().size();
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#copy()
	 */
	@Override
	public Matrix copy() {
		return new AnnotatableMatrix(this);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#getInnerMatrix()
	 */
	@Override
	public Matrix getInnerMatrix() {
		return mM;
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#getType()
	 */
	@Override
	public AnnotationType getType() {
		return mM.getType();
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return mM.getRowCount();
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return mM.getColumnCount();
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#getExtRowCount()
	 */
	@Override
	public int getExtRowCount() {
		return mRows;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#getExtColumnCount()
	 */
	@Override
	public int getExtColumnCount() {
		return mCols;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#getValue(int, int)
	 */
	@Override
	public double getValue(int row, int column) {
		//return mM.getValue(row, column);

		if (row >= 0 && column >= 0) {
			return mM.getValue(row, column);
		} else if (row < 0 && column < 0) {
			return Matrix.NULL_NUMBER;
		} else if (row < 0) {
			return getColumnAnnotations(row).getValue(0, column);
		} else {
			// col < 0
			return getRowAnnotations(column).getValue(0, row);
		}

		/*
		 f (c.row < 0 && c.column < 0) {
				return Matrix.NULL_NUMBER;
			} else if (c.row < 0) {
				return getColumnAnnotations(row).getValue(0, c.column);
			} else if (c.column < 0) {
				return getRowAnnotations(column).getValue(0, c.row);
			} else {
				return mM.getValue(c.row, c.column);
			}
		 */
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#getText(int, int)
	 */
	@Override
	public String getText(int row, int column) {
		//return mM.getText(row, column);

		if (row >= 0 && column >= 0) {
			return mM.getText(row, column);
		} else if (row < 0 && column < 0) {
			return getRowAnnotationNames().get(getRowAnnotationNames().size() + column);
		} else if (row < 0) {
			return getColumnAnnotations(row).getText(0, column);
		} else {
			// col < 0
			return getRowAnnotations(column).getText(0, row);
		}
		
		/*
		MatrixCellRef c = translate(row, column);

		if (c.row < 0 && c.column < 0) {
			return getRowAnnotationNames().get(column);
		} else if (c.row < 0) {
			return getColumnAnnotations(row).getText(0, c.column);
		} else if (c.column < 0) {
			return getRowAnnotations(column).getText(0, c.row);
		} else {
			return mM.getText(c.row, c.column);
		}
		*/
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#get(int, int)
	 */
	@Override
	public Object get(int row, int column) {
		if (row >= 0 && column >= 0) {
			return mM.get(row, column);
		} else if (row < 0 && column < 0) {
			return getRowAnnotationNames().get(getRowAnnotationNames().size() + column);
		} else if (row < 0) {
			return getColumnAnnotations(row).get(0, column);
		} else {
			// col < 0
			return getRowAnnotations(column).get(0, row);
		}
		
		/*
		MatrixCellRef c = translate(row, column);

		if (c.row < 0 && c.column < 0) {
			return getRowAnnotationNames().get(column);
		} else if (c.row < 0) {
			return getColumnAnnotations(row).get(0, c.column);
		} else if (c.column < 0) {
			return getRowAnnotations(column).get(0, c.row);
		} else {
			return mM.get(c.row, c.column);
		}
		*/
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#update(int, int, double)
	 */
	@Override
	public void update(int row, int column, double v) {
		//mM.updateValue(row, column, v);

		if (row >= 0 && column >= 0) {
			mM.update(row, column, v);
		} else if (row < 0 && column < 0) {
			// Do nothing
		} else if (row < 0) {
			getColumnAnnotations(row).update(0, column, v);
		} else {
			// col < 0
			getRowAnnotations(column).update(0, row, v);
		}
		
		/*
		MatrixCellRef c = translate(row, column);

		if (c.row < 0 && c.column < 0) {
			// Do nothing
		} else if (c.row < 0) {
			getColumnAnnotations(row).updateValue(0, c.column, v);
		} else if (c.column < 0) {
			getRowAnnotations(column).updateValue(0, c.row, v);
		} else {
			mM.updateValue(c.row, c.column, v);
		}
		*/
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#update(int, int, java.lang.String)
	 */
	@Override
	public void update(int row, int column, String v) {
		//mM.updateText(row, column, v);

		if (row >= 0 && column >= 0) {
			mM.update(row, column, v);
		} else if (row < 0 && column < 0) {
			// Do nothing
		} else if (row < 0) {
			getColumnAnnotations(row).update(0, column, v);
		} else {
			// col < 0
			getRowAnnotations(column).update(0, row, v);
		}
		
		/*
		MatrixCellRef c = translate(row, column);

		if (c.row < 0 && c.column < 0) {
			// Do nothing
		} else if (c.row < 0) {
			getColumnAnnotations(row).updateText(0, c.column, v);
		} else if (c.column < 0) {
			getRowAnnotations(column).updateText(0, c.row, v);
		} else {
			mM.updateText(c.row, c.column, v);
		}
		*/
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#update(int, int, java.lang.Object)
	 */
	@Override
	public void update(int row, int column, Object v) {
		//mM.update(row, column, v);

		if (row >= 0 && column >= 0) {
			mM.update(row, column, v);
		} else if (row < 0 && column < 0) {
			// Do nothing
		} else if (row < 0) {
			getColumnAnnotations(row).update(0, column, v);
		} else {
			// col < 0
			getRowAnnotations(column).update(0, row, v);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#updateToNull(int, int)
	 */
	@Override
	public void updateToNull(int row, int column) {
		mM.updateToNull(row, column);
	}

	/**
	 * Returns the names of each extra column of annotation for
	 * the rows. This is exclusive of the core matrix.
	 *
	 * @return the row annotation names
	 */
	@Override
	public List<String> getRowAnnotationNames() {
		return mRowAnnotation.getNames();
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.AnnotationMatrix#getRowAnnotationText(java.lang.String)
	 */
	@Override
	public Matrix getRowAnnotations(String name) {
		return mRowAnnotation.getAnnotation(name);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#getRowAnnotations(int)
	 */
	@Override
	public Matrix getRowAnnotations(int i) {
		return mRowAnnotation.getAnnotation(i);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.AnnotationMatrix#setRowAnnotation(java.lang.String, java.util.List)
	 */
	//@Override
	//public void setRowAnnotations(String name, Collection<? extends Object> values) {
	//	mRowAnnotation.setAnnotation(name, values);
	//}

	@Override
	public void setRowAnnotations(String name, Matrix values) {
		mRowAnnotation.setAnnotation(name, values);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#setNumRowAnnotations(java.lang.String, java.util.Collection)
	 */
	@Override
	public void setNumRowAnnotations(String name, Collection<? extends Number> values) {
		mRowAnnotation.setNumAnnotation(name, values);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#setNumRowAnnotations(java.lang.String, double[])
	 */
	@Override
	public void setNumRowAnnotations(String name, double[] values) {
		mRowAnnotation.setNumAnnotation(name, values);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#setNumRowAnnotations(java.lang.String, int[])
	 */
	@Override
	public void setNumRowAnnotations(String name, int[] values) {
		mRowAnnotation.setNumAnnotation(name, values);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#setTextRowAnnotations(java.lang.String, java.util.Collection)
	 */
	@Override
	public void setTextRowAnnotations(String name, Collection<String> values) {
		mRowAnnotation.setTextAnnotation(name, values);
	}
	

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.AnnotationMatrix#setRowAnnotation(java.lang.String, int, java.lang.Object)
	 */
	@Override
	public void setRowAnnotation(String name, int row, double value) {
		mRowAnnotation.setAnnotation(name, row, value);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#setRowAnnotation(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void setRowAnnotation(String name, int row, String value) {
		mRowAnnotation.setAnnotation(name, row, value);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#setRowAnnotation(java.lang.String, int, java.lang.Object)
	 */
	@Override
	public void setRowAnnotation(String name, int row, Object value) {
		mRowAnnotation.setAnnotation(name, row, value);
	}
	
	

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.MatrixAnnotations#getColumnAnnotationNames()
	 */
	@Override
	public List<String> getColumnAnnotationNames() {
		return mColumnAnnotation.getNames();
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.AnnotationMatrix#getColumnAnnotations(java.lang.String)
	 */
	@Override
	public Matrix getColumnAnnotations(String name) {
		return mColumnAnnotation.getAnnotation(name);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#getColumnAnnotations(int)
	 */
	@Override
	public Matrix getColumnAnnotations(int i) {
		return mColumnAnnotation.getAnnotation(i);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.AnnotationMatrix#setColumnAnnotation(java.lang.String, java.util.List)
	 */
	//@Override
	//public void setColumnAnnotations(String name, Collection<? extends Object> values) {
	//	mColumnAnnotation.setAnnotation(name, values);
	//}

	@Override
	public void setColumnAnnotations(String name, Matrix m) {
		mColumnAnnotation.setAnnotation(name, m);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#setNumColumnAnnotations(java.lang.String, java.util.Collection)
	 */
	@Override
	public void setNumColumnAnnotations(String name, 
			Collection<? extends Number> values) {
		mColumnAnnotation.setNumAnnotation(name, values);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#setNumColumnAnnotations(java.lang.String, double[])
	 */
	@Override
	public void setNumColumnAnnotations(String name, double[] values) {
		mColumnAnnotation.setNumAnnotation(name, values);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#setNumColumnAnnotations(java.lang.String, int[])
	 */
	@Override
	public void setNumColumnAnnotations(String name, int[] values) {
		mColumnAnnotation.setNumAnnotation(name, values);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#setTextColumnAnnotations(java.lang.String, java.util.Collection)
	 */
	@Override
	public void setTextColumnAnnotations(String name, Collection<String> values) {
		mColumnAnnotation.setTextAnnotation(name, values);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.AnnotationMatrix#setColumnAnnotation(java.lang.String, int, java.lang.Object)
	 */
	@Override
	public void setColumnAnnotation(String name, int column, double value) {
		mColumnAnnotation.setAnnotation(name, column, value);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#setColumnAnnotation(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void setColumnAnnotation(String name, int column, String value) {
		mColumnAnnotation.setAnnotation(name, column, value);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#setColumnAnnotation(java.lang.String, int, java.lang.Object)
	 */
	@Override
	public void setColumnAnnotation(String name, int column, Object value) {
		mColumnAnnotation.setAnnotation(name, column, value);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#columnAsList(int)
	 */
	@Override
	public List<Object> columnAsList(int column) {
		if (column < 0) {
			return getRowAnnotations(getRowAnnotationNames().get(getRowAnnotationNames().size() + column)).rowAsList(0);
		} else {
			return mM.columnAsList(column);
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#columnAsDouble(int)
	 */
	@Override
	public double[] columnAsDouble(int column) {
		if (column < 0) {
			return getRowAnnotations(getRowAnnotationNames().get(getRowAnnotationNames().size() + column)).rowAsDouble(0);
		} else {
			return mM.columnAsDouble(column);
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#columnAsText(int)
	 */
	@Override
	public List<String> columnAsText(int column) {
		if (column < 0) {
			return getRowAnnotations(getRowAnnotationNames().get(getRowAnnotationNames().size() + column)).rowAsText(0);
		} else {
			return mM.columnAsText(column);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#rowAsList(int)
	 */
	@Override
	public List<Object> rowAsList(int column) {
		if (column < 0) {
			return getRowAnnotations(getRowAnnotationNames().get(getRowAnnotationNames().size() + column)).rowAsList(0);
		} else {
			return mM.rowAsList(column);
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#rowAsDouble(int)
	 */
	@Override
	public double[] rowAsDouble(int column) {
		if (column < 0) {
			return getRowAnnotations(getRowAnnotationNames().get(getRowAnnotationNames().size() + column)).rowAsDouble(0);
		} else {
			return mM.rowAsDouble(column);
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#rowAsText(int)
	 */
	@Override
	public List<String> rowAsText(int column) {
		if (column < 0) {
			return getRowAnnotations(getRowAnnotationNames().get(getRowAnnotationNames().size() + column)).rowAsText(0);
		} else {
			return mM.rowAsText(column);
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#extractText()
	 */
	@Override
	public AnnotationMatrix extractText() {
		Matrix innerM = getInnerMatrix();
		
		int cn = innerM.getColumnCount();
		
		List<Integer> columns = new ArrayList<Integer>(cn);

		int rn = innerM.getRowCount();
		
		List<Integer> rows = new ArrayList<Integer>(rn);
		
		Matrix m = Matrix.extractText(innerM, rows, columns);
		
		if (columns.size() == 0 || rows.size() == 0) {
			return null;
		}
		
		AnnotationMatrix ret = AnnotatableMatrix.createAnnotatableMatrix(m);
		
		copyAnnotations(rows, columns, this, ret);
		
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.AnnotationMatrix#extractNumbers()
	 */
	@Override
	public AnnotationMatrix extractNumbers() {
		Matrix innerM = getInnerMatrix();
		
		int cn = innerM.getColumnCount();
		
		List<Integer> columns = new ArrayList<Integer>(cn);
		
		int rn = innerM.getRowCount();
		
		List<Integer> rows = new ArrayList<Integer>(rn);
		
		Matrix m = Matrix.extractNumbers(innerM, rows, columns);
		
		if (columns.size() == 0 || rows.size() == 0) {
			return null;
		}
		
		AnnotationMatrix ret = AnnotatableMatrix.createAnnotatableMatrix(m);
		
		copyAnnotations(rows, columns, this, ret);
		
		return ret;
	}
	
	//
	// Static methods
	//


	/**
	 * Copy inner columns.
	 *
	 * @param m the m
	 * @param columns the columns
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix copyInnerColumns(final AnnotationMatrix m, 
			int... columns) {
		return copyInnerColumns(m, CollectionUtils.toList(columns));
	}

	/**
	 * Copy inner columns.
	 *
	 * @param m the m
	 * @param iter the iter
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix copyInnerColumns(final AnnotationMatrix m, 
			final Iterable<? extends MatrixGroup> iter) {
		return copyColumns(m, iter);
	}
	
	/**
	 * Copy columns.
	 *
	 * @param m the m
	 * @param iter the iter
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix copyColumns(final AnnotationMatrix m, 
			final Iterable<? extends MatrixGroup> iter) {
		List<Integer> columns = new UniqueArrayList<Integer>();

		for (MatrixGroup g : iter) {
			columns.addAll(MatrixGroup.findColumnIndices(m, g));
		}

		return copyColumns(m, columns);
	}


	/**
	 * Copy inner columns.
	 *
	 * @param <T> the generic type
	 * @param m the m
	 * @param g the g
	 * @return the annotation matrix
	 */
	public static <T extends MatrixGroup> AnnotationMatrix copyInnerColumns(final AnnotationMatrix m, 
			T g) {
		List<Integer> columns = MatrixGroup.findColumnIndices(m, g);

		return copyInnerColumns(m, columns);
	}

	/**
	 * Copy columns.
	 *
	 * @param m the m
	 * @param columns the columns
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix copyColumns(final AnnotationMatrix m, 
			final List<Integer> columns) {
		Matrix innerM = m.getInnerMatrix();

		Matrix newInnerM = ofSameType(m, m.getRowCount(), columns.size());

		copyColumns(innerM, newInnerM, columns);

		AnnotationMatrix ret = new AnnotatableMatrix(newInnerM);

		copyRowAnnotations(m, ret);
		copyColumnAnnotations(m, ret, columns);

		return ret;
	}
	
	/**
	 * Copy inner columns.
	 *
	 * @param m the m
	 * @param columns the columns
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix copyInnerColumns(final AnnotationMatrix m, 
			final List<Integer> columns) {
		return copyColumns(m, columns);
	}

	/**
	 * Copy inner columns indexed.
	 *
	 * @param <V> the value type
	 * @param m the m
	 * @param columns the columns
	 * @return the annotation matrix
	 */
	public static <V extends Comparable<? super V>> AnnotationMatrix copyInnerColumnsIndexed(AnnotationMatrix m, 
			List<Indexed<Integer, V>> columns) {
		Matrix innerM = m.getInnerMatrix();

		Matrix newInnerM = ofSameType(m, m.getRowCount(), columns.size());

		copyColumnsIndexed(innerM, newInnerM, columns);

		AnnotationMatrix ret = new AnnotatableMatrix(newInnerM);

		copyRowAnnotations(m, ret);
		copyColumnAnnotationsIndexed(m, ret, columns);

		return ret;
	}

	/**
	 * Copy inner rows.
	 *
	 * @param m the m
	 * @param rows the rows
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix copyInnerRows(AnnotationMatrix m, int... rows) {
		return copyRows(m, CollectionUtils.toList(rows));
	}

	/**
	 * Copy inner rows.
	 *
	 * @param m the m
	 * @param iter the iter
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix copyInnerRows(AnnotationMatrix m, Iterable<? extends MatrixGroup> iter) {
		List<Integer> columns = new UniqueArrayList<Integer>();

		for (MatrixGroup g : iter) {
			columns.addAll(MatrixGroup.findColumnIndices(m, g));
		}

		return copyRows(m, columns);
	}


	/**
	 * Copy inner rows.
	 *
	 * @param <T> the generic type
	 * @param m the m
	 * @param g the g
	 * @return the annotation matrix
	 */
	public static <T extends MatrixGroup> AnnotationMatrix copyInnerRows(AnnotationMatrix m, T g) {
		List<Integer> columns = MatrixGroup.findColumnIndices(m, g);

		return copyRows(m, columns);
	}

	/**
	 * Copy some rows from an annotation matrix to a new annotation matrix.
	 * Indices begin at zero and do not include the column annotations, i.e.
	 * row indices refer to the inner matrix
	 *
	 * @param m the m
	 * @param rows the rows
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix copyRows(AnnotationMatrix m, 
			List<Integer> rows) {
		Matrix innerM = m.getInnerMatrix();

		Matrix newInnerM = ofSameType(m, rows.size(), m.getColumnCount());

		copyRows(innerM, newInnerM, rows);

		AnnotationMatrix ret = new AnnotatableMatrix(newInnerM);

		copyRowAnnotations(m, ret, rows);
		copyColumnAnnotations(m, ret);

		return ret;
	}

	/**
	 * Copy inner rows indexed.
	 *
	 * @param <V> the value type
	 * @param m the m
	 * @param rows the rows
	 * @return the annotation matrix
	 */
	public static <V extends Comparable<? super V>> AnnotationMatrix copyInnerRowsIndexed(AnnotationMatrix m, 
			List<Indexed<Integer, V>> rows) {
		Matrix innerM = m.getInnerMatrix();

		Matrix newInnerM = ofSameType(m, rows.size(), m.getColumnCount());

		copyRowsIndexed(innerM, newInnerM, rows);

		AnnotationMatrix ret = new AnnotatableMatrix(newInnerM);

		copyRowAnnotationsIndexed(m, ret, rows);
		copyColumnAnnotations(m, ret);

		return ret;
	}

	/**
	 * Creates an annotatable matrix with an underlying general purpose
	 * matrix for storing numbers and strings.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix createAnnotatableMatrix(int rows, 
			int columns) {
		return createAnnotatableMixedMatrix(rows, columns);
	}

	/**
	 * Creates a new annotation matrix of size row x columns. The size of
	 * the new matrix includes the row and column annotations copied from m
	 * to the new matrix. Thus if m has 1 row annotation and 1 column annotation
	 * (row names and column names) and rows = 5 and columns = 4, the new
	 * matrix will have an inner matrix 4 x 3 and annotations to give it
	 * a final size of 5 X 4.
	 *
	 * @param m the m
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix createAnnotatableMatrix(final AnnotationMatrix m) {
		Matrix im = m.getInnerMatrix();

		AnnotationMatrix ret = 
				createAnnotatableMatrix(im.getRowCount(), im.getColumnCount());

		copyAnnotations(m, ret);

		return ret;
	}

	/**
	 * Creates the annotatable mixed matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix createAnnotatableMixedMatrix(int rows, 
			int columns) {
		return new AnnotatableMatrix(new MixedMatrix(rows, columns));
	}
	
	/**
	 * Creates an annotation matrix from an underlying numerical matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix createNumericalMatrix(int rows, int columns) {
		return new AnnotatableMatrix(new DoubleMatrix(rows, columns));
	}

	/**
	 * Creates the numerical matrix.
	 *
	 * @param m the m
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix createNumericalMatrix(AnnotationMatrix m) {
		AnnotationMatrix ret = 
				createNumericalMatrix(m.getRowCount(), m.getColumnCount());

		copyAnnotations(m, ret);

		return ret;
	}

	/**
	 * Creates the text matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix createTextMatrix(int rows, int columns) {
		return new AnnotatableMatrix(new TextMatrix(rows, columns));
	}

	/**
	 * Creates the text matrix.
	 *
	 * @param m the m
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix createTextMatrix(AnnotationMatrix m) {
		AnnotationMatrix ret = createTextMatrix(m.getRowCount(), m.getColumnCount());

		copyAnnotations(m, ret);

		return ret;
	}
	
	/**
	 * Creates the dynamic matrix.
	 *
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix createDynamicMatrix() {
		return createAnnotatableMatrix(new DynamicMixedMatrix());
	}

	/**
	 * Creates the dynamic matrix.
	 *
	 * @param m the m
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix createDynamicMatrix(AnnotationMatrix m) {
		AnnotationMatrix ret = createDynamicMatrix();

		copyAnnotations(m, ret);

		return ret;
	}
	
	/**
	 * Create an annotatable matrix from a matrix.
	 *
	 * @param m the m
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix createAnnotatableMatrix(Matrix m) {
		return new AnnotatableMatrix(m);
	}
	
	/**
	 * Creates the annotatable matrix from columns.
	 *
	 * @param m the m
	 * @param columns the columns
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix createAnnotatableMatrixFromColumns(AnnotationMatrix m, 
			List<Integer> columns) {
		AnnotationMatrix ret = createAnnotatableMatrix(m.getRowCount(), columns.size());
		
		AnnotationMatrix.copyRowAnnotations(m, ret);
		
		for (int i = 0; i < columns.size(); ++i) {
			ret.copyColumn(m, columns.get(i), i);
		}
		
		return ret;
	}
	
	/**
	 * Creates the annotatable matrix from rows.
	 *
	 * @param m the m
	 * @param rows the rows
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix createAnnotatableMatrixFromRows(AnnotationMatrix m, 
			List<Integer> rows) {
		AnnotationMatrix ret = createAnnotatableMatrix(rows.size(), m.getColumnCount());
		
		AnnotationMatrix.copyColumnAnnotations(m, ret);
		
		for (int i = 0; i < rows.size(); ++i) {
			ret.copyRow(m, rows.get(i), i);
		}
		
		return ret;
	}
}
