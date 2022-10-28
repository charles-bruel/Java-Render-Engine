package net.chazzvader.core.generic.engine.uilegacy.anchorpoint;

import net.chazzvader.core.generic.math.Vector2f;

/**
 * Relative anchor point, the coordinate given in screen space (0, 0, to 1,
 * 1).
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
@Deprecated
public class AnchorPointRelative extends AnchorPoint {

	private Vector2f coordinates;

	/**
	 * Creates the anchor point
	 * 
	 * @param posX The x position of the anchor point in screen space.
	 * @param posY The y position of the anchor point in screen space.
	 */
	public AnchorPointRelative(float posX, float posY) {
		this(new Vector2f(posX, posY));
	}
	
	/**
	 * Creates the anchor point
	 * 
	 * @param coordinates The coordinates of the anchor point in screen space.
	 */
	public AnchorPointRelative(Vector2f coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public Vector2f getPosition() {
		return coordinates;
	}

}