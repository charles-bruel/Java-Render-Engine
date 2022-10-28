package net.chazzvader.core.generic.math;

/**
 * Many static, math-related, math utilities. Hence the name.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class MathUtils {
	
	private MathUtils() {
	}

	/**
	 * Measures tolerance, i.e. are <code>v1</code> and <code>v2</code> within a certain tolerance of each other.
	 * @param v1 The first value.
	 * @param v2 The second value.
	 * @param tolerance The tolerance, how far apart the two values can be and still return true.
	 * @return True if the two values are within the tolerance of each other.
	 */
	public static boolean tolerance(double v1, double v2, double tolerance) {
		return Math.abs(v1 - v2) <= tolerance;
	}

}
