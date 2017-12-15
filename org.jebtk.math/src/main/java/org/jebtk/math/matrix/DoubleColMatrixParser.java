package org.jebtk.math.matrix;

import java.util.Collection;

import org.jebtk.core.text.TextUtils;

public class DoubleColMatrixParser extends MixedMatrixParser {
	
	public DoubleColMatrixParser(boolean hasHeader,
			int rowAnnotations,
			String delimiter) {
		this(hasHeader, TextUtils.EMPTY_LIST, rowAnnotations, delimiter);
	}
	
	public DoubleColMatrixParser(boolean hasHeader, 
			Collection<String> skipMatches, 
			int rowAnnotations,
			String delimiter) {
		super(hasHeader, skipMatches, rowAnnotations, delimiter);
	}

	@Override
	public DataFrame createMatrix(int rows, int columns) {
		return DataFrame.createDataFrame(new DoubleColMatrix(rows, columns)); 
	}
}
