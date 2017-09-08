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
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.text.Join;
import org.jebtk.core.text.Splitter;
import org.jebtk.core.text.TextUtils;

// TODO: Auto-generated Javadoc
/**
 * Provides annotation and row naming for a matrix but does
 * not provide a concrete implementation of data storage
 * since this can be either something conventional requiring n*m
 * elements or something more efficient such as upper
 * triangular form.
 * 
 * @author Antony Holmes Holmes
 */
public abstract class AnnotationMatrix extends Matrix implements NameProperty, MatrixAnnotations {

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
	 * Instantiates a new annotation matrix.
	 */
	public AnnotationMatrix() {
		super(-1, -1);
	}

	/**
	 * Optionally give the matrix a name.
	 *
	 * @param name the name
	 * @return the annotation matrix
	 */
	public AnnotationMatrix setName(String name) {
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
	 * Returns the matrix this annotation matrix encapsulates.
	 *
	 * @return the inner matrix
	 */
	public abstract Matrix getInnerMatrix();

	/**
	 * Equivalent to getInnerMatrix().getRowCount()
	 *
	 * @return the row count
	 */
	@Override
	public int getRowCount() {
		return getInnerMatrix().getRowCount();
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return getInnerMatrix().getColumnCount();
	}

	/**
	 * Gets the extended column count consisting of the column count +
	 * the number of row annotations.
	 *
	 * @return the ext column count
	 */
	public int getExtColumnCount() {
		return getInnerMatrix().getColumnCount() + 
				getRowAnnotationNames().size();
	}

	/**
	 * Gets the extended row count.
	 *
	 * @return the ext row count
	 */
	public int getExtRowCount() {
		return getInnerMatrix().getRowCount() + 
				getColumnAnnotationNames().size();
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
			getInnerMatrix().set(row, column, value);
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
			getInnerMatrix().set(row, column, value);
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
			return getInnerMatrix().getCellType(row, column);
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

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#getValue(int, int)
	 */
	@Override
	public double getValue(int row, int column) {
		if (row >= 0 && column >= 0) {
			return getInnerMatrix().getValue(row, column);
		} else if (row < 0 && column < 0) {
			return Matrix.NULL_NUMBER;
		} else if (row < 0) {
			int s = altIndexModulo(row, getColumnAnnotationNames().size());
			return getColumnAnnotations(s).getValue(0, column);
		} else {
			int s = altIndexModulo(column, getRowAnnotationNames().size());
			return getRowAnnotations(s).getValue(0, row);
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#getText(int, int)
	 */
	@Override
	public String getText(int row, int column) {
		if (row >= 0 && column >= 0) {
			return getInnerMatrix().getText(row, column);
		} else if (row < 0 && column < 0) {
			int s = altIndexModulo(column, getRowAnnotationNames().size());
			return getRowAnnotationNames().get(s);
		} else if (row < 0) {
			int s = altIndexModulo(row, getColumnAnnotationNames().size());
			return getColumnAnnotations(s).getText(0, column);
		} else {
			int s = altIndexModulo(column, getRowAnnotationNames().size());
			return getRowAnnotations(s).getText(0, row);
		}
	}

	/*
	public int translateRow(int row) {
		return getColumnAnnotationNames().size() - row; //row - getColumnAnnotationNames().size();
	}

	public int translateCol(int col) {
		return getRowAnnotationNames().size() - col; //col - getRowAnnotationNames().size();
	}

	public MatrixCellRef translate(int row, int col) {
		return new MatrixCellRef(translateRow(row), translateCol(col));
	}
	 */

	/**
	 * Sets the row annotation.
	 *
	 * @param name the name
	 * @param values the values
	 */
	//public abstract void setRowAnnotations(String name, 
	//		Collection<? extends Object> values);

	public abstract void setNumRowAnnotations(String name, 
			Collection<? extends Number> values);

	/**
	 * Sets the num row annotations.
	 *
	 * @param name the name
	 * @param values the values
	 */
	public abstract void setNumRowAnnotations(String name, 
			double[] values);
	
	/**
	 * Sets the num row annotations.
	 *
	 * @param name the name
	 * @param values the values
	 */
	public abstract void setNumRowAnnotations(String name, 
			int[] values);

	/**
	 * Sets the text row annotations.
	 *
	 * @param name the name
	 * @param values the values
	 */
	public abstract void setTextRowAnnotations(String name, 
			Collection<String> values);

	/**
	 * Sets the row annotations.
	 *
	 * @param name the name
	 * @param values the values
	 */
	public abstract void setRowAnnotations(String name, 
			Matrix values);

	/**
	 * Sets the row annotation.
	 *
	 * @param name the name
	 * @param row the row
	 * @param value the value
	 */
	public abstract void setRowAnnotation(String name, 
			int row, 
			String value);

	/**
	 * Sets the row annotation.
	 *
	 * @param name the name
	 * @param column the column
	 * @param value the value
	 */
	public abstract void setRowAnnotation(String name, 
			int column, 
			double value);

	/**
	 * Sets the row annotation.
	 *
	 * @param name the name
	 * @param column the column
	 * @param value the value
	 */
	public abstract void setRowAnnotation(String name, 
			int column, 
			Object value);


	/**
	 * Adds the column annotation.
	 *
	 * @param name the name
	 * @param values the values
	 */
	//public abstract void addColumnAnnotation(String name, List<? extends Object> values);

	/**
	 * Sets the column annotation.
	 *
	 * @param name the name
	 * @param values the values
	 */
	//public abstract void setColumnAnnotations(String name, 
	//		Collection<? extends Object> values);

	public abstract void setNumColumnAnnotations(String name, 
			Collection<? extends Number> values);
	
	/**
	 * Convert a double array to a column annotation.
	 *
	 * @param name the name
	 * @param values the values
	 */
	public abstract void setNumColumnAnnotations(String name, double[] values);
	
	/**
	 * Convert an int array to a column annotation.
	 *
	 * @param name the name
	 * @param values the values
	 */
	public abstract void setNumColumnAnnotations(String name, int[] values);

	/**
	 * Sets the text column annotations.
	 *
	 * @param name the name
	 * @param values the values
	 */
	public abstract void setTextColumnAnnotations(String name, 
			Collection<String> values);

	/**
	 * Sets the column annotations.
	 *
	 * @param name the name
	 * @param values the values
	 */
	public abstract void setColumnAnnotations(String name, 
			Matrix values);

	/**
	 * Sets the column annotation.
	 *
	 * @param name the name
	 * @param column the column
	 * @param value the value
	 */
	public abstract void setColumnAnnotation(String name, 
			int column, 
			String value);

	/**
	 * Sets the column annotation.
	 *
	 * @param name the name
	 * @param column the column
	 * @param value the value
	 */
	public abstract void setColumnAnnotation(String name, 
			int column, 
			double value);

	/**
	 * Sets the column annotation.
	 *
	 * @param name the name
	 * @param column the column
	 * @param value the value
	 */
	public abstract void setColumnAnnotation(String name, 
			int column, 
			Object value);

	//
	// Methods not dependent on implementation
	//

	/**
	 * Gets the row annotation name.
	 *
	 * @param i the i
	 * @return the row annotation name
	 */
	public String getRowAnnotationName(int i) {
		return getRowAnnotationNames().get(i);
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

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.MatrixAnnotations#getRowAnnotationNames()
	 */
	@Override
	public List<String> getRowAnnotationNames() {
		return Collections.emptyList();
	}

	/**
	 * Gets the row annotations.
	 *
	 * @param name the name
	 * @return the row annotations
	 */
	public abstract Matrix getRowAnnotations(String name);

	/**
	 * Gets the row annotations.
	 *
	 * @param i the i
	 * @return the row annotations
	 */
	public abstract Matrix getRowAnnotations(int i);

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

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.MatrixAnnotations#getColumnAnnotationNames()
	 */
	@Override
	public List<String> getColumnAnnotationNames() {
		return Collections.emptyList();
	}

	/**
	 * Gets the column annotation name.
	 *
	 * @param i the i
	 * @return the column annotation name
	 */
	public String getColumnAnnotationName(int i) {
		return getColumnAnnotationNames().get(i);
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

	/**
	 * Gets the column annotations.
	 *
	 * @param name the name
	 * @return the column annotations
	 */
	public abstract Matrix getColumnAnnotations(String name);

	/**
	 * Gets the column annotations.
	 *
	 * @param i the i
	 * @return the column annotations
	 */
	public abstract Matrix getColumnAnnotations(int i);


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

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#columnAsList(int)
	 */
	@Override
	public List<Object> columnAsList(int column) {
		if (column < 0) {
			return getRowAnnotations(getRowAnnotationNames().get(getRowAnnotationNames().size() + column)).columnAsList(0);
		} else {
			return getInnerMatrix().columnAsList(column);
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#columnAsDouble(int)
	 */
	@Override
	public double[] columnAsDouble(int column) {
		if (column < 0) {
			return getRowAnnotations(getRowAnnotationNames().get(getRowAnnotationNames().size() + column)).rowAsDouble(0);
		} else {
			return getInnerMatrix().columnAsDouble(column);
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#columnAsText(int)
	 */
	@Override
	public List<String> columnAsText(int column) {
		if (column < 0) {
			return getRowAnnotations(getRowAnnotationNames().get(getRowAnnotationNames().size() + column)).rowAsText(0);
		} else {
			return getInnerMatrix().columnAsText(column);
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#rowAsList(int)
	 */
	@Override
	public List<Object> rowAsList(int row) {
		if (row >= 0) {
			return getInnerMatrix().rowAsList(row);
		} else {
			return getColumnAnnotations(row).rowAsList(0);
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#rowAsDouble(int)
	 */
	@Override
	public double[] rowAsDouble(int row) {
		if (row >= 0) {
			return getInnerMatrix().rowAsDouble(row);
		} else {
			return getColumnAnnotations(row).rowAsDouble(0);
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#rowAsText(int)
	 */
	@Override
	public List<String> rowAsText(int row) {
		if (row >= 0) {
			return getInnerMatrix().rowAsText(row);
		} else {
			return getColumnAnnotations(row).rowAsText(0);
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#transpose()
	 */
	@Override
	public Matrix transpose() {
		// Transpose the main matrix
		Matrix innerM = getInnerMatrix().transpose();

		AnnotationMatrix ret = new AnnotatableMatrix(innerM);

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

	/**
	 * Returns a sub matrix containing just the text cells or null if
	 * the matrix contains no text.
	 *
	 * @return the annotation matrix
	 */
	public AnnotationMatrix extractText() {
		return null;
	}

	/**
	 * Returns a sub matrix containing just the number cells or null if
	 * the matrix contains no text.
	 *
	 * @return the annotation matrix
	 */
	public AnnotationMatrix extractNumbers() {
		return null;
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
	public static void writeEstMatrixV1(AnnotationMatrix matrix, Path file) throws IOException {
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
	public static void writeEstMatrixV2(AnnotationMatrix matrix, Path file) throws IOException {
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
	public static <T> void writeAnnotationMatrix(AnnotationMatrix matrix, 
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
	public static List<Integer> findRows(AnnotationMatrix matrix,
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
	public static List<Integer> matchRows(AnnotationMatrix m,
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
	public static List<Integer> matchRows(AnnotationMatrix m,
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
	public static List<Integer> matchRows(AnnotationMatrix m,
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
	public static List<Integer> matchRows(AnnotationMatrix m,
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
		getInnerMatrix().copyRow(from, row, toRow);
	}

	/**
	 * Copy a row from one matrix to another allowing the row from the source
	 * to be different from the target.
	 *
	 * @param from the from
	 * @param row the row
	 * @param toRow the to row
	 */
	public void copyRow(final AnnotationMatrix from, 
			int row,
			int toRow) {

		if (row >= 0) {
			getInnerMatrix().copyRow(from.getInnerMatrix(), row, toRow);
			copyRowAnnotation(from, row, this, toRow);
		} else {
			//co >= 0
			// We are copying the row annotation of the from matrix to a
			// column in the to matrix
			List<Object> values = 
					from.getColumnAnnotations(row).rowAsList(0);

			//to.setRowAnnotation(from.getColumnAnnotationNames().get(0), row, );
			getInnerMatrix().setRow(toRow, values);
		}
	}

	/**
	 * Copy rows.
	 *
	 * @param from the from
	 * @param row the row
	 * @param toRow the to row
	 */
	public void copyRows(final AnnotationMatrix from, 
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
	public void copyRows(final AnnotationMatrix from, 
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
	public static void copyRowAnnotation(final AnnotationMatrix from, 
			AnnotationMatrix to,
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
	public static void copyRowAnnotation(final AnnotationMatrix from,
			int fromRow,
			AnnotationMatrix to,
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
	public static void copyRowAnnotations(final AnnotationMatrix from, 
			AnnotationMatrix to) {
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
	public static void copyRowAnnotations(final AnnotationMatrix from,
			String name,
			AnnotationMatrix to) {
		to.setRowAnnotations(name, from.getRowAnnotations(name));
	}

	/**
	 * Copy row annotations.
	 *
	 * @param from the from
	 * @param to the to
	 * @param rows the rows
	 */
	public static void copyRowAnnotations(final AnnotationMatrix from, 
			AnnotationMatrix to,
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
	public static void copyRowAnnotations(final AnnotationMatrix from, 
			AnnotationMatrix to,
			Collection<Integer> rows) {
		for (String name : from.getRowAnnotationNames()) {
			
			switch (from.getRowAnnotations(name).getType()) {
			case NUMERIC:
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
	public static <V extends Comparable<? super V>> void copyRowAnnotationsIndexed(final AnnotationMatrix from, 
			AnnotationMatrix to,
			List<Indexed<Integer, V>> rows) {
		for (String name : from.getRowAnnotationNames()) {
			switch (from.getRowAnnotations(name).getType()) {
			case NUMERIC:
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
		if (from instanceof AnnotationMatrix) {
			copyColumnAnnotation((AnnotationMatrix)from, column, this, toColumn);
		}
		
		super.copyColumn(from, column, toColumn); //getInnerMatrix().copyColumn(from, column, toColumn);
	}

	/**
	 * Copy column.
	 *
	 * @param from the from
	 * @param column the column
	 */
	public void copyColumn(final AnnotationMatrix from, int column) {
		copyColumn(from, column, column);
	}

	/**
	 * Copy the column from one matrix to another.
	 *
	 * @param from the from
	 * @param column the column
	 * @param toColumn the offset
	 */
	public void copyColumn(final AnnotationMatrix from, 
			int column,
			int toColumn) {

		if (column >= 0) {
			copyColumnAnnotation(from, column, this, toColumn);
			
			super.copyColumn(from, column, toColumn);
		} else {
			//co >= 0
			// We are copying the row annotation of the from matrix to a
			// column in the to matrix
			List<Object> values = from.getRowAnnotations(column).rowAsList(0);

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
	public static void copyColumnAnnotation(final AnnotationMatrix from,
			int column,
			AnnotationMatrix to,
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
	public static void copyColumnAnnotations(final AnnotationMatrix from, 
			AnnotationMatrix to) {
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
	public static void copyColumnAnnotations(final AnnotationMatrix from, 
			AnnotationMatrix to,
			Collection<Integer> columns) {
		
		for (String name : from.getColumnAnnotationNames()) {
			//List<Object> annotations = from.getColumnAnnotations(name).rowAsList(0);
			//List<Object> subAnnotations = CollectionUtils.subList(annotations, columns);
			//to.setColumnAnnotations(name, subAnnotations);
			
			switch (from.getColumnAnnotations(name).getType()) {
			case NUMERIC:
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
	public static void copyColumnAnnotations(final AnnotationMatrix from,
			int fromStart,
			int fromEnd,
			AnnotationMatrix to,
			int toStart) {
		for (String name : from.getColumnAnnotationNames()) {
			List<Object> annotations = from.getColumnAnnotations(name).rowAsList(0);

			List<Object> subAnnotations = CollectionUtils.subList(annotations, fromStart, fromEnd - fromStart + 1);

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
	public static void copyColumnAnnotations(final AnnotationMatrix from,
			AnnotationMatrix to,
			int toStart) {
		for (String name : from.getColumnAnnotationNames()) {
			List<Object> annotations = 
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
	public static void copyAnnotations(final AnnotationMatrix from, 
			AnnotationMatrix to) {
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
	public static <V extends Comparable<? super V>> void copyColumnAnnotationsIndexed(final AnnotationMatrix from, 
			AnnotationMatrix to,
			List<Indexed<Integer, V>> columns) {
		for (String name : from.getColumnAnnotationNames()) {
			//List<Object> annotations = 
			//		from.getColumnAnnotations(name).rowAsList(0);
			//List<Object> subAnnotations = 
			//		CollectionUtils.subListIndexed(annotations, columns);
			//to.setColumnAnnotations(name, subAnnotations);
			
			switch (from.getColumnAnnotations(name).getType()) {
			case NUMERIC:
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
	public static void setAnnotation(AnnotationMatrix m,
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
	public static void setAnnotation(AnnotationMatrix m,
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
	public static void setAnnotation(AnnotationMatrix m,
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
	 * @param fromColumnStart the from column start
	 * @param fromColumnEnd the from column end
	 * @param to the to
	 */
	public static void copyColumns(AnnotationMatrix from,
			int fromColumnStart,
			int fromColumnEnd,
			AnnotationMatrix to) {
		copyColumns(from,
				fromColumnStart,
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
	public static void copyColumns(AnnotationMatrix from,
			AnnotationMatrix to,
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
	 * @param fromColumnStart the from column start
	 * @param to the to
	 */
	public static void copyColumns(AnnotationMatrix from,
			int fromColumnStart,
			AnnotationMatrix to) {
		copyColumns(from,
				fromColumnStart,
				to,
				0);
	}

	/**
	 * Copy a range of columns from one matrix to another with the
	 * ability to offset the copy in the to matrix.
	 *
	 * @param from the from
	 * @param fromColumnStart the from column start
	 * @param to the to
	 * @param toColOffset the to col offset
	 */
	public static void copyColumns(AnnotationMatrix from,
			int fromColumnStart,
			AnnotationMatrix to,
			int toColOffset) {
		copyColumns(from,
				fromColumnStart,
				from.getColumnCount() - 1,
				to,
				toColOffset);
	}

	/**
	 * Copy columns.
	 *
	 * @param from 	The from matrix.
	 * @param fromColumnStart 	The from column start column
	 * @param fromColumnEnd the from column end
	 * @param to the to
	 * @param toColOffset the to col offset
	 */
	public static void copyColumns(AnnotationMatrix from,
			int fromColumnStart,
			int fromColumnEnd,
			AnnotationMatrix to,
			int toColOffset) {
		int cols = fromColumnEnd - fromColumnStart + 1;

		for (int j = 0; j < cols; ++j) {
			to.copyColumn(from, j + fromColumnStart, j + toColOffset);
		}
	}

	/**
	 * Copy columns.
	 *
	 * @param m the m
	 * @param iter the iter
	 * @param to the to
	 */
	public static void copyColumns(final AnnotationMatrix m,
			final Iterable<? extends MatrixGroup> iter,
			AnnotationMatrix to) {
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
	public static void copyColumns(final AnnotationMatrix m,
			final Iterable<? extends MatrixGroup> iter,
			AnnotationMatrix to,
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
	public static void copyColumns(final AnnotationMatrix from,
			final Collection<Integer> indices,
			AnnotationMatrix to) {
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
	public static void copyColumns(final AnnotationMatrix from,
			final Collection<Integer> indices,
			AnnotationMatrix to,
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
	public static void copy(AnnotationMatrix from, AnnotationMatrix to) {
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
	public static void copy(AnnotationMatrix from,
			int fromRowOffset,
			int fromColOffset,
			AnnotationMatrix to,
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
	public static void writeHeader(AnnotationMatrix m,
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
	public static void writeRow(AnnotationMatrix m, 
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
	public static int findColumn(AnnotationMatrix m, String... terms) {
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
	public static Map<String, Integer> findColumns(AnnotationMatrix m, 
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
	public static MatrixCellRef find(AnnotationMatrix m, 
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
	public static MatrixCellRef find(AnnotationMatrix m, 
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
	public static List<MatrixCellRef> findAll(AnnotationMatrix m, 
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
	public static AnnotationMatrix parseTxtMatrix(Path file,
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
	public static AnnotationMatrix parseTxtMatrix(Path file,
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
	public static AnnotationMatrix parseCsvMatrix(Path file, 
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
	public static AnnotationMatrix parseDynamicMatrix(Path file,
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
	public static void copyRows(final AnnotationMatrix m, 
			final  List<Integer> rows, 
			AnnotationMatrix ret) {
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
	public static void copyRows(final AnnotationMatrix m, 
			final List<Integer> rows, 
			AnnotationMatrix ret,
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
	public static void copyColumnNames(AnnotationMatrix from,
			AnnotationMatrix to) {
		copyColumnNames(from, to, 0);
	}

	/**
	 * Copy column names.
	 *
	 * @param from the from
	 * @param to the to
	 * @param offset the offset
	 */
	public static void copyColumnNames(AnnotationMatrix from,
			AnnotationMatrix to, 
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
	public static List<String> columnNames(AnnotationMatrix mMatrix, Collection<Integer> columns) {
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
	public static List<String> rowNames(AnnotationMatrix mMatrix, Collection<Integer> rows) {
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
			final AnnotationMatrix m,
			AnnotationMatrix ret) {

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