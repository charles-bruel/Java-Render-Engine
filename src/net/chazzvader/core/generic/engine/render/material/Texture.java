package net.chazzvader.core.generic.engine.render.material;

import net.chazzvader.core.generic.engine.EngineItem;
import net.chazzvader.core.generic.engine.IDeletable;
import net.chazzvader.core.generic.engine.creator.TextureCreator;
import net.chazzvader.core.opengl.engine.render.OpenGLTexture;

/**
 * A texture. Contains functionality to load from file or raw data.
 * Implementation depends on renderer.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 * @see OpenGLTexture
 */
public abstract class Texture extends EngineItem implements IDeletable {

	protected boolean prepared = false;
	protected boolean loaded = false;

	/**
	 * Is the texture prepared yet?
	 * 
	 * @return True if the texture is prepared.
	 */
	public boolean isPrepared() {
		return this.prepared;
	}

	/**
	 * Is the texture loaded yet?
	 * 
	 * @return True if the texture is loaded.
	 */
	public boolean isLoaded() {
		return this.loaded;
	}

	/**
	 * A basic transparent texture.
	 */
	public static final Texture TRANSPARENT = TextureCreator.fromRaw(new int[] { 0x00000000 }, 1, 1);

	/**
	 * A fallback texture, purple and black checkerboard.
	 */
	public static final Texture MISSING = TextureCreator
			.fromRaw(new int[] { 0xFF000000, 0xFFFF00FF, 0xFFFF00FF, 0xFF000000 }, 2, 2);
	/**
	 * A basic white texture.
	 */
	public static final Texture BLANK = TextureCreator.fromRaw(new int[] { 0xFFFFFFFF }, 1, 1);
	/**
	 * A basic texture for a normal map.
	 */
	public static final Texture BLANK_NORMALS = TextureCreator.fromRaw(new int[] { 0xFFFF7F7F }, 1, 1);

	static {
		TRANSPARENT.dontDeletePixelData();
		MISSING.dontDeletePixelData();
		BLANK.dontDeletePixelData();
		BLANK_NORMALS.dontDeletePixelData();

	}

	protected boolean dontDeletePixelData = false;

	/**
	 * Sets the <code>dontDeletePixelData</code> to true.
	 */
	public void dontDeletePixelData() {
		dontDeletePixelData = true;
	}

	protected boolean dontDelete = true;

	/**
	 * Marks this texture as deletable.
	 * 
	 * @return this.
	 */
	public Texture deletable() {
		dontDelete = false;
		return this;
	}

	/**
	 * Returns if the texture is deletable
	 * 
	 * @return Is the texture deletable
	 */
	public boolean isDeletable() {
		return !dontDelete;
	}

	/**
	 * Can the underlying pixel data of this texture be deleted once it is loaded
	 * into memory. For memory savings.
	 * 
	 * @return Can the data be deleted.
	 */
	public boolean canDeletePixelData() {
		return !dontDeletePixelData;
	}

	/**
	 * Prepares the texture by converting the color codes to the correct order. Many
	 * methods bypass this step.
	 */
	public abstract void prepare();

	/**
	 * Loads the texture into GPU VRAM.
	 * 
	 * @return If the operation was successful immediately. If false, assume it is
	 *         loading on a different thread and use a missing texture instead.
	 */
	public abstract boolean load();

	/**
	 * Binds the texture.
	 */
	public abstract void bind();

	/**
	 * Unbinds the texture.
	 */
	public abstract void unbind();

	/**
	 * Gets the width.
	 * 
	 * @return The width
	 */
	public abstract int getWidth();

	/**
	 * Gets the height.
	 * 
	 * @return The height.
	 */
	public abstract int getHeight();

	/**
	 * Returns the raw pixel data. This might be null or out of date if
	 * <code>canDeletePixelData()</code> returns true.
	 * 
	 * @see Texture#canDeletePixelData()
	 * @return The raw pixel data.
	 */
	public abstract int[] getPixels();

	/**
	 * Deletes the texture.
	 */
	public void delete() {
		super.delete();
	}

	/**
	 * Swaps a portion of the data.
	 * 
	 * @param rawTexture The new pixel data.
	 * @param startX     The lower X coordinate.
	 * @param startY     The upper X coordinate.
	 * @param endX       The lower Y coordinate.
	 * @param endY       The upper Y coordinate.
	 */
	public abstract void swapData(int[] rawTexture, int startX, int startY, int endX, int endY);

}