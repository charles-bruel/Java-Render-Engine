package net.chazzvader.core.generic.engine.ui;

/**
 * The scaling basis, helps deal with different size screens. This is optimized
 * for games, so by default it is based on height, you wouldn't want
 * transitioning to a 21:9 monitor to increase the UI size. However, in certain
 * cases, notably stack elements, this is not desirable.
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
@SuppressWarnings("javadoc")
public enum ScalingBasis {

	HEIGHT, WIDTH;

}
