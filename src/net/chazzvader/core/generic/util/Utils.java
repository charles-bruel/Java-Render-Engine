package net.chazzvader.core.generic.util;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import org.lwjgl.glfw.GLFW;

import net.chazzvader.core.generic.Configuration;
import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.Application;
import net.chazzvader.core.generic.math.Vector3f;

/**
 * Various static universal utilities
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class Utils {

	private Utils() {
	}

	/**
	 * Creates a byte buffer and fills it with the given array
	 * 
	 * @param array The bytes to fill the buffer with
	 * @return The buffer
	 */
	public static ByteBuffer createByteBuffer(byte[] array) {
		ByteBuffer result = ByteBuffer.allocateDirect(array.length).order(ByteOrder.nativeOrder());
		result.put(array).flip();
		return result;
	}

	/**
	 * Creates a float buffer and fills it with the given array
	 * 
	 * @param array The floats to fill the buffer with
	 * @return The buffer
	 */
	public static FloatBuffer createFloatBuffer(float[] array) {
		FloatBuffer result = ByteBuffer.allocateDirect(array.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		result.put(array).flip();
		return result;
	}

	/**
	 * Creates a int buffer and fills it with the given array
	 * 
	 * @param array The ints to fill the buffer with
	 * @return The buffer
	 */
	public static IntBuffer createIntBuffer(int[] array) {
		IntBuffer result = ByteBuffer.allocateDirect(array.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
		result.put(array).flip();
		return result;
	}

	/**
	 * Takes a file and returns the text content of the file.
	 * 
	 * @param file       The file path
	 * @param fileFormat The format of the file, say <code>UTF-8</code>
	 * @return The text content of the file
	 */
	public static String loadAsString(String file, String fileFormat) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(getFromPath(file), fileFormat));
			StringBuilder result = new StringBuilder();
			if (reader != null) {
				String buffer = "";
				while ((buffer = reader.readLine()) != null) {
					result.append(buffer + '\n');
				}
				reader.close();
				return result.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "ERROR";
	}

	/**
	 * Takes a file and returns the text content of the file. <code>Assumes UTF-8</code>.
	 * 
	 * @param file The file path
	 * @return The text content of the file
	 */
	public static String loadAsString(String file) {
		return loadAsString(file, "UTF-8");
	}

	/**
	 * Turns the byte buffer into a string of hex digits, used for debugging.
	 * 
	 * @param array The ByteBuffer.
	 * @return The hex digits
	 */
	public static String byteBufferToHex(ByteBuffer array) {
		byte[] ba = new byte[array.capacity()];
		array.get(ba);
		String ret = "";
		for (int i = 0; i < ba.length; i++) {
			ret += String.format("%02x", ba[i]);
		}
		array.flip();
		return ret;
	}

	/**
	 * Turns the int buffer into a string of hex digits, used for debugging.
	 * 
	 * @param array The IntBuffer.
	 * @return The hex digits
	 */
	public static String intBufferToHex(IntBuffer array) {
		int[] ba = new int[array.capacity()];
		array.get(ba);
		String ret = "";
		for (int i = 0; i < ba.length; i++) {
			ret += String.format("%02x", ba[i]);
		}
		array.flip();
		return ret;
	}

	/**
	 * Translates a java color into the equivalent in a Vector3f
	 * 
	 * @param color The color to translate.
	 * @return The result.
	 */
	public static Vector3f vectorFromColor(Color color) {
		return new Vector3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
	}

	/**
	 * Gets an input stream, a way to access the contents of a file, from a
	 * filepath. This applies the necessary processing to for example check the jar
	 * files as well as regular files.<br>
	 * <br>
	 * Use this for file loading.
	 * 
	 * @param file The filepath.
	 * @return The InputStream
	 */
	public static InputStream getFromPath(String file) {
		file = file.replace('\\', '/');
		InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
		if (stream == null) {
			try {
				Logging.log(
						"Could not find " + file
								+ " in jar, trying to load from file. This is bad unless you're in a dev enviorment.",
						"Utils", LoggingLevel.DEBUG);
				stream = new FileInputStream(Configuration.getFilepathPrefix() + file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return stream;
	}

	/**
	 * Turns a ByteBuffer into a float array. Does what it says on the tin.
	 * 
	 * @param buffer The buffer.
	 * @return The result.
	 */
	public static float[] byteBufferToFloat(ByteBuffer buffer) {
		if (buffer == null)
			return null;
		FloatBuffer floatBuffer = buffer.asFloatBuffer();
		float[] floatArray = new float[floatBuffer.capacity()];
		floatBuffer.get(floatArray);
		Logging.log(Arrays.toString(floatArray), "Buffer", LoggingLevel.INFO);
		return floatArray;
	}

	/**
	 * Returns the contents of the clipboard as a string.
	 * 
	 * @return The contents.
	 */
	public static String getClipboardContents() {
		if (Application.getInstance() != null) {
			if (Application.getInstance().getWindow() != null) {
				return GLFW.glfwGetClipboardString(Application.getInstance().getWindow().getPtr());
			}
		}
		return null;
	}

	/**
	 * Sets the contents of the clipboard to the passed string.
	 * 
	 * @param toSet What the clipboard to.
	 * 
	 * @return True if the operation was sucessful.
	 */
	public static boolean setClipboardContents(String toSet) {
		if (Application.getInstance() != null) {
			if (Application.getInstance().getWindow() != null) {
				GLFW.glfwSetClipboardString(Application.getInstance().getWindow().getPtr(), toSet);
				return true;
			}
		}
		return false;
	}

}
