package net.chazzvader.core.generic.engine.event.type;

/**
 * Represents a window resize event
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class EventDataWindowResized extends EventData {

	/**
	 * The new window width
	 */
	public final int width;

	/**
	 * The new window height
	 */
	public final int height;

	/**
	 * Creates a new window resized event
	 * 
	 * @param width  The new window width
	 * @param height The new window height
	 */
	public EventDataWindowResized(int width, int height) {
		super(EventTypes.EVENT_WINDOW_RESIZED);
		this.width = width;
		this.height = height;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + width;
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
		EventDataWindowResized other = (EventDataWindowResized) obj;
		if (height != other.height)
			return false;
		if (width != other.width)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EventDataWindowResized [width=" + width + ", height=" + height + "]";
	}

}
