package net.chazzvader.core.generic.engine.event;

/**
 * The dispatcher type. Details in definition.
 * @author csbru
 * @version 1
 * @since 1.0
 */
public enum EventDispatcherType {

	/**
	 * <code>SIMPLE</code> is just doing checks and calling the correct method in the interface.<br>
	 * <code>COMPLEX</code> is the doing the entire thing in the method.
	 */
	SIMPLE,
	/**
	 * <code>SIMPLE</code> is just doing checks and calling the correct method in the interface.<br>
	 * <code>COMPLEX</code> is the doing the entire thing in the method.
	 */
	COMPLEX;
	
}