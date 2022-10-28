package net.chazzvader.core.generic.engine.event.type;

import org.lwjgl.glfw.GLFW;

import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.event.ActionType;

/**
 * Represented data for a keyboard event
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class EventDataIDAndAction extends EventData {

	/**
	 * What type of key related event was this?
	 */
	public final ActionType type;

	/**
	 * The GLFW id
	 * 
	 * @see GLFW
	 */
	public final int id;

	/**
	 * The modifiers, i.e. control and shift.
	 * <a href="https://www.glfw.org/docs/latest/group__mods.html">GLFW Key
	 * Modifiers</a>
	 */
	public final int modifers;

	/**
	 * Boolean versions of modifier keys.
	 * <a href="https://www.glfw.org/docs/latest/group__mods.html">GLFW Key
	 * Modifiers</a><br>
	 * OS is os specific, in glfw as super, but thats a java keyword. For example,
	 * the windows key.
	 */
	@SuppressWarnings("javadoc")
	public boolean ctrl, shift, alt, os, capLock, numLock;

	/**
	 * Creates a new key event given a event type and id
	 * 
	 * @param type      The type of key event
	 * @param id        The id of the key as given by GLFW, i.e.
	 *                  <code>GLFW.GLFW_KEY_B</code>
	 * @param modifers  Meta keys (I think)
	 * @param eventType Since this can be used for more than one event type, the
	 *                  event type must be given
	 * @see #type
	 * @see #id
	 */
	public EventDataIDAndAction(ActionType type, int id, int modifers, EventType eventType) {
		super(validateType(eventType));
		this.type = type;
		this.id = id;
		this.modifers = modifers;
		initModifiers();
	}

	private void initModifiers() {
		shift = (modifers & GLFW.GLFW_MOD_SHIFT) > 0;
		ctrl = (modifers & GLFW.GLFW_MOD_CONTROL) > 0;
		alt = (modifers & GLFW.GLFW_MOD_ALT) > 0;
		os = (modifers & GLFW.GLFW_MOD_SUPER) > 0;
		numLock = (modifers & GLFW.GLFW_MOD_NUM_LOCK) > 0;
		capLock = (modifers & GLFW.GLFW_MOD_CAPS_LOCK) > 0;
	}

	private static EventType validateType(EventType eventType) {
		if (eventType == EventTypes.EVENT_KEY || eventType == EventTypes.EVENT_MOUSE_BUTTON) {
			return eventType;
		}
		Logging.log("Invalid event type for data!", "Event System", LoggingLevel.ERR);
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventDataIDAndAction other = (EventDataIDAndAction) obj;
		if (id != other.id)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EventDataKey [type=" + type + ", key=" + id + "]";
	}

}
