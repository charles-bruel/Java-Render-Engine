package net.chazzvader.core.generic.util;

/**
 * Very simple key filter to let everything through.
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
public class KeyFilterAll implements IKeyFilter {

	@Override
	public boolean valid(char character) {
		return true;
	}

}
