package net.chazzvader.core.generic.engine.event.type;

/**
 * An event handler to manage key pressed/released events
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
public interface IEventHandlerKey {

	/**
	 * Key pressed event.
	 * 
	 * @param key     The GLFW key code. For example, the <code>A</code> on the
	 *                keyboard is <code>GLFW.GLFW_KEY_A</code>, whether or not its
	 *                capital is dependent on shift/caps lock status.
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
	public boolean keyPressed(int key, int mods, boolean ctrl, boolean shift, boolean alt, boolean os, boolean capLock,
			boolean numLock);
	
	/**
	 * Key released event.
	 * 
	 * @param key     The GLFW key code. For example, the <code>A</code> on the
	 *                keyboard is <code>GLFW.GLFW_KEY_A</code>, whether or not its
	 *                capital is dependent on shift/caps lock status.
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
	public boolean keyReleased(int key, int mods, boolean ctrl, boolean shift, boolean alt, boolean os, boolean capLock,
			boolean numLock);

}
