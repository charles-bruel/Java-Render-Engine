package net.chazzvader.core.generic.engine.ui;

/**
 * Represents a direction.
 * @author csbru
 * @since 1.0
 * @version 1
 */
@SuppressWarnings("javadoc")
public enum Direction {
	
	UP(0, 1), DOWN(0, -1), LEFT(-1, 0), RIGHT(1, 0), NONE(0, 0);

	/**
	 * The multiplier to translate an x direction vector to this direction.
	 */
	public int xMul;
	/**
	 * The multiplier to translate an x direction vector to this direction.
	 */
	public int yMul;
	
	private Direction(int xMul, int yMul) {
		this.xMul = xMul;
		this.yMul = yMul;
	}

}
