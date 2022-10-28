package net.chazzvader.core.generic.engine.uilegacy.element;

import org.lwjgl.glfw.GLFW;

import net.chazzvader.core.generic.engine.creator.TextureCreator;
import net.chazzvader.core.generic.engine.render.material.Texture;
import net.chazzvader.core.generic.engine.uilegacy.anchorpoint.AnchorPoint;

@Deprecated
@SuppressWarnings("javadoc")
public class UIElementCheckbox extends UIElementTexture {

	private static final Texture UNCHECKED = TextureCreator.fromFile("net\\chazzvader\\core\\generic\\engine\\ui\\res\\checkbox_unchecked.png");
	private static final Texture CHECKED = TextureCreator.fromFile("net\\chazzvader\\core\\generic\\engine\\ui\\res\\checkbox_checked.png");
	
	public boolean checked = false;
	
	public UIElementCheckbox(AnchorPoint anchorPoint) {
		super(anchorPoint, UNCHECKED, 0.075f, 0.075f);
	}

	private boolean _checked;
	
	@Override
	public void update() {
		if(checked != _checked) {
			_checked = checked;
			texture = checked ? CHECKED : UNCHECKED;
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
			checked = !checked;
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

}