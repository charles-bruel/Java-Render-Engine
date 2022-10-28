package net.chazzvader.core.generic.engine.object.ui;

import net.chazzvader.core.generic.engine.uilegacy.element.UIElement;

/**
 * Simple interface marking an object as for the UI.
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
@Deprecated
public interface IEngineObjectUI {

	/**
	 * Returns the element associated with this object.
	 * @return The associated element.
	 */
	public UIElement getParentElement();

}