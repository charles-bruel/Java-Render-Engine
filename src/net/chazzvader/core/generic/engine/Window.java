package net.chazzvader.core.generic.engine;

import net.chazzvader.core.generic.Configuration;
import net.chazzvader.core.generic.Configuration.Renderer;
import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.render.RenderPipeline;
import net.chazzvader.core.generic.engine.render.material.Texture;
import net.chazzvader.core.generic.math.Vector3f;
import net.chazzvader.core.opengl.engine.OpenGLWindow;

/**
 * The window, you know, the things that appears on your screen.<br>
 * Implementation depends on renderer.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 * @see OpenGLWindow
 */
public abstract class Window extends EngineItem {

	/**
	 * Most graphics libraries reference a window with a pointer-like long
	 */
	protected long ptr;

	/**
	 * Most graphics libraries reference a window with a pointer-like long
	 * 
	 * @return The pointer
	 */
	public long getPtr() {
		return ptr;
	}

	/**
	 * The current width and height of the window
	 */
	protected int width, height;

	/**
	 * The current width of the window
	 * 
	 * @return The current width of the window
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * The current height of the window
	 * 
	 * @return The current height of the window
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets a window based on the current rendering API. This also assumes the
	 * window setup process also does some basic engine setup
	 * 
	 * @param width    The width of the window in pixels
	 * @param height   The height of the window in pixels
	 * @param instance The application instance
	 * 
	 * @return The window, unless there was an issue, then it returns null
	 */
	public static Window create(int width, int height, Application instance) {
		Configuration.assertRendererFinalized();
		if (instance == null || instance.window != null) {
			Logging.log("Failed to create window, invalid instance", "Window", LoggingLevel.ERR);
		}
		Renderer renderer = Configuration.getRendererVerifyFinalized();
		if (renderer == null) {
			Logging.log("Failed to create window, renderer not finalized", "Window", LoggingLevel.ERR);
			return null;
		}

		Logging.log("Creating Window! ", "Window", LoggingLevel.INFO);

		switch (renderer) {
		case OPEN_GL:
			instance.window = new OpenGLWindow(width, height);
			return instance.window;
		default:
			Logging.log("Unable to find window for renderer " + renderer, "Window", LoggingLevel.ERR);
			return null;
		}
	}

	/**
	 * Should the loop run (i.e. GLFW has a should window close function)
	 * 
	 * @return If the loop should run
	 */
	public abstract boolean shouldRun();

	public abstract void preUpdate(double delta);

	public abstract void postUpdate(double delta);

	public abstract void preRender();

	public abstract void postRender();

	/**
	 * Locks the mouse to the window (FPS game style)
	 */
	public abstract void lockMouse();

	/**
	 * Unlocks the mouse from the window
	 */
	public abstract void unlockMouse();

	/**
	 * Is the mouse locked
	 * 
	 * @return True is the mouse locked
	 */
	public abstract boolean isMouseLocked();

	/**
	 * Draws wireframe
	 */
	public abstract void wireframeDrawMode();

	/**
	 * Draws regular
	 */
	public abstract void regularDrawMode();

	/**
	 * Is the mouse locked
	 * 
	 * @return True is the mouse locked
	 */
	public abstract boolean isWireframeMode();

	/**
	 * Draws so the front is regular and the back is wireframe
	 */
	public abstract void debugDrawMode();
	
	/**
	 * Draws so the front and back are regular
	 */
	public abstract void noCullDrawMode();

	/**
	 * Sets the background color
	 * 
	 * @param color The new background color
	 */
	public abstract void setBackgroundColor(Vector3f color);

	/**
	 * Gets the rendering pipeline used for the window. Used in several places.
	 * 
	 * @return The rendering pipeline
	 * @see RenderPipeline
	 */
	public abstract RenderPipeline getRenderPipeline();

	/**
	 * Sets the icon of the window.<br>
	 * <br>
	 * <b>NOTE</b>: In openGL mode, this uses a the raw data in a texture, <em>which
	 * is deleted</em> after the is loaded into VRAM. Use a texture before it loaded
	 * or mark it with <code>dontDeletePixelData()</code>.
	 * 
	 * @see Texture#dontDeletePixelData()
	 * 
	 * @param texture A texture object.
	 */
	public abstract void setWindowIcon(Texture texture);

	/**
	 * Gets the background color
	 * @return The background color
	 */
	public abstract Vector3f getBackgroundColor();

}
