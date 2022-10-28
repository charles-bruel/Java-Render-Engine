package net.chazzvader.core.generic.engine.uilegacy.anchorpoint;

import net.chazzvader.core.generic.engine.Application;
import net.chazzvader.core.generic.math.Vector2f;
import net.chazzvader.core.generic.math.Vector2i;

/**
 * Absolute anchor point, the coordinate given in pixels
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
@Deprecated
public class AnchorPointAbsolute extends AnchorPoint {

	private Vector2f coordinates;

	/**
	 * Creates the anchor point
	 * 
	 * @param posX The x position of the anchor point in pixels.
	 * @param posY The y position of the anchor point in pixels.
	 */
	public AnchorPointAbsolute(int posX, int posY) {
		this(new Vector2f(posX, posY));
	}

	/**
	 * Creates the anchor point
	 * 
	 * @param coordinates The coordinates of the anchor point in pixels.
	 */
	public AnchorPointAbsolute(Vector2i coordinates) {
		this(coordinates.toVector2f());
	}

	/**
	 * Creates the anchor point
	 * 
	 * @param posX The x position of the anchor point in pixels.
	 * @param posY The y position of the anchor point in pixels.
	 */
	public AnchorPointAbsolute(float posX, float posY) {
		this(new Vector2f(posX, posY));
	}

	/**
	 * Creates the anchor point
	 * 
	 * @param coordinates The coordinates of the anchor point in pixels.
	 */
	public AnchorPointAbsolute(Vector2f coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public Vector2f getPosition() {
		return coordinates.mapCopy(0, Application.getInstance().getWindow().getWidth(), 0,
				Application.getInstance().getWindow().getHeight(), 0, 1, 0, 1);
	}

}