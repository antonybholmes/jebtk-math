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
import org.jebtk.core.text.TextUtils;
import org.jebtk.math.statistics.Statistics;
import org.jebtk.math.statistics.TTest;


// TODO: Auto-generated Javadoc
/**
 * Matrix for storing longs only.
 *
 * @author Antony Holmes Holmes
 */
public class LongMatrix extends IndexableMatrix {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member data.
	 */
	public long[] mData;

	/**
	 * Instantiates a new numerical matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 */
	public LongMatrix(int rows, int columns) {
		this(rows, columns, Long.MIN_VALUE);
	}

	/**
	 * Create a new matrix and initialize all cells to a common value.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @param v the v
	 */
	public LongMatrix(int rows, int columns, long v) {
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
		mData = new long[mSize];
		//mTextData = new String[mSize];
	}

	/**
	 * Clone a matrix.
	 *
	 * @param m the m
	 */
	public LongMatrix(Matrix m) {
		this(m.getRowCount(), m.getColumnCount());

		update(m);
	}


	/**
	 * Instantiates a new long matrix.
	 *
	 * @param m the m
	 */
	public LongMatrix(IndexableMatrix m) {
		this(m.getRowCount(), m.getColumnCount());

		update(m);
	}

	/**
	 * Instantiates a new long matrix.
	 *
	 * @param m the m
	 */
	public LongMatrix(LongMatrix m) {
		this(m.getRowCount(), m.getColumnCount());

		update(m);
	}

	/**
	 * Sets the.
	 *
	 * @param m the m
	 */
	public void set(LongMatrix m) {
		update(m);

		fireMatrixChanged();
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#updateToNull(int)
	 */
	public void updateToNull(int index) {
		mData[index] = Long.MIN_VALUE;
	}

	/**
	 * Update.
	 *
	 * @param m the m
	 */
	public void update(LongMatrix m) {
		System.arraycopy(m.mData, 0, mData, 0, Math.min(m.mData.length, mData.length));
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#copy()
	 */
	@Override
	public Matrix copy() {
		return new LongMatrix(this);
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
		long v = mData[index];
		
		if (v == Long.MIN_VALUE) {
			return Double.NaN;
		} else  {
			return v;
		}		
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#getIntValue(int)
	 */
	@Override
	public int getIntValue(int index) {
		return (int)getLongValue(index);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#getLongValue(int)
	 */
	@Override
	public long getLongValue(int index) {
		return mData[index];
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.Matrix#update(double)
	 */
	@Override
	public void update(double v) {
		//for (int i = 0; i < mData.length; ++i) {
		//	mData[i] = v;
		//}

		Arrays.fill(mData, (int)v);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#update(int, double)
	 */
	@Override
	public void update(int index, double v) {
		update(index, (int)v);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#update(int, int)
	 */
	public void update(int index, int v) {
		mData[index] = v;
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.IndexMatrix#getText(int)
	 */
	@Override
	public String getText(int index) {
		return Long.toString(mData[index]);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#setValueColumn(int, java.util.List)
	 */
	@Override
	public void setValueColumn(int column, final List<Double> values) {
		int r = Math.min(getRowCount(), values.size());

		int ix = getIndex(0, column);

		for (int i = 0; i < r; ++i) {
			mData[ix] = values.get(i).intValue();

			ix += mCols;
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.Matrix#copyColumn(org.abh.common.math.matrix.DoubleMatrix, int, int)
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
			mData[i2] = (long)from.mData[i1];

			i1 += from.mCols;
			i2 += mCols;
		}
		
		fireMatrixChanged();
	}
	
	/**
	 * Specialized instance of column copying for numerical matrices.
	 *
	 * @param from the from
	 * @param column the column
	 * @param toColumn the to column
	 */
	public void copyColumn(final LongMatrix from, 
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

			i1 += from.mCols;
			i2 += mCols;
		}
		
		fireMatrixChanged();
	}

	/**
	 * Copy row.
	 *
	 * @param from the from
	 * @param row the row
	 * @param toRow the to row
	 */
	public void copyRow(final LongMatrix from, 
			int row,
			int toRow) {

		int c = Math.min(from.getColumnCount(), getColumnCount());

		System.arraycopy(from.mData, from.mRowOffsets[row], mData, mRowOffsets[toRow], c);
		
		fireMatrixChanged();
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#transpose()
	 */
	@Override
	public Matrix transpose() { 
		LongMatrix ret = createLongMatrix(mCols, mRows);

		int i2 = 0;
		int c = 0;
		
		for (int i = 0; i < mData.length; ++i) {
			// Each time we end a row, reset i2 back to the next column
			if (i % mCols == 0) {
				i2 = c++;
			}
			
			ret.mData[i2] = mData[i];
		
			// Skip blocks
			i2 += mRows;
		}
		
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#columnAsDouble(int)
	 */
	@Override
	public double[] columnAsDouble(int column) {
		int r = getRowCount();
		
		double[] values = new double[r];
		
		int i1 = column;
		
		for (int row = 0; row < r; ++row) {
			values[row] = mData[i1];
			
			i1 += mCols;
		}

		return values;
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.math.matrix.IndexMatrix#rowAsDouble(int)
	 */
	@Override
	public double[] rowAsDouble(int row) {
		int c = getColumnCount();
		
		double[] values = new double[c];
		
		int i1 = getIndex(row, 0);
		
		System.arraycopy(mData, i1, values, 0, c);
		
		return values;
	}
	

	//
	// Static methods
	//

	/**
	 * Returns the max value in a matrix.
	 *
	 * @param m the m
	 * @return the double
	 */
	public static double max(Matrix m) {
		if (m instanceof LongMatrix) {
			return max((LongMatrix)m);
		}
		
		double max = Double.MIN_VALUE;

		for (int i = 0; i < m.getRowCount(); ++i) {
			for (int j = 0; j < m.getColumnCount(); ++j) {
				double v = m.getValue(i, j);

				if (!Mathematics.isValidNumber(v)) {
					continue;
				}

				if (v > max) {
					max = v;
				}
			}
		}

		return max;
	}
	
	/**
	 * Max.
	 *
	 * @param m the m
	 * @return the double
	 */
	public static double max(LongMatrix m) {
		double max = Double.MIN_VALUE;

		for (double v : m.mData) {
			if (!Mathematics.isValidNumber(v)) {
				continue;
			}

			if (v > max) {
				max = v;
			}
		}

		return max;
	}

	/**
	 * Returns the absolute max value.
	 *
	 * @param m the m
	 * @return the double
	 */
	public static double absMax(Matrix m) {
		double max = Double.MIN_VALUE;

		for (int i = 0; i < m.getRowCount(); ++i) {
			for (int j = 0; j < m.getColumnCount(); ++j) {
				double v = m.getValue(i, j);

				if (!Mathematics.isValidNumber(v)) {
					continue;
				}

				if (v > max) {
					max = v;
				}
			}
		}
		return max;
	}

	/**
	 * Returns the min value in a matrix.
	 *
	 * @param m the m
	 * @return the double
	 */
	public static double min(Matrix m) {
		if (m instanceof LongMatrix) {
			return min((LongMatrix)m);
		}
		
		double min = Double.MAX_VALUE;

		for (int i = 0; i < m.getRowCount(); ++i) {
			for (int j = 0; j < m.getColumnCount(); ++j) {
				double v = m.getValue(i, j);

				if (!Mathematics.isValidNumber(v)) {
					continue;
				}

				if (v < min) {
					min = v;
				}
			}
		}

		return min;
	}
	
	/**
	 * Min.
	 *
	 * @param m the m
	 * @return the double
	 */
	public static double min(LongMatrix m) {
		double min = Double.MAX_VALUE;

		for (double v : m.mData) {
			if (!Mathematics.isValidNumber(v)) {
				continue;
			}

			if (v < min) {
				min = v;
			}
		}

		return min;
	}
	
	
	
	

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
	 * Return the row indices of rows that have differential expression.
	 *
	 * @param m the m
	 * @param g1 the g1
	 * @param g2 the g2
	 * @param equalVariance the equal variance
	 * @return the list
	 */
	public static List<Double> tTest(AnnotationMatrix m, 
			MatrixGroup g1,
			MatrixGroup g2,
			boolean equalVariance) {
		Matrix im = m.getInnerMatrix();
		
		List<Double> pvalues = new ArrayList<Double>(m.getRowCount());

		List<Integer> g11 = MatrixGroup.findColumnIndices(m, g1);
		List<Integer> g22 = MatrixGroup.findColumnIndices(m, g2);

		for (int i = 0; i < im.getRowCount(); ++i) {
			List<Double> p1 = new ArrayList<Double>(g11.size());

			for (int c : g11) {
				p1.add(m.getValue(i, c));
			}

			List<Double> p2 = new ArrayList<Double>(g22.size());

			for (int c : g22) {
				p2.add(m.getValue(i, c));
			}

			double p;

			if (equalVariance) {
				p = TTest.twoTailEqualVarianceTTest(p1, p2);
			} else {
				p = TTest.twoTailUnequalVarianceTTest(p1, p2);
			}

			// Set strange values to NaN
			if (Mathematics.isInvalidNumber(p)) {
				p = 1; //Double.NaN;
			}

			pvalues.add(p);
		}

		return pvalues;
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
	public static List<Double> diffGroupZScores(AnnotationMatrix matrix, 
			MatrixGroup phenGroup, 
			MatrixGroup controlGroup) {
		List<Integer> phenIndices = 
				MatrixGroup.findColumnIndices(matrix, phenGroup);

		List<Integer> controlIndices = 
				MatrixGroup.findColumnIndices(matrix, controlGroup);

		Matrix im = matrix.getInnerMatrix();

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
	public static List<Double> logFoldChange(AnnotationMatrix matrix, 
			MatrixGroup g1, 
			MatrixGroup g2) {
		List<Integer> g11 = MatrixGroup.findColumnIndices(matrix, g1);
		List<Integer> g22 = MatrixGroup.findColumnIndices(matrix, g2);

		Matrix im = matrix.getInnerMatrix();

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
	public static List<Double> foldChange(AnnotationMatrix matrix, 
			MatrixGroup g1, 
			MatrixGroup g2) {
		List<Integer> g11 = MatrixGroup.findColumnIndices(matrix, g1);
		List<Integer> g22 = MatrixGroup.findColumnIndices(matrix, g2);

		Matrix im = matrix.getInnerMatrix();

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
	public static AnnotationMatrix parseEstMatrix(Path file) throws IOException {
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
	public static List<Double> means(AnnotationMatrix m, MatrixGroup g) {
		List<Integer> g1 = MatrixGroup.findColumnIndices(m, g);

		Matrix im = m.getInnerMatrix();

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
	public static double maxRowSum(AnnotationMatrix m) {
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
	 * Creates the.
	 *
	 * @param m the m
	 * @return the long matrix
	 */
	public static LongMatrix create(Matrix m) {
		return create(m.getRowCount(), m.getColumnCount());
	}

	/**
	 * Creates the.
	 *
	 * @param r the r
	 * @param c the c
	 * @return the long matrix
	 */
	public static LongMatrix create(int r, int c) {
		return new LongMatrix(r, c);
	}

	/**
	 * Column means.
	 *
	 * @param m the m
	 * @return the double[]
	 */
	public static double[] columnMeans(Matrix m) {
		if (m instanceof LongMatrix) {
			return columnMeans((LongMatrix)m);
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
	public static double[] columnMeans(LongMatrix m) {
		int r = m.getRowCount();
		int c = m.getColumnCount();
		
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
		if (m instanceof LongMatrix) {
			return columnPopStdDev((LongMatrix)m);
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
	public static double[] columnPopStdDev(LongMatrix m) {
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
	 * Creates the long matrix.
	 *
	 * @param m the m
	 * @return the long matrix
	 */
	public static LongMatrix createLongMatrix(Matrix m) {
		return createLongMatrix(m.getRowCount(), m.getColumnCount());
	}
	
	/**
	 * Creates the long matrix.
	 *
	 * @param rows the rows
	 * @param cols the cols
	 * @return the long matrix
	 */
	public static LongMatrix createLongMatrix(int rows, int cols) {
		return new LongMatrix(rows, cols);
	}
}
