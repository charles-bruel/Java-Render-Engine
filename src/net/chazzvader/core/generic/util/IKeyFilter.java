package net.chazzvader.core.generic.util;

/**
 * Provides an easy method to filter out character for input in text boxes and
 * such.
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
public interface IKeyFilter {

	/**
	 * Returns true if the character is valid to be typed.
	 * 
	 * @param character The character to test.
	 * @return True if the character is typable.
	 */
	public boolean valid(char character);

}
