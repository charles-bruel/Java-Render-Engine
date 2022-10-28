package net.chazzvader.core.generic.engine.event.type;

/**
 * An event handler to mouse movements.
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
public interface IEventHandlerScroll {

	/**
	 * This updates the absolute position of the mouse.
	 * 
	 * @param xoffset The x offset in unspecified units.
	 * @param yoffset The y offset in unspecified units. Main axis of a scroll wheel.
	 * @return True if the event got blocked
	 */
	public boolean scrollWheel(float xoffset, float yoffset);

}
