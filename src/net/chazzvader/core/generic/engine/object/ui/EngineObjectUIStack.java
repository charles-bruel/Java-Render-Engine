package net.chazzvader.core.generic.engine.object.ui;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import net.chazzvader.core.generic.Configuration;
import net.chazzvader.core.generic.Configuration.Renderer;
import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.creator.ObjectCreator;
import net.chazzvader.core.generic.engine.object.EngineObjectMesh;
import net.chazzvader.core.generic.engine.render.Framebuffer;
import net.chazzvader.core.generic.engine.render.RenderContext;
import net.chazzvader.core.generic.engine.render.material.MaterialUILegacyStack;
import net.chazzvader.core.generic.engine.uilegacy.element.UIElement;
import net.chazzvader.core.generic.engine.uilegacy.element.UIElementStack;
import net.chazzvader.core.generic.math.Vector2f;
import net.chazzvader.core.generic.math.Vector2i;
import net.chazzvader.core.generic.math.Vector3f;
import net.chazzvader.core.opengl.engine.render.OpenGLFramebuffer;
import net.chazzvader.core.opengl.engine.render.OpenGLRenderContext;
import net.chazzvader.core.opengl.engine.util.OpenGLStateMachine;

/**
 * An engine object intended to be used with the ui system. This one renders an
 * entire UI stack to a texture and then uses that. Used for scrolling, more
 * complex layouts and more.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 * @see UIElementStack
 * @see MaterialUILegacyStack
 */
@Deprecated
public class EngineObjectUIStack extends EngineObjectMesh implements IEngineObjectUI {

	private UIElementStack element;
	private Framebuffer framebuffer;
		
	/**
	 * Creates a new EngineObjectUIStack. It pulls all relevant info from the given
	 * UIElementStack.
	 * 
	 * @param element The associated element.
	 */
	public EngineObjectUIStack(UIElementStack element) {
		super(ObjectCreator.quadTextured(new Vector3f(-1, -1, 0), new Vector3f(1, -1, 0), new Vector3f(-1, 1, 0),
				new Vector3f(1, 1, 0), new Vector2f(0, 1), new Vector2f(1, 1), new Vector2f(0, 0), new Vector2f(1, 0)),
				new MaterialUILegacyStack(element));
		this.element = element;
		provider = element;
		generateFramebuffer();
		
		element.childStack.setOverrideRenderContext(getOverrideRenderContext());
	}

	private Vector2i _size;

	@Override
	public void update(double delta) {
		super.update(delta);
	}
	
	@Override
	public void render() {
		checkDelete();
		Renderer renderer = Configuration.getRendererVerifyFinalized();
		switch (renderer) {
		case GENERIC:
			break;
		case OPEN_GL:
			Vector3f color = element.backgroundColor;
			getOverrideRenderContext().bind();
			GL11.glClearColor(color.x, color.y, color.z, 0.0f);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

			Vector2i size = getViewportSize();
			if (!size.equals(_size)) {
				generateFramebuffer();
				_size = size;
			}
			element.childStack.render();
			break;
		}
		getRenderContext().bind();
		super.render();
	}

	private RenderContext overrideContext = null;

	private RenderContext getOverrideRenderContext() {
		if (overrideContext == null) {
			createOverrideRenderContext();
		}
		return overrideContext;
	}

	private void createOverrideRenderContext() {
		Renderer renderer = Configuration.getRendererVerifyFinalized();
		switch (renderer) {
		case GENERIC:
			break;
		case OPEN_GL:
			OpenGLRenderContext temp = new OpenGLRenderContext();

			temp.framebuffer = (OpenGLFramebuffer) framebuffer;
			temp.blend = true;

			overrideContext = temp;
			break;
		}
	}

	private void generateFramebuffer() {
		checkDelete();
		Renderer renderer = Configuration.getRendererVerifyFinalized();
		switch (renderer) {
		case GENERIC:
			break;
		case OPEN_GL:
			if (framebuffer == null) {
				Vector2i size = getViewportSize();
				framebuffer = new OpenGLFramebufferStack(size.x, size.y);
			} else {
				framebuffer.recreate();
			}
			break;
		}
	}

	private Vector2i getViewportSize() {
		int width = (int) (element.getAdjustedSize().x * getWindowWidth());
		int height = (int) (width * element.aspectRatio);
		return new Vector2i(width, height);
	}

	@Override
	public void delete() {
		checkDelete();
		framebuffer.delete();
		deleted = true;
		super.delete();
	}

	private boolean deleted = false;

	private void checkDelete() {
		if (deleted) {
			Logging.log("Object Deleted", "EngineObjectUIStack", LoggingLevel.ERR);
		}
	}

	@Override
	public UIElement getParentElement() {
		return element;
	}

	/**
	 * Returns the ID associated with the texture that has been rendered.
	 * 
	 * @return A texture id.
	 */
	public int getTexture() {
		Renderer renderer = Configuration.getRendererVerifyFinalized();
		switch (renderer) {
		case GENERIC:
			break;
		case OPEN_GL:
			return ((OpenGLFramebufferStack) framebuffer).cto;
		}
		return 0;
	}

	private class OpenGLFramebufferStack extends OpenGLFramebuffer {

		private int cto;
		private int dsrbo;

		public OpenGLFramebufferStack(int width, int height) {
			super(width, height);
			recreate();
		}

		@Override
		public void recreate() {
			if (fbo != 0) {
				GL30.glDeleteFramebuffers(fbo);
				GL30.glDeleteRenderbuffers(dsrbo);
				GL11.glDeleteTextures(cto);
			}
			fbo = GL30.glGenFramebuffers();
			GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
			Vector2i size = getViewportSize();
			width = size.x;
			height = size.y;

			// Color Texture
			cto = GL11.glGenTextures();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, cto);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, size.x, size.y, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE,
					(ByteBuffer) null);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, cto, 0);

			// Depth/Stencil Buffer
			dsrbo = GL30.glGenRenderbuffers();
			GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, dsrbo);
			GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH24_STENCIL8, size.x, size.y);
			GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
			GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_STENCIL_ATTACHMENT, GL30.GL_RENDERBUFFER,
					dsrbo);

			GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
			OpenGLStateMachine.bindTexture(0);// Ensures the state machine is reset
		}

		@Override
		public void delete() {
			GL30.glDeleteFramebuffers(fbo);
			GL30.glDeleteRenderbuffers(dsrbo);
			GL11.glDeleteTextures(cto);
		}

		@Override
		public void bind() {
			super.bind();
		}

	}

}