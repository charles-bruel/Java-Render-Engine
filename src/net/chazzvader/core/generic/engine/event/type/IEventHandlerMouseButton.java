package net.chazzvader.core.generic.engine.event.type;

/**
 * An event handler to manage mouse button pressed/released events
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
public interface IEventHandlerMouseButton {

	/**
	 * Mouse button pressed event.
	 * 
	 * @param button     The mouse button code.
	 * @param mods       Any key mods, contain if modifier keys are down (shift,
	 *                   ctrl). Already separated out in different parameters for
	 *                   your convenience.
	 * @param ctrl       Is the control key held down.
	 * @param shift      Is the shift key held down.
	 * @param alt        Is the alt key held down.
	 * @param os         Is the os-specific key held down. I believe this is the
	 *                   window key on windows and command on macOS.
	 * @param capLock    Is the caps lock key on.
	 * @param numLock    Is the num lock key on.
	 * @param clickCount Number of clicks, used to track double/triple clicks. Each
	 *                   click must be within 500 milliseconds of the previous.
	 * @return True if the event got blocked
	 */
	public boolean mouseButtonPressed(int button, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
			boolean capLock, boolean numLock, int clickCount);

	/**
	 * Mouse button released event.
	 * 
	 * @param button  The mouse button code.
	 * @param mods    Any key mods, contain if modifier keys are down (shift, ctrl).
	 *                Already separated out in different parameters for your
	 *                convenience.
	 * @param ctrl    Is the control key held down.
	 * @param shift   Is the shift key held down.
	 * @param alt     Is the alt key held down.
	 * @param os      Is the os-specific key held down. I believe this is the window
	 *                key on windows and command on macOS.
	 * @param capLock Is the caps lock key on.
	 * @param numLock Is the num lock key on.
	 * @return True if the event got blocked
	 */
	public boolean mouseButtonReleased(int button, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
			boolean capLock, boolean numLock);

}
