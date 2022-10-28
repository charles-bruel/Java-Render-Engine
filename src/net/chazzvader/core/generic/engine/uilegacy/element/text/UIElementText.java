package net.chazzvader.core.generic.engine.uilegacy.element.text;

import java.awt.Font;
import java.awt.geom.Rectangle2D;

import net.chazzvader.core.generic.engine.object.EngineObjectMesh;
import net.chazzvader.core.generic.engine.object.text.EngineObjectTextUI;
import net.chazzvader.core.generic.engine.uilegacy.anchorpoint.AnchorPoint;
import net.chazzvader.core.generic.engine.uilegacy.element.UIElement;
import net.chazzvader.core.generic.math.Vector2f;
import net.chazzvader.core.generic.math.Vector3f;
import net.chazzvader.core.generic.util.TextUtils;

@Deprecated
@SuppressWarnings("javadoc")
public class UIElementText extends UIElement {

	/**
	 * Text to draw
	 */
	public String text;

	/**
	 * Font to draw with
	 */
	public Font font;

	public TextAlignment textAlignment = TextAlignment.LEFT;
	
	/**
	 * Color of the text
	 */
	public Vector3f color = new Vector3f(0, 0, 0);

	public float getMaxWidth() {
		return this.maxWidth;
	}

	protected EngineObjectTextUI object;
	protected float size;

//	/**
//	 * Color is black.<br>
//	 * Takes a int pixel size instead a float ratio size.<br>
//	 * Uses Arial a for the font.<br>
//	 * Uses a default font size of 36 pixels.
//	 * 
//	 * @param anchorPoint The anchor point
//	 * @param text        The text to draw
//	 */
//	public UIElementText(AnchorPoint anchorPoint, String text, float width) {
//		this(anchorPoint, text, 36, width);
//	}
//
//	/**
//	 * Color is black.<br>
//	 * Takes a int pixel size instead a float ratio size.<br>
//	 * Uses a default font size of 36 pixels.
//	 * 
//	 * @param anchorPoint The anchor point
//	 * @param text        The text to draw
//	 * @param font        The font to use
//	 */
//	public UIElementText(AnchorPoint anchorPoint, String text, Font font, float width) {
//		this(anchorPoint, text, font, 36, width);
//	}
//
//	/**
//	 * Color is black.<br>
//	 * Takes a int pixel size instead a float ratio size.<br>
//	 * Uses Arial a for the font and auto generates font size.
//	 * 
//	 * @param anchorPoint The anchor point
//	 * @param text        The text to draw
//	 * @param size        The size of the text, in pixels, <em>relative to the
//	 *                    current resolution.</em>
//	 */
//	public UIElementText(AnchorPoint anchorPoint, String text, int size, float width) {
//		this(anchorPoint, text, "Arial", size, width);
//	}
//
//	/**
//	 * Color is black.<br>
//	 * Takes a int pixel size instead a float ratio size.<br>
//	 * Uses just a name for the font and auto generates font size.
//	 * 
//	 * @param anchorPoint The anchor point
//	 * @param text        The text to draw
//	 * @param font        The name of the font to use
//	 * @param size        The size of the text, in pixels, <em>relative to the
//	 *                    current resolution.</em>
//	 */
//	public UIElementText(AnchorPoint anchorPoint, String text, String font, int size, float width) {
//		this(anchorPoint, text, new Font(font, 0, size), size, width);
//	}
//
//	/**
//	 * Color is black.<br>
//	 * Takes a int pixel size instead a float ratio size.
//	 * 
//	 * @param anchorPoint The anchor point
//	 * @param text        The text to draw
//	 * @param font        The font to use
//	 * @param size        The size of the text, in pixels, <em>relative to the
//	 *                    current resolution.</em>
//	 */
//	public UIElementText(AnchorPoint anchorPoint, String text, Font font, int size, float width) {
//		this(anchorPoint, text, font, 2 * (float) size / (float) Application.getInstance().getWindow().getHeight(),
//				width);
//	}
//
//	/**
//	 * Color is black.<br>
//	 * Takes a int pixel size instead a float ratio size.
//	 * 
//	 * @param anchorPoint The anchor point
//	 * @param text        The text to draw
//	 * @param font        The font to use
//	 * @param size        The size of the text, in pixels, <em>relative to the
//	 *                    current resolution.</em>
//	 */
//	public UIElementText(AnchorPoint anchorPoint, String text, Font font, float size, float width) {
//		this(anchorPoint, text, font, size, new Vector3f(), width);
//	}
	
	public UIElementText(AnchorPoint anchorPoint, String text, float size) {
		this(anchorPoint, text, new Font("Arial", 36, 0), size, new Vector3f(0, 0, 0));
	}
	
	public UIElementText(AnchorPoint anchorPoint, String text, float size, float width) {
		this(anchorPoint, text, new Font("Arial", 36, 0), size, new Vector3f(0, 0, 0), width);
	}

	public UIElementText(AnchorPoint anchorPoint, String text, Font font, float size, Vector3f color) {
		this(anchorPoint, text, font, size, color, -1);
	}
	
	public UIElementText(AnchorPoint anchorPoint, String text, Font font, float size, Vector3f color, float width) {
		super(anchorPoint);
		this.font = font;
		this.text = text;
		this.size = size;
		this.color = color;
		object = new EngineObjectTextUI(this);
		this.maxWidth = width;
	}

	@Override
	public EngineObjectMesh getObject() {
		return object;
	}

	@Override
	public Vector2f getBaseSize() {
		if (maxWidth < 0) {
			Rectangle2D bounds = TextUtils.getBounds(text);
			return new Vector2f((float) (size * bounds.getWidth() / bounds.getHeight()), size);
		} else {
			return new Vector2f(maxWidth * 2, size);
		}
	}

	protected String _text;
	protected Font _font;

	private Vector3f _color;

	protected boolean triggerFlag = false;

	public void triggerUpdate() {
		triggerFlag = true;
	}

	/**
	 * The maximum width of the text, used extensive in text boxes. A number between
	 * 0 and 1 (or -1 for no max width) that is a ratio of screen width.
	 */
	protected float maxWidth = -1;

	protected void triggeredUpdate() {
		object.textAligment = textAlignment;
		triggerFlag = false;
		object.forceNewTextureFlag = true;
		object.set(text);
		object.updateTexture();
	}

	@Override
	public void update() {
		super.update();
		if (triggerFlag) {
			triggeredUpdate();
		}
		if (text.equals(_text) && font.equals(_font)) {

		} else {
			_text = text;
			_font = font;
			triggerUpdate();
		}
		if (color.equals(_color)) {

		} else {
			_color = color;
			object.setColor(color);
		}
	}

	@Override
	public void refresh() {
		triggeredUpdate();
	}
	
	@Override
	public void delete() {
		super.delete();
	}
	
	@Override
	public boolean selectable() {
		return false;
	}
}
