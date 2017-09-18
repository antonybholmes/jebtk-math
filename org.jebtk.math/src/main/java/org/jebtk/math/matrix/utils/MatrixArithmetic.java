package org.jebtk.math.matrix.utils;

import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.math.matrix.Matrix;
import org.jebtk.math.matrix.MatrixFunction;

/**
 * Inline functions that perform arithmetic on a matrix and modify it in place.
 * Use MatrixOperations to modify a copy of a matrix and leave the original
 * alone.
 * 
 * @author antony
 *
 */
public class MatrixArithmetic {
	private static abstract class ArithFunc implements MatrixFunction {

		protected double mV;

		public ArithFunc(double v) {
			mV = v;
		}
	}
	
	private static class AddFunc extends ArithFunc {
		public AddFunc(double v) {
			super(v);
		}
		
		@Override
		public double apply(int i, int j, double v) {
			return v + mV;
		}
	}
	
	private static class MultiplyFunc extends ArithFunc {
		public MultiplyFunc(double v) {
			super(v);
		}
		
		@Override
		public double apply(int i, int j, double v) {
			return v * mV;
		}
	}
	
	private static class DivideFunc extends ArithFunc {
		public DivideFunc(double v) {
			super(v);
		}
		
		@Override
		public double apply(int i, int j, double v) {
			return v / mV;
		}
	}
	
	public static void add(double x, AnnotationMatrix m) {
		add(x, m.getInnerMatrix());
	}
	
	public static void add(double x, Matrix m) {
		m.apply(new AddFunc(x));

		m.fireMatrixChanged();
	}
	
	public static void subtract(double x, AnnotationMatrix m) {
		subtract(x, m.getInnerMatrix());
	}
	
	public static void subtract(double x, Matrix m) {
		add(-x, m);
	}
	
	public static void multiply(double x, AnnotationMatrix m) {
		multiply(x, m.getInnerMatrix());
	}
	
	public static void multiply(double x, Matrix m) {
		m.apply(new MultiplyFunc(x));

		m.fireMatrixChanged();
	}
	
	public static void divide(double x, AnnotationMatrix m) {
		divide(x, m.getInnerMatrix());
	}
	
	public static void divide(double x, Matrix m) {
		m.apply(new DivideFunc(x));
		
		m.fireMatrixChanged();
	}
}
