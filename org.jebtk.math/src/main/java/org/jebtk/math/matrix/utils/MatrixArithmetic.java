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

import org.jebtk.math.matrix.Matrix;
import org.jebtk.math.matrix.MatrixCellFunction;

// TODO: Auto-generated Javadoc
/**
 * Inline functions that perform arithmetic on a matrix and modify it in place.
 * Use MatrixOperations to modify a copy of a matrix and leave the original
 * alone.
 * 
 * @author antony
 *
 */
public class MatrixArithmetic {
	
	/**
	 * The Class ArithFunc.
	 */
	private static abstract class ArithFunc implements MatrixCellFunction {

		/** The m V. */
		protected double mV;

		/**
		 * Instantiates a new arith func.
		 *
		 * @param v the v
		 */
		public ArithFunc(double v) {
			mV = v;
		}
	}
	
	/**
	 * The Class AddFunc.
	 */
	private static class AddFunc extends ArithFunc {
		
		/**
		 * Instantiates a new adds the func.
		 *
		 * @param v the v
		 */
		public AddFunc(double v) {
			super(v);
		}
		
		/* (non-Javadoc)
		 * @see org.jebtk.math.matrix.MatrixFunction#apply(int, int, double)
		 */
		@Override
		public double apply(int i, int j, double v) {
			return v + mV;
		}
	}
	
	/**
	 * The Class MultiplyFunc.
	 */
	private static class MultiplyFunc extends ArithFunc {
		
		/**
		 * Instantiates a new multiply func.
		 *
		 * @param v the v
		 */
		public MultiplyFunc(double v) {
			super(v);
		}
		
		/* (non-Javadoc)
		 * @see org.jebtk.math.matrix.MatrixFunction#apply(int, int, double)
		 */
		@Override
		public double apply(int i, int j, double v) {
			return v * mV;
		}
	}
	
	/**
	 * The Class DivideFunc.
	 */
	private static class DivideFunc extends ArithFunc {
		
		/**
		 * Instantiates a new divide func.
		 *
		 * @param v the v
		 */
		public DivideFunc(double v) {
			super(v);
		}
		
		/* (non-Javadoc)
		 * @see org.jebtk.math.matrix.MatrixFunction#apply(int, int, double)
		 */
		@Override
		public double apply(int i, int j, double v) {
			return v / mV;
		}
	}
	
	/**
	 * Adds the.
	 *
	 * @param x the x
	 * @param m the m
	 */
	public static void add(double x, Matrix m) {
		m.apply(new AddFunc(x));
	}
	
	/**
	 * Subtract.
	 *
	 * @param x the x
	 * @param m the m
	 */
	public static void subtract(double x, Matrix m) {
		add(-x, m);
	}
	
	/**
	 * Multiply.
	 *
	 * @param x the x
	 * @param m the m
	 */
	public static void multiply(double x, Matrix m) {
		m.apply(new MultiplyFunc(x));
	}
	
	/**
	 * Divide.
	 *
	 * @param x the x
	 * @param m the m
	 */
	public static void divide(double x, Matrix m) {
		m.apply(new DivideFunc(x));
	}
}
