package net.chazzvader.core.generic.engine.uilegacy.element;

import java.util.ArrayList;

import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.EngineItemRenderable;
import net.chazzvader.core.generic.engine.IDeletable;
import net.chazzvader.core.generic.engine.IRenderContextProvider;
import net.chazzvader.core.generic.engine.object.EngineObject;
import net.chazzvader.core.generic.engine.object.EngineObjectMesh;
import net.chazzvader.core.generic.engine.ui.Alignment;
import net.chazzvader.core.generic.engine.ui.ScalingBasis;
import net.chazzvader.core.generic.engine.uilegacy.UILayer;
import net.chazzvader.core.generic.engine.uilegacy.UIStack;
import net.chazzvader.core.generic.engine.uilegacy.anchorpoint.AnchorPoint;
import net.chazzvader.core.generic.math.Matrix4f;
import net.chazzvader.core.generic.math.Vector2f;
import net.chazzvader.core.generic.math.Vector3f;
import net.chazzvader.core.generic.math.Vector4f;

/**
 * The basic form of any aspect of the ui rendered to the screen. Comes with no
 * default usage, and requires an implementation to do anything.<br>
 * Comes with a lot of default behavior for event management, rendering, custom
 * sizing options, and more.
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
@Deprecated
public abstract class UIElement extends EngineItemRenderable implements IDeletable, IRenderContextProvider {

	/*
	 * SIZING BASICS
	 */
	/**
	 * The alignment of the element. More detailed explanation in Alignment.
	 * 
	 * @see Alignment
	 */
	public Alignment alignment = Alignment.CENTER;

	/**
	 * The anchor point this element is attached to
	 */
	public AnchorPoint anchorPoint;

	/**
	 * Returns the size of the ui element with 1, 1 covering the entire screen.
	 * 
	 * @return The size of the element
	 */
	public Vector2f getAdjustedSize() {
		return adjustSize(getBaseSize());
	}

	/**
	 * The position in screen space of the element, equivalent to
	 * <code>anchorPoint.getPosition()</code>
	 * 
	 * @see AnchorPoint
	 * @see AnchorPoint#getPosition()
	 * @return The position in screen space
	 */
	public Vector2f getPosition() {
		return anchorPoint.getPosition();
	}

	/*
	 * SIZING FLAGS
	 */
	/**
	 * True if the size given is in pixels. Will use standard size of 1920x1080p and
	 * adjust to other sizes according.<br>
	 * <br>
	 * Defaults to false.
	 */
	public boolean sizeAbsolute = false;

	/**
	 * Only applies if {@link #sizeAbsolute} is true. Should the element size be
	 * shrinked to fit a smaller screen, and if so what proportion of the screen can
	 * the element take up before the effect applies. <br>
	 * <br>
	 * Set to 0 to disable.<br>
	 * Defaults to 0.
	 * 
	 * @see UIElement#sizeAbsolute
	 */
	public float sizeSquish = 0;

	/**
	 * True if the element should maintain its aspect ration if the window is
	 * resized.<br>
	 * <br>
	 * Defaults to true.
	 */
	public boolean maintainAspectRatio = true;

	/*
	 * STATICS
	 */

	/**
	 * The standard color for unselected borders.
	 */
	public static Vector3f UNSELECTED_COLOR = new Vector3f(0.48f, 0.48f, 0.48f);

	/**
	 * The standard color for selected borders.
	 */
	public static Vector3f SELECTED_COLOR = new Vector3f(0.25f, 0.48f, 0.67f);

	/*
	 * UI SYSTEM
	 */
	private UILayer layer;

	/**
	 * Returns the layer this element is a part of.
	 * 
	 * @return The layer
	 */
	public UILayer getLayer() {
		return this.layer;
	}

	/**
	 * Sets the layer this element is a part of.
	 * 
	 * @param layer The new layer.
	 */
	public void setLayer(UILayer layer) {
		if (this.layer != null) {
			this.layer.elements.remove(this);
		}
		this.layer = layer;
		layer.add(this);
		provider = layer.getStack();
	}

	/*
	 * CONSTRUCTOR
	 */
	/**
	 * Basic constructor, only assigns the anchor point.
	 * 
	 * @param anchorPoint The anchor point
	 */
	public UIElement(AnchorPoint anchorPoint) {
		this.anchorPoint = anchorPoint;
		provider = getWindow().getRenderPipeline().getUIProvider();// Temp
	}

	/*
	 * GETTERS
	 */

	/**
	 * Returns the engine object to render from
	 * 
	 * @return The engine object
	 */
	public abstract EngineObjectMesh getObject();

	/**
	 * Gets the base size before a slew of adjustments.
	 * 
	 * @return The base size.
	 */
	public abstract Vector2f getBaseSize();

	/**
	 * Does this element need focus to receive events.
	 * 
	 * @return True if focus is required
	 */
	public boolean needFocus() {
		return false;
	}

	/**
	 * The mouse position, <em>relative to this element</em>.<br>
	 * The value will be between 0 and 1 if its on the element.
	 */
	public float mouseX;

	/**
	 * The mouse position, <em>relative to this element</em>.<br>
	 * The value will be between 0 and 1 if its on the element.
	 */
	public float mouseY;

	/**
	 * If this element is selectable, should you be able to focus on it, etc.
	 * 
	 * @return The selectability of this element.
	 */
	public boolean selectable() {
		return true;
	}

	/**
	 * Gets the stack this element is a part of.
	 * 
	 * @return The stack.
	 */
	public UIStack getStack() {
		if (getLayer() == null) {
			return null;
		}
		return getLayer().getStack();
	}

	/*
	 * RENDER LOGIC
	 */

	/**
	 * Gets the world matrix for transforming a -1,-1 to 1,1 square to the correct
	 * screen space coordinates.<br>
	 * Performs caching.
	 * 
	 * @return The world matrix.
	 */
	public Matrix4f getWorldMatrix() {
		if (getPosition().equals(cachedPos) && getAdjustedSize().equals(cachedSize)
				&& alignment.equals(cachedAligment)) {
			return cachedMatrix;
		} else {
			cachedPos = getPosition();
			cachedSize = getAdjustedSize();
			cachedAligment = alignment;
			cachedMatrix = _getWorldMatrix();
			return cachedMatrix;
		}
	}

	/**
	 * Renders the element
	 */
	public void render() {
		renderEngineObjectRecursive(getObject());
	}

	protected Matrix4f _getWorldMatrix() {
		Vector2f position = getPosition().copy();// BASE
		Vector2f size = getAdjustedSize();// BASE

		position.x += alignment.x * size.x * 0.5f;// HORIZONTAL ALIGNMENT
		position.y += alignment.y * size.y * 0.5f;// VERTICAL ALIGNMENT

		Matrix4f toReturn = new Matrix4f();

		toReturn.applyTranslation(new Vector3f(position, 0));
		toReturn.applyScale(new Vector3f(size.mulCopy(0.5f), 1));

		return toReturn;
	}

	private static void renderEngineObjectRecursive(EngineObject object) {
		if (object == null) {
			return;
		}
		object.render();
		ArrayList<EngineObject> children = object.children;
		for (int i = 0; i < children.size(); i++) {
			if (children.get(i).isActive())
				renderEngineObjectRecursive(children.get(i));
		}
	}

	/*
	 * UPDATE
	 */

	/**
	 * Called every frame, use for updating things. Fixed timestep, called 60 times
	 * per second.
	 */
	public void update() {
		getObject().update(1d / 60d);
	}

	/**
	 * Refreshes the element, typically after some change, such as a window resize.
	 */
	public abstract void refresh();

	/*
	 * MISC UTIL METHODS
	 */

	/**
	 * Is this element the focus of the layer the element is a part of?
	 * 
	 * @return True if the element is focused.
	 */
	public boolean isFocused() {
		return layer.getStack().focus == this;
	}

	protected Vector2f adjustSize(Vector2f size) {
		float tempX = size.x;
		float tempY = size.y;
		if (maintainAspectRatio) {
			if (getStack() == null) {
				tempX /= getWindowAspectRatio();
			} else {
				ScalingBasis scalingBasis = getStack().scalingBasis;

				switch (scalingBasis) {
				case WIDTH:
					tempY /= 1 / getWindowAspectRatio();
					tempY /= 16f / 9f;
					tempX /= 16f / 9f;
					break;
				default:
				case HEIGHT:
					tempX /= getWindowAspectRatio();
					break;
				}
			}
		}
		if (!sizeAbsolute) {
			return new Vector2f(tempX, tempY);
		} else {
			float x = tempX * 1920 / getWindowWidth();
			float y = tempY * 1080 / getWindowHeight();

			if (sizeSquish != 0) {
				float maxX = sizeSquish;
				float maxY = sizeSquish;
				if (maxX <= x) {
					float ratio = x / maxX;
					x /= ratio;
					y /= ratio;
				}

				if (maxY <= y) {
					float ratio = y / maxY;
					x /= ratio;
					y /= ratio;
				}
			}

			return new Vector2f(x, y);
		}
	}

	/*
	 * EVENT LOGIC
	 */

	/**
	 * Are the following pixel coordinates on the screen within the bounds of the
	 * element.
	 * 
	 * @param x The x coordinate of the position.
	 * @param y The y coordinate of the position.
	 * @return True if the point falls witihin the element.
	 */
	public boolean within(int x, int y) {
		Vector2f coord = translateToRelativeCoordinates(x, y);
		return (coord.x >= 0 && coord.y >= 0 && coord.x <= 1 && coord.y <= 1);
	}

	/**
	 * Event, can be overloaded. Has some default behavior, which is unusual. Make
	 * sure when overloading to do a super call.<br>
	 * <br>
	 * Updates the mouse position, and also updates all derivatives, such as the
	 * relative mouse positions.<br>
	 * <br>
	 * Performs a similar function to
	 * {@link #translateToRelativeCoordinates(int, int)}.
	 * 
	 * @param x The x coordinate of the mouse position.
	 * @param y The y coordinate of the mouse position.
	 * @return True if the event was blocked.
	 */
	public boolean updateMouseAbsolutePosition(int x, int y) {
		mouseX = x;
		mouseY = y;
		relativeMouseX = ((float) x / getWindowWidth());
		relativeMouseY = 1 - ((float) y / getWindowHeight());

		Vector4f temp = new Vector4f(relativeMouseX, relativeMouseY, 0, 1);
		temp = getWorldMatrix().inverseCopy().mul(temp);

		mouseX = temp.x;
		mouseY = temp.y;

		mouseX = (mouseX + 1) / 2;
		mouseY = (mouseY + 1) / 2;

		if (mouseX < 0 || mouseX > 1 || mouseY < 0 || mouseY > 1) {
			if (in) {
				mouseLeaves();
			}
		} else {
			if (!in) {
				mouseEnters();
			}
		}

		return false;
	}

	/**
	 * Translates a pixel coordinate to coordinates relative to the element.
	 * 
	 * @param x The x coordinate of the position.
	 * @param y The y coordinate of the position.
	 * @return The relative position, 0,0 to 1,1 if the given position falls on the
	 *         element.
	 */
	public Vector2f translateToRelativeCoordinates(int x, int y) {
		float tmpX = ((float) x / getWindowWidth());
		float tmpY = 1 - ((float) y / getWindowHeight());

		Vector4f temp = new Vector4f(tmpX, tmpY, 0, 1);
		temp = getWorldMatrix().inverseCopy().mul(temp);
		float retX = temp.x;
		float retY = temp.y;

		retX = (retX + 1) / 2;
		retY = (retY + 1) / 2;

		return new Vector2f(retX, retY);
	}

	/**
	 * Event to update the offset of the mouse from the previous position.
	 * 
	 * @param x The x offset.
	 * @param y The y offset.
	 * @return True if the event got blocked.
	 */
	public boolean updateMouseOffset(int x, int y) {
		return false;
	}

	/**
	 * Mouse button pressed event.
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
	public boolean mouseButtonPressed(int button, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
			boolean capLock, boolean numLock, int clickCount) {
		return false;
	}

	/**
	 * Mouse button released event.
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
	public boolean mouseButtonReleased(int button, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
			boolean capLock, boolean numLock) {
		return false;
	}

	/**
	 * Key button pressed event.
	 * 
	 * @param key     The key code.
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
	public boolean keyPressed(int key, int mods, boolean ctrl, boolean shift, boolean alt, boolean os, boolean capLock,
			boolean numLock) {
		return false;
	}
	
	/**
	 * Key button released event.
	 * 
	 * @param key     The key code.
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
	public boolean keyReleased(int key, int mods, boolean ctrl, boolean shift, boolean alt, boolean os, boolean capLock,
			boolean numLock) {
		return false;
	}

	/**
	 * Scroll wheel event.
	 * 
	 * @param xoffset The x offset in unspecified units.
	 * @param yoffset The y offset in unspecified units. Main axis of a scroll
	 *                wheel.
	 * @return True if the event got blocked
	 */
	public boolean scrollWheel(float xoffset, float yoffset) {
		return false;
	}

	/**
	 * Basic event for when the mouse enters the element.
	 */
	public void mouseLeaves() {

	}
	
	/**
	 * Basic event for when the mouse leaves the element.
	 */
	public void mouseEnters() {

	}

	/*
	 * DELETE LOGIC
	 */

	@Override
	public void delete() {
		checkDelete();
		deleted = true;
		getObject().delete();
		super.delete();
	}

	private boolean deleted = false;

	private void checkDelete() {
		if (deleted) {
			Logging.log("Element Deleted", "UIElement", LoggingLevel.ERR);
		}
	}

	/*
	 * PRIVATE INTERNAL LOGIC VARIABLES
	 */
	private Vector2f cachedPos;
	private Vector2f cachedSize;
	private Alignment cachedAligment;

	private Matrix4f cachedMatrix;

	protected int absoluteMouseX, absoluteMouseY;// Absolute
	protected float relativeMouseX, relativeMouseY;// Relative to screen

	private boolean in = false;
}
