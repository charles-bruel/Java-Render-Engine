package net.chazzvader.core.generic.engine.render;

import net.chazzvader.core.generic.engine.EngineItem;
import net.chazzvader.core.generic.engine.IDeletable;

/**
 * A framebuffer is a render target. The default framebuffer is typically just
 * the screen, or it could be more complex like the Vulkan swap chain.<br>
 * <br>
 * This class has suptypes like OpenGLFramebuffer and even that is designed to
 * be overloaded with custom behavior due to the numerous and varied uses of
 * framebuffers.
 * 
 * @author csbru
 *
 */
public abstract class Framebuffer extends EngineItem implements IDeletable {

	/**
	 * Binds the framebuffer
	 */
	public abstract void bind();

	/**
	 * Gets the width of the framebuffer.
	 * @return The width
	 */
	public abstract int getWidth();
	
	/**
	 * Gets the height of the framebuffer.
	 * @return The height
	 */
	public abstract int getHeight();

	/**
	 * Recreates the framebuffer. Should be called when the size needs to be changed.
	 */
	public abstract void recreate();

	@Override
	public abstract void delete();

}
