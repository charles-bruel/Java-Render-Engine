package net.chazzvader.core.opengl.engine.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL43;

import net.chazzvader.core.generic.Configuration;
import net.chazzvader.core.generic.Configuration.Renderer;
import net.chazzvader.core.generic.engine.Application;
import net.chazzvader.core.generic.engine.IRenderContextProvider;
import net.chazzvader.core.generic.engine.Scene;
import net.chazzvader.core.generic.engine.Window;
import net.chazzvader.core.generic.engine.creator.ShaderCreator;
import net.chazzvader.core.generic.engine.object.Light;
import net.chazzvader.core.generic.engine.render.RenderContext;
import net.chazzvader.core.generic.engine.render.RenderPipeline;
import net.chazzvader.core.opengl.engine.util.OpenGLStateMachine;
import net.chazzvader.core.opengl.shader.OpenGLShaderLocations;

/**
 * OpenGL implementation of <code>RenderPipeline</code> <br>
 * <br>
 * This contains the majority of OpenGL calls. In addition, since OpenGL and
 * GLSL are based on c/c++ things such as structs exist in a way in this class.
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 * @see RenderPipeline
 */
public class OpenGLRenderPipeline extends RenderPipeline {

	/**
	 * The static instance to use for everything
	 */
	public static final OpenGLRenderPipeline INSTANCE = new OpenGLRenderPipeline();

	/**
	 * The size of each face of the shadow map. Must be square, power of 2
	 * recommended. Functionality to configure this is in progress.
	 */
	public static final int SHADOW_SIZE = 4096;// TODO: Make configurable

	/**
	 * Simple constructor, just calls ensures we are using OpenGL.
	 */
	public OpenGLRenderPipeline() {
		setup();
	}

	private void setup() {
		Configuration.assertRenderer(Renderer.OPEN_GL);
	}

	@Override
	public void preRender(Window window, Application application) {
		GL11.glClearColor(window.getBackgroundColor().x, window.getBackgroundColor().y, window.getBackgroundColor().z, 0.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear framebuffers
	}

	@Override
	public void render(Window window, Application application, Scene activeScene) {
		render3d(window, application, activeScene);
	}

	private void render3d(Window window, Application application, Scene activeScene) {
		if (!Configuration.isRenderOverride()) {
			updateLightBuffers(activeScene);
			if (Configuration.shadows) {
				renderShadows(window, application, activeScene);
			}
			_3dContext.depthOnly = false;
			renderingMode = RenderingMode.REGULAR;
			activeScene.render(this);
		} else {
			application.render();
		}
	}

	private void renderShadows(Window window, Application application, Scene scene) {
		if(_3dContext == null) {
			_3dContext = new OpenGLRenderContext();
		}
		_3dContext.depthOnly = true;
		renderingMode = RenderingMode.SHADOWS;
		
		ArrayList<Light> lights = scene.getLights();
		if (shadowFramebuffers.length != lights.size()) {
			recreateShadowTextures(lights.size());
		}		
		for (int i = 0; i < lights.size(); i++) {
			renderingLight = lights.get(i);
			_3dContext.framebuffer = shadowFramebuffers[i];
			shadowFramebuffers[i].bind();
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			ShaderCreator.shadowShader().preRender(scene, this);
			scene.render(this);
		}
		_3dContext.framebuffer = OpenGLFramebuffer.getDefault();
	}
	
	private ShadowFramebuffer[] shadowFramebuffers = new ShadowFramebuffer[0];

	private void recreateShadowTextures(int size) {
		if (size > OpenGLShaderLocations.MAX_SHADOW_MAPS) {
			size = OpenGLShaderLocations.MAX_SHADOW_MAPS;
		}
		for(int i = 0;i < shadowFramebuffers.length;i ++) {
			shadowFramebuffers[i].delete();
			shadowFramebuffers[i] = null;
		}
		shadowFramebuffers = new ShadowFramebuffer[size];
		for (int i = 0; i < size; i++) {
			shadowFramebuffers[i] = new ShadowFramebuffer();
		}
	}

	@Override
	public void postRender(Window window, Application application) {
		window.postRender();
		OpenGLStateMachine.bindShader(0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
		GLFW.glfwSwapBuffers(window.getPtr());
	}

	/**
	 * The id of the lights SSBO in the shader.
	 * 
	 * @return The id of the lights SSBO in the shader.
	 */
	public int getLightingSSBOID() {
		return lightingSSBOID;
	}

	private int lightingSSBOID = 0;

	/**
	 * This updates in the information in the buffer and recreates the lights buffer
	 * if it is needed.
	 * 
	 * @param scene The scene, used for the light data.
	 */
	public void updateLightBuffers(Scene scene) {
		if (numLights != scene.getLights().size()) {
			if (lightingSSBOID != 0) {
				GL15.glDeleteBuffers(lightingSSBOID);
			}
			lightingSSBOID = GL15.glGenBuffers();
		}
		GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, lightingSSBOID);

		IntBuffer buffer = createLightsBuffer(scene.getLights());
		if (numLights != scene.getLights().size()) {
			GL30.glBindBufferBase(GL43.GL_SHADER_STORAGE_BUFFER, OpenGLShaderLocations.SSBO_POINT_LIGHTS,
					lightingSSBOID);
			GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, buffer, GL15.GL_DYNAMIC_DRAW);
		} else {
			GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, 0, buffer);
		}
		GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);

		numLights = scene.getLights().size();
	}

	/**
	 * The current number of lights in memory.
	 */
	public int numLights;

	/**
	 * The size of a point light struct.<br>
	 * The struct <code>point_light</code> is defined as:
	 * 
	 * <pre>
	 * struct
	 * {
	 * 	vec3 light_color;
	 * 	vec3 light_pos;
	 *	float constant;
	 * 	float linear;
	 * 	float quadratic;
	 * }
	 * </pre>
	 */
	public static final int POINT_LIGHT_STRUCT_SIZE;

	/**
	 * The struct <code>point_light</code> is defined as:
	 * 
	 * <pre>
	 * struct
	 * {
	 * 	vec3 light_color;
	 * 	vec3 light_pos;
	 *	float constant;
	 * 	float linear;
	 * 	float quadratic;
	 * }
	 * </pre>
	 */
	static {
		int size = 0;
		size += 4 * 3; // vec3 light_color; Float is size 4, 3 floats and 1 for alignment
		size += 4; // float linear; Float is size 4
		size += 4 * 3; // vec3 light_pos; Float is size 4, 3 floats and 1 for alignment
		size += 4; // float quadratic; Float is size 4
		size += 4; // float constant; Float is size 4;
		size += 4; // int textureID; Int is size 4;
		size += 8; // padding
		POINT_LIGHT_STRUCT_SIZE = size;
	}

	/**
	 * Gets the size of the lights buffer. Used to allow easier creation of the
	 * <code>SSBO</code>
	 * 
	 * @param lights The array of lights, purely used for length (as of now).
	 * @return The size in bytes.
	 */
	public static int lightsBufferSize(ArrayList<Light> lights) {
		int dataSize = lights.size() * POINT_LIGHT_STRUCT_SIZE + 16;
		return dataSize;
	}

	/**
	 * Creates a buffer used for a SSBO for an arraylist of lights. This also deals
	 * with alignment. The data structure is as follows:
	 * 
	 * <pre>
	 * buffer lights_buffer
	 * {
	 * 	int num_lights;
	 * 	point_light point_lights[];
	 * }
	 * </pre>
	 * 
	 * And the struct <code>point_light</code> is defined as:
	 * 
	 * <pre>
	 * struct
	 * {
	 * 	vec3 light_color;
	 * 	vec3 light_pos;
	 *	float constant;
	 * 	float linear;
	 * 	float quadratic;
	 * }
	 * </pre>
	 * 
	 * @param lights an ArrayList of light to create the buffer off of
	 * @return An intbuffer with the values for the light buffer
	 */
	public static IntBuffer createLightsBuffer(ArrayList<Light> lights) {
		int dataSize = lightsBufferSize(lights);
		ByteBuffer ret = ByteBuffer.allocateDirect(dataSize).order(ByteOrder.nativeOrder());
		ret.putInt(lights.size());
		ret.putInt(0);
		ret.putInt(0);
		ret.putInt(0);
		for (int i = 0; i < lights.size(); i++) {
			float[] fa = createPointLightBufferData(lights.get(i), i < 4 ? i : -1);
			//Not bind valid texture if more points lights are added. Nothing can go wrong here. 
			//TODO: Next Version. Fix.
			for (int j = 0; j < fa.length; j++) {
				ret.putFloat(fa[j]);
			}
		}
		ret.flip();
		return ret.asIntBuffer();
	}

	/**
	 * Creates a float[] for the data in the struct point_light as defined by:
	 * 
	 * <pre>
	 * struct
	 * {
	 * 	vec3 light_color;
	 * 	vec3 light_pos;
	 *	float constant;
	 * 	float linear;
	 * 	float quadratic;
	 * }
	 * </pre>
	 * 
	 * @param light A light to create the float data for the struct
	 * @param textureID The id of the used texture
	 * @return A float array which has the data for the struct
	 */
	public static float[] createPointLightBufferData(Light light, int textureID) {
		float[] ret = new float[POINT_LIGHT_STRUCT_SIZE / 4];
		ret[0] = light.lightColor.x;
		ret[1] = light.lightColor.y;
		ret[2] = light.lightColor.z;
		ret[3] = light.linear;
		ret[4] = light.pos().x;
		ret[5] = light.pos().y;
		ret[6] = light.pos().z;
		ret[7] = light.quadratic;
		ret[8] = light.constant;
		ret[9] = Float.intBitsToFloat(textureID);
		ret[10] = light.targetDistance * 2;
		ret[11] = 0;
		return ret;
	}

	/**
	 * Gets the number of lights in the scene.
	 * 
	 * @return The number of lights in the scene.
	 */
	public int numLights() {
		return numLights;
	}

	private RenderingMode renderingMode = RenderingMode.REGULAR;

	@Override
	public RenderingMode getRenderingMode() {
		return renderingMode;
	}

	private Light renderingLight;

	@Override
	public Light getRenderingLight() {
		return renderingLight;
	}

	/**
	 * Gets the array of framebuffers for the shadow maps, 1 for each light.
	 * 
	 * @return The array of framebuffers.
	 */
	public ShadowFramebuffer[] getShadowFramebuffers() {
		return shadowFramebuffers;
	}

	private OpenGLRenderContext _3dContext;
	private OpenGLRenderContext uiContext;
	
	@Override
	public RenderContext get3dContext() {
		if(_3dContext == null) {
			_3dContext = new OpenGLRenderContext();
		}
		return _3dContext;
	}
	
	@Override
	public RenderContext getUIContext() {
		if(uiContext == null) {
			uiContext = new OpenGLRenderContext();
			uiContext.blend = true;
			uiContext.depthFunc = GL11.GL_ALWAYS;
		}
		return uiContext;
	}
	
	/**
	 * The framebuffer type to render shadows to.
	 * @author csbru
	 * @version 1
	 * @since 1.0
	 */
	public class ShadowFramebuffer extends OpenGLFramebuffer {

		/**
		 * The id of the texture.
		 */
		public int shadowTexture;
		
		private ShadowFramebuffer() {
			super(SHADOW_SIZE, SHADOW_SIZE);
			recreate();
		}

		@Override
		public void bind() {
			super.bind();
		}
		
		@Override
		public void recreate() {
			if(fbo != 0) {
				GL11.glDeleteTextures(shadowTexture);
				GL30.glDeleteFramebuffers(fbo);
			}
			shadowTexture = GL11.glGenTextures();
			OpenGLStateMachine.bindTextureOverride(GL13.GL_TEXTURE_CUBE_MAP, shadowTexture);
			for (int j = 0; j < 6; j++) {
				GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + j, 0, GL11.GL_DEPTH_COMPONENT, SHADOW_SIZE,
						SHADOW_SIZE, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
			}
			GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE);

			fbo = GL30.glGenFramebuffers();
			OpenGLStateMachine.bindFramebufferOverride(fbo);
			GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, shadowTexture, 0);
			GL30.glDrawBuffer(GL11.GL_NONE);
			GL30.glReadBuffer(GL11.GL_NONE);
			OpenGLStateMachine.bindTexture(GL13.GL_TEXTURE_CUBE_MAP, 0);
		}

		@Override
		public void delete() {
			GL11.glDeleteTextures(shadowTexture);
			GL30.glDeleteFramebuffers(fbo);
		}
		
	}

	@Override
	public IRenderContextProvider get3dProvider() {
		return new IRenderContextProvider() {
			
			@Override
			public RenderContext getRenderContext() {
				return get3dContext();
			}
		};
	}

	@Override
	public IRenderContextProvider getUIProvider() {
		return new IRenderContextProvider() {
			
			@Override
			public RenderContext getRenderContext() {
				return getUIContext();
			}
		};
	}

}
