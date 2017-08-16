package org.abh.common.math.test;

import org.jebtk.math.matrix.Matrix;
import org.jebtk.math.matrix.UpperTriangularDoubleMatrix;
import org.junit.Assert;
import org.junit.Test;

public class MatrixTest {
	@Test
	public void indexTest() {
		Matrix matrix = 
				UpperTriangularDoubleMatrix.createUpperTriangularMatrix(42);
		
		matrix.set(32, 16, 4);
		
		Assert.assertEquals("UT matrix (32, 16) = 4", 4, matrix.getValue(16, 32), 0);
	}
	
	@Test
	public void diagonalTest() {
		Matrix matrix = 
				UpperTriangularDoubleMatrix.createUpperTriangularMatrix(42);
		
		matrix.set(41, 41, 4);
		
		Assert.assertEquals("UT matrix (41, 41) = 4", 4, matrix.getValue(41, 41), 0);
	}
}
