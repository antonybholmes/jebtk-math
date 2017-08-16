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
package org.jebtk.math.statistics;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.inference.TestUtils;
import org.jebtk.core.collections.CollectionUtils;

// TODO: Auto-generated Javadoc
/**
 * Perform T-tests making use of the apache commons lib.
 * 
 * @author Antony Holmes Holmes
 *
 */
public class TTest {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		List<Double> v1 = new ArrayList<Double>();
		
		/*
		v1.add(3.0);
		v1.add(4.0);
		v1.add(5.0);
		v1.add(8.0);
		v1.add(9.0);
		v1.add(1.0);
		v1.add(2.0);
		v1.add(4.0);
		v1.add(5.0);
		*/
		
		v1.add(30.02);
		v1.add(29.99);
		v1.add(30.11);
		v1.add(29.97);
		v1.add(30.01);
		v1.add(29.99);
		
		List<Double> v2 = new ArrayList<Double>();
		
		/*
		v2.add(6.0);
		v2.add(19.0);
		v2.add(3.0);
		v2.add(2.0);
		v2.add(14.0);
		v2.add(4.0);
		v2.add(5.0);
		v2.add(17.0);
		v2.add(1.0);
		*/
		
		v2.add(29.89);
		v2.add(29.93);
		v2.add(29.72);
		v2.add(29.98);
		v2.add(30.02);
		v2.add(29.98);
		
		System.err.println(twoTailUnequalVarianceTTest(v1, v2));
	}
	
	/**
	 * Returns the t-statistic for two samples.
	 *
	 * @param s1 the s1
	 * @param s2 the s2
	 * @return the double
	 */
	public static double tStat(List<Double> s1, List<Double> s2) {
		
		double[] sample1 = CollectionUtils.toDoublePrimitive(s1);
		
		double[] sample2 = CollectionUtils.toDoublePrimitive(s2);
		
		return TestUtils.t(sample1, sample2);
	}
	
	/**
	 * TTest unequal variance, heteroscedastic.
	 *
	 * @param s1 the s1
	 * @param s2 the s2
	 * @return the double
	 */
	public static double twoTailUnequalVarianceTTest(List<Double> s1, List<Double> s2) {
		
		double[] sample1 = CollectionUtils.toDoublePrimitive(s1);
		
		double[] sample2 = CollectionUtils.toDoublePrimitive(s2);
		
		return TestUtils.tTest(sample1, sample2);
	}
	
	/**
	 * TTest equal variance, heteroscedastic.
	 *
	 * @param s1 the s1
	 * @param s2 the s2
	 * @return the double
	 */
	public static double twoTailEqualVarianceTTest(List<Double> s1, List<Double> s2) {
		
		double[] sample1 = CollectionUtils.toDoublePrimitive(s1);
		
		double[] sample2 = CollectionUtils.toDoublePrimitive(s2);
		
		return TestUtils.homoscedasticTTest(sample1, sample2);
	}
}
