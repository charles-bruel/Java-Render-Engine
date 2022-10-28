package net.chazzvader.core.generic.math;

/**
 * A quaternion is a 4 component complex number that has uses for computing
 * rotations. Quaternion rotations are more stable than Euler angles, (not
 * having issues near -90 and 90 degrees pitch, for example), so they are used
 * in computer graphics extensively.<br>
 * <br>
 * A quaternion is simply a vector of 4 floats, however unlike Euler angles that
 * just exist as a subset of <code>Vector3f</code>, the massive difference in
 * multiplication and other required functions necessitates a new class.<br>
 * <br>
 * Since rotations are the primary and in many cases only use of quaternions, it
 * makes sense to explain how they work. I can recommend 3b1b and Ben Eater's
 * excellent interactive video series
 * <a href="https://eater.net/quaternions">here</a> (I have no connection with
 * them, its just a great resource).<br>
 * <br>
 * Simply put, a quaternion is a 4d number with 1 real component and 3 complex
 * ones. By putting it through this cryptic formula
 * <code>v' = qvq<sup>-1</sup></code>, where <code>q</code> is the quaternion
 * and <code>v</code> is the vector, with <code>xyz</code> corresponding to the
 * <code>ijk</code> imaginary components with no real components, and
 * <code>v'</code> is the result, again with <code>xyz</code> being the
 * <code>ijk</code> components. Creating rotations is simply just the angle and
 * the axis
 * <code>cos(θ) + sin(θ)(a<sub>x</sub>i + a<sub>y</sub>j + a<sub>z</sub>k)</code>
 * where <code>θ</code> is half the angle to rotate by, and <code>a</code> is
 * the <em>normalized</em> vector representing the axis of rotation. Combing
 * rotations is as simple as multiplying them.
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 * @see Vector3f
 * @see Vector4f
 * @see Matrix4f
 */
@SuppressWarnings("javadoc")
public class Quaternion {

	/**
	 * A singularity arises during quaternion to Euler conversion as the
	 * pitch/attitude approaches <code>±90°</code>, where Euler gimbal lock occurs.
	 * This number is the threshold after which the <code>sin</code> of the pitch is
	 * considered close enough to the singularities for the special case actions,
	 * modifying the other angles.<br>
	 * <br>
	 * Setting this to high could cause floating point precision issues, setting it
	 * too low restrict pitch freedom and can cause weird drift of the yaw/heading.
	 * This value after an exhaustive 1 test was determined, so it may need to be
	 * refined slightly.
	 */
	public static final float SINGULARITY_THRESHOLD = 0.99999f;

	/**
	 * The components of the quaternion. Several different standards are used for
	 * labeling, I have personally gone with abcd but other source may rijk, wxyz,
	 * etc.<br>
	 * <br>
	 * <code>a</code>: the real component, AKA <code>r</code>, <code>w</code>,
	 * <code>q<sub>r</sub></code>, <code>q<sub>w</sub></code>.<br>
	 * <code>b</code>: the real component, AKA <code>i</code>, <code>x</code>,
	 * <code>q<sub>i</sub></code>, <code>q<sub>x</sub></code>.<br>
	 * <code>c</code>: the real component, AKA <code>j</code>, <code>y</code>,
	 * <code>q<sub>j</sub></code>, <code>q<sub>y</sub></code>.<br>
	 * <code>d</code>: the real component, AKA <code>k</code>, <code>z</code>,
	 * <code>q<sub>k</sub></code>, <code>q<sub>z</sub></code>.<br>
	 */
	public float a = 1, b = 0, c = 0, d = 0;

	/**
	 * Creates a new quaternion with the specified a, b, c, and d values.
	 * 
	 * @param a The a value
	 * @param b The b value
	 * @param c The c value
	 * @param d The d value
	 */
	public Quaternion(float a, float b, float c, float d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	/**
	 * Generates a quaternion from the vector, assuming the position is going to be
	 * used for rotation. Thus the quaternion will be (0, x, y, z).
	 * 
	 * @param position The position to generate from.
	 * @see #applyRotation(Quaternion)
	 */
	public Quaternion(Vector3f position) {
		this(0, position.x, position.y, position.z);
	}

	/**
	 * Creates a new, identity quaternion.
	 */
	public Quaternion() {

	}

	/**
	 * Multiplies two quaternions. (the quaternion this is called on is
	 * affected)<br>
	 * <br>
	 * This is equivalent to apply the rotation q after the rotation this quaternion
	 * represents, if you are using quaternions to represent rotations.
	 * 
	 * @param q The quaternion to multiply by.
	 * @return The multiplied quaternion.
	 */
	public Quaternion mul(Quaternion q) {
		Quaternion copy = copyOf(this);
		this.a = (copy.a * q.a - copy.b * q.b - copy.c * q.c - copy.d * q.d);
		this.b = (copy.a * q.b + copy.b * q.a + copy.c * q.d - copy.d * q.c);
		this.c = (copy.a * q.c - copy.b * q.d + copy.c * q.a + copy.d * q.b);
		this.d = (copy.a * q.d + copy.b * q.c - copy.c * q.b + copy.d * q.a);
		return this;
	}

	/**
	 * Multiplies two quaternions. (the quaternion this is called on is not
	 * affected)<br>
	 * <br>
	 * This is equivalent to apply the rotation q after the rotation this quaternion
	 * represents, if you are using quaternions to represent rotations.
	 * 
	 * @param q The quaternion to multiply by.
	 * @return The multiplied quaternion.
	 */
	public Quaternion mulCopy(Quaternion q) {
		return copyOf(this).mul(q);
	}

	/**
	 * Creates a rotation quaternion, for a rotation of the angle value <em>IN
	 * RADIANS</em> around the specified axis value.
	 * 
	 * @param angle The angle to rotate by.
	 * @param axis  The axis to rotate around.
	 * @return The created rotation quaternion.
	 */
	public static Quaternion rotation(float angle, Vector3f axis) {
		axis = axis.normalizeCopy();
		float a = (float) Math.sin(angle / 2);
		float b = (float) Math.cos(angle / 2);
		return new Quaternion(b, axis.x * a, axis.y * a, axis.z * a);
	}

	/**
	 * Creates a rotation quaternion, for a rotation of the angle value <em>IN
	 * RADIANS</em> around the specified axis value. This is global a global
	 * operation and works by constructing an arbitrary axis <code>A</code> that
	 * would rotate around. This is done by applying the inverse quaternion rotation
	 * to the axis to construct it.<br>
	 * <code>A = q<sup>-1</sup>gq</code>.
	 * 
	 * @param angle      The angle to rotate by.
	 * @param axis       The axis to rotate around.
	 * @param quaternion The quaternion to make the rotation global for.
	 * @return The created rotation quaternion.
	 */
	public static Quaternion rotationGlobal(float angle, Vector3f axis, Quaternion quaternion) {
		axis = axis.normalizeCopy();
		quaternion = quaternion.inverseCopy();
		axis = quaternion.applyRotation(Vector3f.copyOf(axis));
		return rotation(angle, axis);
	}

	/**
	 * Applies a rotation to this quaternion of the rotation of the angle value
	 * <em>IN RADIANS</em> around the specified axis value. This is global a global
	 * operation and works by constructing an arbitrary axis <code>A</code> that
	 * would rotate around. This is done by applying the inverse quaternion rotation
	 * to the axis to construct it.<br>
	 * <code>A = q<sup>-1</sup>gq</code>. (the quaternion this is called on is
	 * affected)
	 * 
	 * @param angle      The angle to rotate by.
	 * @param axis       The axis to rotate around.
	 * @param quaternion The quaternion to make the rotation global for.
	 * @return The created rotation quaternion.
	 */
	public Quaternion rotateGlobal(float angle, Vector3f axis, Quaternion quaternion) {
		return mul(rotationGlobal(angle, axis, quaternion));
	}

	/**
	 * Applies a rotation to this quaternion of the rotation of the angle value
	 * <em>IN RADIANS</em> around the specified axis value. This is global a global
	 * operation and works by constructing an arbitrary axis <code>A</code> that
	 * would rotate around. This is done by applying the inverse quaternion rotation
	 * to the axis to construct it.<br>
	 * <code>A = q<sup>-1</sup>gq</code>. (the quaternion this is called on is not
	 * affected)
	 * 
	 * @param angle      The angle to rotate by.
	 * @param axis       The axis to rotate around.
	 * @param quaternion The quaternion to make the rotation global for.
	 * @return The created rotation quaternion.
	 */
	public Quaternion rotateGlobalCopy(float angle, Vector3f axis, Quaternion quaternion) {
		return copyOf(this).rotateGlobal(angle, axis, quaternion);
	}

	/**
	 * Applies a rotation to this quaternion of the rotation of the angle value
	 * <em>IN RADIANS</em> around the specified axis value. (the quaternion this is
	 * called on is affected)
	 * 
	 * @param angle The angle to rotate by.
	 * @param axis  The axis to rotate around.
	 * @return The modified rotation quaternion.
	 */
	public Quaternion rotate(float angle, Vector3f axis) {
		return mul(rotation(angle, axis));
	}

	/**
	 * Applies a rotation to this quaternion of the rotation of the angle value
	 * <em>IN RADIANS</em> around the specified axis value. (the quaternion this is
	 * called on is not affected)
	 * 
	 * @param angle The angle to rotate by.
	 * @param axis  The axis to rotate around.
	 * @return The modified rotation quaternion.
	 */
	public Quaternion rotateCopy(float angle, Vector3f axis) {
		return copyOf(this).rotate(angle, axis);
	}

	/**
	 * This applies a the rotation the quaternion represents to the given
	 * vector.<br>
	 * <br>
	 * Quaternion rotation is represents by <code>v' = qvq<sup>-1</sup></code>, if
	 * <code>v</code> is the input vector, <code>v'</code> is the output vector and
	 * <code>q</code> is this quaternion. The output XYZ is the imaginary components
	 * b, c and d, respectively. That is why some resources use the labeling wxyz.
	 * 
	 * @param input The input vector to transform.
	 * @return The rotated vector.
	 * @see #inverse()
	 */
	public Vector3f applyRotation(Vector3f input) {		
		Quaternion result = applyRotation(new Quaternion(input));
		return result.toPosition();
	}
	
	/**
	 * This applies a the rotation the quaternion represents to the given
	 * vector.<br>
	 * <br>
	 * Quaternion rotation is represents by <code>v' = qvq<sup>-1</sup></code>, if
	 * <code>v</code> is the input vector, <code>v'</code> is the output vector and
	 * <code>q</code> is this quaternion. The output XYZ is the imaginary components
	 * b, c and d, respectively. That is why some resources use the labeling wxyz.
	 * 
	 * @param input The input quaternion to transform.
	 * @return The rotated quaternion.
	 * @see #inverse()
	 */
	public Quaternion applyRotation(Quaternion input) {
		Quaternion q = copyOf(this);
		Quaternion qInv = copyOf(q).inverse();

		Quaternion result = q.mul(copyOf(input)).mul(qInv);

		return result;
	}
	
	/**
	 * Extracts the position from a quaternion, used after rotating.
	 * @return The bcd values in a Vector3f.
	 * @see #applyRotation(Quaternion)
	 */
	public Vector3f toPosition() {
		return new Vector3f(b, c, d);
	}

	/**
	 * Generates a quaternion from the Euler angles. Source is <a href=
	 * "https://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm">Euclidean
	 * Space</a>
	 * 
	 * @param angles The angles.
	 * @return A quaternion representing the rotation
	 * @see Vector3f
	 */
	public static Quaternion fromEuler(Vector3f angles) {
		// Abbreviations for the various angular functions
		float c1 = (float) Math.cos(angles.y * 0.5);
		float s1 = (float) Math.sin(angles.y * 0.5);
		float c2 = (float) Math.cos(angles.z * 0.5);
		float s2 = (float) Math.sin(angles.z * 0.5);
		float c3 = (float) Math.cos(angles.x * 0.5);
		float s3 = (float) Math.sin(angles.x * 0.5);

		Quaternion result = new Quaternion();
		result.a = c1 * c2 * c3 - s1 * s2 * s3;
		result.b = s1 * s2 * c3 + c1 * c2 * s3;
		result.c = s1 * c2 * c3 + c1 * s2 * s3;
		result.d = c1 * s2 * c3 - s1 * c2 * s3;

		return result;
	}

	/**
	 * Converts the quaternion into equivalent Euler angles. <a href=
	 * "https://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm">Euclidean
	 * Space</a>
	 * 
	 * @return The a set of Euler angles equivalent to the quaternion.
	 * @see Vector3f
	 */
	public Vector3f toEuler() {
		Vector3f result = new Vector3f();

		float ypy = 2 * c * a - 2 * b * d;
		float ypx = 1 - 2 * c * c - 2 * d * d;
		result.y = (float) Math.atan2(ypy, ypx);

		float xpy = 2 * b * a - 2 * c * d;
		float xpx = 1 - 2 * b * b - 2 * d * d;
		result.x = (float) Math.atan2(xpy, xpx);

		float yp = 2 * b * c + 2 * d * a;

		if (yp >= SINGULARITY_THRESHOLD) {
			result.y = (float) (2 * Math.atan2(b, a));
			result.x = 0;
			yp = SINGULARITY_THRESHOLD;
		} else if (yp <= -SINGULARITY_THRESHOLD) {
			result.y = (float) (-2 * Math.atan2(b, a));
			result.x = 0;
			yp = -SINGULARITY_THRESHOLD;
		}

		result.z = (float) Math.asin(yp);

		return result;
	}

	/**
	 * Creates and returns a copy of the passed quaternion
	 * 
	 * @param q The quaternion to copy
	 * @return The copy
	 */
	public static Quaternion copyOf(Quaternion q) {
		return new Quaternion(q.a, q.b, q.c, q.d);
	}

	/**
	 * Gets the inverse of this quaternion, <code>q<sup>-1</sup></code>. Used for
	 * apply rotations. (the quaternion this is called on is affected)
	 * 
	 * @return The inverse of this quaternion.
	 * @see #applyRotation(Vector3f)
	 */
	public Quaternion inverse() {
		b *= -1;
		c *= -1;
		d *= -1;

		mul(1 / (a * a + b * b + c * c + d * d));

		return this;
	}

	/**
	 * Gets the inverse of this quaternion, <code>q<sup>-1</sup></code>. Used for
	 * apply rotations. (the quaternion this is called on is not affected)
	 * 
	 * @return The inverse of this quaternion.
	 * @see #applyRotation(Vector3f)
	 */
	public Quaternion inverseCopy() {
		return copyOf(this).inverse();
	}

	/**
	 * Multiplies the quaternion by a static scalar. This does make it a non unit
	 * quaternion, which is not useful for rotations. (the quaternion this is called
	 * on is affected)
	 * 
	 * @param scalar The scalar to multiply by.
	 */
	public Quaternion mul(float scalar) {
		a *= scalar;
		b *= scalar;
		c *= scalar;
		d *= scalar;

		return this;
	}

	/**
	 * Multiplies the quaternion by a static scalar. This does make it a non unit
	 * quaternion, which is not useful for rotations. (the quaternion this is called
	 * on is not affected)
	 * 
	 * @param scalar The scalar to multiply by.
	 */
	public Quaternion mulCopy(float scalar) {
		return copyOf(this).mul(scalar);
	}

	/**
	 * Gets the length of the quaternion, used for normalization.
	 * 
	 * @return The length of the quaternion.
	 */
	public float length() {
		return (float) Math.sqrt(a * a + b * b + c * c + d * d);
	}

	/**
	 * Normalizes a quaternion. The result will a quaternion with the ratio between
	 * components the same but a length of 1. (the quaternion this is called on is
	 * affected)<br>
	 * <br>
	 * Quaternions are required to be normalized for rotations to work.
	 * 
	 * @return The normalized quaternion
	 */
	public Quaternion normalize() {
		float length = length();
		if (length == 0) {
			return this;
		}
		a /= length;
		b /= length;
		c /= length;
		d /= length;
		return this;
	}

	/**
	 * Normalizes a quaternion. The result will a quaternion with the ratio between
	 * components the same but a length of 1. (the quaternion this is called on is
	 * not affected)<br>
	 * <br>
	 * Quaternions are required to be normalized for rotations to work.
	 * 
	 * @return The normalized quaternion
	 */
	public Quaternion normalizeCopy() {
		return copyOf(this).normalize();
	}
	
	public Vector3f toDirection() {
		Vector3f direction = StaticMath.POSITIVE_X;
		direction = applyRotation(direction);
		return direction;
	}

	@Override
	public String toString() {
		return "(" + a + ", " + b + "i, " + c + "j, " + d + "k)";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(this.a);
		result = prime * result + Float.floatToIntBits(this.b);
		result = prime * result + Float.floatToIntBits(this.c);
		result = prime * result + Float.floatToIntBits(this.d);
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
		Quaternion other = (Quaternion) obj;
		if (Float.floatToIntBits(this.a) != Float.floatToIntBits(other.a))
			return false;
		if (Float.floatToIntBits(this.b) != Float.floatToIntBits(other.b))
			return false;
		if (Float.floatToIntBits(this.c) != Float.floatToIntBits(other.c))
			return false;
		if (Float.floatToIntBits(this.d) != Float.floatToIntBits(other.d))
			return false;
		return true;
	}

}
