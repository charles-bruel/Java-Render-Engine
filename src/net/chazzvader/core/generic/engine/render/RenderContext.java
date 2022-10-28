package net.chazzvader.core.generic.engine.render;

/**
 * A render context is contains all the info the renderer needs to correctly
 * render the scene of ui stack or other items. Contains the framebuffer to
 * render to, blending modes, etc.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public abstract class RenderContext {

	/**
	 * Binds the context, essentially activates it.
	 */
	public abstract void bind();

	/**
	 * Gets the width in pixels of the context.
	 * @return The width
	 */
	public abstract int getWidth();

	/**
	 * Gets the height in pixels of the context.
	 * @return The height
	 */
	public abstract int getHeight();

	/**
	 * Is this context a depth-only one? (used for shadows)
	 * @return True if the context is depth-only.
	 */
	public abstract boolean isDepthOnly();

	/**
	 * Gets the framebuffer.
	 * @return The framebuffer.
	 */
	public abstract Framebuffer getFramebuffer();

}
