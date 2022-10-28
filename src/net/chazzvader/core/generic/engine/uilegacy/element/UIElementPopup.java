package net.chazzvader.core.generic.engine.uilegacy.element;

import net.chazzvader.core.generic.engine.creator.TextureCreator;
import net.chazzvader.core.generic.engine.render.material.Texture;
import net.chazzvader.core.generic.engine.uilegacy.UILayer;
import net.chazzvader.core.generic.engine.uilegacy.UIStack;
import net.chazzvader.core.generic.engine.uilegacy.anchorpoint.AnchorPoint;
import net.chazzvader.core.generic.engine.uilegacy.anchorpoint.AnchorPointPopup;
import net.chazzvader.core.generic.engine.uilegacy.anchorpoint.AnchorPointRelative;
import net.chazzvader.core.generic.math.Vector2f;
import net.chazzvader.core.generic.math.Vector4f;

@Deprecated
@SuppressWarnings("javadoc")
public class UIElementPopup extends UIElementTexture {
	
	private static final Texture POPUP = TextureCreator.fromFile("net\\chazzvader\\core\\generic\\engine\\ui\\res\\popup.png");

	public float targetSize;
	
	public UIElementPopup(UIStack stack) {
		this(stack, false);
	}
	
	public UIElementPopup(UIStack stack, boolean includeClose) {
		this(stack, includeClose, 0.5f);
	}
	
	public UIElementPopup(UIStack stack, boolean includeClose, float targetSize) {
		super(new AnchorPointRelative(new Vector2f(0.5f, 0.5f)), POPUP, 0.1f, 0.1f);
		stack.pushLayer(new UILayer(stack, new Vector4f(0, 0, 0, 0.1f), true));
		stack.getTopLayer().add(this);
		this.targetSize = targetSize;
		this.sizeAbsolute = true;
		if(includeClose) {
			includeClose();
		}
		sizeSquish = 0.9f;
	}
	
	private void includeClose() {
		AnchorPoint ap = new AnchorPointPopup(this, new Vector2f(0.5f, 0.05f));
		UIElementButton closeButton = new UIElementButton(ap, "Close");
		closeButton.event = () -> {close();};
		closeButton.sizeAbsolute = true;
		closeButton.sizeSquish = 0.8f;
		getLayer().add(closeButton);
	}

	@Override
	public Vector2f getBaseSize() {
		float targetWidth = targetSize * getWindowAspectRatio();
		float targetHeight = targetSize;
		Vector2f temp = new Vector2f(targetWidth, targetHeight);
		return temp;
	}

	@Override
	public void refresh() {
		
	}
	
	public void close() {
		getLayer().getStack().popLayer();
	}

}