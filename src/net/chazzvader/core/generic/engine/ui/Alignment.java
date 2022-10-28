package net.chazzvader.core.generic.engine.ui;

/**
 * Alignment can be though of as what point on the element snaps to the
 * position, and thus may behave counter intuitively.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
@SuppressWarnings("javadoc")
public enum Alignment {

	CENTER(0, 0), UPPER(0, -1), LOWER(0, 1), LEFT(1, 0), RIGHT(-1, 0), UPPER_LEFT(1, -1), UPPER_RIGHT(-1, -1),
	LOWER_LEFT(1, 1), LOWER_RIGHT(-1, 1);

	private Alignment(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public final int x, y;

	public static Alignment eliminateVertical(Alignment alignment) {
		switch(alignment) {
		case CENTER:		
		case LOWER:
		case UPPER:
			return CENTER;
		case LEFT:
		case LOWER_LEFT:
		case UPPER_LEFT:
			return LEFT;
		case RIGHT:
		case LOWER_RIGHT:
		case UPPER_RIGHT:
			return RIGHT;
		default:
			return CENTER;
		}
		
	}

}