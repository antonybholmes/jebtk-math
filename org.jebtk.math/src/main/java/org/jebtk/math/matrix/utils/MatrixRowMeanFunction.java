package org.jebtk.math.matrix.utils;

import org.jebtk.math.matrix.MatrixReduceFunction;
import org.jebtk.math.statistics.Stats;

public class MatrixRowMeanFunction extends MatrixReduceFunction {

	@Override
	public double apply(int index, int s, int l, double[] data) {
		return new Stats(data, s, l).mean();
	}

}
