package net.chazzvader.core.generic.engine.object;

import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.mesh.Mesh;
import net.chazzvader.core.generic.engine.render.MeshRendererImplementation;
import net.chazzvader.core.generic.engine.render.material.Material;
import net.chazzvader.core.generic.math.Quaternion;
import net.chazzvader.core.generic.math.Vector3f;

/**
 * A EngineObject that has a mesh and therefore appears.<br>
 * Abstract because implementation depends on engine.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class EngineObjectMesh extends EngineObject {

	/**
	 * The material the mesh will render with.
	 */
	public Material material;

	/**
	 * The MeshRendererImplementation, platform dependent. Does the actual
	 * rendering.
	 * 
	 * @see MeshRendererImplementation
	 */
	public final MeshRendererImplementation meshRendererImplementation;

	/**
	 * Creates an object, at the origin, default scale, default rotation with the
	 * specified mesh and material.
	 * 
	 * @param mesh     The mesh to use.
	 * @param material The material to use.
	 */
	public EngineObjectMesh(Mesh mesh, Material material) {
		this(mesh, material, new Vector3f(), new Quaternion(), new Vector3f(1, 1, 1));
	}

	/**
	 * Creates an object, with the specified mesh and material at specified
	 * transform.
	 * 
	 * @param mesh     The mesh to use.
	 * @param material The material to use.
	 * @param pos      The position of the new object.
	 * @param rot      The rotation of the new object.
	 * @param scale    The scale of the new object.
	 */
	public EngineObjectMesh(Mesh mesh, Material material, Vector3f pos, Quaternion rot, Vector3f scale) {
		super(pos, rot, scale);
		this.material = material;
		meshRendererImplementation = MeshRendererImplementation.getMeshImplementation(mesh, material);
	}

	/**
	 * Renders the object, by whatever means the engine requires.
	 */
	@Override
	public void render() {
		checkDelete();
		material.bind(this);
		meshRendererImplementation.render();
	}

	@Override
	public void update(double delta) {
		checkDelete();
		super.update(delta);
	}
	
	@Override
	public void delete() {
		checkDelete();
		meshRendererImplementation.delete();
		deleted = true;
		super.delete();
	}

	private boolean deleted = false;
	
	private void checkDelete() {
		if(deleted) {
			Logging.log("Object Deleted", "Engine Object", LoggingLevel.ERR);
		}
	}

}