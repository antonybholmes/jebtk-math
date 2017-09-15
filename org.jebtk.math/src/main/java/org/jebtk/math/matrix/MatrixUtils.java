/**
 * Copyright 2017 Antony Holmes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jebtk.math.matrix;

import java.util.ArrayList;
import java.util.List;

import org.jebtk.core.Mathematics;
import org.jebtk.math.statistics.Statistics;
import org.jebtk.math.statistics.TTest;

// TODO: Auto-generated Javadoc
/**
 * The Class MatrixUtils.
 */
public class MatrixUtils {

	/**
	 * Instantiates a new matrix utils.
	 */
	private MatrixUtils() {

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
	 * Row means.
	 *
	 * @param m the m
	 * @return the double[]
	 */
	public static double[] rowMeans(Matrix m) {
		if (m instanceof DoubleMatrix) {
			return rowMeans((DoubleMatrix)m);
		} else if (m instanceof IndexableMatrix) {
			return rowMeans((IndexableMatrix)m);
		} else {
			int r = m.getRowCount();
			int c = m.getColumnCount();

			double[] means = new double[r];

			for (int i = 0; i < r; ++i) {
				double[] values = new double[c];

				for (int j = 0; j < c; ++j) {
					values[j] = m.getValue(i, j);
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
	public static double[] rowMeans(IndexableMatrix m) {
		int r = m.getRowCount();
		int c = m.getColumnCount();

		double[] means = new double[r];

		for (int i = 0; i < r; ++i) {
			double[] values = new double[c];

			int index = i * c;

			for (int j = 0; j < c; ++j) {
				values[j] = m.getValue(index);

				++index;
			}

			double mean = Statistics.mean(values);

			means[i] = mean;
		}

		return means;
	}

	public static double[] rowMeans(DoubleMatrix m) {
		double[] means = new double[m.mRows];

		for (int i = 0; i < m.mRows; ++i) {
			double[] values = new double[m.mCols];

			int index = i * m.mCols;

			for (int j = 0; j < m.mCols; ++j) {
				values[j] = m.getValue(index);

				++index;
			}

			double mean = Statistics.mean(values);

			means[i] = mean;
		}

		return means;
	}

	/**
	 * Column rows.
	 *
	 * @param m the m
	 * @return the double[]
	 */
	public static double[] columnRows(DoubleMatrix m) {
		int r = m.getRowCount();
		int c = m.getColumnCount();

		double[] means = new double[r];

		for (int i = 0; i < r; ++i) {
			double[] values = new double[c];

			int index = i * c;

			for (int j = 0; j < r; ++j) {
				values[j] = m.mData[index];

				++index;
			}

			double mean = Statistics.mean(values);

			means[i] = mean;
		}

		return means;
	}

	/**
	 * Max row sum.
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
	 * Max col sum.
	 *
	 * @param m the m
	 * @return the double
	 */
	public static double maxColSum(AnnotationMatrix m) {
		double max = Double.MIN_VALUE;

		for (int i = 0; i < m.getColumnCount(); ++i) {
			double sum = 0;

			for (int j = 0; j < m.getRowCount(); ++j) {
				double v = m.getValue(j, i);

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
	 * T test.
	 *
	 * @param m the m
	 * @param g1 the g 1
	 * @param g2 the g 2
	 * @param equalVariance the equal variance
	 * @return the list
	 */
	public static List<Double> tTest(AnnotationMatrix m, 
			MatrixGroup g1,
			MatrixGroup g2,
			boolean equalVariance) {
		List<Double> pvalues = new ArrayList<Double>(m.getRowCount());

		List<Integer> g11 = MatrixGroup.findColumnIndices(m, g1);
		List<Integer> g22 = MatrixGroup.findColumnIndices(m, g2);

		for (int i = 0; i < m.getRowCount(); ++i) {
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
	 * Log fold change.
	 *
	 * @param matrix the matrix
	 * @param g1 the g 1
	 * @param g2 the g 2
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

	//
	// Apply functions
	//
	
	public static void rowApply(AnnotationMatrix m, DimNumFunction f) {
		rowApply(m.getInnerMatrix(), f);
	}

	public static void rowApply(Matrix m, DimNumFunction f) {
		if (m instanceof DoubleMatrix) {
			rowApply((DoubleMatrix)m, f);
		} else if (m instanceof IndexableMatrix) {
			rowApply((IndexableMatrix)m, f);
		} else {
			int rows = m.getRowCount();
			int cols = m.getColumnCount();

			double[] ret = new double[cols];

			for (int i = 0; i < rows; ++i) {
				for (int j = 0; j < cols; ++j) {
					ret[j] = m.getValue(i, j);
				}

				f.apply(ret, 0, cols, i);
			}
		}
	}
	
	public static void rowApply(AnnotationMatrix m, DimNumFunction f, int row) {
		rowApply(m.getInnerMatrix(), f, row);
	}

	public static void rowApply(Matrix m, DimNumFunction f, int row) {
		if (m instanceof DoubleMatrix) {
			rowApply((DoubleMatrix)m, f, row);
		} else if (m instanceof IndexableMatrix) {
			rowApply((IndexableMatrix)m, f, row);
		} else {
			int cols = m.getColumnCount();

			double[] ret = new double[cols];

			for (int j = 0; j < cols; ++j) {
				ret[j] = m.getValue(row, j);
			}

			f.apply(ret, 0, cols, row);
		}
	}

	public static void colApply(AnnotationMatrix m, DimNumFunction f) {
		colApply(m.getInnerMatrix(), f);
	}
	
	/**
	 * Apply a function to the values of the rows in each column of a matrix.
	 * Whilst you can write for loops to go over the matrix, the colApply
	 * function is optimized for the underlying data structure of a matrix
	 * so it can iterate more quickly.
	 * 
	 * @param f 	A function to apply to each column.
	 */
	public static void colApply(Matrix m, DimNumFunction f) {
		if (m instanceof DoubleMatrix) {
			colApply((DoubleMatrix)m, f);
		} else {
			int rows = m.getRowCount();
			int cols = m.getColumnCount();

			double[] ret = new double[rows];

			for (int i = 0; i < cols; ++i) {
				for (int j = 0; j < rows; ++j) {
					ret[j] = m.getValue(j, i);
				}

				f.apply(ret, 0, rows, i);
			}
		}
	}
	
	public static void colApply(AnnotationMatrix m, DimNumFunction f, int col) {
		colApply(m.getInnerMatrix(), f, col);
	}

	public static void colApply(Matrix m, DimNumFunction f, int col) {
		if (m instanceof DoubleMatrix) {
			colApply((DoubleMatrix)m, f, col);
		} else {
			int rows = m.getRowCount();

			double[] ret = new double[rows];

			for (int j = 0; j < rows; ++j) {
				ret[j] = m.getValue(j, col);
			}

			f.apply(ret, 0, rows, col);
		}
	}

	public static void rowApply(IndexMatrix m, DimNumFunction f) {
		int r = m.getRowCount();
		int c = m.getColumnCount();

		double[] ret = new double[c];

		for (int i = 0; i < r; ++i) {
			int i1 = m.getIndex(i, 0);

			for (int j = 0; j < c; ++j) {
				ret[j] = m.getValue(i1++);
			}

			f.apply(ret, 0, c, i);
		}
	}

	public static void rowApply(IndexMatrix m, DimNumFunction f, int row) {
		int c = m.getColumnCount();

		double[] ret = new double[c];

		int i1 = m.getIndex(row, 0);

		for (int j = 0; j < c; ++j) {
			ret[j] = m.getValue(i1++);
		}

		f.apply(ret, 0, c, row);
	}

	public static void rowApply(DoubleMatrix m, DimNumFunction f) {
		int offset = 0;

		for (int i = 0; i < m.mRows; ++i) {
			f.apply(m.mData, offset, m.mCols, i);

			offset += m.mCols;
		}
	}

	public static void rowApply(DoubleMatrix m, DimNumFunction f, int row) {
		int offset = row * m.mCols;

		f.apply(m.mData, offset, m.mCols, row);
	}

	public static void colApply(DoubleMatrix m, DimNumFunction f) {
		int offset = 0;

		double[] ret = new double[m.mRows];

		for (int i = 0; i < m.mCols; ++i) {
			offset = i;

			for (int j = 0; j < m.mRows; ++j) {
				ret[j] = m.mData[offset];

				offset += m.mCols;
			}

			f.apply(ret, 0, m.mRows, i);
		}
	}

	public static void colApply(DoubleMatrix m, DimNumFunction f, int col) {
		double[] ret = new double[m.mRows];

		int offset = col;

		for (int i = 0; i < m.mRows; ++i) {
			ret[i] = m.mData[offset];

			offset += m.mCols;
		}

		f.apply(ret, 0, m.mRows, col);
	}
}
