package org.jebtk.math.matrix;

public abstract class MatrixStatFunction implements MatrixCellFunction {
	
	protected double mStat = Matrix.NULL_NUMBER;
	
	/**
	 * Run once before matrix is iterated over
	 */
	public void init() {
		
	}
	
	public void setStat(double v) {
		mStat = v;
	}
	
	public double getStat() {
		return mStat;
	}
}
