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

// TODO: Auto-generated Javadoc
/**
 * The class TransMatrix3D.
 */
public class TransMatrix3D extends DoubleMatrix {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new trans matrix3 d.
	 *
	 * @param dx the dx
	 * @param dy the dy
	 * @param dz the dz
	 */
	public TransMatrix3D(double dx, double dy, double dz) {
		super(4 ,4, 0);
		
		set(0, 0, 1);
		set(1, 1, 1);
		set(2, 2, 1);
		set(3, 3, 1);
		
		set(0, 3, dx);
		set(1, 3, dy);
		set(2, 3, dz);
	}

	/**
	 * Gets the x trans.
	 *
	 * @param d the d
	 * @return the x trans
	 */
	public static TransMatrix3D getXTrans(double d) {
		return new TransMatrix3D(d, 0, 0);
	}
	
	/**
	 * Gets the y trans.
	 *
	 * @param d the d
	 * @return the y trans
	 */
	public static TransMatrix3D getYTrans(double d) {
		return new TransMatrix3D(0, d, 0);
	}
	
	/**
	 * Gets the z trans.
	 *
	 * @param d the d
	 * @return the z trans
	 */
	public static TransMatrix3D getZTrans(double d) {
		return new TransMatrix3D(0, 0, d);
	}
}
