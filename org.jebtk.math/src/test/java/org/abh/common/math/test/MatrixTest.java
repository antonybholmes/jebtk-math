/**
 * Copyright 2017 Antony Holmes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.abh.common.math.test;

import java.util.Arrays;

import org.jebtk.math.matrix.DoubleColMatrix;
import org.jebtk.math.matrix.DoubleMatrix;
import org.jebtk.math.matrix.Matrix;
import org.jebtk.math.matrix.UpperTriangularDoubleMatrix;
import org.junit.Assert;
import org.junit.Test;

// TODO: Auto-generated Javadoc
/**
 * The Class MatrixTest.
 */
public class MatrixTest {
	
	/**
	 * Index test.
	 */
	@Test
	public void indexTest() {
		Matrix matrix = 
				UpperTriangularDoubleMatrix.createUpperTriangularMatrix(42);
		
		matrix.set(32, 16, 4);
		
		Assert.assertEquals("UT matrix (32, 16) = 4", 4, matrix.getValue(16, 32), 0);
	}
	
	/**
	 * Diagonal test.
	 */
	@Test
	public void diagonalTest() {
		Matrix matrix = 
				UpperTriangularDoubleMatrix.createUpperTriangularMatrix(42);
		
		matrix.set(41, 41, 4);
		
		Assert.assertEquals("UT matrix (41, 41) = 4", 4, matrix.getValue(41, 41), 0);
	}
	
	@Test
	public void colTest() {
		Matrix matrix = 
				DoubleMatrix.createDoubleMatrix(2, 2, 1, 2, 3, 4);
		
		System.err.println("regular " + matrix.getValue(1, 1));
		
		DoubleColMatrix m2 = DoubleColMatrix.createDoubleColMatrix(2, 2);
		
		m2.set(0, 1, 2);
		m2.set(1, 1, 4);
		
		System.err.println("col " + m2.getValue(0, 1));
		
		System.err.println("v " + Arrays.toString(m2.columnToDoubleArray(1)));
	}
}
