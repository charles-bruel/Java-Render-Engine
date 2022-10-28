package net.chazzvader.core.generic.engine.uilegacy.element;

import org.lwjgl.glfw.GLFW;

import net.chazzvader.core.generic.engine.creator.ObjectCreator;
import net.chazzvader.core.generic.engine.creator.TextureCreator;
import net.chazzvader.core.generic.engine.mesh.Mesh;
import net.chazzvader.core.generic.engine.object.EngineObjectMesh;
import net.chazzvader.core.generic.engine.render.material.MaterialUILegacy;
import net.chazzvader.core.generic.engine.render.material.Texture;
import net.chazzvader.core.generic.engine.ui.Alignment;
import net.chazzvader.core.generic.engine.uilegacy.anchorpoint.AnchorPoint;
import net.chazzvader.core.generic.engine.uilegacy.anchorpoint.AnchorPointElementRelative;
import net.chazzvader.core.generic.engine.uilegacy.element.text.UIElementText;
import net.chazzvader.core.generic.math.Matrix4f;
import net.chazzvader.core.generic.math.Vector3f;

@Deprecated
@SuppressWarnings("javadoc")

public class UIElementSlider extends UIElementTexture {

	private static final Texture GRAB = TextureCreator
			.fromFile("net\\chazzvader\\core\\generic\\engine\\ui\\res\\slider_grab.png");
	private static final Texture GROOVE = TextureCreator
			.fromFile("net\\chazzvader\\core\\generic\\engine\\ui\\res\\slider_groove.png");

	private static final float GRAB_HEIGHT = 0.05f;
	private static final float GRAB_WIDTH = GRAB_HEIGHT * GRAB.getWidth() / GRAB.getHeight();

	/**
	 * The minimum value of the slider.
	 */
	public float min;
	
	/**
	 * The maximum value of the slider.
	 */
	public float max;
	
	/**
	 * The current value of the slider.
	 */
	public float value;
	
	/**
	 * Should the slider snap to integer values.
	 */
	public boolean roundToInt = false;

	private EngineObjectMesh sliderGrab;
	private MaterialUILegacy sliderGrabMaterial;
	private float width;

	public UIElementSlider(AnchorPoint anchorPoint, float width, float min, float max) {
		super(anchorPoint, GROOVE, width, GRAB_HEIGHT);
		this.width = width;
		this.min = min;
		this.max = max;
		this.value = (min + max) / 2f;

		Mesh quad = ObjectCreator.quadTextured(new Vector3f(-1, -1, 0), new Vector3f(1, -1, 0), new Vector3f(-1, 1, 0),
				new Vector3f(1, 1, 0));
		sliderGrabMaterial = new MaterialUILegacy(GRAB, this);
		sliderGrabMaterial.color_multiplier = new Vector3f(1, 1, 1);
		sliderGrab = new EngineObjectMesh(quad, sliderGrabMaterial);
		sliderGrab.provider = this;
	}

	private float _value;
	private boolean click;

	@Override
	public void update() {
		sliderGrabMaterial.adjustmentMatrix = getSliderGrabAdjustmentMatrix();
		if (value != _value) {
			if (roundToInt) {
				value = (int) (value + 0.5f);
			}
			_value = value;
		}
		if (click) {
			value = (mouseX * (max - min)) + min;
		}
		if (!isFocused()) {
			click = false;
		}
		if (value < min) {
			value = min;
		}
		if (value > max) {
			value = max;
		}
		super.update();
	}

	private Matrix4f getSliderGrabAdjustmentMatrix() {
		float xScale = GRAB_WIDTH / width;
		float xTrans = (((value - min) / (max - min)) * 40 - 20) * width * 2;
		return Matrix4f.scale(xScale, 1, 1).applyTranslation(xTrans, 0, 0);
	}

	@Override
	public void render() {
		super.render();
		sliderGrab.render();
	}

	@Override
	public boolean mouseButtonPressed(int button, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
			boolean capLock, boolean numLock, int clickCount) {
		if(!isFocused()) {
			return false;
		}
		if (mouseX >= 0 && mouseX <= 1 && mouseY >= 0 && mouseY <= 1) {
			click = true;
			return true;
		} else {
			getLayer().getStack().focus = null;
		}
		return false;
	}

	@Override
	public boolean mouseButtonReleased(int button, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
			boolean capLock, boolean numLock) {
		click = false;
		return true;
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
	public boolean scrollWheel(float xoffset, float yoffset) {
		if (mouseX >= 0 && mouseX <= 1 && mouseY >= 0 && mouseY <= 1) {
			value += yoffset * (max-min) / 20f;
			if (value < min) {
				value = min;
			}
			if (value > max) {
				value = max;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * If there is an attached text display, how many digits to draw. Defaults to 5,
	 * includes . and -.
	 */
	public int digits = 5;
	
	public UIElementText addTextDisplay(Alignment alignment) {
		UIElementText element = new UIElementText(new AnchorPointElementRelative(this, alignment, 0.01f), "", GRAB_HEIGHT, 0.1f) {
			float _val;

			@Override
			public void update() {
				if (_val != value) {
					_val = value;
					String txt;
					if(roundToInt) {
						txt = (int) value + "";
					} else {
						txt = value + "";
					}
					this.text = txt.substring(0, txt.length() < digits ? txt.length() : digits);
				}
				super.update();
			}
		};
		switch (alignment) {
		case CENTER:
			element.alignment = Alignment.CENTER;
			break;
		case LEFT:
			element.alignment = Alignment.RIGHT;
			break;
		case LOWER:
			element.alignment = Alignment.UPPER;
			break;
		case LOWER_LEFT:
			element.alignment = Alignment.UPPER_RIGHT;
			break;
		case LOWER_RIGHT:
			element.alignment = Alignment.UPPER_LEFT;
			break;
		case RIGHT:
			element.alignment = Alignment.LEFT;
			break;
		case UPPER:
			element.alignment = Alignment.LOWER;
			break;
		case UPPER_LEFT:
			element.alignment = Alignment.LOWER_RIGHT;
			break;
		case UPPER_RIGHT:
			element.alignment = Alignment.LOWER_LEFT;
			break;
		}
		getLayer().add(element);
		return element;
	}
	
	@Override
	public boolean selectable() {
		return true;
	}

}
