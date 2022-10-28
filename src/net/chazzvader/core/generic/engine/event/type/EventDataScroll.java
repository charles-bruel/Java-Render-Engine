package net.chazzvader.core.generic.engine.event.type;

/**
 * A class representing a mouse move event
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class EventDataScroll extends EventData {

	/**
	 * The x offset in unspecified units.
	 */
	public final float xoffset;
	
	/**
	 * The y offset in unspecified units. Main axis of a scroll wheel.
	 */
	public final float yoffset;

	/**
	 * Creates a mouse moved event
	 * @param xoffset The x offset in unspecified units.
	 * @param yoffset The y offset in unspecified units. Main axis of a scroll wheel.
	 * @see #xoffset
	 * @see #yoffset
	 */
	public EventDataScroll(float xoffset, float yoffset) {
		super(EventTypes.EVENT_SCROLL);
		this.xoffset = xoffset;
		this.yoffset = yoffset;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(this.xoffset);
		result = prime * result + Float.floatToIntBits(this.yoffset);
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
		EventDataScroll other = (EventDataScroll) obj;
		if (Float.floatToIntBits(this.xoffset) != Float.floatToIntBits(other.xoffset))
			return false;
		if (Float.floatToIntBits(this.yoffset) != Float.floatToIntBits(other.yoffset))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EventDataScroll [xoffset=" + this.xoffset + ", yoffset=" + this.yoffset + "]";
	}

}
