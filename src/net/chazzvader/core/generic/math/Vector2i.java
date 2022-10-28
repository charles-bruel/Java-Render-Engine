package net.chazzvader.core.generic.math;

/**
 * Two component int vector. Can represent anything.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
@SuppressWarnings("javadoc")
public class Vector2i {

	@Override
	public String toString() {
		return "Vector2i [x=" + x + ", y=" + y + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		Vector2i other = (Vector2i) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	/**
	 * The 2 values of the vector
	 */
	public int x = 0, y = 0;
	
	/**
	 * Creates a vector with the specified x and y values
	 * 
	 * @param x The x value
	 * @param y The y value
	 */
	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Creates a new blank vector.
	 */
	public Vector2i() {

	}

	/**
	 * Converts this to a Vector2f
	 * @return The converted vector.
	 */
	public Vector2f toVector2f() {
		return new Vector2f(x, y);
	}

}
