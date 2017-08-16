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

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The interface MatrixAnnotations.
 */
public interface MatrixAnnotations {
	
	/**
	 * Gets the column annotation names.
	 *
	 * @return the column annotation names
	 */
	public List<String> getColumnAnnotationNames();
	
	/**
	 * Gets the column annotation values.
	 *
	 * @param column the column
	 * @return the column annotation values
	 */
	public List<Double> getColumnAnnotationValues(int column);
	
	/**
	 * Gets the column annotation text.
	 *
	 * @param column the column
	 * @return the column annotation text
	 */
	public List<String> getColumnAnnotationText(int column);
	
	/**
	 * Gets the column name.
	 *
	 * @param column the column
	 * @return the column name
	 */
	public String getColumnName(int column);
	
	/**
	 * Gets the row annotation names.
	 *
	 * @return the row annotation names
	 */
	public List<String> getRowAnnotationNames();
	
	/**
	 * Gets the row annotation values.
	 *
	 * @param row the row
	 * @return the row annotation values
	 */
	public List<Double> getRowAnnotationValues(int row);
	
	/**
	 * Gets the row annotation text.
	 *
	 * @param row the row
	 * @return the row annotation text
	 */
	public List<String> getRowAnnotationText(int row);
	
	/**
	 * Gets the row name.
	 *
	 * @param row the row.
	 * @return the row name
	 */
	public String getRowName(int row);
}
