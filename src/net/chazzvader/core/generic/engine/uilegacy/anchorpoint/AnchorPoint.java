package net.chazzvader.core.generic.engine.uilegacy.anchorpoint;

import net.chazzvader.core.generic.engine.EngineItem;
import net.chazzvader.core.generic.math.Vector2f;

/**
 * Serves as a point to build a UI off of.
 * @author csbru
 * @since 1.0
 * @version 1
 */
@Deprecated
public abstract class AnchorPoint extends EngineItem {

	/**
	 * Gets the position of the anchor point in screen space, -1, -1 to 1, 1
	 * @return The vector representing the position of the anchor point
	 */
	public abstract Vector2f getPosition();
	
}
