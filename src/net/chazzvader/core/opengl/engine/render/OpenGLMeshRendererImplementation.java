package net.chazzvader.core.opengl.engine.render;

import net.chazzvader.core.generic.Configuration;
import net.chazzvader.core.generic.Configuration.Renderer;
import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.mesh.Mesh;
import net.chazzvader.core.generic.engine.render.MeshRendererImplementation;
import net.chazzvader.core.generic.engine.render.material.Material;

/**
 * The OpenGL version of <code>MeshRendererImplementation</code>.
 * @author csbru
 * @version 1
 * @since 1.0
 * @see MeshRendererImplementation
 */
public class OpenGLMeshRendererImplementation extends MeshRendererImplementation {

	private OpenGLVertexArray array;
	
	/**
	 * Creates a new implementation.
	 * 
	 * @param mesh     The mesh to render.
	 * @param material The material to render with.
	 * @param object   The object this is attached to.
	 */
	public OpenGLMeshRendererImplementation(Mesh mesh, Material material) {
		super(mesh, material);
		Configuration.assertRenderer(Renderer.OPEN_GL);
		array = new OpenGLVertexArray(mesh);
	}

	@Override
	public void render() {
		array.render();
	}

	@Override
	public void delete() {
		checkDelete();
		array.delete();
		array = null;
		deleted = true;
		super.delete();
	}

	private boolean deleted = false;
	
	private void checkDelete() {
		if(deleted) {
			Logging.log("Mesh Renderer Deleted", "MeshRendererImplementation", LoggingLevel.ERR);
		}
	}
}