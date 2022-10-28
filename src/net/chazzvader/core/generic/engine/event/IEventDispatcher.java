package net.chazzvader.core.generic.engine.event;

import net.chazzvader.core.generic.engine.event.type.EventData;

/**
 * Since every interface implements different methods, there is no universal way
 * to call methods, especially since some events do it differently (think mouse
 * moved vs key).<br>
 * This is called to dispatch events, you call the correct methods from your
 * implementation.
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 * @see EventDispatcherType
 */
public interface IEventDispatcher {

	/**
	 * Dispatches the event to the proper methods of the interface.
	 * 
	 * @param eventManager The static event manager instance.
	 * @param eventData    The data to send.
	 * @param handler      The event handler, all events have a different interface,
	 *                     so it is object.
	 * @return True if the event got blocked
	 */
	public boolean dispatch(EventManager eventManager, EventData eventData, Object handler);

}
