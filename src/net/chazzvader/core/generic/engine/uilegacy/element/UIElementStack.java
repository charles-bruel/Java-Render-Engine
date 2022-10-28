package net.chazzvader.core.generic.engine.uilegacy.element;

import net.chazzvader.core.generic.engine.object.EngineObjectMesh;
import net.chazzvader.core.generic.engine.object.ui.EngineObjectUIStack;
import net.chazzvader.core.generic.engine.object.ui.EngineObjectUITexture;
import net.chazzvader.core.generic.engine.render.material.MaterialUILegacy;
import net.chazzvader.core.generic.engine.render.material.MaterialUILegacyStack;
import net.chazzvader.core.generic.engine.render.material.Texture;
import net.chazzvader.core.generic.engine.ui.ScalingBasis;
import net.chazzvader.core.generic.engine.ui.ScrollBarViewType;
import net.chazzvader.core.generic.engine.uilegacy.UIStack;
import net.chazzvader.core.generic.engine.uilegacy.anchorpoint.AnchorPoint;
import net.chazzvader.core.generic.math.Matrix4f;
import net.chazzvader.core.generic.math.Vector2f;
import net.chazzvader.core.generic.math.Vector3f;

/**
 * An UI that renders an entire separate UI stack to a texture and then
 * displayed that. Used for scrolling, more complex layouts and more.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 * @see EngineObjectUIStack
 * @see MaterialUILegacyStack
 * @see UIStack
 */
@Deprecated
public class UIElementStack extends UIElement {

	/**
	 * The size of the ui element with 1, 1 covering the entire screen. Does not
	 * maintain aspect ratio, just a raw proportion.
	 */
	public Vector2f size;

	/**
	 * The aspect ratio of the element. Remains constant, any extra space required
	 * is made by scrolling.
	 */
	public float aspectRatio;

	/**
	 * The ui stack contained in the element, what gets rendered.
	 */
	public UIStack childStack;

	/**
	 * The single color background of this element. You can create a more complex
	 * background with an <code>UIElementTexture</code> if you want.
	 */
	public Vector3f backgroundColor = new Vector3f(1, 1, 1);

	/**
	 * A ratio of the total height representing how scrolled it is. Auto bounded by
	 * {@link #recalculate()}.
	 */
	public float scroll;

	/**
	 * A ratio of the total height representing how far you can scroll. Auto
	 * calculated by {@link #recalculate()}.
	 */
	public float scrollMax;

	/**
	 * A ratio of the total height representing the lower bound of the texture,
	 * given the current scroll. Auto calculated by {@link #recalculate()}.
	 */
	public float lowerBound;

	/**
	 * A ratio of the total height representing the upper bound of the texture,
	 * given the current scroll. Auto calculated by {@link #recalculate()}.
	 */
	public float upperBound;

	/**
	 * The scroll bar type.
	 */
	public ScrollBarViewType scrollBar = ScrollBarViewType.INVISIBLE;

	/**
	 * A multiplier for scroll speed. Is negative so scroll works correctly<br>
	 * <br>
	 * Code for the {@link #scrollWheel(float, float)} event is as follows:<br>
	 * <code>scroll += yoffset * SCROLL_SPEED_MULTIPLIER;</code>
	 */
	public static final float SCROLL_SPEED_MULTIPLIER = -0.075f;
	
	/**
	 * The width of the scroll bar in pixels.
	 */
	public static final int SCROLL_BAR_WIDTH = 10;

	private EngineObjectUIStack object;
	private EngineObjectUITexture scrollBarObject;

	/**
	 * Final constructor, all constructors lead here.
	 * 
	 * @param anchorPoint     The anchor point.
	 * @param size            The size of the element.
	 * @param aspectRatio     The aspect ratio of the element.
	 * @param stack           The stack to draw.
	 * @param backgroundColor The background color of the element.
	 */
	public UIElementStack(AnchorPoint anchorPoint, Vector2f size, float aspectRatio, UIStack stack,
			Vector3f backgroundColor) {
		super(anchorPoint);
		this.size = size;
		this.aspectRatio = aspectRatio;
		this.childStack = stack;
		this.backgroundColor = backgroundColor;

		this.object = new EngineObjectUIStack(this);

		maintainAspectRatio = false;

		childStack.scalingBasis = ScalingBasis.WIDTH;
		
		scrollBarObject = new EngineObjectUITexture(Texture.BLANK, this);
		MaterialUILegacy temp = ((MaterialUILegacy) ((EngineObjectMesh) scrollBarObject).material);
		temp.color_multiplier = new Vector3f(0.5f, 0.5f, 0.5f);
		temp.adjustmentMatrix = getAdjustmentMatrix();
	}

	private Matrix4f getAdjustmentMatrix() {
		Vector2f currentSize = getAdjustedSize();
		float width = currentSize.x * getWindowWidth();
		
		float xScale = SCROLL_BAR_WIDTH / width;
		float xTrans = 0;
		switch(scrollBar) {
		case INVISIBLE:
			break;
		case LEFT:
			xTrans = -1 + xScale;
			break;
		case RIGHT:
			xTrans = 1 - xScale;
			break;
		}
		
		float yScale = upperBound - lowerBound;
		float yTrans = 1 - upperBound - lowerBound;
		
		return Matrix4f.translation(xTrans, yTrans, 0).applyScale(xScale, yScale, 1);
	}

	/**
	 * Constructor.
	 * 
	 * @param anchorPoint The anchor point.
	 * @param size        The size of the element.
	 * @param aspectRatio The aspect ratio of the element.
	 * @param stack       The stack to draw.
	 */
	public UIElementStack(AnchorPoint anchorPoint, Vector2f size, float aspectRatio, UIStack stack) {
		this(anchorPoint, size, aspectRatio, stack, new Vector3f(1, 1, 1));
	}

	/**
	 * Constructor.
	 * 
	 * @param anchorPoint The anchor point.
	 * @param aspectRatio The aspect ratio of the element.
	 * @param stack       The stack to draw.
	 */
	public UIElementStack(AnchorPoint anchorPoint, float aspectRatio, UIStack stack) {
		this(anchorPoint, new Vector2f(0.5f, 0.5f), aspectRatio, stack);
	}

	/**
	 * Constructor.
	 * 
	 * @param anchorPoint The anchor point.
	 * @param stack       The stack to draw.
	 */
	public UIElementStack(AnchorPoint anchorPoint, UIStack stack) {
		this(anchorPoint, 1, stack);
	}

	/**
	 * Constructor. Provides a stack. Get with <code>.childStack</code>
	 * 
	 * @param anchorPoint The anchor point.
	 */
	public UIElementStack(AnchorPoint anchorPoint) {
		this(anchorPoint, new UIStack(1, 1, 2, false));
	}

	@Override
	public void update() {
		recalculate();

		childStack.update(1 / 60f);
		
		MaterialUILegacy temp = ((MaterialUILegacy) ((EngineObjectMesh) scrollBarObject).material);
		temp.adjustmentMatrix = getAdjustmentMatrix();

		super.update();
	}

	/**
	 * Recalculates many values, called every frame and on scroll event, etc by
	 * default. Not much point to calling it more.
	 */
	public void recalculate() {
		if (scroll < 0) {
			scroll = 0;
		}
		if (scroll > scrollMax) {
			scroll = scrollMax;
		}

		Vector2f currentSize = getAdjustedSize();
		float width = currentSize.x * getWindowWidth();
		float height = currentSize.y * getWindowHeight();
		float framebufferHeight = width * aspectRatio;

		scrollMax = (framebufferHeight - height) / framebufferHeight;

		lowerBound = scroll * framebufferHeight;
		upperBound = lowerBound + height;

		lowerBound /= framebufferHeight;
		upperBound /= framebufferHeight;
	}

	@Override
	public EngineObjectMesh getObject() {
		return object;
	}

	@Override
	public Vector2f getBaseSize() {
		return size;
	}

	@Override
	public void refresh() {

	}
	
	@Override
	public void render() {
		super.render();
		if(scrollBar != ScrollBarViewType.INVISIBLE) {
			scrollBarObject.render();
		}
	}

	@Override
	public boolean updateMouseAbsolutePosition(int x, int y) {
		super.updateMouseAbsolutePosition(x, y);

		Vector2f currentSize = getAdjustedSize();
		float width = currentSize.x * getWindowWidth();
		float height = currentSize.y * getWindowHeight();

		float relativeX = mouseX;
		float relativeY = mouseY;

		float framebufferHeight = width * aspectRatio;

		relativeX *= width;
		relativeY *= height;
		relativeY = height - relativeY;

		relativeY += lowerBound * framebufferHeight;

		childStack.pushUpdateMouseAbsolutePosition((int) relativeX, (int) relativeY);

		return false;
	}

	@Override
	public boolean updateMouseOffset(int x, int y) {
		super.updateMouseOffset(x, y);

		childStack.pushUpdateMouseOffset(x, y);

		return false;
	}

	@Override
	public boolean keyPressed(int key, int mods, boolean ctrl, boolean shift, boolean alt, boolean os, boolean capLock,
			boolean numLock) {
		childStack.pushKeyPressed(key, mods, ctrl, shift, alt, os, capLock, numLock);
		return false;
	}

	@Override
	public boolean keyReleased(int key, int mods, boolean ctrl, boolean shift, boolean alt, boolean os, boolean capLock,
			boolean numLock) {
		childStack.pushKeyReleased(key, mods, ctrl, shift, alt, os, capLock, numLock);
		return false;
	}

	@Override
	public boolean mouseButtonPressed(int button, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
			boolean capLock, boolean numLock, int clickCount) {
		if (mouseX > 0 && mouseY > 0 && mouseX < 1 && mouseY < 1) {
			childStack.pushMouseButtonPressed(button, mods, ctrl, shift, alt, os, capLock, numLock, clickCount);
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseButtonReleased(int button, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
			boolean capLock, boolean numLock) {
		childStack.pushMouseButtonReleased(button, mods, ctrl, shift, alt, os, capLock, numLock);
		return false;
	}

	@Override
	public boolean scrollWheel(float xoffset, float yoffset) {
		childStack.pushScrollWheel(xoffset, yoffset);
		scroll += yoffset * SCROLL_SPEED_MULTIPLIER;
		recalculate();
		return false;
	}

	@Override
	public boolean selectable() {
		return false;
	}

}
