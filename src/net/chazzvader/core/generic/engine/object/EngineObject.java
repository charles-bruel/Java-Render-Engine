package net.chazzvader.core.generic.engine.object;

import java.util.ArrayList;

import net.chazzvader.core.generic.engine.EngineItemRenderable;
import net.chazzvader.core.generic.engine.Scene;
import net.chazzvader.core.generic.math.Matrix4f;
import net.chazzvader.core.generic.math.Quaternion;
import net.chazzvader.core.generic.math.Vector3f;
import net.chazzvader.core.generic.math.Vector4f;

/**
 * A object that exists in the scene, very generic, does nothing by itself.<br>
 * Object was the logical name, but thats kind of important in java and we don't
 * want to name a class Object. Thats just asking for trouble.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class EngineObject extends EngineItemRenderable {

	/**
	 * The scene this shader is a part of. <em>Only use as reference.</em>
	 */
	public Scene scene;

	/**
	 * Creates a new, generic, empty object at the given position, rotation and
	 * scale.
	 * 
	 * @param pos   The position of the object
	 * @param rot   The rotation of the object
	 * @param scale The scale of the object
	 */
	public EngineObject(Vector3f pos, Quaternion rot, Vector3f scale) {
		this.relativePos = pos;
		this.relativeRot = rot;
		this.relativeScale = scale;
	}

	/**
	 * Called when the object is added to a scene. 
	 * 
	 * @param scene The scene the object is being added to.
	 */
	public void onAdd(Scene scene) {
		provider = scene;
	}

	/**
	 * Creates a new, generic, empty object at at 0, 0, 0 with no rotation and
	 * regular scale.
	 */
	public EngineObject() {
		this(new Vector3f(0, 0, 0), new Quaternion(), new Vector3f(1, 1, 1));
	}

	/**
	 * The parent of the object. Only use as reference, modify through
	 * setParent(EngineObject)
	 * 
	 * @see EngineObject#setParent(EngineObject)
	 */
	public EngineObject parent = null;

	/**
	 * The children of the object. Only use as reference, modify through
	 * setParent(EngineObject)
	 * 
	 * @see EngineObject#setParent(EngineObject)
	 */
	public ArrayList<EngineObject> children = new ArrayList<>();

	/**
	 * If the object is active, defaults to true. This can mean many things, for
	 * example, an inactive mesh object isn't rendered.
	 */
	public boolean active = true;

	/**
	 * Sets a new parent for an object and also handles updating the children of the
	 * new parent and the old parent if applicable.
	 * 
	 * @param newParent The new parent
	 * 
	 * @return This object, for stringing calls together
	 */
	public EngineObject setParent(EngineObject newParent) {
		if (parent != null) {
			parent.children.remove(this);
		}
		if (newParent != null)
			newParent.children.add(this);
		parent = newParent;
		return this;
	}

	/**
	 * Is the object active. Kind of self explanatory. You are writing java code, I
	 * don't think I need to explain this to you, but I did for completeness sake.
	 * 
	 * @return Is the object active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets if the object is active
	 * 
	 * @param active If the object should be active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Renders the object, designed to be overloaded.<br>
	 * No call to <code>super.render()</code> is needed.
	 */
	public void render() {
		
	}

	private ArrayList<IScript> scripts = new ArrayList<>();

	/**
	 * Updates the object. Designed to be overloaded.<br>
	 * A call to <code>super.update(delta)</code> <em>is</em> needed, because by
	 * default, this method runs all attached scripts
	 * 
	 * @param delta The time, in seconds, since the last update.
	 * @see IScript
	 */
	public void update(double delta) {
//		fixRot();
		for (int i = 0; i < scripts.size(); i++) {
			scripts.get(i).run(this, delta);
		}
	}

//	protected void fixRot() {
//		while (relativeRot.x < 0)
//			relativeRot.x += 360;
//		while (relativeRot.y < 0)
//			relativeRot.y += 360;
//		while (relativeRot.z < 0)
//			relativeRot.z += 360;
//		while (relativeRot.x > 360)
//			relativeRot.x -= 360;
//		while (relativeRot.y > 360)
//			relativeRot.y -= 360;
//		while (relativeRot.z > 360)
//			relativeRot.z -= 360;
//	}

	/**
	 * Adds a new script to the object
	 * 
	 * @param script The script to be added
	 */
	public void addScript(IScript script) {
		scripts.add(script);
	}

	/**
	 * <em>THIS WILL BE INACURRATE IF THERE IS A PARENT</em><br>
	 * The position of the object in world space relative to the parent, or the
	 * origin if there is no parent. If there is a parent, use pos() instead.
	 * 
	 * @see EngineObject#pos()
	 */
	public Vector3f relativePos = new Vector3f();

	/**
	 * <em>THIS WILL BE INACURRATE IF THERE IS A PARENT</em><br>
	 * The relative rotation of the object to it's parent, typically in xyz, but
	 * sometimes (e.g. Camera) in ypr.
	 * 
	 * @see EngineObject#rot()
	 */
	public Quaternion relativeRot = new Quaternion();

	/**
	 * <em>THIS WILL BE INACURRATE IF THERE IS A PARENT</em><br>
	 * The relative scale of the object.
	 */
	public Vector3f relativeScale = new Vector3f(1, 1, 1);

	private Vector3f _pos = null;
	private Quaternion _rot = null;
	private Vector3f _scale = null;

	private Matrix4f cachedMatrix = null;

	private Vector3f cachedPos = null;
	private Vector3f _relPos = null;
	private Matrix4f _ptm = null;

	/**
	 * Gets the actual position of the object, as opposed to the position relative
	 * to the parent.
	 * 
	 * @return The actual position of the object in world space.
	 */
	public Vector3f pos() {
		if (parent != null) {
			if (relativePos.equals(_relPos) && parent.getWorldMatrix().equals(_ptm)) {
				return cachedPos;
			} else {
				cachedPos = _pos();
				_relPos = relativePos;
				_ptm = parent.getWorldMatrix();
				return cachedPos;
			}
		}
		return relativePos;
	}

	private Vector3f _pos() {
		if (parent != null) {
			Vector4f hc = new Vector4f(relativePos);
			hc = parent.getWorldMatrix().mul(hc);
			return new Vector3f(hc.x, hc.y, hc.z);
		} else {
			return relativePos;
		}
	}

	/**
	 * Gets the actual rotation of the object, as opposed to the rotation relative
	 * to the parent.
	 * 
	 * @return The actual rotation of the object.
	 */
	public Quaternion rot() {
		if (parent != null) {
			return parent.rot().mulCopy(relativeRot);
		} else {
			return relativeRot;
		}
	}

	/**
	 * Gets the absolute scale of the object, as opposed to the scale relative to
	 * the parent.
	 * 
	 * @return The actual scale of the object.
	 */
	public Vector3f scale() {
		if (parent != null) {
			return relativeScale.mulCopy(parent.scale());
		} else {
			return relativeScale;
		}
	}

	/**
	 * If necessary creates a new world matrix and returns it. If not, it returns a
	 * cached matrix. Whatever, the point is it returns a accurate world matrix that
	 * will apply the transformations and doesn't create a new one every time so you
	 * can call this pretty much as often as you want.
	 * 
	 * @return The world (sometimes called model) matrix
	 */
	public Matrix4f getWorldMatrix() {
		if (pos().equals(_pos) && rot().equals(_rot) && scale().equals(_scale)) {
			return cachedMatrix;
		} else {
			_pos = Vector3f.copyOf(pos());
			_rot = Quaternion.copyOf(rot());
			_scale = Vector3f.copyOf(scale());
			cachedMatrix = _getWorldMatrix();
			return cachedMatrix;
		}
	}

	private Matrix4f normalCache = null;
	private Matrix4f normalCacheMatrix;

	/**
	 * If necessary creates a new normal matrix and returns it. If not, it returns a
	 * cached matrix. Whatever, the point is it returns a accurate normal matrix
	 * that will apply the transformations and doesn't create a new one every time
	 * so you can call this pretty much as often as you want.
	 * 
	 * @return The normal matrix
	 */
	public Matrix4f getNormalMatrix() {
		if (getWorldMatrix().equals(normalCache)) {
			return normalCacheMatrix;
		} else {
			normalCache = getWorldMatrix();
			normalCacheMatrix = _getNormalMatrix();
			return normalCacheMatrix;
		}
	}

	private Matrix4f _getWorldMatrix() {
		return new Matrix4f().applyTranslation(_pos).applyRotation(_rot).applyScale(_scale);
	}

	private Matrix4f _getNormalMatrix() {
		return getWorldMatrix().inverseCopy().transpose();
	}

}
