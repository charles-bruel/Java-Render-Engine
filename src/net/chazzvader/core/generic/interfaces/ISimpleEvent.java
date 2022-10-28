package net.chazzvader.core.generic.interfaces;

/**
 * A simple event, generally one that would not be worth the bother to add to
 * the event system. <br>
 * <br>
 * An example might be a button. You don't a complex and verbose system because
 * in all likelihood, only one class needs that event.
 * 
 * @author csbru
 *
 */
public interface ISimpleEvent {

	/**
	 * Runs the event, whatever that may be.
	 */
	public void event();

}
