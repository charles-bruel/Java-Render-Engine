package net.chazzvader.core.generic.engine.uilegacy.anchorpoint;

import net.chazzvader.core.generic.engine.uilegacy.element.UIElementPopup;
import net.chazzvader.core.generic.math.Vector2f;

/**
 * An anchor point specifically designed for attaching things a
 * <code>UIElementPopup</code>.
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
@Deprecated
public class AnchorPointPopup extends AnchorPoint {

	private UIElementPopup popup;
	private Vector2f pos;

	/**
	 * Creates a new anchor point.
	 * 
	 * @param popup The popup to attach to.
	 * @param posX  The x position relative to that popup.
	 * @param posY  The y position relative to that popup.
	 */
	public AnchorPointPopup(UIElementPopup popup, float posX, float posY) {
		this(popup, new Vector2f(posX, posY));
	}

	/**
	 * Creates a new anchor point.
	 * 
	 * @param popup The popup to attach to.
	 * @param pos   The position relative to that popup.
	 */
	public AnchorPointPopup(UIElementPopup popup, Vector2f pos) {
		this.popup = popup;
		this.pos = pos;
	}

	@Override
	public Vector2f getPosition() {
		Vector2f size = popup.getAdjustedSize();
		Vector2f temp = pos.mapCopy(0, 1, 0, 1, 0.5f - size.x / 2, 0.5f + size.x / 2, 0.5f - size.y / 2,
				0.5f + size.y / 2);
		return temp;
	}

}
