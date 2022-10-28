package net.chazzvader.core.generic.math;

/**
 * Many static, geometry math equations and things.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class Geometry {

	private Geometry() {
	}

	/**
	 * Checks the 4 given points are
	 * <a href="https://en.wikipedia.org/wiki/Coplanarity">coplanar</a>.<br>
	 * Source: <a href=
	 * "https://www.geeksforgeeks.org/program-to-check-whether-4-points-in-a-3-d-plane-are-coplanar/">GeeksForGeeks</a>
	 * @param p1 The first point
	 * @param p2 The second point
	 * @param p3 The third point
	 * @param p4 The fourth point
	 * @return True if the points are coplanar and false if they aren't
	 */
	public static boolean coplanar(Vector3f p1, Vector3f p2, Vector3f p3, Vector3f p4) {
		float a1 = p2.x - p1.x;
		float b1 = p2.y - p1.y;
		float c1 = p2.z - p1.z;
		float a2 = p3.x - p1.x;
		float b2 = p3.y - p1.y;
		float c2 = p3.z - p1.z;
		float a = b1 * c2 - b2 * c1;
		float b = a2 * c1 - a1 * c2;
		float c = a1 * b2 - b1 * a2;
		float d = (-a * p1.x - b * p1.y - c * p1.z);

		// equation of plane is: a*x + b*y + c*z = 0

		// checking if the 4th point satisfies
		// the above equation
		if (MathUtils.tolerance(a * p4.x + b * p4.y + c * p4.z + d, 0, 0.0001d))
			return true;
		else
			return false;
	}

	/**
	 * Returns a normal for the triangle given by the 3 points. Loosely based on
	 * <a href=
	 * "https://www.khronos.org/opengl/wiki/Calculating_a_Surface_Normal">this</a>
	 * 
	 * @param p1 The first point
	 * @param p2 The second point
	 * @param p3 The third point
	 * @return The normal vector
	 */
	public static Vector3f calculateNormal(Vector3f p1, Vector3f p2, Vector3f p3) {
//		return p2.subCopy(p1).normalize().crossProduct(p3.subCopy(p1).normalize()).normalize();
		Vector3f ret = new Vector3f();
		
		Vector3f u = p2.subCopy(p1);
		Vector3f v = p3.subCopy(p1);
		
		ret.x = u.y * v.z - u.z * v.y;
		ret.y = u.z * v.x - u.x * v.z;
		ret.z = u.x * v.y - u.y * v.x;
		
		return ret.normalize();
	}

}
