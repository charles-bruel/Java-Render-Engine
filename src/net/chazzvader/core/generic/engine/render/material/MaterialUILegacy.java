package net.chazzvader.core.generic.engine.render.material;

import net.chazzvader.core.generic.Configuration;
import net.chazzvader.core.generic.Configuration.Renderer;
import net.chazzvader.core.generic.engine.creator.ShaderCreator;
import net.chazzvader.core.generic.engine.object.EngineObject;
import net.chazzvader.core.generic.engine.uilegacy.element.UIElement;
import net.chazzvader.core.generic.math.Matrix4f;
import net.chazzvader.core.generic.math.Vector3f;
import net.chazzvader.core.opengl.engine.render.OpenGLTexture;
import net.chazzvader.core.opengl.engine.util.OpenGLStateMachine;
import net.chazzvader.core.opengl.shader.OpenGLShaderLocations;

/**
 * Generic material for UI things.
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
@Deprecated
public class MaterialUILegacy extends Material {

	/**
	 * An offset of where to put the background texture.
	 */
	public static final int BACKGROUND_TEXTURE_OFFSET = 1;

	/**
	 * The adjustment matrix, if there is one, to move the object off the element
	 * boundaries. Used for text blinkers and similar.
	 */
	public Matrix4f adjustmentMatrix = null;

	private Shader shader;

	/**
	 * The main texture of the element.
	 */
	public Texture texture;

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

	private UIElement element;

	/**
	 * Constructor, creates a new material.
	 * 
	 * @param texture The primary texture of the element.
	 * @param element The element this is connected to. Used for positioning.
	 */
	public MaterialUILegacy(Texture texture, UIElement element) {
		super(true);
		this.texture = texture;
		this.element = element;
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
		float windowAspectRatio = (float) getWindowWidth() / (float) getWindowHeight();
		float elementAspectRatio = element.getAdjustedSize().x / element.getAdjustedSize().y;
		float aspectRatio = elementAspectRatio * windowAspectRatio;
		shader.setUniform1f("aspect_ratio", aspectRatio);
		if (adjustmentMatrix != null) {
			shader.setUniformMat4f("world_matrix", element.getWorldMatrix().mulCopy(adjustmentMatrix));
		} else {
			shader.setUniformMat4f("world_matrix", element.getWorldMatrix());
		}
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
