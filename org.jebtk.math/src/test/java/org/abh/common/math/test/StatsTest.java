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

import org.jebtk.math.statistics.Stats;
import org.junit.Assert;
import org.junit.Test;

// TODO: Auto-generated Javadoc
/**
 * The Class StatsTest.
 */
public class StatsTest {
	
	/**
	 * Percentile test.
	 */
	@Test
	public void percentileTest() {
		double[] values = {20, 15, 40, 35, 50};
		
		double p25 = new Stats(values).percentile(25);
		
		System.err.println("p " + p25);
		
		Assert.assertEquals("Percentile 0.25 = 17.5", p25, 17.5, 0);
	}
	
	/**
	 * Median test.
	 */
	@Test
	public void medianTest() {
		double[] values = {1,3,3,6,7,8,9};
		
		double m = new Stats(values).median();
		
		System.err.println("m " + m);
		
		Assert.assertEquals("median == 6", m, 6, 0);
	}
	
	/**
	 * Quart coeff dist test.
	 */
	@Test
	public void quartCoeffDistTest() {
		double[] values = {1.8, 2, 2.1, 2.4, 2.6, 2.9, 3};
		
		double m = new Stats(values).quartCoeffDisp();
		
		System.err.println("quartCoeffDist " + m);
		
		Assert.assertEquals("quartCoeffDist == 0.18", m, 0.18, 0.01);
	}
}
