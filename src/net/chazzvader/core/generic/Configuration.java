package net.chazzvader.core.generic;

import net.chazzvader.core.generic.Logging.LoggingLevel;

/**
 * A class containing a bunch of static configuration details
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class Configuration {

	/* Rendering Engine */

	/**
	 * List of valid rendering APIs
	 * 
	 * @author csbru
	 * @since 1.0
	 * @version 1
	 */
	public enum Renderer {
		/**
		 * <h1>OpenGL 4.3.</h1><br>
		 * OpenGL is one of the most commonly used graphics libraries. If you are
		 * planning on doing some of the graphics work yourself, OpenGL is a good
		 * idea.<br>
		 * <a href="https://en.wikipedia.org/wiki/OpenGL">Wikipedia</a>
		 */
		OPEN_GL,
		/**
		 * No rendering engine. You can't actually <em>do</em> anything with this
		 * option.
		 */
		GENERIC;
	}

	/**
	 * Is the renderer finalized? A finalized renderer <em>cannot</em> be changed
	 */
	private static boolean rendererFinalized = false;

	/**
	 * What rendering API to use
	 */
	private static Renderer renderer = null;

	/**
	 * Is the renderer finalized? <em>No graphics code should be run before the
	 * renderer is finalized</em>
	 * 
	 * @return If the renderer is finalized
	 */
	public static boolean rendererFinalized() {
		return rendererFinalized;
	}

	/**
	 * Asserts that the renderer is the selected one.<br>
	 * Assertion are used as checks to make sure everything is working and are
	 * remove once development is done automatically.<br>
	 * You may need to change a JVM setting to get these to work.
	 * 
	 * @param renderer The renderer to check for.
	 */
	public static void assertRenderer(Renderer renderer) {
		assertRendererFinalized();
		assert renderer == Configuration.renderer : "Incorrect Renderer";
	}

	/**
	 * Makes sure there is a finalized renderer, so graphics work can begin.<br>
	 * Assertion are used as checks to make sure everything is working and are
	 * remove once development is done automatically.<br>
	 * You may need to change a JVM setting to get these to work.
	 */
	public static void assertRendererFinalized() {
		assert rendererFinalized : "Renderer not finalized";
	}

	/**
	 * Finalizes the renderer, allows for rendering to happen but prevents renderer
	 * change.
	 * 
	 * @return If the finalization was successful
	 */
	public static boolean rendererFinalize() {
		if (renderer == null) {
			Logging.log("Cannot finalized renderer because the renderer is null!", "Configuration", LoggingLevel.WARN);
			return false;
		}
		if (renderer == Renderer.GENERIC) {
			Logging.log("Generic is not a valid renderer to be finalized to", "Configuration", LoggingLevel.WARN);
			return false;
		}
		Logging.log("Finalized rendering engine to " + renderer, "Configuration", LoggingLevel.INFO);
		rendererFinalized = true;
		return true;
	}

	/**
	 * Sets the renderer
	 * 
	 * @param renderer The renderer to set to
	 * @return If the change was successful
	 */
	public static boolean setRenderer(Renderer renderer) {
		if (rendererFinalized) {
			Logging.log("Renderer already finalized!", "Configuration", LoggingLevel.WARN);
			return false;
		}
		Configuration.renderer = renderer;
		Logging.log("Set rendering engine to " + renderer, "Configuration", LoggingLevel.INFO);
		return true;
	}

	/**
	 * What rendering API to use
	 * 
	 * @return The rendering API
	 */
	public static Renderer getRenderer() {
		return renderer;
	}

	/**
	 * What rendering API to use
	 * 
	 * @return The rendering API
	 */
	public static Renderer getRendererVerifyFinalized() {
		if (!rendererFinalized) {
			Logging.log("Renderer not finalized yet!", "Configuration", LoggingLevel.WARN);
			return null;
		}
		return renderer;
	}

	/* Debug Mode */

	/**
	 * Should additional debug information be printed
	 */
	private static boolean debugMode = false;

	/**
	 * Should additional debug information be printed
	 * 
	 * @return If additional debug information should be printed
	 */
	public static boolean isDebugMode() {
		return debugMode;
	}

	/**
	 * Sets if additional debug information be printed
	 * 
	 * @param debug What to set debug mode to
	 */
	public static void setDebugMode(boolean debug) {
		Configuration.debugMode = debug;
		Logging.log("Set debug mode to " + debug, "Configuration", LoggingLevel.DEBUG);
	}

	/**
	 * The name of the application
	 */
	private static String applicationName;

	/**
	 * Gets the currently set application name
	 * 
	 * @return The application name
	 */
	public static String getApplicationName() {
		return applicationName;
	}

	/**
	 * Sets the current application name. This is <em>not</em> guaranteed to update
	 * any currently existing things
	 * 
	 * @param applicationName The application name to set
	 */
	public static void setApplicationName(String applicationName) {
		Configuration.applicationName = applicationName;
	}

	/* File stuff */

	private static String filepathPrefix = "src\\";

	/**
	 * Loading files when compiled in a .jar file is different then in the
	 * workspace. When in the workspace, a relative path is used from the project.
	 * The path typically start with "src\", and then you get to packages. However,
	 * the "src\" part is not in a jar file. When loading files, the path is given
	 * as it would be in a jar file. If it fails to load in the jar file way, it
	 * will load from the workspace, and the prefix is added.<br>
	 * This method gets the prefix.
	 * 
	 * @return The prefix.
	 */
	public static String getFilepathPrefix() {
		return filepathPrefix;
	}

	/**
	 * Loading files when compiled in a .jar file is different then in the
	 * workspace. When in the workspace, a relative path is used from the project.
	 * The path typically start with "src\", and then you get to packages. However,
	 * the "src\" part is not in a jar file. When loading files, the path is given
	 * as it would be in a jar file. If it fails to load in the jar file way, it
	 * will load from the workspace, and the prefix is added.<br>
	 * This method sets the prefix.
	 * 
	 * @param filepathPrefix The prefix to set.
	 */
	public static void setFilepathPrefix(String filepathPrefix) {
		Configuration.filepathPrefix = filepathPrefix;
	}

	/* Overrides */

	private static boolean renderOverride = false;

	/**
	 * Is the render override flag set?
	 * 
	 * @return The state of the render override flag
	 */
	public static boolean isRenderOverride() {
		return renderOverride;
	}

	/**
	 * Sets the render override flag. If true, all render goes through the
	 * application, more customizability
	 * 
	 * @param renderOverride The flag to set
	 */
	public static void setRenderOverride(boolean renderOverride) {
		Configuration.renderOverride = renderOverride;
	}

	/* VARIOUS */

	/**
	 * Are shadows enabled?
	 */
	public static boolean shadows = true;

}
