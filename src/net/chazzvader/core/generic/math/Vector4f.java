package net.chazzvader.core.generic.math;

/**
 * Four component float vector. Can represent anything. Many of the functions
 * modify the objects they were called on, use the copy version of the method to
 * not modify the original. Mostly used for homogeneous coordinates for certain
 * transformations.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
@SuppressWarnings("javadoc")
public class Vector4f {

	/**
	 * The four values of the vector. W defaults to 1 because that makes sense for
	 * RGBA color and homogeneous coordinates.
	 */
	public float x = 0, y = 0, z = 0, w = 1;

	/**
	 * Creates a new, blank vector
	 */
	public Vector4f() {
	}

	/**
	 * Creates a new vector with the specified x, y, z, and w values.
	 * 
	 * @param x The x value
	 * @param y The y value
	 * @param z The z value
	 * @param w The w value
	 */
	public Vector4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 * Creates a new vector with the specified x, y, z, and w values.
	 * 
	 * @param xyz The x, y, and z value
	 * @param w   The w value
	 */
	public Vector4f(Vector3f xyz, float w) {
		this.x = xyz.x;
		this.y = xyz.y;
		this.z = xyz.z;
		this.w = w;
	}

	/**
	 * Creates a new vector with the specified x, y, and z values. W defaults to 1.
	 * 
	 * @param xyz The x, y, and z values
	 */
	public Vector4f(Vector3f xyz) {
		this.x = xyz.x;
		this.y = xyz.y;
		this.z = xyz.z;
	}

	/**
	 * Creates a new vector with the specified x, y, z, and w values.
	 * 
	 * @param x   The x value
	 * @param yzw The y, z, and w values
	 */
	public Vector4f(float x, Vector3f yzw) {
		this.x = x;
		this.y = yzw.x;
		this.z = yzw.y;
		this.w = yzw.z;
	}

	/**
	 * Creates a new vector with the specified x, y, z, and w values.
	 * 
	 * @param xy The x and y values
	 * @param z  The z value
	 * @param w  The w value
	 */
	public Vector4f(Vector2f xy, float z, float w) {
		this.x = xy.x;
		this.y = xy.y;
		this.z = z;
		this.w = w;
	}

	/**
	 * Creates a new vector with the specified x, y, and z values. W defaults to 1.
	 * 
	 * @param xy The x and y values
	 * @param z  The z value
	 */
	public Vector4f(Vector2f xy, float z) {
		this.x = xy.x;
		this.y = xy.y;
		this.z = z;
	}

	/**
	 * Creates a new vector with the specified x, y, z, and w values.
	 * 
	 * @param x  The x value
	 * @param yz The y and z values
	 * @param w  The w value
	 */
	public Vector4f(float x, Vector2f yz, float w) {
		this.x = x;
		this.y = yz.x;
		this.z = yz.y;
		this.w = w;
	}

	/**
	 * Creates a new vector with the specified x, y, and z values. W defaults to 1
	 * 
	 * @param x  The x value
	 * @param yz The y and z values
	 */
	public Vector4f(float x, Vector2f yz) {
		this.x = x;
		this.y = yz.x;
		this.z = yz.y;
	}

	/**
	 * Creates a new vector with the specified x, y, z, and w values.
	 * 
	 * @param xy The x and y values
	 * @param zw The z and w values
	 */
	public Vector4f(Vector2f xy, Vector2f zw) {
		this.x = xy.x;
		this.y = xy.y;
		this.z = zw.x;
		this.z = zw.y;
	}

	/**
	 * Creates a new vector with the specified x, y, z, and w values.
	 * 
	 * @param x  The x value
	 * @param y  The y value
	 * @param zw The z and w values
	 */
	public Vector4f(float x, float y, Vector2f zw) {
		this.x = x;
		this.y = y;
		this.z = zw.x;
		this.z = zw.y;
	}

	/**
	 * Creates and returns a copy of the passed vector
	 * 
	 * @param vec The vector to copy
	 * @return The copy
	 */
	public static Vector4f copyOf(Vector4f vec) {
		return new Vector4f(vec.x, vec.y, vec.z, vec.w);
	}

	/**
	 * Individually adds the x, y, z components of each vector together (the vector
	 * this is called on is affected)
	 * 
	 * @param vec The vector to add
	 * @return The result
	 */
	public Vector4f add(Vector4f vec) {
		x += vec.x;
		y += vec.y;
		z += vec.z;
		w += vec.w;
		return this;
	}

	/**
	 * Individually adds the x, y, z components of each vector together (the vector
	 * this is called on is not affected)
	 * 
	 * @param vec The vector to add
	 * @return The result
	 */
	public Vector4f addCopy(Vector4f vec) {
		return copyOf(this).add(vec);
	}

	/**
	 * Performs dot product on this vector and the passed vector (the vector this is
	 * called on is not affected)
	 * 
	 * @param vec The vector to dot product with
	 * @return The dot product
	 */
	public float dotProduct(Vector4f vec) {
		float result = x * vec.x;
		result += y * vec.y;
		result += z * vec.z;
		result += w * vec.w;
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(w);
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
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
		Vector4f other = (Vector4f) obj;
		if (Float.floatToIntBits(w) != Float.floatToIntBits(other.w))
			return false;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ", " + w + ")";
	}

	/**
	 * Returns the length of the vector. The length of the vector can also be
	 * thought of as the distance between the vector position and 0, 0, 0, 0. Of
	 * course anything in 4d is hard to visualize.
	 * 
	 * @return The length
	 */
	public float length() {
		return (float) Math.sqrt((x * x) + (y * y) + (z * z) + (w * w));
	}
	
	/**
	 * Performs scalar multiplication on the vector (the vector this is called on is
	 * affected)
	 * 
	 * @param scalar The scalar to multiply by
	 * @return The result
	 */
	public Vector4f mul(float scalar) {
		x *= scalar;
		y *= scalar;
		z *= scalar;
		w *= scalar;
		return this;
	}

	/**
	 * Individually multiplies the x, y, z, and w components of each vector (the vector
	 * this is called on is affected)
	 * 
	 * @param vec The vector to multiply
	 * @return The result
	 */
	public Vector4f mul(Vector4f vec) {
		x *= vec.x;
		y *= vec.y;
		z *= vec.z;
		w *= vec.w;
		return this;
	}

	/**
	 * Performs scalar multiplication on the vector (the vector this is called on is
	 * not affected)
	 * 
	 * @param scalar The scalar to multiply by
	 * @return The result
	 */
	public Vector4f mulCopy(float scalar) {
		return copyOf(this).mul(scalar);
	}

	/**
	 * Individually multiplies the x, y, z, and w components of each vector (the vector
	 * this is called on is not affected)
	 * 
	 * @param vec The vector to multiply
	 * @return The result
	 */
	public Vector4f mulCopy(Vector4f vec) {
		return copyOf(this).mul(vec);
	}
	
	/**
	 * Negates the vector (reverse coordinates) (the vector this is called on is
	 * affected)
	 * 
	 * @return The negated vector
	 */
	public Vector4f negate() {
		return mul(-1);
	}

	/**
	 * Negates the vector (reverse coordinates) (the vector this is called on is not
	 * affected)
	 * 
	 * @return The negated vector
	 */
	public Vector4f negateCopy() {
		return mulCopy(-1);
	}

	/**
	 * Normalizes the vector. The result will a vector with the ratio between
	 * components the same but a length of 1 (the vector this is called on is
	 * affected)
	 * 
	 * @return The normalized vector
	 */
	public Vector4f normalize() {
		float length = length();
		if (length == 0) {
			return this;
		}
		x /= length;
		y /= length;
		z /= length;
		w /= length;
		return this;
	}
	
	/**
	 * Normalizes a vector. The result will a vector with the ratio between
	 * components the same but a length of 1 (the vector this is called on is not
	 * affected)
	 * 
	 * @return The normalized vector
	 */
	public Vector4f normalizeCopy() {
		return copyOf(this).normalize();
	}

	/**
	 * Individually subtracts the x, y, z components of each vector (the vector this
	 * is called on is affected)
	 * 
	 * @param vec The vector to subtract
	 * @return The result
	 */
	public Vector4f sub(Vector4f vec) {
		x -= vec.x;
		y -= vec.y;
		z -= vec.z;
		w -= vec.w;
		return this;
	}

	/**
	 * Individually subtracts the x, y, z components of each vector (the vector this
	 * is called on is not affected)
	 * 
	 * @param vec The vector to subtract
	 * @return The result
	 */
	public Vector4f subCopy(Vector4f vec) {
		return copyOf(this).sub(vec);
	}

	/*
	 * Swizzle
	 */
	public float x(){return x;}
	public float r(){return x;}
	public float y(){return y;}
	public float g(){return y;}
	public float z(){return z;}
	public float b(){return z;}
	public float w(){return w;}
	public float a(){return w;}
	public Vector2f xx(){return new Vector2f(x,x);}
	public Vector2f rr(){return new Vector2f(x,x);}
	public Vector2f xy(){return new Vector2f(x,y);}
	public Vector2f rg(){return new Vector2f(x,y);}
	public Vector2f xz(){return new Vector2f(x,z);}
	public Vector2f rb(){return new Vector2f(x,z);}
	public Vector2f xw(){return new Vector2f(x,w);}
	public Vector2f ra(){return new Vector2f(x,w);}
	public Vector2f yx(){return new Vector2f(y,x);}
	public Vector2f gr(){return new Vector2f(y,x);}
	public Vector2f yy(){return new Vector2f(y,y);}
	public Vector2f gg(){return new Vector2f(y,y);}
	public Vector2f yz(){return new Vector2f(y,z);}
	public Vector2f gb(){return new Vector2f(y,z);}
	public Vector2f yw(){return new Vector2f(y,w);}
	public Vector2f ga(){return new Vector2f(y,w);}
	public Vector2f zx(){return new Vector2f(z,x);}
	public Vector2f br(){return new Vector2f(z,x);}
	public Vector2f zy(){return new Vector2f(z,y);}
	public Vector2f bg(){return new Vector2f(z,y);}
	public Vector2f zz(){return new Vector2f(z,z);}
	public Vector2f bb(){return new Vector2f(z,z);}
	public Vector2f zw(){return new Vector2f(z,w);}
	public Vector2f ba(){return new Vector2f(z,w);}
	public Vector2f wx(){return new Vector2f(w,x);}
	public Vector2f ar(){return new Vector2f(w,x);}
	public Vector2f wy(){return new Vector2f(w,y);}
	public Vector2f ag(){return new Vector2f(w,y);}
	public Vector2f wz(){return new Vector2f(w,z);}
	public Vector2f ab(){return new Vector2f(w,z);}
	public Vector2f ww(){return new Vector2f(w,w);}
	public Vector2f aa(){return new Vector2f(w,w);}
	public Vector3f xxx(){return new Vector3f(x,x,x);}
	public Vector3f rrr(){return new Vector3f(x,x,x);}
	public Vector3f xxy(){return new Vector3f(x,x,y);}
	public Vector3f rrg(){return new Vector3f(x,x,y);}
	public Vector3f xxz(){return new Vector3f(x,x,z);}
	public Vector3f rrb(){return new Vector3f(x,x,z);}
	public Vector3f xxw(){return new Vector3f(x,x,w);}
	public Vector3f rra(){return new Vector3f(x,x,w);}
	public Vector3f xyx(){return new Vector3f(x,y,x);}
	public Vector3f rgr(){return new Vector3f(x,y,x);}
	public Vector3f xyy(){return new Vector3f(x,y,y);}
	public Vector3f rgg(){return new Vector3f(x,y,y);}
	public Vector3f xyz(){return new Vector3f(x,y,z);}
	public Vector3f rgb(){return new Vector3f(x,y,z);}
	public Vector3f xyw(){return new Vector3f(x,y,w);}
	public Vector3f rga(){return new Vector3f(x,y,w);}
	public Vector3f xzx(){return new Vector3f(x,z,x);}
	public Vector3f rbr(){return new Vector3f(x,z,x);}
	public Vector3f xzy(){return new Vector3f(x,z,y);}
	public Vector3f rbg(){return new Vector3f(x,z,y);}
	public Vector3f xzz(){return new Vector3f(x,z,z);}
	public Vector3f rbb(){return new Vector3f(x,z,z);}
	public Vector3f xzw(){return new Vector3f(x,z,w);}
	public Vector3f rba(){return new Vector3f(x,z,w);}
	public Vector3f xwx(){return new Vector3f(x,w,x);}
	public Vector3f rar(){return new Vector3f(x,w,x);}
	public Vector3f xwy(){return new Vector3f(x,w,y);}
	public Vector3f rag(){return new Vector3f(x,w,y);}
	public Vector3f xwz(){return new Vector3f(x,w,z);}
	public Vector3f rab(){return new Vector3f(x,w,z);}
	public Vector3f xww(){return new Vector3f(x,w,w);}
	public Vector3f raa(){return new Vector3f(x,w,w);}
	public Vector3f yxx(){return new Vector3f(y,x,x);}
	public Vector3f grr(){return new Vector3f(y,x,x);}
	public Vector3f yxy(){return new Vector3f(y,x,y);}
	public Vector3f grg(){return new Vector3f(y,x,y);}
	public Vector3f yxz(){return new Vector3f(y,x,z);}
	public Vector3f grb(){return new Vector3f(y,x,z);}
	public Vector3f yxw(){return new Vector3f(y,x,w);}
	public Vector3f gra(){return new Vector3f(y,x,w);}
	public Vector3f yyx(){return new Vector3f(y,y,x);}
	public Vector3f ggr(){return new Vector3f(y,y,x);}
	public Vector3f yyy(){return new Vector3f(y,y,y);}
	public Vector3f ggg(){return new Vector3f(y,y,y);}
	public Vector3f yyz(){return new Vector3f(y,y,z);}
	public Vector3f ggb(){return new Vector3f(y,y,z);}
	public Vector3f yyw(){return new Vector3f(y,y,w);}
	public Vector3f gga(){return new Vector3f(y,y,w);}
	public Vector3f yzx(){return new Vector3f(y,z,x);}
	public Vector3f gbr(){return new Vector3f(y,z,x);}
	public Vector3f yzy(){return new Vector3f(y,z,y);}
	public Vector3f gbg(){return new Vector3f(y,z,y);}
	public Vector3f yzz(){return new Vector3f(y,z,z);}
	public Vector3f gbb(){return new Vector3f(y,z,z);}
	public Vector3f yzw(){return new Vector3f(y,z,w);}
	public Vector3f gba(){return new Vector3f(y,z,w);}
	public Vector3f ywx(){return new Vector3f(y,w,x);}
	public Vector3f gar(){return new Vector3f(y,w,x);}
	public Vector3f ywy(){return new Vector3f(y,w,y);}
	public Vector3f gag(){return new Vector3f(y,w,y);}
	public Vector3f ywz(){return new Vector3f(y,w,z);}
	public Vector3f gab(){return new Vector3f(y,w,z);}
	public Vector3f yww(){return new Vector3f(y,w,w);}
	public Vector3f gaa(){return new Vector3f(y,w,w);}
	public Vector3f zxx(){return new Vector3f(z,x,x);}
	public Vector3f brr(){return new Vector3f(z,x,x);}
	public Vector3f zxy(){return new Vector3f(z,x,y);}
	public Vector3f brg(){return new Vector3f(z,x,y);}
	public Vector3f zxz(){return new Vector3f(z,x,z);}
	public Vector3f brb(){return new Vector3f(z,x,z);}
	public Vector3f zxw(){return new Vector3f(z,x,w);}
	public Vector3f bra(){return new Vector3f(z,x,w);}
	public Vector3f zyx(){return new Vector3f(z,y,x);}
	public Vector3f bgr(){return new Vector3f(z,y,x);}
	public Vector3f zyy(){return new Vector3f(z,y,y);}
	public Vector3f bgg(){return new Vector3f(z,y,y);}
	public Vector3f zyz(){return new Vector3f(z,y,z);}
	public Vector3f bgb(){return new Vector3f(z,y,z);}
	public Vector3f zyw(){return new Vector3f(z,y,w);}
	public Vector3f bga(){return new Vector3f(z,y,w);}
	public Vector3f zzx(){return new Vector3f(z,z,x);}
	public Vector3f bbr(){return new Vector3f(z,z,x);}
	public Vector3f zzy(){return new Vector3f(z,z,y);}
	public Vector3f bbg(){return new Vector3f(z,z,y);}
	public Vector3f zzz(){return new Vector3f(z,z,z);}
	public Vector3f bbb(){return new Vector3f(z,z,z);}
	public Vector3f zzw(){return new Vector3f(z,z,w);}
	public Vector3f bba(){return new Vector3f(z,z,w);}
	public Vector3f zwx(){return new Vector3f(z,w,x);}
	public Vector3f bar(){return new Vector3f(z,w,x);}
	public Vector3f zwy(){return new Vector3f(z,w,y);}
	public Vector3f bag(){return new Vector3f(z,w,y);}
	public Vector3f zwz(){return new Vector3f(z,w,z);}
	public Vector3f bab(){return new Vector3f(z,w,z);}
	public Vector3f zww(){return new Vector3f(z,w,w);}
	public Vector3f baa(){return new Vector3f(z,w,w);}
	public Vector3f wxx(){return new Vector3f(w,x,x);}
	public Vector3f arr(){return new Vector3f(w,x,x);}
	public Vector3f wxy(){return new Vector3f(w,x,y);}
	public Vector3f arg(){return new Vector3f(w,x,y);}
	public Vector3f wxz(){return new Vector3f(w,x,z);}
	public Vector3f arb(){return new Vector3f(w,x,z);}
	public Vector3f wxw(){return new Vector3f(w,x,w);}
	public Vector3f ara(){return new Vector3f(w,x,w);}
	public Vector3f wyx(){return new Vector3f(w,y,x);}
	public Vector3f agr(){return new Vector3f(w,y,x);}
	public Vector3f wyy(){return new Vector3f(w,y,y);}
	public Vector3f agg(){return new Vector3f(w,y,y);}
	public Vector3f wyz(){return new Vector3f(w,y,z);}
	public Vector3f agb(){return new Vector3f(w,y,z);}
	public Vector3f wyw(){return new Vector3f(w,y,w);}
	public Vector3f aga(){return new Vector3f(w,y,w);}
	public Vector3f wzx(){return new Vector3f(w,z,x);}
	public Vector3f abr(){return new Vector3f(w,z,x);}
	public Vector3f wzy(){return new Vector3f(w,z,y);}
	public Vector3f abg(){return new Vector3f(w,z,y);}
	public Vector3f wzz(){return new Vector3f(w,z,z);}
	public Vector3f abb(){return new Vector3f(w,z,z);}
	public Vector3f wzw(){return new Vector3f(w,z,w);}
	public Vector3f aba(){return new Vector3f(w,z,w);}
	public Vector3f wwx(){return new Vector3f(w,w,x);}
	public Vector3f aar(){return new Vector3f(w,w,x);}
	public Vector3f wwy(){return new Vector3f(w,w,y);}
	public Vector3f aag(){return new Vector3f(w,w,y);}
	public Vector3f wwz(){return new Vector3f(w,w,z);}
	public Vector3f aab(){return new Vector3f(w,w,z);}
	public Vector3f www(){return new Vector3f(w,w,w);}
	public Vector3f aaa(){return new Vector3f(w,w,w);}
	public Vector4f xxxx(){return new Vector4f(x,x,x,x);}
	public Vector4f rrrr(){return new Vector4f(x,x,x,x);}
	public Vector4f xxxy(){return new Vector4f(x,x,x,y);}
	public Vector4f rrrg(){return new Vector4f(x,x,x,y);}
	public Vector4f xxxz(){return new Vector4f(x,x,x,z);}
	public Vector4f rrrb(){return new Vector4f(x,x,x,z);}
	public Vector4f xxxw(){return new Vector4f(x,x,x,w);}
	public Vector4f rrra(){return new Vector4f(x,x,x,w);}
	public Vector4f xxyx(){return new Vector4f(x,x,y,x);}
	public Vector4f rrgr(){return new Vector4f(x,x,y,x);}
	public Vector4f xxyy(){return new Vector4f(x,x,y,y);}
	public Vector4f rrgg(){return new Vector4f(x,x,y,y);}
	public Vector4f xxyz(){return new Vector4f(x,x,y,z);}
	public Vector4f rrgb(){return new Vector4f(x,x,y,z);}
	public Vector4f xxyw(){return new Vector4f(x,x,y,w);}
	public Vector4f rrga(){return new Vector4f(x,x,y,w);}
	public Vector4f xxzx(){return new Vector4f(x,x,z,x);}
	public Vector4f rrbr(){return new Vector4f(x,x,z,x);}
	public Vector4f xxzy(){return new Vector4f(x,x,z,y);}
	public Vector4f rrbg(){return new Vector4f(x,x,z,y);}
	public Vector4f xxzz(){return new Vector4f(x,x,z,z);}
	public Vector4f rrbb(){return new Vector4f(x,x,z,z);}
	public Vector4f xxzw(){return new Vector4f(x,x,z,w);}
	public Vector4f rrba(){return new Vector4f(x,x,z,w);}
	public Vector4f xxwx(){return new Vector4f(x,x,w,x);}
	public Vector4f rrar(){return new Vector4f(x,x,w,x);}
	public Vector4f xxwy(){return new Vector4f(x,x,w,y);}
	public Vector4f rrag(){return new Vector4f(x,x,w,y);}
	public Vector4f xxwz(){return new Vector4f(x,x,w,z);}
	public Vector4f rrab(){return new Vector4f(x,x,w,z);}
	public Vector4f xxww(){return new Vector4f(x,x,w,w);}
	public Vector4f rraa(){return new Vector4f(x,x,w,w);}
	public Vector4f xyxx(){return new Vector4f(x,y,x,x);}
	public Vector4f rgrr(){return new Vector4f(x,y,x,x);}
	public Vector4f xyxy(){return new Vector4f(x,y,x,y);}
	public Vector4f rgrg(){return new Vector4f(x,y,x,y);}
	public Vector4f xyxz(){return new Vector4f(x,y,x,z);}
	public Vector4f rgrb(){return new Vector4f(x,y,x,z);}
	public Vector4f xyxw(){return new Vector4f(x,y,x,w);}
	public Vector4f rgra(){return new Vector4f(x,y,x,w);}
	public Vector4f xyyx(){return new Vector4f(x,y,y,x);}
	public Vector4f rggr(){return new Vector4f(x,y,y,x);}
	public Vector4f xyyy(){return new Vector4f(x,y,y,y);}
	public Vector4f rggg(){return new Vector4f(x,y,y,y);}
	public Vector4f xyyz(){return new Vector4f(x,y,y,z);}
	public Vector4f rggb(){return new Vector4f(x,y,y,z);}
	public Vector4f xyyw(){return new Vector4f(x,y,y,w);}
	public Vector4f rgga(){return new Vector4f(x,y,y,w);}
	public Vector4f xyzx(){return new Vector4f(x,y,z,x);}
	public Vector4f rgbr(){return new Vector4f(x,y,z,x);}
	public Vector4f xyzy(){return new Vector4f(x,y,z,y);}
	public Vector4f rgbg(){return new Vector4f(x,y,z,y);}
	public Vector4f xyzz(){return new Vector4f(x,y,z,z);}
	public Vector4f rgbb(){return new Vector4f(x,y,z,z);}
	public Vector4f xyzw(){return new Vector4f(x,y,z,w);}
	public Vector4f rgba(){return new Vector4f(x,y,z,w);}
	public Vector4f xywx(){return new Vector4f(x,y,w,x);}
	public Vector4f rgar(){return new Vector4f(x,y,w,x);}
	public Vector4f xywy(){return new Vector4f(x,y,w,y);}
	public Vector4f rgag(){return new Vector4f(x,y,w,y);}
	public Vector4f xywz(){return new Vector4f(x,y,w,z);}
	public Vector4f rgab(){return new Vector4f(x,y,w,z);}
	public Vector4f xyww(){return new Vector4f(x,y,w,w);}
	public Vector4f rgaa(){return new Vector4f(x,y,w,w);}
	public Vector4f xzxx(){return new Vector4f(x,z,x,x);}
	public Vector4f rbrr(){return new Vector4f(x,z,x,x);}
	public Vector4f xzxy(){return new Vector4f(x,z,x,y);}
	public Vector4f rbrg(){return new Vector4f(x,z,x,y);}
	public Vector4f xzxz(){return new Vector4f(x,z,x,z);}
	public Vector4f rbrb(){return new Vector4f(x,z,x,z);}
	public Vector4f xzxw(){return new Vector4f(x,z,x,w);}
	public Vector4f rbra(){return new Vector4f(x,z,x,w);}
	public Vector4f xzyx(){return new Vector4f(x,z,y,x);}
	public Vector4f rbgr(){return new Vector4f(x,z,y,x);}
	public Vector4f xzyy(){return new Vector4f(x,z,y,y);}
	public Vector4f rbgg(){return new Vector4f(x,z,y,y);}
	public Vector4f xzyz(){return new Vector4f(x,z,y,z);}
	public Vector4f rbgb(){return new Vector4f(x,z,y,z);}
	public Vector4f xzyw(){return new Vector4f(x,z,y,w);}
	public Vector4f rbga(){return new Vector4f(x,z,y,w);}
	public Vector4f xzzx(){return new Vector4f(x,z,z,x);}
	public Vector4f rbbr(){return new Vector4f(x,z,z,x);}
	public Vector4f xzzy(){return new Vector4f(x,z,z,y);}
	public Vector4f rbbg(){return new Vector4f(x,z,z,y);}
	public Vector4f xzzz(){return new Vector4f(x,z,z,z);}
	public Vector4f rbbb(){return new Vector4f(x,z,z,z);}
	public Vector4f xzzw(){return new Vector4f(x,z,z,w);}
	public Vector4f rbba(){return new Vector4f(x,z,z,w);}
	public Vector4f xzwx(){return new Vector4f(x,z,w,x);}
	public Vector4f rbar(){return new Vector4f(x,z,w,x);}
	public Vector4f xzwy(){return new Vector4f(x,z,w,y);}
	public Vector4f rbag(){return new Vector4f(x,z,w,y);}
	public Vector4f xzwz(){return new Vector4f(x,z,w,z);}
	public Vector4f rbab(){return new Vector4f(x,z,w,z);}
	public Vector4f xzww(){return new Vector4f(x,z,w,w);}
	public Vector4f rbaa(){return new Vector4f(x,z,w,w);}
	public Vector4f xwxx(){return new Vector4f(x,w,x,x);}
	public Vector4f rarr(){return new Vector4f(x,w,x,x);}
	public Vector4f xwxy(){return new Vector4f(x,w,x,y);}
	public Vector4f rarg(){return new Vector4f(x,w,x,y);}
	public Vector4f xwxz(){return new Vector4f(x,w,x,z);}
	public Vector4f rarb(){return new Vector4f(x,w,x,z);}
	public Vector4f xwxw(){return new Vector4f(x,w,x,w);}
	public Vector4f rara(){return new Vector4f(x,w,x,w);}
	public Vector4f xwyx(){return new Vector4f(x,w,y,x);}
	public Vector4f ragr(){return new Vector4f(x,w,y,x);}
	public Vector4f xwyy(){return new Vector4f(x,w,y,y);}
	public Vector4f ragg(){return new Vector4f(x,w,y,y);}
	public Vector4f xwyz(){return new Vector4f(x,w,y,z);}
	public Vector4f ragb(){return new Vector4f(x,w,y,z);}
	public Vector4f xwyw(){return new Vector4f(x,w,y,w);}
	public Vector4f raga(){return new Vector4f(x,w,y,w);}
	public Vector4f xwzx(){return new Vector4f(x,w,z,x);}
	public Vector4f rabr(){return new Vector4f(x,w,z,x);}
	public Vector4f xwzy(){return new Vector4f(x,w,z,y);}
	public Vector4f rabg(){return new Vector4f(x,w,z,y);}
	public Vector4f xwzz(){return new Vector4f(x,w,z,z);}
	public Vector4f rabb(){return new Vector4f(x,w,z,z);}
	public Vector4f xwzw(){return new Vector4f(x,w,z,w);}
	public Vector4f raba(){return new Vector4f(x,w,z,w);}
	public Vector4f xwwx(){return new Vector4f(x,w,w,x);}
	public Vector4f raar(){return new Vector4f(x,w,w,x);}
	public Vector4f xwwy(){return new Vector4f(x,w,w,y);}
	public Vector4f raag(){return new Vector4f(x,w,w,y);}
	public Vector4f xwwz(){return new Vector4f(x,w,w,z);}
	public Vector4f raab(){return new Vector4f(x,w,w,z);}
	public Vector4f xwww(){return new Vector4f(x,w,w,w);}
	public Vector4f raaa(){return new Vector4f(x,w,w,w);}
	public Vector4f yxxx(){return new Vector4f(y,x,x,x);}
	public Vector4f grrr(){return new Vector4f(y,x,x,x);}
	public Vector4f yxxy(){return new Vector4f(y,x,x,y);}
	public Vector4f grrg(){return new Vector4f(y,x,x,y);}
	public Vector4f yxxz(){return new Vector4f(y,x,x,z);}
	public Vector4f grrb(){return new Vector4f(y,x,x,z);}
	public Vector4f yxxw(){return new Vector4f(y,x,x,w);}
	public Vector4f grra(){return new Vector4f(y,x,x,w);}
	public Vector4f yxyx(){return new Vector4f(y,x,y,x);}
	public Vector4f grgr(){return new Vector4f(y,x,y,x);}
	public Vector4f yxyy(){return new Vector4f(y,x,y,y);}
	public Vector4f grgg(){return new Vector4f(y,x,y,y);}
	public Vector4f yxyz(){return new Vector4f(y,x,y,z);}
	public Vector4f grgb(){return new Vector4f(y,x,y,z);}
	public Vector4f yxyw(){return new Vector4f(y,x,y,w);}
	public Vector4f grga(){return new Vector4f(y,x,y,w);}
	public Vector4f yxzx(){return new Vector4f(y,x,z,x);}
	public Vector4f grbr(){return new Vector4f(y,x,z,x);}
	public Vector4f yxzy(){return new Vector4f(y,x,z,y);}
	public Vector4f grbg(){return new Vector4f(y,x,z,y);}
	public Vector4f yxzz(){return new Vector4f(y,x,z,z);}
	public Vector4f grbb(){return new Vector4f(y,x,z,z);}
	public Vector4f yxzw(){return new Vector4f(y,x,z,w);}
	public Vector4f grba(){return new Vector4f(y,x,z,w);}
	public Vector4f yxwx(){return new Vector4f(y,x,w,x);}
	public Vector4f grar(){return new Vector4f(y,x,w,x);}
	public Vector4f yxwy(){return new Vector4f(y,x,w,y);}
	public Vector4f grag(){return new Vector4f(y,x,w,y);}
	public Vector4f yxwz(){return new Vector4f(y,x,w,z);}
	public Vector4f grab(){return new Vector4f(y,x,w,z);}
	public Vector4f yxww(){return new Vector4f(y,x,w,w);}
	public Vector4f graa(){return new Vector4f(y,x,w,w);}
	public Vector4f yyxx(){return new Vector4f(y,y,x,x);}
	public Vector4f ggrr(){return new Vector4f(y,y,x,x);}
	public Vector4f yyxy(){return new Vector4f(y,y,x,y);}
	public Vector4f ggrg(){return new Vector4f(y,y,x,y);}
	public Vector4f yyxz(){return new Vector4f(y,y,x,z);}
	public Vector4f ggrb(){return new Vector4f(y,y,x,z);}
	public Vector4f yyxw(){return new Vector4f(y,y,x,w);}
	public Vector4f ggra(){return new Vector4f(y,y,x,w);}
	public Vector4f yyyx(){return new Vector4f(y,y,y,x);}
	public Vector4f gggr(){return new Vector4f(y,y,y,x);}
	public Vector4f yyyy(){return new Vector4f(y,y,y,y);}
	public Vector4f gggg(){return new Vector4f(y,y,y,y);}
	public Vector4f yyyz(){return new Vector4f(y,y,y,z);}
	public Vector4f gggb(){return new Vector4f(y,y,y,z);}
	public Vector4f yyyw(){return new Vector4f(y,y,y,w);}
	public Vector4f ggga(){return new Vector4f(y,y,y,w);}
	public Vector4f yyzx(){return new Vector4f(y,y,z,x);}
	public Vector4f ggbr(){return new Vector4f(y,y,z,x);}
	public Vector4f yyzy(){return new Vector4f(y,y,z,y);}
	public Vector4f ggbg(){return new Vector4f(y,y,z,y);}
	public Vector4f yyzz(){return new Vector4f(y,y,z,z);}
	public Vector4f ggbb(){return new Vector4f(y,y,z,z);}
	public Vector4f yyzw(){return new Vector4f(y,y,z,w);}
	public Vector4f ggba(){return new Vector4f(y,y,z,w);}
	public Vector4f yywx(){return new Vector4f(y,y,w,x);}
	public Vector4f ggar(){return new Vector4f(y,y,w,x);}
	public Vector4f yywy(){return new Vector4f(y,y,w,y);}
	public Vector4f ggag(){return new Vector4f(y,y,w,y);}
	public Vector4f yywz(){return new Vector4f(y,y,w,z);}
	public Vector4f ggab(){return new Vector4f(y,y,w,z);}
	public Vector4f yyww(){return new Vector4f(y,y,w,w);}
	public Vector4f ggaa(){return new Vector4f(y,y,w,w);}
	public Vector4f yzxx(){return new Vector4f(y,z,x,x);}
	public Vector4f gbrr(){return new Vector4f(y,z,x,x);}
	public Vector4f yzxy(){return new Vector4f(y,z,x,y);}
	public Vector4f gbrg(){return new Vector4f(y,z,x,y);}
	public Vector4f yzxz(){return new Vector4f(y,z,x,z);}
	public Vector4f gbrb(){return new Vector4f(y,z,x,z);}
	public Vector4f yzxw(){return new Vector4f(y,z,x,w);}
	public Vector4f gbra(){return new Vector4f(y,z,x,w);}
	public Vector4f yzyx(){return new Vector4f(y,z,y,x);}
	public Vector4f gbgr(){return new Vector4f(y,z,y,x);}
	public Vector4f yzyy(){return new Vector4f(y,z,y,y);}
	public Vector4f gbgg(){return new Vector4f(y,z,y,y);}
	public Vector4f yzyz(){return new Vector4f(y,z,y,z);}
	public Vector4f gbgb(){return new Vector4f(y,z,y,z);}
	public Vector4f yzyw(){return new Vector4f(y,z,y,w);}
	public Vector4f gbga(){return new Vector4f(y,z,y,w);}
	public Vector4f yzzx(){return new Vector4f(y,z,z,x);}
	public Vector4f gbbr(){return new Vector4f(y,z,z,x);}
	public Vector4f yzzy(){return new Vector4f(y,z,z,y);}
	public Vector4f gbbg(){return new Vector4f(y,z,z,y);}
	public Vector4f yzzz(){return new Vector4f(y,z,z,z);}
	public Vector4f gbbb(){return new Vector4f(y,z,z,z);}
	public Vector4f yzzw(){return new Vector4f(y,z,z,w);}
	public Vector4f gbba(){return new Vector4f(y,z,z,w);}
	public Vector4f yzwx(){return new Vector4f(y,z,w,x);}
	public Vector4f gbar(){return new Vector4f(y,z,w,x);}
	public Vector4f yzwy(){return new Vector4f(y,z,w,y);}
	public Vector4f gbag(){return new Vector4f(y,z,w,y);}
	public Vector4f yzwz(){return new Vector4f(y,z,w,z);}
	public Vector4f gbab(){return new Vector4f(y,z,w,z);}
	public Vector4f yzww(){return new Vector4f(y,z,w,w);}
	public Vector4f gbaa(){return new Vector4f(y,z,w,w);}
	public Vector4f ywxx(){return new Vector4f(y,w,x,x);}
	public Vector4f garr(){return new Vector4f(y,w,x,x);}
	public Vector4f ywxy(){return new Vector4f(y,w,x,y);}
	public Vector4f garg(){return new Vector4f(y,w,x,y);}
	public Vector4f ywxz(){return new Vector4f(y,w,x,z);}
	public Vector4f garb(){return new Vector4f(y,w,x,z);}
	public Vector4f ywxw(){return new Vector4f(y,w,x,w);}
	public Vector4f gara(){return new Vector4f(y,w,x,w);}
	public Vector4f ywyx(){return new Vector4f(y,w,y,x);}
	public Vector4f gagr(){return new Vector4f(y,w,y,x);}
	public Vector4f ywyy(){return new Vector4f(y,w,y,y);}
	public Vector4f gagg(){return new Vector4f(y,w,y,y);}
	public Vector4f ywyz(){return new Vector4f(y,w,y,z);}
	public Vector4f gagb(){return new Vector4f(y,w,y,z);}
	public Vector4f ywyw(){return new Vector4f(y,w,y,w);}
	public Vector4f gaga(){return new Vector4f(y,w,y,w);}
	public Vector4f ywzx(){return new Vector4f(y,w,z,x);}
	public Vector4f gabr(){return new Vector4f(y,w,z,x);}
	public Vector4f ywzy(){return new Vector4f(y,w,z,y);}
	public Vector4f gabg(){return new Vector4f(y,w,z,y);}
	public Vector4f ywzz(){return new Vector4f(y,w,z,z);}
	public Vector4f gabb(){return new Vector4f(y,w,z,z);}
	public Vector4f ywzw(){return new Vector4f(y,w,z,w);}
	public Vector4f gaba(){return new Vector4f(y,w,z,w);}
	public Vector4f ywwx(){return new Vector4f(y,w,w,x);}
	public Vector4f gaar(){return new Vector4f(y,w,w,x);}
	public Vector4f ywwy(){return new Vector4f(y,w,w,y);}
	public Vector4f gaag(){return new Vector4f(y,w,w,y);}
	public Vector4f ywwz(){return new Vector4f(y,w,w,z);}
	public Vector4f gaab(){return new Vector4f(y,w,w,z);}
	public Vector4f ywww(){return new Vector4f(y,w,w,w);}
	public Vector4f gaaa(){return new Vector4f(y,w,w,w);}
	public Vector4f zxxx(){return new Vector4f(z,x,x,x);}
	public Vector4f brrr(){return new Vector4f(z,x,x,x);}
	public Vector4f zxxy(){return new Vector4f(z,x,x,y);}
	public Vector4f brrg(){return new Vector4f(z,x,x,y);}
	public Vector4f zxxz(){return new Vector4f(z,x,x,z);}
	public Vector4f brrb(){return new Vector4f(z,x,x,z);}
	public Vector4f zxxw(){return new Vector4f(z,x,x,w);}
	public Vector4f brra(){return new Vector4f(z,x,x,w);}
	public Vector4f zxyx(){return new Vector4f(z,x,y,x);}
	public Vector4f brgr(){return new Vector4f(z,x,y,x);}
	public Vector4f zxyy(){return new Vector4f(z,x,y,y);}
	public Vector4f brgg(){return new Vector4f(z,x,y,y);}
	public Vector4f zxyz(){return new Vector4f(z,x,y,z);}
	public Vector4f brgb(){return new Vector4f(z,x,y,z);}
	public Vector4f zxyw(){return new Vector4f(z,x,y,w);}
	public Vector4f brga(){return new Vector4f(z,x,y,w);}
	public Vector4f zxzx(){return new Vector4f(z,x,z,x);}
	public Vector4f brbr(){return new Vector4f(z,x,z,x);}
	public Vector4f zxzy(){return new Vector4f(z,x,z,y);}
	public Vector4f brbg(){return new Vector4f(z,x,z,y);}
	public Vector4f zxzz(){return new Vector4f(z,x,z,z);}
	public Vector4f brbb(){return new Vector4f(z,x,z,z);}
	public Vector4f zxzw(){return new Vector4f(z,x,z,w);}
	public Vector4f brba(){return new Vector4f(z,x,z,w);}
	public Vector4f zxwx(){return new Vector4f(z,x,w,x);}
	public Vector4f brar(){return new Vector4f(z,x,w,x);}
	public Vector4f zxwy(){return new Vector4f(z,x,w,y);}
	public Vector4f brag(){return new Vector4f(z,x,w,y);}
	public Vector4f zxwz(){return new Vector4f(z,x,w,z);}
	public Vector4f brab(){return new Vector4f(z,x,w,z);}
	public Vector4f zxww(){return new Vector4f(z,x,w,w);}
	public Vector4f braa(){return new Vector4f(z,x,w,w);}
	public Vector4f zyxx(){return new Vector4f(z,y,x,x);}
	public Vector4f bgrr(){return new Vector4f(z,y,x,x);}
	public Vector4f zyxy(){return new Vector4f(z,y,x,y);}
	public Vector4f bgrg(){return new Vector4f(z,y,x,y);}
	public Vector4f zyxz(){return new Vector4f(z,y,x,z);}
	public Vector4f bgrb(){return new Vector4f(z,y,x,z);}
	public Vector4f zyxw(){return new Vector4f(z,y,x,w);}
	public Vector4f bgra(){return new Vector4f(z,y,x,w);}
	public Vector4f zyyx(){return new Vector4f(z,y,y,x);}
	public Vector4f bggr(){return new Vector4f(z,y,y,x);}
	public Vector4f zyyy(){return new Vector4f(z,y,y,y);}
	public Vector4f bggg(){return new Vector4f(z,y,y,y);}
	public Vector4f zyyz(){return new Vector4f(z,y,y,z);}
	public Vector4f bggb(){return new Vector4f(z,y,y,z);}
	public Vector4f zyyw(){return new Vector4f(z,y,y,w);}
	public Vector4f bgga(){return new Vector4f(z,y,y,w);}
	public Vector4f zyzx(){return new Vector4f(z,y,z,x);}
	public Vector4f bgbr(){return new Vector4f(z,y,z,x);}
	public Vector4f zyzy(){return new Vector4f(z,y,z,y);}
	public Vector4f bgbg(){return new Vector4f(z,y,z,y);}
	public Vector4f zyzz(){return new Vector4f(z,y,z,z);}
	public Vector4f bgbb(){return new Vector4f(z,y,z,z);}
	public Vector4f zyzw(){return new Vector4f(z,y,z,w);}
	public Vector4f bgba(){return new Vector4f(z,y,z,w);}
	public Vector4f zywx(){return new Vector4f(z,y,w,x);}
	public Vector4f bgar(){return new Vector4f(z,y,w,x);}
	public Vector4f zywy(){return new Vector4f(z,y,w,y);}
	public Vector4f bgag(){return new Vector4f(z,y,w,y);}
	public Vector4f zywz(){return new Vector4f(z,y,w,z);}
	public Vector4f bgab(){return new Vector4f(z,y,w,z);}
	public Vector4f zyww(){return new Vector4f(z,y,w,w);}
	public Vector4f bgaa(){return new Vector4f(z,y,w,w);}
	public Vector4f zzxx(){return new Vector4f(z,z,x,x);}
	public Vector4f bbrr(){return new Vector4f(z,z,x,x);}
	public Vector4f zzxy(){return new Vector4f(z,z,x,y);}
	public Vector4f bbrg(){return new Vector4f(z,z,x,y);}
	public Vector4f zzxz(){return new Vector4f(z,z,x,z);}
	public Vector4f bbrb(){return new Vector4f(z,z,x,z);}
	public Vector4f zzxw(){return new Vector4f(z,z,x,w);}
	public Vector4f bbra(){return new Vector4f(z,z,x,w);}
	public Vector4f zzyx(){return new Vector4f(z,z,y,x);}
	public Vector4f bbgr(){return new Vector4f(z,z,y,x);}
	public Vector4f zzyy(){return new Vector4f(z,z,y,y);}
	public Vector4f bbgg(){return new Vector4f(z,z,y,y);}
	public Vector4f zzyz(){return new Vector4f(z,z,y,z);}
	public Vector4f bbgb(){return new Vector4f(z,z,y,z);}
	public Vector4f zzyw(){return new Vector4f(z,z,y,w);}
	public Vector4f bbga(){return new Vector4f(z,z,y,w);}
	public Vector4f zzzx(){return new Vector4f(z,z,z,x);}
	public Vector4f bbbr(){return new Vector4f(z,z,z,x);}
	public Vector4f zzzy(){return new Vector4f(z,z,z,y);}
	public Vector4f bbbg(){return new Vector4f(z,z,z,y);}
	public Vector4f zzzz(){return new Vector4f(z,z,z,z);}
	public Vector4f bbbb(){return new Vector4f(z,z,z,z);}
	public Vector4f zzzw(){return new Vector4f(z,z,z,w);}
	public Vector4f bbba(){return new Vector4f(z,z,z,w);}
	public Vector4f zzwx(){return new Vector4f(z,z,w,x);}
	public Vector4f bbar(){return new Vector4f(z,z,w,x);}
	public Vector4f zzwy(){return new Vector4f(z,z,w,y);}
	public Vector4f bbag(){return new Vector4f(z,z,w,y);}
	public Vector4f zzwz(){return new Vector4f(z,z,w,z);}
	public Vector4f bbab(){return new Vector4f(z,z,w,z);}
	public Vector4f zzww(){return new Vector4f(z,z,w,w);}
	public Vector4f bbaa(){return new Vector4f(z,z,w,w);}
	public Vector4f zwxx(){return new Vector4f(z,w,x,x);}
	public Vector4f barr(){return new Vector4f(z,w,x,x);}
	public Vector4f zwxy(){return new Vector4f(z,w,x,y);}
	public Vector4f barg(){return new Vector4f(z,w,x,y);}
	public Vector4f zwxz(){return new Vector4f(z,w,x,z);}
	public Vector4f barb(){return new Vector4f(z,w,x,z);}
	public Vector4f zwxw(){return new Vector4f(z,w,x,w);}
	public Vector4f bara(){return new Vector4f(z,w,x,w);}
	public Vector4f zwyx(){return new Vector4f(z,w,y,x);}
	public Vector4f bagr(){return new Vector4f(z,w,y,x);}
	public Vector4f zwyy(){return new Vector4f(z,w,y,y);}
	public Vector4f bagg(){return new Vector4f(z,w,y,y);}
	public Vector4f zwyz(){return new Vector4f(z,w,y,z);}
	public Vector4f bagb(){return new Vector4f(z,w,y,z);}
	public Vector4f zwyw(){return new Vector4f(z,w,y,w);}
	public Vector4f baga(){return new Vector4f(z,w,y,w);}
	public Vector4f zwzx(){return new Vector4f(z,w,z,x);}
	public Vector4f babr(){return new Vector4f(z,w,z,x);}
	public Vector4f zwzy(){return new Vector4f(z,w,z,y);}
	public Vector4f babg(){return new Vector4f(z,w,z,y);}
	public Vector4f zwzz(){return new Vector4f(z,w,z,z);}
	public Vector4f babb(){return new Vector4f(z,w,z,z);}
	public Vector4f zwzw(){return new Vector4f(z,w,z,w);}
	public Vector4f baba(){return new Vector4f(z,w,z,w);}
	public Vector4f zwwx(){return new Vector4f(z,w,w,x);}
	public Vector4f baar(){return new Vector4f(z,w,w,x);}
	public Vector4f zwwy(){return new Vector4f(z,w,w,y);}
	public Vector4f baag(){return new Vector4f(z,w,w,y);}
	public Vector4f zwwz(){return new Vector4f(z,w,w,z);}
	public Vector4f baab(){return new Vector4f(z,w,w,z);}
	public Vector4f zwww(){return new Vector4f(z,w,w,w);}
	public Vector4f baaa(){return new Vector4f(z,w,w,w);}
	public Vector4f wxxx(){return new Vector4f(w,x,x,x);}
	public Vector4f arrr(){return new Vector4f(w,x,x,x);}
	public Vector4f wxxy(){return new Vector4f(w,x,x,y);}
	public Vector4f arrg(){return new Vector4f(w,x,x,y);}
	public Vector4f wxxz(){return new Vector4f(w,x,x,z);}
	public Vector4f arrb(){return new Vector4f(w,x,x,z);}
	public Vector4f wxxw(){return new Vector4f(w,x,x,w);}
	public Vector4f arra(){return new Vector4f(w,x,x,w);}
	public Vector4f wxyx(){return new Vector4f(w,x,y,x);}
	public Vector4f argr(){return new Vector4f(w,x,y,x);}
	public Vector4f wxyy(){return new Vector4f(w,x,y,y);}
	public Vector4f argg(){return new Vector4f(w,x,y,y);}
	public Vector4f wxyz(){return new Vector4f(w,x,y,z);}
	public Vector4f argb(){return new Vector4f(w,x,y,z);}
	public Vector4f wxyw(){return new Vector4f(w,x,y,w);}
	public Vector4f arga(){return new Vector4f(w,x,y,w);}
	public Vector4f wxzx(){return new Vector4f(w,x,z,x);}
	public Vector4f arbr(){return new Vector4f(w,x,z,x);}
	public Vector4f wxzy(){return new Vector4f(w,x,z,y);}
	public Vector4f arbg(){return new Vector4f(w,x,z,y);}
	public Vector4f wxzz(){return new Vector4f(w,x,z,z);}
	public Vector4f arbb(){return new Vector4f(w,x,z,z);}
	public Vector4f wxzw(){return new Vector4f(w,x,z,w);}
	public Vector4f arba(){return new Vector4f(w,x,z,w);}
	public Vector4f wxwx(){return new Vector4f(w,x,w,x);}
	public Vector4f arar(){return new Vector4f(w,x,w,x);}
	public Vector4f wxwy(){return new Vector4f(w,x,w,y);}
	public Vector4f arag(){return new Vector4f(w,x,w,y);}
	public Vector4f wxwz(){return new Vector4f(w,x,w,z);}
	public Vector4f arab(){return new Vector4f(w,x,w,z);}
	public Vector4f wxww(){return new Vector4f(w,x,w,w);}
	public Vector4f araa(){return new Vector4f(w,x,w,w);}
	public Vector4f wyxx(){return new Vector4f(w,y,x,x);}
	public Vector4f agrr(){return new Vector4f(w,y,x,x);}
	public Vector4f wyxy(){return new Vector4f(w,y,x,y);}
	public Vector4f agrg(){return new Vector4f(w,y,x,y);}
	public Vector4f wyxz(){return new Vector4f(w,y,x,z);}
	public Vector4f agrb(){return new Vector4f(w,y,x,z);}
	public Vector4f wyxw(){return new Vector4f(w,y,x,w);}
	public Vector4f agra(){return new Vector4f(w,y,x,w);}
	public Vector4f wyyx(){return new Vector4f(w,y,y,x);}
	public Vector4f aggr(){return new Vector4f(w,y,y,x);}
	public Vector4f wyyy(){return new Vector4f(w,y,y,y);}
	public Vector4f aggg(){return new Vector4f(w,y,y,y);}
	public Vector4f wyyz(){return new Vector4f(w,y,y,z);}
	public Vector4f aggb(){return new Vector4f(w,y,y,z);}
	public Vector4f wyyw(){return new Vector4f(w,y,y,w);}
	public Vector4f agga(){return new Vector4f(w,y,y,w);}
	public Vector4f wyzx(){return new Vector4f(w,y,z,x);}
	public Vector4f agbr(){return new Vector4f(w,y,z,x);}
	public Vector4f wyzy(){return new Vector4f(w,y,z,y);}
	public Vector4f agbg(){return new Vector4f(w,y,z,y);}
	public Vector4f wyzz(){return new Vector4f(w,y,z,z);}
	public Vector4f agbb(){return new Vector4f(w,y,z,z);}
	public Vector4f wyzw(){return new Vector4f(w,y,z,w);}
	public Vector4f agba(){return new Vector4f(w,y,z,w);}
	public Vector4f wywx(){return new Vector4f(w,y,w,x);}
	public Vector4f agar(){return new Vector4f(w,y,w,x);}
	public Vector4f wywy(){return new Vector4f(w,y,w,y);}
	public Vector4f agag(){return new Vector4f(w,y,w,y);}
	public Vector4f wywz(){return new Vector4f(w,y,w,z);}
	public Vector4f agab(){return new Vector4f(w,y,w,z);}
	public Vector4f wyww(){return new Vector4f(w,y,w,w);}
	public Vector4f agaa(){return new Vector4f(w,y,w,w);}
	public Vector4f wzxx(){return new Vector4f(w,z,x,x);}
	public Vector4f abrr(){return new Vector4f(w,z,x,x);}
	public Vector4f wzxy(){return new Vector4f(w,z,x,y);}
	public Vector4f abrg(){return new Vector4f(w,z,x,y);}
	public Vector4f wzxz(){return new Vector4f(w,z,x,z);}
	public Vector4f abrb(){return new Vector4f(w,z,x,z);}
	public Vector4f wzxw(){return new Vector4f(w,z,x,w);}
	public Vector4f abra(){return new Vector4f(w,z,x,w);}
	public Vector4f wzyx(){return new Vector4f(w,z,y,x);}
	public Vector4f abgr(){return new Vector4f(w,z,y,x);}
	public Vector4f wzyy(){return new Vector4f(w,z,y,y);}
	public Vector4f abgg(){return new Vector4f(w,z,y,y);}
	public Vector4f wzyz(){return new Vector4f(w,z,y,z);}
	public Vector4f abgb(){return new Vector4f(w,z,y,z);}
	public Vector4f wzyw(){return new Vector4f(w,z,y,w);}
	public Vector4f abga(){return new Vector4f(w,z,y,w);}
	public Vector4f wzzx(){return new Vector4f(w,z,z,x);}
	public Vector4f abbr(){return new Vector4f(w,z,z,x);}
	public Vector4f wzzy(){return new Vector4f(w,z,z,y);}
	public Vector4f abbg(){return new Vector4f(w,z,z,y);}
	public Vector4f wzzz(){return new Vector4f(w,z,z,z);}
	public Vector4f abbb(){return new Vector4f(w,z,z,z);}
	public Vector4f wzzw(){return new Vector4f(w,z,z,w);}
	public Vector4f abba(){return new Vector4f(w,z,z,w);}
	public Vector4f wzwx(){return new Vector4f(w,z,w,x);}
	public Vector4f abar(){return new Vector4f(w,z,w,x);}
	public Vector4f wzwy(){return new Vector4f(w,z,w,y);}
	public Vector4f abag(){return new Vector4f(w,z,w,y);}
	public Vector4f wzwz(){return new Vector4f(w,z,w,z);}
	public Vector4f abab(){return new Vector4f(w,z,w,z);}
	public Vector4f wzww(){return new Vector4f(w,z,w,w);}
	public Vector4f abaa(){return new Vector4f(w,z,w,w);}
	public Vector4f wwxx(){return new Vector4f(w,w,x,x);}
	public Vector4f aarr(){return new Vector4f(w,w,x,x);}
	public Vector4f wwxy(){return new Vector4f(w,w,x,y);}
	public Vector4f aarg(){return new Vector4f(w,w,x,y);}
	public Vector4f wwxz(){return new Vector4f(w,w,x,z);}
	public Vector4f aarb(){return new Vector4f(w,w,x,z);}
	public Vector4f wwxw(){return new Vector4f(w,w,x,w);}
	public Vector4f aara(){return new Vector4f(w,w,x,w);}
	public Vector4f wwyx(){return new Vector4f(w,w,y,x);}
	public Vector4f aagr(){return new Vector4f(w,w,y,x);}
	public Vector4f wwyy(){return new Vector4f(w,w,y,y);}
	public Vector4f aagg(){return new Vector4f(w,w,y,y);}
	public Vector4f wwyz(){return new Vector4f(w,w,y,z);}
	public Vector4f aagb(){return new Vector4f(w,w,y,z);}
	public Vector4f wwyw(){return new Vector4f(w,w,y,w);}
	public Vector4f aaga(){return new Vector4f(w,w,y,w);}
	public Vector4f wwzx(){return new Vector4f(w,w,z,x);}
	public Vector4f aabr(){return new Vector4f(w,w,z,x);}
	public Vector4f wwzy(){return new Vector4f(w,w,z,y);}
	public Vector4f aabg(){return new Vector4f(w,w,z,y);}
	public Vector4f wwzz(){return new Vector4f(w,w,z,z);}
	public Vector4f aabb(){return new Vector4f(w,w,z,z);}
	public Vector4f wwzw(){return new Vector4f(w,w,z,w);}
	public Vector4f aaba(){return new Vector4f(w,w,z,w);}
	public Vector4f wwwx(){return new Vector4f(w,w,w,x);}
	public Vector4f aaar(){return new Vector4f(w,w,w,x);}
	public Vector4f wwwy(){return new Vector4f(w,w,w,y);}
	public Vector4f aaag(){return new Vector4f(w,w,w,y);}
	public Vector4f wwwz(){return new Vector4f(w,w,w,z);}
	public Vector4f aaab(){return new Vector4f(w,w,w,z);}
	public Vector4f wwww(){return new Vector4f(w,w,w,w);}
	public Vector4f aaaa(){return new Vector4f(w,w,w,w);}
	public void xyzw(Vector4f vec){x = vec.x;y = vec.y;z = vec.z;w = vec.w;}
	public void rgba(Vector4f vec){x = vec.x;y = vec.y;z = vec.z;w = vec.w;}
	public void xywz(Vector4f vec){x = vec.x;y = vec.y;w = vec.z;z = vec.w;}
	public void rgab(Vector4f vec){x = vec.x;y = vec.y;w = vec.z;z = vec.w;}
	public void xzyw(Vector4f vec){x = vec.x;z = vec.y;y = vec.z;w = vec.w;}
	public void rbga(Vector4f vec){x = vec.x;z = vec.y;y = vec.z;w = vec.w;}
	public void xzwy(Vector4f vec){x = vec.x;z = vec.y;w = vec.z;y = vec.w;}
	public void rbag(Vector4f vec){x = vec.x;z = vec.y;w = vec.z;y = vec.w;}
	public void xwyz(Vector4f vec){x = vec.x;w = vec.y;y = vec.z;z = vec.w;}
	public void ragb(Vector4f vec){x = vec.x;w = vec.y;y = vec.z;z = vec.w;}
	public void xwzy(Vector4f vec){x = vec.x;w = vec.y;z = vec.z;y = vec.w;}
	public void rabg(Vector4f vec){x = vec.x;w = vec.y;z = vec.z;y = vec.w;}
	public void yxzw(Vector4f vec){y = vec.x;x = vec.y;z = vec.z;w = vec.w;}
	public void grba(Vector4f vec){y = vec.x;x = vec.y;z = vec.z;w = vec.w;}
	public void yxwz(Vector4f vec){y = vec.x;x = vec.y;w = vec.z;z = vec.w;}
	public void grab(Vector4f vec){y = vec.x;x = vec.y;w = vec.z;z = vec.w;}
	public void yzxw(Vector4f vec){y = vec.x;z = vec.y;x = vec.z;w = vec.w;}
	public void gbra(Vector4f vec){y = vec.x;z = vec.y;x = vec.z;w = vec.w;}
	public void yzwx(Vector4f vec){y = vec.x;z = vec.y;w = vec.z;x = vec.w;}
	public void gbar(Vector4f vec){y = vec.x;z = vec.y;w = vec.z;x = vec.w;}
	public void ywxz(Vector4f vec){y = vec.x;w = vec.y;x = vec.z;z = vec.w;}
	public void garb(Vector4f vec){y = vec.x;w = vec.y;x = vec.z;z = vec.w;}
	public void ywzx(Vector4f vec){y = vec.x;w = vec.y;z = vec.z;x = vec.w;}
	public void gabr(Vector4f vec){y = vec.x;w = vec.y;z = vec.z;x = vec.w;}
	public void zxyw(Vector4f vec){z = vec.x;x = vec.y;y = vec.z;w = vec.w;}
	public void brga(Vector4f vec){z = vec.x;x = vec.y;y = vec.z;w = vec.w;}
	public void zxwy(Vector4f vec){z = vec.x;x = vec.y;w = vec.z;y = vec.w;}
	public void brag(Vector4f vec){z = vec.x;x = vec.y;w = vec.z;y = vec.w;}
	public void zyxw(Vector4f vec){z = vec.x;y = vec.y;x = vec.z;w = vec.w;}
	public void bgra(Vector4f vec){z = vec.x;y = vec.y;x = vec.z;w = vec.w;}
	public void zywx(Vector4f vec){z = vec.x;y = vec.y;w = vec.z;x = vec.w;}
	public void bgar(Vector4f vec){z = vec.x;y = vec.y;w = vec.z;x = vec.w;}
	public void zwxy(Vector4f vec){z = vec.x;w = vec.y;x = vec.z;y = vec.w;}
	public void barg(Vector4f vec){z = vec.x;w = vec.y;x = vec.z;y = vec.w;}
	public void zwyx(Vector4f vec){z = vec.x;w = vec.y;y = vec.z;x = vec.w;}
	public void bagr(Vector4f vec){z = vec.x;w = vec.y;y = vec.z;x = vec.w;}
	public void wxyz(Vector4f vec){w = vec.x;x = vec.y;y = vec.z;z = vec.w;}
	public void argb(Vector4f vec){w = vec.x;x = vec.y;y = vec.z;z = vec.w;}
	public void wxzy(Vector4f vec){w = vec.x;x = vec.y;z = vec.z;y = vec.w;}
	public void arbg(Vector4f vec){w = vec.x;x = vec.y;z = vec.z;y = vec.w;}
	public void wyxz(Vector4f vec){w = vec.x;y = vec.y;x = vec.z;z = vec.w;}
	public void agrb(Vector4f vec){w = vec.x;y = vec.y;x = vec.z;z = vec.w;}
	public void wyzx(Vector4f vec){w = vec.x;y = vec.y;z = vec.z;x = vec.w;}
	public void agbr(Vector4f vec){w = vec.x;y = vec.y;z = vec.z;x = vec.w;}
	public void wzxy(Vector4f vec){w = vec.x;z = vec.y;x = vec.z;y = vec.w;}
	public void abrg(Vector4f vec){w = vec.x;z = vec.y;x = vec.z;y = vec.w;}
	public void wzyx(Vector4f vec){w = vec.x;z = vec.y;y = vec.z;x = vec.w;}
	public void abgr(Vector4f vec){w = vec.x;z = vec.y;y = vec.z;x = vec.w;}
	public void yzw(Vector3f vec){y = vec.x;z = vec.y;w = vec.z;}
	public void gba(Vector3f vec){y = vec.x;z = vec.y;w = vec.z;}
	public void ywz(Vector3f vec){y = vec.x;w = vec.y;z = vec.z;}
	public void gab(Vector3f vec){y = vec.x;w = vec.y;z = vec.z;}
	public void zyw(Vector3f vec){z = vec.x;y = vec.y;w = vec.z;}
	public void bga(Vector3f vec){z = vec.x;y = vec.y;w = vec.z;}
	public void zwy(Vector3f vec){z = vec.x;w = vec.y;y = vec.z;}
	public void bag(Vector3f vec){z = vec.x;w = vec.y;y = vec.z;}
	public void wyz(Vector3f vec){w = vec.x;y = vec.y;z = vec.z;}
	public void agb(Vector3f vec){w = vec.x;y = vec.y;z = vec.z;}
	public void wzy(Vector3f vec){w = vec.x;z = vec.y;y = vec.z;}
	public void abg(Vector3f vec){w = vec.x;z = vec.y;y = vec.z;}
	public void zw(Vector2f vec){z = vec.x;w = vec.y;}
	public void ba(Vector2f vec){z = vec.x;w = vec.y;}
	public void wz(Vector2f vec){w = vec.x;z = vec.y;}
	public void ab(Vector2f vec){w = vec.x;z = vec.y;}
	public void w(float f){w = f;}
	public void a(float f){w = f;}
	public void z(float f){z = f;}
	public void b(float f){z = f;}
	public void yw(Vector2f vec){y = vec.x;w = vec.y;}
	public void ga(Vector2f vec){y = vec.x;w = vec.y;}
	public void wy(Vector2f vec){w = vec.x;y = vec.y;}
	public void ag(Vector2f vec){w = vec.x;y = vec.y;}
	public void y(float f){y = f;}
	public void g(float f){y = f;}
	public void yz(Vector2f vec){y = vec.x;z = vec.y;}
	public void gb(Vector2f vec){y = vec.x;z = vec.y;}
	public void zy(Vector2f vec){z = vec.x;y = vec.y;}
	public void bg(Vector2f vec){z = vec.x;y = vec.y;}
	public void xzw(Vector3f vec){x = vec.x;z = vec.y;w = vec.z;}
	public void rba(Vector3f vec){x = vec.x;z = vec.y;w = vec.z;}
	public void xwz(Vector3f vec){x = vec.x;w = vec.y;z = vec.z;}
	public void rab(Vector3f vec){x = vec.x;w = vec.y;z = vec.z;}
	public void zxw(Vector3f vec){z = vec.x;x = vec.y;w = vec.z;}
	public void bra(Vector3f vec){z = vec.x;x = vec.y;w = vec.z;}
	public void zwx(Vector3f vec){z = vec.x;w = vec.y;x = vec.z;}
	public void bar(Vector3f vec){z = vec.x;w = vec.y;x = vec.z;}
	public void wxz(Vector3f vec){w = vec.x;x = vec.y;z = vec.z;}
	public void arb(Vector3f vec){w = vec.x;x = vec.y;z = vec.z;}
	public void wzx(Vector3f vec){w = vec.x;z = vec.y;x = vec.z;}
	public void abr(Vector3f vec){w = vec.x;z = vec.y;x = vec.z;}
	public void xw(Vector2f vec){x = vec.x;w = vec.y;}
	public void ra(Vector2f vec){x = vec.x;w = vec.y;}
	public void wx(Vector2f vec){w = vec.x;x = vec.y;}
	public void ar(Vector2f vec){w = vec.x;x = vec.y;}
	public void x(float f){x = f;}
	public void r(float f){x = f;}
	public void xz(Vector2f vec){x = vec.x;z = vec.y;}
	public void rb(Vector2f vec){x = vec.x;z = vec.y;}
	public void zx(Vector2f vec){z = vec.x;x = vec.y;}
	public void br(Vector2f vec){z = vec.x;x = vec.y;}
	public void xyw(Vector3f vec){x = vec.x;y = vec.y;w = vec.z;}
	public void rga(Vector3f vec){x = vec.x;y = vec.y;w = vec.z;}
	public void xwy(Vector3f vec){x = vec.x;w = vec.y;y = vec.z;}
	public void rag(Vector3f vec){x = vec.x;w = vec.y;y = vec.z;}
	public void yxw(Vector3f vec){y = vec.x;x = vec.y;w = vec.z;}
	public void gra(Vector3f vec){y = vec.x;x = vec.y;w = vec.z;}
	public void ywx(Vector3f vec){y = vec.x;w = vec.y;x = vec.z;}
	public void gar(Vector3f vec){y = vec.x;w = vec.y;x = vec.z;}
	public void wxy(Vector3f vec){w = vec.x;x = vec.y;y = vec.z;}
	public void arg(Vector3f vec){w = vec.x;x = vec.y;y = vec.z;}
	public void wyx(Vector3f vec){w = vec.x;y = vec.y;x = vec.z;}
	public void agr(Vector3f vec){w = vec.x;y = vec.y;x = vec.z;}
	public void xy(Vector2f vec){x = vec.x;y = vec.y;}
	public void rg(Vector2f vec){x = vec.x;y = vec.y;}
	public void yx(Vector2f vec){y = vec.x;x = vec.y;}
	public void gr(Vector2f vec){y = vec.x;x = vec.y;}
	public void xyz(Vector3f vec){x = vec.x;y = vec.y;z = vec.z;}
	public void rgb(Vector3f vec){x = vec.x;y = vec.y;z = vec.z;}
	public void xzy(Vector3f vec){x = vec.x;z = vec.y;y = vec.z;}
	public void rbg(Vector3f vec){x = vec.x;z = vec.y;y = vec.z;}
	public void yxz(Vector3f vec){y = vec.x;x = vec.y;z = vec.z;}
	public void grb(Vector3f vec){y = vec.x;x = vec.y;z = vec.z;}
	public void yzx(Vector3f vec){y = vec.x;z = vec.y;x = vec.z;}
	public void gbr(Vector3f vec){y = vec.x;z = vec.y;x = vec.z;}
	public void zxy(Vector3f vec){z = vec.x;x = vec.y;y = vec.z;}
	public void brg(Vector3f vec){z = vec.x;x = vec.y;y = vec.z;}
	public void zyx(Vector3f vec){z = vec.x;y = vec.y;x = vec.z;}
	public void bgr(Vector3f vec){z = vec.x;y = vec.y;x = vec.z;}

}
