package incubator;

import java.awt.Point;
import java.awt.geom.Point2D;

import java.lang.reflect.Accessor;

public class SimpleSubjectToChange {

	private double weight = 1.0f;
	private Point2D p;

	public SimpleSubjectToChange() {
		this.p = new Point2D.Double();
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double newValue) {
		if (newValue <= 0) {
			throw new IllegalArgumentException(
					"Setting weight with a value of 0 or less than 0 is not allowed");
		}
		double ration = weight / this.weight;
		this.p.setLocation(p.getX() * ration, p.getY() * ration);
		this.weight = newValue;
	}

	public double getDistanceToCenter() {
		return p.distance(new Point());
	}

	private void setPoint(Point point) {
		this.p.setLocation(point.x / weight, point.y / weight);
	}

	// Some simple self-Testing
	public static void main(String[] args) {
		System.out.println("Starting SelfTest of " + SimpleSubjectToChange.class);
		SimpleSubjectToChange s = new SimpleSubjectToChange();
		s.setWeight(2d);
		s.setPoint(new Point(2, 2));
		test(s.getDistanceToCenter(), Math.sqrt(2), 0.1);
		test(s.getUnweightedX(),2,0.1);
		test(s.getUnweightedY(),2,0.1);
		s.setWeight(4d);
		test(s.getDistanceToCenter(), Math.sqrt(2), 0.1);
		test(s.getUnweightedX(),4,0.1);
		test(s.getUnweightedY(),4,0.1);
		s.setUnweightedX(2);
		s.setUnweightedY(2);
		test(s.getDistanceToCenter(), Math.sqrt(0.5), 0.1);
		test(s.getUnweightedX(),2,0.1);
		test(s.getUnweightedY(),2,0.1);
		System.out.println("End SelfTest");
	}

	private static void test(double f, double exp, double r) {
		if (Math.abs(f - exp) > r) {
			System.out.println("Unexpected Value: " + f + " Expected is " + exp
					+ " in a range of " + r);
		}
	}

	// Accessor for old Versions

	@Accessor("x")
	@Deprecated
	public float getUnweightedX() {
		return (float) (p.getX() * weight);
	}

	@Accessor("x")
	@Deprecated
	public void setUnweightedX(float newValue) {
		p.setLocation(newValue / weight, p.getY());
	}

	@Accessor("y")
	@Deprecated
	public float getUnweightedY() {
		return (float) (p.getY() * weight);
	}

	@Accessor("y")
	@Deprecated
	public void setUnweightedY(float newValue) {
		p.setLocation(p.getX(), newValue / weight);
	}

	@Accessor("weight")
	@Deprecated
	public float getLowPrecisionWeight() {
		return (float) weight;
	}

	@Accessor("weight")
	@Deprecated
	public void setWeight(float newValue) {
		setWeight((double) newValue);
	}

}
