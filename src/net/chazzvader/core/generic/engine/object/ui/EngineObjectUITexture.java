package net.chazzvader.core.generic.engine.object.ui;

import net.chazzvader.core.generic.engine.creator.ObjectCreator;
import net.chazzvader.core.generic.engine.mesh.Mesh;
import net.chazzvader.core.generic.engine.object.EngineObjectMesh;
import net.chazzvader.core.generic.engine.render.material.MaterialUILegacy;
import net.chazzvader.core.generic.engine.render.material.Texture;
import net.chazzvader.core.generic.engine.uilegacy.element.UIElement;
import net.chazzvader.core.generic.engine.uilegacy.element.UIElementTexture;
import net.chazzvader.core.generic.math.Vector2f;
import net.chazzvader.core.generic.math.Vector3f;

/**
 * An engine object to represent a UI texture. Not part of a simple scene
 * hierarchy.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
@Deprecated
public class EngineObjectUITexture extends EngineObjectMesh implements IEngineObjectUI {

	private UIElement element;
	private MaterialUILegacy material;

	/**
	 * Constructor, DO NOT USE!<br>
	 * You will almost always want to use the UIElementTexture or some variation for
	 * this.
	 * 
	 * @param texture The texture to display.
	 * @param element The UI element to link to.
	 * @see UIElementTexture
	 */
	public EngineObjectUITexture(Texture texture, UIElement element) {
		this(texture, element,
				ObjectCreator.quadTextured(new Vector3f(-1, -1, 0), new Vector3f(1, -1, 0), new Vector3f(-1, 1, 0),
						new Vector3f(1, 1, 0), new Vector2f(0, 1), new Vector2f(1, 1), new Vector2f(0, 0),
						new Vector2f(1, 0)));
	}

	/**
	 * Constructor, DO NOT USE!<br>
	 * You will almost always want to use the UIElementTexture or some variation for
	 * this.
	 * 
	 * @param texture    The texture to display.
	 * @param element    The UI element to link to.
	 * @param customMesh A custom mesh for rendering.
	 * @see UIElementTexture
	 */
	public EngineObjectUITexture(Texture texture, UIElement element, Mesh customMesh) {
		super(customMesh, new MaterialUILegacy(texture, element));
		this.material = (MaterialUILegacy) super.material;
		this.element = element;
		this.material.color_multiplier = new Vector3f(1, 1, 1);
		float aspectRatio = (float) texture.getWidth() / (float) texture.getHeight();
		this.relativeScale.x = aspectRatio;
		provider = element;
	}

	/**
	 * Sets the texture to display
	 * 
	 * @param texture The new texture
	 */
	public void setTexture(Texture texture) {
		material.texture = texture;
		material.background = Texture.TRANSPARENT;
	}

	/**
	 * Gets the aspect ratio of the texture, the ratio between texture width and
	 * height
	 * 
	 * @return The aspect ratio.
	 */
	public float getAspectRatio() {
		return this.relativeScale.x;
	}

	@Override
	public UIElement getParentElement() {
		return element;
	}

}
