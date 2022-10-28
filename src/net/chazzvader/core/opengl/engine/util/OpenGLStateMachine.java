package net.chazzvader.core.opengl.engine.util;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import net.chazzvader.core.generic.Configuration;
import net.chazzvader.core.generic.Configuration.Renderer;

/**
 * Manages and keeps track of what OpenGL is doing, attempts to minimize GPU
 * calls, as that is expensive, especially with LWJGL.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class OpenGLStateMachine {

	private OpenGLStateMachine() {
	}

	/* FINAL FIELDS */
	private static final int MAX_TEXTURES;

	static {
		Configuration.assertRenderer(Renderer.OPEN_GL);
		MAX_TEXTURES = GL11.glGetInteger(GL20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS);
	}

	/* FIELDS */
	private static int currentShader = 0;
	private static int currentActiveTextureSlot = 0;
	private static int[] currentTexture = new int[MAX_TEXTURES];
	private static int currentFramebuffer = 0;
	private static HashMap<Integer, Boolean> currentEnables = new HashMap<Integer, Boolean>();
	private static int currentDepthFunction = 0;
	private static int currentBlendFunctionA = 0, currentBlendFunctionB = 0;
	private static int currentViewportWidth = 0, currentViewportHeight = 0;

	/*
	 * GETTERS
	 */

	/**
	 * The maximum number of bound textures at once.<br>
	 * Is the saved valued of
	 * <code>GL11.glGetInteger(GL20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS);</code>
	 * 
	 * @return The maximum number of bound textures at once.
	 */
	public static int getMaxTextures() {
		return MAX_TEXTURES;
	}

	/**
	 * The current bound shader, <em>assuming all bindings go through this
	 * class.</em>
	 * 
	 * @return The current bound shader.
	 */
	public static int getCurrentShader() {
		return currentShader;
	}

	/**
	 * The current active texture slot, <em>assuming all bindings go through this
	 * class.</em>
	 * 
	 * @return The current active texture slot.
	 */
	public static int getCurrentActiveTextureSlot() {
		return currentActiveTextureSlot;
	}

	/**
	 * The currently bound textures, in an array based on what slot they are bound
	 * to, <em>assuming all bindings go through this class.</em>
	 * 
	 * @return The currently bound textures.
	 */
	public static int[] getCurrentTextures() {
		return currentTexture;
	}

	/**
	 * The current texture in a specific texture slot, <em>assuming all bindings go
	 * through this class.</em>
	 * 
	 * @param index The texture slot to look up.
	 * @return The current texture in a specific texture slot.
	 */
	public static int getCurrentTexture(int index) {
		return currentTexture[index];
	}

	/**
	 * The current texture in a the active texture slot, <em>assuming all bindings
	 * go through this class.</em>
	 * 
	 * @return The current texture.
	 */
	public static int getCurrentTexture() {
		return currentTexture[getCurrentActiveTextureSlot()];
	}

	/*
	 * SETTERS
	 */

	/**
	 * Checks and makes sure the shader isn't already bound, and then binds it.
	 * 
	 * @param program The shader program.
	 * @see GL20#glUseProgram(int)
	 */
	public static void bindShader(int program) {
		if (currentShader == program) {
			return;
		}
		bindShaderOverride(program);
	}

	/**
	 * Binds the shader, ignoring the status of the state machine.
	 * 
	 * @param program The shader program.
	 * @see GL20#glUseProgram(int)
	 */
	public static void bindShaderOverride(int program) {
		currentShader = program;
		GL20.glUseProgram(program);
	}

	/**
	 * Checks and makes sure the active slot isn't already set to that, and then
	 * sets it.
	 * 
	 * @param slot The slot to set it to.
	 * @see GL13#glActiveTexture(int)
	 */
	public static void setActiveTextureSlot(int slot) {
		if (currentActiveTextureSlot == slot) {
			return;
		}
		setActiveTextureSlotOverride(slot);
	}

	/**
	 * Sets the active texture slot, ignoring the status of the state machine.
	 * 
	 * @param slot The slot to set it to.
	 * @see GL13#glActiveTexture(int)
	 */
	public static void setActiveTextureSlotOverride(int slot) {
		currentActiveTextureSlot = slot;
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + slot);
	}

	/**
	 * Checks and makes sure the texture isn't already bound, and then binds it.
	 * 
	 * @param textureType The texture type, i.e. <code>GL11.GL_TEXTURE_2D</code>
	 * @param texture     The texture.
	 * @see GL11#glBindTexture(int, int)
	 */
	public static void bindTexture(int textureType, int texture) {
		if (texture == currentTexture[currentActiveTextureSlot])
			return;
		bindTextureOverride(textureType, texture);
	}

	/**
	 * Checks and makes sure the texture isn't already bound, and then binds it.
	 * Assumes standard <code>GL11.GL_TEXTURE_2D</code> texture type.
	 * 
	 * @param texture The texture.
	 * @see GL11#glBindTexture(int, int)
	 */
	public static void bindTexture(int texture) {
		bindTexture(GL11.GL_TEXTURE_2D, texture);
	}

	/**
	 * Bind the texture, ignoring the status of the state machine.
	 * 
	 * @param textureType The texture type, i.e. <code>GL11.GL_TEXTURE_2D</code>
	 * @param texture     The texture.'
	 * @see GL11#glBindTexture(int, int)
	 */
	public static void bindTextureOverride(int textureType, int texture) {
		currentTexture[currentActiveTextureSlot] = texture;
		GL11.glBindTexture(textureType, texture);
	}

	/**
	 * Checks and makes sure the framebuffer isn't already bound, and then binds it.
	 * 
	 * @param framebuffer The framebuffer.
	 * @see GL30#glBindFramebuffer(int, int)
	 */
	public static void bindFramebuffer(int framebuffer) {
		if (framebuffer == currentFramebuffer) {
			return;
		}
		bindFramebufferOverride(framebuffer);
	}

	/**
	 * Bind the framebuffer, ignoring the status of the state machine.
	 * 
	 * @param framebuffer The framebuffer.
	 * @see GL30#glBindFramebuffer(int, int)
	 */
	public static void bindFramebufferOverride(int framebuffer) {
		currentFramebuffer = framebuffer;
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebuffer);
	}

	/**
	 * Enables the target item.
	 * 
	 * @param item An item, i.e. <code>GL11.GL_BLEND</code>.
	 * @see GL11#glEnable(int)
	 */
	public static void enableValue(int item) {
		setValue(item, true);
	}

	/**
	 * Disables the target item.
	 * 
	 * @param item An item, i.e. <code>GL11.GL_BLEND</code>.
	 * @see GL11#glDisable(int)
	 */
	public static void disableValue(int item) {
		setValue(item, false);
	}

	/**
	 * Set the value of the target item.
	 * 
	 * @param item  An item, i.e. <code>GL11.GL_BLEND</code>.
	 * @param value The value, true or false.
	 * @see GL11#glEnable(int)
	 * @see GL11#glDisable(int)
	 */
	public static void setValue(int item, boolean value) {
		if (currentEnables.containsKey(Integer.valueOf(item))) {
			if (currentEnables.get(Integer.valueOf(item)).booleanValue() == value) {
				return;
			}
		}
		setValueOverride(item, value);
	}

	/**
	 * Set the value of the target item, ignoring the status of the state machine.
	 * 
	 * @param item  An item, i.e. <code>GL11.GL_BLEND</code>.
	 * @param value The value, true or false.
	 * @see GL11#glEnable(int)
	 * @see GL11#glDisable(int)
	 */
	public static void setValueOverride(int item, boolean value) {
		if (!currentEnables.containsKey(Integer.valueOf(item))) {
			currentEnables.put(Integer.valueOf(item), Boolean.valueOf(value));
		} else {
			currentEnables.replace(Integer.valueOf(item), Boolean.valueOf(value));
		}
		if (value) {
			GL11.glEnable(item);
		} else {
			GL11.glDisable(item);
		}
	}

	/**
	 * Sets the depth function.
	 * 
	 * @param depthFunction The new depth function, i.e. <code>GL11.GL_LESS</code>.
	 * @see GL11#glDepthFunc(int)
	 */
	public static void setDepthFunction(int depthFunction) {
		if (currentDepthFunction == depthFunction) {
			return;
		}
		setDepthFunctionOverride(depthFunction);
	}

	/**
	 * Sets the depth function, ignoring the status of the state machine.
	 * 
	 * @param depthFunction The new depth function, i.e. <code>GL11.GL_LESS</code>.
	 * @see GL11#glDepthFunc(int)
	 */
	public static void setDepthFunctionOverride(int depthFunction) {
		currentDepthFunction = depthFunction;
		GL11.glDepthFunc(depthFunction);
	}

	/**
	 * Sets the blend function.
	 * 
	 * @param blendFunctionA The new first blend function, i.e.
	 *                       <code>GL11.GL_SRC_ALPHA</code>.
	 * @param blendFunctionB The new second blend function, i.e.
	 *                       <code>GL11.GL__ONE_MINUS_SRC_ALPHA</code>.
	 * @see GL11#glBlendFunc(int, int)
	 */
	public static void setBlendFunction(int blendFunctionA, int blendFunctionB) {
		if (currentBlendFunctionA == blendFunctionA && currentBlendFunctionB == blendFunctionB) {
			return;
		}
		setBlendFunctionOverride(blendFunctionA, blendFunctionB);
	}

	/**
	 * Sets the blend function, ignoring the status of the state machine.
	 * 
	 * @param blendFunctionA The new first blend function, i.e.
	 *                       <code>GL11.GL_SRC_ALPHA</code>.
	 * @param blendFunctionB The new second blend function, i.e.
	 *                       <code>GL11.GL__ONE_MINUS_SRC_ALPHA</code>.
	 * @see GL11#glBlendFunc(int, int)
	 */
	public static void setBlendFunctionOverride(int blendFunctionA, int blendFunctionB) {
		currentBlendFunctionA = blendFunctionA;
		currentBlendFunctionB = blendFunctionB;
		GL11.glBlendFunc(blendFunctionA, blendFunctionB);
	}

	/**
	 * Sets the size of the viewport to render to.
	 * 
	 * @param width  The new width.
	 * @param height The new height.
	 * @see GL11#glViewport(int, int, int, int)
	 */
	public static void setRenderSize(int width, int height) {
		if (currentViewportWidth == width && currentViewportHeight == height) {
			return;
		}
		setRenderSizeOverride(width, height);
	}

	/**
	 * Sets the size of the viewport to render to, ignoring the status of the state
	 * machine.
	 * 
	 * @param width  The new width.
	 * @param height The new height.
	 * @see GL11#glViewport(int, int, int, int)
	 */
	public static void setRenderSizeOverride(int width, int height) {
		currentViewportWidth = width;
		currentViewportHeight = height;
		GL11.glViewport(0, 0, width, height);
	}

}
