package net.chazzvader.core.generic.engine.creator;

import net.chazzvader.core.generic.Configuration;
import net.chazzvader.core.generic.Configuration.Renderer;
import net.chazzvader.core.generic.engine.render.material.Shader;
import net.chazzvader.core.generic.engine.uilegacy.element.UIElementStack;
import net.chazzvader.core.opengl.shader.OpenGLShader;

/**
 * Creates shaders
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
@SuppressWarnings("deprecation")
public class ShaderCreator {

	private ShaderCreator() {
	}

	private static Shader basicShader = null;

	/**
	 * Gets a basic shader
	 * @return A basic shader
	 */
	public static Shader basicShader() {
		return basicShader != null ? basicShader : (basicShader = createBasicShader());
	}

	private static Shader createBasicShader() {
		Renderer renderer = Configuration.getRendererVerifyFinalized();
		switch (renderer) {
		case GENERIC:
			break;
		case OPEN_GL:
			OpenGLShader ret = new OpenGLShader("net/chazzvader/core/opengl/shader/src/generic/basic_tbn.vert",
					"net/chazzvader/core/opengl/shader/src/generic/basic.frag");
			ret.shadows = true;
			return ret;
		}
		return null;
	}

	private static Shader lightShader = null;
	
	/**
	 * Gets a shader for a light
	 * @return A light shader
	 */
	public static Shader lightShader() {
		return lightShader != null ? lightShader : (lightShader = createLightShader());
	}

	private static Shader createLightShader() {
		Renderer renderer = Configuration.getRendererVerifyFinalized();
		switch (renderer) {
		case GENERIC:
			break;
		case OPEN_GL:
			return new OpenGLShader("net/chazzvader/core/opengl/shader/src/generic/basic.vert",
					"net/chazzvader/core/opengl/shader/src/generic/basic_light.frag");
		}
		return null;
	}

	private static Shader texCoords = null;
	
	/**
	 * Gets a shader for visualizing texture coordinates
	 * @return A texture coordinate tester shader
	 */
	public static Shader textureCoordinatesTest() {
		return texCoords != null ? texCoords : (texCoords = createTexCoordsShader());
	}

	private static Shader createTexCoordsShader() {
		Renderer renderer = Configuration.getRendererVerifyFinalized();
		switch (renderer) {
		case GENERIC:
			break;
		case OPEN_GL:
			return new OpenGLShader("net/chazzvader/core/opengl/shader/src/generic/basic.vert",
					"net/chazzvader/core/opengl/shader/src/debug/tex_coord_test.frag");
		}
		return null;
	}

	private static Shader testGeoShader = null;
	
	/**
	 * Gets a shader for testing geometry shaders. <em>DO NOT USE!</em>
	 * @return A geometry test shader
	 */
	public static Shader testGeoShader() {
		return testGeoShader != null ? testGeoShader : (testGeoShader = createTestGeoShader());
	}

	private static Shader createTestGeoShader() {
		Renderer renderer = Configuration.getRendererVerifyFinalized();
		switch (renderer) {
		case GENERIC:
			break;
		case OPEN_GL:
			return new OpenGLShader("net/chazzvader/core/opengl/shader/src/debug/debug.vert",
					"net/chazzvader/core/opengl/shader/src/debug/test.geom", "net/chazzvader/core/opengl/shader/src/debug/debug.frag");
		}
		return null;
	}

	private static Shader shadowShader = null;
	
	/**
	 * The shader used for shadow mapping. <em>DO NOT USE UNLESS YOU KNOW WHAT YOU ARE DOING</em>
	 * @return The shadow mapping shader
	 */
	public static Shader shadowShader() {
		return shadowShader != null ? shadowShader : (shadowShader = createShadowShader());
	}

	private static Shader createShadowShader() {
		Renderer renderer = Configuration.getRendererVerifyFinalized();
		switch (renderer) {
		case GENERIC:
			break;
		case OPEN_GL:
			return new OpenGLShader("net/chazzvader/core/opengl/shader/src/lighting/shadow.vert", "net/chazzvader/core/opengl/shader/src/lighting/shadow.geom", "net/chazzvader/core/opengl/shader/src/lighting/shadow.frag", false);
		}
		return null;
	}
	
	private static Shader transparentShader = null;
	
	/**
	 * A transparent shader
	 * @return A completely transparent shader
	 */
	public static Shader transparentShader() {
		return transparentShader != null ? transparentShader : (transparentShader = createTransparentShader());
	}

	private static Shader createTransparentShader() {
		Renderer renderer = Configuration.getRendererVerifyFinalized();
		switch (renderer) {
		case GENERIC:
			break;
		case OPEN_GL:
			return new OpenGLShader("net/chazzvader/core/opengl/shader/src/other/transparent.vert", "net/chazzvader/core/opengl/shader/src/other/transparent.frag", false);
		}
		return null;
	}
	
	private static Shader simpleShader = null;
	
	/**
	 * A simple shader which just renders the texture with a tint.
	 * @return A simple shader
	 */
	public static Shader simpleShader() {
		return simpleShader != null ? simpleShader : (simpleShader = createSimpleShader());
	}

	private static Shader createSimpleShader() {
		Renderer renderer = Configuration.getRendererVerifyFinalized();
		switch (renderer) {
		case GENERIC:
			break;
		case OPEN_GL:
			return new OpenGLShader("net/chazzvader/core/opengl/shader/src/generic/simple.vert", "net/chazzvader/core/opengl/shader/src/generic/simple.frag");
		}
		return null;
	}

	private static Shader uiShader = null;
	
	/**
	 * A UI shader for rendering 2d textures
	 * @return A UI shader
	 */
	public static Shader uiShader() {
		return uiShader != null ? uiShader : (uiShader = createUiShader());
	}

	private static Shader createUiShader() {
		Renderer renderer = Configuration.getRendererVerifyFinalized();
		switch (renderer) {
		case GENERIC:
			break;
		case OPEN_GL:
			return new OpenGLShader("net/chazzvader/core/opengl/shader/src/ui/ui.vert", "net/chazzvader/core/opengl/shader/src/ui/ui.frag", false);
		}
		return null;
	}

	private static Shader normals = null;
	
	/**
	 * Gets a shader for visualizing texture coordinates
	 * @return A texture coordinate tester shader
	 */
	public static Shader normalsTest() {
		return normals != null ? normals : (normals = createNormalsShader());
	}

	private static Shader createNormalsShader() {
		Renderer renderer = Configuration.getRendererVerifyFinalized();
		switch (renderer) {
		case GENERIC:
			break;
		case OPEN_GL:
			return new OpenGLShader("net/chazzvader/core/opengl/shader/src/generic/basic.vert",
					"net/chazzvader/core/opengl/shader/src/debug/normals_test.frag");
		}
		return null;
	}
	
	private static Shader uiShade = null;
	
	/**
	 * The shader for rendering UI shades, that is a tint applied to the entire screen.
	 * @return The shader for rendering UI shades.
	 */
	public static Shader uiShade() {
		return uiShade != null ? uiShade : (uiShade = createUiShade());
	}

	private static Shader createUiShade() {
		Renderer renderer = Configuration.getRendererVerifyFinalized();
		switch (renderer) {
		case GENERIC:
			break;
		case OPEN_GL:
			return new OpenGLShader("net/chazzvader/core/opengl/shader/src/ui/shade.vert",
					"net/chazzvader/core/opengl/shader/src/ui/shade.frag", false);
		}
		return null;
	}

	@Deprecated
	private static Shader uiStackShader = null;
	
	/**
	 * The shader for <code>UIElementStack</code>.
	 * @return The shader for <code>UIElementStack</code>.
	 * @see UIElementStack
	 */
	@Deprecated
	public static Shader uiStackShader() {
		return uiStackShader != null ? uiStackShader : (uiStackShader = createUiStackShader());
	}

	@Deprecated
	private static Shader createUiStackShader() {
		Renderer renderer = Configuration.getRendererVerifyFinalized();
		switch (renderer) {
		case GENERIC:
			break;
		case OPEN_GL:
			return new OpenGLShader("net/chazzvader/core/opengl/shader/src/ui/ui_stack.vert", "net/chazzvader/core/opengl/shader/src/ui/ui_stack.frag", false);
		}
		return null;
	}
	
}
