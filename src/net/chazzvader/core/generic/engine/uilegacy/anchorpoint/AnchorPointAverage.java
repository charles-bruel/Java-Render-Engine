package net.chazzvader.core.generic.engine.uilegacy.anchorpoint;

import net.chazzvader.core.generic.math.Vector2f;

/**
 * Average anchor point, its the average of 2 anchor points
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
@Deprecated
public class AnchorPointAverage extends AnchorPoint {

	private AnchorPoint anchorPoint1, anchorPoint2;
	
	/**
	 * Creates the anchor point
	 * 
	 * @param anchorPoint1 The first anchor point to average from.
	 * @param anchorPoint2 The second anchor point to average from.
	 */
	public AnchorPointAverage(AnchorPoint anchorPoint1, AnchorPoint anchorPoint2) {
		this.anchorPoint1 = anchorPoint1;
		this.anchorPoint2 = anchorPoint2;
	}

	@Override
	public Vector2f getPosition() {
		Vector2f ap1, ap2;
		ap1 = anchorPoint1.getPosition();
		ap2 = anchorPoint2.getPosition();
		return new Vector2f(ap1.x/2f+ap2.x/2f, ap1.y/2f+ap2.y/2f);
	}

}
