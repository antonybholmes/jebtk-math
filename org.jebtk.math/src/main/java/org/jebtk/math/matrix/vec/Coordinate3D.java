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
import org.jebtk.math.matrix.Matrix;
import org.jebtk.math.matrix.MatrixOperations;

// TODO: Auto-generated Javadoc
/**
 * The class Coordinate3D.
 */
public class Coordinate3D extends DoubleMatrix {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The constant UP.
	 */
	public static final Coordinate3D UP = new Coordinate3D(0, 1, 0);
	
	/**
	 * The constant ORIGIN.
	 */
	public static final Coordinate3D ORIGIN = new Coordinate3D(0, 0, 0);
	
	/**
	 * Instantiates a new coordinate3 d.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public Coordinate3D(double x, double y) {
		this(x, y, 0, 1);
	}
	
	/**
	 * Instantiates a new coordinate3 d.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 */
	public Coordinate3D(double x, double y, double z) {
		this(x, y, z, 1);
	}
	
	/**
	 * Instantiates a new coordinate3 d.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @param w the w
	 */
	public Coordinate3D(double x, double y, double z, double w) {
		super(4, 1);
		
		set(0, 0, x);
		set(0, 1, y);
		set(0, 2, z);
		set(0, 3, w);
	}

	/**
	 * Instantiates a new coordinate3 d.
	 *
	 * @param c the c
	 */
	public Coordinate3D(Coordinate3D c) {
		this(c.getX(), c.getY(), c.getZ(), c.getW());
	}
	
	/**
	 * Instantiates a new coordinate3 d.
	 *
	 * @param c the c
	 */
	public Coordinate3D(Matrix c) {
		this(c.getValue(0, 0), c.getValue(0, 1), c.getValue(0, 2), c.getValue(0, 3));
	}
	
	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public double getX() {
		return getValue(0);
	}
	
	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public double getY() {
		return getValue(1);
	}
	
	/**
	 * Gets the z.
	 *
	 * @return the z
	 */
	public double getZ() {
		return getValue(2);
	}
	
	/**
	 * Gets the w.
	 *
	 * @return the w
	 */
	public double getW() {
		return getValue(3);
	}

	/**
	 * Rotate z.
	 *
	 * @param c the c
	 * @param a the a
	 * @return the numerical matrix
	 */
	public static Matrix rotateZ(DoubleMatrix c, double a) {
		DoubleMatrix rm = new RotMatrix3DZ(a);
		
		return MatrixOperations.multiply(rm, c);
	}
	
	/**
	 * Cross product.
	 *
	 * @param c1 the c1
	 * @param c2 the c2
	 * @return the coordinate3 d
	 */
	public static Coordinate3D crossProduct(Coordinate3D c1, Coordinate3D c2) {
		double x = c1.getY() * c2.getZ() - 
				c1.getZ() * c2.getY();
		
		double y = c1.getZ() * c2.getY() - 
				c1.getY() * c2.getZ();
		
		double z = c1.getX() * c2.getY() - 
				c1.getY() * c2.getX();
		
		return new Coordinate3D(x, y, z);
	}
	
	/**
	 * Normalize.
	 *
	 * @param c1 the c1
	 * @return the coordinate3 d
	 */
	public static Coordinate3D normalize(Coordinate3D c1) {
		double x = c1.getX();
		
		double y = c1.getY();
		
		double z = c1.getZ();
		
		double l = Math.sqrt(x * x + y * y + z * z);
		
		return new Coordinate3D(x / l, y / l, z / l);
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static final void main(String[] args) {
		DoubleMatrix m1 = new Coordinate3D(1, 1, 0);
		
		System.err.println(m1.toString());
		
		Matrix m2 = rotateZ(m1, -Math.PI/2);
		
		System.err.println(m2.toString());
		
		/*
		NumericalMatrix m1 = new NumericalMatrix(3 , 2);
		
		m1.setNumValue(0, 1);
		m1.setNumValue(1, 2);
		m1.setNumValue(2, 3);
		m1.setNumValue(3, 4);
		m1.setNumValue(4, 5);
		m1.setNumValue(5, 6);
		
		System.err.println(m1.toString());
		
		
		NumericalMatrix m2 = new NumericalMatrix(2, 4);
		
		m2.setNumValue(0, 1);
		m2.setNumValue(1, 2);
		m2.setNumValue(2, 3);
		m2.setNumValue(3, 4);
		m2.setNumValue(4, 5);
		m2.setNumValue(5, 6);
		m2.setNumValue(6, 7);
		m2.setNumValue(7, 8);
		
		System.err.println(m2.toString());
		
		NumericalMatrix m3 = MatrixOperations.multiply(m1, m2);
		
		System.err.println(m3.toString());
		*/
	}

	/**
	 * Gets the x.
	 *
	 * @param x the x
	 * @return the x
	 */
	public static Coordinate3D getX(double x) {
		return new Coordinate3D(x, 0, 0);
	}
	
	/**
	 * Gets the y.
	 *
	 * @param y the y
	 * @return the y
	 */
	public static Coordinate3D getY(double y) {
		return new Coordinate3D(0, y, 0);
	}
	
	/**
	 * Gets the z.
	 *
	 * @param z the z
	 * @return the z
	 */
	public static Coordinate3D getZ(double z) {
		return new Coordinate3D(0, 0, z);
	}
	
	/**
	 * Invert.
	 *
	 * @param c the c
	 * @return the coordinate3 d
	 */
	public static Coordinate3D invert(Coordinate3D c) {
		return new Coordinate3D(-c.getX(), -c.getY(), -c.getZ());
	}

	/**
	 * Substract c2 from c1.
	 *
	 * @param c1 the c1
	 * @param c2 the c2
	 * @return the coordinate3 d
	 */
	public static Coordinate3D subtract(Coordinate3D c1, Coordinate3D c2) {
		return new Coordinate3D(c1.getX() - c2.getX(), 
				c1.getY() - c2.getY(),
				c1.getZ() - c2.getZ());
	}

	/**
	 * Returns coordinate where only the xyz components are divided by leaving
	 * w intact. This is used for perspective correction where w is used to
	 * maintain z which would otherwise become 0.
	 *
	 * @param c1 the c1
	 * @param numValue the num value
	 * @return the coordinate3 d
	 */
	public static Coordinate3D divideXYZ(Coordinate3D c1, double numValue) {
		return new Coordinate3D(c1.getX() / numValue, 
				c1.getY() / numValue,
				c1.getZ() / numValue,
				c1.getW());
	}

	/**
	 * Sets the z.
	 *
	 * @param c the c
	 * @param z the z
	 * @return the coordinate3 d
	 */
	public static Coordinate3D setZ(Coordinate3D c, double z) {
		return new Coordinate3D(c.getX(), 
				c.getY(),
				z,
				c.getW());
	}
	
	/**
	 * Adds the z.
	 *
	 * @param c the c
	 * @param z the z
	 * @return the coordinate3 d
	 */
	public static Coordinate3D addZ(Coordinate3D c, double z) {
		return new Coordinate3D(c.getX(), 
				c.getY(),
				c.getZ() + z,
				c.getW());
	}
	
	/**
	 * Translate.
	 *
	 * @param c1 the c1
	 * @param c2 the c2
	 * @return the coordinate3 d
	 */
	public static Coordinate3D translate(Coordinate3D c1, Coordinate3D c2) {
		return new Coordinate3D(c1.getX() + c2.getX(), 
				c1.getY() + c2.getY(),
				c1.getZ() + c2.getZ(),
				c1.getW() + c2.getW());
	}
}
