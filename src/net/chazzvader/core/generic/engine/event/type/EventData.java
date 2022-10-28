package net.chazzvader.core.generic.engine.event.type;

import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;

/**
 * Supertype for all event data
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
public class EventData {

	/**
	 * The type of the event data
	 */
	public final EventType EVENT_TYPE;

	@Override
	public String toString() {
		return "EventData [EVENT_TYPE=" + this.EVENT_TYPE + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.EVENT_TYPE == null) ? 0 : this.EVENT_TYPE.hashCode());
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
		EventData other = (EventData) obj;
		if (this.EVENT_TYPE == null) {
			if (other.EVENT_TYPE != null)
				return false;
		} else if (!this.EVENT_TYPE.equals(other.EVENT_TYPE))
			return false;
		return true;
	}

	protected EventData(EventType EVENT_TYPE) {
		if (EVENT_TYPE == null) {
			Logging.log("Tried to create an EventData instance with null EventType", "Event System", LoggingLevel.ERR);
		}
		this.EVENT_TYPE = EVENT_TYPE;
	}

}
