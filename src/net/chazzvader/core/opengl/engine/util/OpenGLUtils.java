package net.chazzvader.core.opengl.engine.util;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

import net.chazzvader.core.generic.Configuration;
import net.chazzvader.core.generic.Configuration.Renderer;
import net.chazzvader.core.generic.util.Utils;

/**
 * Various static openGL-only utilities
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class OpenGLUtils {

	private OpenGLUtils() {
	}

	/**
	 * Loads the shader from shader files
	 * 
	 * @param vertex   The vertex shader file path
	 * @param fragment The fragment shader file path
	 * @return The id of the compiled shader program
	 * @see OpenGLUtils#createShader(String, String)
	 */
	public static int loadShader(String vertex, String fragment) {
		Configuration.assertRenderer(Renderer.OPEN_GL);
		String vert = Utils.loadAsString(vertex);
		String frag = Utils.loadAsString(fragment);
		return createShader(vert, frag);
	}

	/**
	 * Loads the shader from shader files
	 * 
	 * @param vertex   The vertex shader file path
	 * @param geometry The geometry shader file path
	 * @param fragment The fragment shader file path
	 * @return The id of the compiled shader program
	 * @see OpenGLUtils#createShader(String, String, String)
	 */
	public static int loadShader(String vertex, String geometry, String fragment) {
		Configuration.assertRenderer(Renderer.OPEN_GL);
		String vert = Utils.loadAsString(vertex);
		String geo = Utils.loadAsString(geometry);
		String frag = Utils.loadAsString(fragment);
		return createShader(vert, geo, frag);
	}

	/**
	 * Creates a shader program from shader text
	 * 
	 * @param vertex   The vertex shader, as plain text
	 * @param fragment The fragment shader, as plain text
	 * @return The id of the compiled shader program
	 */
	public static int createShader(String vertex, String fragment) {
		Configuration.assertRenderer(Renderer.OPEN_GL);
		int program = GL20.glCreateProgram();
		int vertID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		int fragID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(vertID, vertex);
		GL20.glShaderSource(fragID, fragment);

		GL20.glCompileShader(vertID);
		if (GL20.glGetShaderi(vertID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Failed to compile vertex shader!");
			System.err.println(GL20.glGetShaderInfoLog(vertID));
			return -1;
		}

		GL20.glCompileShader(fragID);
		if (GL20.glGetShaderi(fragID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Failed to compile fragment shader!");
			System.err.println(GL20.glGetShaderInfoLog(fragID));
			return -1;
		}

		GL20.glAttachShader(program, vertID);
		GL20.glAttachShader(program, fragID);
		GL20.glLinkProgram(program);
		GL20.glValidateProgram(program);

		GL20.glDeleteShader(vertID);
		GL20.glDeleteShader(fragID);

		return program;
	}

	/**
	 * Creates a shader program from shader text
	 * 
	 * @param vertex   The vertex shader, as plain text
	 * @param geometry The geometry shader, as plain text
	 * @param fragment The fragment shader, as plain text
	 * @return The id of the compiled shader program
	 */
	public static int createShader(String vertex, String geometry, String fragment) {
		Configuration.assertRenderer(Renderer.OPEN_GL);
		int program = GL20.glCreateProgram();
		int vertID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		int geoId = GL20.glCreateShader(GL32.GL_GEOMETRY_SHADER);
		int fragID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(vertID, vertex);
		GL20.glShaderSource(geoId, geometry);
		GL20.glShaderSource(fragID, fragment);

		GL20.glCompileShader(vertID);
		if (GL20.glGetShaderi(vertID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Failed to compile vertex shader!");
			System.err.println(GL20.glGetShaderInfoLog(vertID));
			return -1;
		}

		GL20.glCompileShader(geoId);
		if (GL20.glGetShaderi(geoId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Failed to compile geometry shader!");
			System.err.println(GL20.glGetShaderInfoLog(geoId));
			return -1;
		}

		GL20.glCompileShader(fragID);
		if (GL20.glGetShaderi(fragID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Failed to compile fragment shader!");
			System.err.println(GL20.glGetShaderInfoLog(fragID));
			return -1;
		}

		GL20.glAttachShader(program, vertID);
		GL20.glAttachShader(program, geoId);
		GL20.glAttachShader(program, fragID);
		GL20.glLinkProgram(program);
		GL20.glValidateProgram(program);

		GL20.glDeleteShader(vertID);
		GL20.glDeleteShader(geoId);
		GL20.glDeleteShader(fragID);

		return program;
	}

}
