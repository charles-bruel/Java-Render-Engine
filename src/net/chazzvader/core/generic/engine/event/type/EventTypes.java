package net.chazzvader.core.generic.engine.event.type;

import net.chazzvader.core.generic.engine.event.EventDispatcherType;
import net.chazzvader.core.generic.engine.event.EventManager;

/**
 * A simple static containing all basic event types.
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */

public class EventTypes {

	private EventTypes() {
	}

	/**
	 * Blank method, but running a method initializes all static fields.
	 */
	public static void init() {
	}

	/**
	 * Event for key press/release.
	 * 
	 * @see EventDataIDAndAction
	 */
	public static final EventType EVENT_KEY = new EventType(IEventHandlerKey.class,
			EventDispatchers.ID_AND_ACTION_EVENT_DISPATCHER, EventDispatcherType.SIMPLE);

	/**
	 * Event for mouse button press/release.
	 * 
	 * @see EventDataIDAndAction
	 */
	public static final EventType EVENT_MOUSE_BUTTON = new EventType(IEventHandlerMouseButton.class,
			EventDispatchers.ID_AND_ACTION_EVENT_DISPATCHER, EventDispatcherType.SIMPLE);

	/**
	 * Event for mouse movement.
	 * 
	 * @see EventDataMouseMoved
	 */
	public static final EventType EVENT_MOUSE_MOVED = new EventType(IEventHandlerMouseMoved.class,
			EventDispatchers.MOUSE_MOVED_EVENT_DISPATCHER, EventDispatcherType.COMPLEX);

	/**
	 * Event for window resizes.
	 * 
	 * @see EventDataWindowResized
	 */
	public static final EventType EVENT_WINDOW_RESIZED = new EventType(IEventHandlerWindowResized.class,
			EventDispatchers.WINDOW_RESIZED_EVENT_DISPATCHER, EventDispatcherType.SIMPLE);
	
	/**
	 * Event for scroll wheel action.
	 * 
	 * @see EventDataScroll
	 */
	public static final EventType EVENT_SCROLL = new EventType(IEventHandlerScroll.class,
			EventDispatchers.SCROLL_EVENT_DISPATCHER, EventDispatcherType.SIMPLE);

	static {
		EventManager.registerEventType(EVENT_KEY);
		EventManager.registerEventType(EVENT_MOUSE_BUTTON);
		EventManager.registerEventType(EVENT_MOUSE_MOVED);
		EventManager.registerEventType(EVENT_WINDOW_RESIZED);
		EventManager.registerEventType(EVENT_SCROLL);
	}

}
