package net.chazzvader.core.generic.engine.render.material;

import net.chazzvader.core.generic.engine.creator.ShaderCreator;
import net.chazzvader.core.generic.engine.object.EngineObject;

/**
 * Very simple shader, purely transparent. Mostly useful for creating shadows
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
public class MaterialTransparent extends Material {

	/**
	 * Constructor, only takes if the shadow should be transparent.
	 * 
	 * @param transparentShadow Should this not make shadows.
	 */
	public MaterialTransparent(boolean transparentShadow) {
		super(transparentShadow);
	}

	@Override
	protected void _bind(EngineObject object) {

	}

	@Override
	public Shader getShader() {
		return ShaderCreator.transparentShader();
	}

}