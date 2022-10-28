package net.chazzvader.core.generic.engine.uilegacy.element;

import java.awt.geom.Rectangle2D;

import org.lwjgl.glfw.GLFW;

import net.chazzvader.core.generic.engine.creator.TextureCreator;
import net.chazzvader.core.generic.engine.mesh.Mesh;
import net.chazzvader.core.generic.engine.mesh.VertexedProperty;
import net.chazzvader.core.generic.engine.mesh.VertexedProperty.VertexPropertyType;
import net.chazzvader.core.generic.engine.object.ui.EngineObjectUITexture;
import net.chazzvader.core.generic.engine.render.material.Texture;
import net.chazzvader.core.generic.engine.ui.Alignment;
import net.chazzvader.core.generic.engine.uilegacy.anchorpoint.AnchorPoint;
import net.chazzvader.core.generic.engine.uilegacy.anchorpoint.AnchorPointElementRelative;
import net.chazzvader.core.generic.engine.uilegacy.element.text.TextAlignment;
import net.chazzvader.core.generic.engine.uilegacy.element.text.UIElementText;
import net.chazzvader.core.generic.interfaces.ISimpleEvent;
import net.chazzvader.core.generic.math.Vector2f;
import net.chazzvader.core.generic.util.TextUtils;

/**
 * A button element, one of the simplest forms of input.
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
@Deprecated
public class UIElementButton extends UIElementTexture {

	private static final Texture REGULAR = TextureCreator
			.fromFile("net\\chazzvader\\core\\generic\\engine\\ui\\res\\button.png");
	private static final Texture HOVER = TextureCreator
			.fromFile("net\\chazzvader\\core\\generic\\engine\\ui\\res\\button_hover.png");

	/**
	 * The name of the button, the text displayed on the button.
	 */
	public String name;

	/**
	 * The event to trigger when the button is pressed.
	 */
	public ISimpleEvent event;

	/**
	 * Constructor.
	 * 
	 * @param anchorPoint The anchor point.
	 * @param name        The text on the button.
	 */
	public UIElementButton(AnchorPoint anchorPoint, String name) {
		this(anchorPoint, name, 0.05f);
	}

	/**
	 * Constructor.
	 * 
	 * @param anchorPoint The anchor point.
	 * @param name        The text on the button.
	 * @param size        The height of the button as a ratio.
	 */
	public UIElementButton(AnchorPoint anchorPoint, String name, float size) {
		this(anchorPoint, name, new ISimpleEvent() {
			public void event() {
			}
		}, size);
	}

	private boolean onLayerAddedText = false;
	private UIElementText text;

	/**
	 * Final constructor, all constructors lead here
	 * 
	 * @param anchorPoint The anchor point.
	 * @param name        The text on the button.
	 * @param event       The event to trigger.
	 * @param size        The height of the button as a ratio.
	 */
	public UIElementButton(AnchorPoint anchorPoint, String name, ISimpleEvent event, float size) {
		super(anchorPoint, REGULAR, size, size);
		this.name = name;
		this.event = event;

		Rectangle2D temp = TextUtils.getBounds(name);
		Vector2f textBounds = new Vector2f((float) temp.getWidth(), (float) temp.getHeight());

		float targetLength = textBounds.x / textBounds.y;

		float textSize = size * 0.95f;
		Rectangle2D bounds = TextUtils.getBounds(name);

		text = new UIElementText(new AnchorPointElementRelative(this, Alignment.CENTER, 0), name, textSize,
				(float) (textSize * bounds.getWidth() / bounds.getHeight()));
		text.textAlignment = TextAlignment.CENTER;
		object.delete();
		object = new EngineObjectUITexture(HOVER, this, getMesh(targetLength));
		this.size = new Vector2f(0.5f * targetLength + 0.3f, 1);

		this.maintainAspectRatio = false;
	}

	private Vector2f size;

	@Override
	public Vector2f getBaseSize() {
		Vector2f temp = size.mulCopy(super.getBaseSize());
		temp.x *= (16f / 9f) / getWindowAspectRatio();
		return temp;
	}

	private Mesh getMesh(float targetLength) {
		float[] pos;
		byte[] ind;
		float[] tex;

		float total = 0.6f + targetLength;

		float lower = 0.3f / total;
		float upper = (0.3f + targetLength) / total;

		lower *= 2;
		lower -= 1;
		upper *= 2;
		upper -= 1;

		pos = new float[] { -1, -1, 0, lower, -1, 0, upper, -1, 0, 1, -1, 0, -1, 1, 0, lower, 1, 0, upper, 1, 0, 1, 1,
				0 };
		ind = new byte[] { 0, 1, 4, 1, 5, 4, 1, 2, 5, 2, 6, 5, 2, 3, 6, 3, 7, 6 };
		tex = new float[] { 0, 0, 0.3f, 0, 0.7f, 0, 1, 0, 0, 1, 0.3f, 1, 0.7f, 1, 1, 1 };

		return new Mesh(new VertexedProperty(pos, VertexPropertyType.VERTEX_POS),
				new VertexedProperty(ind, VertexPropertyType.INDICES),
				new VertexedProperty[] { new VertexedProperty(tex, VertexPropertyType.TEXTURE_COORDINATES) });
	}

	private boolean _hover;

	@Override
	public void update() {
		if (!onLayerAddedText && getLayer() != null) {
			getLayer().add(text);
			onLayerAddedText = true;
		}
		boolean temp = mouseX >= 0 && mouseX <= 1 && mouseY >= 0 && mouseY <= 1;
		if (temp != _hover) {
			_hover = temp;
			texture = temp ? HOVER : REGULAR;
		}
		text.sizeAbsolute = sizeAbsolute;
//		text.sizeSquish = sizeSquish;
		super.update();
	}

	@Override
	public boolean mouseButtonPressed(int button, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
			boolean capLock, boolean numLock, int clickCount) {
		if (mouseX >= 0 && mouseX <= 1 && mouseY >= 0 && mouseY <= 1) {
			event.event();
			return true;
		} else {
			getLayer().getStack().focus = null;
		}
		return false;
	}

	@Override
	public boolean keyPressed(int key, int mods, boolean ctrl, boolean shift, boolean alt, boolean os, boolean capLock,
			boolean numLock) {
		if (key == GLFW.GLFW_KEY_ESCAPE) {
			getLayer().getStack().focus = null;// De-focus on escape
		}
		return false;
	}

	@Override
	public void delete() {
		super.delete();
	}

}
