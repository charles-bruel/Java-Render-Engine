package net.chazzvader.core.generic.engine.render;

import net.chazzvader.core.generic.engine.Application;
import net.chazzvader.core.generic.engine.EngineItem;
import net.chazzvader.core.generic.engine.IRenderContextProvider;
import net.chazzvader.core.generic.engine.Scene;
import net.chazzvader.core.generic.engine.Window;
import net.chazzvader.core.generic.engine.object.Light;
import net.chazzvader.core.opengl.engine.render.OpenGLRenderPipeline;

/**
 * A rendering pipeline does everything to render the scene, including
 * shadows.<br>
 * Implementation depends on renderer.<br>
 * Call {@link #preRender(Window, Application)},
 * {@link #render(Window, Application, Scene)},
 * {@link #postRender(Window, Application)} sequentially.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 * @see OpenGLRenderPipeline
 */
public abstract class RenderPipeline extends EngineItem {

	/**
	 * Any steps to take before the bulk of rendering. I.e. clearing the screen.
	 * 
	 * @param window      The window rendering to.
	 * @param application The application.
	 */
	public abstract void preRender(Window window, Application application);

	/**
	 * The bulk of rendering, this does everything from shadows to the actual
	 * scene.
	 * 
	 * @param window      The window rendering to.
	 * @param application The application.
	 * @param activeScene The active scene to be rendering.
	 */
	public abstract void render(Window window, Application application, Scene activeScene);

	/**
	 * Any steps to take after the bulk of rendering. I.e. swapping buffers.
	 * 
	 * @param window      The window rendering to.
	 * @param application The application.
	 */
	public abstract void postRender(Window window, Application application);

	/**
	 * A simple enum representing what major step the rendering is in.<br>
	 * <code>REGULAR</code> is regular 3d rendering.
	 * <code>SHADOWS</code> is for when shadows are being rendered.
	 * 
	 * @author csbru
	 * @version 1
	 * @since 1.0
	 */
	@SuppressWarnings("javadoc")
	public static enum RenderingMode {
		REGULAR, SHADOWS;
	}

	/**
	 * Simply returns the current rendering mode.
	 * @return The current rendering mode.
	 */
	public abstract RenderingMode getRenderingMode();

	/**
	 * Shadows are rendered light by light, so this tells the shader what light to
	 * render with for shadows, and is thus not used during regular rendering.
	 * 
	 * @return The current light to render shadow maps with.
	 */
	public abstract Light getRenderingLight();

	/**
	 * Gets the standard 3d render context for this pipeline.
	 * @return The standard 3d render
	 */
	public abstract RenderContext get3dContext();
	
	/**
	 * Gets the standard UI render context for this pipeline.
	 * @return The standard UI render
	 */
	public abstract RenderContext getUIContext();
	
	/**
	 * Gets the standard 3d render context provider for this pipeline.
	 * @return The standard 3d render provider
	 */
	public abstract IRenderContextProvider get3dProvider();
	
	/**
	 * Gets the standard UI render context provider for this pipeline.
	 * @return The standard UI render provider
	 */
	public abstract IRenderContextProvider getUIProvider();


}
