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
package org.jebtk.math.matrix.vec;

import org.jebtk.math.matrix.DoubleMatrix;
import org.jebtk.math.matrix.MatrixOperations;

// TODO: Auto-generated Javadoc
/**
 * The class Proj3DTo2DMatrix3D.
 */
public class Proj3DTo2DMatrix3D extends DoubleMatrix {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new proj3 d to2 d matrix3 d.
	 *
	 * @param e the e
	 * @param nearZ the near z
	 * @param farZ the far z
	 */
	public Proj3DTo2DMatrix3D(double e, double nearZ, double farZ) {
		super(4, 4, 0);
		
		double nd = nearZ - farZ;
				
		//setNumValue(0, 0, 1);
		//setNumValue(1, 1, 1);
		//setNumValue(2, 2, 1);
		
		//setNumValue(0, 2, -e.getNumValue(0) / e.getNumValue(2));
		//setNumValue(1, 2, -e.getNumValue(1) / e.getNumValue(2));
		//setNumValue(3, 2, 1 / e.getNumValue(2));
		
		
		set(0, 0, e);
		set(1, 1, e);
		set(2, 2, (-nearZ - farZ) / nd);
		set(2, 3, 2 * farZ * nearZ / nd);
		set(3, 2, 1);
	}
	
	/**
	 * Assume .
	 *
	 * @param a the a
	 * @param camera the camera
	 * @param ez the ez
	 * @return the coordinate3 d
	 */
	public static Coordinate3D proj3DTo2D(Coordinate3D a,
			Coordinate3D camera,
			double ez) {

		Coordinate3D d = Coordinate3D.subtract(a, camera);
		
		System.err.println("e " + ez);
		System.err.println("d " + d);
		
		Proj3DTo2DMatrix3D proj = new Proj3DTo2DMatrix3D(ez, 1, 10);
		
		System.err.println("p " + proj);
		
		Coordinate3D intermediate = 
				new Coordinate3D(MatrixOperations.multiply(proj, d));
		
		System.err.println("i " + intermediate);
		
		System.err.println("w " + intermediate.getW());
		
		// need to divide ret by w (which contains z)
		Coordinate3D ret = Coordinate3D.divideXYZ(intermediate, intermediate.getW());
		
		System.err.println("ret " + ret);
		
		return a; //ret;
	}
}
