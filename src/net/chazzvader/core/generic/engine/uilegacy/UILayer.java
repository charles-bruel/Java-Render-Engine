package net.chazzvader.core.generic.engine.uilegacy;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import net.chazzvader.core.generic.Configuration;
import net.chazzvader.core.generic.Configuration.Renderer;
import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.EngineItem;
import net.chazzvader.core.generic.engine.creator.ShaderCreator;
import net.chazzvader.core.generic.engine.render.material.Shader;
import net.chazzvader.core.generic.engine.uilegacy.element.UIElement;
import net.chazzvader.core.generic.math.Vector3f;
import net.chazzvader.core.generic.math.Vector4f;
import net.chazzvader.core.opengl.engine.render.OpenGLVertexArray;
import net.chazzvader.core.opengl.engine.util.OpenGLStateMachine;

@Deprecated
@SuppressWarnings("javadoc")
public class UILayer extends EngineItem {

	private UIStack stack;

	public Vector4f shadeColor;
	public boolean lockBehind;

	public UILayer(UIStack stack, Vector4f shadeColor, boolean lockBehind) {
		this.stack = stack;
		this.shadeColor = shadeColor;
		this.lockBehind = lockBehind;
	}

	public UILayer(UIStack stack, Vector4f shadeColor) {
		this(stack, shadeColor, true);
	}

	public UILayer(UIStack stack, boolean lockBehind) {
		this(stack, new Vector4f(0, 0, 0, 0), lockBehind);
	}

	public UILayer(UIStack stack) {
		this(stack, new Vector4f(0, 0, 0, 0), true);
	}

	public UIStack getStack() {
		checkDelete();
		return this.stack;
	}

	public ArrayList<UIElement> elements = new ArrayList<>();

	private static boolean mesh = false;
	private static OpenGLVertexArray openGLMesh = null;

	public void render() {
		checkDelete();
		renderShade();
		for (int i = 0; i < elements.size(); i++) {
			elements.get(i).getRenderContext().bind();
			elements.get(i).render();
		}
	}

	private void renderShade() {
		checkDelete();
		if (shadeColor.w != 0) {
			Renderer renderer = Configuration.getRendererVerifyFinalized();
			if (!mesh) {
				switch (renderer) {
				case GENERIC:
					break;
				case OPEN_GL:
					openGLMesh = new OpenGLVertexArray(
							net.chazzvader.core.generic.engine.creator.ObjectCreator.quad(new Vector3f(-1, -1, 0),
									new Vector3f(1, -1, 0), new Vector3f(-1, 1, 0), new Vector3f(1, 1, 0)));
				}
				mesh = true;
			}
			Shader shader = ShaderCreator.uiShade();
			shader.bind();
			shader.setUniform4f("shadeColor", shadeColor);
			switch (renderer) {
			case GENERIC:
				break;
			case OPEN_GL:
				getStack().getRenderContext().bind();
				OpenGLStateMachine.disableValue(GL11.GL_DEPTH_TEST);
				openGLMesh.render();
				OpenGLStateMachine.enableValue(GL11.GL_DEPTH_TEST);
			}
			shader.unbind();
		}
	}

	private int mouseX, mouseY;

	public boolean pushUpdateMouseAbsolutePosition(int x, int y) {
		checkDelete();
		mouseX = x;
		mouseY = y;
		for (int i = 0; i < elements.size(); i++) {
			UIElement element = elements.get(i);
			boolean temp = element.updateMouseAbsolutePosition(x, y);
			if (temp)
				return true;
		}
		return false;
	}

	public boolean pushUpdateMouseOffset(int x, int y) {
		checkDelete();
		for (int i = 0; i < elements.size(); i++) {
			UIElement element = elements.get(i);
			boolean temp = element.updateMouseOffset(x, y);
			if (temp)
				return true;
		}
		return false;
	}

	/**
	 * Pushes a mouse button released event to all elements in the layer.
	 * 
	 * @param button     The mouse button code.
	 * @param mods       Any key mods, contain if modifier keys are down (shift,
	 *                   ctrl). Already separated out in different parameters for
	 *                   your convenience.
	 * @param ctrl       Is the control key held down.
	 * @param shift      Is the shift key held down.
	 * @param alt        Is the alt key held down.
	 * @param os         Is the os-specific key held down. I believe this is the
	 *                   window key on windows and command on macOS.
	 * @param capLock    Is the caps lock key on.
	 * @param numLock    Is the num lock key on.
	 * @param clickCount Number of clicks, used to track double/triple clicks. Each
	 *                   click must be within 500 milliseconds of the previous.
	 * @return True if the event got blocked
	 */
	public boolean pushMouseButtonPressed(int button, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
			boolean capLock, boolean numLock, int clickCount) {
		checkDelete();
		for (int i = 0; i < elements.size(); i++) {
			UIElement element = elements.get(i);
			if (element.selectable() && element.within(mouseX, mouseY)) {
				Logging.log("Selected " + element, "UILayer", LoggingLevel.DEBUG);
				stack.focus = element;
			}
		}
		for (int i = 0; i < elements.size(); i++) {
			UIElement element = elements.get(i);
			if (element.isFocused() || !element.needFocus()) {
				boolean temp = element.mouseButtonPressed(button, mods, ctrl, shift, alt, os, capLock, numLock,
						clickCount);
				if (temp) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean pushMouseButtonReleased(int button, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
			boolean capLock, boolean numLock) {
		checkDelete();
		for (int i = 0; i < elements.size(); i++) {
			UIElement element = elements.get(i);
			if (element.isFocused() || !element.needFocus()) {
				boolean temp = element.mouseButtonReleased(button, mods, ctrl, shift, alt, os, capLock, numLock);
				if (temp) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean pushKeyPressed(int key, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
			boolean capLock, boolean numLock) {
		checkDelete();
		for (int i = 0; i < elements.size(); i++) {
			UIElement element = elements.get(i);
			if (element.isFocused() || !element.needFocus()) {
				boolean temp = element.keyPressed(key, mods, ctrl, shift, alt, os, capLock, numLock);
				if (temp) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean pushKeyReleased(int key, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
			boolean capLock, boolean numLock) {
		checkDelete();
		for (int i = 0; i < elements.size(); i++) {
			UIElement element = elements.get(i);
			if (element.isFocused() || !element.needFocus()) {
				boolean temp = element.keyReleased(key, mods, ctrl, shift, alt, os, capLock, numLock);
				if (temp) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Pushes a scroll wheel event to all elements in the layer.
	 * 
	 * @param xoffset The x offset in unspecified units.
	 * @param yoffset The y offset in unspecified units. Main axis of a scroll
	 *                wheel.
	 * @return True if the event got blocked
	 */
	public boolean pushScrollWheel(float xoffset, float yoffset) {
		checkDelete();
		for (int i = 0; i < elements.size(); i++) {
			UIElement element = elements.get(i);
			boolean temp = element.scrollWheel(xoffset, yoffset);
			if (temp)
				return true;
		}
		return false;
	}

	public void add(UIElement element) {
		checkDelete();
		elements.add(element);
		element.setLayer(this);
	}

	private double current_time = 0;

	private double threshold = 1 / 60d;

	public void update(double delta) {
		checkDelete();
		current_time += delta;
		while (current_time >= threshold) {
			current_time -= threshold;
			for (int i = 0; i < elements.size(); i++) {
				elements.get(i).update();
			}
		}
	}

	public void pushRefresh() {
		checkDelete();
		for (int i = 0; i < elements.size(); i++) {
			elements.get(i).refresh();
		}
	}

	@Override
	public void delete() {
		checkDelete();
		stack = null;
		if (openGLMesh != null) {
			openGLMesh.delete();
			openGLMesh = null;
		}
		while (elements.size() > 0) {
			elements.get(0).delete();
			elements.remove(0);
		}
		deleted = true;
		mesh = false;
		super.delete();
	}

	private boolean deleted = false;

	private void checkDelete() {
		if (deleted) {
			Logging.log("Layer Deleted", "UILayer", LoggingLevel.ERR);
		}
	}
}
