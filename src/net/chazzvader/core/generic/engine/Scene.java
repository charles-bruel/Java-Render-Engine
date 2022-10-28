package net.chazzvader.core.generic.engine;

import java.awt.Color;
import java.util.ArrayList;

import net.chazzvader.core.generic.engine.object.Camera;
import net.chazzvader.core.generic.engine.object.EngineObject;
import net.chazzvader.core.generic.engine.object.Light;
import net.chazzvader.core.generic.engine.render.RenderContext;
import net.chazzvader.core.generic.engine.render.RenderPipeline;
import net.chazzvader.core.generic.engine.render.material.Shader;
import net.chazzvader.core.generic.math.Vector3f;
import net.chazzvader.core.generic.util.Utils;

/**
 * A scene, contains all the rendered objects on screen and a camera
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class Scene extends EngineItem implements IDeletable, IRenderContextProvider {

	/**
	 * Creates a new scene. Literally does nothing.
	 */
	public Scene() {
	}

	boolean active = false;

	private Camera activeCamera;

	private Vector3f ambientLightColor = new Vector3f(1, 1, 1);
	private float ambientLightStrength = 0.1f;

	/**
	 * Returns the ambient color of the scene, a slight amount of lighting that is
	 * applied to everything. Think of how outside, a shadow still can block you're
	 * view to the only light source (the sun) but there is still light.
	 * 
	 * @return The ambient color of the scene.
	 */
	public Vector3f getAmbientLightColor() {
		return ambientLightColor;
	}

	/**
	 * Sets the ambient color of the scene, a slight amount of lighting that is
	 * applied to everything. Think of how outside, a shadow still can block you're
	 * view to the only light source (the sun) but there is still light.
	 * 
	 * @param ambientLightColor The ambient color of the scene.
	 */
	public void setAmbientLightColor(Vector3f ambientLightColor) {
		this.ambientLightColor = ambientLightColor;
	}
	
	/**
	 * Sets the ambient color of the scene, a slight amount of lighting that is
	 * applied to everything. Think of how outside, a shadow still can block you're
	 * view to the only light source (the sun) but there is still light.
	 * 
	 * @param ambientLightColor The ambient color of the scene.
	 */
	public void setAmbientLightColor(Color ambientLightColor) {
		setAmbientLightColor(Utils.vectorFromColor(ambientLightColor));
	}

	private Vector3f backgroundColor = new Vector3f();

	/**
	 * Returns the background color, the color that anything that isn't drawn takes.
	 * @return The background color.
	 */
	public Vector3f getBackgroundColor() {
		return backgroundColor;
	}
	
	/**
	 * Sets the background color, the color that anything that isn't drawn takes.
	 * @param backgroundColor The background color
	 */
	public void setBackgroundColor(Vector3f backgroundColor) {
		this.backgroundColor = backgroundColor;
		if (Application.getInstance().getActiveScene() == this) {
			Application.getInstance().getWindow().setBackgroundColor(backgroundColor);
		}
	}
	
	/**
	 * Sets the background color, the color that anything that isn't drawn takes.
	 * @param backgroundColor The background color
	 */
	public void setBackgroundColor(Color backgroundColor) {
		setBackgroundColor(Utils.vectorFromColor(backgroundColor));
	}

	/**
	 * Returns the ambient light strength, or how bright it is. 
	 * @return The ambient light strength.
	 */
	public float getAmbientLightStrength() {
		return ambientLightStrength;
	}

	/**
	 * Sets the ambient light strength, or how bright it is.  Set to 0 to disable ambient light. This is quite often small, as it acts a multiplier.
	 * @param ambientLightStrength The ambient light strength.
	 */
	public void setAmbientLightStrength(float ambientLightStrength) {
		this.ambientLightStrength = ambientLightStrength;
	}

	private ArrayList<Light> lights = new ArrayList<>();

	/**
	 * Returns an array list contained all of the lights in the scene, active or
	 * not.
	 * 
	 * @return An array list with the lights
	 */
	public ArrayList<Light> getLights() {
		return lights;
	}

	/**
	 * Sets the active camera. The active camera is what things are rendered from
	 * 
	 * @param camera The camera to set as active
	 */
	public void setActiveCamera(Camera camera) {
		camera.setActive(true);
		if (activeCamera != null)
			activeCamera.setActive(false);
		activeCamera = camera;
		if (!objects.contains(camera)) {
			objects.add(camera);
		}
	}

	/**
	 * Gets the active camera.
	 * 
	 * @return The active camera.
	 */
	public Camera getActiveCamera() {
		return activeCamera;
	}

	/**
	 * Is the scene active?
	 * 
	 * @return Is the scene active?
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * All the objects in the scene.
	 * 
	 * @return An array list with all the objects in it.
	 */
	public ArrayList<EngineObject> getObjects() {
		return objects;
	}

	private ArrayList<EngineObject> objects = new ArrayList<>();

	/**
	 * Renders the scene, and all the objects in it.
	 * @param pipeline The current pipeline. Used for basic context.
	 */
	public void render(RenderPipeline pipeline) {
		_render(pipeline);
	}

	private void _render(RenderPipeline pipeline) {
		if (!pipeline.get3dContext().isDepthOnly()) {
			Shader.preRenderAll(this, pipeline);
		}
		for (int i = 0; i < objects.size(); i++) {
			EngineObject object = objects.get(i);
			if (object.isActive())
				object.getRenderContext().bind();
				object.render();
		}
	}

	/**
	 * Updates the scene, and all the objects in it
	 * 
	 * @param delta The time, in seconds, since the last update
	 */
	public void update(double delta) {
		for (int i = 0; i < objects.size(); i++) {
			if (objects.get(i).isActive())
				objects.get(i).update(delta);
		}
	}

	/**
	 * Adds a new object to the scene
	 * 
	 * @param engineObject The object to add
	 */
	public void add(EngineObject engineObject) {
		if (!objects.contains(engineObject)) {
			objects.add(engineObject);
			engineObject.onAdd(this);
			engineObject.scene = this;
			if (engineObject instanceof Light) {
				lights.add((Light) engineObject);
			}
		}
	}

	@Override
	public RenderContext getRenderContext() {
		return getWindow().getRenderPipeline().get3dContext();
	}
	
	@Override
	public void delete() {
		// TODO Implement
		super.delete();
	}

}
