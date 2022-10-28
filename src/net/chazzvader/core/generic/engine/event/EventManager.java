package net.chazzvader.core.generic.engine.event;

import java.util.ArrayList;

import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.event.type.EventData;
import net.chazzvader.core.generic.engine.event.type.EventType;
import net.chazzvader.core.generic.engine.event.type.EventTypes;

/**
 * An event manager, manages all event related things for the application.
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
public class EventManager {

	private static final EventManager INSTANCE = new EventManager();

	static {
		EventTypes.init();
	}

	/**
	 * A public list of all registered <code>EventTypes</code>.
	 */
	public ArrayList<EventType> types = new ArrayList<>();

	private EventManager() {

	}

	/**
	 * Static method that dispatches the following event data to the correct event
	 * handler.
	 * 
	 * @param eventData The data, also determines event type.
	 * @return Whether the event was caught. Even if this returns false, it does not
	 *         mean nothing happened.
	 * @see EventData
	 */
	public static boolean dispatch(EventData eventData) {
		return INSTANCE.dispatch(eventData, (byte) 0);
	}

	private boolean dispatch(EventData eventData, byte signatureChanger) {
		if (eventData == null) {
			Logging.log("Tried to use an EventData instance with null EventType", "Event System", LoggingLevel.ERR);
			return false;
		}
		EventType type = null;
		for (int i = 0; i < types.size(); i++) {
			if (types.get(i) == eventData.EVENT_TYPE) {
				type = types.get(i);
				break;
			}
		}
		if (type == null) {
			Logging.log("Tried to use an EventData instance with an EventType that couldn't be found", "Event System",
					LoggingLevel.ERR);
			return false;
		}
		return type.dispatch(this, eventData);
	}

	/**
	 * Registers a new event listener with a priority. The correct listener types
	 * are identified based off of what handlers are implemented.
	 * 
	 * @param eventListener The class implementing listener interfaces to register.
	 * @param priority      The priority of the event, determined order. <em>Lower
	 *                      priorities go first.</em>
	 * @return If it was successful.
	 */
	public static boolean registerEventListener(Object eventListener, int priority) {
		return INSTANCE.registerEventListener(eventListener, priority, (byte) 0);
	}

	private boolean registerEventListener(Object eventListener, int priority, byte signatureChanger) {
		if (eventListener == null) {
			Logging.log("Tried to register a null EventListener", "Event System", LoggingLevel.ERR);
			return false;
		}
		if (priority < 0 || priority > 15) {
			Logging.log("Tried to register an EventListener with an invalid priority", "Event System",
					LoggingLevel.ERR);
			return false;
		}
		Class<?> clazz = eventListener.getClass();
		Class<?>[] interfaces = clazz.getInterfaces();
		int interfacesImplemeted = 0;

		for (int i = 0; i < interfaces.length; i++) {
			for (int j = 0; j < types.size(); j++) {
				if (interfaces[i] == types.get(j).getImplementationInterface()) {
					types.get(j).getEventHandlers().get(priority).add(eventListener);
					interfacesImplemeted++;
					break;
				}
			}
		}

		if (interfacesImplemeted == 0) {
			Logging.log("Tried to register an EventListener with no valid interfaces", "Event System",
					LoggingLevel.ERR);
			return false;
		}

		Logging.log("Sucessfully registered new EventListener implementing " + interfacesImplemeted
				+ " different interfaces.", "Event System", LoggingLevel.DEBUG);

		return true;
	}

	/**
	 * Registers a new event type. If you register a listener to this event type
	 * then register the event type after, the listener will not work.
	 * 
	 * @param eventType The event type to register.
	 * @return If it was successful.
	 * @see EventType
	 */
	public static boolean registerEventType(EventType eventType) {
		return INSTANCE.registerEventType(eventType, (byte) 0);
	}

	/**
	 * Registers a new event type. This creates the event type object from the 3
	 * components, using the basic constructor. If you register a listener to this
	 * event type then register the event type after, the listener will not work.
	 * 
	 * @param implementationInterface What interfaces event listeners implement to
	 *                                get events of this event type.
	 * @param eventDispatcher         The dispatcher that calls the interface
	 *                                methods, to dispatch the event.
	 * @param eventDispatcherType     What type the dispatcher is. If you don't
	 *                                know, choose <code>SIMPLE</code>.
	 * @return If it was successful.
	 * @see EventDispatcherType
	 * @see EventType
	 */
	public static EventType registerEventType(Class<?> implementationInterface, IEventDispatcher eventDispatcher,
			EventDispatcherType eventDispatcherType) {
		EventType type = new EventType(implementationInterface, eventDispatcher, eventDispatcherType);
		INSTANCE.registerEventType(type, (byte) 0);
		return type;
	}

	private boolean registerEventType(EventType eventType, byte signatureChanger) {
		if (eventType == null) {
			Logging.log("Tried to register a null EventType", "Event System", LoggingLevel.ERR);
			return false;
		}
		if (types.contains(eventType)) {
			Logging.log("Tried to register a duplicate EventType", "Event System", LoggingLevel.ERR);
			return false;
		}
		types.add(eventType);
		return true;
	}

}
