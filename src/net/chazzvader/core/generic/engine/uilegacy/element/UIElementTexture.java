package net.chazzvader.core.generic.engine.uilegacy.element;

import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.Application;
import net.chazzvader.core.generic.engine.object.EngineObjectMesh;
import net.chazzvader.core.generic.engine.object.ui.EngineObjectUITexture;
import net.chazzvader.core.generic.engine.render.material.Texture;
import net.chazzvader.core.generic.engine.uilegacy.anchorpoint.AnchorPoint;
import net.chazzvader.core.generic.math.Vector2f;

@Deprecated
@SuppressWarnings("javadoc")
public class UIElementTexture extends UIElement {

	protected EngineObjectUITexture object;
	public Vector2f size;

	public Texture texture;

	/**
	 * Width and height auto determined by given height. <br>
	 * Size given as pixels int not ratio float
	 * 
	 * @param anchorPoint The anchor point
	 * @param texture     The texture to draw
	 * @param size        The height, used to determine width
	 */
	public UIElementTexture(AnchorPoint anchorPoint, Texture texture, int size) {
		this(anchorPoint, texture, 2 * ((float) size / Application.getInstance().getWindow().getHeight()));
	}

	/**
	 * Width and height auto determined by given height
	 * 
	 * @param anchorPoint The anchor point
	 * @param texture     The texture to draw
	 * @param size        The height, used to determine width
	 */
	public UIElementTexture(AnchorPoint anchorPoint, Texture texture, float size) {
		this(anchorPoint, texture, size * texture.getHeight() / texture.getWidth(), size);
	}

	/**
	 * Final constructor, all constructors lead here.
	 * 
	 * @param anchorPoint The anchor point
	 * @param texture     The texture to draw
	 * @param width       The width of the texture relative to the screen height
	 * @param height      The height of the texture relative to the screen height
	 */
	public UIElementTexture(AnchorPoint anchorPoint, Texture texture, float width, float height) {
		super(anchorPoint);
		this.size = new Vector2f(width, height);
		this.object = new EngineObjectUITexture(texture, this);
		this.texture = texture;
	}

	public Texture getTexture() {
		checkDelete();
		return texture;
	}

	@Override
	public EngineObjectMesh getObject() {
		return object;
	}

	private Texture _texture;
	
	@Override
	public void update() {
		checkDelete();
		super.update();
		if(texture != _texture) {
			_texture = texture;
			object.setTexture(texture);
		}
	}
	
	@Override
	public Vector2f getBaseSize() {
		checkDelete();
		return size;
	}

	@Override
	public void refresh() {
		checkDelete();
		//TODO: Implement this
	}

	@Override
	public void delete() {
		checkDelete();
		texture.delete();
		texture = null;
		deleted = true;
		super.delete();
	}

	private boolean deleted = false;
	
	private void checkDelete() {
		if(deleted) {
			Logging.log("Element Deleted", "UIElementTexture", LoggingLevel.ERR);
		}
	}
	
	@Override
	public boolean selectable() {
		return false;
	}
	
}
