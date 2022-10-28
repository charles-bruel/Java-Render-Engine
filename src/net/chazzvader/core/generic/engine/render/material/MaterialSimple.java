package net.chazzvader.core.generic.engine.render.material;

import net.chazzvader.core.generic.Configuration;
import net.chazzvader.core.generic.Configuration.Renderer;
import net.chazzvader.core.generic.engine.creator.ShaderCreator;
import net.chazzvader.core.generic.engine.object.EngineObject;
import net.chazzvader.core.generic.math.Vector3f;
import net.chazzvader.core.opengl.engine.render.OpenGLTexture;
import net.chazzvader.core.opengl.engine.util.OpenGLStateMachine;
import net.chazzvader.core.opengl.shader.OpenGLShader;
import net.chazzvader.core.opengl.shader.OpenGLShaderLocations;

/**
 * A simple material, a texture with a tint.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class MaterialSimple extends Material {

	private Shader shader;

	/**
	 * The texture to render.
	 */
	public Texture texture;
	/**
	 * The tint color
	 */
	public Vector3f color = new Vector3f(1, 1, 1);

	/**
	 * Constructor, takes texture and color.
	 * 
	 * @param texture The texture.
	 * @param color   The tint color.
	 */
	public MaterialSimple(Texture texture, Vector3f color) {
		this();
		this.texture = texture;
		this.color = color;
	}

	/**
	 * Constructor, just takes texture, color is initialized to (1, 1, 1) meaning
	 * the texture appears normal.
	 * 
	 * @param texture The texture.
	 */
	public MaterialSimple(Texture texture) {
		this();
		this.texture = texture;
	}

	private MaterialSimple() {
		super(true);
		Configuration.assertRendererFinalized();
		this.shader = ShaderCreator.simpleShader();
		Renderer renderer = Configuration.getRendererVerifyFinalized();
		switch (renderer) {
		case GENERIC:
			break;
		case OPEN_GL:
			((OpenGLShader) shader).bind();
			((OpenGLShader) shader).setUniform1i("tex", OpenGLShaderLocations.TEXTURE_DIFFUSE);
			((OpenGLShader) shader).unbind();
			break;
		}

	}

	@Override
	protected void _bind(EngineObject object) {
		shader.bind();
		shader.setUniform3f("color_multiplier", color);
		shader.setUniformMat4f("world_matrix", object.getWorldMatrix());
		Renderer renderer = Configuration.getRendererVerifyFinalized();
		switch (renderer) {
		case GENERIC:
			break;
		case OPEN_GL:
			OpenGLStateMachine.setActiveTextureSlot(OpenGLShaderLocations.TEXTURE_DIFFUSE);
			((OpenGLTexture) texture).bind();
			break;
		}
	}

	@Override
	public Shader getShader() {
		return shader;
	}

}
