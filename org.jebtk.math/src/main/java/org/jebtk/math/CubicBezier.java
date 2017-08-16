package org.jebtk.math;

import java.awt.geom.Point2D;

import org.jebtk.core.geom.DoublePos;

/**
 * Evaluate a point on a cubic bezier curve.
 * 
 * @author antony
 *
 */
public class CubicBezier {
	
	private static final DoublePos P0 = new DoublePos(0, 0);
	private static final DoublePos P3 = new DoublePos(1, 1);

	private double[] mX = new double[4];
	private double[] mY = new double[4];

	public CubicBezier(DoublePos p0, 
			DoublePos p1, 
			DoublePos p2, 
			DoublePos p3) {
		mX[0] = p0.getX();
		mX[1] = p1.getX();
		mX[2] = p2.getX();
		mX[3] = p3.getX();

		mY[0] = p0.getY();
		mY[1] = p1.getY();
		mY[2] = p2.getY();
		mY[3] = p3.getY();
	}

	public CubicBezier(Point2D.Double p0, 
			Point2D.Double p1, 
			Point2D.Double p2, 
			Point2D.Double p3) {
		mX[0] = p0.x;
		mX[1] = p1.x;
		mX[2] = p2.x;
		mX[3] = p3.x;

		mY[0] = p0.y;
		mY[1] = p1.y;
		mY[2] = p2.y;
		mY[3] = p3.y;
	}
	
	public CubicBezier(double x0,
			double y0,
			double x1,
			double y1,
			double x2,
			double y2,
			double x3,
			double y3) {
		mX[0] = x0;
		mX[1] = x1;
		mX[2] = x2;
		mX[3] = x3;

		mY[0] = y0;
		mY[1] = y1;
		mY[2] = y2;
		mY[3] = y3;
	}

	public DoublePos eval(double t) {
		double u = 1 - t;
		double tt = t*t;
		double uu = u*u;
		double uuu = uu * u;
		double ttt = tt * t;

		return new DoublePos(evalX(t, u, tt, uu, uuu, ttt), 
				evalY(t, u, tt, uu, uuu, ttt));
	}

	private double evalX(double t, 
			double u,
			double tt,
			double uu,
			double uuu,
			double ttt) {
		double x = uuu * mX[0]; //first term
		x += 3 * uu * t * mX[1]; //second term
		x += 3 * u * tt * mX[2]; //third term
		x += ttt * mX[3]; //fourth term

		return x;
	}
	
	private double evalY(double t, 
			double u,
			double tt,
			double uu,
			double uuu,
			double ttt) {
		double y = uuu * mY[0]; //first term
		y += 3 * uu * t * mY[1]; //second term
		y += 3 * u * tt * mY[2]; //third term
		y += ttt * mY[3]; //fourth term

		return y;
	}
	
	/**
	 * Return a normalized Cubic Bezier where the start and end points are
	 * fixed at (0, 0) and (1, 1) respectively.
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static CubicBezier normCubicBezier(DoublePos p1, DoublePos p2) {
		return new CubicBezier(P0, p1, p2, P3);
	}
	
	public static CubicBezier normCubicBezier(double x1, 
			double y1, 
			double x2, 
			double y2) {
		return new CubicBezier(0, 0, x1, y1, x2, y2, 1, 1);
	}
}
