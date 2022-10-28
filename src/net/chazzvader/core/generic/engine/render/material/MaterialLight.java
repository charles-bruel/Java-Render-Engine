package net.chazzvader.core.generic.engine.render.material;

import net.chazzvader.core.generic.engine.creator.ShaderCreator;
import net.chazzvader.core.generic.engine.object.EngineObject;
import net.chazzvader.core.generic.engine.object.Light;

/**
 * A material to represent a light. The material applies flat shading based off
 * the light's color.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class MaterialLight extends Material {

	private Shader shader;
	private final Light light;

	/**
	 * Creates a new light material using the properties of the given light.
	 * 
	 * @param light The light to base this material off of, only uses color.
	 */
	public MaterialLight(Light light) {
		super(true);
		this.shader = ShaderCreator.lightShader();
		this.light = light;
	}

	@Override
	protected void _bind(EngineObject object) {
		shader.bind();
		shader.setUniform3f("light_color", light.lightColor);
		shader.setUniformMat4f("world_matrix", object.getWorldMatrix());
	}

	@Override
	public Shader getShader() {
		return shader;
	}

}
