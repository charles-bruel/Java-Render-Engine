package net.chazzvader.core.generic.engine.object.text;

import java.awt.Font;

import net.chazzvader.core.generic.engine.creator.ObjectCreator;
import net.chazzvader.core.generic.engine.render.material.MaterialSimple;
import net.chazzvader.core.generic.math.Vector2f;
import net.chazzvader.core.generic.math.Vector3f;

/**
 * An engine object to represent text not part of the UI. The text object is
 * relatively primitive, entirely 2d and no complex shading. Can be quickly
 * updated.
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
public class EngineObjectTextNonUI extends EngineObjectTextBase {

	private MaterialSimple material;

	/**
	 * Constructor, fairly self explanatory. Any sizing can be done by changing the
	 * scale.
	 * 
	 * @param font   The font to use.
	 * @param string The text to draw, can be updated later.
	 */
	public EngineObjectTextNonUI(Font font, String string) {
		super(ObjectCreator.quadTextured(new Vector3f(0, 0, 0), new Vector3f(1, 0, 0), new Vector3f(0, 1, 0),
				new Vector3f(1, 1, 0), new Vector2f(0, 1), new Vector2f(1, 1), new Vector2f(0, 0), new Vector2f(1, 0)),
				new MaterialSimple(null, new Vector3f(0, 0, 0)), font, string);
	}

	@Override
	protected void updateMaterialTexture() {
		if (this.material == null)
			this.material = (MaterialSimple) super.material;
		this.material.texture = super.texture;
	}

	@Override
	public void setColor(Vector3f color) {
		material.color = color;
	}

}
