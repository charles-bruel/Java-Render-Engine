package net.chazzvader.core.generic.engine.object.text;

import java.awt.Font;

import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.mesh.Mesh;
import net.chazzvader.core.generic.engine.object.EngineObjectMesh;
import net.chazzvader.core.generic.engine.render.material.Material;
import net.chazzvader.core.generic.engine.render.material.Texture;
import net.chazzvader.core.generic.engine.uilegacy.element.text.TextAlignment;
import net.chazzvader.core.generic.math.Vector2i;
import net.chazzvader.core.generic.math.Vector3f;
import net.chazzvader.core.generic.util.TextUtils;

/**
 * A basic text object, abstract because there are two separate implementations,
 * one for UI and one for non UI.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public abstract class EngineObjectTextBase extends EngineObjectMesh {

	protected Font font;
	protected String string;
	protected Vector2i size = new Vector2i(1, 1);
	protected int maxWidth = -1;
	protected Texture texture;

	/**
	 * The text alignment of the text, what direction its from. Defaults to left.
	 */
	public TextAlignment textAligment = TextAlignment.LEFT;

	/**
	 * A constructor doing some main stuff.
	 * 
	 * @param mesh     The mesh, provided by implementation.
	 * @param material The material, provided by implementation.
	 * @param font     The font.
	 * @param string   The text.
	 */
	public EngineObjectTextBase(Mesh mesh, Material material, Font font, String string) {
		super(mesh, material);
		this.font = font;
		this.string = string;
		updateFlag = true;
	}

	private boolean updateFlag = false;

	/**
	 * Forces <code>needNewTextureObject()</code> to return true. However, an
	 * implementation of that method could chose too ignore the flag. That is
	 * inadvisable, and should be changed immediately, however oversights happen.
	 */
	public boolean forceNewTextureFlag = false;

	/**
	 * Does the whole process of updating the texture, as that is not done every
	 * frame. Typically called by a change, can also be called manually.<br>
	 * Generates a new textures and then uploads it to the shader. This will attempt
	 * to edit the existing texture if the size is the same.
	 */
	public void updateTexture() {
		generateTexture();
		updateMaterialTexture();
	}

	/**
	 * Changes the texture of the material. Abstract because different
	 * implementations use different materials.
	 */
	protected abstract void updateMaterialTexture();

	/**
	 * Modifies the text object.
	 * 
	 * @param string The new text.
	 */
	public void set(String string) {
		set(font, string);
	}

	/**
	 * Modifies the text object.
	 * 
	 * @param font The new font.
	 */
	public void set(Font font) {
		set(font, string);
	}

	/**
	 * Modifies the text object.
	 * 
	 * @param string The new text.
	 * @param font   The new font.
	 */
	public void set(Font font, String string) {
		set(font, string, maxWidth);
	}

	/**
	 * Modifies the text object.
	 * 
	 * @param font     The new font.
	 * @param maxWidth The new maxWidth.
	 */
	public void set(Font font, int maxWidth) {
		set(font, string, maxWidth);
	}

	/**
	 * Modifies the text object.
	 * 
	 * @param string   The new text.
	 * @param maxWidth The new maxWidth.
	 */
	public void set(String string, int maxWidth) {
		set(font, string, maxWidth);
	}

	/**
	 * Modifies the text object.
	 * 
	 * @param string   The new text.
	 * @param font     The new font.
	 * @param maxWidth The new maxWidth.
	 */
	public void set(Font font, String string, int maxWidth) {
		this.font = font;
		this.string = string;
		this.maxWidth = maxWidth;
		updateTexture();
	}

	/**
	 * Modifies the color of the object. Abstract because implementation is material
	 * dependent.
	 * 
	 * @param color The new color.
	 */
	public abstract void setColor(Vector3f color);

	protected Texture generateTexture() {
		checkDelete();
		TextUtils.setFont(font);
		if (needNewTextureObject()) {
			if (this.texture != null) {
				texture.delete();
			}
			this.texture = getTexture();
			size.x = texture.getWidth();
			size.y = texture.getHeight();
		} else {
			this.texture.swapData(getRawTexture(), 0, 0, size.x, size.y);
		}
		return texture.deletable();
	}

	protected boolean needNewTextureObject() {
		return true;
	}
	
	private float perlinLerp(float n) {
		return 6*n*n*n*n*n-15*n*n*n*n+10*n*n*n;
	}
	
	private int getHeight() {
		Vector3f scale = scale();
		float scaleVal = (scale.x+scale.y+scale.z)/3;
		if(scaleVal <= 0.5f) {
			return (int) ((5*perlinLerp(1-scaleVal)+10*perlinLerp(scaleVal)) * 10);
		} else {
			return (int) ((scaleVal + 0.25f)*100);
		}
	}

	protected int[] getRawTexture() {
		return TextUtils.renderToTextureRaw(this.string, this.maxWidth, getHeight(), this.textAligment);

	}

	protected Texture getTexture() {
		return TextUtils.renderToTexture(this.string, this.maxWidth, getHeight(), this.textAligment);
	}

	/**
	 * Returns the aspect ratio of the text, width divided by height.
	 * 
	 * @return The aspect ratio.
	 */
	public float getAspectRatio() {
		return (float) size.x / (float) size.y;
	}

	@Override
	public Vector3f scale() {
		return new Vector3f(relativeScale.x * getAspectRatio(), relativeScale.y, relativeScale.z);
	}

	@Override
	public void render() {
		checkDelete();
		if (updateFlag) {
			updateFlag = false;
			updateTexture();
		}
		super.render();
	}

	/**
	 * The maximum aspect ratio the specified width will allow.
	 * 
	 * @return The maximum aspect ratio
	 */
	public float getMaxAspectRatio() {
		if (maxWidth == -1) {
			return getAspectRatio();
		} else {
			return maxWidth / texture.getHeight();
		}
	}
	
	@Override
	public void delete() {
		checkDelete();
		texture.delete();
		texture = null;
		deleted = true;
		super.delete();
	}

	private boolean deleted = false;
	
	private void checkDelete() {
		if(deleted) {
			Logging.log("Text Deleted", "EngineObjectText", LoggingLevel.ERR);
		}
	}
}