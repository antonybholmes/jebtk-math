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

import org.jebtk.math.matrix.AnnotatableMatrix;
import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.math.matrix.DoubleMatrix;
import org.jebtk.math.matrix.IndexMatrix;
import org.jebtk.math.matrix.IntMatrix;
import org.jebtk.math.matrix.Matrix;
import org.jebtk.math.matrix.MixedMatrix;

// TODO: Auto-generated Javadoc
/**
 * The Class MatrixUtils.
 */
public class Transpose {

	/**
	 * Instantiates a new matrix utils.
	 */
	private Transpose() {

	}

	public Matrix transpose(final AnnotationMatrix m) {
		// Transpose the main matrix
		Matrix innerM = transpose(m.getInnerMatrix());

		AnnotationMatrix ret = new AnnotatableMatrix(innerM);

		// The first name is the row-name, which must be swapped for the
		// column name so we only copy the annotation for names(1, end) 
		// verbatim. The same is true for the columns
		//ret.setColumnNames(getRowNames());
		//ret.setRowNames(getColumnNames());

		for (String name : m.getRowAnnotationNames()) { //CollectionUtils.tail(getRowAnnotationNames())) {
			ret.setColumnAnnotations(name, m.getRowAnnotations(name));
		}

		for (String name : m.getColumnAnnotationNames()) { //CollectionUtils.tail(getColumnAnnotationNames())) {
			ret.setRowAnnotations(name, m.getColumnAnnotations(name));
		}

		return ret;
	}


	public Matrix transpose(final Matrix m) {
		if (m instanceof DoubleMatrix) {
			return transpose((DoubleMatrix)m);
		} else if (m instanceof IntMatrix) {
			return transpose((IntMatrix)m);
		} else if (m instanceof IndexMatrix) {
			return transpose((IndexMatrix)m);
		} else {
			MixedMatrix ret = 
					MixedMatrix.createMixedMatrix(m.getColumnCount(), m.getRowCount());

			// Swap row and column indices. We use index lookup to reduce
			// the number of number of times indices must be looked up to
			// set cell elements.

			for (int i = 0; i < m.getRowCount(); ++i) {
				for (int j = 0; j < m.getColumnCount(); ++j) {
					ret.set(j, i, m.get(i, j));
				}
			}

			return ret;
		}
	}

	public static Matrix transpose(final IndexMatrix m) { 
		MixedMatrix ret = MixedMatrix.createMixedMatrix(m.mCols, m.mRows);

		int i2 = 0;
		int c = 0;

		for (int i = 0; i < m.getNumCells(); ++i) {
			// Each time we end a row, reset i2 back to the next column
			if (i % m.mCols == 0) {
				i2 = c++;
			}

			ret.set(i2, m.get(i));

			// Skip blocks
			i2 += m.mRows;
		}

		return ret;
	}

	public static Matrix transpose(final DoubleMatrix m) { 
		DoubleMatrix ret = DoubleMatrix.createDoubleMatrix(m.mCols, m.mRows);

		int i2 = 0;
		int c = 0;

		for (int i = 0; i < m.mData.length; ++i) {
			// Each time we end a row, reset i2 back to the next column
			if (i % m.mCols == 0) {
				i2 = c++;
			}

			ret.mData[i2] = m.mData[i];

			// Skip blocks
			i2 += m.mRows;
		}

		return ret;
	}
	
	public static Matrix transpose(final IntMatrix m) { 
		IntMatrix ret = IntMatrix.createIntMatrix(m.mCols, m.mRows);

		int i2 = 0;
		int c = 0;

		for (int i = 0; i < m.mData.length; ++i) {
			// Each time we end a row, reset i2 back to the next column
			if (i % m.mCols == 0) {
				i2 = c++;
			}

			ret.mData[i2] = m.mData[i];

			// Skip blocks
			i2 += m.mRows;
		}

		return ret;
	}
}
