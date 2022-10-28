package net.chazzvader.core.generic.engine.uilegacy.element.text;

import java.awt.Font;

import org.lwjgl.glfw.GLFW;

import net.chazzvader.core.generic.engine.Application;
import net.chazzvader.core.generic.engine.creator.ObjectCreator;
import net.chazzvader.core.generic.engine.mesh.Mesh;
import net.chazzvader.core.generic.engine.object.EngineObjectMesh;
import net.chazzvader.core.generic.engine.render.material.MaterialUILegacy;
import net.chazzvader.core.generic.engine.render.material.Texture;
import net.chazzvader.core.generic.engine.uilegacy.anchorpoint.AnchorPoint;
import net.chazzvader.core.generic.math.Matrix4f;
import net.chazzvader.core.generic.math.Vector2f;
import net.chazzvader.core.generic.math.Vector3f;
import net.chazzvader.core.generic.util.IKeyFilter;
import net.chazzvader.core.generic.util.KeyFilterWord;
import net.chazzvader.core.generic.util.TextUtils;
import net.chazzvader.core.generic.util.Utils;

@Deprecated
@SuppressWarnings("javadoc")
public class UIElementTextBox extends UIElementText {

	/**
	 * When holding down a key, the delay in frames (60 fps) between the first and
	 * second characters appearing on screen.
	 */
	public static final int DELAY_1_2 = 30;

	/**
	 * When holding down a key, the delay in frames (60 fps) between the second and
	 * subsequent characters appearing on screen.
	 */
	public static final int DELAY_2_3_ON = 3;

	/**
	 * Doesn't mean anything <em>directly</em>, just adjusted the width of the
	 * blinker.
	 */
	public static final float CONSTANT_OF_BLINKER_WIDTH = 30;

	/**
	 * Height of blinker relative to the text
	 */
	public static final float BLINKER_HEIGHT = 0.8f;

	/**
	 * The number of frame the blinker is visible/invisible.
	 */
	public static final int BLINKER_BLINK_TIME = 30;

	public static final float DEFAULT_BORDER = 0.005f;

	public int cursorIndex = 0;

	public int highlightIndex = 0;

	/**
	 * Color is black.<br>
	 * Takes a int pixel size instead a float ratio size.<br>
	 * Uses Arial a for the font.<br>
	 * Uses a default font size of 36 pixels.
	 * 
	 * @param anchorPoint The anchor point
	 */
	public UIElementTextBox(AnchorPoint anchorPoint) {
		this(anchorPoint, "");
	}

	/**
	 * Color is black.<br>
	 * Takes a int pixel size instead a float ratio size.<br>
	 * Uses Arial a for the font.<br>
	 * Uses a default font size of 36 pixels. Uses default width of 10% of the
	 * screen
	 * 
	 * @param anchorPoint The anchor point
	 * @param text        The text to start with
	 */
	public UIElementTextBox(AnchorPoint anchorPoint, String text) {
		this(anchorPoint, text, 36);
	}

	/**
	 * Color is black.<br>
	 * Takes a int pixel size instead a float ratio size.<br>
	 * Uses a default font size of 36 pixels. Uses default width of 10% of the
	 * screen
	 * 
	 * @param anchorPoint The anchor point
	 * @param text        The text to start with
	 * @param font        The font to use. <em>The given size in the font object is
	 *                    ignored!</em>
	 */
	public UIElementTextBox(AnchorPoint anchorPoint, String text, Font font) {
		this(anchorPoint, text, font, 36);
	}

	/**
	 * Color is black.<br>
	 * Takes a int pixel size instead a float ratio size.<br>
	 * Uses a default font size of 36 pixels. Uses default width of 10% of the
	 * screen
	 * 
	 * @param anchorPoint The anchor point
	 * @param font        The font to use. <em>The given size in the font object is
	 *                    ignored!</em>
	 */
	public UIElementTextBox(AnchorPoint anchorPoint, Font font) {
		this(anchorPoint, "", font, 36);
	}

	/**
	 * Color is black.<br>
	 * Takes a int pixel size instead a float ratio size.<br>
	 * Uses a default font size of 36 pixels.
	 * 
	 * @param anchorPoint The anchor point
	 * @param font        The font to use. <em>The given size in the font object is
	 *                    ignored!</em>
	 * @param width       The width of the text box, <em>relative to the current
	 *                    resolution.</em>
	 */
	public UIElementTextBox(AnchorPoint anchorPoint, Font font, float width) {
		this(anchorPoint, "", font, 2 * (float) 36 / (float) Application.getInstance().getWindow().getHeight(),
				width);
	}

	/**
	 * Color is black.<br>
	 * Takes a int pixel size instead a float ratio size.<br>
	 * Uses Arial a for the font and auto generates font size. Uses default width of
	 * 10% of the screen
	 * 
	 * @param anchorPoint The anchor point
	 * @param text        The text to start with
	 * @param size        The size of the text, in pixels, <em>relative to the
	 *                    current resolution.</em>
	 */
	public UIElementTextBox(AnchorPoint anchorPoint, String text, int size) {
		this(anchorPoint, text, "Arial", size);
	}

	/**
	 * Color is black.<br>
	 * Takes a int pixel size instead a float ratio size.<br>
	 * Uses just a name for the font and auto generates font size. Uses default
	 * width of 10% of the screen
	 * 
	 * @param anchorPoint The anchor point
	 * @param text        The text to start with
	 * @param font        The name of the font to use. <em>The given size in the font object is
	 *                    ignored!</em>
	 * @param size        The size of the text, in pixels, <em>relative to the
	 *                    current resolution.</em>
	 */
	public UIElementTextBox(AnchorPoint anchorPoint, String text, String font, int size) {
		this(anchorPoint, text, new Font(font, 0, size), size);
	}

	/**
	 * Color is black.<br>
	 * Takes a int pixel size instead a float ratio size. Uses default width of 10%
	 * of the screen
	 * 
	 * @param anchorPoint The anchor point
	 * @param text        The text to start with
	 * @param font        The font to use. <em>The given size in the font object is
	 *                    ignored!</em>
	 * @param size        The size of the text, in pixels, <em>relative to the
	 *                    current resolution.</em>
	 */
	public UIElementTextBox(AnchorPoint anchorPoint, String text, Font font, int size) {
		this(anchorPoint, text, font, 2 * (float) size / (float) Application.getInstance().getWindow().getHeight(),
				0.1f);
	}

	/**
	 * Color is black.<br>
	 * Takes a int pixel size instead a float ratio size.
	 * 
	 * @param anchorPoint The anchor point
	 * @param text        The text to start with
	 * @param font        The font to use. <em>The given size in the font object is
	 *                    ignored!</em>
	 * @param size        The size of the text, in pixels, <em>relative to the
	 *                    current resolution.</em>
	 * @param width       The width of the text box, <em>relative to the current
	 *                    resolution.</em>
	 */
	public UIElementTextBox(AnchorPoint anchorPoint, String text, Font font, float size, float width) {
		// TODO: Simpler constructor with width
		super(anchorPoint, text, font, size, new Vector3f(), width);
		Mesh quad = ObjectCreator.quad(new Vector3f(-1, -1, 0), new Vector3f(1, -1, 0), new Vector3f(-1, 1, 0),
				new Vector3f(1, 1, 0));
		blinkerMaterial = new MaterialUILegacy(Texture.BLANK, this);
		blinkerMaterial.color_multiplier = new Vector3f();
		blinker = new EngineObjectMesh(quad, blinkerMaterial);
		blinker.setParent(object);
		blinker.provider = this;
		object.material.border_size = DEFAULT_BORDER;
		object.material.background_color_multiplier = new Vector3f(1f, 1f, 1f);

		object.material.background = TextUtils.getTextBackgroundTexture(0, 0, (int) (size * getWindowHeight()),
				object.getAspectRatio());
		maintainAspectRatio = false;
	}

	private EngineObjectMesh blinker;
	private MaterialUILegacy blinkerMaterial;

	private int blinkerTick = 0;
	private float _start, _end;
	private boolean _focus;

	@Override
	public void update() {
		super.update();
		if (this.isFocused())
			doKeyInput();

		// Blinker
		// Material
		blinkerMaterial.adjustmentMatrix = getBlinkerAdjustmentMatrix();
		// Flashing
		blinkerTick++;
		if (blinkerTick == BLINKER_BLINK_TIME) {
			if (!isFocused()) {
				blinker.active = false;
			} else {
				blinker.active = !blinker.active;
			}
			blinkerTick = 0;
		}

		float start = determinePosRatio(highlightIndex);
		float end = determinePosRatio(cursorIndex);

		if (start > end) {
			float temp = start;
			start = end;
			end = temp;
		}

		if (!(start == _start && end == _end)) {
			_start = start;
			_end = end;
			object.updateBackgroundTexture(start, end, (int) (size * getWindowHeight()), object.getAspectRatio());
		}
		if (holdingDownMouseButton) {
			if (mouseX < 0 || mouseX > 1 || mouseY < 0 || mouseY > 1) {
				holdingDownMouseButton = false;
			}
			if (clickCount == 1) {
				highlightIndex = positionCursor(mouseX);
			} else if (clickCount == 2) {
				doubleClickSelect(cursorIndex, positionCursor(mouseX));
			} else {
				cursorIndex = 0;
				highlightIndex = text.length();
			}
		}
		boolean tmp = isFocused();
		if (tmp != _focus) {
			_focus = tmp;
			if (_focus) {
				object.material.border_color = SELECTED_COLOR;
			} else {
				object.material.border_color = UNSELECTED_COLOR;
			}
		}

	}

	private void doubleClickSelect(int a, int b) {
		char[] text = this.text.toCharArray();
		if(text.length == 0) {
			return;
		}
		int lower = a <= b ? a : b;
		int higher = a > b ? a : b;
		IKeyFilter filter = new KeyFilterWord();
		if (higher > text.length - 1) {
			higher = text.length - 1;
		}
		if (lower > text.length - 1) {
			lower = text.length - 1;
		}
		if (higher < 0) {
			higher = 0;
		}
		if (lower < 0) {
			lower = 0;
		}
		while (filter.valid(text[lower])) {
			lower--;
			if (lower == -1) {
				break;
			}
		}
		lower++;
		while (filter.valid(text[higher])) {
			higher++;
			if (higher == text.length) {
				break;
			}
		}
		if (cursorIndex <= highlightIndex) {
			cursorIndex = lower;
			highlightIndex = higher;
		}
		if (cursorIndex > highlightIndex) {
			cursorIndex = higher;
			highlightIndex = lower;
		}
	}

	@Override
	public void render() {
		object.render();
		if (blinker.active)
			blinker.render();
	}

	private void doKeyInput() {
		for (int i = 0; i < keys.length; i++) {
			key_ticks[i]++;
			if (key_ticks[i] == DELAY_2_3_ON) {// Standard
				key_ticks[i] = 0;
				key(i);
			}
			if (key_ticks[i] == DELAY_2_3_ON + 1) {// First key press.
				// Intended lack of key_ticks[i] = 0;
				key(i);
			}
			if (key_ticks[i] == DELAY_2_3_ON + DELAY_1_2) {// Second key press.
				key_ticks[i] = 0;
				key(i);
			}
		}

	}

	private int test = 720;

	private Matrix4f getBlinkerAdjustmentMatrix() {
		float adjustedBlinkerWidth = CONSTANT_OF_BLINKER_WIDTH * 1920 / getWindowWidth() * size;
		float offset = (getBaseSize().x * test) / adjustedBlinkerWidth;
		float width = adjustedBlinkerWidth / (getBaseSize().x * test);
		return Matrix4f.scale(new Vector3f(width, BLINKER_HEIGHT, 1))
				.applyTranslation(-offset + determinePosRatio(cursorIndex) * offset * 2, 0, 0);
	}

	private float _blinkerPosRatio;

	private float determinePosRatio(int index) {
		TextUtils.setFont(font);
		String toLeft = text.substring(0, index);
		float textWidth = textWidth(toLeft);
		switch (textAlignment) {
		case CENTER:
			textWidth = 0.5f - textWidth(text) / 2 + textWidth;
			break;
		case LEFT:// Default
			break;
		case RIGHT:
			textWidth = 1 - textWidth(text) + textWidth;
			break;
		}
		_blinkerPosRatio = textWidth;
		return adjustForBorder(_blinkerPosRatio);
	}

	private float textWidth(String s) {
		// Bad practice I know, easiest way to make it take into account trailing
		// whitespace
		float val = (float) TextUtils.getBounds(s + "|").getWidth() - (float) TextUtils.getBounds("|").getWidth();
//		float ratio = ((getBaseSize().y / 2) * getWindowHeight()) / TextUtils.CONSTANT_FONT_HEIGHT;
//		float pixelHeight = (val * ratio);
//		float tmp = pixelHeight / getWindowHeight();
//		return tmp * 2 / (maxWidth * 2);
//		System.out.println();
//		System.out.println((getAdjustedSize().x * getWindowWidth()));
//		System.out.println(val);
//		System.out.println(val / (getAdjustedSize().x * getWindowWidth()));
		val /= TextUtils.CONSTANT_FONT_HEIGHT;
		val *= getAdjustedSize().y * getWindowHeight() * (1-DEFAULT_BORDER*2);
		return val / (getAdjustedSize().x * getWindowWidth());
	}

	private float adjustForBorder(float val) {
		float border = object.material.border_size;
		val *= (1 + border);
		val -= border / 2;
		return val;
	}

	private void replaceSelection(String toReplace) {
		if (highlightIndex != cursorIndex) {
			int lower = (highlightIndex < cursorIndex) ? highlightIndex : cursorIndex;
			int upper = (highlightIndex > cursorIndex) ? highlightIndex : cursorIndex;

			String left = text.substring(0, lower);
			String right = text.substring(upper, text.length());
			text = left + toReplace + right;
			cursorIndex = lower + toReplace.length();
			highlightIndex = cursorIndex;
		}
	}

	private void append(String toAppend) {
		if (highlightIndex == cursorIndex) {
			text = text.substring(0, cursorIndex) + toAppend + text.substring(cursorIndex, text.length());
			cursorIndex += toAppend.length();
			highlightIndex = cursorIndex;
		}
	}

	private void key(int index) {// TODO: Should probably clean that up at some point.
		if (keys[index]) {
			if (!ctrl) {
				switch (index) {
				case GLFW.GLFW_KEY_BACKSPACE:// Backspace (regular delete, deletes to left)
					if (text.length() > 0) {
						if (highlightIndex == cursorIndex) {
							if (cursorIndex != 0) {
								String before, after = "";
								before = text.substring(0, cursorIndex - 1);
								if (!(cursorIndex == text.length() + 1)) {
									after = text.substring(cursorIndex, text.length());
								}
								text = before + after;
								cursorIndex--;
								highlightIndex = cursorIndex;
							}
						} else {
							replaceSelection("");
						}
					}
					break;
				case GLFW.GLFW_KEY_DELETE:// Delete (deletes to right)
					if (text.length() > 0) {
						if (highlightIndex == cursorIndex) {
							if (cursorIndex != text.length()) {
								String before, after = "";
								before = text.substring(0, cursorIndex);
								after = text.substring(cursorIndex + 1, text.length());
								text = before + after;
							}
						} else {
							replaceSelection("");
						}
					}
					break;
				case GLFW.GLFW_KEY_LEFT:// Left arrow
					if (!shift) {
						if (highlightIndex == cursorIndex) {
							cursorIndex--;
							if (cursorIndex < 0) {
								cursorIndex = 0;
							}
						}
						highlightIndex = cursorIndex;
					} else {
						highlightIndex--;
						if (highlightIndex < 0) {
							highlightIndex = 0;
						}
					}
					break;
				case GLFW.GLFW_KEY_RIGHT:// Right arrow
					if (!shift) {
						if (highlightIndex == cursorIndex) {
							cursorIndex++;
							if (cursorIndex > text.length()) {
								cursorIndex = text.length();
							}
						}
						highlightIndex = cursorIndex;
					} else {
						highlightIndex++;
						if (highlightIndex > text.length()) {
							highlightIndex = text.length();
						}
					}
					break;
				default:// Regular key press
					String toAdd = TextUtils.glfwKeycodeToChar(index, shift, caps);
					if (highlightIndex == cursorIndex) {
						append(toAdd);
					} else {
						if (toAdd.length() != 0) {
							replaceSelection(toAdd);
						}
					}
				}
			} else {
				switch (index) {
				case GLFW.GLFW_KEY_C:// Copy
					if (!(highlightIndex == cursorIndex)) {
						int lower = (highlightIndex < cursorIndex) ? highlightIndex : cursorIndex;
						int upper = (highlightIndex > cursorIndex) ? highlightIndex : cursorIndex;
						String toCopy = text.substring(lower, upper);
						Utils.setClipboardContents(toCopy);
					}
					break;
				case GLFW.GLFW_KEY_X:// Cut
					if (!(highlightIndex == cursorIndex)) {
						int lower = (highlightIndex < cursorIndex) ? highlightIndex : cursorIndex;
						int upper = (highlightIndex > cursorIndex) ? highlightIndex : cursorIndex;
						String toCopy = text.substring(lower, upper);
						Utils.setClipboardContents(toCopy);
						String left = text.substring(0, lower);
						String right = text.substring(upper, text.length());
						text = left + right;
						cursorIndex = lower;
						highlightIndex = cursorIndex;
					}
					break;
				case GLFW.GLFW_KEY_V:// Paste
					String toAdd = Utils.getClipboardContents();
					if (highlightIndex == cursorIndex) {
						append(toAdd);
					} else {
						if (toAdd.length() != 0) {
							replaceSelection(toAdd);
						}
					}
					break;
				}
			}
		}
	}

	private boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST + 1];
	private short[] key_ticks = new short[GLFW.GLFW_KEY_LAST + 1];

	private boolean shift, caps, ctrl;

	private boolean holdingDownMouseButton = false;

	private int positionCursor(float x) {
		float border = object.material.border_size;
		x += border / 2;
		x /= (1 + border);

		return getIndexFromX(x);
	}

	private int getIndexFromX(float x) {
		float[] values = new float[text.length() + 1];
		values[0] = 0;

		for (int i = 1; i < values.length; i++) {
			values[i] = textWidth(text.substring(0, i));
		}

		for (int i = 1; i < values.length; i++) {
			if (x <= values[i] && x >= values[i - 1]) {
				return i - 1;
			}
		}
		return values.length - 1;
	}

	private int clickCount = 1;

	@Override
	public boolean mouseButtonPressed(int button, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
			boolean capLock, boolean numLock, int clickCount) {
		this.clickCount = clickCount;
		cursorIndex = positionCursor(mouseX);
		highlightIndex = cursorIndex;
		holdingDownMouseButton = true;
		return true;
	}

	@Override
	public boolean mouseButtonReleased(int button, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
			boolean capLock, boolean numLock) {
		holdingDownMouseButton = false;
		return false;
	}

	@Override
	public boolean keyPressed(int key, int mods, boolean ctrl, boolean shift, boolean alt, boolean os, boolean capLock,
			boolean numLock) {
		if (key == GLFW.GLFW_KEY_ESCAPE) {
			getLayer().getStack().focus = null;// De-focus on escape
		}
		keys[key] = true;
		this.shift = shift;
		this.caps = capLock;
		this.ctrl = ctrl;
		key_ticks[key] = DELAY_2_3_ON;
		return true;
	}

	@Override
	public boolean keyReleased(int key, int mods, boolean ctrl, boolean shift, boolean alt, boolean os, boolean capLock,
			boolean numLock) {
		keys[key] = false;
		this.shift = shift;
		this.caps = capLock;
		this.ctrl = ctrl;
		clickCount = 1;
		return false;
	}

	@Override
	protected void triggeredUpdate() {
		super.triggeredUpdate();
	}

	@Override
	public Vector2f getBaseSize() {
//		float pxWidth = maxWidth * 2f * getWindowHeight() * 16f / 9f;
//		float width = pxWidth / getWindowWidth();
		return new Vector2f(maxWidth, size);
	}

	@Override
	public boolean needFocus() {
		return true;
	}
	
	@Override
	public boolean selectable() {
		return true;
	}

}