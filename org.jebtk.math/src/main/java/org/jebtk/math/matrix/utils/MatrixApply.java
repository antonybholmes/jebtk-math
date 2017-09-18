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

package org.jebtk.math.matrix.utils;

import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.math.matrix.DoubleMatrix;
import org.jebtk.math.matrix.IndexMatrix;
import org.jebtk.math.matrix.IndexableMatrix;
import org.jebtk.math.matrix.Matrix;

// TODO: Auto-generated Javadoc
/**
 * The Class MatrixUtils.
 */
public class MatrixApply {

	/**
	 * Instantiates a new matrix utils.
	 */
	private MatrixApply() {

	}

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
