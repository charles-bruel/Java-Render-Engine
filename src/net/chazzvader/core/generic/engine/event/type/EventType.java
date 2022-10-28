package net.chazzvader.core.generic.engine.event.type;

import java.util.ArrayList;

import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.event.EventDispatcherType;
import net.chazzvader.core.generic.engine.event.EventManager;
import net.chazzvader.core.generic.engine.event.IEventDispatcher;

/**
 * An event type. An event type is a type of event that can be triggered, for
 * example a mousebutton event. This contains the interface to implement to
 * listen to this event, and the dispatcher.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class EventType {

	private Class<?> implementationInterface;
	private IEventDispatcher eventDispatcher;
	private EventDispatcherType eventDispatcherType;
	private ArrayList<ArrayList<Object>> eventHandlers = new ArrayList<>(16);

	{
		for (int i = 0; i < 0xF; i++) {
			eventHandlers.add(new ArrayList<>());
		}
	}

	/**
	 * Returns the event handlers for the event. Is a nested arraylist because they
	 * are sorted by priority. For example, the arraylist at index 0 contains all
	 * priority 0 (highest) events
	 * 
	 * @return A double array list with all event handlers.
	 */
	public ArrayList<ArrayList<Object>> getEventHandlers() {
		return eventHandlers;
	}

	/**
	 * Creates a new instance of an event type. You still need to register it with
	 * <code>EventManager</code>.
	 * 
	 * @param implementationInterface The interface that needs to implemented to get
	 *                                events of this type.
	 * @param eventDispatcher         The dispatcher of events.
	 * @param eventDispatcherType     The dispatcher type.
	 * @see EventManager
	 * @see IEventDispatcher
	 */
	public EventType(Class<?> implementationInterface, IEventDispatcher eventDispatcher,
			EventDispatcherType eventDispatcherType) {
		this.implementationInterface = implementationInterface;
		this.eventDispatcher = eventDispatcher;
		this.eventDispatcherType = eventDispatcherType;
	}

	/**
	 * Returns the interface an event handler needs to implement to get events.
	 * 
	 * @return The interface.
	 */
	public Class<?> getImplementationInterface() {
		return implementationInterface;
	}

	/**
	 * Returns the event dispatcher used to call the methods of the implementation
	 * interface.
	 * 
	 * @return The dispatcher
	 */
	public IEventDispatcher getEventDispatcher() {
		return eventDispatcher;
	}

	/**
	 * If the dispatcher returned from <code>getEventDispatcher()</code> is simple
	 * or complex.
	 * 
	 * @see EventType#getEventDispatcher()
	 * @see EventDispatcherType
	 * 
	 * @return The dispatcher
	 */
	public EventDispatcherType getEventDispatcherType() {
		return eventDispatcherType;
	}

	/**
	 * Dispatches an event through an event dispatcher.
	 * @param eventManager The static event manager instance.
	 * @param eventData The data to dispatch
	 * @return If the event was blocked.
	 */
	public boolean dispatch(EventManager eventManager, EventData eventData) {
		if (eventData.EVENT_TYPE != this) {
			Logging.log("Passed event data does not correspond with this event type!", "Event System",
					LoggingLevel.ERR);
			return false;
		}
		if (eventDispatcherType == EventDispatcherType.SIMPLE) {
			for (int i = 0; i < eventHandlers.size(); i++) {
				ArrayList<Object> handlers = eventHandlers.get(i);
				for (int j = 0; j < handlers.size(); j++) {
					boolean retTrue = eventDispatcher.dispatch(null, eventData, handlers.get(j));
					if (retTrue)
						return true;
				}
			}
		} else {
			return eventDispatcher.dispatch(eventManager, eventData, eventHandlers);
		}
		return false;
	}

}
