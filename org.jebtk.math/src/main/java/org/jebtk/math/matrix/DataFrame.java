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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jebtk.core.Indexed;
import org.jebtk.core.NameProperty;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.collections.UniqueArrayList;
import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.text.Join;
import org.jebtk.core.text.Splitter;
import org.jebtk.core.text.TextUtils;

// TODO: Auto-generated Javadoc
/**
 * Wraps a matrix in annotatable columns and rows to make it more useful in
 * data analysis.
 * 
 * @author Antony Holmes
 *
 */
public class DataFrame extends Matrix implements NameProperty, MatrixAnnotations {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The constant ROW_NAMES.
	 */
	public static final String ROW_NAMES = "Row Names";

	/**
	 * The constant COLUMN_NAMES.
	 */
	public static final String COLUMN_NAMES = "Column Names";

	/**
	 * The constant EST_ANNOTATION_ROWS.
	 */
	public static final String EST_ANNOTATION_ROWS = "#annotation-rows";

	/**
	 * The constant EST_ANNOTATION_COLUMNS.
	 */
	public static final String EST_ANNOTATION_COLUMNS = "#annotation-columns";

	/**
	 * The constant EST_ANNOTATION_ROW.
	 */
	public static final String EST_ANNOTATION_ROW = "#annotation-row";

	/**
	 * The constant EST_ANNOTATION_COLUMN.
	 */
	public static final String EST_ANNOTATION_COLUMN = "#annotation-column";

	/**
	 * The constant EST_ANNOTATION_GROUPS.
	 */
	public static final String EST_ANNOTATION_GROUPS = "#groups";

	/**
	 * The constant EST_ROWS.
	 */
	public static final String EST_ROWS = "#rows";

	/**
	 * The constant EST_COLUMNS.
	 */
	public static final String EST_COLUMNS = "#columns";

	/**
	 * The constant EST_VERSION_1.
	 */
	public static final String EST_VERSION_1 = "#1.0";

	/**
	 * The constant EST_VERSION_2.
	 */
	public static final String EST_VERSION_2 = "#2.0";

	/**
	 * The constant EST_MATRIX.
	 */
	private static final String EST_MATRIX = "#matrix";

	/**
	 * The constant START_CELL.
	 */
	public static final MatrixCellRef START_CELL = new MatrixCellRef(0, 0);

	/** The m name. */
	private String mName = TextUtils.EMPTY_STRING;

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
	private MatrixDim mDim;
	/**
	 * Instantiates a new annotable matrix.
	 *
	 * @param m the m
	 */
	public DataFrame(Matrix m) {
		this(m, false);
	}

	/**
	 * Create a new Annotatable matrix wrapping m.
	 *
	 * @param m the m
	 * @param copy If true, causes the underlying matrix to be copied rather
	 * 				than referenced.
	 */
	public DataFrame(Matrix m, boolean copy) {
		super(-1, -1);
		
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
		
		getMatrix().addMatrixListener(new MatrixEventListener() {
			@Override
			public void matrixChanged(ChangeEvent e) {
				changed();
			}});
		
		changed();
	}

	/**
	 * Instantiates a new annotatable matrix.
	 *
	 * @param frame the matrix
	 */
	public DataFrame(DataFrame frame) {
		this(frame, false);
	}

	/**
	 * Create a new annotation matrix by copying another.
	 *
	 * @param frame the matrix
	 * @param copy 	If true causes both the annotatable and its underlying
	 * 					matrix to be copied. This deep copy should only be
	 * 					used when there is a need to manipulate the matrix
	 * 					itself.
	 */
	public DataFrame(DataFrame frame, boolean copy) {
		this(frame.getMatrix(), copy);

		copyAnnotations(frame, this);

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
	public DataFrame(DataFrame matrix, Matrix m) {
		this(m);

		copyAnnotations(matrix, this);

		refresh();
	}
	
	/**
	 * Optionally give the matrix a name.
	 *
	 * @param name the name
	 * @return the annotation matrix
	 */
	public DataFrame setName(String name) {
		mName = name;

		return this;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.NameProperty#getName()
	 */
	@Override
	public String getName() {
		return mName;
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
		mDim = new MatrixDim(getMatrix().getRowCount() + getColumnAnnotationNames().size(),
				getMatrix().getColumnCount() + getRowAnnotationNames().size());
	}

	/**
	 * If row is negative, this will cause the column annotations to be
	 * edited rather than the matrix. If column annotations are are negative,
	 * this will cause the row annotations to be edited.
	 *
	 * @param row the row
	 * @param column the column
	 * @param value the value
	 */
	@Override
	public void set(int row, int column, double value) {
		if (row >= 0 && column >= 0) {
			getMatrix().set(row, column, value);
		} else if (row < 0 && column >= 0) {
			getColumnAnnotations(-row - 1).set(0, column, value);
		} else if (row >= 0 && column < 0) {
			getRowAnnotations(-column - 1).set(0, row, value);
		} else {
			// Do nothing
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#set(int, int, java.lang.String)
	 */
	@Override
	public void set(int row, int column, String value) {
		if (row >= 0 && column >= 0) {
			getMatrix().set(row, column, value);
		} else if (row < 0 && column >= 0) {
			getColumnAnnotations(row).set(0, column, value);
		} else if (row >= 0 && column < 0) {
			getRowAnnotations(column).set(0, row, value);
		} else {
			// Do nothing
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#getCellType(int, int)
	 */
	@Override
	public CellType getCellType(int row, int column) {
		if (row >= 0 && column >= 0) {
			return getMatrix().getCellType(row, column);
		} else if (row < 0 && column < 0) {
			return CellType.TEXT;
		} else if (row < 0) {
			int s = altIndexModulo(row, getColumnAnnotationNames().size());
			return getColumnAnnotations(s).getCellType(0, column);
		} else {
			int s = altIndexModulo(column, getRowAnnotationNames().size());
			return getRowAnnotations(s).getCellType(0, row);
		}
	}

	/**
	 * Sets the row names.
	 *
	 * @param names the new row names
	 */
	public void setRowNames(List<String> names) {
		if (CollectionUtils.isNullOrEmpty(names)) {
			return;
		}

		int r = 0;

		for (String name : names) {
			setRowAnnotation(ROW_NAMES, r++, name);
		}
	}

	/**
	 * Sets the row names.
	 *
	 * @param names the new row names
	 */
	public void setRowNames(String... names) {
		setRowNames(Arrays.asList(names));
	}

	/**
	 * Sets the row name.
	 *
	 * @param row the row
	 * @param name the name
	 */
	public void setRowName(int row, String name) {
		setRowAnnotation(ROW_NAMES, row, name);
	}

	/**
	 * Gets the row names.
	 *
	 * @return the row names
	 */
	public List<String> getRowNames() {
		List<String> names = getRowAnnotationNames();

		if (names.size() > 0) {
			return getRowAnnotationText(names.get(0));
		} else {
			return Collections.emptyList();
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.MatrixAnnotations#getRowName(int)
	 */
	public String getRowName(int i) {
		return getRowNames().get(i);
	}
	
	/**
	 * Get the row annotation of a.
	 *
	 * @param name the name
	 * @param row the row
	 * @return the row annotation
	 */
	public Object getRowAnnotation(String name, int row) {
		return getRowAnnotations(name).get(0, row);
	}

	/**
	 * Gets the row annotation text.
	 *
	 * @param name the name
	 * @param row the row
	 * @return the row annotation text
	 */
	public String getRowAnnotationText(String name, int row) {
		String s = getRowAnnotations(name).getText(0, row);

		if (s != null) {
			return s;
		} else {
			return TextUtils.EMPTY_STRING;
		}
	}

	/**
	 * Gets the row annotation value.
	 *
	 * @param name the name
	 * @param row the row
	 * @return the row annotation value
	 */
	public double getRowAnnotationValue(String name, int row) {
		return getRowAnnotations(name).getValue(0, row);
	}

	/**
	 * Returns the row annotation at a particular column.
	 *
	 * @param row the row
	 * @return the row annotations
	 */
	@Override
	public List<String> getRowAnnotationText(int row) {
		List<String> ret = new ArrayList<String>();

		for (String name : getRowAnnotationNames()) {
			ret.add(getRowAnnotationText(name, row));
		}

		return ret;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.MatrixAnnotations#getRowAnnotationValues(int)
	 */
	@Override
	public List<Double> getRowAnnotationValues(int row) {
		List<Double> ret = new ArrayList<Double>();

		for (String name : getRowAnnotationNames()) {
			ret.add(getRowAnnotationValue(name, row));
		}

		return ret;
	}

	/**
	 * Returns the names of each extra column of annotation for
	 * the rows. This is exclusive of the core matrix.
	 *
	 * @param name the name
	 * @return the row annotations
	 */
	public double[] getRowAnnotationValues(String name) {
		return getRowAnnotations(name).rowAsDouble(0);
	}

	/**
	 * Gets the row annotation text.
	 *
	 * @param name the name
	 * @return the row annotation text
	 */
	public List<String> getRowAnnotationText(String name) {
		return getRowAnnotations(name).rowAsText(0);
	}

	//
	// Columns
	//

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.MatrixAnnotations#getColumnName(int)
	 */
	@Override
	public String getColumnName(int i) {
		if (i >= 0) {
			return getColumnNames().get(i % getColumnCount());
		} else {
			// If there is a negative number, choose the row annotation name
			// going right to left
			int s = altIndexModulo(i, getRowAnnotationNames().size());
			return getRowAnnotationName(s);
		}
	}

	/**
	 * Sets the column names.
	 *
	 * @param names the new column names
	 */
	public void setColumnNames(List<String> names) {
		if (CollectionUtils.isNullOrEmpty(names)) {
			return;
		}

		int c = 0;

		for (String name : names) {
			setColumnAnnotation(COLUMN_NAMES, c++, name);
		}
	}

	/**
	 * Sets the column names.
	 *
	 * @param names the new column names
	 */
	public void setColumnNames(String... names) {
		setColumnNames(Arrays.asList(names));
	}

	/**
	 * Sets the column name.
	 *
	 * @param column the column
	 * @param name the name
	 */
	public void setColumnName(int column, String name) {
		setColumnAnnotation(COLUMN_NAMES, column, name);
	}

	/**
	 * Gets the column names. This does not include the names of row
	 * annotations so its length will match the number of columns in the
	 * matrix.
	 *
	 * @return the column names
	 */
	public List<String> getColumnNames() {
		List<String> names = getColumnAnnotationNames();

		if (names.size() > 0) {
			return getColumnAnnotationText(names.get(0));
		} else {
			return Collections.emptyList();
		}
	}

	/**
	 * Returns true if the matrix has a header.
	 *
	 * @return the checks for header
	 */
	public boolean getHasHeader() {
		return getColumnNames().size() > 0;
	}


	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.MatrixAnnotations#getColumnAnnotationText(int)
	 */
	@Override
	public List<String> getColumnAnnotationText(int row) {
		List<String> ret = new ArrayList<String>();

		for (String name : getColumnAnnotationNames()) {
			ret.add(getColumnAnnotationText(name, row));
		}

		return ret;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.MatrixAnnotations#getColumnAnnotationValues(int)
	 */
	@Override
	public List<Double> getColumnAnnotationValues(int row) {
		List<Double> ret = new ArrayList<Double>();

		for (String name : getColumnAnnotationNames()) {
			ret.add(getColumnAnnotationValue(name, row));
		}

		return ret;
	}

	/**
	 * Gets the column annotation values.
	 *
	 * @param name the name
	 * @return the column annotation values
	 */
	public double[] getColumnAnnotationValues(String name) {
		return getColumnAnnotations(name).rowAsDouble(0);
	}

	/**
	 * Gets the column annotation text.
	 *
	 * @param name the name
	 * @return the column annotation text
	 */
	public List<String> getColumnAnnotationText(String name) {
		return getColumnAnnotations(name).rowAsText(0);
	}

	/**
	 * Gets the column annotations.
	 *
	 * @param name the name
	 * @param row the row
	 * @return the column annotations
	 */
	public Object getColumnAnnotations(String name, int row) {
		if (row < 0 || row >= getRowCount()) {
			return null;
		}

		return getColumnAnnotations(name).get(0, row);
	}

	/**
	 * Gets the column annotation.
	 *
	 * @param name the name
	 * @param row the row
	 * @return the column annotation
	 */
	public Object getColumnAnnotation(String name, int row) {
		return getColumnAnnotations(name).get(0, row);
	}

	/**
	 * Gets the column annotation text.
	 *
	 * @param name the name
	 * @param row the row
	 * @return the column annotation text
	 */
	public String getColumnAnnotationText(String name, int row) {
		String s = getColumnAnnotations(name).getText(0, row);

		if (s != null) {
			return s;
		} else {
			return TextUtils.EMPTY_STRING;
		}
	}

	/**
	 * Gets the column annotation value.
	 *
	 * @param name the name
	 * @param row the row
	 * @return the column annotation value
	 */
	public double getColumnAnnotationValue(String name, int row) {
		return getColumnAnnotations(name).getValue(0, row);
	}

	/*
	@Override
	public List<Object> columnAsList(int column) {
		if (column >= 0) {
			return getInnerMatrix().columnAsList(column);
		} else {
			return getRowAnnotations(column).rowAsList(0);
		}
	}

	@Override
	public List<Double> columnAsDouble(int column) {
		if (column >= 0) {
			return getInnerMatrix().columnAsDouble(column);
		} else {
			return getRowAnnotations(column).rowAsDouble(0);
		}
	}

	@Override
	public List<String> columnAsText(int column) {
		System.err.println("col as text " + column);

		if (column >= 0) {
			return getInnerMatrix().columnAsText(column);
		} else {
			return getRowAnnotations(column).rowAsText(0);
		}
	}
	 */
	
	@Override
	public Matrix applied(MatrixCellFunction f) {
		// Copy the matrix
		DataFrame ret = new DataFrame(this, true);

		ret.apply(f);

		return ret;
	}
	
	@Override
	public Matrix rowApplied(MatrixCellFunction f, int index) {
		// Copy the matrix
		DataFrame ret = new DataFrame(this, true);

		ret.rowApply(f, index);

		return ret;
	}
	
	@Override
	public Matrix colApplied(MatrixCellFunction f, int index) {
		// Copy the matrix
		DataFrame ret = new DataFrame(this, true);

		ret.colApply(f, index);

		return ret;
	}

	@Override
	public void apply(MatrixCellFunction f) {
		getMatrix().apply(f);
	}
	
	@Override
	public void rowApply(MatrixDimFunction f) {
		getMatrix().rowApply(f);
	}
	
	@Override
	public void rowApply(MatrixCellFunction f, int index) {
		getMatrix().rowApply(f, index);
	}
	
	@Override
	public void colApply(MatrixDimFunction f) {
		getMatrix().colApply(f);
	}
	
	@Override
	public void colApply(MatrixCellFunction f, int index) {
		getMatrix().colApply(f, index);
	}
	
	@Override
	public void rowEval(MatrixReduceFunction f, double[] ret) {
		getMatrix().rowEval(f, ret);
	}
	
	@Override
	public void rowEval(MatrixDimFunction f, int col, double[] ret) {
		getMatrix().rowEval(f, col, ret);
	}
	
	@Override
	public void colEval(MatrixDimFunction f, double[] ret) {
		getMatrix().colEval(f, ret);
	}
	
	@Override
	public void colEval(MatrixDimFunction f, int col, double[] ret) {
		getMatrix().colEval(f, col, ret);
	}
	
	/**
	 * Apply a stat function over a matrix.
	 * 
	 * @param f
	 * @return 
	 */
	@Override
	public double stat(MatrixStatFunction f) {
		return getMatrix().stat(f);
	}
	
	@Override
	public double rowStat(MatrixStatFunction f, int index) {
		return getMatrix().rowStat(f, index);
	}
	
	@Override
	public double colStat(MatrixStatFunction f, int index) {
		return getMatrix().colStat(f, index);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#transpose()
	 */
	@Override
	public Matrix transpose() {
		// Transpose the main matrix
		Matrix innerM = getMatrix().transpose();

		DataFrame ret = new DataFrame(innerM);

		// The first name is the row-name, which must be swapped for the
		// column name so we only copy the annotation for names(1, end) 
		// verbatim. The same is true for the columns
		//ret.setColumnNames(getRowNames());
		//ret.setRowNames(getColumnNames());

		for (String name : getRowAnnotationNames()) { //CollectionUtils.tail(getRowAnnotationNames())) {
			ret.setColumnAnnotations(name, getRowAnnotations(name));
		}

		for (String name : getColumnAnnotationNames()) { //CollectionUtils.tail(getColumnAnnotationNames())) {
			ret.setRowAnnotations(name, getColumnAnnotations(name));
		}

		return ret;
	}
	
	@Override
	public double[] toDouble() {
		return getMatrix().toDouble();
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#copy()
	 */
	@Override
	public Matrix copy() {
		return new DataFrame(this);
	}

	/**
	 * Returns the matrix underlying the data frame.
	 * 
	 * @return
	 */
	public Matrix getMatrix() {
		return mM;
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#getType()
	 */
	@Override
	public MatrixType getType() {
		return getMatrix().getType();
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return getMatrix().getRowCount();
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return getMatrix().getColumnCount();
	}

	/**
	 * Returns the row count inclusive of the number of annotation rows.
	 * 
	 * @return		a row count.
	 */
	public int getExtRowCount() {
		return mDim.mRows;
	}

	/**
	 * Returns the column count inclusive of the number of annotation rows.
	 * 
	 * @return		a column count.
	 */
	public int getExtColumnCount() {
		return mDim.mCols;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#getValue(int, int)
	 */
	@Override
	public double getValue(int row, int column) {
		//return getInnerMatrix().getValue(row, column);

		if (row >= 0 && column >= 0) {
			return getMatrix().getValue(row, column);
		} else if (row < 0 && column < 0) {
			return Matrix.NULL_NUMBER;
		} else if (row < 0) {
			return getColumnAnnotations(row).getValue(0, column);
		} else {
			// col < 0
			return getRowAnnotations(column).getValue(0, row);
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#getText(int, int)
	 */
	@Override
	public String getText(int row, int column) {
		//return getInnerMatrix().getText(row, column);

		if (row >= 0 && column >= 0) {
			return getMatrix().getText(row, column);
		} else if (row < 0 && column < 0) {
			return getRowAnnotationName(column);
		} else if (row < 0) {
			return getColumnAnnotations(row).getText(0, column);
		} else {
			// col < 0
			return getRowAnnotations(column).getText(0, row);
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#get(int, int)
	 */
	@Override
	public Object get(int row, int column) {
		if (row >= 0 && column >= 0) {
			return getMatrix().get(row, column);
		} else if (row < 0 && column < 0) {
			return getRowAnnotationName(column);
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
			return getInnerMatrix().get(c.row, c.column);
		}
		*/
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#update(int, int, double)
	 */
	@Override
	public void update(int row, int column, double v) {
		//getInnerMatrix().updateValue(row, column, v);

		if (row >= 0 && column >= 0) {
			getMatrix().update(row, column, v);
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
			getInnerMatrix().updateValue(c.row, c.column, v);
		}
		*/
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#update(int, int, java.lang.String)
	 */
	@Override
	public void update(int row, int column, String v) {
		//getInnerMatrix().updateText(row, column, v);

		if (row >= 0 && column >= 0) {
			getMatrix().update(row, column, v);
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
	 * @see org.abh.common.math.matrix.Matrix#update(int, int, java.lang.Object)
	 */
	@Override
	public void update(int row, int column, Object v) {
		//getInnerMatrix().update(row, column, v);

		if (row >= 0 && column >= 0) {
			getMatrix().update(row, column, v);
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
		getMatrix().updateToNull(row, column);
	}
	
	public Annotation getRowAnnotation() {
		return mRowAnnotation;
	}

	public String getRowAnnotationName(int index) {
		return mRowAnnotation.getName(index);
	}
	
	/**
	 * Returns the names of each extra column of annotation for
	 * the rows. This is exclusive of the core matrix.
	 *
	 * @return the row annotation names
	 */
	public List<String> getRowAnnotationNames() {
		return mRowAnnotation.getNames();
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.DataFrame#getRowAnnotationText(java.lang.String)
	 */
	public Matrix getRowAnnotations(String name) {
		return mRowAnnotation.getAnnotation(name);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#getRowAnnotations(int)
	 */
	public Matrix getRowAnnotations(int i) {
		return mRowAnnotation.getAnnotation(i);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.DataFrame#setRowAnnotation(java.lang.String, java.util.List)
	 */
	//@Override
	//public void setRowAnnotations(String name, Collection<? extends Object> values) {
	//	mRowAnnotation.setAnnotation(name, values);
	//}

	public void setRowAnnotations(String name, Matrix values) {
		mRowAnnotation.setAnnotation(name, values);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#setNumRowAnnotations(java.lang.String, java.util.Collection)
	 */
	public void setNumRowAnnotations(String name, Collection<? extends Number> values) {
		mRowAnnotation.setNumAnnotation(name, values);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#setNumRowAnnotations(java.lang.String, double[])
	 */
	public void setNumRowAnnotations(String name, double[] values) {
		mRowAnnotation.setNumAnnotation(name, values);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#setNumRowAnnotations(java.lang.String, int[])
	 */
	public void setNumRowAnnotations(String name, int[] values) {
		mRowAnnotation.setNumAnnotation(name, values);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#setTextRowAnnotations(java.lang.String, java.util.Collection)
	 */
	public void setTextRowAnnotations(String name, Collection<String> values) {
		mRowAnnotation.setTextAnnotation(name, values);
	}
	

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.DataFrame#setRowAnnotation(java.lang.String, int, java.lang.Object)
	 */
	public void setRowAnnotation(String name, int row, double value) {
		mRowAnnotation.setAnnotation(name, row, value);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#setRowAnnotation(java.lang.String, int, java.lang.String)
	 */
	public void setRowAnnotation(String name, int row, String value) {
		mRowAnnotation.setAnnotation(name, row, value);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#setRowAnnotation(java.lang.String, int, java.lang.Object)
	 */
	public void setRowAnnotation(String name, int row, Object value) {
		mRowAnnotation.setAnnotation(name, row, value);
	}
	
	public String getColumnAnnotationName(int index) {
		return mColumnAnnotation.getName(index);
	}
	
	public Annotation getColumnAnnotation() {
		return mColumnAnnotation;
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.MatrixAnnotations#getColumnAnnotationNames()
	 */
	public List<String> getColumnAnnotationNames() {
		return mColumnAnnotation.getNames();
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.DataFrame#getColumnAnnotations(java.lang.String)
	 */
	public Matrix getColumnAnnotations(String name) {
		return mColumnAnnotation.getAnnotation(name);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#getColumnAnnotations(int)
	 */
	public Matrix getColumnAnnotations(int i) {
		return mColumnAnnotation.getAnnotation(i);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.DataFrame#setColumnAnnotation(java.lang.String, java.util.List)
	 */
	//@Override
	//public void setColumnAnnotations(String name, Collection<? extends Object> values) {
	//	mColumnAnnotation.setAnnotation(name, values);
	//}

	public void setColumnAnnotations(String name, Matrix m) {
		mColumnAnnotation.setAnnotation(name, m);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#setNumColumnAnnotations(java.lang.String, java.util.Collection)
	 */
	public void setNumColumnAnnotations(String name, 
			Collection<? extends Number> values) {
		mColumnAnnotation.setNumAnnotation(name, values);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#setNumColumnAnnotations(java.lang.String, double[])
	 */
	public void setNumColumnAnnotations(String name, double[] values) {
		mColumnAnnotation.setNumAnnotation(name, values);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#setNumColumnAnnotations(java.lang.String, int[])
	 */
	public void setNumColumnAnnotations(String name, int[] values) {
		mColumnAnnotation.setNumAnnotation(name, values);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#setTextColumnAnnotations(java.lang.String, java.util.Collection)
	 */
	public void setTextColumnAnnotations(String name, Collection<String> values) {
		mColumnAnnotation.setTextAnnotation(name, values);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.DataFrame#setColumnAnnotation(java.lang.String, int, java.lang.Object)
	 */
	public void setColumnAnnotation(String name, int column, double value) {
		mColumnAnnotation.setAnnotation(name, column, value);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#setColumnAnnotation(java.lang.String, int, java.lang.String)
	 */
	public void setColumnAnnotation(String name, int column, String value) {
		mColumnAnnotation.setAnnotation(name, column, value);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#setColumnAnnotation(java.lang.String, int, java.lang.Object)
	 */
	public void setColumnAnnotation(String name, int column, Object value) {
		mColumnAnnotation.setAnnotation(name, column, value);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#columnAsList(int)
	 */
	@Override
	public Object[] columnAsList(int column) {
		if (column < 0) {
			return getRowAnnotations(getRowAnnotationNames().get(getRowAnnotationNames().size() + column)).rowAsList(0);
		} else {
			return getMatrix().columnAsList(column);
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#columnAsDouble(int)
	 */
	@Override
	public double[] columnAsDouble(int column) {
		if (column < 0) {
			return getRowAnnotations(getRowAnnotationNames().get(getRowAnnotationNames().size() + column)).rowAsDouble(0);
		} else {
			return getMatrix().columnAsDouble(column);
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#columnAsText(int)
	 */
	@Override
	public List<String> columnAsText(int column) {
		if (column < 0) {
			return getRowAnnotations(getRowAnnotationNames().get(getRowAnnotationNames().size() + column)).rowAsText(0);
		} else {
			return getMatrix().columnAsText(column);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#rowAsList(int)
	 */
	@Override
	public Object[] rowAsList(int column) {
		if (column < 0) {
			return getRowAnnotations(getRowAnnotationNames().get(getRowAnnotationNames().size() + column)).rowAsList(0);
		} else {
			return getMatrix().rowAsList(column);
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#rowAsDouble(int)
	 */
	@Override
	public double[] rowAsDouble(int column) {
		if (column < 0) {
			return getRowAnnotations(getRowAnnotationNames().get(getRowAnnotationNames().size() + column)).rowAsDouble(0);
		} else {
			return getMatrix().rowAsDouble(column);
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#rowAsText(int)
	 */
	@Override
	public List<String> rowAsText(int column) {
		if (column < 0) {
			return getRowAnnotations(getRowAnnotationNames().get(getRowAnnotationNames().size() + column)).rowAsText(0);
		} else {
			return getMatrix().rowAsText(column);
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#extractText()
	 */
	public DataFrame extractText() {
		Matrix innerM = getMatrix();
		
		int cn = innerM.getColumnCount();
		
		List<Integer> columns = new ArrayList<Integer>(cn);

		int rn = innerM.getRowCount();
		
		List<Integer> rows = new ArrayList<Integer>(rn);
		
		Matrix m = Matrix.extractText(innerM, rows, columns);
		
		if (columns.size() == 0 || rows.size() == 0) {
			return null;
		}
		
		DataFrame ret = DataFrame.createDataFrame(m);
		
		copyAnnotations(rows, columns, this, ret);
		
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.DataFrame#extractNumbers()
	 */
	public DataFrame extractNumbers() {
		Matrix innerM = getMatrix();
		
		int cn = innerM.getColumnCount();
		
		List<Integer> columns = new ArrayList<Integer>(cn);
		
		int rn = innerM.getRowCount();
		
		List<Integer> rows = new ArrayList<Integer>(rn);
		
		Matrix m = Matrix.extractNumbers(innerM, rows, columns);
		
		if (columns.size() == 0 || rows.size() == 0) {
			return null;
		}
		
		DataFrame ret = DataFrame.createDataFrame(m);
		
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
	public static DataFrame copyInnerColumns(final DataFrame m, 
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
	public static DataFrame copyInnerColumns(final DataFrame m, 
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
	public static DataFrame copyColumns(final DataFrame m, 
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
	public static <T extends MatrixGroup> DataFrame copyInnerColumns(final DataFrame m, 
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
	public static DataFrame copyColumns(final DataFrame m, 
			final List<Integer> columns) {
		Matrix innerM = m.getMatrix();

		Matrix newInnerM = ofSameType(m, m.getRowCount(), columns.size());

		copyColumns(innerM, newInnerM, columns);

		DataFrame ret = new DataFrame(newInnerM);

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
	public static DataFrame copyInnerColumns(final DataFrame m, 
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
	public static <V extends Comparable<? super V>> DataFrame copyInnerColumnsIndexed(DataFrame m, 
			List<Indexed<Integer, V>> columns) {
		Matrix innerM = m.getMatrix();

		Matrix newInnerM = ofSameType(m, m.getRowCount(), columns.size());

		copyColumnsIndexed(innerM, newInnerM, columns);

		DataFrame ret = new DataFrame(newInnerM);

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
	public static DataFrame copyInnerRows(DataFrame m, int... rows) {
		return copyRows(m, CollectionUtils.toList(rows));
	}

	/**
	 * Copy inner rows.
	 *
	 * @param m the m
	 * @param iter the iter
	 * @return the annotation matrix
	 */
	public static DataFrame copyInnerRows(DataFrame m, Iterable<? extends MatrixGroup> iter) {
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
	public static <T extends MatrixGroup> DataFrame copyInnerRows(DataFrame m, T g) {
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
	public static DataFrame copyRows(DataFrame m, 
			List<Integer> rows) {
		Matrix innerM = m.getMatrix();

		Matrix newInnerM = ofSameType(m, rows.size(), m.getColumnCount());

		copyRows(innerM, newInnerM, rows);

		DataFrame ret = new DataFrame(newInnerM);

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
	public static <V extends Comparable<? super V>> DataFrame copyInnerRowsIndexed(DataFrame m, 
			List<Indexed<Integer, V>> rows) {
		Matrix innerM = m.getMatrix();

		Matrix newInnerM = ofSameType(m, rows.size(), m.getColumnCount());

		copyRowsIndexed(innerM, newInnerM, rows);

		DataFrame ret = new DataFrame(newInnerM);

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
	public static DataFrame createDataFrame(int rows, int columns) {
		return createMixedMatrix(rows, columns);
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
	public static DataFrame createDataFrame(final DataFrame m) {
		Matrix im = m.getMatrix();

		DataFrame ret = createDataFrame(im.getRowCount(), im.getColumnCount());

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
	public static DataFrame createMixedMatrix(int rows, 
			int columns) {
		return new DataFrame(new MixedMatrix(rows, columns));
	}
	
	/**
	 * Creates an annotation matrix from an underlying numerical matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @return the annotation matrix
	 */
	public static DataFrame createNumericalMatrix(int rows, int columns) {
		return new DataFrame(new DoubleMatrix(rows, columns));
	}

	/**
	 * Creates the numerical matrix.
	 *
	 * @param m the m
	 * @return the annotation matrix
	 */
	public static DataFrame createNumericalMatrix(DataFrame m) {
		DataFrame ret = createNumericalMatrix(m.getRowCount(), 
				m.getColumnCount());

		copyAnnotations(m, ret);

		return ret;
	}
	
	public static DataFrame createDoubleMatrix(int rows, int columns) {
		return new DataFrame(new DoubleMatrix(rows, columns));
	}

	/**
	 * Creates the numerical matrix.
	 *
	 * @param m the m
	 * @return the annotation matrix
	 */
	public static DataFrame createDoubleMatrix(DataFrame m) {
		DataFrame ret = createDoubleMatrix(m.getRowCount(), m.getColumnCount());

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
	public static DataFrame createTextMatrix(int rows, int columns) {
		return new DataFrame(new TextMatrix(rows, columns));
	}

	/**
	 * Creates the text matrix.
	 *
	 * @param m the m
	 * @return the annotation matrix
	 */
	public static DataFrame createTextMatrix(DataFrame m) {
		DataFrame ret = createTextMatrix(m.getRowCount(), m.getColumnCount());

		copyAnnotations(m, ret);

		return ret;
	}
	
	/**
	 * Creates the dynamic matrix.
	 *
	 * @return the annotation matrix
	 */
	public static DataFrame createDynamicMatrix() {
		return createDataFrame(new DynamicMixedMatrix());
	}

	/**
	 * Creates the dynamic matrix.
	 *
	 * @param m the m
	 * @return the annotation matrix
	 */
	public static DataFrame createDynamicMatrix(DataFrame m) {
		DataFrame ret = createDynamicMatrix();

		copyAnnotations(m, ret);

		return ret;
	}
	
	/**
	 * Create an annotatable matrix from a matrix.
	 *
	 * @param m the m
	 * @return the annotation matrix
	 */
	public static DataFrame createDataFrame(Matrix m) {
		return new DataFrame(m);
	}
	
	/**
	 * Creates the annotatable matrix from columns.
	 *
	 * @param m the m
	 * @param columns the columns
	 * @return the annotation matrix
	 */
	public static DataFrame createAnnotatableMatrixFromCols(DataFrame m, 
			List<Integer> columns) {
		DataFrame ret = createDataFrame(m.getRowCount(), columns.size());
		
		DataFrame.copyRowAnnotations(m, ret);
		
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
	public static DataFrame createAnnotatableMatrixFromRows(DataFrame m, 
			List<Integer> rows) {
		DataFrame ret = createDataFrame(rows.size(), m.getColumnCount());
		
		DataFrame.copyColumnAnnotations(m, ret);
		
		for (int i = 0; i < rows.size(); ++i) {
			ret.copyRow(m, rows.get(i), i);
		}
		
		return ret;
	}
	
	

	

	//
	// Static methods
	//

	/**
	 * Write a simple expression matrix in GCT format.
	 *
	 * @param matrix the matrix
	 * @param file the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeEstMatrixV1(DataFrame matrix, Path file) throws IOException {
		BufferedWriter writer = FileUtils.newBufferedWriter(file);

		try {
			writer.write(EST_VERSION_1);
			writer.newLine();

			writer.write(EST_ROWS);
			writer.write(TextUtils.TAB_DELIMITER);
			writer.write(Integer.toString(matrix.getRowCount()));
			writer.newLine();

			writer.write(EST_COLUMNS);
			writer.write(TextUtils.TAB_DELIMITER);
			writer.write(Integer.toString(matrix.getColumnCount()));
			writer.newLine();

			List<String> groups = new ArrayList<String>();

			//for (Group group : matrix.getColumnGroups()) {
			//	groups.add(group.toString());
			//}

			writer.write(EST_ANNOTATION_GROUPS);
			writer.write(TextUtils.TAB_DELIMITER);
			writer.write(Integer.toString(groups.size()));

			if (groups.size() > 0) {	
				writer.write(TextUtils.TAB_DELIMITER);
				writer.write(Join.onTab(groups));
			}

			writer.newLine();

			List<String> rowAnnotationNames = 
					CollectionUtils.head(matrix.getRowAnnotationNames(), 1);

			writer.write(EST_ANNOTATION_ROWS);

			writer.write(TextUtils.TAB_DELIMITER);

			// Since the first annotation is the row name and we are already
			// writing that, ignore the first row annotation
			writer.write(Integer.toString(rowAnnotationNames.size()));

			if (rowAnnotationNames.size() > 0) {	
				writer.write(TextUtils.TAB_DELIMITER);
				writer.write(Join.onTab(rowAnnotationNames));
			}

			writer.newLine();

			List<String> columnAnnotationNames = 
					CollectionUtils.head(matrix.getColumnAnnotationNames(), 1);

			writer.write(EST_ANNOTATION_COLUMNS);

			writer.write(TextUtils.TAB_DELIMITER);
			writer.write(Integer.toString(columnAnnotationNames.size()));

			if (columnAnnotationNames.size() > 0) {
				writer.write(TextUtils.TAB_DELIMITER);
				writer.write(Join.onTab(columnAnnotationNames));
			}

			writer.newLine();

			// column names

			// The first annotation row is the Id and column names
			writer.write(ROW_NAMES);
			writer.write(TextUtils.TAB_DELIMITER);

			if (rowAnnotationNames.size() > 1) {
				writer.write(Join.onTab(rowAnnotationNames));
				writer.write(TextUtils.TAB_DELIMITER);
			}

			writer.write(Join.onTab(matrix.getColumnNames()));
			writer.newLine();

			for (String name : columnAnnotationNames) {
				writer.write(name);
				writer.write(TextUtils.repeat(TextUtils.TAB_DELIMITER, rowAnnotationNames.size() + 1));

				writer.write(Join.onTab(matrix.getColumnAnnotationText(name)));
				writer.newLine();
			}

			for (int i = 0; i < matrix.getRowCount(); ++i) {
				writer.write(matrix.getRowName(i));

				for (String name : rowAnnotationNames) {
					writer.write(TextUtils.TAB_DELIMITER);
					writer.write(matrix.getRowAnnotation(name, i).toString());
				}

				for (int j = 0; j < matrix.getColumnCount(); ++j) {
					writer.write(TextUtils.TAB_DELIMITER);
					writer.write(formatValue(matrix.getValue(i, j)));
				}

				writer.newLine();
			}
		} finally {
			writer.close();
		}
	}

	/**
	 * Write a simple expression matrix in GCT format.
	 *
	 * @param matrix the matrix
	 * @param file the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeEstMatrixV2(DataFrame matrix, Path file) throws IOException {
		BufferedWriter writer = FileUtils.newBufferedWriter(file);

		try {
			writer.write(EST_VERSION_2);
			writer.newLine();

			writer.write(EST_ROWS);
			writer.write(TextUtils.TAB_DELIMITER);
			writer.write(Integer.toString(matrix.getRowCount()));
			writer.newLine();

			writer.write(EST_COLUMNS);
			writer.write(TextUtils.TAB_DELIMITER);
			writer.write(Integer.toString(matrix.getColumnCount()));
			writer.newLine();

			List<String> groups = new ArrayList<String>();

			//for (Group group : matrix.getColumnGroups()) {
			//	groups.add(group.toString());
			//}

			writer.write(EST_ANNOTATION_GROUPS);
			writer.write(TextUtils.TAB_DELIMITER);
			writer.write(Integer.toString(groups.size()));

			if (groups.size() > 0) {	
				writer.write(TextUtils.TAB_DELIMITER);
				writer.write(Join.onTab(groups));
			}

			writer.newLine();

			//
			// Write out the row annotations
			//

			writer.write(EST_ANNOTATION_ROWS);

			writer.write(TextUtils.TAB_DELIMITER);
			writer.write(Integer.toString(matrix.getRowAnnotationNames().size()));

			if (matrix.getRowAnnotationNames().size() > 0) {	
				writer.write(TextUtils.TAB_DELIMITER);
				writer.write(Join.onTab(matrix.getRowAnnotationNames()));
			}

			writer.newLine();

			for (String name : matrix.getRowAnnotationNames()) {
				writer.write(EST_ANNOTATION_ROW);
				writer.write(TextUtils.TAB_DELIMITER);
				writer.write(name);
				writer.write(TextUtils.TAB_DELIMITER);
				writer.write(Join.onTab().values(matrix.getRowAnnotationText(name)).toString());
				writer.newLine();
			}

			//
			// Write out the column annotations
			//

			writer.write(EST_ANNOTATION_COLUMNS);

			writer.write(TextUtils.TAB_DELIMITER);
			writer.write(Integer.toString(matrix.getColumnAnnotationNames().size()));

			if (matrix.getColumnAnnotationNames().size() > 0) {
				writer.write(TextUtils.TAB_DELIMITER);
				writer.write(Join.onTab().values(matrix.getColumnAnnotationNames()).toString());
			}

			writer.newLine();


			for (String name : matrix.getColumnAnnotationNames()) {
				writer.write(EST_ANNOTATION_COLUMN);
				writer.write(TextUtils.TAB_DELIMITER);
				writer.write(name);
				writer.write(TextUtils.TAB_DELIMITER);
				writer.write(Join.onTab().values(matrix.getColumnAnnotationText(name)).toString());
				writer.newLine();
			}

			//
			// Write out the data
			//

			writer.write(EST_MATRIX);
			writer.newLine();

			for (int i = 0; i < matrix.getRowCount(); ++i) {
				for (int j = 0; j < matrix.getColumnCount(); ++j) {
					writer.write(formatValue(matrix.getValue(i, j)));

					if (j < matrix.getColumnCount() - 1) {
						writer.write(TextUtils.TAB_DELIMITER);
					}
				}

				writer.newLine();
			}
		} finally {
			writer.close();
		}
	}





	/**
	 * Write a simple expression matrix in tab delimited text format.
	 *
	 * @param <T> the generic type
	 * @param matrix the matrix
	 * @param file the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static <T> void writeDataFrame(DataFrame matrix, 
			Path file) throws IOException {
		BufferedWriter writer = FileUtils.newBufferedWriter(file);

		try {
			boolean hasHeader = false;

			if (matrix.getRowAnnotationNames() != null && 
					matrix.getRowAnnotationNames().size() > 0) {
				writer.write(Join.onTab(matrix.getRowAnnotationNames()));

				writer.write(TextUtils.TAB_DELIMITER);

				hasHeader = true;
			}

			if (matrix.getColumnNames() != null && 
					matrix.getColumnNames().size() > 0) {
				writer.write(Join.onTab(matrix.getColumnNames()));

				hasHeader = true;
			} else {
				// If there are row annotation names, then a header must be
				// present. If there are no column names, fill in row with
				// empty tabs
				if (hasHeader) {
					//pad with empty cells if there are not column name

					writer.write(TextUtils.emptyCells(matrix.getColumnCount()));
				}
			}

			if (hasHeader) {
				writer.newLine();
			}

			for (int i = 0; i < matrix.getRowCount(); ++i) {
				for (String name : matrix.getRowAnnotationNames()) {
					//System.err.println("aha " + name + " " + i);

					writer.write(matrix.getRowAnnotationText(name, i));
					writer.write(TextUtils.TAB_DELIMITER);
				}

				for (int j = 0; j < matrix.getColumnCount(); ++j) {

					writer.write(formatValue(matrix.getText(i, j)));

					if (j < matrix.getColumnCount() - 1) {
						writer.write(TextUtils.TAB_DELIMITER);
					}
				}

				writer.newLine();
			}
		} finally {
			writer.close();
		}
	}

	/**
	 * Format value.
	 *
	 * @param <T> the generic type
	 * @param value the value
	 * @return the string
	 */
	public static <T> String formatValue(T value) {
		if (value != null) {
			return value.toString();
		} else {
			return TextUtils.EMPTY_STRING;
		}
	}



	/**
	 * Search a matrix row annotations and return matching rows.
	 * This method is case insensitive.
	 *
	 * @param matrix the matrix
	 * @param text the text
	 * @return the list
	 */
	public static List<Integer> findRows(DataFrame matrix,
			String text) {
		List<Integer> ret = new ArrayList<Integer>();

		String ls = text.toLowerCase();

		for (int i = 0; i < matrix.getRowCount(); ++i) {
			for (String name : matrix.getRowAnnotationNames()) {
				if (matrix.getRowAnnotationText(name, i).toLowerCase().contains(ls)) {
					ret.add(i);
					break;
				}
			}
		}

		return ret;
	}

	/**
	 * Match rows.
	 *
	 * @param m the m
	 * @param rowAnnotation the row annotation
	 * @param regex the regex
	 * @return the list
	 */
	public static List<Integer> matchRows(DataFrame m,
			String rowAnnotation,
			String regex) {
		return matchRows(m, rowAnnotation, regex, true);
	}

	/**
	 * Match rows.
	 *
	 * @param m the m
	 * @param rowAnnotation the row annotation
	 * @param regex the regex
	 * @return the list
	 */
	public static List<Integer> matchRows(DataFrame m,
			String rowAnnotation,
			Pattern regex) {
		return matchRows(m, rowAnnotation, regex, true);
	}

	/**
	 * Matches row annotation by regex and returns the indices of
	 * rows.
	 *
	 * @param m the m
	 * @param rowAnnotation the row annotation
	 * @param regex the regex
	 * @param keep the keep
	 * @return the list
	 */
	public static List<Integer> matchRows(DataFrame m,
			String rowAnnotation,
			String regex,
			boolean keep) {
		return matchRows(m, rowAnnotation, Pattern.compile(regex), keep);
	}

	/**
	 * Matches row annotation by regex and returns the indices of
	 * rows.
	 *
	 * @param m the m
	 * @param rowAnnotation the row annotation
	 * @param regex the regex
	 * @param keep the keep
	 * @return the list
	 */
	public static List<Integer> matchRows(DataFrame m,
			String rowAnnotation,
			Pattern regex,
			boolean keep) {
		List<Integer> ret = new ArrayList<Integer>();

		List<String> annotations = 
				m.getRowAnnotationText(rowAnnotation);

		if (keep) {
			for (int i = 0; i < m.getRowCount(); ++i) {
				if (regex.matcher(annotations.get(i)).matches()) {
					ret.add(i);
				}
			}
		} else {
			for (int i = 0; i < m.getRowCount(); ++i) {
				if (!regex.matcher(annotations.get(i)).matches()) {
					ret.add(i);
				}
			}
		}

		return ret;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#copyRow(org.abh.common.math.matrix.Matrix, int, int)
	 */
	@Override
	public void copyRow(final Matrix from, 
			int row,
			int toRow) {
		getMatrix().copyRow(from, row, toRow);
	}

	/**
	 * Copy a row from one matrix to another allowing the row from the source
	 * to be different from the target.
	 *
	 * @param from the from
	 * @param row the row
	 * @param toRow the to row
	 */
	public void copyRow(final DataFrame from, 
			int row,
			int toRow) {

		if (row >= 0) {
			getMatrix().copyRow(from.getMatrix(), row, toRow);
			copyRowAnnotation(from, row, this, toRow);
		} else {
			//co >= 0
			// We are copying the row annotation of the from matrix to a
			// column in the to matrix
			Object[] values = 
					from.getColumnAnnotations(row).rowAsList(0);

			//to.setRowAnnotation(from.getColumnAnnotationNames().get(0), row, );
			getMatrix().setRow(toRow, values);
		}
	}

	/**
	 * Copy rows.
	 *
	 * @param from the from
	 * @param row the row
	 * @param toRow the to row
	 */
	public void copyRows(final DataFrame from, 
			int row,
			int toRow) {

		copyRows(from, row, toRow, 0);
	}

	/**
	 * Copy rows.
	 *
	 * @param from the from
	 * @param row the row
	 * @param toRow the to row
	 * @param toOffset the to offset
	 */
	public void copyRows(final DataFrame from, 
			int row,
			int toRow,
			int toOffset) {

		for (int i = row; i <= toRow; ++i) {
			copyRow(from, i, i + toOffset);
		}
	}

	/**
	 * Copy row annotation.
	 *
	 * @param from the from
	 * @param to the to
	 * @param row the row
	 */
	public static void copyRowAnnotation(final DataFrame from, 
			DataFrame to,
			int row) {
		copyRowAnnotation(from, row, to, row);
	}

	/**
	 * Copy row annotation.
	 *
	 * @param from the from
	 * @param fromRow the from row
	 * @param to the to
	 * @param toRow the to row
	 */
	public static void copyRowAnnotation(final DataFrame from,
			int fromRow,
			DataFrame to,
			int toRow) {
		for (String name : from.getRowAnnotationNames()) {
			to.setRowAnnotation(name, toRow, from.getRowAnnotation(name, fromRow));
		}
	}

	/**
	 * Copy row annotations.
	 *
	 * @param from the from
	 * @param to the to
	 */
	public static void copyRowAnnotations(final DataFrame from, 
			DataFrame to) {
		for (String name : from.getRowAnnotationNames()) {
			copyRowAnnotations(from, name, to);
		}
	}

	/**
	 * Copy row annotations.
	 *
	 * @param from the from
	 * @param name the name
	 * @param to the to
	 */
	public static void copyRowAnnotations(final DataFrame from,
			String name,
			DataFrame to) {
		to.setRowAnnotations(name, from.getRowAnnotations(name));
	}

	/**
	 * Copy row annotations.
	 *
	 * @param from the from
	 * @param to the to
	 * @param rows the rows
	 */
	public static void copyRowAnnotations(final DataFrame from, 
			DataFrame to,
			int... rows) {
		for (String name : from.getRowAnnotationNames()) {
			to.setRowAnnotations(name, from.getRowAnnotations(name));
		}
	}

	/**
	 * Copy row annotations.
	 *
	 * @param from the from
	 * @param to the to
	 * @param rows the rows
	 */
	public static void copyRowAnnotations(final DataFrame from, 
			DataFrame to,
			Collection<Integer> rows) {
		for (String name : from.getRowAnnotationNames()) {

			switch (from.getRowAnnotations(name).getType()) {
			case NUMBER:
				double[] annotations = 
				from.getRowAnnotations(name).rowAsDouble(0);

				double[] subAnnotations = 
						CollectionUtils.subList(annotations, rows);

				to.setNumRowAnnotations(name, subAnnotations);
				break;
			default:
				List<String> l = 
				from.getRowAnnotations(name).rowAsText(0);

				List<String> ls = 
						CollectionUtils.subList(l, rows);

				to.setTextRowAnnotations(name, ls);
				break;
			}

			/*
			List<Object> annotations = from.getRowAnnotations(name).rowAsList(0);

			List<Object> subAnnotations = 
					CollectionUtils.subList(annotations, rows);

			to.setRowAnnotations(name, subAnnotations);
			 */
		}
	}

	/**
	 * Copy row annotations indexed.
	 *
	 * @param <V> the value type
	 * @param from the from
	 * @param to the to
	 * @param rows the rows
	 */
	public static <V extends Comparable<? super V>> void copyRowAnnotationsIndexed(final DataFrame from, 
			DataFrame to,
			List<Indexed<Integer, V>> rows) {
		for (String name : from.getRowAnnotationNames()) {
			switch (from.getRowAnnotations(name).getType()) {
			case NUMBER:
				double[] annotations = 
				from.getRowAnnotations(name).rowAsDouble(0);

				double[] subAnnotations = 
						CollectionUtils.subListIndexed(annotations, rows);

				to.setNumRowAnnotations(name, subAnnotations);
				break;
			default:
				List<String> l = 
				from.getRowAnnotations(name).rowAsText(0);

				List<String> ls = 
						CollectionUtils.subListIndexed(l, rows);

				to.setTextRowAnnotations(name, ls);
				break;
			}

			/*
			List<Object> annotations = 
					from.getRowAnnotations(name).rowAsList(0);

			List<Object> subAnnotations = 
					CollectionUtils.subListIndexed(annotations, rows);

			to.setRowAnnotations(name, subAnnotations);
			 */
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#copyColumn(org.abh.common.math.matrix.Matrix, int, int)
	 */
	@Override
	public void copyColumn(final Matrix from, 
			int column,
			int toColumn) {
		if (from instanceof DataFrame) {
			copyColumnAnnotation((DataFrame)from, column, this, toColumn);
		}

		super.copyColumn(from, column, toColumn); //getInnerMatrix().copyColumn(from, column, toColumn);
	}

	/**
	 * Copy column.
	 *
	 * @param from the from
	 * @param column the column
	 */
	public void copyColumn(final DataFrame from, int column) {
		copyColumn(from, column, column);
	}

	/**
	 * Copy the column from one matrix to another.
	 *
	 * @param from the from
	 * @param column the column
	 * @param toColumn the offset
	 */
	public void copyColumn(final DataFrame from, 
			int column,
			int toColumn) {

		if (column >= 0) {
			copyColumnAnnotation(from, column, this, toColumn);

			super.copyColumn(from, column, toColumn);
		} else {
			//co >= 0
			// We are copying the row annotation of the from matrix to a
			// column in the to matrix
			Object[] values = from.getRowAnnotations(column).rowAsList(0);

			setColumnName(toColumn, from.getRowAnnotationName(column));
			setColumn(toColumn, values);
		}
	}

	/**
	 * Copy column annotation.
	 *
	 * @param from the from
	 * @param column the column
	 * @param to the to
	 * @param offset the offset
	 */
	public static void copyColumnAnnotation(final DataFrame from,
			int column,
			DataFrame to,
			int offset) {

		for (String name : from.getColumnAnnotationNames()) {
			to.setColumnAnnotation(name, 
					offset, 
					from.getColumnAnnotation(name, column));
		}
	}

	/**
	 * Copy column annotations.
	 *
	 * @param from the from
	 * @param to the to
	 */
	public static void copyColumnAnnotations(final DataFrame from, 
			DataFrame to) {
		for (String name : from.getColumnAnnotationNames()) {
			to.setColumnAnnotations(name, from.getColumnAnnotations(name));
		}
	}

	/**
	 * Copy column annotations.
	 *
	 * @param from the from
	 * @param to the to
	 * @param columns the columns
	 */
	public static void copyColumnAnnotations(final DataFrame from, 
			DataFrame to,
			Collection<Integer> columns) {

		for (String name : from.getColumnAnnotationNames()) {
			//List<Object> annotations = from.getColumnAnnotations(name).rowAsList(0);
			//List<Object> subAnnotations = CollectionUtils.subList(annotations, columns);
			//to.setColumnAnnotations(name, subAnnotations);

			switch (from.getColumnAnnotations(name).getType()) {
			case NUMBER:
				double[] annotations = 
				from.getColumnAnnotations(name).rowAsDouble(0);

				double[] subAnnotations = 
						CollectionUtils.subList(annotations, columns);

				to.setNumColumnAnnotations(name, subAnnotations);
				break;
			default:
				List<String> l = 
				from.getColumnAnnotations(name).rowAsText(0);

				List<String> ls = 
						CollectionUtils.subList(l, columns);

				to.setTextColumnAnnotations(name, ls);
				break;
			}


		}
	}

	/**
	 * Copy column annotations.
	 *
	 * @param from the from
	 * @param fromStart the from start
	 * @param fromEnd the from end
	 * @param to the to
	 * @param toStart the to start
	 */
	public static void copyColumnAnnotations(final DataFrame from,
			int fromStart,
			int fromEnd,
			DataFrame to,
			int toStart) {
		for (String name : from.getColumnAnnotationNames()) {
			Object[] annotations = from.getColumnAnnotations(name).rowAsList(0);

			Object[] subAnnotations = 
					CollectionUtils.subList(annotations, fromStart, fromEnd - fromStart + 1);

			int s = toStart;

			for (Object m : subAnnotations) {
				to.setColumnAnnotation(name, s++, m);
			}
		}
	}

	/**
	 * Copy column annotations.
	 *
	 * @param from the from
	 * @param to the to
	 * @param toStart the to start
	 */
	public static void copyColumnAnnotations(final DataFrame from,
			DataFrame to,
			int toStart) {
		for (String name : from.getColumnAnnotationNames()) {
			Object[] annotations = 
					from.getColumnAnnotations(name).rowAsList(0);

			int s = toStart;

			for (Object m : annotations) {
				to.setColumnAnnotation(name, s++, m);
			}
		}
	}

	/**
	 * Copy the row and column annotations from matrix to another.
	 *
	 * @param from the from
	 * @param to the to
	 */
	public static void copyAnnotations(final DataFrame from, 
			DataFrame to) {
		to.setName(from.getName());

		copyRowAnnotations(from, to);
		copyColumnAnnotations(from, to);
	}

	/**
	 * Copy column annotations indexed.
	 *
	 * @param <V> the value type
	 * @param from the from
	 * @param to the to
	 * @param columns the columns
	 */
	public static <V extends Comparable<? super V>> void copyColumnAnnotationsIndexed(final DataFrame from, 
			DataFrame to,
			List<Indexed<Integer, V>> columns) {
		for (String name : from.getColumnAnnotationNames()) {
			//List<Object> annotations = 
			//		from.getColumnAnnotations(name).rowAsList(0);
			//List<Object> subAnnotations = 
			//		CollectionUtils.subListIndexed(annotations, columns);
			//to.setColumnAnnotations(name, subAnnotations);

			switch (from.getColumnAnnotations(name).getType()) {
			case NUMBER:
				double[] annotations = 
				from.getColumnAnnotations(name).rowAsDouble(0);

				double[] subAnnotations = 
						CollectionUtils.subListIndexed(annotations, columns);

				to.setNumColumnAnnotations(name, subAnnotations);
				break;
			default:
				List<String> l = 
				from.getColumnAnnotations(name).rowAsText(0);

				List<String> ls = 
						CollectionUtils.subListIndexed(l, columns);

				to.setTextColumnAnnotations(name, ls);
				break;
			}
		}
	}

	/**
	 * Set the row annotation of multiple rows.
	 *
	 * @param m the m
	 * @param name the name
	 * @param rows the rows
	 * @param value the value
	 */
	public static void setAnnotation(DataFrame m,
			String name, 
			List<Integer> rows,
			double value) {
		for (int i : rows) {
			m.setRowAnnotation(name, i, value);
		}
	}

	/**
	 * Sets the annotation.
	 *
	 * @param m the m
	 * @param name the name
	 * @param rows the rows
	 * @param value the value
	 */
	public static void setAnnotation(DataFrame m,
			String name, 
			List<Integer> rows,
			String value) {
		for (int i : rows) {
			m.setRowAnnotation(name, i, value);
		}
	}

	/**
	 * Sets the annotation.
	 *
	 * @param m the m
	 * @param name the name
	 * @param rows the rows
	 * @param value the value
	 */
	public static void setAnnotation(DataFrame m,
			String name, 
			List<Integer> rows,
			MatrixCell value) {
		for (int i : rows) {
			m.setRowAnnotation(name, i, value);
		}
	}

	/**
	 * Copy columns.
	 *
	 * @param from the from
	 * @param fromColstart the from column start
	 * @param fromColumnEnd the from column end
	 * @param to the to
	 */
	public static void copyColumns(DataFrame from,
			int fromColstart,
			int fromColumnEnd,
			DataFrame to) {
		copyColumns(from,
				fromColstart,
				fromColumnEnd,
				to,
				0);
	}

	/**
	 * Copy columns.
	 *
	 * @param from the from
	 * @param to the to
	 * @param toColOffset the to col offset
	 */
	public static void copyColumns(DataFrame from,
			DataFrame to,
			int toColOffset) {
		copyColumns(from,
				0,
				to,
				toColOffset);
	}

	/**
	 * Copy columns from one matrix starting at a given offset to the
	 * beginning of the to matrix.
	 *
	 * @param from the from
	 * @param fromColstart the from column start
	 * @param to the to
	 */
	public static void copyColumns(DataFrame from,
			int fromColstart,
			DataFrame to) {
		copyColumns(from,
				fromColstart,
				to,
				0);
	}

	/**
	 * Copy a range of columns from one matrix to another with the
	 * ability to offset the copy in the to matrix.
	 *
	 * @param from the from
	 * @param fromColstart the from column start
	 * @param to the to
	 * @param toColOffset the to col offset
	 */
	public static void copyColumns(DataFrame from,
			int fromColstart,
			DataFrame to,
			int toColOffset) {
		copyColumns(from,
				fromColstart,
				from.getColumnCount() - 1,
				to,
				toColOffset);
	}

	/**
	 * Copy columns.
	 *
	 * @param from 	The from matrix.
	 * @param fromColStart 	The from column start column
	 * @param fromColumnEnd the from column end
	 * @param to the to
	 * @param toColOffset the to col offset
	 */
	public static void copyColumns(DataFrame from,
			int fromColStart,
			int fromColumnEnd,
			DataFrame to,
			int toColOffset) {
		int cols = fromColumnEnd - fromColStart + 1;

		for (int j = 0; j < cols; ++j) {
			to.copyColumn(from, j + fromColStart, j + toColOffset);
		}
	}

	/**
	 * Copy columns.
	 *
	 * @param m the m
	 * @param iter the iter
	 * @param to the to
	 */
	public static void copyColumns(final DataFrame m,
			final Iterable<? extends MatrixGroup> iter,
			DataFrame to) {
		copyColumns(m, iter, to, 0);
	}

	/**
	 * Copy columns.
	 *
	 * @param m the m
	 * @param iter the iter
	 * @param to the to
	 * @param offset the offset
	 */
	public static void copyColumns(final DataFrame m,
			final Iterable<? extends MatrixGroup> iter,
			DataFrame to,
			int offset) {
		int c = offset;

		for (MatrixGroup g : iter) {
			for (int i : MatrixGroup.findColumnIndices(m, g)) {
				to.copyColumn(m, i, c);

				++c;
			}
		}
	}

	/**
	 * Copy columns.
	 *
	 * @param from the from
	 * @param indices the indices
	 * @param to the to
	 */
	public static void copyColumns(final DataFrame from,
			final Collection<Integer> indices,
			DataFrame to) {
		copyColumns(from, indices, to, 0);
	}

	/**
	 * Copy columns.
	 *
	 * @param from the from
	 * @param indices the indices
	 * @param to the to
	 * @param offset the offset
	 */
	public static void copyColumns(final DataFrame from,
			final Collection<Integer> indices,
			DataFrame to,
			int offset) {
		int c = offset;

		for (int i : indices) {
			to.copyColumn(from, i, c);

			++c;
		}
	}

	/**
	 * Copy.
	 *
	 * @param from the from
	 * @param to the to
	 */
	public static void copy(DataFrame from, DataFrame to) {
		copy(from, 0, 0, to, 0, 0);
	}

	/**
	 * Copy.
	 *
	 * @param from the from
	 * @param fromRowOffset the from row offset
	 * @param fromColOffset the from col offset
	 * @param to the to
	 * @param toRowOffset the to row offset
	 * @param toColOffset the to col offset
	 */
	public static void copy(DataFrame from,
			int fromRowOffset,
			int fromColOffset,
			DataFrame to,
			int toRowOffset,
			int toColOffset) {
		int fromRows = from.getRowCount() - fromRowOffset;
		int fromCols = from.getColumnCount() - fromColOffset;

		int toRows = to.getRowCount() - toRowOffset;
		int toCols = to.getColumnCount() - toColOffset;

		int rows = Math.min(fromRows, toRows);
		int cols = Math.min(fromCols, toCols);

		for (int j = 0; j < cols; ++j) {
			int cFrom = j + fromColOffset;
			int cTo = j + toColOffset;

			to.setColumnName(cTo, from.getColumnName(cFrom));
		}

		List<String> names = from.getRowAnnotationNames();

		for (int i = 0; i < rows; ++i) {
			int rFrom = i + fromRowOffset;
			int rTo = i + toRowOffset;

			for (String name : names) {
				to.setRowAnnotation(name, rTo, from.getRowAnnotation(name, rFrom));
			}
		}


		for (int i = 0; i < rows; ++i) {
			int rFrom = i + fromRowOffset;
			int rTo = i + toRowOffset;

			for (int j = 0; j < cols; ++j) {
				int cFrom = j + fromColOffset;
				int cTo = j + toColOffset;

				to.set(rTo, cTo, from.get(rFrom, cFrom));
			}
		}
	}

	/**
	 * Write row.
	 *
	 * @param m the m
	 * @param writer the writer
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeHeader(DataFrame m,
			BufferedWriter writer) throws IOException {
		if (m.getRowAnnotationNames().size() > 0) {
			writer.write(TextUtils.tabJoin(m.getRowAnnotationNames()));
			writer.write(TextUtils.TAB_DELIMITER);
		}

		writer.write(TextUtils.tabJoin(m.getColumnNames()));
		writer.newLine();
	}

	/**
	 * Write row.
	 *
	 * @param m the m
	 * @param row the row
	 * @param writer the writer
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeRow(DataFrame m, 
			int row,
			BufferedWriter writer) throws IOException {
		if (m.getRowAnnotationNames().size() > 0) {
			writer.write(TextUtils.tabJoin(m.getRowAnnotationText(row)));
			writer.write(TextUtils.TAB_DELIMITER);
		}

		writer.write(TextUtils.tabJoin(m.rowAsText(row)));
		writer.newLine();
	}

	/**
	 * Find the first column header containing text (case insensitive).
	 *
	 * @param m the m
	 * @param terms the terms
	 * @return the int
	 */
	public static int findColumn(DataFrame m, String... terms) {
		return TextUtils.findFirst(m.getColumnNames(), terms);
	}

	/**
	 * For each term, find the first column matching that term. Each term
	 * can be composed of multiple keyswords separated by pipe | if a column
	 * is expected to contain variations of a name.
	 *
	 * @param m the m
	 * @param terms the terms
	 * @return the map
	 */
	public static Map<String, Integer> findColumns(DataFrame m, 
			String... terms) {
		Map<String, Integer> indexMap = new HashMap<String, Integer>();

		for (String term : terms) {
			List<String> alts = Splitter.on('|').text(term);

			indexMap.put(term, TextUtils.findFirst(m.getColumnNames(), alts));
		}

		return indexMap;
	}

	/**
	 * Find.
	 *
	 * @param m the m
	 * @param text the text
	 * @param wholeCell the whole cell
	 * @param caseSensitive the case sensitive
	 * @return the matrix cell
	 */
	public static MatrixCellRef find(DataFrame m, 
			String text, 
			boolean wholeCell,
			boolean caseSensitive) {
		return find(m, text, wholeCell, caseSensitive, START_CELL);
	}

	/**
	 * Search for a string in a matrix and return the cell reference if
	 * found.
	 *
	 * @param m the m
	 * @param text the text
	 * @param wholeCell the whole cell
	 * @param caseSensitive the case sensitive
	 * @param startCell the start cell
	 * @return the matrix cell
	 */
	public static MatrixCellRef find(DataFrame m, 
			String text, 
			boolean wholeCell,
			boolean caseSensitive,
			MatrixCellRef startCell) {
		String t;

		int index;
		int columnIndex;

		Pattern pattern;

		if (wholeCell) {
			if (caseSensitive) {
				pattern = Pattern.compile("^" + text + "$");
			} else {
				pattern = Pattern.compile("^" + text + "$", Pattern.CASE_INSENSITIVE);
			}
		} else {
			if (caseSensitive) {
				pattern = Pattern.compile(text);
			} else {
				pattern = Pattern.compile(text, Pattern.CASE_INSENSITIVE);
			}
		}

		Matcher matcher = pattern.matcher(TextUtils.EMPTY_STRING);

		int r = -1;

		for (String name : m.getRowAnnotationNames()) {
			for (int i = 0; i < m.getRowCount(); ++i) {
				index = (i + startCell.row) % m.getRowCount();

				t = m.getRowAnnotationText(name, index);

				if (t != null) {
					matcher.reset(t);

					if (matcher.find()) {
						return new MatrixCellRef(index, r);
					}
				}
			}

			--r;
		}

		r = -1;

		for (String name : m.getColumnAnnotationNames()) {
			for (int i = 0; i < m.getColumnCount(); ++i) {
				index = (i + startCell.column) % m.getColumnCount();

				t = m.getColumnAnnotationText(name, index);

				if (t != null) {
					matcher.reset(t);

					if (matcher.find()) {
						return new MatrixCellRef(r, index);
					}
				}
			}

			--r;
		}

		for (int i = 0; i < m.getRowCount(); ++i) {
			index = (i + startCell.row) % m.getRowCount();

			for (int j = 0; j < m.getColumnCount(); ++j) {
				columnIndex = (j + startCell.column) % m.getColumnCount();

				t = m.getText(index, columnIndex);

				if (t != null) {
					matcher.reset(t);

					if (matcher.find()) {
						return new MatrixCellRef(index, columnIndex);
					}
				}
			}
		}

		return null;
	}

	/**
	 * Find all.
	 *
	 * @param m the m
	 * @param text the text
	 * @param wholeCell the whole cell
	 * @param caseSensitive the case sensitive
	 * @param startCell the start cell
	 * @return the list
	 */
	public static List<MatrixCellRef> findAll(DataFrame m, 
			String text, 
			boolean wholeCell,
			boolean caseSensitive,
			MatrixCellRef startCell) {
		String t;

		int index;
		int columnIndex;

		Pattern pattern;

		if (wholeCell) {
			if (caseSensitive) {
				pattern = Pattern.compile("^" + text + "$");
			} else {
				pattern = Pattern.compile("^" + text + "$", Pattern.CASE_INSENSITIVE);
			}
		} else {
			if (caseSensitive) {
				pattern = Pattern.compile(text);
			} else {
				pattern = Pattern.compile(text, Pattern.CASE_INSENSITIVE);
			}
		}

		List<MatrixCellRef> cells = new ArrayList<MatrixCellRef>();

		Matcher matcher = pattern.matcher(TextUtils.EMPTY_STRING);

		int r = -1;

		for (String name : m.getRowAnnotationNames()) {
			for (int i = 0; i < m.getRowCount(); ++i) {
				index = (i + startCell.row) % m.getRowCount();

				t = m.getRowAnnotationText(name, index);

				if (t != null) {
					matcher.reset(t);

					if (matcher.find()) {
						cells.add(new MatrixCellRef(index, r));
					}
				}
			}

			--r;
		}

		r = -1;

		for (String name : m.getColumnAnnotationNames()) {
			for (int i = 0; i < m.getColumnCount(); ++i) {
				index = (i + startCell.column) % m.getColumnCount();

				t = m.getColumnAnnotationText(name, index);

				if (t != null) {
					matcher.reset(t);

					if (matcher.find()) {
						cells.add(new MatrixCellRef(r, index));
					}
				}
			}

			--r;
		}

		for (int i = 0; i < m.getRowCount(); ++i) {
			index = (i + startCell.row) % m.getRowCount();

			for (int j = 0; j < m.getColumnCount(); ++j) {
				columnIndex = (j + startCell.column) % m.getColumnCount();

				t = m.getText(index, columnIndex);

				if (t != null) {
					matcher.reset(t);

					if (matcher.find()) {
						cells.add(new MatrixCellRef(index, columnIndex));
					}
				}
			}
		}

		return cells;
	}

	/**
	 * Parses the txt matrix.
	 *
	 * @param file the file
	 * @param hasHeader the has header
	 * @param rowAnnotations the row annotations
	 * @return the annotation matrix
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static DataFrame parseTxtMatrix(Path file,
			boolean hasHeader,
			int rowAnnotations) throws IOException {
		return parseTxtMatrix(file, 
				hasHeader, 
				TextUtils.EMPTY_LIST, 
				rowAnnotations, 
				TextUtils.TAB_DELIMITER);
	}

	/**
	 * Parses the txt matrix.
	 *
	 * @param file the file
	 * @param hasHeader the has header
	 * @param skipMatches the skip matches
	 * @param rowAnnotations the row annotations
	 * @param delimiter the delimiter
	 * @return the annotation matrix
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static DataFrame parseTxtMatrix(Path file,
			boolean hasHeader,
			List<String> skipMatches,
			int rowAnnotations, 
			String delimiter) throws IOException {
		if (hasHeader) {
			return new MixedMatrixParser(hasHeader, skipMatches, rowAnnotations, delimiter).parse(file);
		} else {
			return parseDynamicMatrix(file, skipMatches, rowAnnotations, delimiter);
		}
	}

	/**
	 * Parses the csv matrix.
	 *
	 * @param file the file
	 * @param hasHeader the has header
	 * @param skipMatches the skip matches
	 * @param rowAnnotations the row annotations
	 * @return the annotation matrix
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static DataFrame parseCsvMatrix(Path file, 
			boolean hasHeader, 
			List<String> skipMatches,
			int rowAnnotations) throws IOException {
		if (hasHeader) {
			return new CsvMatrixParser(hasHeader, rowAnnotations).parse(file);
		} else {
			return parseDynamicMatrix(file, skipMatches, rowAnnotations, TextUtils.COMMA_DELIMITER);
		}
	}

	/**
	 * Parses the dynamic matrix.
	 *
	 * @param file the file
	 * @param hasHeader the has header
	 * @param skipMatches the skip matches
	 * @param rowAnnotations the row annotations
	 * @param delimiter the delimiter
	 * @return the annotation matrix
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static DataFrame parseDynamicMatrix(Path file,
			List<String> skipMatches,
			int rowAnnotations, 
			String delimiter) throws IOException {
		return new DynamicMatrixParser(skipMatches, rowAnnotations, delimiter).parse(file);
	}



	/**
	 * Sets the column.
	 *
	 * @param <T> the generic type
	 * @param column the column
	 * @param value the value
	 * @param m the m
	 */
	public static <T> void setColumn(int column, T value, Matrix m) {
		for (int r = 0; r < m.getRowCount(); ++r) {
			m.set(r, column, value);
		}
	}

	/**
	 * Copy a number of rows from one matrix to another.
	 *
	 * @param m the m
	 * @param rows the rows
	 * @param ret the ret
	 */
	public static void copyRows(final DataFrame m, 
			final  List<Integer> rows, 
			DataFrame ret) {
		copyRows(m, rows, ret, 0);
	}

	/**
	 * Copy a number of rows from matrix to another where each row that is
	 * copied is copied consecutively into the new array.
	 *
	 * @param m the m
	 * @param rows the rows
	 * @param ret the ret
	 * @param offset the offset
	 */
	public static void copyRows(final DataFrame m, 
			final List<Integer> rows, 
			DataFrame ret,
			int offset) {

		for (int row : rows) {
			ret.copyRow(m, row, offset++);
		}
	}

	/**
	 * Loads the first.
	 *
	 * @param file the file
	 * @param skipHeader the skip header
	 * @return the list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static List<String> firstColAsList(Path file, boolean skipHeader) throws IOException {
		return colAsList(file, skipHeader, 0);
	}

	/**
	 * Parses a matrix file and extracts a column as a list.
	 *
	 * @param file the file
	 * @param skipHeader the skip header
	 * @param col the col
	 * @return the list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static List<String> colAsList(Path file, boolean skipHeader, int col) throws IOException {
		//LOG.info("Load list from {}, {}...", file, skipHeader);

		BufferedReader reader = FileUtils.newBufferedReader(file);

		String line;

		List<String> rows = new ArrayList<String>();

		Splitter splitter = Splitter.onTab();

		try {

			if (skipHeader) {
				reader.readLine();
			}

			while ((line = reader.readLine()) != null) {
				rows.add(splitter.text(line).get(col));
			}
		} finally {
			reader.close();
		}

		return rows;
	}

	/**
	 * Copy column names.
	 *
	 * @param from the from
	 * @param to the to
	 */
	public static void copyColumnNames(DataFrame from,
			DataFrame to) {
		copyColumnNames(from, to, 0);
	}

	/**
	 * Copy column names.
	 *
	 * @param from the from
	 * @param to the to
	 * @param offset the offset
	 */
	public static void copyColumnNames(DataFrame from,
			DataFrame to, 
			int offset) {
		int c = Math.min(from.getColumnCount(), to.getColumnCount());

		for (int i = 0; i < c; ++i) {
			to.setColumnName(i + offset, from.getColumnName(i));
		}
	}

	/**
	 * Returns a list of column names given a list of indices.
	 *
	 * @param mMatrix the m matrix
	 * @param columns the columns
	 * @return the list
	 */
	public static List<String> columnNames(DataFrame mMatrix, Collection<Integer> columns) {
		List<String> ret = new ArrayList<String>(columns.size());

		for (int c : columns) {
			ret.add(mMatrix.getColumnName(c));
		}

		return ret;
	}

	/**
	 * Row names.
	 *
	 * @param mMatrix the m matrix
	 * @param rows the rows
	 * @return the list
	 */
	public static List<String> rowNames(DataFrame mMatrix, Collection<Integer> rows) {
		List<String> ret = new ArrayList<String>(rows.size());

		for (int r : rows) {
			ret.add(mMatrix.getRowName(r));
		}

		return ret;
	}

	/**
	 * Copy annotations for lists of rows and columns.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @param m the m
	 * @param ret the ret
	 */
	public static void copyAnnotations(List<Integer> rows,
			List<Integer> columns,
			final DataFrame m,
			DataFrame ret) {

		for (int i = 0; i < columns.size(); ++i) {
			copyColumnAnnotation(m, columns.get(i), ret, i);
		}

		for (int i = 0; i < rows.size(); ++i) {
			copyRowAnnotation(m, rows.get(i), ret, i);
		}
	}

	/**
	 * Alt index modulo.
	 *
	 * @param i the i
	 * @param size the size
	 * @return the int
	 */
	public static int altIndexModulo(int i, int size) {
		return (size + i) % size;
	}
}
