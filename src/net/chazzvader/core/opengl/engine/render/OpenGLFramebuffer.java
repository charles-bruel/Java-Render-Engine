package net.chazzvader.core.opengl.engine.render;

import net.chazzvader.core.generic.engine.Application;
import net.chazzvader.core.generic.engine.render.Framebuffer;
import net.chazzvader.core.opengl.engine.util.OpenGLStateMachine;

/**
 * OpenGL implementation of a framebuffer
 * @author csbru
 * @version 1
 * @since 1.0
 */
public abstract class OpenGLFramebuffer extends Framebuffer {

	protected int width, height;
	protected int fbo;
	
	/**
	 * Creates a framebuffer from size dimensions.
	 * @param width The width of the framebuffer.
	 * @param height The height of the framebuffer.
	 */
	public OpenGLFramebuffer(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Gets the width of the framebuffer.
	 * @param width The width.
	 */
	public void setWidth(int width) {
		this.width = width;
		recreate();
	}
	
	/**
	 * Gets the height of the framebuffer.
	 * @param height The height.
	 */
	public void setHeight(int height) {
		this.height = height;
		recreate();
	}
	
	@Override
	public void bind() {
		OpenGLStateMachine.setRenderSize(getWidth(), getHeight());
		OpenGLStateMachine.bindFramebuffer(fbo);
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
	
	private static OpenGLFramebuffer fbo0 = new OpenGLFramebuffer(0, 0) {
		
		{
			fbo = 0;
		}
		
		@Override
		public void recreate() {
			fbo = 0;
		}
		
		@Override
		public int getWidth() {
			return Application.getInstance().getWindow().getWidth();
		}
		
		@Override
		public int getHeight() {
			return Application.getInstance().getWindow().getHeight();
		}

		@Override
		public void delete() {
			
		}
		
	};
	
	protected static OpenGLFramebuffer getDefault() {
		return fbo0;
	}

}
