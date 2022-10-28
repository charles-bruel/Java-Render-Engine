package net.chazzvader.core.generic.engine.event.type;

/**
 * An event handler to manage window resizing.
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
public interface IEventHandlerWindowResized {

	/**
	 * Window resized event.
	 * 
	 * @param width  The new width of the window.
	 * @param height The new height of the window.
	 * @return True if the event got blocked
	 */
	public boolean windowResized(int width, int height);

}
