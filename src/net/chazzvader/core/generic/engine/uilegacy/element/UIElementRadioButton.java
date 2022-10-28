package net.chazzvader.core.generic.engine.uilegacy.element;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;

import net.chazzvader.core.generic.engine.creator.TextureCreator;
import net.chazzvader.core.generic.engine.render.material.Texture;
import net.chazzvader.core.generic.engine.uilegacy.anchorpoint.AnchorPoint;

@Deprecated
@SuppressWarnings("javadoc")
public class UIElementRadioButton extends UIElementTexture {

	private static final Texture UNSELECTED = TextureCreator.fromFile("net\\chazzvader\\core\\generic\\engine\\ui\\res\\radio_unselected.png");
	private static final Texture SELECTED = TextureCreator.fromFile("net\\chazzvader\\core\\generic\\engine\\ui\\res\\radio_selected.png");
	
	public boolean selected = false;
	
	public UIElementRadioButton(AnchorPoint anchorPoint, UIElementRadioButton toLink) {
		this(anchorPoint);
		link(toLink);
	}
	
	public UIElementRadioButton(AnchorPoint anchorPoint) {
		super(anchorPoint, UNSELECTED, 0.075f, 0.075f);
		System.out.println(maintainAspectRatio);
	}

	private boolean _selected;
	
	private ArrayList<UIElementRadioButton> linked = new ArrayList<>();
	
	public void link(UIElementRadioButton toLink) {
		for(int i = 0;i < linked.size();i ++) {
			linked.get(i).linked.remove(this);
		}
		linked = new ArrayList<>();
		for(int i = 0;i < toLink.linked.size();i ++) {
			toLink.linked.get(i).linked.add(this);
			linked.add(toLink.linked.get(i));
		}
		toLink.linked.add(this);
		linked.add(toLink);
	}
	
	@Override
	public void update() {
		if(selected != _selected) {
			_selected = selected;
			texture = selected ? SELECTED : UNSELECTED;
			if(selected) {
				for(int i = 0;i < linked.size();i ++) {
					linked.get(i).selected = false;
				}
			}
		}
		super.update();
	}
	
	@Override
	public boolean needFocus() {
		return false;
	}
	
	@Override
	public boolean mouseButtonPressed(int button, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
			boolean capLock, boolean numLock, int clickCount) {
		if(mouseX >= 0 && mouseX <= 1 && mouseY >= 0 && mouseY <= 1) {
			selected = true;
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