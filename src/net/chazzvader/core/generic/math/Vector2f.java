package net.chazzvader.core.generic.math;

import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;

/**
 * Two component float vector. Can represent anything. Many of the functions
 * modify the objects they were called on, use the copy version of the method to
 * not modify the original.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
@SuppressWarnings("javadoc")
public class Vector2f {

	/**
	 * The 2 values of the vector
	 */
	public float x = 0, y = 0;

	/**
	 * Creates a new blank vector
	 */
	public Vector2f() {

	}

	/**
	 * Creates a vector with the specified x and y values
	 * 
	 * @param x The x value
	 * @param y The y value
	 */
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Creates a vector with the values of a int vector.
	 * 
	 * @param vec The Vector2i.
	 */
	public Vector2f(Vector2i vec) {
		this.x = vec.x;
		this.y = vec.y;
	}

	/**
	 * Creates and returns a copy of the passed vector.
	 * 
	 * @param vec The vector to copy
	 * @return The copy
	 */
	public static Vector2f copyOf(Vector2f vec) {
		return new Vector2f(vec.x, vec.y);
	}

	/**
	 * Turns a float array into a vector 2
	 * 
	 * @param array The values to use
	 * @return The array as a vector 2, or a blank vector if the array is null or
	 *         the length is not 2
	 */
	public static Vector2f fromFloatArray(float[] array) {
		if (array == null) {
			Logging.log("Unable to use float[], array is null", "Vector", LoggingLevel.WARN);
			return new Vector2f();
		}
		if (array.length != 2) {
			Logging.log("Unable to use float[], length != 2", "Vector", LoggingLevel.WARN);
			return new Vector2f();
		}
		return new Vector2f(array[0], array[1]);
	}

	/**
	 * Individually adds the x, and y components of each vector together (the vector
	 * this is called on is affected)
	 * 
	 * @param vec The vector to add
	 * @return The result
	 */
	public Vector2f add(Vector2f vec) {
		x += vec.x;
		y += vec.y;
		return this;
	}

	/**
	 * Individually adds the x, and y components of each vector together (the vector
	 * this is called on is not affected)
	 * 
	 * @param vec The vector to add
	 * @return The result
	 */
	public Vector2f addCopy(Vector2f vec) {
		return copyOf(this).add(vec);
	}

	/**
	 * Performs dot product on this vector and the passed vector (the vector this is
	 * called on is not affected)
	 * 
	 * @param vec The vector to dot product with
	 * @return The dot product
	 */
	public float dotProduct(Vector2f vec) {
		float result = x * vec.x;
		result += y * vec.y;
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
		Vector2f other = (Vector2f) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	/**
	 * Returns the length of the vector. The length of the vector can also be
	 * thought of as the distance between the vector position and 0, 0
	 * 
	 * @return The length
	 */
	public float length() {
		return (float) Math.sqrt((x * x) + (y * y));
	}

	/**
	 * Returns the largest component of the vector
	 * 
	 * @return The largest component of the vector
	 */
	public float maxValue() {
		return x >= y ? x : y;
	}

	/**
	 * Performs scalar multiplication on the vector (the vector this is called on is
	 * affected)
	 * 
	 * @param scalar The scalar to multiply by
	 * @return The result
	 */
	public Vector2f mul(float scalar) {
		x *= scalar;
		y *= scalar;
		return this;
	}

	/**
	 * Individually multiplies the x and y components of each vector (the vector
	 * this is called on is affected)
	 * 
	 * @param vec The vector to multiply
	 * @return The result
	 */
	public Vector2f mul(Vector2f vec) {
		x *= vec.x;
		y *= vec.y;
		return this;
	}

	/**
	 * Performs scalar multiplication on the vector (the vector this is called on is
	 * not affected)
	 * 
	 * @param scalar The scalar to multiply by
	 * @return The result
	 */
	public Vector2f mulCopy(float scalar) {
		return copyOf(this).mul(scalar);
	}

	/**
	 * Individually multiplies the x and y components of each vector (the vector
	 * this is called on is not affected)
	 * 
	 * @param vec The vector to multiply
	 * @return The result
	 */
	public Vector2f mulCopy(Vector2f vec) {
		return copyOf(this).mul(vec);
	}

	/**
	 * Negates the vector (reverse coordinates) (the vector this is called on is
	 * affected)
	 * 
	 * @return The negated vector
	 */
	public Vector2f negate() {
		return mul(-1);
	}

	/**
	 * Negates the vector (reverse coordinates) (the vector this is called on is not
	 * affected)
	 * 
	 * @return The negated vector
	 */
	public Vector2f negateCopy() {
		return mulCopy(-1);
	}

	/**
	 * Normalizes the vector. The result will a vector with the ratio between
	 * components the same but a length of 1 (the vector this is called on is
	 * affected)
	 * 
	 * @return The normalized vector
	 */
	public Vector2f normalize() {
		float length = length();
		if (length == 0) {
			return this;
		}
		x /= length;
		y /= length;
		return this;
	}

	/**
	 * Normalizes a vector. The result will a vector with the ratio between
	 * components the same but a length of 1 (the vector this is called on is not
	 * affected)
	 * 
	 * @return The normalized vector
	 */
	public Vector2f normalizeCopy() {
		return copyOf(this).normalize();
	}

	/**
	 * Individually subtracts the x and y components of each vector (the vector this
	 * is called on is affected)
	 * 
	 * @param vec The vector to subtract
	 * @return The result
	 */
	public Vector2f sub(Vector2f vec) {
		x -= vec.x;
		y -= vec.y;
		return this;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	/**
	 * Individually subtracts the x and y components of each vector (the vector this
	 * is called on is not affected)
	 * 
	 * @param vec The vector to subtract
	 * @return The result
	 */
	public Vector2f subCopy(Vector2f vec) {
		return copyOf(this).sub(vec);
	}

	/**
	 * Turns the vector into a float array
	 * 
	 * @return The transformed vector
	 */
	public float[] toFloatArray() {
		return new float[] { x, y };
	}

	/**
	 *
	 * "Maps" the vector. Given an old upper and lower bound and a new upper and
	 * lower bound, transforms. For example -1 to 1 space to 0 to 1 space.(the
	 * vector this is called on is affected)
	 *
	 * @param olbx Old lower bound x
	 * @param oubx Old upper bound x
	 * @param olby Old lower bound y
	 * @param ouby Old upper bound y
	 * @param nlbx New lower bound x
	 * @param nubx New upper bound x
	 * @param nlby New lower bound y
	 * @param nuby New upper bound y
	 * @return This vector, after mapping
	 */
	public Vector2f map(float olbx, float oubx, float olby, float ouby, float nlbx, float nubx, float nlby,
			float nuby) {
		x -= olbx;
		x /= Math.abs(olbx - oubx);
		x *= Math.abs(nlbx - nubx);
		x += nlbx;
		y -= olby;
		y /= Math.abs(olby - ouby);
		y *= Math.abs(nlby - nuby);
		y += nlby;
		return this;
	}

	/**
	 *
	 * "Maps" the vector. Given an old upper and lower bound and a new upper and
	 * lower bound, transforms. For example -1 to 1 space to 0 to 1 space.(the
	 * vector this is called on is not affected)
	 *
	 * @param olbx Old lower bound x
	 * @param oubx Old upper bound x
	 * @param olby Old lower bound y
	 * @param ouby Old upper bound y
	 * @param nlbx New lower bound x
	 * @param nubx New upper bound x
	 * @param nlby New lower bound y
	 * @param nuby New upper bound y
	 * @return This vector, after mapping
	 */
	public Vector2f mapCopy(float olbx, float oubx, float olby, float ouby, float nlbx, float nubx, float nlby,
			float nuby) {
		return copyOf(this).map(olbx, oubx, olby, ouby, nlbx, nubx, nlby, nuby);
	}

	/**
	 * Creates and returns a copy of this vector
	 * 
	 * @return The copy
	 */
	public Vector2f copy() {
		return copyOf(this);
	}

	/*
	 * Swizzle
	 */
	public float x() {
		return x;
	}

	public float u() {
		return x;
	}

	public float y() {
		return y;
	}

	public float v() {
		return y;
	}

	public Vector2f xx() {
		return new Vector2f(x, x);
	}

	public Vector2f uu() {
		return new Vector2f(x, x);
	}

	public Vector2f xy() {
		return new Vector2f(x, y);
	}

	public Vector2f uv() {
		return new Vector2f(x, y);
	}

	public Vector2f yx() {
		return new Vector2f(y, x);
	}

	public Vector2f vu() {
		return new Vector2f(y, x);
	}

	public Vector2f yy() {
		return new Vector2f(y, y);
	}

	public Vector2f vv() {
		return new Vector2f(y, y);
	}

	public Vector3f xxx() {
		return new Vector3f(x, x, x);
	}

	public Vector3f uuu() {
		return new Vector3f(x, x, x);
	}

	public Vector3f xxy() {
		return new Vector3f(x, x, y);
	}

	public Vector3f uuv() {
		return new Vector3f(x, x, y);
	}

	public Vector3f xyx() {
		return new Vector3f(x, y, x);
	}

	public Vector3f uvu() {
		return new Vector3f(x, y, x);
	}

	public Vector3f xyy() {
		return new Vector3f(x, y, y);
	}

	public Vector3f uvv() {
		return new Vector3f(x, y, y);
	}

	public Vector3f yxx() {
		return new Vector3f(y, x, x);
	}

	public Vector3f vuu() {
		return new Vector3f(y, x, x);
	}

	public Vector3f yxy() {
		return new Vector3f(y, x, y);
	}

	public Vector3f vuv() {
		return new Vector3f(y, x, y);
	}

	public Vector3f yyx() {
		return new Vector3f(y, y, x);
	}

	public Vector3f vvu() {
		return new Vector3f(y, y, x);
	}

	public Vector3f yyy() {
		return new Vector3f(y, y, y);
	}

	public Vector3f vvv() {
		return new Vector3f(y, y, y);
	}

	public Vector4f xxxx() {
		return new Vector4f(x, x, x, x);
	}

	public Vector4f uuuu() {
		return new Vector4f(x, x, x, x);
	}

	public Vector4f xxxy() {
		return new Vector4f(x, x, x, y);
	}

	public Vector4f uuuv() {
		return new Vector4f(x, x, x, y);
	}

	public Vector4f xxyx() {
		return new Vector4f(x, x, y, x);
	}

	public Vector4f uuvu() {
		return new Vector4f(x, x, y, x);
	}

	public Vector4f xxyy() {
		return new Vector4f(x, x, y, y);
	}

	public Vector4f uuvv() {
		return new Vector4f(x, x, y, y);
	}

	public Vector4f xyxx() {
		return new Vector4f(x, y, x, x);
	}

	public Vector4f uvuu() {
		return new Vector4f(x, y, x, x);
	}

	public Vector4f xyxy() {
		return new Vector4f(x, y, x, y);
	}

	public Vector4f uvuv() {
		return new Vector4f(x, y, x, y);
	}

	public Vector4f xyyx() {
		return new Vector4f(x, y, y, x);
	}

	public Vector4f uvvu() {
		return new Vector4f(x, y, y, x);
	}

	public Vector4f xyyy() {
		return new Vector4f(x, y, y, y);
	}

	public Vector4f uvvv() {
		return new Vector4f(x, y, y, y);
	}

	public Vector4f yxxx() {
		return new Vector4f(y, x, x, x);
	}

	public Vector4f vuuu() {
		return new Vector4f(y, x, x, x);
	}

	public Vector4f yxxy() {
		return new Vector4f(y, x, x, y);
	}

	public Vector4f vuuv() {
		return new Vector4f(y, x, x, y);
	}

	public Vector4f yxyx() {
		return new Vector4f(y, x, y, x);
	}

	public Vector4f vuvu() {
		return new Vector4f(y, x, y, x);
	}

	public Vector4f yxyy() {
		return new Vector4f(y, x, y, y);
	}

	public Vector4f vuvv() {
		return new Vector4f(y, x, y, y);
	}

	public Vector4f yyxx() {
		return new Vector4f(y, y, x, x);
	}

	public Vector4f vvuu() {
		return new Vector4f(y, y, x, x);
	}

	public Vector4f yyxy() {
		return new Vector4f(y, y, x, y);
	}

	public Vector4f vvuv() {
		return new Vector4f(y, y, x, y);
	}

	public Vector4f yyyx() {
		return new Vector4f(y, y, y, x);
	}

	public Vector4f vvvu() {
		return new Vector4f(y, y, y, x);
	}

	public Vector4f yyyy() {
		return new Vector4f(y, y, y, y);
	}

	public Vector4f vvvv() {
		return new Vector4f(y, y, y, y);
	}

	public void xy(Vector2f vec) {
		x = vec.x;
		y = vec.y;
	}

	public void uv(Vector2f vec) {
		x = vec.x;
		y = vec.y;
	}

	public void yx(Vector2f vec) {
		y = vec.x;
		x = vec.y;
	}

	public void vu(Vector2f vec) {
		y = vec.x;
		x = vec.y;
	}

	public void y(float f) {
		y = f;
	}

	public void v(float f) {
		y = f;
	}

	public void x(float f) {
		x = f;
	}

	public void u(float f) {
		x = f;
	}

}
