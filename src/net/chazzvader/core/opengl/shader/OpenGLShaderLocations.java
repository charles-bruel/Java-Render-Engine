package net.chazzvader.core.opengl.shader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import net.chazzvader.core.generic.Configuration;
import net.chazzvader.core.generic.Configuration.Renderer;
import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.mesh.VertexedProperty;
import net.chazzvader.core.generic.engine.mesh.VertexedProperty.VertexPropertyType;

/**
 * A bunch of static definitions of where various bindings, texture, and buffers
 * go.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class OpenGLShaderLocations {

	private OpenGLShaderLocations() {
	}

	/**
	 * Standard location for position input
	 */
	public static final int IN_POS = 0;

	/**
	 * Standard location for texture coordinate input
	 */
	public static final int IN_TEXTURE_COORD = 1;

	/**
	 * Standard location for color input
	 */
	public static final int IN_COLOR = 2;

	/**
	 * Standard location for normals input
	 */
	public static final int IN_NORMALS = 3;

	/**
	 * Standard location for normal tangent vectors
	 */
	public static final int IN_TANGENT = 4;

	/**
	 * Standard location for normal bitangent vectors
	 */
	public static final int IN_BITANGENT = 5;

	/**
	 * Standard location for other (user defined) input #1
	 */
	public static final int IN_OTHER_1 = 6;

	/**
	 * Standard location for other (user defined) input #2
	 */
	public static final int IN_OTHER_2 = 7;

	/**
	 * Standard location for color output
	 */
	public static final int OUT_COLOR = 0;

	/**
	 * Standard binding point shader storage buffer objects for lights
	 */
	public static final int SSBO_POINT_LIGHTS = 0;

	/**
	 * Standard texture id for diffuse textures
	 */
	public static final int TEXTURE_DIFFUSE = 0;

	/**
	 * Standard texture id for specular textures
	 */
	public static final int TEXTURE_SPECULAR = 1;

	/**
	 * Standard texture id for normals textures
	 */
	public static final int TEXTURE_NORMAL = 2;

	/**
	 * Standard texture ID for the first shadow map, add <code>n</code> for the
	 * <code>n-1</code> shadow map.
	 */
	public static final int TEXTURE_SHADOW_1;

	/**
	 * Max shadow maps.
	 */
	public static final int MAX_SHADOW_MAPS = 4;

	static {
		Configuration.assertRenderer(Renderer.OPEN_GL);
		int maxTextures = GL11.glGetInteger(GL20.GL_MAX_TEXTURE_IMAGE_UNITS);
		TEXTURE_SHADOW_1 = maxTextures - MAX_SHADOW_MAPS;
	}

	/**
	 * Returns the location based off of the usage of a VertexedPropertyType
	 * 
	 * @param type The property type to get based off of
	 * @return The correct location
	 * @see VertexedProperty.VertexPropertyType
	 */
	public static int fromVertexData(VertexPropertyType type) {
		switch (type) {
		case COLOR:
			return IN_COLOR;
		case INDICES:
			Logging.log("Indices don't have a location", "OpenGL Shader", LoggingLevel.ERR);
			return -1;
		case NORMALS:
			return IN_NORMALS;
		case OTHER1:
			return IN_OTHER_1;
		case OTHER2:
			return IN_OTHER_2;
		case TEXTURE_COORDINATES:
			return IN_TEXTURE_COORD;
		case VERTEX_POS:
			return IN_POS;
		case NORMAL_BITANGENT:
			return IN_BITANGENT;
		case NORMAL_TANGENT:
			return IN_TANGENT;
		}
		Logging.log("Couldn't find location", "OpenGL Shader", LoggingLevel.ERR);
		return -1;
	}

	/**
	 * Returns the size based off of the usage of a VertexedPropertyType. <br>
	 * For example, positions size is 3 because a position required xyz whereas as
	 * textures are 2d and only require xy, so texture coordinates size is 2.
	 * 
	 * @param type The property type to get based off of
	 * @return The correct size
	 * @see VertexedProperty.VertexPropertyType
	 */
	public static int sizeFromVertexData(VertexPropertyType type) {
		switch (type) {
		case COLOR:
			return 3;
		case INDICES:
			return -1;
		case NORMALS:
			return 3;
		case OTHER1:
			return 4;
		case OTHER2:
			return 4;
		case TEXTURE_COORDINATES:
			return 2;
		case VERTEX_POS:
			return 3;
		case NORMAL_BITANGENT:
			return 3;
		case NORMAL_TANGENT:
			return 3;
		}
		Logging.log("Couldn't find size", "OpenGL Shader", LoggingLevel.ERR);
		return -1;
	}

}
