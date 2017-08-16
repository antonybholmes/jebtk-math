package org.abh.common.math.test;

import org.jebtk.math.CubicBezier;
import org.junit.Test;

public class MathTest {
	@Test
	public void normCubicBezier() {
		CubicBezier c = CubicBezier.normCubicBezier(0.4, 0.0, 0.2, 1);
		
		for (double i = 0; i <= 1; i += 0.1) {
			System.err.println("bezier " + c.eval(i));
		}
	}
}
