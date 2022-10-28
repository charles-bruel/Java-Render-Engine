package net.chazzvader.core.generic.engine;

import net.chazzvader.core.generic.engine.render.RenderContext;

/**
 * An more complex engine item, with an attached render context.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public abstract class EngineItemRenderable extends EngineItem {

	/**
	 * The provider, gives the render context. Allows for example, an entire scene
	 * to change at one.
	 */
	public IRenderContextProvider provider = null;

	@Override
	protected int getWindowWidth() {
		return getRenderContext().getWidth();
	}

	@Override
	protected int getWindowHeight() {
		return getRenderContext().getHeight();
	}

	/**
	 * Gets the render context from the provider.
	 * @return The render context
	 */
	public RenderContext getRenderContext() {
		return provider.getRenderContext();
	}

}
