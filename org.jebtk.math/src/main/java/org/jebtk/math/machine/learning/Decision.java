package org.jebtk.math.machine.learning;

import java.util.Collection;
import java.util.Iterator;

public class Decision implements Iterable<String> {
	private int mAttIdx;
	private DecisionType mType;
	
	private Collection<String> mValues = null;
	private double mPivot = Double.NaN;

	/**
	 * Should return the tree branch to follow based on the value.
	 * 
	 * @param value
	 * @return
	 */
	public Decision(int attIdx, double pivot) {
		mAttIdx = attIdx;
		mPivot = pivot;
		mType = DecisionType.NUMERICAL;
	}
	
	public Decision(int attIdx, Collection<String> values) {
		mAttIdx = attIdx;
		mValues = values;
		mType = DecisionType.TEXT;
	}
	
	/**
	 * Returns the attribute index in the list of values that will be
	 * processed. Samples
	 * @return
	 */
	public int getAttIdx() {
		return mAttIdx;
	}
	
	public DecisionType getType() {
		return mType;
	}

	@Override
	public Iterator<String> iterator() {
		return mValues.iterator();
	}

	/**
	 * Returns the test pivot value.
	 * 
	 * @return
	 */
	public double getPivot() {
		return mPivot;
	}
}
