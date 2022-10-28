package net.chazzvader.core.opengl.engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLDebugMessageCallback;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import net.chazzvader.core.generic.Configuration;
import net.chazzvader.core.generic.Configuration.Renderer;
import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.Window;
import net.chazzvader.core.generic.engine.event.ActionType;
import net.chazzvader.core.generic.engine.event.EventManager;
import net.chazzvader.core.generic.engine.event.type.EventDataIDAndAction;
import net.chazzvader.core.generic.engine.event.type.EventDataMouseMoved;
import net.chazzvader.core.generic.engine.event.type.EventDataScroll;
import net.chazzvader.core.generic.engine.event.type.EventDataWindowResized;
import net.chazzvader.core.generic.engine.event.type.EventTypes;
import net.chazzvader.core.generic.engine.render.RenderPipeline;
import net.chazzvader.core.generic.engine.render.material.Texture;
import net.chazzvader.core.generic.math.Vector3f;
import net.chazzvader.core.opengl.engine.render.OpenGLRenderPipeline;
import net.chazzvader.core.opengl.engine.util.OpenGLStateMachine;

/**
 * OpenGL implementation of Window
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 * @see Window
 */
public class OpenGLWindow extends Window {

	private Vector3f backgroundColor = new Vector3f(1, 1, 1);
	
	/**
	 * Constructor, do not call directly, use Window.create() Code based on LWJGL
	 * getting started code as of 10/25/19
	 * 
	 * @param width  The width of the window
	 * @param height The height of the window
	 * @see Window#create(int, int, net.chazzvader.core.generic.engine.Application)
	 */
	public OpenGLWindow(int width, int height) {
		Configuration.assertRenderer(Renderer.OPEN_GL);
		this.width = width;
		this.height = height;

		GLFWErrorCallback.createPrint(System.err).set();

		if (!GLFW.glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

		ptr = GLFW.glfwCreateWindow(width, height, Configuration.getApplicationName(), MemoryUtil.NULL,
				MemoryUtil.NULL);
		if (ptr == MemoryUtil.NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		// Center window
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			GLFW.glfwGetWindowSize(ptr, pWidth, pHeight);

			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

			GLFW.glfwSetWindowPos(ptr, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
		}

		setupWindow();
	}

	private void setupWindow() {
		GLFW.glfwMakeContextCurrent(ptr);

		GLFW.glfwSwapInterval(1);// V-sync kinda 0 off 1 on

		GLFW.glfwShowWindow(ptr);

		registerEventListeners();

		GL.createCapabilities();

		GL11.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);//Background color

		GLFW.glfwSetInputMode(ptr, GLFW.GLFW_LOCK_KEY_MODS, GLFW.GLFW_TRUE);//Register caps lock and num lock
		
		OpenGLStateMachine.enableValue(GL11.GL_DEPTH_TEST);

		GL43.glEnable(GL43.GL_DEBUG_OUTPUT);
		GL43.glDebugMessageCallback(new DebugMessageCallback(), 0);
	}

	private void registerEventListeners() {
		GLFW.glfwSetWindowSizeCallback(ptr, new WindowListener());
		GLFW.glfwSetCursorPosCallback(ptr, new MouseMoveListener());
		GLFW.glfwSetKeyCallback(ptr, new KeyListener());
		GLFW.glfwSetMouseButtonCallback(ptr, new MouseButtonListener());
		GLFW.glfwSetScrollCallback(ptr, new ScrollWheelListener());
	}

	@Override
	public boolean shouldRun() {
		return !GLFW.glfwWindowShouldClose(ptr);
	}

	@Override
	public void preUpdate(double delta) {
		GLFW.glfwPollEvents();
	}

	@Override
	public void postUpdate(double delta) {
		
	}

	@Override
	public void preRender() {

	}

	@Override
	public void postRender() {

	}

	private boolean mouseLocked = false;

	@Override
	public void lockMouse() {
		mouseLocked = true;
		GLFW.glfwSetInputMode(ptr, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
	}

	@Override
	public void unlockMouse() {
		mouseLocked = false;
		GLFW.glfwSetInputMode(ptr, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
	}

	@Override
	public boolean isMouseLocked() {
		return mouseLocked;
	}

	private boolean wireframe = false;

	@Override
	public void wireframeDrawMode() {
		wireframe = true;
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
	}

	@Override
	public void regularDrawMode() {
		wireframe = false;
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		GL11.glEnable(GL11.GL_CULL_FACE);
	}

	@Override
	public void debugDrawMode() {
		wireframe = true;
		GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
		GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_LINE);
	}
	
	@Override
	public void noCullDrawMode() {
		wireframe = false;
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	@Override
	public boolean isWireframeMode() {
		return wireframe;
	}

	@Override
	public void setBackgroundColor(Vector3f color) {
		backgroundColor = color;
	}

	@Override
	public Vector3f getBackgroundColor() {
		return this.backgroundColor;
	}

	@Override 
	public RenderPipeline getRenderPipeline() {
		return OpenGLRenderPipeline.INSTANCE;
	}

	@Override
	public void setWindowIcon(Texture texture) {
		if(!texture.isPrepared()) {
			texture.prepare();
		}
		GLFWImage.Buffer imageBuffer = GLFWImage.create(1);
		int[] pixels = texture.getPixels();
		ByteBuffer buffer = ByteBuffer.allocateDirect(pixels.length * 4).order(ByteOrder.nativeOrder());
		for(int i = 0;i < pixels.length;i ++) {
			buffer.putInt(pixels[i]);
		}
		buffer.flip();
		GLFWImage image = GLFWImage.create();
        image.set(texture.getWidth(), texture.getHeight(), buffer);
		imageBuffer.put(0, image);
		GLFW.glfwSetWindowIcon(ptr, imageBuffer);
	}
	
	/*
	 * Callbacks
	 */
	
	private class DebugMessageCallback extends GLDebugMessageCallback {

		private Logging.LoggingLevel getSeverity(int severity) {
			return severity == GL43.GL_DEBUG_SEVERITY_NOTIFICATION ? LoggingLevel.DEBUG : LoggingLevel.ERR;
		}

		private String getSource(int source) {
			switch (source) {
			case GL43.GL_DEBUG_SOURCE_API:
				return "API";
			case GL43.GL_DEBUG_SOURCE_APPLICATION:
				return "Application";
			case GL43.GL_DEBUG_SOURCE_SHADER_COMPILER:
				return "Shader Compiler";
			case GL43.GL_DEBUG_SOURCE_THIRD_PARTY:
				return "Unknown 3rd Party";
			case GL43.GL_DEBUG_SOURCE_WINDOW_SYSTEM:
				return "Window System";
			default:
				return "Other";
			}
		}
		
		private String getType(int type) {
			switch (type) {
			case GL43.GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR:
				return "Deprecated";
			case GL43.GL_DEBUG_TYPE_ERROR:
				return "Generic Error";
			case GL43.GL_DEBUG_TYPE_MARKER:
				return "Marker";
			case GL43.GL_DEBUG_TYPE_PERFORMANCE:
				return "Performance";
			case GL43.GL_DEBUG_TYPE_POP_GROUP:
				return "Pop Group";
			case GL43.GL_DEBUG_TYPE_PORTABILITY:
				return "Portability";
			case GL43.GL_DEBUG_TYPE_PUSH_GROUP:
				return "Push Group";
			case GL43.GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR:
				return "Undefined Behavior";
			default:
				return "Other/Unknown";
			}
		}

		@Override
		public void invoke(int source, int type, int id, int severity, int length, long message, long userParam) {
			String msg = MemoryUtil.memUTF8(MemoryUtil.memByteBufferNT1(message, length));
			Logging.log(getSource(source)+"/"+getType(type)+":\n" + msg, "LWJGL", getSeverity(severity));
		}
	}
	
	private class WindowListener extends GLFWWindowSizeCallback {

		public WindowListener() {
			Configuration.assertRenderer(Renderer.OPEN_GL);
		}

		@Override
		public void invoke(long window, int nWidth, int nHeight) {
			if (window != ptr)
				return;
			width = nWidth;
			height = nHeight;
			EventManager.dispatch(new EventDataWindowResized(nWidth, nHeight));
		}
	}

	private class MouseMoveListener extends GLFWCursorPosCallback {

		public MouseMoveListener() {
			Configuration.assertRenderer(Renderer.OPEN_GL);
		}

		@Override
		public void invoke(long window, double xpos, double ypos) {
			EventManager.dispatch(new EventDataMouseMoved((int) xpos, (int) ypos));
		}

	}
	
	private class ScrollWheelListener extends GLFWScrollCallback {

		public ScrollWheelListener() {
			Configuration.assertRenderer(Renderer.OPEN_GL);
		}

		@Override
		public void invoke(long window, double xoffset, double yoffset) {
			EventManager.dispatch(new EventDataScroll((float) xoffset, (float) yoffset));
		}

	}

	private class KeyListener extends GLFWKeyCallback {

		public KeyListener() {
			Configuration.assertRenderer(Renderer.OPEN_GL);
		}

		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			if (window != ptr)
				return;
			ActionType type = ActionType.PRESSED;// Placeholder
			if (action == GLFW.GLFW_PRESS) {
				type = ActionType.PRESSED;// Not strictly nessecary
			} else if (action == GLFW.GLFW_RELEASE) {
				type = ActionType.RELEASED;
			} else {
				return;
			}
			EventManager.dispatch(new EventDataIDAndAction(type, key, mods, EventTypes.EVENT_KEY));
		}

	}

	private class MouseButtonListener extends GLFWMouseButtonCallback {

		public MouseButtonListener() {
			Configuration.assertRenderer(Renderer.OPEN_GL);
		}

		@Override
		public void invoke(long window, int button, int action, int mods) {
			if (window != ptr)
				return;
			ActionType type = ActionType.PRESSED;// Placeholder
			if (action == GLFW.GLFW_PRESS) {
				type = ActionType.PRESSED;// Not strictly nessecary
			} else if (action == GLFW.GLFW_RELEASE) {
				type = ActionType.RELEASED;
			} else {
				return;
			}
			EventManager.dispatch(new EventDataIDAndAction(type, button, mods, EventTypes.EVENT_MOUSE_BUTTON));
		}

	}

}
