package net.chazzvader.core.generic.util;

import java.util.regex.Pattern;

/**
 * Very simple key filter to let word characters (alphanumeric or underscore)
 * through.<br>
 * <br>
 * <code>a-z A-Z 0-9 _</code>
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
public class KeyFilterWord implements IKeyFilter {

	@Override
	public boolean valid(char character) {
		return Pattern.matches("[\\w]*", ""+character);
	}

}
