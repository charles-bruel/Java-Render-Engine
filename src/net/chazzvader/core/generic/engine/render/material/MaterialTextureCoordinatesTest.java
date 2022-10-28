package net.chazzvader.core.generic.engine.render.material;

import net.chazzvader.core.generic.engine.creator.ShaderCreator;
import net.chazzvader.core.generic.engine.object.EngineObject;

/**
 * Simple shader to test texture coordinates, displayed as
 * color.<code><br>u->x<br>v->y<br>0->z</code>.<br>
 * MaterialBasic does the same thing with the texture coordinate debug flag.
 * 
 * @see MaterialBasic
 * @see MaterialBasic#textureCoordinatesDebug
 * @author csbru
 * @version 1
 * @since 1.0
 */
public class MaterialTextureCoordinatesTest extends Material {

	private Shader shader;

	/**
	 * Constructor, no parameters.
	 */
	public MaterialTextureCoordinatesTest() {
		super(false);
		this.shader = ShaderCreator.textureCoordinatesTest();
	}

	@Override
	protected void _bind(EngineObject object) {
		shader.bind();
		shader.setUniformMat4f("world_matrix", object.getWorldMatrix());
	}

	@Override
	public Shader getShader() {
		return shader;
	}

}
