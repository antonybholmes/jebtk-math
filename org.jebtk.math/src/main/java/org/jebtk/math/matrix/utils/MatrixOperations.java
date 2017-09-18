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
package org.jebtk.math.matrix.utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jebtk.core.Indexed;
import org.jebtk.core.Mathematics;
import org.jebtk.core.collections.ArrayListCreator;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.collections.DefaultHashMap;
import org.jebtk.core.text.Join;
import org.jebtk.math.matrix.AnnotatableMatrix;
import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.math.matrix.CellType;
import org.jebtk.math.matrix.DoubleMatrix;
import org.jebtk.math.matrix.IntMatrix;
import org.jebtk.math.matrix.Matrix;
import org.jebtk.math.matrix.MatrixFunction;
import org.jebtk.math.matrix.MatrixGroup;
import org.jebtk.math.matrix.MixedMatrix;
import org.jebtk.math.matrix.StatMatrixFunction;
import org.jebtk.math.matrix.TextMatrix;
import org.jebtk.math.statistics.Statistics;
import org.jebtk.math.statistics.Stats;
import org.jebtk.math.statistics.TTest;
import org.jebtk.math.statistics.TwoSampleTest;

// TODO: Auto-generated Javadoc
/**
 * The class MatrixOperations.
 */
public class MatrixOperations {

	/**
	 * The Interface Function.
	 */
	private static interface Function {
		
		/**
		 * Apply.
		 *
		 * @param v the v
		 * @return the double
		 */
		public double apply(double v);
	}

	/**
	 * The Class PowerFunction.
	 */
	private static class PowerFunction implements Function {

		/** The m power. */
		private int mPower;

		/**
		 * Instantiates a new power function.
		 *
		 * @param power the power
		 */
		public PowerFunction(int power) {
			mPower = power;
		}

		/* (non-Javadoc)
		 * @see org.abh.common.math.matrix.MatrixOperations.Function#apply(double)
		 */
		@Override
		public double apply(double v) {
			return Math.pow(mPower, v);
		}
	}

	/**
	 * The Class LogFunction.
	 */
	private static class LogFunction implements MatrixFunction {

		/** The m base. */
		private int mBase;

		/**
		 * Instantiates a new log function.
		 *
		 * @param base the base
		 */
		public LogFunction(int base) {
			mBase = base;
		}

		/* (non-Javadoc)
		 * @see org.abh.common.math.matrix.MatrixOperations.Function#apply(double)
		 */
		@Override
		public double apply(int row, int col, double v) {
			return Mathematics.log(v, mBase);
		}
	}
	
	private static class MinThresholdFunction implements MatrixFunction {

		/** The m X. */
		private double mX;

		/**
		 * Instantiates a new mult function.
		 *
		 * @param x the x
		 */
		public MinThresholdFunction(double x) {
			mX = x;
		}

		/* (non-Javadoc)
		 * @see org.abh.common.math.matrix.MatrixOperations.Function#apply(double)
		 */
		@Override
		public double apply(int row, int col, double v) {
			return Math.max(v, mX);
		}
	}
	
	private static class MinFunction extends StatMatrixFunction {

		@Override
		public void init() {
			mStat = Double.MAX_VALUE;
		}

		/* (non-Javadoc)
		 * @see org.abh.common.math.matrix.MatrixOperations.Function#apply(double)
		 */
		@Override
		public void apply(int row, int col, double v) {
			if (Matrix.isValidMatrixNum(v)) {
				if (v < mStat) {
					mStat = v;
				}
			}
		}
	}
	
	private static class MaxFunction extends StatMatrixFunction {

		@Override
		public void init() {
			mStat = Double.MIN_VALUE;
		}

		/* (non-Javadoc)
		 * @see org.abh.common.math.matrix.MatrixOperations.Function#apply(double)
		 */
		@Override
		public void apply(int row, int col, double v) {
			if (Matrix.isValidMatrixNum(v)) {
				if (v > mStat) {
					mStat = v;
				}
			}
		}
	}
	
	private static class ThresholdFunction implements MatrixFunction {

		private double mMin;
		private double mMax;

		/**
		 * Instantiates a new mult function.
		 *
		 * @param x the x
		 */
		public ThresholdFunction(double min, double max) {
			mMin = min;
			mMax = max;
		}

		/* (non-Javadoc)
		 * @see org.abh.common.math.matrix.MatrixOperations.Function#apply(double)
		 */
		@Override
		public double apply(int row, int col, double v) {
			return Mathematics.bound(v, mMin, mMax);
		}
	}

	/** The ln function. */
	private static MatrixFunction LN_FUNCTION = new MatrixFunction() {
		@Override public double apply(int row, int col, double v) {
			return Math.log(v);
		}};
		
	private static StatMatrixFunction MIN_FUNCTION = new MinFunction();
	
	private static StatMatrixFunction MAX_FUNCTION = new MaxFunction();

	/**
	 * Instantiates a new matrix operations.
	 */
	private MatrixOperations() {
		// Do nothing
	}

	//
	// Log operations
	//

	/**
	 * Ln.
	 *
	 * @param m the m
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix ln(final AnnotationMatrix m) {
		return new AnnotatableMatrix(m, ln(m.getInnerMatrix()));
	}

	/**
	 * Returns the natural log of a mixed matrix. If the matrix contains
	 * text, the text will be preserved. For speed it is best to run this
	 * on a mixed matrix containing only numbers
	 *
	 * @param m the m
	 * @return the matrix
	 */
	public static Matrix ln(Matrix m) {
		return m.applied(LN_FUNCTION);
	}

	/**
	 * Log 2.
	 *
	 * @param m the m
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix log2(final AnnotationMatrix m) {
		return new AnnotatableMatrix(m, log2(m.getInnerMatrix()));
	}

	/**
	 * Return a log2 version of a matrix.
	 *
	 * @param m the m
	 * @return the annotation matrix
	 */
	public static Matrix log2(final Matrix m) {
		return log(m, 2);
	}

	/**
	 * Log 10.
	 *
	 * @param m the m
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix log10(final AnnotationMatrix m) {
		return new AnnotatableMatrix(m, log10(m.getInnerMatrix()));
	}

	/**
	 * Log 10.
	 *
	 * @param m the m
	 * @return the matrix
	 */
	public static Matrix log10(final Matrix m) {
		return log(m, 10);
	}

	/**
	 * Log.
	 *
	 * @param m the m
	 * @param base the base
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix log(final AnnotationMatrix m, int base) {
		return new AnnotatableMatrix(m, log(m.getInnerMatrix(), base));
	}

	/**
	 * Return a copy of the matrix log transformed in a given base.
	 *
	 * @param m the m
	 * @param base the base
	 * @return the matrix
	 */
	public static Matrix log(Matrix m, int base) {
		return m.applied(new LogFunction(base));
	}

	//
	// Threshold
	//

	/**
	 * Min threshold.
	 *
	 * @param m the m
	 * @param min the min
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix min(final AnnotationMatrix m, double min) {
		return new AnnotatableMatrix(m, min(m.getInnerMatrix(), min));
	}

	/**
	 * Set all values below min to min.
	 *
	 * @param m the m
	 * @param min the min
	 * @return the annotation matrix
	 */
	public static Matrix min(Matrix m, double min) {
		return m.applied(new MinThresholdFunction(min));
	}

	/**
	 * Bound matrix values between a minimum and maximum.
	 *
	 * @param m the m
	 * @param min the min
	 * @param max the max
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix threshold(final AnnotationMatrix m, 
			double min,
			double max) {
		return new AnnotatableMatrix(m, threshold(m.getInnerMatrix(), min, max));
	}

	/**
	 * Min max threshold.
	 *
	 * @param m the m
	 * @param min the min
	 * @param max the max
	 * @return the annotation matrix
	 */
	public static Matrix threshold(final Matrix m, 
			double min,
			double max) {
		return m.applied(new ThresholdFunction(min, max));
	}

	//
	// Min/Max
	//


	/**
	 * Min.
	 *
	 * @param m the m
	 * @return the double
	 */
	public static double min(final AnnotationMatrix m) {
		return min(m.getInnerMatrix());
	}

	/**
	 * Returns the min value in a matrix.
	 *
	 * @param m the m
	 * @return the double
	 */
	public static double min(final Matrix m) {
		m.stat(MIN_FUNCTION);
		
		return MIN_FUNCTION.getStat();
	}

	/**
	 * Max.
	 *
	 * @param m the m
	 * @return the double
	 */
	public static double max(final AnnotationMatrix m) {
		return max(m.getInnerMatrix());
	}

	public static double max(final Matrix m) {
		m.stat(MAX_FUNCTION);
		
		return MAX_FUNCTION.getStat();
	}

	//
	// Statistics
	//

	/**
	 * Zscore.
	 *
	 * @param m the m
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix zscore(final AnnotationMatrix m) {
		return new AnnotatableMatrix(m, zscore(m.getInnerMatrix()));
	}

	/**
	 * Zscore.
	 *
	 * @param m the m
	 * @return the numerical matrix
	 */
	public static Matrix zscore(Matrix m) {
		if (m instanceof DoubleMatrix) {
			return zscore((DoubleMatrix)m);
		} else if (m instanceof MixedMatrix) {
			return zscore((MixedMatrix)m);
		} else {
			boolean keepText = Matrix.cellTypes(m).contains(CellType.TEXT);

			Matrix ret;

			if (keepText) {
				ret = MixedMatrix.createMixedMatrix(m);
			} else {
				ret = DoubleMatrix.createDoubleMatrix(m);
			}

			List<Double> values = new ArrayList<Double>(m.getNumCells());

			for (int i = 0; i < m.getRowCount(); ++i) {
				for (int j = 0; j < m.getColumnCount(); ++j) {
					double v = m.getValue(i, j);

					if (Mathematics.isValidNumber(v)) {
						values.add(v);
					}
				}
			}

			double mean = Statistics.mean(values);
			double sd = Statistics.popStdDev(values);

			for (int i = 0; i < m.getRowCount(); ++i) {
				for (int j = 0; j < m.getColumnCount(); ++j) {
					if (m.getCellType(i, j) == CellType.NUMBER) {

						double v = m.getValue(i, j);

						if (Mathematics.isValidNumber(v)) {
							// If the sd is zero then we cannot adjust so
							if (sd != 0) {
								v = (v - mean) / sd;

								ret.set(i, j, v);
							} else {
								ret.set(i, j, 0);
							}
						} else {
							ret.set(i, j, 0);
						}
					} else {
						if (keepText) {
							ret.set(i, j, m.getText(i, j));
						}
					}
				}
			}

			return ret;
		}
	}

	/**
	 * Zscore.
	 *
	 * @param m the m
	 * @return the matrix
	 */
	public static Matrix zscore(MixedMatrix m) { 
		boolean keepText = Matrix.cellTypes(m).contains(CellType.TEXT);

		if (keepText) {
			MixedMatrix ret = MixedMatrix.createMixedMatrix(m);

			List<Double> values = new ArrayList<Double>(m.getNumCells());

			for (int i = 0; i < m.mData.length; ++i) {
				if (m.mCellType[i] == CellType.NUMBER) {
					double v = ((Number)m.mData[i]).doubleValue();

					if (Matrix.isValidMatrixNum(v)) {
						values.add(v);
					}
				}
			}

			double mean = Statistics.mean(values);
			double sd = Statistics.popStdDev(values);

			for (int i = 0; i < m.mData.length; ++i) {
				Object v = m.mData[i];

				if (m.mCellType[i] == CellType.NUMBER) {
					if (sd != 0) {
						ret.set(i, (((Number)v).doubleValue() - mean) / sd);
					} else {
						ret.set(i, 0);
					}
				} else {
					ret.set(i, v.toString());
				}
			}

			return ret;
		} else {
			List<Double> values = new ArrayList<Double>(m.getNumCells());

			for (int i = 0; i < m.mData.length; ++i) {
				double v = ((Number)m.mData[i]).doubleValue();

				if (Matrix.isValidMatrixNum(v)) {
					values.add(v);
				}
			}

			double mean = Statistics.mean(values);
			double sd = Statistics.popStdDev(values);

			DoubleMatrix ret = DoubleMatrix.createDoubleMatrix(m);

			for (int i = 0; i < m.mData.length; ++i) {
				double v = ((Number)m.mData[i]).doubleValue();

				if (Matrix.isValidMatrixNum(v)) {
					if (sd != 0) {
						ret.mData[i] = (v - mean) / sd;
					} else {
						ret.mData[i] = 0;
					}
				} else {
					ret.mData[i] = 0;
				}
			}

			return ret;
		}
	}

	/**
	 * Zscore.
	 *
	 * @param m the m
	 * @return the double matrix
	 */
	public static DoubleMatrix zscore(DoubleMatrix m) { 
		DoubleMatrix ret = DoubleMatrix.createDoubleMatrix(m);

		double mean = Statistics.mean(m.mData);
		double sd = Statistics.popStdDev(m.mData);

		if (sd != 0) {
			for (int i = 0; i < m.mData.length; ++i) {
				double v = m.mData[i];

				if (Matrix.isValidMatrixNum(v)) {
					ret.mData[i] = (v - mean) / sd;
				} else {
					ret.mData[i] = 0;
				}
			}
		}

		return ret;
	}

	/**
	 * Row zscore.
	 *
	 * @param m the m
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix rowZscore(final AnnotationMatrix m) {
		return new AnnotatableMatrix(m, rowZscore(m.getInnerMatrix()));
	}

	/**
	 * Row zscore.
	 *
	 * @param m the m
	 * @return the numerical matrix
	 */
	public static DoubleMatrix rowZscore(Matrix m) {

		int r = m.getRowCount();
		int c = m.getColumnCount();

		double[] mean = new double[r];
		double[] sd = new double[r];

		double[] values = new double[c];

		for (int i = 0; i < r; ++i) {
			for (int j = 0; j < c; ++j) {
				values[j] = m.getValue(i, j);
			}

			mean[i] = Statistics.mean(values);
			sd[i] = Statistics.popStdDev(values); // Statistics.sampleStandardDeviation(v);

			//if (sd[i])
		}

		DoubleMatrix zm = DoubleMatrix.createDoubleMatrix(m);

		for (int i = 0; i < r; ++i) {
			for (int j = 0; j < c; ++j) {
				double v = 0;

				if (sd[i] != 0) {
					v = (m.getValue(i, j) - mean[i]) / sd[i];
				}

				//if (Mathematics.isValidNumber(v)) {
				zm.set(i, j, v);
				//}
			}
		}

		return zm;
	}

	/**
	 * Column zscore.
	 *
	 * @param m the m
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix columnZscore(final AnnotationMatrix m) {
		return new AnnotatableMatrix(m, columnZscore(m.getInnerMatrix()));
	}

	/**
	 * Column zscore.
	 *
	 * @param m the m
	 * @return the numerical matrix
	 */
	public static DoubleMatrix columnZscore(Matrix m) {

		int r = m.getRowCount();
		int c = m.getColumnCount();

		double[] mean = new double[c];
		double[] sd = new double[c];

		double[] values = new double[r];

		for (int i = 0; i < c; ++i) {
			for (int j = 0; j < r; ++j) {
				values[j] = m.getValue(j, i);
			}

			mean[i] = Statistics.mean(values);
			sd[i] = Statistics.popStdDev(values); // Statistics.sampleStandardDeviation(v);
		}

		DoubleMatrix zm = DoubleMatrix.createDoubleMatrix(m);

		for (int i = 0; i < c; ++i) {
			for (int j = 0; j < r; ++j) {
				double v = 0;
				
				if (sd[i] != 0) {
					v = (m.getValue(j, i) - mean[i]) / sd[i];
				}
				
				zm.set(j, i, v);
			}
		}

		return zm;
	}


	/**
	 * Group z score.
	 *
	 * @param <X> the generic type
	 * @param m the m
	 * @param groups the groups
	 * @return the numerical matrix
	 */
	public static <X extends MatrixGroup> AnnotationMatrix groupZScore(AnnotationMatrix m, 
			List<X> groups) {
		List<List<Integer>> newGroups = 
				MatrixGroup.findColumnIndices(m, groups);

		Matrix ret = groupZScore(m.getInnerMatrix(), newGroups);

		return new AnnotatableMatrix(m, ret);
	}

	/**
	 * Group Z score.
	 *
	 * @param <X> the generic type
	 * @param m the m
	 * @param newGroups the new groups
	 * @return the matrix
	 */
	public static <X extends MatrixGroup> Matrix groupZScore(Matrix m, 
			List<List<Integer>> newGroups) {

		double[] means = new double[m.getRowCount()];
		double[] sds = new double[m.getRowCount()];

		for (int i = 0; i < m.getRowCount(); ++i) {
			double mean = 0;
			double s = 0;

			double groupCount = 0;

			for (List<Integer> group : newGroups) {
				if (group.size() == 0) {
					continue;
				}

				List<Double> d1 = new ArrayList<Double>(group.size());

				for (int c : group) {
					d1.add(m.getValue(i, c));
				}

				mean += Statistics.mean(d1);
				s += Statistics.popStdDev(d1); // sampleStandardDeviation

				++groupCount;
			}

			means[i] = mean / groupCount;
			sds[i] = s / groupCount;
		}

		DoubleMatrix zm = DoubleMatrix.createDoubleMatrix(m);

		for (int i = 0; i < m.getRowCount(); ++i) {
			for (int j = 0; j < m.getColumnCount(); ++j) {
				double v = 0;

				if (sds[i] != 0) {
					v = (m.getValue(i, j) - means[i]) / sds[i];
				}
				
				zm.set(i, j, v);
			}
		}

		return zm;
	}


	/**
	 * Quantile normalize.
	 *
	 * @param m the m
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix quantileNormalize(final AnnotationMatrix m) {
		return new AnnotatableMatrix(m, quantileNormalize(m.getInnerMatrix()));
	}

	/**
	 * Quantile normalize.
	 *
	 * @param m the m
	 * @return the numerical matrix
	 */
	public static DoubleMatrix quantileNormalize(Matrix m) {

		List<List<Indexed<Integer, Double>>> indexedColumns =
				new ArrayList<List<Indexed<Integer, Double>>>(m.getColumnCount());

		for (int i = 0; i < m.getColumnCount(); ++i) {
			double[] values = m.columnAsDouble(i);

			List<Indexed<Integer, Double>> sorted = 
					Indexed.intIndex(values);

			Collections.sort(sorted);

			indexedColumns.add(sorted);
		}

		List<Double> rowMeans = new ArrayList<Double>(m.getRowCount());

		// get the mean of the values of each row when each column is sorted

		for (int i = 0; i < m.getRowCount(); ++i) {
			List<Double> values = new ArrayList<Double>(m.getColumnCount());

			for (int j = 0; j < m.getColumnCount(); ++j) {
				values.add(indexedColumns.get(j).get(i).getValue());
			}

			rowMeans.add(Statistics.mean(values));
		}

		List<Double> rowMeanRanks = Statistics.tiedRank(rowMeans);

		// now we need a mapping between an index and
		// the column rank of that index

		DoubleMatrix zq = 
				new DoubleMatrix(m.getRowCount(), m.getColumnCount());

		for (int column = 0; column < m.getColumnCount(); ++column) {
			double[] values = m.columnAsDouble(column);

			// the ranks of the values we have
			double[] valueRanks = Mathematics.tiedRank(values);


			// interpolate from these ranks the new value using the row ranks
			double[] interpolatedValues = 
					Mathematics.linearInterpolation(rowMeanRanks, rowMeans, valueRanks);

			for (int row = 0; row < interpolatedValues.length; ++row) {
				zq.set(row, column, interpolatedValues[row]);
			}
		}

		return zq;
	}

	/**
	 * Normalize.
	 *
	 * @param m the m
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix normalize(final AnnotationMatrix m) {
		return new AnnotatableMatrix(m, normalize(m.getInnerMatrix()));
	}

	/**
	 * Scales a matrix so the values are in the range (0 - scale).
	 *
	 * @param m the m
	 * @return the annotation matrix
	 */
	public static DoubleMatrix normalize(final Matrix m) {
		double min = min(m);
		double max = max(m);

		return normalize(m, min, max);
	}

	/**
	 * Normalize a matrix so values are between 0 and 1.
	 *
	 * @param m the m
	 * @param min the min
	 * @param max the max
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix normalize(final AnnotationMatrix m,
			double min,
			double max) {
		return new AnnotatableMatrix(m, normalize(m.getInnerMatrix(), min, max));
	}

	/**
	 * Normalize.
	 *
	 * @param m the m
	 * @param min the min
	 * @param max the max
	 * @return the annotation matrix
	 */
	public static DoubleMatrix normalize(final Matrix m, 
			double min,
			double max) {
		if (m instanceof DoubleMatrix) {
			return normalize((DoubleMatrix)m, min, max);
		}

		double range = max - min;

		DoubleMatrix ret = new DoubleMatrix(m.getRowCount(), m.getColumnCount());

		for (int i = 0; i < m.getRowCount(); ++i) {
			for (int j = 0; j < m.getColumnCount(); ++j) {
				double v = m.getValue(i, j);

				if (Mathematics.isValidNumber(v)) {
					ret.set(i, j, normalize(v, min, range));
				}
			}
		}

		return ret;
	}

	/**
	 * Normalize.
	 *
	 * @param m the m
	 * @param min the min
	 * @param max the max
	 * @return the double matrix
	 */
	public static DoubleMatrix normalize(final DoubleMatrix m,
			double min,
			double max) {
		double range = max - min;

		DoubleMatrix ret = new DoubleMatrix(m.getRowCount(), m.getColumnCount());

		for (int i = 0; i < m.mData.length; ++i) {
			double v = m.mData[i];

			if (Mathematics.isValidNumber(v)) {
				ret.mData[i] = normalize(v, min, range);
			}
		}

		return ret;
	}

	/**
	 * Normalize.
	 *
	 * @param v the v
	 * @param min the min
	 * @param range the range
	 * @return the double
	 */
	private static double normalize(double v,
			double min,
			double range) {
		return Mathematics.bound((v - min) / range, 0.0, 1.0);
		
		//if (range != 0) {
		//	return Mathematics.bound((v - min) / range, 0.0, 1.0);
		//} else {
		//	return 0;
		//}
	}




	/**
	 * Collapse max std dev.
	 *
	 * @param m the m
	 * @param rowAnnotation the row annotation
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix collapseMaxStdDev(AnnotationMatrix m, 
			String rowAnnotation) {
		List<Integer> rows = maxStdev(m, rowAnnotation);

		return AnnotatableMatrix.copyRows(m, rows);
	}

	/**
	 * Max stdev.
	 *
	 * @param m the m
	 * @param rowAnnotation the row annotation
	 * @return the list
	 */
	public static List<Integer> maxStdev(AnnotationMatrix m, 
			String rowAnnotation) {

		Map<String, Integer> maxRow = new HashMap<String, Integer>();
		Map<String, Double> maxStd = new HashMap<String, Double>();

		for (int i = 0; i < m.getRowCount(); ++i) {
			String id = m.getRowAnnotationText(rowAnnotation, i);

			double std = Statistics.popStdDev(m.rowAsDouble(i));

			if (maxRow.containsKey(id)) {
				if (std > maxStd.get(id)) {
					maxStd.put(id, std);
					maxRow.put(id, i);
				}
			} else {
				maxStd.put(id, std);
				maxRow.put(id, i);
			}
		}

		return CollectionUtils.sort(maxRow.values());
	}

	/**
	 * Collapse max mean.
	 *
	 * @param m the m
	 * @param rowAnnotation the row annotation
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix collapseMaxMean(AnnotationMatrix m, 
			String rowAnnotation) {
		
		Map<Integer, List<Integer>> rowToRows = 
				DefaultHashMap.create(new ArrayListCreator<Integer>());
		
		List<Integer> rows = maxMean(m, rowAnnotation, rowToRows);

		AnnotationMatrix ret = AnnotatableMatrix.copyRows(m, rows);
		
		joinAnnotations(m,rowToRows, rowAnnotation, ret);
		
		return ret;
	}
	
	/**
	 * Collapse row annotations by joining fields with a semi colon. This
	 * method requires a row map mapping the original row index to the
	 * group of original row indexes it was grouped with during a collapse.
	 * This is used to extract all of the field values that need to be
	 * collapsed from the original matrix and putting them into the new
	 * matrix
	 *
	 * @param m the m
	 * @param rowToRows the row to rows
	 * @param rowAnnotation 	The row annotation that was used for joining.
	 * 							This will not be joined
	 * @param ret the ret
	 */
	private static void joinAnnotations(final AnnotationMatrix m,
			Map<Integer, List<Integer>> rowToRows,
			String rowAnnotation,
			AnnotationMatrix ret) {
		List<String> names = m.getRowAnnotationNames();
		
		// Keep track of the current row in the new matrix we are editing
		int r = 0;
		
		Join join = Join.onSemiColon();
		
		for (int i = 0; i < m.getRowCount(); ++i) {
			// i is the index in the old matrix
			
			// Skip if this row was not collapsed on
			if (!rowToRows.containsKey(i)) {
				continue;
			}
			
			List<Integer> rows = rowToRows.get(i);
			
			for (String name : names) {
				// For each row annotation field, get the values from each
				// row in the group block and concatenate them
				
				// Do not collapse values on the field that was used for
				// the collapse as its value by definition must be unique
				// and not a concatenation.
				if (name.equals(rowAnnotation)) {
					continue;
				}
				
				List<String> annotations = new ArrayList<String>(rows.size());
				
				for (int row : rows) {
					annotations.add(m.getRowAnnotationText(name, row));
				}
				
				ret.setRowAnnotation(name, r, join.values(annotations).toString());
			}
			
			++r;
		}
	}

	/**
	 * Groups rows in a matrix by id, finds the within group row with the
	 * maximum mean and returns the list of max mean rows.
	 *
	 * @param m 			The matrix to collapse.
	 * @param rowAnnotation Which column to collapse on.
	 * @param rowToRows		A mapping between the row to keep and the
	 * 						rows it belonged to.
	 * @return the list
	 */
	public static List<Integer> maxMean(final AnnotationMatrix m, 
			final String rowAnnotation,
			Map<Integer, List<Integer>> rowToRows) {

		Map<String, Integer> maxRow = new HashMap<String, Integer>();
		Map<String, Double> maxMean = new HashMap<String, Double>();

		for (int i = 0; i < m.getRowCount(); ++i) {
			String id = m.getRowAnnotationText(rowAnnotation, i);

			double std = Statistics.mean(m.rowAsDouble(i));

			if (maxRow.containsKey(id)) {
				if (std > maxMean.get(id)) {
					maxMean.put(id, std);
					maxRow.put(id, i);
				}
			} else {
				maxMean.put(id, std);
				maxRow.put(id, i);
			}
		}
		
		for (int i = 0; i < m.getRowCount(); ++i) {
			String id = m.getRowAnnotationText(rowAnnotation, i);
			
			if (maxRow.containsKey(id)) {
				int index = maxRow.get(id);
				
				if (!rowToRows.containsKey(index)) {
					rowToRows.put(index, new ArrayList<Integer>());
				}
				
				rowToRows.get(index).add(i);
			}
		}

		return CollectionUtils.sort(maxRow.values());
	}

	/**
	 * Collapse max median.
	 *
	 * @param m the m
	 * @param rowAnnotation the row annotation
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix collapseMaxMedian(AnnotationMatrix m, 
			String rowAnnotation) {
		List<Integer> rows = maxMedian(m, rowAnnotation);

		return AnnotatableMatrix.copyRows(m, rows);
	}

	/**
	 * Max median.
	 *
	 * @param m the m
	 * @param rowAnnotation the row annotation
	 * @return the list
	 */
	public static List<Integer> maxMedian(AnnotationMatrix m, 
			String rowAnnotation) {

		Map<String, Integer> maxRow = new HashMap<String, Integer>();
		Map<String, Double> maxMedian = new HashMap<String, Double>();

		for (int i = 0; i < m.getRowCount(); ++i) {
			String id = m.getRowAnnotationText(rowAnnotation, i);

			double std = Statistics.median(m.rowAsDouble(i));

			if (maxRow.containsKey(id)) {
				if (std > maxMedian.get(id)) {
					maxMedian.put(id, std);
					maxRow.put(id, i);
				}
			} else {
				maxMedian.put(id, std);
				maxRow.put(id, i);
			}
		}

		return CollectionUtils.sort(maxRow.values());
	}

	/**
	 * Collapse max.
	 *
	 * @param m the m
	 * @param rowAnnotation the row annotation
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix collapseMax(AnnotationMatrix m, 
			String rowAnnotation) {
		List<Integer> rows = max(m, rowAnnotation);

		return AnnotatableMatrix.copyRows(m, rows);
	}

	/**
	 * Return the row with the greatest standard deviation
	 * for each id in the matrix.
	 *
	 * @param m the m
	 * @param rowAnnotation the row annotation
	 * @return the list
	 */
	public static List<Integer> max(AnnotationMatrix m, String rowAnnotation) {

		Map<String, Integer> maxRow = new HashMap<String, Integer>();
		Map<String, Double> maxMap = new HashMap<String, Double>();

		for (int i = 0; i < m.getRowCount(); ++i) {
			String id = m.getRowAnnotationText(rowAnnotation, i);

			double max = Mathematics.max(m.rowAsDouble(i));

			if (maxRow.containsKey(id)) {
				if (max > maxMap.get(id)) {
					maxMap.put(id, max);
					maxRow.put(id, i);
				}
			} else {
				maxMap.put(id, max);
				maxRow.put(id, i);
			}
		}

		return CollectionUtils.sort(maxRow.values());
	}

	/**
	 * Collapse t test.
	 *
	 * @param m the m
	 * @param rowAnnotation the row annotation
	 * @param group1 the group1
	 * @param group2 the group2
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix collapseTTest(AnnotationMatrix m, 
			String rowAnnotation,
			MatrixGroup group1,
			MatrixGroup group2) {
		List<Integer> rows = minTTest(m, 
				rowAnnotation, 
				group1, 
				group2);

		return AnnotatableMatrix.copyRows(m, rows);
	}

	/**
	 * Return the row with the greatest standard deviation
	 * for each id in the matrix.
	 *
	 * @param m the m
	 * @param rowAnnotation the row annotation
	 * @param g1 the g1
	 * @param g2 the g2
	 * @return the list
	 */
	public static List<Integer> minTTest(AnnotationMatrix m,
			String rowAnnotation,
			MatrixGroup g1,
			MatrixGroup g2) {

		List<Integer> g11 = MatrixGroup.findColumnIndices(m, g1);
		List<Integer> g21 = MatrixGroup.findColumnIndices(m, g2);

		Map<String, Integer> minRow = new HashMap<String, Integer>();
		Map<String, Double> maxP = new HashMap<String, Double>();

		for (int i = 0; i < m.getRowCount(); ++i) {
			String id = m.getRowAnnotationText(rowAnnotation, i);



			List<Double> p1 = new ArrayList<Double>();

			for (int c : g11) {
				p1.add(m.getValue(i, c));
			}

			List<Double> p2 = new ArrayList<Double>();

			for (int c : g21) {
				p2.add(m.getValue(i, c));
			}


			double p = TTest.twoTailUnequalVarianceTTest(p1, p2);

			//System.err.println(id + " " + p + " " + rowAnnotation);

			// Set strange values to zero
			if (Double.isNaN(p) || Double.isInfinite(p)) {
				p = 1;
			}

			if (minRow.containsKey(id)) {
				if (p < maxP.get(id)) {
					maxP.put(id, p);
					minRow.put(id, i);
				}
			} else {
				maxP.put(id, p);
				minRow.put(id, i);
			}
		}

		return CollectionUtils.sort(minRow.values());
	}

	/**
	 * Adds the t stat.
	 *
	 * @param m the m
	 * @param g1 the g1
	 * @param g2 the g2
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix addTStat(AnnotationMatrix m,
			MatrixGroup g1,
			MatrixGroup g2) {
		List<Integer> g11 = MatrixGroup.findColumnIndices(m, g1);
		List<Integer> g21 = MatrixGroup.findColumnIndices(m, g2);

		List<Double> tStats = new ArrayList<Double>(m.getRowCount());
		List<Double> absTStats = new ArrayList<Double>(m.getRowCount());

		for (int i = 0; i < m.getRowCount(); ++i) {
			List<Double> p1 = new ArrayList<Double>();

			for (int c : g11) {
				p1.add(m.getValue(i, c));
			}

			List<Double> p2 = new ArrayList<Double>();

			for (int c : g21) {
				p2.add(m.getValue(i, c));
			}


			double p = TTest.tStat(p1, p2);

			//System.err.println(id + " " + p + " " + rowAnnotation);

			// Set strange values to zero
			if (Double.isNaN(p) || Double.isInfinite(p)) {
				p = 1;
			}

			tStats.add(p);
			absTStats.add(Math.abs(p));
		}

		AnnotationMatrix ret = new AnnotatableMatrix(m);

		ret.setNumRowAnnotations("T-Stat", tStats);
		//ret.setRowAnnotation("Abs T-Stat", ArrayUtils.toObjects(absTStats));

		return ret;
	}

	/**
	 * Adds the median.
	 *
	 * @param m the m
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix addMedian(AnnotationMatrix m) {
		List<Double> values = new ArrayList<Double>(m.getRowCount());

		for (int i = 0; i < m.getRowCount(); ++i) {
			double v = new Stats(m.rowAsDouble(i)).median();

			//System.err.println("iqr " + iqr);

			values.add(v);
		}

		AnnotationMatrix ret = new AnnotatableMatrix(m);

		ret.setNumRowAnnotations("Median", values);

		return ret;
	}

	/**
	 * Adds the mean.
	 *
	 * @param m the m
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix addMean(AnnotationMatrix m) {
		List<Double> values = new ArrayList<Double>(m.getRowCount());

		for (int i = 0; i < m.getRowCount(); ++i) {
			double v = new Stats(m.rowAsDouble(i)).mean();

			values.add(v);
		}

		AnnotationMatrix ret = new AnnotatableMatrix(m);

		ret.setNumRowAnnotations("Mean", values);

		return ret;
	}

	/**
	 * Adds the IQR.
	 *
	 * @param m the m
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix addIQR(AnnotationMatrix m) {
		List<Double> iqrList = new ArrayList<Double>(m.getRowCount());

		for (int i = 0; i < m.getRowCount(); ++i) {
			double iqr = new Stats(m.rowAsDouble(i)).iqr();

			//System.err.println("iqr " + iqr);

			iqrList.add(iqr);
		}

		AnnotationMatrix ret = new AnnotatableMatrix(m);

		ret.setNumRowAnnotations("IQR", iqrList);

		return ret;
	}

	/**
	 * Adds the quart coeff disp.
	 *
	 * @param m the m
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix addQuartCoeffDisp(AnnotationMatrix m) {
		List<Double> iqrList = new ArrayList<Double>(m.getRowCount());

		for (int i = 0; i < m.getRowCount(); ++i) {
			double iqr = new Stats(m.rowAsDouble(i)).quartCoeffDisp();

			iqrList.add(iqr);
		}

		AnnotationMatrix ret = new AnnotatableMatrix(m);

		ret.setNumRowAnnotations("QuartCoeffDisp", iqrList);

		return ret;
	}

	/**
	 * Collapse max t stat.
	 *
	 * @param m the m
	 * @param rowAnnotation the row annotation
	 * @return the annotation matrix
	 * @throws ParseException the parse exception
	 */
	public static AnnotationMatrix collapseMaxTStat(AnnotationMatrix m, 
			String rowAnnotation) throws ParseException {
		return collapse(m, rowAnnotation, "T-Stat");
	}


	/**
	 * Collapse max IQR.
	 *
	 * @param m the m
	 * @param rowAnnotation the row annotation
	 * @return the annotation matrix
	 * @throws ParseException the parse exception
	 */
	public static AnnotationMatrix collapseMaxIQR(AnnotationMatrix m, 
			String rowAnnotation) throws ParseException {
		return collapse(m, rowAnnotation, "IQR");
	}

	/**
	 * Collapse max quart coeff disp.
	 *
	 * @param m the m
	 * @param rowAnnotation the row annotation
	 * @return the annotation matrix
	 * @throws ParseException the parse exception
	 */
	public static AnnotationMatrix collapseMaxQuartCoeffDisp(AnnotationMatrix m, 
			String rowAnnotation) throws ParseException {
		return collapse(m, rowAnnotation, "QuartCoeffDisp");
	}

	/**
	 * Collapse.
	 *
	 * @param m the m
	 * @param rowAnnotation the row annotation
	 * @param valuesName the values name
	 * @return the annotation matrix
	 * @throws ParseException the parse exception
	 */
	public static AnnotationMatrix collapse(AnnotationMatrix m, 
			String rowAnnotation,
			String valuesName) throws ParseException {

		Map<String, Integer> maxRows = new HashMap<String, Integer>();
		Map<String, Double> maxValueMap = new HashMap<String, Double>();

		double[] iqrList = m.getRowAnnotationValues(valuesName);

		for (int i = 0; i < m.getRowCount(); ++i) {
			String id = m.getRowAnnotationText(rowAnnotation, i);

			double iqr = Math.abs(iqrList[i]);

			if (maxRows.containsKey(id)) {
				if (iqr > maxValueMap.get(id)) {
					maxValueMap.put(id, iqr);
					maxRows.put(id, i);
				}
			} else {
				maxValueMap.put(id, iqr);
				maxRows.put(id, i);
			}
		}

		List<Integer> rows = CollectionUtils.sort(maxRows.values());

		return AnnotatableMatrix.copyRows(m, rows);
	}

	/**
	 * Collapse min.
	 *
	 * @param m the m
	 * @param rowAnnotation the row annotation
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix collapseMin(AnnotationMatrix m, 
			String rowAnnotation) {
		List<Integer> rows = min(m, rowAnnotation);

		return AnnotatableMatrix.copyRows(m, rows);
	}

	/**
	 * Return the row with the greatest standard deviation for each id in 
	 * the matrix.
	 *
	 * @param m the m
	 * @param rowAnnotation the row annotation
	 * @return the list
	 */
	public static List<Integer> min(AnnotationMatrix m, String rowAnnotation) {

		Map<String, Integer> minRow = new HashMap<String, Integer>();
		Map<String, Double> minMap = new HashMap<String, Double>();

		for (int i = 0; i < m.getRowCount(); ++i) {
			String id = m.getRowAnnotationText(rowAnnotation, i);

			double min = Mathematics.min(m.rowAsDouble(i));

			if (minRow.containsKey(id)) {
				if (min < minMap.get(id)) {
					minMap.put(id, min);
					minRow.put(id, i);
				}
			} else {
				minMap.put(id, min);
				minRow.put(id, i);
			}
		}

		return CollectionUtils.sort(minRow.values());
	}

	/**
	 * Filter rows.
	 *
	 * @param m the m
	 * @param rowAnnotation the row annotation
	 * @param regex the regex
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix filterRows(AnnotationMatrix m, 
			String rowAnnotation,
			String regex) {
		return filterRows(m, rowAnnotation, regex, true);
	}

	/**
	 * Filter rows.
	 *
	 * @param m the m
	 * @param rowAnnotation the row annotation
	 * @param regex the regex
	 * @param keep the keep
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix filterRows(AnnotationMatrix m, 
			String rowAnnotation,
			String regex,
			boolean keep) {


		List<String> annotations = m.getRowAnnotationText(rowAnnotation);

		List<Integer> rows = new ArrayList<Integer>();

		if (keep) {
			for (int i = 0; i < m.getRowCount(); ++i) {
				String id = annotations.get(i);

				if (id.matches(regex)) {
					rows.add(i);
				}
			}
		} else {
			for (int i = 0; i < m.getRowCount(); ++i) {
				String id = annotations.get(i);

				if (!id.matches(regex)) {
					rows.add(i);
				}
			}
		}

		return AnnotatableMatrix.copyRows(m, rows);
	}

	/**
	 * Filter rows by index.
	 *
	 * @param <V1> the generic type
	 * @param m the m
	 * @param indices the indices
	 * @return the annotation matrix
	 */
	public static <V1 extends Comparable<? super V1>> AnnotationMatrix filterRowsByIndex(AnnotationMatrix m, 
			List<Indexed<Integer, V1>> indices) {

		List<Integer> rows = Indexed.indices(indices);

		return AnnotatableMatrix.copyRows(m, rows);
	}

	/**
	 * Std dev filter.
	 *
	 * @param m the m
	 * @param min the min
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix stdDevFilter(AnnotationMatrix m, 
			double min) {
		double[] v = new double[m.getColumnCount()];

		double[] sd = new double[m.getRowCount()];

		for (int i = 0; i < m.getRowCount(); ++i) {
			for (int j = 0; j < m.getColumnCount(); ++j) {
				v[j] = m.getValue(i, j);
			}

			sd[i] = Statistics.popStdDev(v); //Statistics.sampleStandardDeviation(v);
		}

		List<Integer> indices = new ArrayList<Integer>();

		for (int i = 0; i < m.getRowCount(); ++i) {
			//System.err.println("filter sd " + i + " " + sd[i] + " " + min);

			if (sd[i] >= min) {
				indices.add(i);
			}
		}

		return AnnotatableMatrix.copyRows(m, indices);
	}

	/**
	 * Filter rows so they have a minimum mean value.
	 *
	 * @param m the m
	 * @param min the min
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix meanFilter(AnnotationMatrix m, 
			double min) {
		double[] v = new double[m.getColumnCount()];

		double[] sd = new double[m.getRowCount()];

		for (int i = 0; i < m.getRowCount(); ++i) {
			for (int j = 0; j < m.getColumnCount(); ++j) {
				v[j] = m.getValue(i, j);
			}

			sd[i] = Statistics.mean(v); //Statistics.sampleStandardDeviation(v);
		}

		List<Integer> indices = new ArrayList<Integer>();

		for (int i = 0; i < m.getRowCount(); ++i) {
			if (sd[i] >= min) {
				indices.add(i);
			}
		}

		return AnnotatableMatrix.copyRows(m, indices);
	}

	/**
	 * Row stdev.
	 *
	 * @param m the m
	 * @return the list
	 */
	public static List<Double> rowStdev(AnnotationMatrix m) {
		double[] v = new double[m.getColumnCount()];

		List<Double> sd = new ArrayList<Double>(m.getRowCount());

		for (int i = 0; i < m.getRowCount(); ++i) {
			for (int j = 0; j < m.getColumnCount(); ++j) {
				v[j] = m.getValue(i, j);
			}

			sd.add(Statistics.popStdDev(v)); //Statistics.sampleStandardDeviation(v);
		}

		return sd;
	}

	/**
	 * Min exp filter.
	 *
	 * @param m the m
	 * @param minExp the min exp
	 * @param minSamples the min samples
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix minExpFilter(AnnotationMatrix m, 
			double minExp, 
			int minSamples) {
		List<Integer> indices = new ArrayList<Integer>();

		for (int i = 0; i < m.getRowCount(); ++i) {
			int sampleSum = 0;

			for (int j = 0; j < m.getColumnCount(); ++j) {
				double v = m.getValue(i, j);

				if (Mathematics.isValidNumber(v)) {
					if (v >= minExp) {
						++sampleSum;
					}
				}
			}

			if (sampleSum >= minSamples) {
				indices.add(i);
			}
		}

		return AnnotatableMatrix.copyRows(m, indices);
	}

	/**
	 * Calculates the mean of a set of columns in a given row.
	 *
	 * @param m the m
	 * @param row the row
	 * @param columns the columns
	 * @return the double
	 */
	public static double mean(Matrix m, int row, List<Integer> columns) {
		List<Double> values = rowToList(m, row, columns);

		return Statistics.mean(values);
	}

	/**
	 * Mean.
	 *
	 * @param m the m
	 * @param row the row
	 * @return the double
	 */
	public static double mean(final AnnotationMatrix m, int row) {
		return mean(m.getInnerMatrix(), row);
	}

	/**
	 * Mean.
	 *
	 * @param m the m
	 * @param row the row
	 * @return the double
	 */
	public static double mean(final Matrix m, int row) {
		if (m instanceof DoubleMatrix) {
			return mean((DoubleMatrix)m, row);
		} else {
			return Statistics.mean(m.rowAsDouble(row));
		}
	}

	/**
	 * Mean.
	 *
	 * @param m the m
	 * @param row the row
	 * @return the double
	 */
	public static double mean(final DoubleMatrix m, int row) {
		double[] data = new double[m.mCols];

		System.arraycopy(m.mData, m.mRowOffsets[row], data, 0, m.mCols);

		return Statistics.mean(data);
	}
	
	
	
	
	
	/**
	 * Mean.
	 *
	 * @param m the m
	 * @return the double
	 */
	public static double mean(final AnnotationMatrix m) {
		return mean(m.getInnerMatrix());
	}

	/**
	 * Mean.
	 *
	 * @param m the m
	 * @return the double
	 */
	public static double mean(final Matrix m) {
		if (m instanceof DoubleMatrix) {
			return mean((DoubleMatrix)m);
		} else {
			double mean = 0;
			int c = 0;
			
			for (int i =0; i < m.getRowCount(); ++i) {
				for (int j = 0; j < m.getColumnCount(); ++j) {
					double v = m.getValue(i, j);
					
					if (Matrix.isValidMatrixNum(v)) {
						mean += v; 
						++c;
					}
				}
 			}
			
			if (c > 0) {
				return mean / c;
			} else {
				return Matrix.NULL_NUMBER;
			}
		}
	}

	/**
	 * Mean.
	 *
	 * @param m the m
	 * @return the double
	 */
	public static double mean(final DoubleMatrix m) {
		double mean = 0;
		int c = 0;
		
		for (int i =0; i < m.mData.length; ++i) {
			double v = m.mData[i];
				
			if (Matrix.isValidMatrixNum(v)) {
				mean += v; 
				++c;
			}
		}
		
		if (c > 0) {
			return mean / c;
		} else {
			return Matrix.NULL_NUMBER;
		}
	}
	
	

	/**
	 * Median.
	 *
	 * @param m the m
	 * @param row the row
	 * @return the double
	 */
	public static double median(final AnnotationMatrix m, int row) {
		return median(m.getInnerMatrix(), row);
	}

	/**
	 * Median.
	 *
	 * @param m the m
	 * @param row the row
	 * @return the double
	 */
	public static double median(final Matrix m, int row) {
		return Statistics.median(m.rowAsDouble(row));
	}

	/**
	 * Median.
	 *
	 * @param m the m
	 * @return the double
	 */
	public static double median(final AnnotationMatrix m) {
		return median(m.getInnerMatrix());
	}

	/**
	 * Median.
	 *
	 * @param m the m
	 * @return the double
	 */
	public static double median(final Matrix m) {
		return Statistics.median(Matrix.toDouble(m));
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
			
			TwoSampleTest test = TwoSampleTest.create(p1, p2); 

			if (equalVariance) {
				p = test.twoTailEqualVarianceTTest();
			} else {
				p = test.twoTailUnequalVarianceTTest();
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
	 * Perform a Mann Whitney test on two matrix groups.
	 * 
	 * @param m
	 * @param g1
	 * @param g2
	 * @param equalVariance
	 * @return
	 */
	public static List<Double> mannWhitney(AnnotationMatrix m, 
			MatrixGroup g1,
			MatrixGroup g2) {
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

			TwoSampleTest test = TwoSampleTest.create(p1, p2); 

			double p = test.mannWhitney();

			// Set strange values to NaN
			if (Mathematics.isInvalidNumber(p)) {
				p = 1; //Double.NaN;
			}

			pvalues.add(p);
		}

		return pvalues;
	}

	/**
	 * Row to list.
	 *
	 * @param m the m
	 * @param row the row
	 * @param columns the columns
	 * @return the list
	 */
	public static List<Double> rowToList(AnnotationMatrix m, 
			int row, 
			final List<Integer> columns) {
		return rowToList(m.getInnerMatrix(), row, columns);
	}

	/**
	 * Extracts a number of cells of a given row as an double array.
	 *
	 * @param m the m
	 * @param row the row
	 * @param columns the columns
	 * @return the list
	 */
	public static List<Double> rowToList(Matrix m, 
			int row, 
			final List<Integer> columns) {
		if (m instanceof DoubleMatrix) {
			return rowToList((DoubleMatrix)m, row, columns);
		}

		List<Double> ret = new ArrayList<Double>(columns.size());

		double v;

		for (int c : columns) {
			v = m.getValue(row, c);

			if (Mathematics.isValidNumber(v)) {
				ret.add(v);
			}
		}

		return ret;
	}

	/**
	 * Row to list.
	 *
	 * @param m the m
	 * @param row the row
	 * @param columns the columns
	 * @return the list
	 */
	public static List<Double> rowToList(DoubleMatrix m, 
			int row, 
			final List<Integer> columns) {
		List<Double> ret = new ArrayList<Double>(columns.size());

		double v;

		int index = m.getIndex(row, 0);

		for (int c : columns) {
			v = m.mData[c + index];

			if (Mathematics.isValidNumber(v)) {
				ret.add(v);
			}
		}

		return ret;
	}

	/**
	 * Return the maximum value in a set of columns of a given row.
	 *
	 * @param m the m
	 * @param row the row
	 * @param g the g
	 * @return the double
	 */
	public static double max(AnnotationMatrix m, 
			int row, 
			MatrixGroup g) {
		return max(m, row, MatrixGroup.findColumnIndices(m, g));
	}

	/**
	 * Max.
	 *
	 * @param m the m
	 * @param row the row
	 * @param columns the columns
	 * @return the double
	 */
	public static double max(Matrix m, 
			int row, 
			final List<Integer> columns) {
		double ret = 0;

		double v;

		for (int c : columns) {
			v = m.getValue(row, c);

			if (Mathematics.isValidNumber(v)) {
				if (v > ret) {
					ret = v;
				}
			}
		}

		return ret;
	}

	/**
	 * Sum row.
	 *
	 * @param m the m
	 * @param row the row
	 * @return the double
	 */
	public static double sumRow(Matrix m, int row) {
		double ret = 0;
		double v;

		for (int i = 0; i < m.getColumnCount(); ++i) {
			v = m.getValue(row, i);

			if (Mathematics.isValidNumber(v)) {
				ret += v;
			}
		}

		return ret;
	}

	/**
	 * Raise the value of each cell in a matrix to a given power.
	 *
	 * @param m the m
	 * @param power the power
	 * @return the annotation matrix
	 */
	public static AnnotationMatrix power(final AnnotationMatrix m, int power) {
		return new AnnotatableMatrix(m, power(m.getInnerMatrix(), power));
	}

	/**
	 * Power.
	 *
	 * @param m the m
	 * @param power the power
	 * @return the matrix
	 */
	public static Matrix power(final Matrix m, int power) {
		return apply(m, new PowerFunction(power));
	}

	/**
	 * Power.
	 *
	 * @param m the m
	 * @param power the power
	 * @return the double matrix
	 */
	public static DoubleMatrix power(DoubleMatrix m, int power) {
		return apply(m, new PowerFunction(power));
	}

	/**
	 * Power.
	 *
	 * @param m the m
	 * @param power the power
	 * @return the matrix
	 */
	public static Matrix power(MixedMatrix m, int power) {
		return apply(m, new PowerFunction(power));
	}

	/**
	 * Apply a function to each cell of a matrix.
	 *
	 * @param m the m
	 * @param f the f
	 * @return the matrix
	 */
	public static Matrix apply(final Matrix m, Function f) {
		if (m instanceof DoubleMatrix) {
			return apply((DoubleMatrix)m, f);
		} else if (m instanceof MixedMatrix) {
			return apply((MixedMatrix)m, f);
		} else {
			boolean keepText = Matrix.cellTypes(m).contains(CellType.TEXT);

			Matrix ret = null;

			if (keepText) {
				ret = MixedMatrix.createMixedMatrix(m);
			} else {
				ret = DoubleMatrix.createDoubleMatrix(m);
			}

			for (int i = 0; i < m.getRowCount(); ++i) {
				for (int j = 0; j < m.getColumnCount(); ++j) {
					if (m.getCellType(i, j) == CellType.NUMBER) {
						ret.set(i, j, f.apply(m.getValue(i, j)));
					} else {
						if (keepText) {
							ret.set(i, j, m.getText(i, j));
						}
					}
				}
			}

			return ret;
		}
	}

	/**
	 * Apply a function to a double matrix.
	 *
	 * @param m the m
	 * @param f the f
	 * @return the double matrix
	 */
	public static DoubleMatrix apply(final DoubleMatrix m, Function f) {
		DoubleMatrix ret = DoubleMatrix.createDoubleMatrix(m);

		for (int i = 0; i < m.mData.length; ++i) {
			ret.mData[i] = f.apply(m.mData[i]);
		}

		return ret;
	}

	/**
	 * Apply a function to a mixed array.
	 *
	 * @param m the m
	 * @param f the f
	 * @return the matrix
	 */
	public static Matrix apply(MixedMatrix m, Function f) {
		boolean keepText = Matrix.cellTypes(m).contains(CellType.TEXT);

		if (keepText) {
			MixedMatrix ret = MixedMatrix.createMixedMatrix(m);

			for (int i = 0; i < m.mData.length; ++i) {
				Object v = m.mData[i];

				if (v != null) {
					if (v instanceof Double) {
						ret.set(i, f.apply((double)v));
					} else if (v instanceof Integer) {
						ret.set(i, f.apply((int)v));
					} else if (v instanceof Number) {
						ret.set(i, f.apply(((Number)v).doubleValue()));
					} else {
						ret.set(i, v.toString());
					}
				}
			}

			return ret;
		} else {
			DoubleMatrix ret = DoubleMatrix.createDoubleMatrix(m);

			for (int i = 0; i < m.mData.length; ++i) {
				Object v = m.mData[i];

				if (v != null) {
					if (v instanceof Double) {
						ret.mData[i] = f.apply((double)v);
					} else if (v instanceof Integer) {
						ret.mData[i] = f.apply((int)v);
					} else if (v instanceof Number) {
						ret.mData[i] = f.apply(((Number)v).doubleValue());
					} else {
						// Do nothing
					}
				}
			}

			return ret;
		}
	}

	/**
	 * To row.
	 *
	 * @param values the values
	 * @param row the row
	 * @param m the m
	 */
	public static void toRow(final Collection<? extends Object> values, 
			int row, 
			AnnotationMatrix m) {
		toRow(values, row, m.getInnerMatrix());
	}

	/**
	 * Assign a collection to a matrix row.
	 *
	 * @param values the values
	 * @param row the row
	 * @param m the m
	 */
	public static void toRow(final Collection<? extends Object> values, 
			int row, 
			Matrix m) {
		int c = 0;

		for (Object v : values) {
			m.set(row, c++, v);
		}
	}

	/**
	 * Num to row.
	 *
	 * @param values the values
	 * @param row the row
	 * @param m the m
	 */
	public static void numToRow(final Collection<? extends Number> values, 
			int row, 
			AnnotationMatrix m) {
		numToRow(values, row, m.getInnerMatrix());
	}

	/**
	 * Num to row.
	 *
	 * @param values the values
	 * @param row the row
	 * @param m the m
	 */
	public static void numToRow(final Collection<? extends Number> values, 
			int row, 
			Matrix m) {
		if (m instanceof DoubleMatrix) {
			numToRow(values, row, (DoubleMatrix)m);
		} else {
			toRow(values, row, m);
		}
	}

	/**
	 * Num to row.
	 *
	 * @param values the values
	 * @param row the row
	 * @param m the m
	 */
	public static void numToRow(final Collection<? extends Number> values, 
			int row, 
			DoubleMatrix m) {
		int c = row * m.mCols;

		for (Number v : values) {
			m.mData[c++] = v.doubleValue();
		}
	}


	/**
	 * Num to row.
	 *
	 * @param values the values
	 * @param row the row
	 * @param m the m
	 */
	public static void numToRow(final double[] values, 
			int row, 
			AnnotationMatrix m) {
		numToRow(values, row, m.getInnerMatrix());
	}

	/**
	 * Num to row.
	 *
	 * @param values the values
	 * @param row the row
	 * @param m the m
	 */
	public static void numToRow(final double[] values, 
			int row, 
			Matrix m) {
		if (m instanceof DoubleMatrix) {
			numToRow(values, row, (DoubleMatrix)m);
		} else {
			int c = m.getColumnCount();

			for (int i = 0; i < c; ++i) {
				m.set(row, i, values[i]);
			}
		}
	}

	/**
	 * Num to row.
	 *
	 * @param values the values
	 * @param row the row
	 * @param m the m
	 */
	public static void numToRow(final double[] values, 
			int row, 
			DoubleMatrix m) {
		int c = row * m.mCols;

		for (double v : values) {
			m.mData[c++] = v;
		}
	}

	/**
	 * Num to row.
	 *
	 * @param values the values
	 * @param row the row
	 * @param m the m
	 */
	public static void numToRow(final int[] values, 
			int row, 
			AnnotationMatrix m) {
		numToRow(values, row, m.getInnerMatrix());
	}

	/**
	 * Num to row.
	 *
	 * @param values the values
	 * @param row the row
	 * @param m the m
	 */
	public static void numToRow(final int[] values, 
			int row, 
			Matrix m) {
		if (m instanceof IntMatrix) {
			numToRow(values, row, (IntMatrix)m);
		} else if (m instanceof DoubleMatrix) {
			numToRow(values, row, (DoubleMatrix)m);
		} else {
			int c = m.getColumnCount();

			for (int i = 0; i < c; ++i) {
				m.set(row, i, values[i]);
			}
		}
	}

	/**
	 * Num to row.
	 *
	 * @param values the values
	 * @param row the row
	 * @param m the m
	 */
	public static void numToRow(final int[] values, 
			int row, 
			IntMatrix m) {
		int c = row * m.mCols;

		for (int v : values) {
			m.mData[c++] = v;
		}
	}
	
	/**
	 * Num to row.
	 *
	 * @param values the values
	 * @param row the row
	 * @param m the m
	 */
	public static void numToRow(final int[] values, 
			int row, 
			DoubleMatrix m) {
		int c = row * m.mCols;

		for (int v : values) {
			m.mData[c++] = v;
		}
	}

	/**
	 * Text to row.
	 *
	 * @param values the values
	 * @param row the row
	 * @param m the m
	 */
	public static void textToRow(final Collection<String> values, 
			int row, 
			AnnotationMatrix m) {
		textToRow(values, row, m.getInnerMatrix());
	}

	/**
	 * Text to row.
	 *
	 * @param values the values
	 * @param row the row
	 * @param m the m
	 */
	public static void textToRow(final Collection<String> values, 
			int row, 
			Matrix m) {
		if (Matrix.isText(m)) {
			textToRow(values, row, (TextMatrix)m);
		} else {
			toRow(values, row, m);
		}
	}

	/**
	 * Text to row.
	 *
	 * @param values the values
	 * @param row the row
	 * @param m the m
	 */
	public static void textToRow(final Collection<String> values, 
			int row, 
			TextMatrix m) {
		int c = row * m.mCols;

		for (Object v : values) {
			m.mData[c++] = v.toString();
		}
	}

	public static AnnotationMatrix divide(AnnotationMatrix m, double x) {
		AnnotationMatrix ret = new AnnotatableMatrix(m, true);
		
		MatrixArithmetic.divide(x, ret);
		
		return ret;
	}

	public static AnnotationMatrix add(AnnotationMatrix m, double x) {
		AnnotationMatrix ret = new AnnotatableMatrix(m, true);
		
		MatrixArithmetic.add(x, ret);
		
		return ret;
	}

	public static AnnotationMatrix subtract(AnnotationMatrix m, double x) {
		AnnotationMatrix ret = new AnnotatableMatrix(m, true);
		
		MatrixArithmetic.subtract(x, ret);
		
		return ret;
	}
}
