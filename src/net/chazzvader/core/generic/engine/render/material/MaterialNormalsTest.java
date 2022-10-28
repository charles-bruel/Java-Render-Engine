package net.chazzvader.core.generic.engine.render.material;

import net.chazzvader.core.generic.engine.creator.ShaderCreator;
import net.chazzvader.core.generic.engine.object.EngineObject;

/**
 * A simple material to test normals, displayed as color.
 * <code><br>x->r<br>y->g<br>z->b</code>.<br>
 * MaterialBasic does the same thing with the normal debug flag but with normal
 * maps.
 * 
 * @see MaterialBasic
 * @see MaterialBasic#normalDebug
 * @author csbru
 * @since 1.0O
 * @version 1
 */
public class MaterialNormalsTest extends Material {

	private Shader shader;

	/**
	 * Constructor, no parameters.
	 */
	public MaterialNormalsTest() {
		super(false);
		this.shader = ShaderCreator.normalsTest();
	}

	@Override
	protected void _bind(EngineObject object) {
		shader.bind();
		shader.setUniformMat4f("world_matrix", object.getWorldMatrix());
		shader.setUniformMat4f("normal_matrix", object.getNormalMatrix());
	}

	@Override
	public Shader getShader() {
		return shader;
	}

}
