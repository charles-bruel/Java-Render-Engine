package net.chazzvader.core.generic.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import org.lwjgl.glfw.GLFW;

import net.chazzvader.core.generic.engine.creator.TextureCreator;
import net.chazzvader.core.generic.engine.render.material.Texture;
import net.chazzvader.core.generic.engine.uilegacy.element.text.TextAlignment;
import net.chazzvader.core.generic.math.Vector3f;

/**
 * A class for various text related activities, primarily creating and rendering
 * textures for text, text boxes, and other similar objects.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class TextUtils {// TODO: Lots of reused code.

	private TextUtils() {
	}

	private static Font font;

	/**
	 * The font height used for rendering the vector, which is then upscaled, and
	 * then rasterized. This just has to be consistent.
	 */
	public static int CONSTANT_FONT_HEIGHT = 20;

	/**
	 * The standard background color for text boxes.
	 */
	public static Vector3f TEXT_BOX_BACKGROUND_COLOR = new Vector3f(0.95f, 0.95f, 0.95f);

	/**
	 * The standard highlight color for text boxes.
	 */
	public static Vector3f TEXT_BOX_HIGHLIGHT_COLOR = new Vector3f(0, 0.47f, 0.84f);

	static {
		TextUtils.setFont(new Font("Arial", 0, CONSTANT_FONT_HEIGHT));
	}

	/**
	 * Sets the current font to create objects from. This only has an effect on this
	 * class.
	 * 
	 * @param font The new font
	 */
	public static void setFont(Font font) {
		TextUtils.font = new Font(font.getName(), font.getStyle(), CONSTANT_FONT_HEIGHT);
	}

	/*
	 * Draws the desired text to a texture and returns it. Uses swing, awt, and
	 * GlyphVector to render to image, and then saves it as a texture.
	 * 
	 * @param text The text to draw.
	 * 
	 * @return A preloaded texture object.
	 */
	/*
	 * public static Texture renderToTexture(String text) { return
	 * renderToTexture(text, -1); }
	 */

	/**
	 * Draws the desired text to a texture and returns it. Uses swing, awt, and
	 * GlyphVector to render to image, and then saves it as a texture. Additional
	 * parameter for max width used. As of now, this does not make new lines.
	 * 
	 * @param text   The text to draw.
	 * @param width  The max width, in pixels.
	 * @param height The font height.
	 * @return A preloaded texture object.
	 * 
	 */
	public static Texture renderToTexture(String text, int width, int height) {
		return renderToTexture(text, width, height, TextAlignment.LEFT);
	}

	/**
	 * Draws the desired text to a texture and returns it. Uses swing, awt, and
	 * GlyphVector to render to image, and then saves it as a texture. Additional
	 * parameter for max width used. As of now, this does not make new lines.
	 * 
	 * @param text   The text to draw.
	 * @param width  The max width, in pixels.
	 * @param align  The text align of this text.
	 * @param height The font height.
	 * @return A preloaded texture object.
	 */
	public static Texture renderToTexture(String text, int width, int height, TextAlignment align) {
		Rectangle2D bounds = getBounds(text);
		int texWidth = (int) bounds.getWidth() + 2;
		if (width != -1) {
			texWidth = width;
		}
		int texHeight = (int) bounds.getHeight();
		int fontHeight = getFontHeight();
		if (fontHeight > texHeight) {
			texHeight = fontHeight;
		}
		texHeight += 2;

		texWidth *= (float) height / CONSTANT_FONT_HEIGHT;
		texHeight *= (float) height / CONSTANT_FONT_HEIGHT;

		return TextureCreator.fromRaw(renderToTextureRaw(text, width, height, align), texWidth, texHeight);
	}

	/**
	 * Draws the desired text to a texture array and returns it. Uses swing, awt,
	 * and GlyphVector to render to image, and then saves it as a texture.
	 * Additional parameter for max width used. As of now, this does not make new
	 * lines.
	 * 
	 * @param text   The text to draw.
	 * @param width  The max width, in pixels.
	 * @param align  The text align of this text.
	 * @param height The font height.
	 * @return A array representing texture data.
	 */
	public static int[] renderToTextureRaw(String text, int width, int height, TextAlignment align) {
		// TODO: Really ought to clean this up.
		Rectangle2D bounds = getBounds(text);
		int texWidth = (int) bounds.getWidth() + 2;
		if (width != -1) {
			texWidth = width;
		}
		int texHeight = (int) bounds.getHeight();
		int fontHeight = getFontHeight();
		if (fontHeight > texHeight) {
			texHeight = fontHeight;
		}
		texHeight += 2;

		texWidth *= (float) height / CONSTANT_FONT_HEIGHT;
		texHeight *= (float) height / CONSTANT_FONT_HEIGHT;

		// Black and white (color added later) so its ok, and besides, there is not ABGR
		// option
		if (texWidth <= 0 || texHeight <= 0) {
			return Texture.TRANSPARENT.getPixels();
		}
		BufferedImage image = new BufferedImage(texWidth, texHeight, BufferedImage.TYPE_INT_ARGB);// Setup Texture
		Graphics2D graphics = image.createGraphics();
		graphics.setColor(new Color(0, 0, 0, 1));
		graphics.fillRect(0, 0, texWidth, texHeight);
		graphics.setColor(new Color(255, 255, 255, 255));

		AffineTransform at = new AffineTransform();// Setup translation
		at.scale((float) height / CONSTANT_FONT_HEIGHT, (float) height / CONSTANT_FONT_HEIGHT);
		float xTranslate = 0;
		switch (align) {
		case CENTER:
			xTranslate = texWidth / 2;
			xTranslate -= bounds.getWidth() / 2;
			break;
		case LEFT:// Default
			break;
		case RIGHT:
			xTranslate = texWidth;
			xTranslate -= bounds.getWidth();
			break;
		}

		at.translate(xTranslate, getFontAscent());// Finalize
		GlyphVector vector = TextUtils.font.createGlyphVector(new FontRenderContext(null, true, false), text);
		Shape shape = vector.getOutline();
		graphics.fill(at.createTransformedShape(shape));
		int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		return pixels;
	}

	/**
	 * Draws the desired text to a texture array and returns it. Uses swing, awt,
	 * and GlyphVector to render to image, and then saves it as a texture. As of
	 * now, this does not make new lines.
	 * 
	 * @param text        The text to draw.
	 * @param align       The text align of this text.
	 * @param height      The font height.
	 * @param aspectRatio The ratio of wdith to height.
	 * @return A preloaded texture object.
	 */
	public static Texture renderToTexture(String text, int height, float aspectRatio, TextAlignment align) {
//		int newHeight = height * 2;
		int width = (int) (height * aspectRatio);
		if (width <= 0)
			width = 2;
		if (height <= 0)
			height = 2;
		int[] data = renderToTextureRaw(text, height, aspectRatio, align);
		Texture tex = TextureCreator.fromRaw(data, width, height);
		return tex;
	}

	/**
	 * Draws the desired text to a texture array and returns it. Uses swing, awt,
	 * and GlyphVector to render to image, and then saves it as a texture. As of
	 * now, this does not make new lines.
	 * 
	 * @param text        The text to draw.
	 * @param align       The text align of this text.
	 * @param height      The font height.
	 * @param aspectRatio The ratio of wdith to height.
	 * @return A array representing texture data.
	 */
	public static int[] renderToTextureRaw(String text, int height, float aspectRatio, TextAlignment align) {
		int width = (int) (height * aspectRatio);
		if (width <= 0) {
			width = 2;
		}
		if (height <= 0) {
			height = 2;
		}
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		
		
		GlyphVector vector = TextUtils.font.createGlyphVector(new FontRenderContext(null, true, false), text);
		float heightRatio = (float) height / CONSTANT_FONT_HEIGHT;
		AffineTransform at = new AffineTransform();// Setup translation
		at.scale(heightRatio, heightRatio);
		Shape shape = vector.getOutline();

		float xTranslate = 0;
		switch (align) {
		case CENTER:
			xTranslate = (width / heightRatio) / 2;
			xTranslate -= shape.getBounds().getWidth() / 2;
			break;
		case LEFT:// Default
			break;
		case RIGHT:
			xTranslate = width / heightRatio;
			xTranslate -= shape.getBounds().getWidth();
			break;
		}
		

		at.translate(xTranslate, getFontAscent() - 3.5f);// Finalize
		graphics.fill(at.createTransformedShape(shape));
		int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		
		
		return pixels;
	}

	/**
	 * Gets the bounds (the size) using the constant font height. Used internally.
	 * 
	 * @param text The text.
	 * @return The bounds
	 */
	public static Rectangle2D getBounds(String text) {
		GlyphVector vector = TextUtils.font
				.createGlyphVector(new FontRenderContext(new AffineTransform(), false, false), text);
		Shape shape = vector.getOutline();
		Rectangle2D bounds = shape.getBounds2D();
		return bounds;
	}

	/**
	 * Converts a 3 component float vector to a int color code.
	 * 
	 * @param col The color to convert.
	 * @return The color.
	 */
	public static int vec3ToColorCode(Vector3f col) {
		return 255 << 24 | ((int) (col.z * 255)) << 16 | ((int) (col.y * 255)) << 8 | ((int) (col.x * 255));
	}

	/**
	 * Simple method to just a background highlight for selecting text. The texture
	 * height is one, but the height and aspect ratio is still used because thats
	 * what the other related methods use.
	 * 
	 * @param start       The start ratio.
	 * @param end         The end ratio.
	 * @param height      The height.
	 * @param aspectRatio The aspect ratio.
	 * @return A array representing texture data.
	 */
	public static int[] getTextBackgroundTextureRaw(float start, float end, int height, float aspectRatio) {
		int width = (int) (height * aspectRatio);
		int[] ret = new int[width];

		start *= width;
		end *= width;

		for (int i = 0; i < width; i++) {
			ret[i] = (i >= start && i <= end) ? vec3ToColorCode(TEXT_BOX_HIGHLIGHT_COLOR)
					: vec3ToColorCode(TEXT_BOX_BACKGROUND_COLOR);
		}

		return ret;
	}

	/**
	 * Simple method to just a background highlight for selecting text. The texture
	 * height is one, but the height and aspect ratio is still used because thats
	 * what the other related methods use.
	 * 
	 * @param start       The start ratio.
	 * @param end         The end ratio.
	 * @param height      The height.
	 * @param aspectRatio The aspect ratio.
	 * @return A preloaded texture object.
	 */
	public static Texture getTextBackgroundTexture(float start, float end, int height, float aspectRatio) {
		Texture tex = TextureCreator.fromRaw(getTextBackgroundTextureRaw(start, end, height, aspectRatio),
				(int) (height * aspectRatio), 1);
		return tex;
	}

	/**
	 * Returns a string that represents character typed. Supports full standard
	 * keyboard, registers caps lock.<br>
	 * This uses string not char because sometimes nothing needs to be returned.<br>
	 * A default filter accepting everything is provided.
	 * 
	 * @param glfwKeyCode The key code, using the GLFW system. Example:
	 *                    <code>GLFW.GLFW_KEY_A</code>. The key code only contains
	 *                    information about the key pressed, not shift of caps or
	 *                    any other function key.
	 * @param shift       Is the shift key pressed.
	 * @param caps        Is caps lock enabled.
	 * @return The string.
	 */
	public static String glfwKeycodeToChar(int glfwKeyCode, boolean shift, boolean caps) {
		return glfwKeycodeToChar(glfwKeyCode, shift, caps, new KeyFilterAll());
	}

	/**
	 * Returns a string that represents character typed. Supports full standard
	 * keyboard, registers caps lock.<br>
	 * This uses string not char because sometimes nothing needs to be returned.
	 * 
	 * @param glfwKeyCode The key code, using the GLFW system. Example:
	 *                    <code>GLFW.GLFW_KEY_A</code>. The key code only contains
	 *                    information about the key pressed, not shift of caps or
	 *                    any other function key.
	 * @param shift       Is the shift key pressed.
	 * @param caps        Is caps lock enabled.
	 * @param filter      The filter, allowing for example only alphanumeric keys.
	 * @return The string.
	 * @see IKeyFilter
	 */
	public static String glfwKeycodeToChar(int glfwKeyCode, boolean shift, boolean caps, IKeyFilter filter) {
		if (shift)
			caps = true;// shift affects all, caps only effect letters (caps down while pressing the "/"
						// key gives "/" as opposed to "?" with shift.
		switch (glfwKeyCode) {
		// Top/number row
		case GLFW.GLFW_KEY_GRAVE_ACCENT:
			if (shift) {
				return filter.valid('~') ? "~" : "";
			} else {
				return filter.valid('`') ? "`" : "";
			}
		case GLFW.GLFW_KEY_1:
			if (shift) {
				return filter.valid('!') ? "!" : "";
			} else {
				return filter.valid('1') ? "1" : "";
			}
		case GLFW.GLFW_KEY_2:
			if (shift) {
				return filter.valid('@') ? "@" : "";
			} else {
				return filter.valid('2') ? "2" : "";
			}
		case GLFW.GLFW_KEY_3:
			if (shift) {
				return filter.valid('#') ? "#" : "";
			} else {
				return filter.valid('3') ? "3" : "";
			}
		case GLFW.GLFW_KEY_4:
			if (shift) {
				return filter.valid('$') ? "$" : "";
			} else {
				return filter.valid('4') ? "4" : "";
			}
		case GLFW.GLFW_KEY_5:
			if (shift) {
				return filter.valid('%') ? "%" : "";
			} else {
				return filter.valid('5') ? "5" : "";
			}
		case GLFW.GLFW_KEY_6:
			if (shift) {
				return filter.valid('^') ? "^" : "";
			} else {
				return filter.valid('6') ? "6" : "";
			}
		case GLFW.GLFW_KEY_7:
			if (shift) {
				return filter.valid('&') ? "&" : "";
			} else {
				return filter.valid('7') ? "7" : "";
			}
		case GLFW.GLFW_KEY_8:
			if (shift) {
				return filter.valid('*') ? "*" : "";
			} else {
				return filter.valid('8') ? "8" : "";
			}
		case GLFW.GLFW_KEY_9:
			if (shift) {
				return filter.valid('(') ? "(" : "";
			} else {
				return filter.valid('9') ? "9" : "";
			}
		case GLFW.GLFW_KEY_0:
			if (shift) {
				return filter.valid(')') ? ")" : "";
			} else {
				return filter.valid('0') ? "0" : "";
			}
		case GLFW.GLFW_KEY_MINUS:
			if (shift) {
				return filter.valid('_') ? "_" : "";
			} else {
				return filter.valid('-') ? "-" : "";
			}
		case GLFW.GLFW_KEY_EQUAL:
			if (shift) {
				return filter.valid('+') ? "+" : "";
			} else {
				return filter.valid('=') ? "=" : "";
			}
			// Top letter row
		case GLFW.GLFW_KEY_Q:
			if (caps) {
				return filter.valid('Q') ? "Q" : "";
			} else {
				return filter.valid('q') ? "q" : "";
			}
		case GLFW.GLFW_KEY_W:
			if (caps) {
				return filter.valid('W') ? "W" : "";
			} else {
				return filter.valid('w') ? "w" : "";
			}
		case GLFW.GLFW_KEY_E:
			if (caps) {
				return filter.valid('E') ? "E" : "";
			} else {
				return filter.valid('e') ? "e" : "";
			}
		case GLFW.GLFW_KEY_R:
			if (caps) {
				return filter.valid('R') ? "R" : "";
			} else {
				return filter.valid('r') ? "r" : "";
			}
		case GLFW.GLFW_KEY_T:
			if (caps) {
				return filter.valid('T') ? "T" : "";
			} else {
				return filter.valid('t') ? "t" : "";
			}
		case GLFW.GLFW_KEY_Y:
			if (caps) {
				return filter.valid('Y') ? "Y" : "";
			} else {
				return filter.valid('y') ? "y" : "";
			}
		case GLFW.GLFW_KEY_U:
			if (caps) {
				return filter.valid('U') ? "U" : "";
			} else {
				return filter.valid('u') ? "u" : "";
			}
		case GLFW.GLFW_KEY_I:
			if (caps) {
				return filter.valid('I') ? "I" : "";
			} else {
				return filter.valid('i') ? "i" : "";
			}
		case GLFW.GLFW_KEY_O:
			if (caps) {
				return filter.valid('O') ? "O" : "";
			} else {
				return filter.valid('o') ? "o" : "";
			}
		case GLFW.GLFW_KEY_P:
			if (caps) {
				return filter.valid('P') ? "P" : "";
			} else {
				return filter.valid('p') ? "p" : "";
			}
		case GLFW.GLFW_KEY_LEFT_BRACKET:
			if (shift) {
				return filter.valid('{') ? "{" : "";
			} else {
				return filter.valid('[') ? "[" : "";
			}
		case GLFW.GLFW_KEY_RIGHT_BRACKET:
			if (shift) {
				return filter.valid('}') ? "}" : "";
			} else {
				return filter.valid(']') ? "]" : "";
			}
		case GLFW.GLFW_KEY_BACKSLASH:
			if (shift) {
				return filter.valid('|') ? "|" : "";
			} else {
				return filter.valid('\\') ? "\\" : "";
			}
			// Middle letter row/home row
		case GLFW.GLFW_KEY_A:
			if (caps) {
				return filter.valid('A') ? "A" : "";
			} else {
				return filter.valid('a') ? "a" : "";
			}
		case GLFW.GLFW_KEY_S:
			if (caps) {
				return filter.valid('S') ? "S" : "";
			} else {
				return filter.valid('s') ? "s" : "";
			}
		case GLFW.GLFW_KEY_D:
			if (caps) {
				return filter.valid('D') ? "D" : "";
			} else {
				return filter.valid('d') ? "d" : "";
			}
		case GLFW.GLFW_KEY_F:
			if (caps) {
				return filter.valid('F') ? "F" : "";
			} else {
				return filter.valid('f') ? "f" : "";
			}
		case GLFW.GLFW_KEY_G:
			if (caps) {
				return filter.valid('G') ? "G" : "";
			} else {
				return filter.valid('g') ? "g" : "";
			}
		case GLFW.GLFW_KEY_H:
			if (caps) {
				return filter.valid('H') ? "H" : "";
			} else {
				return filter.valid('h') ? "h" : "";
			}
		case GLFW.GLFW_KEY_J:
			if (caps) {
				return filter.valid('J') ? "J" : "";
			} else {
				return filter.valid('j') ? "j" : "";
			}
		case GLFW.GLFW_KEY_K:
			if (caps) {
				return filter.valid('K') ? "K" : "";
			} else {
				return filter.valid('k') ? "k" : "";
			}
		case GLFW.GLFW_KEY_L:
			if (caps) {
				return filter.valid('L') ? "L" : "";
			} else {
				return filter.valid('l') ? "l" : "";
			}
		case GLFW.GLFW_KEY_SEMICOLON:
			if (shift) {
				return filter.valid(':') ? ":" : "";
			} else {
				return filter.valid(';') ? ";" : "";
			}
		case GLFW.GLFW_KEY_APOSTROPHE:
			if (shift) {
				return filter.valid('\"') ? "\"" : "";
			} else {
				return filter.valid('\'') ? "\'" : "";
			}
			// Bottom letter row
		case GLFW.GLFW_KEY_Z:
			if (caps) {
				return filter.valid('Z') ? "Z" : "";
			} else {
				return filter.valid('z') ? "z" : "";
			}
		case GLFW.GLFW_KEY_X:
			if (caps) {
				return filter.valid('X') ? "X" : "";
			} else {
				return filter.valid('x') ? "x" : "";
			}
		case GLFW.GLFW_KEY_C:
			if (caps) {
				return filter.valid('C') ? "C" : "";
			} else {
				return filter.valid('c') ? "c" : "";
			}
		case GLFW.GLFW_KEY_V:
			if (caps) {
				return filter.valid('V') ? "V" : "";
			} else {
				return filter.valid('v') ? "v" : "";
			}
		case GLFW.GLFW_KEY_B:
			if (caps) {
				return filter.valid('B') ? "B" : "";
			} else {
				return filter.valid('b') ? "b" : "";
			}
		case GLFW.GLFW_KEY_N:
			if (caps) {
				return filter.valid('N') ? "N" : "";
			} else {
				return filter.valid('n') ? "n" : "";
			}
		case GLFW.GLFW_KEY_M:
			if (caps) {
				return filter.valid('M') ? "M" : "";
			} else {
				return filter.valid('m') ? "m" : "";
			}
		case GLFW.GLFW_KEY_COMMA:
			if (shift) {
				return filter.valid('<') ? "<" : "";
			} else {
				return filter.valid(',') ? "," : "";
			}
		case GLFW.GLFW_KEY_PERIOD:
			if (shift) {
				return filter.valid('>') ? ">" : "";
			} else {
				return filter.valid('.') ? "." : "";
			}
		case GLFW.GLFW_KEY_SLASH:
			if (shift) {
				return filter.valid('?') ? "?" : "";
			} else {
				return filter.valid('/') ? "/" : "";
			}
		case GLFW.GLFW_KEY_SPACE:
			return " ";
		default:
			// TODO: Possible something allows this to be uncommented without tons of
			// warnings
			// Logging.log("could not find a key matching code " + glfwKeyCode + ",
			// returning \"\"", "Text Utils",
			// LoggingLevel.WARN);
			return "";
		}
	}

	/*
	 * public static int getFontHeight(Font font) { setFont(font); return
	 * getFontHeight(); }
	 */

	/**
	 * Gets the height, in pixels of the font being used.
	 * 
	 * @return The font height.
	 */
	public static int getFontHeight() {
		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);// Really doesn't matter, just need a
																					// graphics object
		Graphics graphics = image.getGraphics();
		FontMetrics metrics = graphics.getFontMetrics(font);
		int ret = metrics.getMaxAscent() + metrics.getMaxDescent();
		return ret;
	}

	/**
	 * Gets the font ascent, in pixels of the font being used.
	 * 
	 * @return The font ascent.
	 */
	public static int getFontAscent() {
		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);// Really doesn't matter, just need a
																					// graphics object
		Graphics graphics = image.getGraphics();
		FontMetrics metrics = graphics.getFontMetrics(font);
		int ret = metrics.getMaxAscent();
		return ret;
	}

	/**
	 * Gets the font descent, in pixels of the font being used.
	 * 
	 * @return The font descent.
	 */
	public static int getFontDescent() {
		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);// Really doesn't matter, just need a
																					// graphics object
		Graphics graphics = image.getGraphics();
		FontMetrics metrics = graphics.getFontMetrics(font);
		int ret = metrics.getMaxDescent();
		return ret;
	}

}
