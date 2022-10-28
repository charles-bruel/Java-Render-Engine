package net.chazzvader.core.generic.engine.object.text;

import net.chazzvader.core.generic.engine.creator.ObjectCreator;
import net.chazzvader.core.generic.engine.object.ui.IEngineObjectUI;
import net.chazzvader.core.generic.engine.render.material.MaterialUILegacy;
import net.chazzvader.core.generic.engine.render.material.Texture;
import net.chazzvader.core.generic.engine.uilegacy.element.UIElement;
import net.chazzvader.core.generic.engine.uilegacy.element.text.UIElementText;
import net.chazzvader.core.generic.engine.uilegacy.element.text.UIElementTextBox;
import net.chazzvader.core.generic.math.Vector2f;
import net.chazzvader.core.generic.math.Vector3f;
import net.chazzvader.core.generic.util.TextUtils;

/**
 * An engine object primitive to represent text as part of the UI. Not added to
 * the typical rendering chain.
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
@Deprecated
public class EngineObjectTextUI extends EngineObjectTextBase implements IEngineObjectUI {

	/**
	 * The material of this object.<br>
	 * The associated UI elements frequently have to modify this.<br>
	 * There is another field named material, in <code>EngineObjectMesh</code>,
	 * however that is a regular material and therefore you can't do much to it.
	 * This was decided to be preferable to casting every time. The presence of
	 * another field named the same may cause problems, so be careful.
	 */
	public MaterialUILegacy material;

	private UIElementText element;

	/**
	 * Constructor, DO NOT USE!<br>
	 * You will almost always want to use the UIElementText or some variation for
	 * this.
	 * 
	 * @param element The UI element to link to.
	 * @see UIElementText
	 * @see UIElementTextBox
	 */
	public EngineObjectTextUI(UIElementText element) {
		super(ObjectCreator.quadTextured(new Vector3f(-1, -1, 0), new Vector3f(1, -1, 0), new Vector3f(-1, 1, 0),
				new Vector3f(1, 1, 0), new Vector2f(0, 1), new Vector2f(1, 1), new Vector2f(0, 0), new Vector2f(1, 0)),
				new MaterialUILegacy(null, element), element.font, element.text);
		this.element = element;
		if (this.material == null)
			this.material = (MaterialUILegacy) super.material;
		provider = element;
//		context = getWindow().getRenderPipeline().getUIContext();//TODO: Make better
		forceNewTextureFlag = true;
	}

	@Override
	protected void updateMaterialTexture() {
		if (this.material == null)
			this.material = (MaterialUILegacy) super.material;
		this.material.texture = super.texture;
	}

	@Override
	public void setColor(Vector3f color) {
		material.color_multiplier = color;
	}

	@Override
	protected Texture generateTexture() {
		int height = (int) ((element.getAdjustedSize().y / 2) * getWindowHeight());
		if(needNewTextureObject()) {
			TextUtils.setFont(font);
			if (this.texture != null)
				texture.delete();
			this.texture = TextUtils.renderToTexture(this.string, height, getMaxAspectRatio(), this.textAligment);
			size.x = texture.getWidth();
			size.y = texture.getHeight();
		} else {
			this.texture.swapData(TextUtils.renderToTextureRaw(this.string, height, getMaxAspectRatio(), this.textAligment), 0, 0, size.x, size.y);
		}
		return texture.deletable();
	}

	@Override
	public float getMaxAspectRatio() {
		float height = element.getAdjustedSize().y;
		height *= getWindowHeight();
		float width = element.getAdjustedSize().x;
		width *= getWindowWidth();
		return width/height;
	}

//	private int _wh;

	@Override
	public void update(double delta) {
		super.update(delta);
		forceNewTextureFlag = false;
//		_wh = getWindowHeight();
	}

	@Override
	protected boolean needNewTextureObject() {//TODO: Reimplement
//		if (forceNewTextureFlag) {
//			return true;
//		}
//		if (getWindowHeight() != _wh) {
//			return true;
//		}
//		return false;
		return true;
	}

	/**
	 * Updates the background texture. The background texture provides the
	 * highlighting of the text.
	 * 
	 * @param start       The start of the highlighted section, as a ratio between
	 *                    0-1.
	 * @param end         The end of the highlighted section, as a ratio between
	 *                    0-1.
	 * @param height      The height of the text, only used with
	 *                    <code>aspectRatio</code> to determine width. The height
	 *                    isn't used as the texture is the same vertically, so the
	 *                    height is simply set to 1.
	 * @param aspectRatio Used to determine width from height, technically a width
	 *                    parameter would be better but this is more consistent.
	 */
	public void updateBackgroundTexture(float start, float end, int height, float aspectRatio) {
		if (needNewTextureObject()) {
			if (material.background != null) {
				material.background.delete();
			}
			material.background = TextUtils.getTextBackgroundTexture(start, end, height, aspectRatio);
		} else {
			material.background.swapData(TextUtils.getTextBackgroundTextureRaw(start, end, height, aspectRatio), 0, 0,
					size.x, 1);
		}
	}

	@Override
	public UIElement getParentElement() {
		return element;
	}
}
