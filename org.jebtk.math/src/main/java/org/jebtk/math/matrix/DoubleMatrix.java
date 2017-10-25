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

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jebtk.core.Mathematics;
import org.jebtk.core.sys.SysUtils;
import org.jebtk.core.text.TextUtils;
import org.jebtk.math.statistics.Statistics;


// TODO: Auto-generated Javadoc
/**
 * Matrix for storing doubles.
 *
 * @author Antony Holmes Holmes
 */
public class DoubleMatrix extends IndexableMatrix {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** The m data. */
	public double[] mData = null;

	/**
	 * Instantiates a new numerical matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 */
	public DoubleMatrix(int rows, int columns) {
		this(rows, columns, NULL_NUMBER);
	}

	/**
	 * Create a new matrix and initialize all cells to a common value.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @param v the v
	 */
	public DoubleMatrix(int rows, int columns, double v) {
		super(rows, columns);

		// We use a 1d array to store a 2d matrix for speed.
		init();

		// Set the default value
		update(v);
	}

	/**
	 * Inits the.
	 */
	protected void init() {
		mData = new double[mSize];
		//mTextData = new String[mSize];
	}

	/**
	 * Clone a matrix.
	 *
	 * @param m the m
	 */
	public DoubleMatrix(Matrix m) {
		this(m.getRowCount(), m.getColumnCount());

		update(m);
	}


	/**
	 * Instantiates a new double matrix.
	 *
	 * @param m the m
	 */
	public DoubleMatrix(IndexableMatrix m) {
		this(m.getRowCount(), m.getColumnCount());

		update(m);
	}

	/**
	 * Instantiates a new double matrix.
	 *
	 * @param m the m
	 */
	public DoubleMatrix(DoubleMatrix m) {
		this(m.getRowCount(), m.getColumnCount());

		update(m);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#updateToNull(int)
	 */
	@Override
	public void updateToNull(int index) {
		mData[index] = NULL_NUMBER;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#update(org.abh.common.math.matrix.Matrix)
	 */
	@Override
	public void update(Matrix m) {
		if (m instanceof DoubleMatrix) {
			update((DoubleMatrix)m);
		} else {
			super.update(m);
		}
	}

	/**
	 * Update.
	 *
	 * @param m the m
	 */
	public void update(DoubleMatrix m) {
		System.arraycopy(m.mData, 0, mData, 0, Math.min(m.mData.length, mData.length));
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public double[] getData() {
		return mData;
	}
	
	@Override
	public double[] toDouble() {
		return mData;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#copy()
	 */
	@Override
	public Matrix copy() {
		return new DoubleMatrix(this);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.Matrix#getNumCells()
	 */
	@Override
	public int getNumCells() {
		return mData.length;
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#get(int)
	 */
	@Override
	public Object get(int index) {
		return getValue(index);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#getValue(int)
	 */
	@Override
	public double getValue(int index) {
		return mData[index];
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.Matrix#updateValue(double)
	 */
	@Override
	public void update(double v) {
		Arrays.fill(mData, v);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#updateValue(int, double)
	 */
	@Override
	public void update(int index, double v) {
		mData[index] = v;
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#getText(int)
	 */
	@Override
	public String getText(int index) {
		return Double.toString(mData[index]);
	}

	@Override
	public void setRow(int row, double[] values) {
		SysUtils.arraycopy(values, mData, mRowOffsets[row], mDim.mCols);
		
		fireMatrixChanged();
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#setValueColumn(int, java.util.List)
	 */
	@Override
	public void setValueColumn(int column, List<Double> values) {
		int r = Math.min(mDim.mRows, values.size());

		int ix = column;

		for (int i = 0; i < r; ++i) {
			mData[ix] = values.get(i);

			ix += mDim.mCols;
		}
		
		fireMatrixChanged();
	}
	
	@Override
	public void setColumn(int column, double[] values) {
		SysUtils.arraycopy(values, mData, column, mDim.mCols, mDim.mRows);
		
		fireMatrixChanged();
	}
	
	

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#copyColumn(org.abh.common.math.matrix.Matrix, int, int)
	 */
	@Override
	public void copyColumn(final Matrix from, 
			int column,
			int toColumn) {
		if (from instanceof DoubleMatrix) {
			copyColumn((DoubleMatrix)from, column, toColumn);
		} else if (from instanceof TextMatrix) { 
			copyColumn((TextMatrix)from, column, toColumn);
		} else {
			int i1 = getIndex(0, toColumn);

			int r = Math.min(from.getRowCount(), getRowCount());

			for (int i = 0; i < r; ++i) {
				mData[i1] = from.getValue(i, column);

				i1 += mDim.mCols;
			}
		}
	}

	/**
	 * Specialized instance of column copying for numerical matrices.
	 *
	 * @param from the from
	 * @param column the column
	 * @param toColumn the to column
	 */
	@Override
	public void copyColumn(final DoubleMatrix from, 
			int column,
			int toColumn) {
		//if (from.getRowCount() == 0 || getRowCount() == 0) {
		//	return;
		//}

		int i1 = from.getIndex(0, column);
		int i2 = getIndex(0, toColumn);

		int r = Math.min(from.getRowCount(), getRowCount());

		for (int i = 0; i < r; ++i) {
			mData[i2] = from.mData[i1];

			i1 += from.mDim.mCols;
			i2 += mDim.mCols;
		}

		fireMatrixChanged();
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#copyRow(org.abh.common.math.matrix.DoubleMatrix, int, int)
	 */
	@Override
	public void copyRow(final DoubleMatrix from, 
			int row,
			int toRow) {

		int c = Math.min(from.getColumnCount(), getColumnCount());

		System.arraycopy(from.mData, from.mRowOffsets[row], mData, mRowOffsets[toRow], c);

		fireMatrixChanged();
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#columnAsDouble(int)
	 */
	@Override
	public void columnAsDouble(int column, double[] ret) {
		int i1 = column;

		for (int row = 0; row < mDim.mRows; ++row) {
			ret[row] = mData[i1];

			i1 += mDim.mCols;
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#rowAsDouble(int)
	 */
	@Override
	public void rowAsDouble(int row, double[] ret) {
		SysUtils.arraycopy(mData, mRowOffsets[row], ret, mDim.mCols);
	}

	@Override
	public void apply(MatrixCellFunction f) {
		int r = 0;
		int c = 0;
		
		for (int i = 0; i < mData.length; ++i) {
			mData[i] = f.apply(r, c++, mData[i]);
			
			if (c == mDim.mCols) {
				c = 0;
				++r;
			}
		}
		
		fireMatrixChanged();
	}
	
	@Override
	public void rowApply(MatrixCellFunction f) {
		for (int i = 0; i < mDim.mRows; ++i) {
			rowApply(f, i);
		}
		
		fireMatrixChanged();
	}
	
	@Override
	public void rowApply(MatrixCellFunction f, int index) {
		int offset = mRowOffsets[index];
		
		for (int i = 0; i < mDim.mCols; ++i) {
			mData[offset] = f.apply(i, 0, mData[offset]);
			
			++offset;
		}
		
		fireMatrixChanged();
	}
	
	@Override
	public void colApply(MatrixCellFunction f, int col) {
		int offset = col;
		
		for (int i = 0; i < mDim.mCols; ++i) {
			mData[offset] = f.apply(i, 0, mData[offset]);
			
			offset += mDim.mCols;
		}
		
		fireMatrixChanged();
	}
	
	@Override
	public double stat(MatrixStatFunction f) {
		f.init();
		
		for (int i = 0; i < mData.length; ++i) {
			f.apply(i, 0, mData[i]);
		}
		
		return f.getStat();
	}
	
	@Override
	public double rowStat(MatrixStatFunction f, int index) {
		f.init();
		
		int offset = mRowOffsets[index];
		
		for (int i = 0; i < mDim.mCols; ++i) {
			f.apply(i, 0, mData[offset]);
			
			++offset;
		}
		
		return f.getStat();
	}
	
	@Override
	public double colStat(MatrixStatFunction f, int index) {
		int offset = index;
		
		for (int i = 0; i < mDim.mCols; ++i) {
			f.apply(i, 0, mData[offset]);
			
			offset += mDim.mCols;
		}
		
		return f.getStat();
	}

	@Override
	public Matrix dot(final Matrix m) {
		if (m instanceof DoubleMatrix) {
			return dot((DoubleMatrix)m);
		} else {
			return super.dot(m);
		}
	}
	
	public Matrix dot(final DoubleMatrix m) {
		dot(this, m);

		fireMatrixChanged();

		return this;
	}
	
	public static void dot(DoubleMatrix m1, final DoubleMatrix m2) {
		for (int i = 0; i < m1.mData.length; ++i) {
			m1.mData[i] *= m2.mData[i];
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#transpose()
	 */
	@Override
	public Matrix transpose() { 
		return transpose(this);
	}
	
	public static Matrix transpose(final DoubleMatrix m) { 
		DoubleMatrix ret = DoubleMatrix.createDoubleMatrix(m.mDim.mCols, m.mDim.mRows);

		int i2 = 0;
		int c = 0;

		for (int i = 0; i < m.mData.length; ++i) {
			// Each time we end a row, reset i2 back to the next column
			if (i % m.mDim.mCols == 0) {
				i2 = c++;
			}

			ret.mData[i2] = m.mData[i];

			// Skip blocks
			i2 += m.mDim.mRows;
		}

		return ret;
	}

	//
	// Static methods
	//

	/**
	 * Finds the minimum in each row.
	 *
	 * @param matrix the matrix
	 * @return the double[]
	 */
	public static double[] minInRow(Matrix matrix) {
		double[] ret = new double[matrix.getRowCount()];

		int r = matrix.getRowCount();
		int c = matrix.getColumnCount();

		for (int i = 0; i < r; ++i) {
			double min = Double.MAX_VALUE;

			for (int j = 0; j < c; ++j) {
				double v = matrix.getValue(i, j);

				if (Mathematics.isValidNumber(v)) {
					if (v < min) {
						min = v;
					}
				}
			}

			ret[i] = min;
		}

		return ret;
	}


	/**
	 * Finds the maximum in each row of the matrix.
	 *
	 * @param matrix the matrix
	 * @return the double[]
	 */
	public static double[] maxInRow(Matrix matrix) {
		double[] ret = new double[matrix.getRowCount()];

		int r = matrix.getRowCount();
		int c = matrix.getColumnCount();

		for (int i = 0; i < r; ++i) {
			double max = Double.MIN_VALUE;

			for (int j = 0; j < c; ++j) {
				double v = matrix.getValue(i, j);

				if (Mathematics.isValidNumber(v)) {
					if (v > max) {
						max = v;
					}
				}
			}

			ret[i] = max;
		}

		return ret;
	}

	/**
	 * Finds the minimum in each row.
	 *
	 * @param matrix the matrix
	 * @return the double[]
	 */
	public static double[] minInColumn(Matrix matrix) {
		double[] ret = new double[matrix.getColumnCount()];

		int r = matrix.getRowCount();
		int c = matrix.getColumnCount();

		for (int i = 0; i < c; ++i) {
			double min = Double.MAX_VALUE;

			for (int j = 0; j < r; ++j) {
				double v = matrix.getValue(j, i);

				if (Mathematics.isValidNumber(v)) {
					if (v < min) {
						min = v;
					}
				}
			}

			ret[i] = min;
		}

		return ret;
	}


	/**
	 * Finds the maximum in each row of the matrix.
	 *
	 * @param matrix the matrix
	 * @return the double[]
	 */
	public static double[] maxInColumn(Matrix matrix) {
		double[] ret = new double[matrix.getColumnCount()];

		int c = matrix.getColumnCount();

		for (int i = 0; i < c; ++i) {
			ret[i] = maxInColumn(matrix, i);
		}

		return ret;
	}

	/**
	 * Max in column.
	 *
	 * @param matrix the matrix
	 * @param column the column
	 * @return the double
	 */
	public static double maxInColumn(Matrix matrix, int column) {
		double ret = Double.MIN_VALUE;

		int r = matrix.getRowCount();

		for (int j = 0; j < r; ++j) {
			ret = Math.max(ret, matrix.getValue(j, column));
		}

		return ret;
	}

	/**
	 * Min in column.
	 *
	 * @param matrix the matrix
	 * @param column the column
	 * @return the double
	 */
	public static double minInColumn(Matrix matrix, int column) {
		double ret = Double.MAX_VALUE;

		int r = matrix.getRowCount();

		for (int j = 0; j < r; ++j) {
			ret = Math.min(ret, matrix.getValue(j, column));
		}

		return ret;
	}


	/**
	 * Returns the differential z score between two column
	 * groups in a matrix.
	 *
	 * @param matrix the matrix
	 * @param phenGroup the g1
	 * @param controlGroup the g2
	 * @return the list
	 */
	public static List<Double> diffGroupZScores(DataFrame matrix, 
			MatrixGroup phenGroup, 
			MatrixGroup controlGroup) {
		List<Integer> phenIndices = 
				MatrixGroup.findColumnIndices(matrix, phenGroup);

		List<Integer> controlIndices = 
				MatrixGroup.findColumnIndices(matrix, controlGroup);

		Matrix im = matrix.getMatrix();

		List<Double> zscores = new ArrayList<Double>(im.getRowCount());

		for (int i = 0; i < im.getRowCount(); ++i) {
			List<Double> d1 = new ArrayList<Double>();

			for (int c : phenIndices) {
				d1.add(im.getValue(i, c));
			}

			double mean1 = Statistics.mean(d1);
			double sd1 = Statistics.popStdDev(d1); //sampleStandardDeviation(d1);

			List<Double> d2 = new ArrayList<Double>();

			for (int c : controlIndices) {
				d2.add(im.getValue(i, c));
			}

			double mean2 = Statistics.mean(d2);
			double sd2 = Statistics.popStdDev(d2);

			double sd = (sd1 + sd2); // / 2.0;

			double zscore;

			if (sd > 0) {
				zscore = (mean1 - mean2) / sd;
			} else {
				zscore = 0;
			}

			zscores.add(zscore);
		}

		return zscores;
	}

	/**
	 * Returns the log fold changes between groups of a log
	 * transformed matrix.
	 *
	 * @param matrix the matrix
	 * @param g1 the g1
	 * @param g2 the g2
	 * @return the list
	 */
	public static List<Double> logFoldChange(DataFrame matrix, 
			MatrixGroup g1, 
			MatrixGroup g2) {
		List<Integer> g11 = MatrixGroup.findColumnIndices(matrix, g1);
		List<Integer> g22 = MatrixGroup.findColumnIndices(matrix, g2);

		Matrix im = matrix.getMatrix();

		List<Double> foldChanges = new ArrayList<Double>(im.getRowCount());

		for (int i = 0; i < im.getRowCount(); ++i) {
			List<Double> d1 = new ArrayList<Double>(g11.size());

			for (int c : g11) {
				d1.add(im.getValue(i, c));
			}

			double mean1 = Statistics.mean(d1);

			List<Double> d2 = new ArrayList<Double>(g22.size());

			for (int c : g22) {
				d2.add(im.getValue(i, c));
			}

			double mean2 = Statistics.mean(d2);

			double foldChange = mean1 - mean2;

			foldChanges.add(foldChange);
		}

		return foldChanges;
	}

	/**
	 * Calculate the fold changes between two groups.
	 *
	 * @param matrix the matrix
	 * @param g1 the g1
	 * @param g2 the g2
	 * @return the list
	 */
	public static List<Double> foldChange(DataFrame matrix, 
			MatrixGroup g1, 
			MatrixGroup g2) {
		List<Integer> g11 = MatrixGroup.findColumnIndices(matrix, g1);
		List<Integer> g22 = MatrixGroup.findColumnIndices(matrix, g2);

		Matrix im = matrix.getMatrix();

		List<Double> foldChanges = new ArrayList<Double>(im.getRowCount());

		for (int i = 0; i < im.getRowCount(); ++i) {
			List<Double> d1 = new ArrayList<Double>();

			for (int c : g11) {
				d1.add(im.getValue(i, c));
			}

			double mean1 = Statistics.mean(d1);

			List<Double> d2 = new ArrayList<Double>();

			for (int c : g22) {
				d2.add(im.getValue(i, c));
			}

			double mean2 = Statistics.mean(d2);

			double foldChange = mean1 / mean2;

			if (Double.isNaN(foldChange)) {
				foldChange = 0;
			}

			if (Double.isInfinite(foldChange)) {
				foldChange = 0;
			}

			// Division mistake most likely
			if (foldChange > 1000000) {
				foldChange = 0;
			}

			foldChanges.add(foldChange);
		}

		return foldChanges;
	}

	/**
	 * Parses the est matrix.
	 *
	 * @param file the file
	 * @return the annotation matrix
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static DataFrame parseEstMatrix(Path file) throws IOException {
		return new EstDoubleMatrixParser().parse(file);
	}



	/**
	 * Parse a string as a double or return null if
	 * the string == "n/a".
	 *
	 * @param value the value
	 * @return the double
	 */
	public static double parseValue(String value) {
		if (value.toLowerCase().equals(TextUtils.NA)) {
			return Double.NaN;
		}

		try {
			return TextUtils.parseDouble(value);
		} catch (ParseException e) {
			return Double.NaN;
		}
	}

	/**
	 * Return the row wise means of a matrix group.
	 *
	 * @param m the m
	 * @param g the g
	 * @return the list
	 */
	public static List<Double> means(DataFrame m, MatrixGroup g) {
		List<Integer> g1 = MatrixGroup.findColumnIndices(m, g);

		Matrix im = m.getMatrix();

		List<Double> means = 
				new ArrayList<Double>(im.getRowCount());

		for (int i = 0; i < im.getRowCount(); ++i) {
			List<Double> values = new ArrayList<Double>(g1.size());

			for (int c : g1) {
				values.add(im.getValue(i, c));
			}

			double mean = Statistics.mean(values);

			means.add(mean);
		}

		return means;
	}

	/**
	 * Sum.
	 *
	 * @param m the m
	 * @return the double
	 */
	public static double sum(Matrix m) {
		double sum = 0;

		for (int i = 0; i < m.getRowCount(); ++i) {
			for (int j = 0; j < m.getColumnCount(); ++j) {
				double v = m.getValue(i, j);

				if (Mathematics.isValidNumber(v)) {
					sum += v;
				}
			}
		}

		return sum;
	}

	/**
	 * Return the maximum sum of values in a given row.
	 *
	 * @param m the m
	 * @return the double
	 */
	public static double maxRowSum(DataFrame m) {
		double max = Double.MIN_VALUE;

		for (int i = 0; i < m.getRowCount(); ++i) {
			double sum = 0;

			for (int j = 0; j < m.getColumnCount(); ++j) {
				double v = m.getValue(i, j);

				if (Mathematics.isValidNumber(v)) {
					sum += v;
				}
			}

			if (sum > max) {
				max = sum;
			}
		}

		return max;
	}

	/**
	 * Column means.
	 *
	 * @param m the m
	 * @return the double[]
	 */
	public static double[] columnMeans(Matrix m) {
		if (m instanceof DoubleMatrix) {
			return columnMeans((DoubleMatrix)m);
		} else if (m instanceof IndexableMatrix) {
			return columnMeans((IndexableMatrix)m);
		} else {
			int r = m.getRowCount();
			int c = m.getColumnCount();

			double[] means = new double[c];

			for (int i = 0; i < c; ++i) {
				double[] values = new double[r];

				for (int j = 0; j < r; ++j) {
					values[j] = m.getValue(j, i);
				}

				double mean = Statistics.mean(values);

				means[i] = mean;
			}

			return means;
		}
	}

	/**
	 * Return the means of the matrix columns.
	 *
	 * @param m the m
	 * @return the double[]
	 */
	public static double[] columnMeans(IndexableMatrix m) {
		int r = m.getRowCount();
		int c = m.getColumnCount();

		double[] means = new double[c];

		for (int i = 0; i < c; ++i) {
			double[] values = new double[r];

			int index = i;

			for (int j = 0; j < r; ++j) {
				values[j] = m.getValue(index);

				index += c;
			}

			double mean = Statistics.mean(values);

			means[i] = mean;
		}

		return means;
	}

	/**
	 * Column means.
	 *
	 * @param m the m
	 * @return the double[]
	 */
	public static double[] columnMeans(DoubleMatrix m) {
		int r = m.mDim.mRows;
		int c = m.mDim.mCols;

		double[] means = new double[c];

		for (int i = 0; i < c; ++i) {
			double[] values = new double[r];

			int index = i;

			for (int j = 0; j < r; ++j) {
				values[j] = m.mData[index];

				index += c;
			}

			double mean = Statistics.mean(values);

			means[i] = mean;
		}

		return means;
	}

	/**
	 * Column pop std dev.
	 *
	 * @param m the m
	 * @return the double[]
	 */
	public static double[] columnPopStdDev(Matrix m) {
		if (m instanceof DoubleMatrix) {
			return columnPopStdDev((DoubleMatrix)m);
		} else if (m instanceof IndexableMatrix) {
			return columnPopStdDev((IndexableMatrix)m);
		} else {
			int r = m.getRowCount();
			int c = m.getColumnCount();

			double[] ret = new double[c];

			for (int i = 0; i < c; ++i) {
				double[] values = new double[r];

				for (int j = 0; j < r; ++j) {
					values[j] = m.getValue(j, i);
				}

				double sd = Statistics.popStdDev(values);

				ret[i] = sd;
			}

			return ret;
		}
	}

	/**
	 * Return the means of the matrix columns.
	 *
	 * @param m the m
	 * @return the double[]
	 */
	public static double[] columnPopStdDev(IndexableMatrix m) {
		int r = m.getRowCount();
		int c = m.getColumnCount();

		double[] ret = new double[c];

		for (int i = 0; i < c; ++i) {
			double[] values = new double[r];

			int index = i;

			for (int j = 0; j < r; ++j) {
				values[j] = m.getValue(index);

				index += c;
			}

			double sd = Statistics.popStdDev(values);

			ret[i] = sd;
		}

		return ret;
	}

	/**
	 * Column pop std dev.
	 *
	 * @param m the m
	 * @return the double[]
	 */
	public static double[] columnPopStdDev(DoubleMatrix m) {
		int r = m.getRowCount();
		int c = m.getColumnCount();

		double[] ret = new double[c];

		for (int i = 0; i < c; ++i) {
			double[] values = new double[r];

			int index = i;

			for (int j = 0; j < r; ++j) {
				values[j] = m.mData[index];

				index += c;
			}

			double sd = Statistics.popStdDev(values);

			ret[i] = sd;
		}

		return ret;
	}

	/**
	 * Returns a new empty matrix the same dimensions as the input matrix.
	 *
	 * @param m the m
	 * @return the double matrix
	 */
	public static DoubleMatrix createDoubleMatrix(Matrix m) {
		return createDoubleMatrix(m.getRowCount(), m.getColumnCount());
	}

	/**
	 * Creates the double matrix.
	 *
	 * @param rows the rows
	 * @param cols the cols
	 * @return the double matrix
	 */
	public static DoubleMatrix createDoubleMatrix(int rows, int cols) {
		return new DoubleMatrix(rows, cols);
	}

	/**
	 * Create a zero matrix.
	 *
	 * @param rows the rows
	 * @param cols the cols
	 * @return the double matrix
	 */
	public static DoubleMatrix createZerosMatrix(int rows, int cols) {
		return new DoubleMatrix(rows, cols, 0);
	}

	/**
	 * Creates the ones matrix.
	 *
	 * @param rows the rows
	 * @param cols the cols
	 * @return the double matrix
	 */
	public static DoubleMatrix createOnesMatrix(int rows, int cols) {
		return new DoubleMatrix(rows, cols, 1);
	}
}
