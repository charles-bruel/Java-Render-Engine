package net.chazzvader.core.generic.engine.object;

/**
 * A script that can be applied to a EngineObject
 * @author csbru
 * @since 1.0 
 * @version 1
 * @see EngineObject
 */
public interface IScript {
	
	/**
	 * Runs the script
	 * @param object The EngineObject this script is attached to
	 * @param delta The time, in seconds, since the last update
	 */
	public void run(EngineObject object, double delta);

}
