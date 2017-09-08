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
package org.jebtk.math.matrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.Io;
import org.jebtk.core.io.ReaderUtils;
import org.jebtk.core.text.Splitter;
import org.jebtk.core.text.TextUtils;

// TODO: Auto-generated Javadoc
/**
 * Parses a text file and creates a matrix from it.
 * 
 * @author Antony Holmes Holmes
 */
public class MixedMatrixParser implements MatrixParser {

	/**
	 * The member row annotations.
	 */
	protected int mRowAnnotations = -1;

	/**
	 * The member has header.
	 */
	protected boolean mHasHeader = false;

	/**
	 * The member delimiter.
	 */
	protected String mDelimiter = null;

	/** The m skip matches. */
	private Collection<String> mSkipMatches;


	/**
	 * Instantiates a new text matrix parser.
	 *
	 * @param hasHeader the has header
	 * @param skipMatches the skip matches
	 * @param rowAnnotations the row annotations
	 * @param delimiter the delimiter
	 */
	public MixedMatrixParser(boolean hasHeader,
			Collection<String> skipMatches,
			int rowAnnotations, 
			String delimiter) {
		// If row annotations >0, you must have a header otherwise the file
		// is garbage
		mHasHeader = hasHeader || rowAnnotations > 0;
		mSkipMatches = skipMatches;
		mRowAnnotations = rowAnnotations;
		mDelimiter = delimiter;
	}

	/**
	 * Sets the.
	 *
	 * @param matrix the matrix
	 * @param row the row
	 * @param column the column
	 * @param value the value
	 */
	protected void set(Matrix matrix, int row, int column, String value) {

		if (TextUtils.isNumber(value)) {
			matrix.update(row, column, Double.parseDouble(value));
		} else {
			matrix.update(row, column, value);
		}
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.math.matrix.MatrixParser#parse(java.io.Path)
	 */
	@Override
	public AnnotationMatrix parse(Path file) throws IOException {
		AnnotationMatrix matrix = null;
		
		String line;
		List<String> tokens;

		//
		// Work out if we need to skip annotation rows that should be
		// ignored
		
		int skipLines = FileUtils.countHeaderLines(file, mSkipMatches);
		
		//
		// Count rows
		//

		int rows = mHasHeader ? 0 : 1;
		int columns = -1;

		Splitter split = Splitter.on(mDelimiter);

		BufferedReader reader = FileUtils.newBufferedReader(file);
		
		try {
			ReaderUtils.skipLines(reader, skipLines);
			
			line = reader.readLine();

			tokens = split.text(line); //ImmutableList.copyOf(Splitter.on(TextUtils.TAB_DELIMITER).split(line)); //TextUtils.tabSplit(line);

			columns = tokens.size(); // - mRowAnnotations;

			// The first line is read as a header so there is one less line
			// being read than there are rows

			while ((line = reader.readLine()) != null) {
				if (Io.isEmptyLine(line)) {
					continue;
				}

				++rows;
			}
		} finally {
			reader.close();
		}

		matrix = createMatrix(rows, columns - mRowAnnotations); //new MixedSparseMatrix(rows, columns); //new MixedMatrix(rows, columns);


		reader = FileUtils.newBufferedReader(file);

		//List<List<Object>> annotations = null;
		List<String> rowAnnotationNames = null;

		int row = 0;
		int offset = 0;

		try {
			if (skipLines > 0) {
				for (int i = 0; i < skipLines; ++i) {
					reader.readLine();
				}
			}
			
			if (mHasHeader) {
				// add column names
				line = reader.readLine();
				tokens = split.text(TextUtils.removeExcelQuotes(line));

				matrix.setColumnNames(CollectionUtils.subList(tokens, mRowAnnotations));

				//annotations = new ArrayList<List<Object>>();
				rowAnnotationNames = CollectionUtils.subList(tokens, 0, mRowAnnotations);

				offset = -rowAnnotationNames.size();

				for (String name : rowAnnotationNames) {
					// Cause the annotation to be initialized
					matrix.getRowAnnotations(name);
				}

				//for (int i = 0; i < mRowAnnotations; ++i) {
				//		annotations.add(CollectionUtils.replicate(null, rows));
				//}

				//++row;
			}

			while ((line = reader.readLine()) != null) {
				if (Io.isEmptyLine(line)) {
					continue;
				}

				tokens = split.text(TextUtils.removeExcelQuotes(line));

				//if (mHasHeader) {
				//for (int i = 0; i < mRowAnnotations; ++i) {
				//	annotations.get(i).set(row, tokens.get(i));
				//}
				//}

				// the first token is the column name so ignore it
				//for (int i = mRowAnnotations; i < tokens.size(); ++i) {
				for (int i = 0; i < tokens.size(); ++i) {
					//System.err.println("txt parser " + i + " " + offset);

					set(matrix, row, i + offset, tokens.get(i));
				}

				++row;
			}

			//if (mHasHeader) {
			//	for (int i = 0; i < mRowAnnotations; ++i) {
			//		matrix.setRowAnnotations(rowAnnotationNames.get(i), annotations.get(i));
			//	}
			//}
		} finally {
			reader.close();
		}

		return matrix;
	}

	/**
	 * Creates the matrix.
	 *
	 * @param rows the rows
	 * @param columns the columns
	 * @return the annotation matrix
	 */
	public AnnotationMatrix createMatrix(int rows, int columns) {
		return AnnotatableMatrix.createAnnotatableMatrix(rows, columns); 
	}

}
