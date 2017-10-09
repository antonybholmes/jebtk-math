package org.jebtk.math.matrix.stream;

import org.jebtk.core.Mathematics;
import org.jebtk.math.matrix.Matrix;
import org.jebtk.math.matrix.MatrixCellFunction;
import org.jebtk.math.matrix.MatrixDimFunction;
import org.jebtk.math.matrix.MatrixReduceFunction;

public class MatrixStream implements MatrixCellFunction, MatrixDimFunction, MatrixReduceFunction {

	private Matrix mM;

	public MatrixStream(Matrix m) {
		mM = m;
	}
	
	public static MatrixStream applied(Matrix m) {
		return apply(m.copy());
	}

	private static MatrixStream apply(Matrix m) {
		return new MatrixStream(m);
	}
	
	public void apply() {
		for (int i = 0; i < mM.getRowCount(); ++i) {
			for (int j = 0; j < mM.getColumnCount(); ++j) {
				double v = mM.getValue(i, j);

				if (Mathematics.isValidNumber(v)) {
					mM.set(i, j, apply(i, j, v));
				}
			}
		}

		mM.fireMatrixChanged();
	}

	@Override
	public double apply(int row, int col, double value) {
		return Matrix.NULL_NUMBER;
	}
	
	@Override
	public void apply(int index, double[] data, double[] ret) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double apply(int index, double[] data) {
		return Matrix.NULL_NUMBER;
	}
}
