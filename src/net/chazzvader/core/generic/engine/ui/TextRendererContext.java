package net.chazzvader.core.generic.engine.ui;

import java.awt.geom.Rectangle2D;

import net.chazzvader.core.generic.engine.IDeletable;
import net.chazzvader.core.generic.engine.creator.TextureCreator;
import net.chazzvader.core.generic.engine.render.material.Texture;
import net.chazzvader.core.generic.engine.uilegacy.element.text.TextAlignment;
import net.chazzvader.core.generic.math.Vector2f;
import net.chazzvader.core.generic.util.TextUtils;

/**
 * A text render context is a class used in conjunction with the UIRenderer. It
 * stores a texture, so that it does not need to be regenerated every frame.<br>
 * Have one of these for every text element that will be the same for me than 1
 * frame.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 * 
 */
public class TextRendererContext implements IDeletable {

	private Texture texture;
	private String _text;
	private int _windowWidth;
	private int _windowHeight;
	private float _size;
	private ScalingBasis _scalingBasis;
	
	/**
	 * Primary method, gets the texture or creates a new one if there are changes.
	 * 
	 * @param instance     The UI render that will be rendering this texture.
	 * @param size         The size of the texture.
	 * @param scalingBasis The scaling basis.
	 * @param text         The text of the texture.
	 * @return The texture.
	 */
	public Texture get(UIRenderer instance, float size, ScalingBasis scalingBasis, String text) {
		int windowWidth = instance.getRenderContext().getWidth();
		int windowHeight = instance.getRenderContext().getHeight();

		if (windowWidth == _windowWidth && windowHeight == _windowHeight && scalingBasis == _scalingBasis
				&& size == _size && text.equals(_text) && texture != null) {
			return texture;
		}

		if (texture != null) {
			texture.delete();
		}

		_windowWidth = windowWidth;
		_windowHeight = windowHeight;
		_scalingBasis = scalingBasis;
		_size = size;
		_text = text;

		Rectangle2D bounds = TextUtils.getBounds(text);
		float aspectRatio = (float) (bounds.getWidth() / bounds.getHeight());
		Vector2f adjust = new Vector2f(size * aspectRatio, size);
		adjust = instance.adjustForAspectRatio(adjust, scalingBasis);

		int height = (int) (adjust.y * windowHeight);
		int[] raw = TextUtils.renderToTextureRaw(text, height, aspectRatio, TextAlignment.CENTER);

		texture = TextureCreator.fromRaw(raw, (int) (height * aspectRatio), height).deletable();

		return texture;
		
	}

	@Override
	public void delete() {
		if (texture != null) {
			texture.delete();
		}
		texture = null;
	}

}
