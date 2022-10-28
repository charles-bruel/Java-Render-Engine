package net.chazzvader.core.generic.engine.uilegacy.element;

import org.lwjgl.glfw.GLFW;

import net.chazzvader.core.generic.engine.creator.TextureCreator;
import net.chazzvader.core.generic.engine.render.material.Texture;
import net.chazzvader.core.generic.engine.uilegacy.anchorpoint.AnchorPoint;

@Deprecated
@SuppressWarnings("javadoc")
public class UIElementToggleSwitch extends UIElementTexture {

	private static final Texture UNSELECTED = TextureCreator.fromFile("net\\chazzvader\\core\\generic\\engine\\ui\\res\\toggle_unselected.png");
	private static final Texture SELECTED = TextureCreator.fromFile("net\\chazzvader\\core\\generic\\engine\\ui\\res\\toggle_selected.png");
	
	public boolean toggled = false;
	
	public UIElementToggleSwitch(AnchorPoint anchorPoint) {
		super(anchorPoint, UNSELECTED, 0.075f, 0.0375f);
	}

	private boolean _toggled;
	
	@Override
	public void update() {
		if(toggled != _toggled) {
			_toggled = toggled;
			texture = toggled ? SELECTED : UNSELECTED;
		}
		super.update();
	}
	
	@Override
	public boolean needFocus() {
		return true;
	}
	
	@Override
	public boolean mouseButtonPressed(int button, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
			boolean capLock, boolean numLock, int clickCount) {
		if(mouseX >= 0 && mouseX <= 1 && mouseY >= 0 && mouseY <= 1) {
			toggled = !toggled;
			return true;
		} else {
			getLayer().getStack().focus = null;
		}
		return false;
	}
	
	@Override
	public boolean keyPressed(int key, int mods, boolean ctrl, boolean shift, boolean alt, boolean os, boolean capLock,
			boolean numLock) {
		if (key == GLFW.GLFW_KEY_ESCAPE) {
			getLayer().getStack().focus = null;// De-focus on escape
		}
		return false;
	}
	
	@Override
	public boolean selectable() {
		return true;
	}

}