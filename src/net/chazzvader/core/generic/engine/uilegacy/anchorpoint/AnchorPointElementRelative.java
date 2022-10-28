package net.chazzvader.core.generic.engine.uilegacy.anchorpoint;

import net.chazzvader.core.generic.engine.ui.Alignment;
import net.chazzvader.core.generic.engine.uilegacy.element.UIElement;
import net.chazzvader.core.generic.math.Vector2f;

/**
 * An anchor point that allows an element to move with another element. Useful
 * for laying out menus.
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
@Deprecated
public class AnchorPointElementRelative extends AnchorPoint {

	/**
	 * The element to be relative to.
	 */
	public UIElement element;
	/**
	 * The alignment, i.e. what side of the element do you want to be on.
	 */
	public Alignment alignment;
	/**
	 * How much padding, or empty space between the two elements.
	 */
	public float padding;

	/**
	 * Constructor.
	 * 
	 * @param element   The element to be relative to.
	 * @param alignment The alignment, i.e. what side of the element do you want to
	 *                  be on. THE ALIGNMENT OF THE ELEMENT THIS IS APPLIED TO
	 *                  SHOULD BE OPPOSE OF THIS PARAMETER.
	 * @param padding   How much padding, or empty space between the two elements.
	 *                  Measured in ratio of screen, 0 to 1.
	 * @see Alignment
	 */
	public AnchorPointElementRelative(UIElement element, Alignment alignment, float padding) {
		this.element = element;
		this.alignment = alignment;
		this.padding = padding;
	}

	@Override
	public Vector2f getPosition() {
		Vector2f elementPos = Vector2f.copyOf(element.getPosition());
		Vector2f size = element.getAdjustedSize();
		elementPos.x += element.alignment.x * size.x / 2f;
		elementPos.y += element.alignment.y * size.y / 2f;
		elementPos.x -= alignment.x * size.x / 2f;
		elementPos.y -= alignment.y * size.y / 2f;
		elementPos.x -= padding * alignment.x;
		elementPos.y -= padding * alignment.y;
		return elementPos;
	}

}
