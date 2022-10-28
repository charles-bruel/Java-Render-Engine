package net.chazzvader.core.generic.math;

import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;

/**
 * Three component float vector. Can represent anything. Standard for Euler
 * angles is y=heading/yaw z=attitude/pitch x=bank/roll, <em>applied in that
 * order</em>. Many of the functions modify the objects they were called on, use
 * the copy version of the method to not modify the original.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
@SuppressWarnings("javadoc")
public class Vector3f {

	/**
	 * The 3 values of the vector
	 */
	public float x = 0, y = 0, z = 0;

	/**
	 * Creates a new blank vector
	 */
	public Vector3f() {

	}

	/**
	 * Creates a vector with the specified x, y, and z values
	 * 
	 * @param x The x value
	 * @param y The y value
	 * @param z The z value
	 */
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Creates a vector with the specified x, y, and z values
	 * 
	 * @param x  The z value
	 * @param yz The y and z values, the x components maps to y and the y component
	 *           maps to z
	 */
	public Vector3f(float x, Vector2f yz) {
		this(x, yz.x, yz.y);
	}

	/**
	 * Creates a vector with the specified x, y, and z values
	 * 
	 * @param xy The x and y values
	 * @param z  The z value
	 */
	public Vector3f(Vector2f xy, float z) {
		this(xy.x, xy.y, z);
	}

	/**
	 * Creates and returns a copy of the passed vector
	 * 
	 * @param vec The vector to copy
	 * @return The copy
	 */
	public static Vector3f copyOf(Vector3f vec) {
		return new Vector3f(vec.x, vec.y, vec.z);
	}

	/**
	 * Turns a float array into a vector 3
	 * 
	 * @param array The values to use
	 * @return The array as a vector 3, or a blank vector if the array is null or
	 *         the length is not 3
	 */
	public static Vector3f fromFloatArray(float[] array) {
		if (array == null) {
			Logging.log("Unable to use float[], array is null", "Vector", LoggingLevel.WARN);
			return new Vector3f();
		}
		if (array.length != 3) {
			Logging.log("Unable to use float[], length != 3", "Vector", LoggingLevel.WARN);
			return new Vector3f();
		}
		return new Vector3f(array[0], array[1], array[2]);
	}

	/**
	 * Returns an up vector (0, 1, 0)
	 * 
	 * @return A new up vector
	 */
	public static Vector3f up() {
		return new Vector3f(0, 1, 0);
	}

	/**
	 * Individually adds the x, y, z components of each vector together (the vector
	 * this is called on is affected)
	 * 
	 * @param vec The vector to add
	 * @return The result
	 */
	public Vector3f add(Vector3f vec) {
		x += vec.x;
		y += vec.y;
		z += vec.z;
		return this;
	}

	/**
	 * Individually adds the x, y, z components of each vector together (the vector
	 * this is called on is not affected)
	 * 
	 * @param vec The vector to add
	 * @return The result
	 */
	public Vector3f addCopy(Vector3f vec) {
		return copyOf(this).add(vec);
	}

	/**
	 * Performs cross product on this vector and the passed vector (the vector this
	 * is called on is affected)
	 * 
	 * @param vec The vector to cross product with
	 * @return The cross product
	 */
	public Vector3f crossProduct(Vector3f vec) {
		Vector3f copy = copyOf(this);
		this.x = (copy.y * vec.z) - (copy.z * vec.y);
		this.y = (copy.z * vec.x) - (copy.x * vec.z);
		this.z = (copy.x * vec.y) - (copy.y * vec.x);
		return this;
	}

	/**
	 * Performs cross product on this vector and the passed vector (the vector this
	 * is called on is not affected)
	 * 
	 * @param vec The vector to cross product with
	 * @return The cross product
	 */
	public Vector3f crossProductCopy(Vector3f vec) {
		return copyOf(this).crossProduct(vec);
	}

	/**
	 * Performs dot product on this vector and the passed vector (the vector this is
	 * called on is not affected)
	 * 
	 * @param vec The vector to dot product with
	 * @return The dot product
	 */
	public float dotProduct(Vector3f vec) {
		float result = x * vec.x;
		result += y * vec.y;
		result += z * vec.z;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector3f other = (Vector3f) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		return result;
	}

	/**
	 * Returns the length of the vector. The length of the vector can also be
	 * thought of as the distance between the vector position and 0, 0, 0
	 * 
	 * @return The length
	 */
	public float length() {
		return (float) Math.sqrt((x * x) + (y * y) + (z * z));
	}

	/**
	 * Returns the largest component of the vector.
	 * 
	 * @return The largest component of the vector
	 */
	public float maxValue() {
		if (x >= y && x >= z) {
			return x;
		}
		if (y >= x && y >= z) {
			return y;
		}
		if (z >= x && z >= y) {
			return z;
		}
		return 0;
	}

	/**
	 * Performs scalar multiplication on the vector (the vector this is called on is
	 * affected)
	 * 
	 * @param scalar The scalar to multiply by
	 * @return The result
	 */
	public Vector3f mul(float scalar) {
		x *= scalar;
		y *= scalar;
		z *= scalar;
		return this;
	}

	/**
	 * Individually multiplies the x, y, and z components of each vector (the vector
	 * this is called on is affected)
	 * 
	 * @param vec The vector to multiply
	 * @return The result
	 */
	public Vector3f mul(Vector3f vec) {
		x *= vec.x;
		y *= vec.y;
		z *= vec.z;
		return this;
	}

	/**
	 * Performs scalar multiplication on the vector (the vector this is called on is
	 * not affected)
	 * 
	 * @param scalar The scalar to multiply by
	 * @return The result
	 */
	public Vector3f mulCopy(float scalar) {
		return copyOf(this).mul(scalar);
	}

	/**
	 * Individually multiplies the x, y, and z components of each vector (the vector
	 * this is called on is not affected)
	 * 
	 * @param vec The vector to multiply
	 * @return The result
	 */
	public Vector3f mulCopy(Vector3f vec) {
		return copyOf(this).mul(vec);
	}

	/**
	 * Negates the vector (reverse coordinates) (the vector this is called on is
	 * affected)
	 * 
	 * @return The negated vector
	 */
	public Vector3f negate() {
		return mul(-1);
	}

	/**
	 * Negates the vector (reverse coordinates) (the vector this is called on is not
	 * affected)
	 * 
	 * @return The negated vector
	 */
	public Vector3f negateCopy() {
		return mulCopy(-1);
	}

	/**
	 * Normalizes the vector. The result will a vector with the ratio between
	 * components the same but a length of 1 (the vector this is called on is
	 * affected)
	 * 
	 * @return The normalized vector
	 */
	public Vector3f normalize() {
		float length = length();
		if (length == 0) {
			return this;
		}
		x /= length;
		y /= length;
		z /= length;
		return this;
	}

	/**
	 * Normalizes a vector. The result will a vector with the ratio between
	 * components the same but a length of 1 (the vector this is called on is not
	 * affected)
	 * 
	 * @return The normalized vector
	 */
	public Vector3f normalizeCopy() {
		return copyOf(this).normalize();
	}

	/**
	 * Individually subtracts the x, y, z components of each vector (the vector this
	 * is called on is affected)
	 * 
	 * @param vec The vector to subtract
	 * @return The result
	 */
	public Vector3f sub(Vector3f vec) {
		x -= vec.x;
		y -= vec.y;
		z -= vec.z;
		return this;
	}

	/**
	 * Individually subtracts the x, y, z components of each vector (the vector this
	 * is called on is not affected)
	 * 
	 * @param vec The vector to subtract
	 * @return The result
	 */
	public Vector3f subCopy(Vector3f vec) {
		return copyOf(this).sub(vec);
	}

	/**
	 * Might be broken ¯\_(ツ)_/¯ <br>
	 * <br>
	 * 
	 * Assuming this is a vector representing Euler angles, this returns the
	 * direction vector for the angle (the vector this is called on is not affected)
	 * 
	 * @return The direction vector
	 */
	public Vector3f toDirection() {

		Vector3f euler = copyOf(this);

		euler.y = ((-euler.y - 90) % 360) - 90; // TODO: Figure out why this is nessecary.

//		euler.z = -euler.z - 90;

		euler.z *= -1;

		Vector3f direction = new Vector3f();
		float pitch = euler.z;
		if (pitch > 89.9f)
			pitch = 89.9f;
		if (pitch < -89.9f)
			pitch = -89.9f;
		direction.x = (float) (Math.cos(Math.toRadians(euler.y)) * Math.cos(Math.toRadians(pitch)));
		direction.y = (float) Math.sin(Math.toRadians(pitch));
		direction.z = (float) (Math.sin(Math.toRadians(euler.y)) * Math.cos(Math.toRadians(pitch)));
		direction.normalize();
		return direction;
	}

	/**
	 * Turns the vector into a float array
	 * 
	 * @return The transformed vector
	 */
	public float[] toFloatArray() {
		return new float[] { x, y, z };
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}

	/**
	 * Converts all values in the vector to radians, as if they were measured in
	 * degrees. For example, an X value of 90 becomes PI/2. Useful because most
	 * trigometric functions, and quaternions, use radians. (the vector this is
	 * called on is affected)
	 * 
	 * @return The adjusted vector.
	 */
	public Vector3f toRadians() {
		return mul((float) (Math.PI / 180));
	}

	/**
	 * Converts all values in the vector to radians, as if they were measured in
	 * degrees. For example, an X value of 90 becomes PI/2. Useful because most
	 * trigometric functions, and quaternions, use radians. (the vector this is
	 * called on is not affected)
	 * 
	 * @return The adjusted vector.
	 */
	public Vector3f toRadiansCopy() {
		return copyOf(this).toRadians();
	}

	/**
	 * Converts all values in the vector to degrees, as if they were in radians. For
	 * example, an X value of PI/2 becomes 90. Useful because most trigometric
	 * functions, and quaternions, use radians. (the vector this is called on is
	 * affected)
	 * 
	 * @return The adjusted vector.
	 */
	public Vector3f toDegrees() {
		return mul((float) (180 / Math.PI));
	}

	/**
	 * Converts all values in the vector to degrees, as if they were in radians. For
	 * example, an X value of PI/2 becomes 90. Useful because most trigometric
	 * functions, and quaternions, use radians. (the vector this is called on is not
	 * affected)
	 * 
	 * @return The adjusted vector.
	 */
	public Vector3f toDegreesCopy() {
		return copyOf(this).toDegrees();
	}

	/*
	 * Swizzle
	 */
	public float x() {
		return x;
	}

	public float r() {
		return x;
	}

	public float y() {
		return y;
	}

	public float g() {
		return y;
	}

	public float z() {
		return z;
	}

	public float b() {
		return z;
	}

	public Vector2f xx() {
		return new Vector2f(x, x);
	}

	public Vector2f rr() {
		return new Vector2f(x, x);
	}

	public Vector2f xy() {
		return new Vector2f(x, y);
	}

	public Vector2f rg() {
		return new Vector2f(x, y);
	}

	public Vector2f xz() {
		return new Vector2f(x, z);
	}

	public Vector2f rb() {
		return new Vector2f(x, z);
	}

	public Vector2f yx() {
		return new Vector2f(y, x);
	}

	public Vector2f gr() {
		return new Vector2f(y, x);
	}

	public Vector2f yy() {
		return new Vector2f(y, y);
	}

	public Vector2f gg() {
		return new Vector2f(y, y);
	}

	public Vector2f yz() {
		return new Vector2f(y, z);
	}

	public Vector2f gb() {
		return new Vector2f(y, z);
	}

	public Vector2f zx() {
		return new Vector2f(z, x);
	}

	public Vector2f br() {
		return new Vector2f(z, x);
	}

	public Vector2f zy() {
		return new Vector2f(z, y);
	}

	public Vector2f bg() {
		return new Vector2f(z, y);
	}

	public Vector2f zz() {
		return new Vector2f(z, z);
	}

	public Vector2f bb() {
		return new Vector2f(z, z);
	}

	public Vector3f xxx() {
		return new Vector3f(x, x, x);
	}

	public Vector3f rrr() {
		return new Vector3f(x, x, x);
	}

	public Vector3f xxy() {
		return new Vector3f(x, x, y);
	}

	public Vector3f rrg() {
		return new Vector3f(x, x, y);
	}

	public Vector3f xxz() {
		return new Vector3f(x, x, z);
	}

	public Vector3f rrb() {
		return new Vector3f(x, x, z);
	}

	public Vector3f xyx() {
		return new Vector3f(x, y, x);
	}

	public Vector3f rgr() {
		return new Vector3f(x, y, x);
	}

	public Vector3f xyy() {
		return new Vector3f(x, y, y);
	}

	public Vector3f rgg() {
		return new Vector3f(x, y, y);
	}

	public Vector3f xyz() {
		return new Vector3f(x, y, z);
	}

	public Vector3f rgb() {
		return new Vector3f(x, y, z);
	}

	public Vector3f xzx() {
		return new Vector3f(x, z, x);
	}

	public Vector3f rbr() {
		return new Vector3f(x, z, x);
	}

	public Vector3f xzy() {
		return new Vector3f(x, z, y);
	}

	public Vector3f rbg() {
		return new Vector3f(x, z, y);
	}

	public Vector3f xzz() {
		return new Vector3f(x, z, z);
	}

	public Vector3f rbb() {
		return new Vector3f(x, z, z);
	}

	public Vector3f yxx() {
		return new Vector3f(y, x, x);
	}

	public Vector3f grr() {
		return new Vector3f(y, x, x);
	}

	public Vector3f yxy() {
		return new Vector3f(y, x, y);
	}

	public Vector3f grg() {
		return new Vector3f(y, x, y);
	}

	public Vector3f yxz() {
		return new Vector3f(y, x, z);
	}

	public Vector3f grb() {
		return new Vector3f(y, x, z);
	}

	public Vector3f yyx() {
		return new Vector3f(y, y, x);
	}

	public Vector3f ggr() {
		return new Vector3f(y, y, x);
	}

	public Vector3f yyy() {
		return new Vector3f(y, y, y);
	}

	public Vector3f ggg() {
		return new Vector3f(y, y, y);
	}

	public Vector3f yyz() {
		return new Vector3f(y, y, z);
	}

	public Vector3f ggb() {
		return new Vector3f(y, y, z);
	}

	public Vector3f yzx() {
		return new Vector3f(y, z, x);
	}

	public Vector3f gbr() {
		return new Vector3f(y, z, x);
	}

	public Vector3f yzy() {
		return new Vector3f(y, z, y);
	}

	public Vector3f gbg() {
		return new Vector3f(y, z, y);
	}

	public Vector3f yzz() {
		return new Vector3f(y, z, z);
	}

	public Vector3f gbb() {
		return new Vector3f(y, z, z);
	}

	public Vector3f zxx() {
		return new Vector3f(z, x, x);
	}

	public Vector3f brr() {
		return new Vector3f(z, x, x);
	}

	public Vector3f zxy() {
		return new Vector3f(z, x, y);
	}

	public Vector3f brg() {
		return new Vector3f(z, x, y);
	}

	public Vector3f zxz() {
		return new Vector3f(z, x, z);
	}

	public Vector3f brb() {
		return new Vector3f(z, x, z);
	}

	public Vector3f zyx() {
		return new Vector3f(z, y, x);
	}

	public Vector3f bgr() {
		return new Vector3f(z, y, x);
	}

	public Vector3f zyy() {
		return new Vector3f(z, y, y);
	}

	public Vector3f bgg() {
		return new Vector3f(z, y, y);
	}

	public Vector3f zyz() {
		return new Vector3f(z, y, z);
	}

	public Vector3f bgb() {
		return new Vector3f(z, y, z);
	}

	public Vector3f zzx() {
		return new Vector3f(z, z, x);
	}

	public Vector3f bbr() {
		return new Vector3f(z, z, x);
	}

	public Vector3f zzy() {
		return new Vector3f(z, z, y);
	}

	public Vector3f bbg() {
		return new Vector3f(z, z, y);
	}

	public Vector3f zzz() {
		return new Vector3f(z, z, z);
	}

	public Vector3f bbb() {
		return new Vector3f(z, z, z);
	}

	public Vector4f xxxx() {
		return new Vector4f(x, x, x, x);
	}

	public Vector4f rrrr() {
		return new Vector4f(x, x, x, x);
	}

	public Vector4f xxxy() {
		return new Vector4f(x, x, x, y);
	}

	public Vector4f rrrg() {
		return new Vector4f(x, x, x, y);
	}

	public Vector4f xxxz() {
		return new Vector4f(x, x, x, z);
	}

	public Vector4f rrrb() {
		return new Vector4f(x, x, x, z);
	}

	public Vector4f xxyx() {
		return new Vector4f(x, x, y, x);
	}

	public Vector4f rrgr() {
		return new Vector4f(x, x, y, x);
	}

	public Vector4f xxyy() {
		return new Vector4f(x, x, y, y);
	}

	public Vector4f rrgg() {
		return new Vector4f(x, x, y, y);
	}

	public Vector4f xxyz() {
		return new Vector4f(x, x, y, z);
	}

	public Vector4f rrgb() {
		return new Vector4f(x, x, y, z);
	}

	public Vector4f xxzx() {
		return new Vector4f(x, x, z, x);
	}

	public Vector4f rrbr() {
		return new Vector4f(x, x, z, x);
	}

	public Vector4f xxzy() {
		return new Vector4f(x, x, z, y);
	}

	public Vector4f rrbg() {
		return new Vector4f(x, x, z, y);
	}

	public Vector4f xxzz() {
		return new Vector4f(x, x, z, z);
	}

	public Vector4f rrbb() {
		return new Vector4f(x, x, z, z);
	}

	public Vector4f xyxx() {
		return new Vector4f(x, y, x, x);
	}

	public Vector4f rgrr() {
		return new Vector4f(x, y, x, x);
	}

	public Vector4f xyxy() {
		return new Vector4f(x, y, x, y);
	}

	public Vector4f rgrg() {
		return new Vector4f(x, y, x, y);
	}

	public Vector4f xyxz() {
		return new Vector4f(x, y, x, z);
	}

	public Vector4f rgrb() {
		return new Vector4f(x, y, x, z);
	}

	public Vector4f xyyx() {
		return new Vector4f(x, y, y, x);
	}

	public Vector4f rggr() {
		return new Vector4f(x, y, y, x);
	}

	public Vector4f xyyy() {
		return new Vector4f(x, y, y, y);
	}

	public Vector4f rggg() {
		return new Vector4f(x, y, y, y);
	}

	public Vector4f xyyz() {
		return new Vector4f(x, y, y, z);
	}

	public Vector4f rggb() {
		return new Vector4f(x, y, y, z);
	}

	public Vector4f xyzx() {
		return new Vector4f(x, y, z, x);
	}

	public Vector4f rgbr() {
		return new Vector4f(x, y, z, x);
	}

	public Vector4f xyzy() {
		return new Vector4f(x, y, z, y);
	}

	public Vector4f rgbg() {
		return new Vector4f(x, y, z, y);
	}

	public Vector4f xyzz() {
		return new Vector4f(x, y, z, z);
	}

	public Vector4f rgbb() {
		return new Vector4f(x, y, z, z);
	}

	public Vector4f xzxx() {
		return new Vector4f(x, z, x, x);
	}

	public Vector4f rbrr() {
		return new Vector4f(x, z, x, x);
	}

	public Vector4f xzxy() {
		return new Vector4f(x, z, x, y);
	}

	public Vector4f rbrg() {
		return new Vector4f(x, z, x, y);
	}

	public Vector4f xzxz() {
		return new Vector4f(x, z, x, z);
	}

	public Vector4f rbrb() {
		return new Vector4f(x, z, x, z);
	}

	public Vector4f xzyx() {
		return new Vector4f(x, z, y, x);
	}

	public Vector4f rbgr() {
		return new Vector4f(x, z, y, x);
	}

	public Vector4f xzyy() {
		return new Vector4f(x, z, y, y);
	}

	public Vector4f rbgg() {
		return new Vector4f(x, z, y, y);
	}

	public Vector4f xzyz() {
		return new Vector4f(x, z, y, z);
	}

	public Vector4f rbgb() {
		return new Vector4f(x, z, y, z);
	}

	public Vector4f xzzx() {
		return new Vector4f(x, z, z, x);
	}

	public Vector4f rbbr() {
		return new Vector4f(x, z, z, x);
	}

	public Vector4f xzzy() {
		return new Vector4f(x, z, z, y);
	}

	public Vector4f rbbg() {
		return new Vector4f(x, z, z, y);
	}

	public Vector4f xzzz() {
		return new Vector4f(x, z, z, z);
	}

	public Vector4f rbbb() {
		return new Vector4f(x, z, z, z);
	}

	public Vector4f yxxx() {
		return new Vector4f(y, x, x, x);
	}

	public Vector4f grrr() {
		return new Vector4f(y, x, x, x);
	}

	public Vector4f yxxy() {
		return new Vector4f(y, x, x, y);
	}

	public Vector4f grrg() {
		return new Vector4f(y, x, x, y);
	}

	public Vector4f yxxz() {
		return new Vector4f(y, x, x, z);
	}

	public Vector4f grrb() {
		return new Vector4f(y, x, x, z);
	}

	public Vector4f yxyx() {
		return new Vector4f(y, x, y, x);
	}

	public Vector4f grgr() {
		return new Vector4f(y, x, y, x);
	}

	public Vector4f yxyy() {
		return new Vector4f(y, x, y, y);
	}

	public Vector4f grgg() {
		return new Vector4f(y, x, y, y);
	}

	public Vector4f yxyz() {
		return new Vector4f(y, x, y, z);
	}

	public Vector4f grgb() {
		return new Vector4f(y, x, y, z);
	}

	public Vector4f yxzx() {
		return new Vector4f(y, x, z, x);
	}

	public Vector4f grbr() {
		return new Vector4f(y, x, z, x);
	}

	public Vector4f yxzy() {
		return new Vector4f(y, x, z, y);
	}

	public Vector4f grbg() {
		return new Vector4f(y, x, z, y);
	}

	public Vector4f yxzz() {
		return new Vector4f(y, x, z, z);
	}

	public Vector4f grbb() {
		return new Vector4f(y, x, z, z);
	}

	public Vector4f yyxx() {
		return new Vector4f(y, y, x, x);
	}

	public Vector4f ggrr() {
		return new Vector4f(y, y, x, x);
	}

	public Vector4f yyxy() {
		return new Vector4f(y, y, x, y);
	}

	public Vector4f ggrg() {
		return new Vector4f(y, y, x, y);
	}

	public Vector4f yyxz() {
		return new Vector4f(y, y, x, z);
	}

	public Vector4f ggrb() {
		return new Vector4f(y, y, x, z);
	}

	public Vector4f yyyx() {
		return new Vector4f(y, y, y, x);
	}

	public Vector4f gggr() {
		return new Vector4f(y, y, y, x);
	}

	public Vector4f yyyy() {
		return new Vector4f(y, y, y, y);
	}

	public Vector4f gggg() {
		return new Vector4f(y, y, y, y);
	}

	public Vector4f yyyz() {
		return new Vector4f(y, y, y, z);
	}

	public Vector4f gggb() {
		return new Vector4f(y, y, y, z);
	}

	public Vector4f yyzx() {
		return new Vector4f(y, y, z, x);
	}

	public Vector4f ggbr() {
		return new Vector4f(y, y, z, x);
	}

	public Vector4f yyzy() {
		return new Vector4f(y, y, z, y);
	}

	public Vector4f ggbg() {
		return new Vector4f(y, y, z, y);
	}

	public Vector4f yyzz() {
		return new Vector4f(y, y, z, z);
	}

	public Vector4f ggbb() {
		return new Vector4f(y, y, z, z);
	}

	public Vector4f yzxx() {
		return new Vector4f(y, z, x, x);
	}

	public Vector4f gbrr() {
		return new Vector4f(y, z, x, x);
	}

	public Vector4f yzxy() {
		return new Vector4f(y, z, x, y);
	}

	public Vector4f gbrg() {
		return new Vector4f(y, z, x, y);
	}

	public Vector4f yzxz() {
		return new Vector4f(y, z, x, z);
	}

	public Vector4f gbrb() {
		return new Vector4f(y, z, x, z);
	}

	public Vector4f yzyx() {
		return new Vector4f(y, z, y, x);
	}

	public Vector4f gbgr() {
		return new Vector4f(y, z, y, x);
	}

	public Vector4f yzyy() {
		return new Vector4f(y, z, y, y);
	}

	public Vector4f gbgg() {
		return new Vector4f(y, z, y, y);
	}

	public Vector4f yzyz() {
		return new Vector4f(y, z, y, z);
	}

	public Vector4f gbgb() {
		return new Vector4f(y, z, y, z);
	}

	public Vector4f yzzx() {
		return new Vector4f(y, z, z, x);
	}

	public Vector4f gbbr() {
		return new Vector4f(y, z, z, x);
	}

	public Vector4f yzzy() {
		return new Vector4f(y, z, z, y);
	}

	public Vector4f gbbg() {
		return new Vector4f(y, z, z, y);
	}

	public Vector4f yzzz() {
		return new Vector4f(y, z, z, z);
	}

	public Vector4f gbbb() {
		return new Vector4f(y, z, z, z);
	}

	public Vector4f zxxx() {
		return new Vector4f(z, x, x, x);
	}

	public Vector4f brrr() {
		return new Vector4f(z, x, x, x);
	}

	public Vector4f zxxy() {
		return new Vector4f(z, x, x, y);
	}

	public Vector4f brrg() {
		return new Vector4f(z, x, x, y);
	}

	public Vector4f zxxz() {
		return new Vector4f(z, x, x, z);
	}

	public Vector4f brrb() {
		return new Vector4f(z, x, x, z);
	}

	public Vector4f zxyx() {
		return new Vector4f(z, x, y, x);
	}

	public Vector4f brgr() {
		return new Vector4f(z, x, y, x);
	}

	public Vector4f zxyy() {
		return new Vector4f(z, x, y, y);
	}

	public Vector4f brgg() {
		return new Vector4f(z, x, y, y);
	}

	public Vector4f zxyz() {
		return new Vector4f(z, x, y, z);
	}

	public Vector4f brgb() {
		return new Vector4f(z, x, y, z);
	}

	public Vector4f zxzx() {
		return new Vector4f(z, x, z, x);
	}

	public Vector4f brbr() {
		return new Vector4f(z, x, z, x);
	}

	public Vector4f zxzy() {
		return new Vector4f(z, x, z, y);
	}

	public Vector4f brbg() {
		return new Vector4f(z, x, z, y);
	}

	public Vector4f zxzz() {
		return new Vector4f(z, x, z, z);
	}

	public Vector4f brbb() {
		return new Vector4f(z, x, z, z);
	}

	public Vector4f zyxx() {
		return new Vector4f(z, y, x, x);
	}

	public Vector4f bgrr() {
		return new Vector4f(z, y, x, x);
	}

	public Vector4f zyxy() {
		return new Vector4f(z, y, x, y);
	}

	public Vector4f bgrg() {
		return new Vector4f(z, y, x, y);
	}

	public Vector4f zyxz() {
		return new Vector4f(z, y, x, z);
	}

	public Vector4f bgrb() {
		return new Vector4f(z, y, x, z);
	}

	public Vector4f zyyx() {
		return new Vector4f(z, y, y, x);
	}

	public Vector4f bggr() {
		return new Vector4f(z, y, y, x);
	}

	public Vector4f zyyy() {
		return new Vector4f(z, y, y, y);
	}

	public Vector4f bggg() {
		return new Vector4f(z, y, y, y);
	}

	public Vector4f zyyz() {
		return new Vector4f(z, y, y, z);
	}

	public Vector4f bggb() {
		return new Vector4f(z, y, y, z);
	}

	public Vector4f zyzx() {
		return new Vector4f(z, y, z, x);
	}

	public Vector4f bgbr() {
		return new Vector4f(z, y, z, x);
	}

	public Vector4f zyzy() {
		return new Vector4f(z, y, z, y);
	}

	public Vector4f bgbg() {
		return new Vector4f(z, y, z, y);
	}

	public Vector4f zyzz() {
		return new Vector4f(z, y, z, z);
	}

	public Vector4f bgbb() {
		return new Vector4f(z, y, z, z);
	}

	public Vector4f zzxx() {
		return new Vector4f(z, z, x, x);
	}

	public Vector4f bbrr() {
		return new Vector4f(z, z, x, x);
	}

	public Vector4f zzxy() {
		return new Vector4f(z, z, x, y);
	}

	public Vector4f bbrg() {
		return new Vector4f(z, z, x, y);
	}

	public Vector4f zzxz() {
		return new Vector4f(z, z, x, z);
	}

	public Vector4f bbrb() {
		return new Vector4f(z, z, x, z);
	}

	public Vector4f zzyx() {
		return new Vector4f(z, z, y, x);
	}

	public Vector4f bbgr() {
		return new Vector4f(z, z, y, x);
	}

	public Vector4f zzyy() {
		return new Vector4f(z, z, y, y);
	}

	public Vector4f bbgg() {
		return new Vector4f(z, z, y, y);
	}

	public Vector4f zzyz() {
		return new Vector4f(z, z, y, z);
	}

	public Vector4f bbgb() {
		return new Vector4f(z, z, y, z);
	}

	public Vector4f zzzx() {
		return new Vector4f(z, z, z, x);
	}

	public Vector4f bbbr() {
		return new Vector4f(z, z, z, x);
	}

	public Vector4f zzzy() {
		return new Vector4f(z, z, z, y);
	}

	public Vector4f bbbg() {
		return new Vector4f(z, z, z, y);
	}

	public Vector4f zzzz() {
		return new Vector4f(z, z, z, z);
	}

	public Vector4f bbbb() {
		return new Vector4f(z, z, z, z);
	}

	public void xyz(Vector3f vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
	}

	public void rgb(Vector3f vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
	}

	public void xzy(Vector3f vec) {
		x = vec.x;
		z = vec.y;
		y = vec.z;
	}

	public void rbg(Vector3f vec) {
		x = vec.x;
		z = vec.y;
		y = vec.z;
	}

	public void yxz(Vector3f vec) {
		y = vec.x;
		x = vec.y;
		z = vec.z;
	}

	public void grb(Vector3f vec) {
		y = vec.x;
		x = vec.y;
		z = vec.z;
	}

	public void yzx(Vector3f vec) {
		y = vec.x;
		z = vec.y;
		x = vec.z;
	}

	public void gbr(Vector3f vec) {
		y = vec.x;
		z = vec.y;
		x = vec.z;
	}

	public void zxy(Vector3f vec) {
		z = vec.x;
		x = vec.y;
		y = vec.z;
	}

	public void brg(Vector3f vec) {
		z = vec.x;
		x = vec.y;
		y = vec.z;
	}

	public void zyx(Vector3f vec) {
		z = vec.x;
		y = vec.y;
		x = vec.z;
	}

	public void bgr(Vector3f vec) {
		z = vec.x;
		y = vec.y;
		x = vec.z;
	}

	public void yz(Vector2f vec) {
		y = vec.x;
		z = vec.y;
	}

	public void gb(Vector2f vec) {
		y = vec.x;
		z = vec.y;
	}

	public void zy(Vector2f vec) {
		z = vec.x;
		y = vec.y;
	}

	public void bg(Vector2f vec) {
		z = vec.x;
		y = vec.y;
	}

	public void z(float f) {
		z = f;
	}

	public void b(float f) {
		z = f;
	}

	public void y(float f) {
		y = f;
	}

	public void g(float f) {
		y = f;
	}

	public void xz(Vector2f vec) {
		x = vec.x;
		z = vec.y;
	}

	public void rb(Vector2f vec) {
		x = vec.x;
		z = vec.y;
	}

	public void zx(Vector2f vec) {
		z = vec.x;
		x = vec.y;
	}

	public void br(Vector2f vec) {
		z = vec.x;
		x = vec.y;
	}

	public void x(float f) {
		x = f;
	}

	public void r(float f) {
		x = f;
	}

	public void xy(Vector2f vec) {
		x = vec.x;
		y = vec.y;
	}

	public void rg(Vector2f vec) {
		x = vec.x;
		y = vec.y;
	}

	public void yx(Vector2f vec) {
		y = vec.x;
		x = vec.y;
	}

	public void gr(Vector2f vec) {
		y = vec.x;
		x = vec.y;
	}
}
