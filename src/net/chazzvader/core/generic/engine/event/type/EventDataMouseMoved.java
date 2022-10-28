package net.chazzvader.core.generic.engine.event.type;

/**
 * A class representing a mouse move event
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class EventDataMouseMoved extends EventData {

	/**
	 * The x position of the mouse on the screen
	 */
	public final int xpos;
	
	/**
	 * The y position of the mouse on the screen
	 */
	public final int ypos;

	/**
	 * Creates a mouse moved event
	 * @param xpos The x position of the mouse
	 * @param ypos The y position of the mouse
	 * @see #xpos
	 * @see #ypos
	 */
	public EventDataMouseMoved(int xpos, int ypos) {
		super(EventTypes.EVENT_MOUSE_MOVED);
		this.xpos = xpos;
		this.ypos = ypos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + xpos;
		result = prime * result + ypos;
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
		EventDataMouseMoved other = (EventDataMouseMoved) obj;
		if (xpos != other.xpos)
			return false;
		if (ypos != other.ypos)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EventDataMouseMoved [xpos=" + xpos + ", ypos=" + ypos + "]";
	}

}
