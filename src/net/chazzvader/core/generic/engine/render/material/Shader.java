package net.chazzvader.core.generic.engine.render.material;

import java.util.ArrayList;

import net.chazzvader.core.generic.engine.EngineItem;
import net.chazzvader.core.generic.engine.Scene;
import net.chazzvader.core.generic.engine.render.RenderPipeline;
import net.chazzvader.core.generic.math.Matrix4f;
import net.chazzvader.core.generic.math.Vector2f;
import net.chazzvader.core.generic.math.Vector3f;
import net.chazzvader.core.generic.math.Vector4f;

/**
 * A class representing a shader, essentially a way to render things.<br>
 * Abstract because implementation depends on engine.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public abstract class Shader extends EngineItem {

	/**
	 * Creates a new shader object.
	 * 
	 * @param add If the shader should be added to the list of shaders to be pre
	 *            rendered. <em>DO NOT TOUCH</em>.
	 */
	public Shader(boolean add) {
		if (add)
			shaders.add(this);
	}

	/**
	 * Binds the shader, until another shader is bound this will render everything.
	 */
	public abstract void bind();

	/**
	 * Binds the a null shader.
	 */
	public abstract void unbind();

	/**
	 * Sets all the universal values, like lights and camera, and such.
	 * 
	 * @param scene    The scene.
	 * @param pipeline The rendering pipeline.
	 */
	public abstract void preRender(Scene scene, RenderPipeline pipeline);

	private static ArrayList<Shader> shaders = new ArrayList<>();

	/**
	 * Sets all the universal values, like lights and camera, and such for all shaders which are flagged. (see that odd <code>add</code> parameter).
	 * 
	 * @param scene    The scene.
	 * @param pipeline The rendering pipeline.
	 * @see #preRender(Scene, RenderPipeline)
	 * @see #Shader(boolean)
	 */
	public static void preRenderAll(Scene scene, RenderPipeline pipeline) {
		for (int i = 0; i < shaders.size(); i++) {
			shaders.get(i).preRender(scene, pipeline);
		}
	}

	/**
	 * Sets a integer uniform
	 * 
	 * @param location The uniform name
	 * @param val      The value to set it to
	 */
	public abstract void setUniform1i(String location, int val);

	/**
	 * Sets a floating point uniform
	 * 
	 * @param location The uniform name
	 * @param val      The value to set it to
	 */
	public abstract void setUniform1f(String location, float val);

	/**
	 * Sets a 2 component vector uniform
	 * 
	 * @param location The uniform name
	 * @param val      The value to set it to
	 */
	public abstract void setUniform2f(String location, Vector2f val);

	/**
	 * Sets a 3 component vector uniform
	 * 
	 * @param location The uniform name
	 * @param val      The value to set it to
	 */
	public abstract void setUniform3f(String location, Vector3f val);

	/**
	 * Sets a 4 component vector uniform
	 * 
	 * @param location The uniform name
	 * @param val      The value to set it to
	 */
	public abstract void setUniform4f(String location, Vector4f val);

	/**
	 * Sets a Matrix4f uniform
	 * 
	 * @param location The uniform name
	 * @param val      The value to set it to
	 * @see Matrix4f
	 */
	public abstract void setUniformMat4f(String location, Matrix4f val);
}