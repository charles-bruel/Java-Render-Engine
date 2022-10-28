package net.chazzvader.core.opengl.engine.render;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.render.material.Texture;
import net.chazzvader.core.generic.util.Utils;
import net.chazzvader.core.opengl.engine.util.OpenGLStateMachine;

/**
 * The texture implementation for OpenGL.
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
public class OpenGLTexture extends Texture {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(this.data);
		result = prime * result + (this.deleted ? 1231 : 1237);
		result = prime * result + ((this.file == null) ? 0 : this.file.hashCode());
		result = prime * result + this.height;
		result = prime * result + this.id;
		result = prime * result + (this.interpolate ? 1231 : 1237);
		result = prime * result + (this.preparing ? 1231 : 1237);
		result = prime * result + (this.tile ? 1231 : 1237);
		result = prime * result + this.width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OpenGLTexture other = (OpenGLTexture) obj;
		if (!Arrays.equals(this.data, other.data))
			return false;
		if (this.deleted != other.deleted)
			return false;
		if (this.file == null) {
			if (other.file != null)
				return false;
		} else if (!this.file.equals(other.file))
			return false;
		if (this.height != other.height)
			return false;
		if (this.id != other.id)
			return false;
		if (this.interpolate != other.interpolate)
			return false;
		if (this.preparing != other.preparing)
			return false;
		if (this.tile != other.tile)
			return false;
		if (this.width != other.width)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OpenGLTexture [data=" + Arrays.toString(this.data) + ", file=" + this.file + ", width=" + this.width
				+ ", height=" + this.height + ", id=" + this.id + ", interpolate=" + this.interpolate + ", tile="
				+ this.tile + ", preparing=" + this.preparing + ", deleted=" + this.deleted + ", prepared="
				+ this.prepared + ", loaded=" + this.loaded + ", dontDeletePixelData=" + this.dontDeletePixelData
				+ ", dontDelete=" + this.dontDelete + "]";
	}

	private int[] data = null;
	private String file = null;
	private int width = 0;
	private int height = 0;
	private int id = -1;
	private boolean interpolate;
	private boolean tile;

	/**
	 * Loads a texture from a raw data array and width and height measures.
	 * 
	 * @param raw    The raw data. Each value is an int. Use hexadecimal notation.
	 *               <code>0xFFFFFFFF</code> is a perfectly valid integer in java,
	 *               and the it goes the channels go (left to right in the
	 *               hexadecimal) <code>alpha</code>, <code>blue</code>,
	 *               <code>green</code>, <code>red</code>.
	 * @param width  The width of the texture. Ensure
	 *               <code>width * height == raw.length</code>
	 * @param height The height of the texture. Ensure
	 *               <code>width * height == raw.length</code>
	 */
	public OpenGLTexture(int[] raw, int width, int height, boolean interpolate, boolean tile) {
		prepared = true;
		data = raw;
		this.width = width;
		this.height = height;
		this.interpolate = interpolate;
		this.tile = tile;
	}

	/**
	 * Loads a texture from a raw data array and width and height measures.
	 * 
	 * @param raw    The raw data. Each value is an int. Use hexadecimal notation.
	 *               <code>0xFFFFFFFF</code> is a perfectly valid integer in java,
	 *               and the it goes the channels go (left to right in the
	 *               hexadecimal) <code>alpha</code>, <code>blue</code>,
	 *               <code>green</code>, <code>red</code>.
	 * @param width  The width of the texture. Ensure
	 *               <code>width * height == raw.length</code>
	 * @param height The height of the texture. Ensure
	 *               <code>width * height == raw.length</code>
	 */
	public OpenGLTexture(int[] raw, int width, int height) {
		this(raw, width, height, true, false);
	}

	/**
	 * Loads a texture from a file. Any filetype java supports should work just
	 * fine.
	 * 
	 * @param file The file to load from.
	 */
	public OpenGLTexture(String file) {
		this.file = file;
	}

	private boolean preparing = false;
	
	@Override
	public void prepare() {
		checkDelete();
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				int[] pixels = null;
				try {
					BufferedImage image = ImageIO.read(Utils.getFromPath(file));
					width = image.getWidth();
					height = image.getHeight();
					pixels = new int[width * height];
					image.getRGB(0, 0, width, height, pixels, 0, width);
				} catch (IOException e) {
					e.printStackTrace();
				}
				data = new int[width * height];
				for (int i = 0; i < width * height; i++) {
					int a = (pixels[i] & 0xff000000) >> 24;
					int r = (pixels[i] & 0x00ff0000) >> 16;
					int g = (pixels[i] & 0x0000ff00) >> 8;
					int b = (pixels[i] & 0x000000ff) >> 0;

					data[i] = a << 24 | b << 16 | g << 8 | r << 0;
				}
				prepared = true;
				preparing = false;
			}
		});
		thread.setDaemon(true);
		thread.setName("Texture Load Thread");
		thread.start();
		preparing = true;
	}

	@Override
	public boolean load() {
		checkDelete();
		if(preparing) {
			return false;
		} else {
			if(!prepared) {
				prepare();
				return false;
			}
		}
		id = GL11.glGenTextures();
		OpenGLStateMachine.bindTexture(id);
		if (interpolate) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		}
		if(tile) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		}
		while(data == null) {
			System.out.println(this);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				Utils.createIntBuffer(data));
		if (canDeletePixelData()) {
			data = null;
		}
		loaded = true;
		return true;
	}

	@Override
	public void bind() {
		checkDelete();
		if (!loaded) {
			if(!load()) {
				Texture.MISSING.bind();
				return;
			}
		}
		OpenGLStateMachine.bindTexture(id);
	}

	@Override
	public void unbind() {
		OpenGLStateMachine.bindTexture(0);
	}

	@Override
	public int getWidth() {
		checkDelete();
		if (!prepared)
			prepare();
		return width;
	}

	@Override
	public int getHeight() {
		checkDelete();
		if (!prepared)
			prepare();
		return height;
	}

	@Override
	public void swapData(int[] data, int startX, int startY, int endX, int endY) {// TODO: Make this update pixel data
																					// if nessecary.
		checkDelete();
		if (startX < 0 || startY < 0) {
			Logging.log("Start coordinate less than zero for data swap!", "Texture", LoggingLevel.ERR);
			return;
		}
		if (endX > width || endY > height) {
			Logging.log("End coordinate greater than texture size for data swap!", "Texture", LoggingLevel.ERR);
			return;
		}
		OpenGLStateMachine.bindTexture(id);
		GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, startX, startY, endX - startX, endY - startY, GL11.GL_RGBA,
				GL11.GL_UNSIGNED_BYTE, data);
	}

	@Override
	public int[] getPixels() {
		checkDelete();
		return data;
	}

	@Override
	public void delete() {
		checkDelete();
		if (dontDelete) {
			return;
		}
		GL11.glDeleteTextures(id);
		data = null;
		deleted = true;
		super.delete();
	}

	private boolean deleted = false;

	private void checkDelete() {
		if (deleted) {
			Logging.log("Texture Deleted", "Texture", LoggingLevel.ERR);
		}
	}

	public int getId() {
		return this.id;
	}

}
