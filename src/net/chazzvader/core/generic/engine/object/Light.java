package net.chazzvader.core.generic.engine.object;

import java.awt.Color;

import net.chazzvader.core.generic.engine.creator.ObjectCreator;
import net.chazzvader.core.generic.engine.render.material.MaterialLight;
import net.chazzvader.core.generic.math.Matrix4f;
import net.chazzvader.core.generic.math.Quaternion;
import net.chazzvader.core.generic.math.Vector3f;
import net.chazzvader.core.generic.util.Utils;

/**
 * A class for a basic point light.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class Light extends EngineObject {

	/**
	 * The color of the light, defaults to 1, 1, 1 (white)
	 */
	public Vector3f lightColor = new Vector3f(1, 1, 1);

	/**
	 * The constant term for the equation detailing the power of the light over
	 * distance.<br>
	 * The equation is<br>
	 * <code>1 / (constant + linear * distance + quadratic * distance^2)</code><br>
	 * when constant is defined by this variable and quadratic and linear as
	 * specified by other variables.<br>
	 * Constant is almost always 1.
	 * 
	 * @see #linear
	 * @see #quadratic
	 */
	public float constant;

	/**
	 * The linear term for the equation detailing the power of the light over
	 * distance.<br>
	 * The equation is<br>
	 * <code>1 / (constant + linear * distance + quadratic * distance^2)</code><br>
	 * when linear is defined by this variable and quadratic and constant as
	 * specified by other variables.
	 * 
	 * @see #constant
	 * @see #quadratic
	 */
	public float linear;

	/**
	 * The quadratic term for the equation detailing the power of the light over
	 * distance.<br>
	 * The equation is<br>
	 * <code>1 / (constant + linear * distance + quadratic * distance^2)</code><br>
	 * when quadratic is defined by this variable and linear and constant as
	 * specified by other variables.
	 * 
	 * @see #constant
	 * @see #linear
	 */
	public float quadratic;

	/**
	 * The target range of the light.
	 */
	public float targetDistance;

	/**
	 * Calculates values for constant, linear and quadratic given a target distance.
	 * Based of equations that roughly model the values in <a href=
	 * "http://wiki.ogre3d.org/tiki-index.php?page=-Point+Light+Attenuation"> this
	 * Ogre3d article.</a>
	 * 
	 * @param target The target distance
	 * @return A 3 component float vector with the values being constant, linear,
	 *         quadratic.
	 * @see #constant
	 * @see #linear
	 * @see #quadratic
	 */
	public static Vector3f calculateLightValues(float target) {
		return new Vector3f(1, 4.4f / target, 3 / (target - 5));
	}

	/**
	 * Creates a light at the origin with a given color and target range
	 * 
	 * @param lightColor     The color of the light
	 * @param targetDistance The target range or distance
	 */
	public Light(Vector3f lightColor, float targetDistance) {
		this(lightColor, targetDistance, new Vector3f());
	}

	/**
	 * Creates a light with a given position, color and target range. Rotation and
	 * scale is not given because they don't effect lights.
	 * 
	 * @param lightColor     The color of the light
	 * @param targetDistance The target range or distance
	 * @param pos            The position of the light
	 */
	public Light(Vector3f lightColor, float targetDistance, Vector3f pos) {
		super(pos, new Quaternion(), new Vector3f(1, 1, 1));
		this.lightColor = lightColor;
		Vector3f lightData = calculateLightValues(targetDistance);
		this.constant = lightData.x;
		this.linear = lightData.y;
		this.quadratic = lightData.z;
		this.targetDistance = targetDistance;
	}

	/**
	 * Creates a light at the origin with a given color and target range
	 * 
	 * @param lightColor     The color of the light
	 * @param targetDistance The target range or distance
	 */
	public Light(Color lightColor, float targetDistance) {
		this(lightColor, targetDistance, new Vector3f());
	}

	/**
	 * Creates a light with a given position, color and target range. Rotation and
	 * scale is not given because they dont effect lights.
	 * 
	 * @param lightColor     The color of the light
	 * @param targetDistance The target range or distance
	 * @param pos            The position of the light
	 */
	public Light(Color lightColor, float targetDistance, Vector3f pos) {
		this(Utils.vectorFromColor(lightColor), targetDistance, pos);
	}

	/**
	 * Creates a sphere with the color of the light at the position of the light, as
	 * a child. Makes the light visible.
	 * 
	 * @throws NullPointerException if the light isn't part of a scene already
	 */
	public void createPhysicalRepresentation() {
		scene.add(ObjectCreator.instaSphere(0.3f, 16, new MaterialLight(this), false).setParent(this));
	}

	private Vector3f _pos = null;
	private Quaternion _rot = null;
	private Matrix4f[] cachedMatrices;

	/**
	 * Returns an array of 6 view matrices to render shadow maps.
	 * 
	 * @return The 6 view matrices.
	 */
	public Matrix4f[] getViewMatrix() {
		if (pos().equals(_pos) && rot().equals(_rot)) {
			return cachedMatrices;
		} else {
			_rot = rot();
			_pos = pos();
			cachedMatrices = _getViewMatrix();
			return cachedMatrices;
		}
	}

	private Matrix4f[] _getViewMatrix() {
		Matrix4f[] ret = new Matrix4f[6];
		ret[0] = Matrix4f.lookAt(pos(), pos().addCopy(new Vector3f(1, 0, 0)), new Vector3f(0, -1, 0));
		ret[1] = Matrix4f.lookAt(pos(), pos().addCopy(new Vector3f(-1, 0, 0)), new Vector3f(0, -1, 0));
		ret[2] = Matrix4f.lookAt(pos(), pos().addCopy(new Vector3f(0, 1, 0)), new Vector3f(0, 0, 1));
		ret[3] = Matrix4f.lookAt(pos(), pos().addCopy(new Vector3f(0, -1, 0)), new Vector3f(0, 0, -1));
		ret[4] = Matrix4f.lookAt(pos(), pos().addCopy(new Vector3f(0, 0, 1)), new Vector3f(0, -1, 0));
		ret[5] = Matrix4f.lookAt(pos(), pos().addCopy(new Vector3f(0, 0, -1)), new Vector3f(0, -1, 0));
		return ret;
	}

	private float _targetDistance;

	private Matrix4f projMatrix = Matrix4f.perspective(90, 1, 0.1f, targetDistance * 2);

	/**
	 * The projection matrix to render shadow maps.
	 * 
	 * @return The projection matrix
	 */
	public Matrix4f getProjectionMatrix() {
		if (_targetDistance == targetDistance) {
			return projMatrix;
		} else {
			_targetDistance = targetDistance;
			return (projMatrix = Matrix4f.perspective(90, 1, 0.1f, targetDistance * 2));
		}
	}

}