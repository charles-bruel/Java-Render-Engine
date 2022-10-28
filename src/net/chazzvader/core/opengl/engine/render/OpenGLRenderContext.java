package net.chazzvader.core.opengl.engine.render;

import org.lwjgl.opengl.GL11;

import net.chazzvader.core.generic.engine.render.Framebuffer;
import net.chazzvader.core.generic.engine.render.RenderContext;
import net.chazzvader.core.opengl.engine.util.OpenGLStateMachine;

/**
 * An OpenGL version of <code>RenderContext</code>. The changes are mostly
 * OpenGL specific settings plus properly setting them when <code>bind();</code>
 * is called.
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 * @see RenderContext
 */
public class OpenGLRenderContext extends RenderContext {

	/**
	 * The framebuffer this context renders to. By default set to, well, the default
	 * framebuffer.
	 */
	public OpenGLFramebuffer framebuffer = OpenGLFramebuffer.getDefault();

	/**
	 * Should blending be enabled?
	 */
	public boolean blend = false;
	
	/**
	 * Should depth testing be enabled?
	 */
	public boolean depthTest = true;

	/**
	 * If depth testing is enabled, what depth function to use.
	 */
	public int depthFunc = GL11.GL_LESS;
	
	/**
	 * If blending is enabled, what blending functions to use.
	 */
	public int blendFuncA = GL11.GL_SRC_ALPHA;
	
	/**
	 * If blending is enabled, what blending functions to use.
	 */
	public int blendFuncB = GL11.GL_ONE_MINUS_SRC_ALPHA;

	/**
	 * If the framebuffer has the "depth-only" property.
	 */
	public boolean depthOnly = false;

	@Override
	public void bind() {
		framebuffer.bind();

		OpenGLStateMachine.setValue(GL11.GL_BLEND, blend);
		OpenGLStateMachine.setValue(GL11.GL_DEPTH_TEST, depthTest);
		if(depthTest) {
			OpenGLStateMachine.setDepthFunction(depthFunc);
		}
		if(blend) {
			OpenGLStateMachine.setBlendFunction(blendFuncA, blendFuncB);
		}
	}

	@Override
	public int getWidth() {
		return framebuffer.getWidth();
	}

	@Override
	public int getHeight() {
		return framebuffer.getHeight();
	}

	@Override
	public boolean isDepthOnly() {
		return depthOnly;
	}

	@Override
	public Framebuffer getFramebuffer() {
		return framebuffer;
	}

}
