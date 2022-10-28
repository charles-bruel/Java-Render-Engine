package net.chazzvader.core.opengl.shader;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import net.chazzvader.core.generic.Configuration;
import net.chazzvader.core.generic.Configuration.Renderer;
import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.Scene;
import net.chazzvader.core.generic.engine.object.Light;
import net.chazzvader.core.generic.engine.render.RenderPipeline;
import net.chazzvader.core.generic.engine.render.RenderPipeline.RenderingMode;
import net.chazzvader.core.generic.engine.render.material.Shader;
import net.chazzvader.core.generic.math.Matrix4f;
import net.chazzvader.core.generic.math.Vector2f;
import net.chazzvader.core.generic.math.Vector3f;
import net.chazzvader.core.generic.math.Vector4f;
import net.chazzvader.core.generic.util.Utils;
import net.chazzvader.core.opengl.engine.render.OpenGLRenderPipeline;
import net.chazzvader.core.opengl.engine.util.OpenGLStateMachine;
import net.chazzvader.core.opengl.engine.util.OpenGLUtils;

/**
 * OpenGL implementation of Shader
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 * @see Shader
 */
public class OpenGLShader extends Shader {

	@Override
	public void bind() {
		OpenGLStateMachine.bindShader(ID);
	}

	@Override
	public void unbind() {
		OpenGLStateMachine.bindShader(ID);
	}

	private final int ID;
	private Map<String, Integer> locationCache = new HashMap<String, Integer>();

	@SuppressWarnings("unused")
	private String vertSource, geoSource, fragSource;

	/**
	 * Creates a shader based on files
	 * 
	 * @param vertex   The path to the vertex shader file
	 * @param fragment The path to the fragment shader file
	 */
	public OpenGLShader(String vertex, String fragment) {
		super(true);
		Configuration.assertRenderer(Renderer.OPEN_GL);
		ID = OpenGLUtils.loadShader(vertex, fragment);
		vertSource = vertex;
		fragSource = fragment;
	}
	
	/**
	 * Creates a shader based on files
	 * 
	 * @param vertex   The path to the vertex shader file
	 * @param fragment The path to the fragment shader file
	 * @param add      If the shader should be added to the list of shaders to be
	 *                 pre rendered. <em>DO NOT TOUCH</em>. Defaults to true.
	 */
	public OpenGLShader(String vertex, String fragment, boolean add) {
		super(add);
		Configuration.assertRenderer(Renderer.OPEN_GL);
		ID = OpenGLUtils.loadShader(vertex, fragment);
		vertSource = vertex;
		fragSource = fragment;
	}

	/**
	 * Creates a shader based on files
	 * 
	 * @param vertex   The path to the vertex shader file
	 * @param geometry The path to the geometry shader file
	 * @param fragment The path to the fragment shader file
	 */
	public OpenGLShader(String vertex, String geometry, String fragment) {
		super(true);
		Configuration.assertRenderer(Renderer.OPEN_GL);
		ID = OpenGLUtils.loadShader(vertex, geometry, fragment);
		vertSource = vertex;
		geoSource = geometry;
		fragSource = fragment;
	}

	/**
	 * Creates a shader based on files
	 * 
	 * @param vertex   The path to the vertex shader file
	 * @param geometry The path to the geometry shader file
	 * @param fragment The path to the fragment shader file
	 * @param add      If the shader should be added to the list of shaders to be
	 *                 pre rendered. <em>DO NOT TOUCH</em>. Defaults to true.
	 */
	public OpenGLShader(String vertex, String geometry, String fragment, boolean add) {
		super(add);
		Configuration.assertRenderer(Renderer.OPEN_GL);
		ID = OpenGLUtils.loadShader(vertex, geometry, fragment);
		vertSource = vertex;
		geoSource = geometry;
		fragSource = fragment;
	}

	/**
	 * Returns the location of the uniform. This method performs caching
	 * 
	 * @param name The name of the uniform
	 * @return The location
	 */
	public int getUniform(String name) {
		if (locationCache.containsKey(name)) {
			return locationCache.get(name);
		}

		int result = GL20.glGetUniformLocation(ID, name);
		if (result == -1) {
			Logging.log("Could not find uniform " + name + " in shader", "OpenGL Shader", LoggingLevel.ERR);
		} else {
			locationCache.put(name, result);
		}
		return result;
	}

	@Override
	public void setUniform1i(String location, int val) {
		GL20.glUniform1i(getUniform(location), val);
	}

	@Override
	public void setUniform1f(String location, float val) {
		GL20.glUniform1f(getUniform(location), val);
	}

	@Override
	public void setUniform2f(String location, Vector2f val) {
		GL20.glUniform2f(getUniform(location), val.x, val.y);
	}

	@Override
	public void setUniform3f(String location, Vector3f val) {
		GL20.glUniform3f(getUniform(location), val.x, val.y, val.z);
	}

	@Override
	public void setUniform4f(String location, Vector4f val) {
		GL20.glUniform4f(getUniform(location), val.x, val.y, val.z, val.w);
	}

	@Override
	public void setUniformMat4f(String location, Matrix4f val) {
		GL20.glUniformMatrix4fv(getUniform(location), false, Utils.createFloatBuffer(val.toFloatArray()));
	}

	/**
	 * Whether this shader supports shadows.
	 */
	public boolean shadows = false;

	@Override
	public void preRender(Scene scene, RenderPipeline pipeline) {
		bind();
		RenderingMode mode = pipeline.getRenderingMode();
		OpenGLRenderPipeline opengGLpipeline = (OpenGLRenderPipeline) pipeline;
		switch (mode) {
		case REGULAR:
			setUniformMat4f("view_matrix", scene.getActiveCamera().getViewMatrix());
			setUniformMat4f("proj_matrix", scene.getActiveCamera().getProjectionMatrix());
			if (shadows) {
				for(int i = 0;i < OpenGLShaderLocations.MAX_SHADOW_MAPS;i ++) {
					int textureSlot = OpenGLShaderLocations.TEXTURE_SHADOW_1 + i;
					if(i < opengGLpipeline.getShadowFramebuffers().length) {
						OpenGLStateMachine.setActiveTextureSlot(textureSlot);
						OpenGLStateMachine.bindTexture(GL13.GL_TEXTURE_CUBE_MAP, opengGLpipeline.getShadowFramebuffers()[i].shadowTexture);
					}
					setUniform1i("depth_maps[" + i + "]", textureSlot);
				}
			}
			break;
		case SHADOWS:
			Light l = pipeline.getRenderingLight();
			setUniform3f("light_pos", pipeline.getRenderingLight().pos());
			setUniform1f("far_plane", pipeline.getRenderingLight().targetDistance * 2);
			for (int i = 0; i < 6; i++) {
				setUniformMat4f("view_matrices[" + i + "]", l.getViewMatrix()[i]);
			}
			setUniformMat4f("proj_matrix", l.getProjectionMatrix());
			break;
		}
	}
}
