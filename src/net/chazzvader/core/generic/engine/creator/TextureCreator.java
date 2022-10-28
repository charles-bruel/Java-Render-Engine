package net.chazzvader.core.generic.engine.creator;

import net.chazzvader.core.generic.Configuration;
import net.chazzvader.core.generic.Configuration.Renderer;
import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.render.material.Texture;
import net.chazzvader.core.opengl.engine.render.OpenGLTexture;

/**
 * Utilities to create textures
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
public class TextureCreator {

	/**
	 * Creates a texture from a file
	 * 
	 * @param filepath The filepath
	 * @return The texture
	 */
	public static Texture fromFile(String filepath) {
		Renderer renderer = Configuration.getRendererVerifyFinalized();
		switch (renderer) {
		case GENERIC:
			break;
		case OPEN_GL:
			return new OpenGLTexture(filepath);
		}
		return null;
	}

	/**
	 * Creates a texture from raw data.<br>
	 * The data should be organized as:<br>
	 * <link rel="stylesheet" type="text/css" href="C:/dev/Java/Engine/Java
	 * Engine/src/javadoc.css"/>
	 * <table>
	 * <tr>
	 * <td>Alpha</td>
	 * <td>Blue</td>
	 * <td>Green</td>
	 * <td>Red</td>
	 * </tr>
	 * <tr>
	 * <td>Bits 24-32</td>
	 * <td>Bits 16-23</td>
	 * <td>Bits 8-15</td>
	 * <td>Bits 0-7</td>
	 * </tr>
	 * </table>
	 * 
	 * @param data   The raw data
	 * @param width  The width of the texture
	 * @param height The height of the texture
	 * @return The texture
	 */
	public static Texture fromRaw(int[] data, int width, int height, boolean interpolate, boolean tile) {
		Renderer renderer = Configuration.getRendererVerifyFinalized();
		if (data.length != width * height) {
			Logging.log("Texture sizes dont match! ("+width+"x"+height+"!="+data.length+")", "Texture Creator", LoggingLevel.WARN);
		}
		switch (renderer) {
		case GENERIC:
			break;
		case OPEN_GL:
			return new OpenGLTexture(data, width, height);
		}
		return null;
	}
	
	/**
	 * Creates a texture from raw data.<br>
	 * The data should be organized as:<br>
	 * <link rel="stylesheet" type="text/css" href="C:/dev/Java/Engine/Java
	 * Engine/src/javadoc.css"/>
	 * <table>
	 * <tr>
	 * <td>Alpha</td>
	 * <td>Blue</td>
	 * <td>Green</td>
	 * <td>Red</td>
	 * </tr>
	 * <tr>
	 * <td>Bits 24-32</td>
	 * <td>Bits 16-23</td>
	 * <td>Bits 8-15</td>
	 * <td>Bits 0-7</td>
	 * </tr>
	 * </table>
	 * 
	 * @param data   The raw data
	 * @param width  The width of the texture
	 * @param height The height of the texture
	 * @return The texture
	 */
	public static Texture fromRaw(int[] data, int width, int height) {
		return fromRaw(data, width, height, false, false);
	}

}
