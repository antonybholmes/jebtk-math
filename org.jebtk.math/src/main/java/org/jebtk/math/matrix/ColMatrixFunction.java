package org.jebtk.math.matrix;

public abstract class ColMatrixFunction implements ColNumFunction {
	private DoubleMatrix mMatrix;
	private int mC;
	
	public ColMatrixFunction(DoubleMatrix matrix) {
		mC = matrix.getColumnCount();
		mMatrix = matrix;
	}
	
	@Override
	public void apply(double[] data, int offset, int l, int col) {
		int p = col;
		
		// Apply row wise
		for (int i = 0; i < l; ++i) {
			//System.err.println(i + " " + l + " " + mC + " " + mMatrix.getRowCount() + " " + mFactors.length + " " + data.length);
			
			apply(data[i], i, col, p, mMatrix.mData);
			
			p += mC;
		}
	}
	
	public abstract void apply(double d, int row, int col, int p, double[] data);
}