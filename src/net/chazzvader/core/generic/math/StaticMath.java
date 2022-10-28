package net.chazzvader.core.generic.math;

/**
 * Has a variety of static Matrix and Vector values.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
@SuppressWarnings("javadoc")
public final class StaticMath {

	private StaticMath() {

	}

	public static final Matrix4f MAT4F_IDENTITY = new Matrix4f();

	public static final Vector3f UP = new Vector3f(0, 1, 0);
	public static final Vector3f DOWN = new Vector3f(0, -1, 0);

	public static final Vector3f POSITIVE_X = new Vector3f(1, 0, 0);
	public static final Vector3f POSITIVE_Y = new Vector3f(0, 1, 0);
	public static final Vector3f POSITIVE_Z = new Vector3f(0, 0, 1);
	public static final Vector3f NEGATIVE_X = new Vector3f(-1, 0, 0);
	public static final Vector3f NEGATIVE_Y = new Vector3f(0, -1, 0);
	public static final Vector3f NEGATIVE_Z = new Vector3f(0, 0, -1);

	public static final Vector3f YAW_AXIS = new Vector3f(0, 1, 0);
	public static final Vector3f HEADING_AXIS = new Vector3f(0, 1, 0);
	public static final Vector3f PITCH_AXIS = new Vector3f(0, 0, 1);
	public static final Vector3f ATTITUDE_AXIS = new Vector3f(0, 0, 1);
	public static final Vector3f BANK_AXIS = new Vector3f(1, 0, 0);
	public static final Vector3f ROLL_AXIS = new Vector3f(1, 0, 0);

}