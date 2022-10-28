package net.chazzvader.core.generic.interfaces;

/**
 * Basic validator
 * @author csbru
 * @since 1,0
 * @version 1
 */
public interface IValidatorBasic {
	
	/**
	 * Context dependent
	 * @return Is it is valid, again context dependent
	 */
	public boolean validate();

}
