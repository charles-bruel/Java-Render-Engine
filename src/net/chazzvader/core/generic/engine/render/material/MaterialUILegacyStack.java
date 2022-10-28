package net.chazzvader.core.generic.engine.render.material;

import net.chazzvader.core.generic.Configuration;
import net.chazzvader.core.generic.Configuration.Renderer;
import net.chazzvader.core.generic.engine.creator.ShaderCreator;
import net.chazzvader.core.generic.engine.object.EngineObject;
import net.chazzvader.core.generic.engine.object.ui.EngineObjectUIStack;
import net.chazzvader.core.generic.engine.uilegacy.element.UIElementStack;
import net.chazzvader.core.opengl.engine.util.OpenGLStateMachine;
import net.chazzvader.core.opengl.shader.OpenGLShaderLocations;

/**
 * A material to render the special framebuffer used for the special stack
 * element.
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
@Deprecated
public class MaterialUILegacyStack extends Material {

	private UIElementStack element;
	private Shader shader;

	/**
	 * Makes a new material based on an element.
	 * 
	 * @param element The element.
	 */
	public MaterialUILegacyStack(UIElementStack element) {
		super(true);
		this.element = element;
		this.shader = ShaderCreator.uiStackShader();
		shader.bind();
		shader.setUniform1i("tex", OpenGLShaderLocations.TEXTURE_DIFFUSE);
		shader.unbind();
	}

	@Override
	protected void _bind(EngineObject object) {
		shader.bind();
		shader.setUniform3f("background_color", element.backgroundColor);
		shader.setUniformMat4f("world_matrix", element.getWorldMatrix());
		shader.setUniform1f("lower_tex_coord", element.lowerBound);
		shader.setUniform1f("upper_tex_coord", element.upperBound);
				
		Renderer renderer = Configuration.getRendererVerifyFinalized();
		switch (renderer) {
		case GENERIC:
			break;
		case OPEN_GL:
			OpenGLStateMachine.setActiveTextureSlot(OpenGLShaderLocations.TEXTURE_DIFFUSE);
			OpenGLStateMachine.bindTexture(((EngineObjectUIStack) element.getObject()).getTexture());
			break;
		}

	}

	@Override
	public Shader getShader() {
		return shader;
	}

}
