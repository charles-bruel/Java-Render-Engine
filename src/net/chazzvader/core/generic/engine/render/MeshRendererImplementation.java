package net.chazzvader.core.generic.engine.render;

import net.chazzvader.core.generic.Configuration;
import net.chazzvader.core.generic.Configuration.Renderer;
import net.chazzvader.core.generic.engine.EngineItem;
import net.chazzvader.core.generic.engine.mesh.Mesh;
import net.chazzvader.core.generic.engine.render.material.Material;
import net.chazzvader.core.opengl.engine.render.OpenGLMeshRendererImplementation;

/**
 * The implementation of everything required to render a mesh. Allows a mesh to
 * be directly extended while still maintaining different code depending on
 * renderer.<br>
 * <br>
 * Implementation depends on renderer.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 * @see OpenGLMeshRendererImplementation
 */
public abstract class MeshRendererImplementation extends EngineItem {

	protected Mesh mesh;

	/**
	 * The material to render with
	 */
	public Material material;

	/**
	 * Creates a new implementation.
	 * 
	 * @param mesh     The mesh to render.
	 * @param material The material to render with.
	 * @param object   The object this is attached to.
	 */
	public MeshRendererImplementation(Mesh mesh, Material material) {
		this.mesh = mesh;
		this.material = material;
	}

	/**
	 * Static method to get the correctly, API-specific, implementation.
	 * 
	 * @param mesh     The mesh to render.
	 * @param material The material to render with.
	 * @param object   The object to attached to.
	 * @return The implementation.
	 */
	public static MeshRendererImplementation getMeshImplementation(Mesh mesh, Material material) {
		Renderer renderer = Configuration.getRendererVerifyFinalized();
		switch (renderer) {
		case GENERIC:
			break;
		case OPEN_GL:
			return new OpenGLMeshRendererImplementation(mesh, material);
		}
		return null;
	}

	/**
	 * Renders the mesh.<br>
	 * Implementation depends on renderer.
	 */
	public abstract void render();

}
