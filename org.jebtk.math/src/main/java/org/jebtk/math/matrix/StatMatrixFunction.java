package org.jebtk.math.matrix;

public abstract class StatMatrixFunction {
	
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
	
	public abstract void apply(int i, int j, double value);
}
