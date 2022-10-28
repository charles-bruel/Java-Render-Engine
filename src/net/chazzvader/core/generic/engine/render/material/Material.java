package net.chazzvader.core.generic.engine.render.material;

import net.chazzvader.core.generic.engine.EngineItem;
import net.chazzvader.core.generic.engine.creator.ShaderCreator;
import net.chazzvader.core.generic.engine.object.EngineObject;

/**
 * A class to represent a material. Contains everything required to display the
 * material properly. Abstract because implementation depends on material.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public abstract class Material extends EngineItem {

	private boolean transparentShadow = false;

	/**
	 * Basic material constructor
	 * 
	 * @param transparentShadow If this material should be transparent in the shadow
	 *                          process
	 */
	public Material(boolean transparentShadow) {
		this.transparentShadow = transparentShadow;
	}

	private Shader shadowShader = ShaderCreator.shadowShader();
	private Shader transparentShader = ShaderCreator.transparentShader();

	/**
	 * Binds the material.<br>
	 * <br>
	 * Graphics APIs require a shader to be set, this sets the relevant shader. In
	 * addition this method sends up object specific data like textures.
	 * 
	 * @param object The object to bind for
	 */
	public void bind(EngineObject object) {
		if(object == null || !(object.getRenderContext() != null && object.getRenderContext().isDepthOnly())) {
			_bind(object);
		} else {
			if(transparentShadow) {
				transparentShader.bind();
			} else {
				shadowShader.bind();
				shadowShader.setUniformMat4f("world_matrix", object.getWorldMatrix());
			}
		}
	}

	/**
	 * Version of bind, used when not in a stage like shadows that requires a
	 * different shader.
	 * 
	 * @param object The object to bind for
	 */
	protected abstract void _bind(EngineObject object);

	/**
	 * Gets the shader used in this material
	 * 
	 * @return The shader
	 */
	public abstract Shader getShader();
}
