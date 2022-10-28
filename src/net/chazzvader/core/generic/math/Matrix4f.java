package net.chazzvader.core.generic.math;

import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;

/**
 * 4x4 Matrix of floats. Defaults to a identity matrix. Many of the functions
 * modify the objects they were called on, use the copy version of the method to
 * not modify the original. Additionally, many functions rely on creating
 * objects which can slow down the program if too many are created. Many
 * functions, therefore, have a version where you can pass matrices or other
 * math objects to use as scratch space instead of allocating new objects. You
 * can safely pass null to any of those values.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
@SuppressWarnings("javadoc")
public class Matrix4f {

	/**
	 * Creates a new identity matrix
	 */
	public Matrix4f() {
		
	}

	/**
	 * Values stored as v ROW COLUMN.<br>
	 * <link rel="stylesheet" type="text/css" href="C:/dev/Java/Engine/Java
	 * Engine/src/javadoc.css"/>
	 * <table summary="Table of values">
	 * <tr>
	 * <td>v00</td>
	 * <td>v01</td>
	 * <td>v02</td>
	 * <td>v03</td>
	 * </tr>
	 * <tr>
	 * <td>v10</td>
	 * <td>v11</td>
	 * <td>v12</td>
	 * <td>v13</td>
	 * </tr>
	 * <tr>
	 * <td>v20</td>
	 * <td>v21</td>
	 * <td>v22</td>
	 * <td>v23</td>
	 * </tr>
	 * <tr>
	 * <td>v30</td>
	 * <td>v31</td>
	 * <td>v32</td>
	 * <td>v33</td>
	 * </tr>
	 * </table>
	 * <br>
	 * Values are initialized to a identity matrix.
	 */
	public float v00 = 1, v10 = 0, v20 = 0, v30 = 0, v01 = 0, v11 = 1, v21 = 0, v31 = 0, v02 = 0, v12 = 0, v22 = 1,
			v32 = 0, v03 = 0, v13 = 0, v23 = 0, v33 = 1;

	/**
	 * Copies the values from the other matrix to this one.
	 * 
	 * @param mat The matrix to copy from.
	 * @return The matrix with the copied values.
	 */
	public Matrix4f transcribe(Matrix4f mat) {
		this.v00 = mat.v00;
		this.v01 = mat.v01;
		this.v02 = mat.v02;
		this.v03 = mat.v03;
		this.v10 = mat.v10;
		this.v11 = mat.v11;
		this.v12 = mat.v12;
		this.v13 = mat.v13;
		this.v20 = mat.v20;
		this.v21 = mat.v21;
		this.v22 = mat.v22;
		this.v23 = mat.v23;
		this.v30 = mat.v30;
		this.v31 = mat.v31;
		this.v32 = mat.v32;
		this.v33 = mat.v33;

		return this;
	}

	/**
	 * Copies the values to this matrix.
	 * 
	 * @return The matrix with the copied values.
	 */
	public Matrix4f transcribe(float v00, float v10, float v20, float v30, float v01, float v11, float v21, float v31,
			float v02, float v12, float v22, float v32, float v03, float v13, float v23, float v33) {
		this.v00 = v00;
		this.v01 = v01;
		this.v02 = v02;
		this.v03 = v03;
		this.v10 = v10;
		this.v11 = v11;
		this.v12 = v12;
		this.v13 = v13;
		this.v20 = v20;
		this.v21 = v21;
		this.v22 = v22;
		this.v23 = v23;
		this.v30 = v30;
		this.v31 = v31;
		this.v32 = v32;
		this.v33 = v33;

		return this;
	}

	/**
	 * Resets this matrix to a identity matrix.
	 */
	public void reset() {
		v00 = 1;
		v10 = 0;
		v20 = 0;
		v30 = 0;
		v01 = 0;
		v11 = 1;
		v21 = 0;
		v31 = 0;
		v02 = 0;
		v12 = 0;
		v22 = 1;
		v32 = 0;
		v03 = 0;
		v13 = 0;
		v23 = 0;
		v33 = 1;
	}

	/**
	 * Creates a matrix with the specified float values
	 * 
	 * @param v00 Row 1 Column 1
	 * @param v10 Row 2 Column 1
	 * @param v20 Row 3 Column 1
	 * @param v30 Row 4 Column 1
	 * @param v01 Row 1 Column 2
	 * @param v11 Row 2 Column 2
	 * @param v21 Row 3 Column 2
	 * @param v31 Row 4 Column 2
	 * @param v02 Row 1 Column 3
	 * @param v12 Row 2 Column 3
	 * @param v22 Row 3 Column 3
	 * @param v32 Row 4 Column 3
	 * @param v03 Row 1 Column 4
	 * @param v13 Row 2 Column 4
	 * @param v23 Row 3 Column 4
	 * @param v33 Row 4 Column 4
	 */
	public Matrix4f(float v00, float v10, float v20, float v30, float v01, float v11, float v21, float v31, float v02,
			float v12, float v22, float v32, float v03, float v13, float v23, float v33) {
		this.v00 = v00;
		this.v10 = v10;
		this.v20 = v20;
		this.v30 = v30;
		this.v01 = v01;
		this.v11 = v11;
		this.v21 = v21;
		this.v31 = v31;
		this.v02 = v02;
		this.v12 = v12;
		this.v22 = v22;
		this.v32 = v32;
		this.v03 = v03;
		this.v13 = v13;
		this.v23 = v23;
		this.v33 = v33;
	}
	
	/**
	 * Returns a copy of the passed matrix
	 * 
	 * @param copyFrom The matrix to copy from
	 * @return A copy of the matrix
	 */
	public static Matrix4f copyOf(Matrix4f copyFrom) {
		Matrix4f newMat = new Matrix4f();
		newMat.v00 = copyFrom.v00;
		newMat.v01 = copyFrom.v01;
		newMat.v02 = copyFrom.v02;
		newMat.v03 = copyFrom.v03;

		newMat.v10 = copyFrom.v10;
		newMat.v11 = copyFrom.v11;
		newMat.v12 = copyFrom.v12;
		newMat.v13 = copyFrom.v13;

		newMat.v20 = copyFrom.v20;
		newMat.v21 = copyFrom.v21;
		newMat.v22 = copyFrom.v22;
		newMat.v23 = copyFrom.v23;

		newMat.v30 = copyFrom.v30;
		newMat.v31 = copyFrom.v31;
		newMat.v32 = copyFrom.v32;
		newMat.v33 = copyFrom.v33;

		return newMat;
	}

	/**
	 * Takes in a length 16 array, returns the values transposed onto a 4x4 matrix
	 * in column major order
	 * 
	 * @param array The array to take in
	 * @return The array as a matrix
	 */
	public static Matrix4f fromFloatArray(float[] array) {
		return fromFloatArrayCMO(array);
	}

	/**
	 * Takes in a length 16 array, returns the values transposed onto a 4x4 matrix
	 * in column major order
	 * 
	 * @param array The array to take in
	 * @return The array as a matrix, a blank matrix if the array is invalid
	 */
	public static Matrix4f fromFloatArrayCMO(float[] array) {
		if (array == null) {
			Logging.log("Unable to insert float[] to matrix array is null", "Matrix", LoggingLevel.WARN);
			return new Matrix4f();
		}
		if (array.length != 16) {
			Logging.log("Unable to insert float[] to matrix, length != 16", "Matrix", LoggingLevel.WARN);
			return new Matrix4f();
		}
		Matrix4f mat = new Matrix4f();
		mat.v00 = array[0];
		mat.v10 = array[1];
		mat.v20 = array[2];
		mat.v30 = array[3];

		mat.v01 = array[4];
		mat.v11 = array[5];
		mat.v21 = array[6];
		mat.v31 = array[7];

		mat.v02 = array[8];
		mat.v12 = array[9];
		mat.v22 = array[10];
		mat.v32 = array[11];

		mat.v03 = array[12];
		mat.v13 = array[13];
		mat.v23 = array[14];
		mat.v33 = array[15];

		return mat;
	}

	/**
	 * Takes in a length 16 array, returns the values transposed onto a 4x4 matrix
	 * in row major order
	 * 
	 * @param array The array to take in
	 * @return The array as a matrix, a blank matrix if the array is invalid
	 */
	public static Matrix4f fromFloatArrayRMO(float[] array) {
		if (array == null) {
			Logging.log("Unable to insert float[] to matrix array is null", "Matrix", LoggingLevel.WARN);
			return new Matrix4f();
		}
		if (array.length != 16) {
			Logging.log("Unable to insert float[] to matrix, length != 16", "Matrix", LoggingLevel.WARN);
			return new Matrix4f();
		}
		Matrix4f mat = new Matrix4f();
		mat.v00 = array[0];
		mat.v01 = array[1];
		mat.v02 = array[2];
		mat.v03 = array[3];

		mat.v10 = array[4];
		mat.v11 = array[5];
		mat.v12 = array[6];
		mat.v13 = array[7];

		mat.v20 = array[8];
		mat.v21 = array[9];
		mat.v22 = array[10];
		mat.v23 = array[11];

		mat.v30 = array[12];
		mat.v31 = array[13];
		mat.v32 = array[14];
		mat.v33 = array[15];

		return mat;
	}

	/**
	 * Creates a look at matrix for a camera. Includes translation.
	 * <link rel="stylesheet" type="text/css" href="C:/dev/Java/Engine/Java
	 * Engine/src/javadoc.css"/>
	 * <table summary="Table of values">
	 * <tr>
	 * <td>Right<sub>x</sub></td>
	 * <td>Right<sub>y</sub></td>
	 * <td>Right<sub>z</sub></td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>Up<sub>x</sub></td>
	 * <td>Up<sub>y</sub></td>
	 * <td>Up<sub>z</sub></td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>Direction<sub>x</sub></td>
	 * <td>Direction<sub>y</sub></td>
	 * <td>Direction<sub>z</sub></td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>1</td>
	 * </tr>
	 * </table>
	 * 
	 * When<br>
	 * <code>Right</code> is a vector right of the target to the camera and is
	 * <code>normalize(cross(up, z))</code> <br>
	 * <code>Up</code> is a vector up from the target to the camera and is
	 * <code>normalize(cross(z, x))</code> <br>
	 * <code>Direction</code> is a vector pointing from the target to the camera and
	 * is <code>normalize(pos-target)</code>
	 * 
	 * @param pos    The position of the camera in world space
	 * @param target What the camera is pointing at
	 * @return The look at matrix
	 */
	public static Matrix4f lookAt(Vector3f pos, Vector3f target) {
		return lookAt(pos, target, Vector3f.up());
	}

	/**
	 * Creates a look at matrix for a camera. Includes translation.
	 * <link rel="stylesheet" type="text/css" href="C:/dev/Java/Engine/Java
	 * Engine/src/javadoc.css"/>
	 * <table summary="Table of values">
	 * <tr>
	 * <td>Right<sub>x</sub></td>
	 * <td>Right<sub>y</sub></td>
	 * <td>Right<sub>z</sub></td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>Up<sub>x</sub></td>
	 * <td>Up<sub>y</sub></td>
	 * <td>Up<sub>z</sub></td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>Direction<sub>x</sub></td>
	 * <td>Direction<sub>y</sub></td>
	 * <td>Direction<sub>z</sub></td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>1</td>
	 * </tr>
	 * </table>
	 * 
	 * When<br>
	 * <code>Right</code> is a vector right of the target to the camera and is
	 * <code>normalize(cross(up, z))</code> <br>
	 * <code>Up</code> is a vector up from the target to the camera and is
	 * <code>normalize(cross(z, x))</code> <br>
	 * <code>Direction</code> is a vector pointing from the target to the camera and
	 * is <code>normalize(pos-target)</code>
	 * 
	 * @param pos    The position of the camera in world space
	 * @param target What the camera is pointing at
	 * @param up     The up vector
	 * @return The look at matrix
	 */
	public static Matrix4f lookAt(Vector3f pos, Vector3f target, Vector3f up) {
		Vector3f direction = pos.subCopy(target).normalize();
		Vector3f right = up.crossProductCopy(direction).normalize();
		Vector3f cameraUp = direction.crossProductCopy(right).normalize();

		Matrix4f look = new Matrix4f();

		look.v00 = right.x;
		look.v01 = right.y;
		look.v02 = right.z;
		look.v10 = cameraUp.x;
		look.v11 = cameraUp.y;
		look.v12 = cameraUp.z;
		look.v20 = direction.x;
		look.v21 = direction.y;
		look.v22 = direction.z;

		return look.applyTranslation(pos.negateCopy());
	}

	/**
	 * Version of {@link #lookAt(Vector3f, Vector3f)} that takes a Euler angles and
	 * returns the look at matrix for that (ignores roll)
	 * 
	 * @param pos   The position (for the translation aspect)
	 * @param angle The angle, in Euler ryp
	 * @return The look at matrix
	 */
	public static Matrix4f lookAtEuler(Vector3f pos, Vector3f angle) {
		return lookAt(pos, pos.addCopy(angle.toDirection()));
	}
	
	/**
	 * Version of {@link #lookAt(Vector3f, Vector3f)} that takes a Quaternion  and
	 * returns the look at matrix for that (ignores roll)
	 * 
	 * @param pos   The position (for the translation aspect)
	 * @param angle The quaternion
	 * @return The look at matrix
	 */
	public static Matrix4f lookAtQuaternion(Vector3f pos, Quaternion quaternion) {
		return lookAt(pos, pos.addCopy(quaternion.toDirection()));
	}

	/**
	 * Creates a standard orthographic projection matrix
	 * <link rel="stylesheet" type="text/css" href="C:/dev/Java/Engine/Java
	 * Engine/src/javadoc.css"/>
	 * <table summary="Table of values">
	 * <tr>
	 * <td>2/(r-l)</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>-(r+l)/(r-l)</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>2/(t-b)</td>
	 * <td>0</td>
	 * <td>-(t+b)/(t-b)</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>-2/(f-n)</td>
	 * <td>-(f+n)/(f-n)</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>1</td>
	 * </tr>
	 * </table>
	 * 
	 * @param left   The left bound
	 * @param right  The right bound
	 * @param bottom The bottom bound
	 * @param top    The top bound
	 * @param near   The near bound
	 * @param far    The far bound
	 * @return The orthographic projection matrix
	 */
	public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
		Matrix4f ret = new Matrix4f();

		ret.v00 = 2 / (right - left);
		ret.v11 = 2 / (top - bottom);
		ret.v22 = -2 / (far - near);

		ret.v03 = -((right + left) / (right - left));
		ret.v13 = -((top + bottom) / (top - bottom));
		ret.v23 = -((far + near) / (far - near));

		return ret;
	}

	/**
	 * Creates a perspective projection matrix
	 * <link rel="stylesheet" type="text/css" href="C:/dev/Java/Engine/Java
	 * Engine/src/javadoc.css"/>
	 * <table summary="Table of values">
	 * <tr>
	 * <td>1/(ar*tan(fov/2))</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>1/(tan(fov/2))</td>
	 * <td>0</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>(-nZ-fZ)/(nZ-fZ)</td>
	 * <td>(2*nZ*fZ)/(nZ-fZ)</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>-1</td>
	 * <td>0</td>
	 * </tr>
	 * </table>
	 * 
	 * @param fov         The field of view in degrees
	 * @param aspectRatio The aspect ratio (width/height)
	 * @param nearZ       The near z clipping distance
	 * @param farZ        The far z clipping distance
	 * @return The perspective projection matrix
	 */
	public static Matrix4f perspective(float fov, float aspectRatio, float nearZ, float farZ) {
		Matrix4f ret = new Matrix4f();
		float tanHalfFOV = (float) Math.tan(Math.toRadians(fov / 2));

		ret.v00 = 1 / (aspectRatio * tanHalfFOV);
		ret.v11 = 1 / (tanHalfFOV);
		ret.v22 = (-(nearZ + farZ)) / (farZ - nearZ);
		ret.v23 = (-2 * farZ * nearZ) / (farZ - nearZ);

		ret.v33 = 0;
		ret.v32 = -1;

		return ret;
	}

	/**
	 * Creates a new rotation matrix around an arbitrary axis. Source is <a href=
	 * "https://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToMatrix/index.htm">Euclidean
	 * Space</a>
	 * <link rel="stylesheet" type="text/css" href="E:/dev/Java/Engine/Java
	 * Engine/src/javadoc.css"/>
	 * <table summary="Table of values">
	 * <tr>
	 * <td>1-2s(c<sup>2</sup>+d<sup>2</sup>)</td>
	 * <td>2s(bc-da)</td>
	 * <td>2s(bd+ca)</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>2s(bc+da)</td>
	 * <td>1-2s(b<sup>2</sup>+d<sup>2</sup>)</td>
	 * <td>2s(cd-ba)</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>2s(bc-da)</td>
	 * <td>2s(cd+ba)</td>
	 * <td>1-2s(b<sup>2</sup>+c<sup>2</sup>)</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>1</td>
	 * </tr>
	 * </table>
	 * 
	 * @param Quaternion the quaternion representing the rotation.
	 * @return The rotation matrix
	 */
	public static Matrix4f rotation(Quaternion q) {
		return rotation(q, null);
	}

	/**
	 * Creates a new rotation matrix around an arbitrary axis. Source is <a href=
	 * "https://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToMatrix/index.htm">Euclidean
	 * Space</a>
	 * <link rel="stylesheet" type="text/css" href="E:/dev/Java/Engine/Java
	 * Engine/src/javadoc.css"/>
	 * <table summary="Table of values">
	 * <tr>
	 * <td>1-2s(c<sup>2</sup>+d<sup>2</sup>)</td>
	 * <td>2s(bc-da)</td>
	 * <td>2s(bd+ca)</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>2s(bc+da)</td>
	 * <td>1-2s(b<sup>2</sup>+d<sup>2</sup>)</td>
	 * <td>2s(cd-ba)</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>2s(bc-da)</td>
	 * <td>2s(cd+ba)</td>
	 * <td>1-2s(b<sup>2</sup>+c<sup>2</sup>)</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>1</td>
	 * </tr>
	 * </table>
	 * 
	 * @param Quaternion the quaternion representing the rotation.
	 * @param copy       A matrix that is used to avoid calls to new. Any value in
	 *                   that matrix will be overridden.
	 * @return The rotation matrix
	 */
	public static Matrix4f rotation(Quaternion q, Matrix4f copy) {
		copy = _alloc(copy);

		float s = 1 / (q.a * q.a + q.b * q.b + q.c * q.c + q.d * q.d);// Absolute value of the quaternion to the -2
		// power.

		copy.v00 = 1 - 2 * s * (q.c * q.c + q.d * q.d);
		copy.v01 = 2 * s * (q.b * q.c - q.d * q.a);
		copy.v02 = 2 * s * (q.b * q.d + q.c * q.a);

		copy.v10 = 2 * s * (q.b * q.c + q.d * q.a);
		copy.v11 = 1 - 2 * s * (q.b * q.b + q.d * q.d);
		copy.v12 = 2 * s * (q.c * q.d - q.b * q.a);

		copy.v20 = 2 * s * (q.b * q.d - q.c * q.a);
		copy.v21 = 2 * s * (q.c * q.d + q.b * q.a);
		copy.v22 = 1 - 2 * s * (q.b * q.b + q.c * q.c);

		return copy;
	}

	/**
	 * Creates a new rotation matrix around an arbitrary axis
	 * <link rel="stylesheet" type="text/css" href="E:/dev/Java/Engine/Java
	 * Engine/src/javadoc.css"/>
	 * <table summary="Table of values">
	 * <tr>
	 * <td>cos Θ + x<sup>2</sup>(1-cos Θ)</td>
	 * <td>xy(1-cos Θ) - z(sin Θ)</td>
	 * <td>xz(1-cos Θ) + y(sin Θ)</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>yx(1-cos Θ) + z(sin Θ)</td>
	 * <td>cos Θ + y<sup>2</sup>(1-cos Θ)</td>
	 * <td>yz(1-cos Θ) - x(sin Θ)</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>zx(1-cos Θ) - y(sin Θ)</td>
	 * <td>zy(1-cos Θ) + x(sin Θ)</td>
	 * <td>cos Θ + z<sup>2</sup>(1-cos Θ)</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>1</td>
	 * </tr>
	 * </table>
	 * 
	 * @param angle The angle in degrees
	 * @param x     The x-component of the arbitrary axis
	 * @param y     The y-component of the arbitrary axis
	 * @param z     The z-component of the arbitrary axis
	 * @return The rotation matrix
	 */
	public static Matrix4f rotation(float angle, float x, float y, float z) {
		return rotation(angle, x, y, z, null);
	}

	/**
	 * Creates a new rotation matrix around an arbitrary axis
	 * <link rel="stylesheet" type="text/css" href="E:/dev/Java/Engine/Java
	 * Engine/src/javadoc.css"/>
	 * <table summary="Table of values">
	 * <tr>
	 * <td>cos Θ + x<sup>2</sup>(1-cos Θ)</td>
	 * <td>xy(1-cos Θ) - z(sin Θ)</td>
	 * <td>xz(1-cos Θ) + y(sin Θ)</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>yx(1-cos Θ) + z(sin Θ)</td>
	 * <td>cos Θ + y<sup>2</sup>(1-cos Θ)</td>
	 * <td>yz(1-cos Θ) - x(sin Θ)</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>zx(1-cos Θ) - y(sin Θ)</td>
	 * <td>zy(1-cos Θ) + x(sin Θ)</td>
	 * <td>cos Θ + z<sup>2</sup>(1-cos Θ)</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>1</td>
	 * </tr>
	 * </table>
	 * 
	 * @param angle The angle in degrees
	 * @param x     The x-component of the arbitrary axis
	 * @param y     The y-component of the arbitrary axis
	 * @param z     The z-component of the arbitrary axis
	 * @param copy  A matrix that is used to avoid calls to new. Any value in that
	 *              matrix will be overridden.
	 * @return The rotation matrix
	 */
	public static Matrix4f rotation(float angle, float x, float y, float z, Matrix4f copy) {
		double theta = Math.toRadians(angle);
		float sinTheta = (float) Math.sin(theta);
		float cosTheta = (float) Math.cos(theta);
		copy = _alloc(copy);

		copy.v00 = cosTheta + x * x * (1 - cosTheta);
		copy.v01 = x * y * (1 - cosTheta) - z * sinTheta;
		copy.v02 = x * z * (1 - cosTheta) - y * sinTheta;

		copy.v10 = y * x * (1 - cosTheta) + z * sinTheta;
		copy.v11 = cosTheta + y * y * (1 - cosTheta);
		copy.v12 = y * z * (1 - cosTheta) - x * sinTheta;

		copy.v20 = z * x * (1 - cosTheta) - y * sinTheta;
		copy.v21 = z * y * (1 - cosTheta) + x * sinTheta;
		copy.v22 = cosTheta + z * z * (1 - cosTheta);

		return copy;
	}

	/**
	 * Creates a new rotation matrix around an arbitrary axis
	 * 
	 * @param angle The angle in degrees
	 * @param vec   A vector representing the arbitrary axis
	 * @return The rotation matrix
	 * @see Matrix4f#rotation(float, float, float, float)
	 */
	public static Matrix4f rotation(float angle, Vector3f vec) {
		return rotation(angle, vec.x, vec.y, vec.z);
	}

	/**
	 * Creates a new rotation matrix around an arbitrary axis
	 * 
	 * @param angle The angle in degrees
	 * @param vec   A vector representing the arbitrary axis
	 * @param copy  A matrix that is used to avoid calls to new. Any value in that
	 *              matrix will be overridden.
	 * @return The rotation matrix
	 * @see Matrix4f#rotation(float, float, float, float)
	 */
	public static Matrix4f rotation(float angle, Vector3f vec, Matrix4f copy) {
		return rotation(angle, vec.x, vec.y, vec.z, copy);
	}

	/**
	 * Creates a new rotation matrix around the x axis
	 * <link rel="stylesheet" type="text/css" href="C:/dev/Java/Engine/Java
	 * Engine/src/javadoc.css"/>
	 * <table summary="Table of values">
	 * <tr>
	 * <td>1</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>cos Θ</td>
	 * <td>-sin Θ</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>sin Θ</td>
	 * <td>cos Θ</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>1</td>
	 * </tr>
	 * </table>
	 * 
	 * @param angle The angle in degrees
	 * @return The rotation matrix
	 */
	public static Matrix4f rotationX(float angle) {
		return rotationX(angle, null);
	}

	/**
	 * Creates a new rotation matrix around the x axis
	 * <link rel="stylesheet" type="text/css" href="C:/dev/Java/Engine/Java
	 * Engine/src/javadoc.css"/>
	 * <table summary="Table of values">
	 * <tr>
	 * <td>1</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>cos Θ</td>
	 * <td>-sin Θ</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>sin Θ</td>
	 * <td>cos Θ</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>1</td>
	 * </tr>
	 * </table>
	 * 
	 * @param angle The angle in degrees
	 * @param copy  A matrix that is used to avoid calls to new. Any value in that
	 *              matrix will be overridden.
	 * @return The rotation matrix
	 */
	public static Matrix4f rotationX(float angle, Matrix4f copy) {
		double theta = Math.toRadians(angle);
		float sinTheta = (float) Math.sin(theta);
		float cosTheta = (float) Math.cos(theta);
		copy = _alloc(copy);
		copy.v11 = cosTheta;
		copy.v12 = -sinTheta;
		copy.v21 = sinTheta;
		copy.v22 = cosTheta;
		return copy;
	}

	/**
	 * Creates a new rotation matrix around the y axis
	 * <link rel="stylesheet" type="text/css" href="C:/dev/Java/Engine/Java
	 * Engine/src/javadoc.css"/>
	 * <table summary="Table of values">
	 * <tr>
	 * <td>cos Θ</td>
	 * <td>0</td>
	 * <td>sin Θ</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>1</td>
	 * <td>0</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>-sin Θ</td>
	 * <td>0</td>
	 * <td>cos Θ</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>1</td>
	 * </tr>
	 * </table>
	 * 
	 * @param angle The angle in degrees
	 * @return The rotation matrix
	 */
	public static Matrix4f rotationY(float angle) {
		return rotationZ(angle, null);
	}

	/**
	 * Creates a new rotation matrix around the y axis
	 * <link rel="stylesheet" type="text/css" href="C:/dev/Java/Engine/Java
	 * Engine/src/javadoc.css"/>
	 * <table summary="Table of values">
	 * <tr>
	 * <td>cos Θ</td>
	 * <td>0</td>
	 * <td>sin Θ</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>1</td>
	 * <td>0</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>-sin Θ</td>
	 * <td>0</td>
	 * <td>cos Θ</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>1</td>
	 * </tr>
	 * </table>
	 * 
	 * @param angle The angle in degrees
	 * @param copy  A matrix that is used to avoid calls to new. Any value in that
	 *              matrix will be overridden.
	 * @return The rotation matrix
	 */
	public static Matrix4f rotationY(float angle, Matrix4f copy) {
		double theta = Math.toRadians(angle);
		float sinTheta = (float) Math.sin(theta);
		float cosTheta = (float) Math.cos(theta);
		copy = _alloc(copy);
		copy.v00 = cosTheta;
		copy.v02 = sinTheta;
		copy.v20 = -sinTheta;
		copy.v22 = cosTheta;
		return copy;
	}

	/**
	 * Creates a new rotation matrix around the z axis
	 * <link rel="stylesheet" type="text/css" href="C:/dev/Java/Engine/Java
	 * Engine/src/javadoc.css"/>
	 * <table summary="Table of values">
	 * <tr>
	 * <td>cos Θ</td>
	 * <td>-sin Θ</td>
	 * <td>0</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>sin Θ</td>
	 * <td>cos Θ</td>
	 * <td>0</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>1</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>1</td>
	 * </tr>
	 * </table>
	 * 
	 * @param angle The angle in degrees
	 * @return The rotation matrix
	 */
	public static Matrix4f rotationZ(float angle) {
		return rotationZ(angle, null);
	}

	/**
	 * Creates a new rotation matrix around the z axis
	 * <link rel="stylesheet" type="text/css" href="C:/dev/Java/Engine/Java
	 * Engine/src/javadoc.css"/>
	 * <table summary="Table of values">
	 * <tr>
	 * <td>cos Θ</td>
	 * <td>-sin Θ</td>
	 * <td>0</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>sin Θ</td>
	 * <td>cos Θ</td>
	 * <td>0</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>1</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>1</td>
	 * </tr>
	 * </table>
	 * 
	 * @param angle The angle in degrees
	 * @param copy  A matrix that is used to avoid calls to new. Any value in that
	 *              matrix will be overridden.
	 * @return The rotation matrix
	 */
	public static Matrix4f rotationZ(float angle, Matrix4f copy) {
		double theta = Math.toRadians(angle);
		float sinTheta = (float) Math.sin(theta);
		float cosTheta = (float) Math.cos(theta);
		copy = _alloc(copy);
		copy.v00 = cosTheta;
		copy.v01 = -sinTheta;
		copy.v10 = sinTheta;
		copy.v11 = cosTheta;
		return copy;
	}

//	/**
//	 * Creates a new scaling matrix
//	 * 
//	 * @param factor    The xyz factor to scale
//	 * @param container A container to put the results in.
//	 * @return The scaling matrix
//	 * @see Matrix4f#scale(float, float, float)
//	 */
//	public static Matrix4f scale(float factor, Matrix4f container) {
//		return scale(factor, factor, factor, container);
//	}

	/**
	 * Creates a new scaling matrix
	 * <link rel="stylesheet" type="text/css" href="C:/dev/Java/Engine/Java
	 * Engine/src/javadoc.css"/>
	 * <table summary="Table of values">
	 * <tr>
	 * <td>x</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>y</td>
	 * <td>0</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>z</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>1</td>
	 * </tr>
	 * </table>
	 * 
	 * @param x    The x factor to scale
	 * @param y    The y factor to scale
	 * @param z    The z factor to scale
	 * @param copy A matrix that is used to avoid calls to new. Any value in that
	 *             matrix will be overridden.
	 * @return The scaling matrix
	 */
	public static Matrix4f scale(float x, float y, float z, Matrix4f copy) {
		copy = _alloc(copy);

		copy.v00 = x;
		copy.v11 = y;
		copy.v22 = z;

		return copy;
	}

	/**
	 * Creates a new scaling matrix
	 * 
	 * @param vec A vector, it will the xyz factor to scale
	 * @return The scaling matrix
	 * @param copy A matrix that is used to avoid calls to new. Any value in that
	 *             matrix will be overridden.
	 * @see Matrix4f#scale(float, float, float)
	 */
	public static Matrix4f scale(Vector3f vec, Matrix4f copy) {
		return scale(vec.x, vec.y, vec.z, copy);
	}

	/**
	 * Creates a new scaling matrix
	 * 
	 * @param factor The xyz factor to scale
	 * @return The scaling matrix
	 * @see Matrix4f#scale(float, float, float)
	 */
	public static Matrix4f scale(float factor) {
		return scale(factor, factor, factor);
	}

	/**
	 * Creates a new scaling matrix
	 * <link rel="stylesheet" type="text/css" href="C:/dev/Java/Engine/Java
	 * Engine/src/javadoc.css"/>
	 * <table summary="Table of values">
	 * <tr>
	 * <td>x</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>y</td>
	 * <td>0</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>z</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>1</td>
	 * </tr>
	 * </table>
	 * 
	 * @param x The x factor to scale
	 * @param y The y factor to scale
	 * @param z The z factor to scale
	 * @return The scaling matrix
	 */
	public static Matrix4f scale(float x, float y, float z) {
		return scale(x, y, z, null);
	}

	/**
	 * Creates a new scaling matrix
	 * 
	 * @param vec A vector, it will the xyz factor to scale
	 * @return The scaling matrix
	 * @see Matrix4f#scale(float, float, float)
	 */
	public static Matrix4f scale(Vector3f vec) {
		return scale(vec.x, vec.y, vec.z);
	}

	/**
	 * Creates a new translation matrix
	 * <link rel="stylesheet" type="text/css" href="C:/dev/Java/Engine/Java
	 * Engine/src/javadoc.css"/>
	 * <table summary="Table of values">
	 * <tr>
	 * <td>1</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>x</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>1</td>
	 * <td>0</td>
	 * <td>y</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>1</td>
	 * <td>z</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>1</td>
	 * </tr>
	 * </table>
	 * 
	 * @param x    The x distance to translate
	 * @param y    The y distance to translate
	 * @param z    The z distance to translate
	 * @param copy A matrix that is used to avoid calls to new. Any value in that
	 *             matrix will be overridden.
	 * @return The translation matrix
	 */
	public static Matrix4f translation(float x, float y, float z, Matrix4f copy) {
		copy = _alloc(copy);

		copy.v03 = x;
		copy.v13 = y;
		copy.v23 = z;

		return copy;
	}

	/**
	 * Creates a new translation matrix
	 * 
	 * @param vec A vector, it will the xyz component to translate
	 * @return The translation matrix
	 * @param copy A matrix that is used to avoid calls to new. Any value in that
	 *             matrix will be overridden.
	 * @see Matrix4f#translation(float, float, float)
	 */
	public static Matrix4f translation(Vector3f vec, Matrix4f copy) {
		return translation(vec.x, vec.y, vec.z, copy);
	}

	/**
	 * Creates a new translation matrix
	 * <link rel="stylesheet" type="text/css" href="C:/dev/Java/Engine/Java
	 * Engine/src/javadoc.css"/>
	 * <table summary="Table of values">
	 * <tr>
	 * <td>1</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>x</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>1</td>
	 * <td>0</td>
	 * <td>y</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>1</td>
	 * <td>z</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>1</td>
	 * </tr>
	 * </table>
	 * 
	 * @param x The x distance to translate
	 * @param y The y distance to translate
	 * @param z The z distance to translate
	 * @return The translation matrix
	 */
	public static Matrix4f translation(float x, float y, float z) {
		return translation(x, y, z, null);
	}

	/**
	 * Creates a new translation matrix
	 * 
	 * @param vec A vector, it will the xyz component to translate
	 * @return The translation matrix
	 * @see Matrix4f#translation(float, float, float)
	 */
	public static Matrix4f translation(Vector3f vec) {
		return translation(vec.x, vec.y, vec.z);
	}

	private static String _f(float f) {
		return (f + S10).substring(0, 10) + S1;
	}

	/**
	 * Creates a look at matrix and applies it (the matrix this is called on is
	 * affected)
	 * 
	 * @param pos    The position of the camera in world space
	 * @param target What the camera is pointing at
	 * @return The look at matrix
	 * @see Matrix4f#lookAt(Vector3f, Vector3f)
	 */
	public Matrix4f applyLookAt(Vector3f pos, Vector3f target) {
		Matrix4f lookAt = lookAt(pos, target);
		return mul(lookAt);
	}

	/**
	 * Creates a look at matrix and applies it Euler version (the matrix this is
	 * called on is affected)
	 * 
	 * @param pos   The position (for the translation aspect)
	 * @param angle The angle, in Euler ryp
	 * @return The look at matrix
	 * @see Matrix4f#lookAtEuler(Vector3f, Vector3f)
	 */
	public Matrix4f applyLookAtEuler(Vector3f pos, Vector3f angle) {
		Matrix4f lookAt = lookAtEuler(pos, angle);
		return mul(lookAt);
	}
	
	/**
	 * Creates a look at matrix and applies it Quaternion version (the matrix this is
	 * called on is affected)
	 * 
	 * @param pos   The position (for the translation aspect)
	 * @param quaternion The quaternion
	 * @return The look at matrix
	 * @see Matrix4f#lookAtEuler(Vector3f, Vector3f)
	 */
	public Matrix4f applyLookAtQuaternion(Vector3f pos, Quaternion quaternion) {
		Matrix4f lookAt = lookAtQuaternion(pos, quaternion);
		return mul(lookAt);
	}

	/**
	 * Creates a rotation matrix for the quaternion and applies it (the matrix this
	 * is called on is affected)
	 * 
	 * @param q The quaternion.
	 * @return The matrix with the rotation applied.
	 * @see #rotation(Quaternion)
	 */
	public Matrix4f applyRotation(Quaternion q) {
		return applyRotation(q, null, null);
	}

	/**
	 * Creates a rotation matrix for the quaternion and applies it (the matrix this
	 * is called on is affected)
	 * 
	 * @param q       The quaternion.
	 * @param mulHelp A matrix that is used to avoid calls to new. Any value in that
	 *                matrix will be overridden.
	 * @param rot     A matrix that is used to avoid calls to new. Any value in that
	 *                matrix will be overridden.
	 * @return The matrix with the rotation applied.
	 * @see #rotation(Quaternion)
	 */
	public Matrix4f applyRotation(Quaternion q, Matrix4f rot, Matrix4f mulHelp) {
		Matrix4f rotation = rotation(q, rot);
		return mul(rotation, mulHelp);
	}

	/**
	 * Creates a rotation matrix around an arbitrary axis and applies it (the matrix
	 * this is called on is affected)
	 * 
	 * @param angle The angle in degrees
	 * @param x     The x component of the arbitrary axis
	 * @param y     The y component of the arbitrary axis
	 * @param z     The z component of the arbitrary axis
	 * @return The matrix with the rotation applied
	 * @see Matrix4f#rotation(float, float, float, float)
	 */
	public Matrix4f applyRotation(float angle, float x, float y, float z) {
		return applyRotation(angle, x, y, z, null, null);
	}

	/**
	 * Creates a rotation matrix around an arbitrary axis and applies it (the matrix
	 * this is called on is affected)
	 * 
	 * @param angle The angle in degrees
	 * @param vec   A vector representing the arbitrary axis
	 * @return The matrix with the rotation applied
	 * @see Matrix4f#rotation(float, float, float, float)
	 */
	public Matrix4f applyRotation(float angle, Vector3f vec) {
		return applyRotation(angle, vec.x, vec.y, vec.z, null, null);
	}

	/**
	 * Creates a rotation matrix around an arbitrary axis and applies it (the matrix
	 * this is called on is affected)
	 * 
	 * @param angle   The angle in degrees
	 * @param x       The x component of the arbitrary axis
	 * @param y       The y component of the arbitrary axis
	 * @param z       The z component of the arbitrary axis
	 * @param mulHelp A matrix that is used to avoid calls to new. Any value in that
	 *                matrix will be overridden.
	 * @param rot     A matrix that is used to avoid calls to new. Any value in that
	 *                matrix will be overridden.
	 * @return The matrix with the rotation applied
	 * @see Matrix4f#rotation(float, float, float, float)
	 */
	public Matrix4f applyRotation(float angle, float x, float y, float z, Matrix4f rot, Matrix4f mulHelp) {
		Matrix4f rotation = rotation(angle, x, y, z, rot);
		return mul(rotation, mulHelp);
	}

	/**
	 * Creates a rotation matrix around an arbitrary axis and applies it (the matrix
	 * this is called on is affected)
	 * 
	 * @param angle   The angle in degrees
	 * @param vec     A vector representing the arbitrary axis
	 * @param mulHelp A matrix that is used to avoid calls to new. Any value in that
	 *                matrix will be overridden.
	 * @param rot     A matrix that is used to avoid calls to new. Any value in that
	 *                matrix will be overridden.
	 * @return The matrix with the rotation applied
	 * @see Matrix4f#rotation(float, float, float, float)
	 */
	public Matrix4f applyRotation(float angle, Vector3f vec, Matrix4f rot, Matrix4f mulHelp) {
		return applyRotation(angle, vec.x, vec.y, vec.z, rot, mulHelp);
	}

	/**
	 * Creates a rotation matrix around the x axis and applies it (the matrix this
	 * is called on is affected)
	 * 
	 * @param angle The angle in degrees
	 * @return The matrix with the rotation applied
	 * @see Matrix4f#rotationX(float)
	 */
	public Matrix4f applyRotationX(float angle) {
		return applyRotationX(angle, null, null);
	}
	
	/**
	 * Creates a rotation matrix around the x axis and applies it (the matrix this
	 * is called on is affected)
	 * 
	 * @param angle The angle in degrees
	 * @param mulHelp A matrix that is used to avoid calls to new. Any value in that
	 *             matrix will be overridden.
	 * @param rot A matrix that is used to avoid calls to new. Any value in that
	 *             matrix will be overridden.
	 * @return The matrix with the rotation applied
	 * @see Matrix4f#rotationX(float)
	 */
	public Matrix4f applyRotationX(float angle, Matrix4f rot, Matrix4f mulHelp) {
		Matrix4f rotation = rotationX(angle, rot);
		return mul(rotation, mulHelp);
	}

	/**
	 * Creates a rotation matrix around the y axis and applies it (the matrix this
	 * is called on is affected)
	 * 
	 * @param angle The angle in degrees
	 * @return The matrix with the rotation applied
	 * @see Matrix4f#rotationY(float)
	 */
	public Matrix4f applyRotationY(float angle) {
		return applyRotationY(angle, null, null);
	}
	
	/**
	 * Creates a rotation matrix around the y axis and applies it (the matrix this
	 * is called on is affected)
	 * 
	 * @param angle The angle in degrees
	 * @param mulHelp A matrix that is used to avoid calls to new. Any value in that
	 *             matrix will be overridden.
	 * @param rot A matrix that is used to avoid calls to new. Any value in that
	 *             matrix will be overridden.
	 * @return The matrix with the rotation applied
	 * @see Matrix4f#rotationY(float)
	 */
	public Matrix4f applyRotationY(float angle, Matrix4f rot, Matrix4f mulHelp) {
		Matrix4f rotation = rotationY(angle, rot);
		return mul(rotation, mulHelp);
	}

	/**
	 * Creates a rotation matrix around the z axis and applies it (the matrix this
	 * is called on is affected)
	 * 
	 * @param angle The angle in degrees
	 * @return The matrix with the rotation applied
	 * @see Matrix4f#rotationZ(float)
	 */
	public Matrix4f applyRotationZ(float angle) {
		return applyRotationZ(angle, null, null);
	}
	
	/**
	 * Creates a rotation matrix around the z axis and applies it (the matrix this
	 * is called on is affected)
	 * 
	 * @param angle The angle in degrees
	 * @param mulHelp A matrix that is used to avoid calls to new. Any value in that
	 *             matrix will be overridden.
	 * @param rot A matrix that is used to avoid calls to new. Any value in that
	 *             matrix will be overridden.
	 * @return The matrix with the rotation applied
	 * @see Matrix4f#rotationZ(float)
	 */
	public Matrix4f applyRotationZ(float angle, Matrix4f rot, Matrix4f mulHelp) {
		Matrix4f rotation = rotationZ(angle, rot);
		return mul(rotation, mulHelp);
	}

	/**
	 * Creates a scaling matrix, and applies it (the matrix this is called on is
	 * affected)
	 * 
	 * @param factor The xyz factor to scale
	 * @return The matrix with the scaling applied
	 * @see Matrix4f#scale(float, float, float)
	 */
	public Matrix4f applyScale(float factor) {
		return applyScale(factor, factor, factor);
	}

	/**
	 * Creates a scaling matrix, and applies it (the matrix this is called on is
	 * affected)
	 * 
	 * @param factor  The xyz factor to scale
	 * @param scale   A matrix that is used to avoid calls to new. Any value in that
	 *                matrix will be overridden.
	 * @param mulHelp A matrix that is used to avoid calls to new. Any value in that
	 *                matrix will be overridden.
	 * @return The matrix with the scaling applied
	 * @see Matrix4f#scale(float, float, float)
	 */
	public Matrix4f applyScale(float factor, Matrix4f scale, Matrix4f mulHelp) {
		return applyScale(factor, factor, factor, scale, mulHelp);
	}

	/**
	 * Creates a scaling matrix, and applies it (the matrix this is called on is
	 * affected)
	 * 
	 * @param x The x factor to scale
	 * @param y The y factor to scale
	 * @param z The z factor to scale
	 * @return The matrix with the scaling applied
	 * @see Matrix4f#scale(float, float, float)
	 */
	public Matrix4f applyScale(float x, float y, float z) {
		return applyScale(x, y, z, null, null);
	}

	/**
	 * Creates a scaling matrix, and applies it (the matrix this is called on is
	 * affected)
	 * 
	 * @param x       The x factor to scale
	 * @param y       The y factor to scale
	 * @param z       The z factor to scale
	 * @param scale   A matrix that is used to avoid calls to new. Any value in that
	 *                matrix will be overridden.
	 * @param mulHelp A matrix that is used to avoid calls to new. Any value in that
	 *                matrix will be overridden.
	 * @return The matrix with the scaling applied
	 * @see Matrix4f#scale(float, float, float)
	 */
	public Matrix4f applyScale(float x, float y, float z, Matrix4f scale, Matrix4f mulHelp) {
		return mul(scale(x, y, z, scale), mulHelp);
	}

	/**
	 * Creates a scaling matrix, and applies it (the matrix this is called on is
	 * affected)
	 * 
	 * @param vec The xyz factor to scale
	 * @return The matrix with the scaling applied
	 * @see Matrix4f#scale(float, float, float)
	 */
	public Matrix4f applyScale(Vector3f vec) {
		return applyScale(vec.x, vec.y, vec.z);
	}

	/**
	 * Creates a scaling matrix, and applies it (the matrix this is called on is
	 * affected)
	 * 
	 * @param vec     The xyz factor to scale
	 * @param scale   A matrix that is used to avoid calls to new. Any value in that
	 *                matrix will be overridden.
	 * @param mulHelp A matrix that is used to avoid calls to new. Any value in that
	 *                matrix will be overridden.
	 * @return The matrix with the scaling applied
	 * @see Matrix4f#scale(float, float, float)
	 */
	public Matrix4f applyScale(Vector3f vec, Matrix4f scale, Matrix4f mulHelp) {
		return applyScale(vec.x, vec.y, vec.z, scale, mulHelp);
	}

	/**
	 * Creates a translation matrix, and applies it (the matrix this is called on is
	 * affected)
	 * 
	 * @param x The x distance to translate
	 * @param y The y distance to translate
	 * @param z The z distance to translate
	 * @return The matrix with the translation applied
	 * @see Matrix4f#translation(float, float, float)
	 */
	public Matrix4f applyTranslation(float x, float y, float z) {
		return applyTranslation(x, y, z, null, null);
	}

	/**
	 * Creates a translation matrix, and applies it (the matrix this is called on is
	 * affected)
	 * 
	 * @param x           The x distance to translate
	 * @param y           The y distance to translate
	 * @param z           The z distance to translate
	 * @param translation A matrix that is used to avoid calls to new. Any value in
	 *                    that matrix will be overridden.
	 * @param mulHelp     A matrix that is used to avoid calls to new. Any value in
	 *                    that matrix will be overridden.
	 * @return The matrix with the translation applied
	 * @see Matrix4f#translation(float, float, float)
	 */
	public Matrix4f applyTranslation(float x, float y, float z, Matrix4f translation, Matrix4f mulHelp) {
		return mul(translation(x, y, z, translation), mulHelp);
	}

	/**
	 * Creates a translation matrix, and applies it (the matrix this is called on is
	 * affected)
	 * 
	 * @param vec A vector, it will the xyz component to translate
	 * @return The matrix with the translation applied
	 * @see Matrix4f#translation(float, float, float)
	 */
	public Matrix4f applyTranslation(Vector3f vec) {
		return applyTranslation(vec.x, vec.y, vec.z);
	}

	/**
	 * Creates a translation matrix, and applies it (the matrix this is called on is
	 * affected)
	 * 
	 * @param vec         A vector, it will the xyz component to translate
	 * @param translation A matrix that is used to avoid calls to new. Any value in
	 *                    that matrix will be overridden.
	 * @param mulHelp     A matrix that is used to avoid calls to new. Any value in
	 *                    that matrix will be overridden.
	 * @return The matrix with the translation applied
	 * @see Matrix4f#translation(float, float, float)
	 */
	public Matrix4f applyTranslation(Vector3f vec, Matrix4f translation, Matrix4f mulHelp) {
		return applyTranslation(vec.x, vec.y, vec.z, translation, mulHelp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Matrix4f other = (Matrix4f) obj;
		if (Float.floatToIntBits(v00) != Float.floatToIntBits(other.v00))
			return false;
		if (Float.floatToIntBits(v01) != Float.floatToIntBits(other.v01))
			return false;
		if (Float.floatToIntBits(v02) != Float.floatToIntBits(other.v02))
			return false;
		if (Float.floatToIntBits(v03) != Float.floatToIntBits(other.v03))
			return false;
		if (Float.floatToIntBits(v10) != Float.floatToIntBits(other.v10))
			return false;
		if (Float.floatToIntBits(v11) != Float.floatToIntBits(other.v11))
			return false;
		if (Float.floatToIntBits(v12) != Float.floatToIntBits(other.v12))
			return false;
		if (Float.floatToIntBits(v13) != Float.floatToIntBits(other.v13))
			return false;
		if (Float.floatToIntBits(v20) != Float.floatToIntBits(other.v20))
			return false;
		if (Float.floatToIntBits(v21) != Float.floatToIntBits(other.v21))
			return false;
		if (Float.floatToIntBits(v22) != Float.floatToIntBits(other.v22))
			return false;
		if (Float.floatToIntBits(v23) != Float.floatToIntBits(other.v23))
			return false;
		if (Float.floatToIntBits(v30) != Float.floatToIntBits(other.v30))
			return false;
		if (Float.floatToIntBits(v31) != Float.floatToIntBits(other.v31))
			return false;
		if (Float.floatToIntBits(v32) != Float.floatToIntBits(other.v32))
			return false;
		if (Float.floatToIntBits(v33) != Float.floatToIntBits(other.v33))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(v00);
		result = prime * result + Float.floatToIntBits(v01);
		result = prime * result + Float.floatToIntBits(v02);
		result = prime * result + Float.floatToIntBits(v03);
		result = prime * result + Float.floatToIntBits(v10);
		result = prime * result + Float.floatToIntBits(v11);
		result = prime * result + Float.floatToIntBits(v12);
		result = prime * result + Float.floatToIntBits(v13);
		result = prime * result + Float.floatToIntBits(v20);
		result = prime * result + Float.floatToIntBits(v21);
		result = prime * result + Float.floatToIntBits(v22);
		result = prime * result + Float.floatToIntBits(v23);
		result = prime * result + Float.floatToIntBits(v30);
		result = prime * result + Float.floatToIntBits(v31);
		result = prime * result + Float.floatToIntBits(v32);
		result = prime * result + Float.floatToIntBits(v33);
		return result;
	}

	/**
	 * Inverts the matrix (the matrix this is called on is affected) Code generated
	 * by <a href=
	 * "https://github.com/willnode/N-Matrix-Programmer">N-Matrix-Programmer</a>,
	 * thanks<br>
	 * Code adopted by csbru
	 * 
	 * @return The inverted matrix
	 */
	public Matrix4f inverse() {
		float A2323 = v22 * v33 - v23 * v32;
		float A1323 = v21 * v33 - v23 * v31;
		float A1223 = v21 * v32 - v22 * v31;
		float A0323 = v20 * v33 - v23 * v30;
		float A0223 = v20 * v32 - v22 * v30;
		float A0123 = v20 * v31 - v21 * v30;
		float A2313 = v12 * v33 - v13 * v32;
		float A1313 = v11 * v33 - v13 * v31;
		float A1213 = v11 * v32 - v12 * v31;
		float A2312 = v12 * v23 - v13 * v22;
		float A1312 = v11 * v23 - v13 * v21;
		float A1212 = v11 * v22 - v12 * v21;
		float A0313 = v10 * v33 - v13 * v30;
		float A0213 = v10 * v32 - v12 * v30;
		float A0312 = v10 * v23 - v13 * v20;
		float A0212 = v10 * v22 - v12 * v20;
		float A0113 = v10 * v31 - v11 * v30;
		float A0112 = v10 * v21 - v11 * v20;

		float det = v00 * (v11 * A2323 - v12 * A1323 + v13 * A1223) - v01 * (v10 * A2323 - v12 * A0323 + v13 * A0223)
				+ v02 * (v10 * A1323 - v11 * A0323 + v13 * A0123) - v03 * (v10 * A1223 - v11 * A0223 + v12 * A0123);
		det = 1 / det;

		Matrix4f copy = copyOf(this);

		v00 = det * (copy.v11 * A2323 - copy.v12 * A1323 + copy.v13 * A1223);
		v01 = det * -(copy.v01 * A2323 - copy.v02 * A1323 + copy.v03 * A1223);
		v02 = det * (copy.v01 * A2313 - copy.v02 * A1313 + copy.v03 * A1213);
		v03 = det * -(copy.v01 * A2312 - copy.v02 * A1312 + copy.v03 * A1212);
		v10 = det * -(copy.v10 * A2323 - copy.v12 * A0323 + copy.v13 * A0223);
		v11 = det * (copy.v00 * A2323 - copy.v02 * A0323 + copy.v03 * A0223);
		v12 = det * -(copy.v00 * A2313 - copy.v02 * A0313 + copy.v03 * A0213);
		v13 = det * (copy.v00 * A2312 - copy.v02 * A0312 + copy.v03 * A0212);
		v20 = det * (copy.v10 * A1323 - copy.v11 * A0323 + copy.v13 * A0123);
		v21 = det * -(copy.v00 * A1323 - copy.v01 * A0323 + copy.v03 * A0123);
		v22 = det * (copy.v00 * A1313 - copy.v01 * A0313 + copy.v03 * A0113);
		v23 = det * -(copy.v00 * A1312 - copy.v01 * A0312 + copy.v03 * A0112);
		v30 = det * -(copy.v10 * A1223 - copy.v11 * A0223 + copy.v12 * A0123);
		v31 = det * (copy.v00 * A1223 - copy.v01 * A0223 + copy.v02 * A0123);
		v32 = det * -(copy.v00 * A1213 - copy.v01 * A0213 + copy.v02 * A0113);
		v33 = det * (copy.v00 * A1212 - copy.v01 * A0212 + copy.v02 * A0112);

		return this;
	}

	/**
	 * Inverts the matrix (the matrix this is called on is not affected)
	 * 
	 * @return The inverted matrix
	 * @see Matrix4f#inverse()
	 */
	public Matrix4f inverseCopy() {
		return copyOf(this).inverse();
	}

	/**
	 * Performs scalar multiplication on the matrix (the matrix this is called on is
	 * affected)
	 * 
	 * @param scalar The scalar to multiply by
	 * @return The result
	 */
	public Matrix4f mul(float scalar) {
		this.v00 *= scalar;
		this.v01 *= scalar;
		this.v02 *= scalar;
		this.v03 *= scalar;

		this.v10 *= scalar;
		this.v11 *= scalar;
		this.v12 *= scalar;
		this.v13 *= scalar;

		this.v20 *= scalar;
		this.v21 *= scalar;
		this.v22 *= scalar;
		this.v23 *= scalar;

		this.v30 *= scalar;
		this.v31 *= scalar;
		this.v32 *= scalar;
		this.v33 *= scalar;

		return this;
	}

	/**
	 * Performs matrix multiplication this matrix with the passed matrix (the matrix
	 * this is called on is affected)
	 * 
	 * @param mat The matrix to multiply with
	 * @return The multiplied matrix
	 */
	public Matrix4f mul(Matrix4f mat) {
		return mul(mat, null);
	}

	/**
	 * Performs matrix multiplication this matrix with the passed matrix (the matrix
	 * this is called on is affected)
	 * 
	 * @param mat     The matrix to multiply with
	 * @param mulHelp A matrix that is used to avoid calls to new. Any value in that
	 *                matrix will be overridden.
	 * @return The multiplied matrix
	 */
	public Matrix4f mul(Matrix4f mat, Matrix4f mulHelp) {
		mulHelp = _alloc(mulHelp, this);

		this.v00 = (mulHelp.v00 * mat.v00) + (mulHelp.v01 * mat.v10) + (mulHelp.v02 * mat.v20)
				+ (mulHelp.v03 * mat.v30);
		this.v10 = (mulHelp.v10 * mat.v00) + (mulHelp.v11 * mat.v10) + (mulHelp.v12 * mat.v20)
				+ (mulHelp.v13 * mat.v30);
		this.v20 = (mulHelp.v20 * mat.v00) + (mulHelp.v21 * mat.v10) + (mulHelp.v22 * mat.v20)
				+ (mulHelp.v23 * mat.v30);
		this.v30 = (mulHelp.v30 * mat.v00) + (mulHelp.v31 * mat.v10) + (mulHelp.v32 * mat.v20)
				+ (mulHelp.v33 * mat.v30);

		this.v01 = (mulHelp.v00 * mat.v01) + (mulHelp.v01 * mat.v11) + (mulHelp.v02 * mat.v21)
				+ (mulHelp.v03 * mat.v31);
		this.v11 = (mulHelp.v10 * mat.v01) + (mulHelp.v11 * mat.v11) + (mulHelp.v12 * mat.v21)
				+ (mulHelp.v13 * mat.v31);
		this.v21 = (mulHelp.v20 * mat.v01) + (mulHelp.v21 * mat.v11) + (mulHelp.v22 * mat.v21)
				+ (mulHelp.v23 * mat.v31);
		this.v31 = (mulHelp.v30 * mat.v01) + (mulHelp.v31 * mat.v11) + (mulHelp.v32 * mat.v21)
				+ (mulHelp.v33 * mat.v31);

		this.v02 = (mulHelp.v00 * mat.v02) + (mulHelp.v01 * mat.v12) + (mulHelp.v02 * mat.v22)
				+ (mulHelp.v03 * mat.v32);
		this.v12 = (mulHelp.v10 * mat.v02) + (mulHelp.v11 * mat.v12) + (mulHelp.v12 * mat.v22)
				+ (mulHelp.v13 * mat.v32);
		this.v22 = (mulHelp.v20 * mat.v02) + (mulHelp.v21 * mat.v12) + (mulHelp.v22 * mat.v22)
				+ (mulHelp.v23 * mat.v32);
		this.v32 = (mulHelp.v30 * mat.v02) + (mulHelp.v31 * mat.v12) + (mulHelp.v32 * mat.v22)
				+ (mulHelp.v33 * mat.v32);

		this.v03 = (mulHelp.v00 * mat.v03) + (mulHelp.v01 * mat.v13) + (mulHelp.v02 * mat.v23)
				+ (mulHelp.v03 * mat.v33);
		this.v13 = (mulHelp.v10 * mat.v03) + (mulHelp.v11 * mat.v13) + (mulHelp.v12 * mat.v23)
				+ (mulHelp.v13 * mat.v33);
		this.v23 = (mulHelp.v20 * mat.v03) + (mulHelp.v21 * mat.v13) + (mulHelp.v22 * mat.v23)
				+ (mulHelp.v23 * mat.v33);
		this.v33 = (mulHelp.v30 * mat.v03) + (mulHelp.v31 * mat.v13) + (mulHelp.v32 * mat.v23)
				+ (mulHelp.v33 * mat.v33);

		return this;
	}

	/**
	 * Performs scalar multiplication on the matrix (the matrix this is called on is
	 * not affected)
	 * 
	 * @param scalar The scalar to multiply by
	 * @return The result
	 */
	public Matrix4f mulCopy(float scalar) {
		return mulCopy(scalar, null);
	}

	/**
	 * Performs scalar multiplication on the matrix (the matrix this is called on is
	 * not affected)
	 * 
	 * @param scalar The scalar to multiply by
	 * @param copy   A matrix that is used to avoid calls to new. Any value in that
	 *               matrix will be overridden.
	 * @return The result
	 */
	public Matrix4f mulCopy(float scalar, Matrix4f copy) {
		return _alloc(copy, this).mul(scalar);
	}

	/**
	 * Performs matrix multiplication this matrix with the passed matrix (the matrix
	 * this is called on is not affected)
	 * 
	 * @param mat The matrix to multiply with
	 * @return The multiplied matrix
	 */
	public Matrix4f mulCopy(Matrix4f mat) {
		return mulCopy(mat, null, null);
	}

	/**
	 * Performs matrix multiplication this matrix with the passed matrix (the matrix
	 * this is called on is not affected)
	 * 
	 * @param mat     The matrix to multiply with
	 * @param mulHelp A matrix that is used to avoid calls to new. Any value in that
	 *                matrix will be overridden.
	 * @param copy    A matrix that is used to avoid calls to new. Any value in that
	 *                matrix will be overridden.
	 * @return The multiplied matrix
	 */
	public Matrix4f mulCopy(Matrix4f mat, Matrix4f mulHelp, Matrix4f copy) {
		return _alloc(copy, this).mul(mat, mulHelp);
	}

	/**
	 * Gives the matrix as an array organized into column major order
	 * 
	 * @return The matrix as an array
	 */
	public float[] toFloatArray() {
		return toFloatArrayCMO();
	}

	/**
	 * Gives the matrix as an array organized into column major order
	 * 
	 * @return The matrix as an array
	 */
	public float[] toFloatArrayCMO() {
		float[] ret = new float[16];

		ret[0] = v00;
		ret[1] = v10;
		ret[2] = v20;
		ret[3] = v30;

		ret[4] = v01;
		ret[5] = v11;
		ret[6] = v21;
		ret[7] = v31;

		ret[8] = v02;
		ret[9] = v12;
		ret[10] = v22;
		ret[11] = v32;

		ret[12] = v03;
		ret[13] = v13;
		ret[14] = v23;
		ret[15] = v33;

		return ret;
	}

	/**
	 * Gives the matrix as an array organized into row major order
	 * 
	 * @return The matrix as an array
	 */
	public float[] toFloatArrayRMO() {
		float[] ret = new float[16];

		ret[0] = v00;
		ret[1] = v01;
		ret[2] = v02;
		ret[3] = v03;

		ret[4] = v10;
		ret[5] = v11;
		ret[6] = v12;
		ret[7] = v13;

		ret[8] = v20;
		ret[9] = v21;
		ret[10] = v22;
		ret[11] = v23;

		ret[12] = v30;
		ret[13] = v31;
		ret[14] = v32;
		ret[15] = v33;

		return ret;
	}

	@Override
	public String toString() {
		return super.toString() + "\n[" + _f(v00) + _f(v01) + _f(v02) + _f(v03) + "]\n[" + _f(v10) + _f(v11) + _f(v12)
				+ _f(v13) + "]\n[" + _f(v20) + _f(v21) + _f(v22) + _f(v23) + "]\n[" + _f(v30) + _f(v31) + _f(v32)
				+ _f(v33) + "]";
	}

	/**
	 * Multiplies the this matrix by the passed vector (treating is as a 4x1 matrix)
	 * (the passed vector is affected)
	 * 
	 * @param vec The vector to multiply by
	 * @return The result
	 */
	public Vector4f mul(Vector4f vec) {
		float v0 = vec.dotProduct(row0());
		float v1 = vec.dotProduct(row1());
		float v2 = vec.dotProduct(row2());
		float v3 = vec.dotProduct(row3());
		vec.x = v0;
		vec.y = v1;
		vec.z = v2;
		vec.w = v3;
		return vec;
	}

	/**
	 * Multiplies the this matrix by the passed vector (treating is as a 4x1 matrix)
	 * (the passed vector is affected)
	 * 
	 * @param vec The vector to multiply by
	 * @return The result
	 */
	public Vector4f mulCopy(Vector4f vec) {
		return mul(Vector4f.copyOf(vec));
	}

	/**
	 * Returns the transposed matrix i.e. the columns are replaced by the rows and
	 * vice versa (the matrix this is called on is affected)
	 * 
	 * @return The transposed matrix
	 */
	public Matrix4f transpose() {
		Matrix4f copy = copyOf(this);
		v01 = copy.v10;
		v02 = copy.v20;
		v03 = copy.v30;

		v10 = copy.v01;
		v12 = copy.v21;
		v13 = copy.v31;

		v20 = copy.v02;
		v21 = copy.v12;
		v23 = copy.v32;

		v30 = copy.v03;
		v31 = copy.v13;
		v32 = copy.v23;

		return this;
	}

	/**
	 * Returns the transposed matrix i.e. the columns are replaced by the rows and
	 * vice versa (the matrix this is called on is not affected)
	 * 
	 * @return The transposed matrix
	 */
	public Matrix4f transposeCopy() {
		return copyOf(this).transpose();
	}

	// May be incorrect nomenclature, but its private so it doesn't really matter.
	@SuppressWarnings("unused")
	private static Matrix4f _alloc(Matrix4f copy, float v00, float v10, float v20, float v30, float v01, float v11,
			float v21, float v31, float v02, float v12, float v22, float v32, float v03, float v13, float v23,
			float v33) {
		if (copy != null) {
			return copy.transcribe(v00, v10, v20, v30, v01, v11, v21, v31, v02, v12, v22, v32, v03, v13, v23, v33);
		} else {
			return new Matrix4f(v00, v10, v20, v30, v01, v11, v21, v31, v02, v12, v22, v32, v03, v13, v23, v33);
		}
	}

	private static Matrix4f _alloc(Matrix4f copy, Matrix4f source) {
		if (copy != null) {
			return copy.transcribe(source);
		} else {
			return copyOf(source);
		}
	}

	private static Matrix4f _alloc(Matrix4f copy) {
		return _alloc(copy, StaticMath.MAT4F_IDENTITY);
	}

	private static final String S1 = " "; // 1 space

	private static final String S10 = "          ";// 10 spaces

	/**
	 * Creates and returns a copy of this matrix
	 * 
	 * @return The copy
	 */
	public Matrix4f copy() {
		return copyOf(this);
	}

	/*
	 * ROW/COLUMN ACESS
	 */

	public Vector4f row0() {
		return new Vector4f(v00, v01, v02, v03);
	}

	public Vector4f row1() {
		return new Vector4f(v10, v11, v12, v13);
	}

	public Vector4f row2() {
		return new Vector4f(v20, v21, v22, v23);
	}

	public Vector4f row3() {
		return new Vector4f(v30, v31, v32, v33);
	}

	public Vector4f column0() {
		return new Vector4f(v00, v10, v20, v30);
	}

	public Vector4f column1() {
		return new Vector4f(v01, v11, v21, v31);
	}

	public Vector4f column2() {
		return new Vector4f(v02, v12, v22, v32);
	}

	public Vector4f column3() {
		return new Vector4f(v03, v13, v23, v33);
	}

	public void row0(Vector4f vec) {
		v00 = vec.x;
		v01 = vec.y;
		v02 = vec.z;
		v03 = vec.w;
	}

	public void row1(Vector4f vec) {
		v10 = vec.x;
		v11 = vec.y;
		v12 = vec.z;
		v13 = vec.w;
	}

	public void row2(Vector4f vec) {
		v20 = vec.x;
		v21 = vec.y;
		v22 = vec.z;
		v23 = vec.w;
	}

	public void row3(Vector4f vec) {
		v30 = vec.x;
		v31 = vec.y;
		v32 = vec.z;
		v33 = vec.w;
	}

	public void column0(Vector4f vec) {
		v00 = vec.x;
		v10 = vec.y;
		v20 = vec.z;
		v30 = vec.w;
	}

	public void column1(Vector4f vec) {
		v01 = vec.x;
		v11 = vec.y;
		v21 = vec.z;
		v31 = vec.w;
	}

	public void column2(Vector4f vec) {
		v02 = vec.x;
		v12 = vec.y;
		v22 = vec.z;
		v32 = vec.w;
	}

	public void column3(Vector4f vec) {
		v03 = vec.x;
		v13 = vec.y;
		v23 = vec.z;
		v33 = vec.w;
	}
}
