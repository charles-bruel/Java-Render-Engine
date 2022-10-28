package net.chazzvader.core.generic.engine.render.material;

import net.chazzvader.core.generic.Configuration;
import net.chazzvader.core.generic.Configuration.Renderer;
import net.chazzvader.core.generic.engine.creator.ShaderCreator;
import net.chazzvader.core.generic.engine.object.EngineObject;
import net.chazzvader.core.generic.math.Matrix4f;
import net.chazzvader.core.generic.math.Vector3f;
import net.chazzvader.core.generic.math.Vector4f;
import net.chazzvader.core.opengl.engine.render.OpenGLTexture;
import net.chazzvader.core.opengl.engine.util.OpenGLStateMachine;
import net.chazzvader.core.opengl.shader.OpenGLShaderLocations;

/**
 * A material for UI rendering. Used in almost every case.
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
public class MaterialUI extends Material {

	/**
	 * An offset of where to put the background texture.
	 */
	public static final int BACKGROUND_TEXTURE_OFFSET = 1;

	private Shader shader;

	/**
	 * The main texture of the element.
	 */
	public Texture texture = Texture.BLANK;

	/**
	 * The background texture of the element. Used if the main texture is
	 * transparent at that pixel.
	 */
	public Texture background = Texture.TRANSPARENT;

	/**
	 * A color multiplier, defaults to (1, 1, 1) which means everything is the same
	 * color as the texture.
	 */
	public Vector3f color_multiplier = new Vector3f(1, 1, 1);

	/**
	 * A color multiplier, for the background texture, defaults to (1, 1, 1) which
	 * means everything is the same color as the texture.
	 */
	public Vector3f background_color_multiplier = new Vector3f(1, 1, 1);

	/**
	 * The absolute color of the border if there is one.
	 */
	public Vector3f border_color = new Vector3f(0, 0, 0);

	/**
	 * The size of the border, in % of the element size.
	 */
	public float border_size = 0;

	/**
	 * The width of the rendered element compared to the height.
	 */
	public float aspectRatio = 1;

	/**
	 * The world matrix, transforms to proper coordinates.
	 */
	public Matrix4f world_matrix = new Matrix4f();

	/**
	 * Texture coordinates to use, for example selecting from a texture atlas for a
	 * bitmap font. If you change it, you should change it back after the operation.
	 */
	public Vector4f overrideTexCoords = new Vector4f(0, 0, 1, 1);

	/**
	 * Creates and binds basic details.
	 */
	public MaterialUI() {
		super(true);

		Configuration.assertRendererFinalized();
		this.shader = ShaderCreator.uiShader();
		shader.bind();
		shader.setUniform1i("tex", OpenGLShaderLocations.TEXTURE_DIFFUSE);
		shader.setUniform1i("bg", OpenGLShaderLocations.TEXTURE_DIFFUSE + BACKGROUND_TEXTURE_OFFSET);
		shader.unbind();
	}

	@Override
	protected void _bind(EngineObject object) {
		shader.bind();
		shader.setUniform3f("color_multiplier", color_multiplier);
		shader.setUniform3f("background_color_multiplier", background_color_multiplier);
		shader.setUniform3f("border_color", border_color);
		shader.setUniform1f("border_size", border_size);
		shader.setUniform1f("aspect_ratio", aspectRatio);
		shader.setUniformMat4f("world_matrix", world_matrix);
		shader.setUniform4f("override_tex_coords", overrideTexCoords);
		Renderer renderer = Configuration.getRendererVerifyFinalized();
		switch (renderer) {
		case GENERIC:
			break;
		case OPEN_GL:
			OpenGLStateMachine.setActiveTextureSlot(OpenGLShaderLocations.TEXTURE_DIFFUSE);
			((OpenGLTexture) texture).bind();
			OpenGLStateMachine.setActiveTextureSlot(OpenGLShaderLocations.TEXTURE_DIFFUSE + BACKGROUND_TEXTURE_OFFSET);
			((OpenGLTexture) background).bind();
			break;
		}
	}

	@Override
	public Shader getShader() {
		return shader;
	}

}
