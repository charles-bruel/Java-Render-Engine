package net.chazzvader.core.generic.engine.uilegacy;

import java.util.ArrayList;

import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.EngineItem;
import net.chazzvader.core.generic.engine.IDeletable;
import net.chazzvader.core.generic.engine.IRenderContextProvider;
import net.chazzvader.core.generic.engine.event.EventManager;
import net.chazzvader.core.generic.engine.event.type.IEventHandlerKey;
import net.chazzvader.core.generic.engine.event.type.IEventHandlerMouseButton;
import net.chazzvader.core.generic.engine.event.type.IEventHandlerMouseMoved;
import net.chazzvader.core.generic.engine.event.type.IEventHandlerScroll;
import net.chazzvader.core.generic.engine.event.type.IEventHandlerWindowResized;
import net.chazzvader.core.generic.engine.render.RenderContext;
import net.chazzvader.core.generic.engine.ui.ScalingBasis;
import net.chazzvader.core.generic.engine.uilegacy.element.UIElement;

@Deprecated
@SuppressWarnings("javadoc")
public class UIStack extends EngineItem implements IDeletable, IRenderContextProvider {

	/**
	 * The layers this stack contains. Try not to edit these manually.
	 */
	public ArrayList<UILayer> layers = new ArrayList<>();

	public ScalingBasis scalingBasis = ScalingBasis.HEIGHT;

	private int numLayers;
	private int minLayers;

	public UIElement focus = null;

	public UIStack(int numLayers, int minLayers, int inputPriority, boolean attachDefaultEventListener) {
		if (minLayers < 1)
			minLayers = 1;
		this.numLayers = numLayers;
		this.minLayers = minLayers;
		for (int i = 0; i < numLayers; i++) {
			layers.add(new UILayer(this));
		}
		if(attachDefaultEventListener) {
			EventManager.registerEventListener(new UIInputHandler(), inputPriority);
		}
	}
	
	public UIStack(int numLayers, int minLayers, int inputPriority) {
		this(numLayers, minLayers, inputPriority, true);
	}

	public UIStack(int numLayers, int inputPriority) {
		this(numLayers, 1, inputPriority);
	}

	public UIStack(int numLayers) {
		this(numLayers, 1, 2);
	}

	public UILayer getTopLayer() {
		checkDelete();
		return layers.get(layers.size() - 1);
	}

	public void pushLayer(UILayer layer) {
		checkDelete();
		layers.add(layer);
	}

	public void popLayer() {
		checkDelete();
		if (layers.size() > minLayers)
			layers.remove(layers.size() - 1).delete();
	}

	public ArrayList<UILayer> getLayers() {
		checkDelete();
		return layers;
	}

	public int getNumLayers() {
		checkDelete();
		return numLayers;
	}

	public int getMinLayers() {
		checkDelete();
		return minLayers;
	}

	public void render() {
		checkDelete();
		for (int i = 0; i < layers.size(); i++) {
			layers.get(i).render();
		}
	}

	public void update(double delta) {
		checkDelete();
		for (int i = 0; i < layers.size(); i++) {
			layers.get(i).update(delta);
		}
	}

	public boolean pushUpdateMouseAbsolutePosition(int x, int y) {
		checkDelete();
		for (int i = layers.size() - 1; i >= 0; i--) {
			boolean temp = layers.get(i).pushUpdateMouseAbsolutePosition(x, y);
			if (temp)
				return true;
			if (layers.get(i).lockBehind) {
				return false;
			}
		}
		return false;
	}

	public boolean pushUpdateMouseOffset(int x, int y) {
		checkDelete();
		for (int i = layers.size() - 1; i >= 0; i--) {
			boolean temp = layers.get(i).pushUpdateMouseOffset(x, y);
			if (temp)
				return true;
			if (layers.get(i).lockBehind) {
				return false;
			}
		}
		return false;
	}

	/**
	 * Pushes a mouse button pressed event to all layers in the stack.
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
		for (int i = layers.size() - 1; i >= 0; i--) {
			boolean temp = layers.get(i).pushMouseButtonPressed(button, mods, ctrl, shift, alt, os, capLock, numLock,
					clickCount);
			if (temp)
				return true;
			if (layers.get(i).lockBehind) {
				return false;
			}
		}
		return false;
	}

	/**
	 * Pushes a mouse button released event to all layers in the stack.
	 * 
	 * @param button  The mouse button code.
	 * @param mods    Any key mods, contain if modifier keys are down (shift, ctrl).
	 *                Already separated out in different parameters for your
	 *                convenience.
	 * @param ctrl    Is the control key held down.
	 * @param shift   Is the shift key held down.
	 * @param alt     Is the alt key held down.
	 * @param os      Is the os-specific key held down. I believe this is the window
	 *                key on windows and command on macOS.
	 * @param capLock Is the caps lock key on.
	 * @param numLock Is the num lock key on.
	 * @return True if the event got blocked
	 */
	public boolean pushMouseButtonReleased(int button, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
			boolean capLock, boolean numLock) {
		checkDelete();
		for (int i = layers.size() - 1; i >= 0; i--) {
			boolean temp = layers.get(i).pushMouseButtonReleased(button, mods, ctrl, shift, alt, os, capLock, numLock);
			if (temp)
				return true;
			if (layers.get(i).lockBehind) {
				return false;
			}
		}
		return false;
	}

	public boolean pushKeyPressed(int key, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
			boolean capLock, boolean numLock) {
		checkDelete();
		for (int i = layers.size() - 1; i >= 0; i--) {
			boolean temp = layers.get(i).pushKeyPressed(key, mods, ctrl, shift, alt, os, capLock, numLock);
			if (temp)
				return true;
			if (layers.get(i).lockBehind) {
				return false;
			}
		}
		return false;
	}

	public boolean pushKeyReleased(int key, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
			boolean capLock, boolean numLock) {
		checkDelete();
		for (int i = layers.size() - 1; i >= 0; i--) {
			boolean temp = layers.get(i).pushKeyReleased(key, mods, ctrl, shift, alt, os, capLock, numLock);
			if (temp)
				return true;
			if (layers.get(i).lockBehind) {
				return false;
			}
		}
		return false;
	}

	/**
	 * Pushes a scroll wheel event to all layers in the stack.
	 * @param xoffset The x offset in unspecified units.
	 * @param yoffset The y offset in unspecified units. Main axis of a scroll wheel.
	 * @return True if the event got blocked
	 */
	public boolean pushScrollWheel(float xoffset, float yoffset) {
		checkDelete();
		for (int i = layers.size() - 1; i >= 0; i--) {
			boolean temp = layers.get(i).pushScrollWheel(xoffset, yoffset);
			if (temp)
				return true;
			if (layers.get(i).lockBehind) {
				return false;
			}
		}
		return false;
	}
	
	public void pushRefresh() {
		checkDelete();
		for (int i = 0; i < layers.size(); i++) {
			layers.get(i).pushRefresh();
		}
	}

	private class UIInputHandler implements IEventHandlerKey, IEventHandlerMouseButton, IEventHandlerMouseMoved,
			IEventHandlerWindowResized, IEventHandlerScroll {

		@Override
		public boolean updateMouseAbsolutePosition(int x, int y) {
			return pushUpdateMouseAbsolutePosition(x, y);
		}

		@Override
		public boolean updateMouseOffset(int x, int y) {
			return pushUpdateMouseOffset(x, y);
		}

		@Override
		public boolean mouseButtonPressed(int button, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
				boolean capLock, boolean numLock, int clickCount) {
			return pushMouseButtonPressed(button, mods, ctrl, shift, alt, os, capLock, numLock, clickCount);
		}

		@Override
		public boolean mouseButtonReleased(int button, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
				boolean capLock, boolean numLock) {
			return pushMouseButtonReleased(button, mods, ctrl, shift, alt, os, capLock, numLock);
		}

		@Override
		public boolean keyPressed(int key, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
				boolean capLock, boolean numLock) {
			return pushKeyPressed(key, mods, ctrl, shift, alt, os, capLock, numLock);
		}

		@Override
		public boolean keyReleased(int key, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
				boolean capLock, boolean numLock) {
			return pushKeyReleased(key, mods, ctrl, shift, alt, os, capLock, numLock);
		}

		@Override
		public boolean windowResized(int width, int height) {
			pushRefresh();
			return false;
		}

		@Override
		public boolean scrollWheel(float xoffset, float yoffset) {
			return pushScrollWheel(xoffset, yoffset);
		}

	}

	@Override
	public void delete() {
		checkDelete();
		while (layers.size() > 0) {
			layers.get(0).delete();
			layers.remove(0);
		}
		layers = null;
		focus = null;
		deleted = true;
		super.delete();
	}

	private boolean deleted = false;

	private void checkDelete() {
		if (deleted) {
			Logging.log("UI Deleted", "UIStack", LoggingLevel.ERR);
		}
	}

	private RenderContext overrideRenderContext = null;
	
	@Override
	public RenderContext getRenderContext() {
		if(overrideRenderContext == null) {
			return getWindow().getRenderPipeline().getUIContext();
		}
		return overrideRenderContext;
	}

	public void setOverrideRenderContext(RenderContext overrideRenderContext) {
		this.overrideRenderContext = overrideRenderContext;
	}

}