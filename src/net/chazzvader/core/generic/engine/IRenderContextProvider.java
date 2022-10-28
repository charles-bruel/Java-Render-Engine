package net.chazzvader.core.generic.engine;

import net.chazzvader.core.generic.engine.render.RenderContext;

/**
 * Provides a render context, allows for the mass change of settings easily.
 * @author csbru
 * @version 1
 * @since 1.0
 */
public interface IRenderContextProvider {

	/**
	 * Provides a render context.
	 * @return The render context.
	 */
	public RenderContext getRenderContext();
	
}
