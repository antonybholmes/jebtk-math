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
 * The class TransCameraMatrix3D.
 */
public class TransCameraMatrix3D extends DoubleMatrix {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	

	/**
	 * Instantiates a new trans camera matrix3 d.
	 */
	public TransCameraMatrix3D() {
		this(Coordinate3D.ORIGIN);
	}
	
	/**
	 * Instantiates a new trans camera matrix3 d.
	 *
	 * @param target the target
	 */
	public TransCameraMatrix3D(Coordinate3D target) {
		this(target, Coordinate3D.UP);
	}
	
	/**
	 * Instantiates a new trans camera matrix3 d.
	 *
	 * @param target the target
	 * @param up the up
	 */
	public TransCameraMatrix3D(Coordinate3D target,
			Coordinate3D up) {
		super(4 ,4, 0);
		
		Coordinate3D n = Coordinate3D.normalize(target);
		
		Coordinate3D u = 
				Coordinate3D.crossProduct(Coordinate3D.normalize(up), target);
		
		Coordinate3D v = Coordinate3D.crossProduct(n, u);
		
		
		set(0, 0, u.getValue(0));
		set(0, 1, u.getValue(1));
		set(0, 2, u.getValue(2));
		
		set(1, 0, v.getValue(0));
		set(1, 1, v.getValue(1));
		set(1, 2, v.getValue(2));

		set(2, 0, n.getValue(0));
		set(2, 1, n.getValue(1));
		set(2, 2, n.getValue(2));
		
		set(3, 3, 1);
	}
}
