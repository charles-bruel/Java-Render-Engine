package net.chazzvader.core.generic.engine.event.type;

/**
 * An event handler to mouse movements.
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
public interface IEventHandlerMouseMoved {

	/**
	 * This updates the absolute position of the mouse.
	 * 
	 * @param x The x coordinate, in pixels.
	 * @param y The y coordinate, in pixels.
	 * @return True if the event got blocked
	 */
	public boolean updateMouseAbsolutePosition(int x, int y);

	/**
	 * This updates the relative position of the mouse, relative to the last mouse position.
	 * 
	 * @param x The x coordinate, in pixels.
	 * @param y The y coordinate, in pixels.
	 * @return True if the event got blocked
	 */
	public boolean updateMouseOffset(int x, int y);
}
