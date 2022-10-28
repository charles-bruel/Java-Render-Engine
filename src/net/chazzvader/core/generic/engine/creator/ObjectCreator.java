package net.chazzvader.core.generic.engine.creator;

import net.chazzvader.core.generic.Configuration;
import net.chazzvader.core.generic.engine.mesh.Mesh;
import net.chazzvader.core.generic.engine.mesh.VertexedProperty;
import net.chazzvader.core.generic.engine.mesh.VertexedProperty.VertexPropertyType;
import net.chazzvader.core.generic.engine.object.EngineObjectMesh;
import net.chazzvader.core.generic.engine.render.material.Material;
import net.chazzvader.core.generic.math.Geometry;
import net.chazzvader.core.generic.math.Vector2f;
import net.chazzvader.core.generic.math.Vector3f;

/**
 * A class to create complex mesh objects out of multiple primitives.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class ObjectCreator {

	private static final ObjectCreator INSTANCE = new ObjectCreator();

	private Mesh mesh = null;

	/**
	 * creates a new, blank object creator. This allows multiple objects to be
	 * created in parallel. All methods that internally use ObjectCreator will use a
	 * new instance so nothing is disrupted.
	 */
	public ObjectCreator() {
		newObject();
	}

	/**
	 * Creates a new, empty mesh object
	 * 
	 * @return This ObjectCreator instance, for string multiple calls together
	 */
	public ObjectCreator newObject() {
		mesh = new Mesh(new VertexedProperty(new float[0], VertexPropertyType.VERTEX_POS),
				new VertexedProperty(new int[0], VertexPropertyType.INDICES),
				new VertexedProperty[] { new VertexedProperty(new float[0], VertexPropertyType.NORMALS),
						new VertexedProperty(new float[0], VertexPropertyType.TEXTURE_COORDINATES) });
		return this;
	}

	/**
	 * Appends the given mesh object to the current object. This only works if the
	 * vertex types the two objects have are the same. A new mesh is created. The
	 * original goes before the new object.
	 * 
	 * @param part the part to append
	 * @return This ObjectCreator instance, for string multiple calls together
	 */
	public ObjectCreator append(Mesh part) {
		mesh = mesh.append(part);
		return this;
	}

	/**
	 * Returns the working mesh
	 * 
	 * @param normals If to do normal tangent/bitangent calculations
	 * @return The working mesh
	 */
	public Mesh getMesh(boolean normals) {
		if (normals)
			return mesh.addTangentBitangent();
		return mesh;
	}

	/**
	 * Creates and returns a EngineObject referencing the working mesh
	 * 
	 * @param material The material to use
	 * @param normals  If to do normal tangent/bitangent calculations
	 * @return A EngineObject referencing the working mesh
	 */
	public EngineObjectMesh getObject(Material material, boolean normals) {
		EngineObjectMesh ret = newEngineObject(getMesh(normals), material);
		return ret;
	}

	/**
	 * Creates and returns a EngineObject based on a mesh and material. This
	 * automatic creates the right type based on renderer.
	 * 
	 * @param mesh     The mesh to use
	 * @param material The material to use
	 * @return A EngineObject referencing the working mesh.
	 */
	public static EngineObjectMesh newEngineObject(Mesh mesh, Material material) {
		Configuration.assertRendererFinalized();
		return new EngineObjectMesh(mesh, material);
	}

	/**
	 * Is there a working mesh?
	 * 
	 * @return True if there is a working mesh.
	 */
	public boolean inProgress() {
		return mesh != null;
	}

	/**
	 * Creates a new, empty mesh object. This applies to a private, static
	 * ObjectCreator.
	 * 
	 * @return This ObjectCreator instance, for string multiple calls together
	 * @see #newObject()
	 */
	public static ObjectCreator newObjectStatic() {
		return INSTANCE.newObject();
	}

	/**
	 * Appends the given mesh object to the current object. This only works if the
	 * vertex types the two objects have are the same. A new mesh is created. The
	 * original goes before the new object. This applies to a private, static
	 * ObjectCreator.
	 * 
	 * @param part the part to append
	 * @return This ObjectCreator instance, for string multiple calls together
	 * @see #append(Mesh)
	 */
	public static ObjectCreator appendStatic(Mesh part) {
		return INSTANCE.append(part);
	}

	/**
	 * Returns the working mesh. This applies to a private, static ObjectCreator.
	 * 
	 * @param normals If to do normal tangent/bitangent calculations
	 * @return The working mesh
	 * @see ObjectCreator#getMesh(boolean)
	 */
	public static Mesh getMeshStatic(boolean normals) {
		return INSTANCE.getMesh(normals);
	}

	/**
	 * Creates and returns a EngineObject referencing the working mesh. This applies
	 * to a private, static ObjectCreator.
	 * 
	 * @param normals  If to do normal tangent/bitangent calculations
	 * @param material The material to use
	 * @return A EngineObject referencing the working mesh
	 * @see ObjectCreator#getObject(Material, boolean)
	 */
	public static EngineObjectMesh getObjectStatic(Material material, boolean normals) {
		return INSTANCE.getObject(material, normals);
	}

	/**
	 * Is there a working mesh? This applies to a private, static ObjectCreator.
	 * 
	 * @return True if there is a working mesh.
	 * @see #inProgress()
	 */
	public static boolean inProgressStatic() {
		return INSTANCE.inProgress();
	}

	/**
	 * Creates a triangle from 3 points. Texture coordinates is WIP.
	 * 
	 * @param p1 The first point
	 * @param p2 The second point
	 * @param p3 The third point
	 * @return A mesh that is a triangle, based on the 3 points
	 */
	public static Mesh triangle(Vector3f p1, Vector3f p2, Vector3f p3) {
		float[] vertices = new float[] { p1.x, p1.y, p1.z, p2.x, p2.y, p2.z, p3.x, p3.y, p3.z };
		int[] indices = new int[] { 0, 1, 2 };
		Vector3f n = Geometry.calculateNormal(p1, p2, p3);
		float[] normals = new float[] { n.x, n.y, n.z, n.x, n.y, n.z, n.x, n.y, n.z };
		return new Mesh(new VertexedProperty(vertices, VertexPropertyType.VERTEX_POS),
				new VertexedProperty(indices, VertexPropertyType.INDICES),
				new VertexedProperty[] { new VertexedProperty(normals, VertexPropertyType.NORMALS) });
	}

	/**
	 * Creates a quad from 4 points.
	 * 
	 * @param p1 The first point, upper left, ++
	 * @param p2 The second point, upper right, -+
	 * @param p3 The third point, lower left, +-
	 * @param p4 The fourth point, lower right, --
	 * @return A mesh that is a quad, based on the 4 points
	 */
	public static Mesh quad(Vector3f p1, Vector3f p2, Vector3f p3, Vector3f p4) {
		if (Geometry.coplanar(p1, p2, p3, p4)) {
			float[] vertices = new float[] { p1.x, p1.y, p1.z, p2.x, p2.y, p2.z, p3.x, p3.y, p3.z, p4.x, p4.y, p4.z };
			int[] indices = new int[] { 0, 1, 2, 3, 2, 1 };
			Vector3f n = Geometry.calculateNormal(p1, p2, p3);
			float[] normals = new float[] { n.x, n.y, n.z, n.x, n.y, n.z, n.x, n.y, n.z, n.x, n.y, n.z };
			return new Mesh(new VertexedProperty(vertices, VertexPropertyType.VERTEX_POS),
					new VertexedProperty(indices, VertexPropertyType.INDICES),
					new VertexedProperty[] { new VertexedProperty(normals, VertexPropertyType.NORMALS) });
		} else {
			ObjectCreator oc = new ObjectCreator();
			oc.newObject();
			oc.append(triangle(p1, p2, p3));
			oc.append(triangle(p4, p3, p2));
			return oc.getMesh(false);
		}
	}

	/**
	 * Creates a quad from 4 points. The texture coordinates are auto-generated.
	 * 
	 * @param p1 The first point, upper left, ++
	 * @param p2 The second point, upper right, -+
	 * @param p3 The third point, lower left, +-
	 * @param p4 The fourth point, lower right, --
	 * @return A mesh that is a quad, based on the 4 points
	 */
	public static Mesh quadTextured(Vector3f p1, Vector3f p2, Vector3f p3, Vector3f p4) {// TODO: Finish this
		/*
		 * Vector2f tc1, tc2, tc3, tc4; Vector3f origin = new Vector3f((p1.x + p2.x +
		 * p3.x + p4.x) / 4, (p1.y + p2.y + p3.y + p4.y) / 4, (p1.z + p2.z + p3.z +
		 * p4.z) / 4);
		 * 
		 * Vector3f cwp1 = p1; Vector3f cwp2 = p2; Vector3f cwp3 = p4; Vector3f cwp4 =
		 * p3;
		 * 
		 * float el1 = cwp1.subCopy(cwp2).length(); float el2 =
		 * cwp2.subCopy(cwp3).length(); float el3 = cwp3.subCopy(cwp4).length(); float
		 * el4 = cwp4.subCopy(cwp1).length(); float er1 = el4 / el1; float er2 = el1 /
		 * el2; float er3 = el2 / el3; float er4 = el3 / el4;
		 * 
		 * float val = cwp2.subCopy(origin).length(); float ml =
		 * cwp1.subCopy(origin).length(); if (val > ml) ml = val; val =
		 * cwp3.subCopy(origin).length(); if (val > ml) ml = val; val =
		 * cwp4.subCopy(origin).length(); if (val > ml) ml = val;
		 * 
		 * float tcl1 = cwp1.subCopy(origin).length() / ml; float tcl2 =
		 * cwp2.subCopy(origin).length() / ml; float tcl3 =
		 * cwp3.subCopy(origin).length() / ml; float tcl4 =
		 * cwp4.subCopy(origin).length() / ml;
		 * 
		 * final float const1 = 1f;
		 * 
		 * tc1 = new Vector2f(tcl1 / er1, tcl1 * er1).mul(new Vector2f(-const1,
		 * -const1)); tc2 = new Vector2f(tcl2 * er2, tcl2 / er2).mul(new
		 * Vector2f(-const1, const1)); tc3 = new Vector2f(tcl3 / er3, tcl3 *
		 * er3).mul(new Vector2f(const1, -const1)); tc4 = new Vector2f(tcl4 * er4, tcl4
		 * / er4).mul(new Vector2f(const1, const1));
		 * 
		 * float minX = tc1.x < tc2.x ? tc1.x : tc2.x; float maxX = tc3.x > tc4.x ?
		 * tc3.x : tc4.x; float minY = tc1.y < tc3.y ? tc1.y : tc3.y; float maxY = tc2.y
		 * > tc4.y ? tc2.y : tc4.y;
		 * 
		 * tc1.map(minX, maxX, minY, maxY, 0, 1, 0, 1); tc2.map(minX, maxX, minY, maxY,
		 * 0, 1, 0, 1); tc3.map(minX, maxX, minY, maxY, 0, 1, 0, 1); tc4.map(minX, maxX,
		 * minY, maxY, 0, 1, 0, 1);
		 * 
		 * ObjectCreator oc = new ObjectCreator(); oc.newObject();
		 * oc.append(quadTextured(p1, p2, p3, p4, tc3, tc4, tc1, tc2));// Dont know what
		 * (tc1, tc2) and (tc3, tc4) are // swapped, but seems to work
		 */
		ObjectCreator oc = new ObjectCreator();
		oc.newObject();
		oc.append(quadTextured(p1, p2, p3, p4, new Vector2f(1, 1), new Vector2f(0, 1), new Vector2f(1, 0),
				new Vector2f(0, 0)));
		return oc.getMesh(false);
	}

	/**
	 * Creates a cuboid from 8 points. Texture coordinates is WIP.
	 * 
	 * @param p1 The first point, close upper left, +++
	 * @param p2 The second point, close upper right, -++
	 * @param p3 The third point, close lower left, +-+
	 * @param p4 The fourth point, close lower right, --+
	 * @param p5 The fifth point, far upper left, ++-
	 * @param p6 The sixth point, far upper right, -+-
	 * @param p7 The seventh point, far lower left, +--
	 * @param p8 The eight point, far lower right, ---
	 * @return A mesh that is a quad, based on the 4 points
	 */
	public static Mesh cubiod(Vector3f p1, Vector3f p2, Vector3f p3, Vector3f p4, Vector3f p5, Vector3f p6, Vector3f p7,
			Vector3f p8) {
		ObjectCreator oc = new ObjectCreator();
		oc.newObject();
		oc.append(ObjectCreator.quad(p1, p2, p3, p4));
		oc.append(ObjectCreator.quad(p5, p7, p6, p8));
		oc.append(ObjectCreator.quad(p1, p3, p5, p7));
		oc.append(ObjectCreator.quad(p2, p6, p4, p8));
		oc.append(ObjectCreator.quad(p1, p5, p2, p6));
		oc.append(ObjectCreator.quad(p3, p4, p7, p8));
		return oc.getMesh(false);
	}

	/**
	 * Creates a cube with texture coordinates of a given size
	 * 
	 * @param pos  The position of the center of the cube.
	 * @param size The dimension of the cube
	 * @return A mesh representing the cube
	 */
	public static Mesh cube(Vector3f pos, float size) {
		size /= 2;
		Vector2f t1 = new Vector2f(1, 1);
		Vector2f t2 = new Vector2f(0, 1);
		Vector2f t3 = new Vector2f(1, 0);
		Vector2f t4 = new Vector2f(0, 0);
		return ObjectCreator.cubiodTextured(new Vector3f(size, size, size).add(pos),
				new Vector3f(-size, size, size).add(pos), new Vector3f(size, -size, size).add(pos),
				new Vector3f(-size, -size, size).add(pos), new Vector3f(size, size, -size).add(pos),
				new Vector3f(-size, size, -size).add(pos), new Vector3f(size, -size, -size).add(pos),
				new Vector3f(-size, -size, -size).add(pos), t1, t2, t3, t4);
	}

	/**
	 * Creates a cube with texture coordinates of a given size
	 * 
	 * @param size The dimension of the cube
	 * @return A mesh representing the cube
	 */
	public static Mesh cube(float size) {
		return cube(new Vector3f(), size);
	}

	/**
	 * Creates a triangle from 3 points with texture coordinates.
	 * 
	 * @param p1 The first point
	 * @param p2 The second point
	 * @param p3 The third point
	 * @param t1 The first texture coordinate
	 * @param t2 The second texture coordinate
	 * @param t3 The third texture coordinate
	 * @return A mesh that is a triangle, based on the 3 points
	 */
	public static Mesh triangleTextured(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f t1, Vector2f t2, Vector2f t3) {
		float[] vertices = new float[] { p1.x, p1.y, p1.z, p2.x, p2.y, p2.z, p3.x, p3.y, p3.z };
		float[] textureCoordinates = new float[] { t1.x, t1.y, t2.x, t2.y, t3.x, t3.y };
		int[] indices = new int[] { 0, 1, 2 };
		Vector3f n = Geometry.calculateNormal(p1, p2, p3);
		float[] normals = new float[] { n.x, n.y, n.z, n.x, n.y, n.z, n.x, n.y, n.z };
		return new Mesh(new VertexedProperty(vertices, VertexPropertyType.VERTEX_POS),
				new VertexedProperty(indices, VertexPropertyType.INDICES),
				new VertexedProperty[] { new VertexedProperty(normals, VertexPropertyType.NORMALS),
						new VertexedProperty(textureCoordinates, VertexPropertyType.TEXTURE_COORDINATES) });
	}

	/**
	 * Creates a quad from 4 points with texture coordinates. The texture will be
	 * stretched over the entire image.
	 * 
	 * @param p1 The first point, upper left, ++
	 * @param p2 The second point, upper right, -+
	 * @param p3 The third point, lower left, +-
	 * @param p4 The fourth point, lower right, --
	 * @param t1 The first texture coordinate, upper left, ++
	 * @param t2 The second texture coordinate, upper right, -+
	 * @param t3 The third texture coordinate, lower left, +-
	 * @param t4 The fourth texture coordinate, lower right, --
	 * @return A mesh that is a quad, based on the 4 points
	 */
	public static Mesh quadTextured(Vector3f p1, Vector3f p2, Vector3f p3, Vector3f p4, Vector2f t1, Vector2f t2,
			Vector2f t3, Vector2f t4) {
		if (Geometry.coplanar(p1, p2, p3, p4)) {
			float[] vertices = new float[] { p1.x, p1.y, p1.z, p2.x, p2.y, p2.z, p3.x, p3.y, p3.z, p4.x, p4.y, p4.z };
			float[] textureCoordinates = new float[] { t1.x, t1.y, t2.x, t2.y, t3.x, t3.y, t4.x, t4.y };
			int[] indices = new int[] { 0, 1, 2, 3, 2, 1 };
			Vector3f n = Geometry.calculateNormal(p1, p2, p3);
			float[] normals = new float[] { n.x, n.y, n.z, n.x, n.y, n.z, n.x, n.y, n.z, n.x, n.y, n.z };
			return new Mesh(new VertexedProperty(vertices, VertexPropertyType.VERTEX_POS),
					new VertexedProperty(indices, VertexPropertyType.INDICES),
					new VertexedProperty[] { new VertexedProperty(normals, VertexPropertyType.NORMALS),
							new VertexedProperty(textureCoordinates, VertexPropertyType.TEXTURE_COORDINATES) });
		} else {
			ObjectCreator oc = new ObjectCreator();
			oc.newObject();
			oc.append(triangleTextured(p1, p2, p3, t1, t2, t3));
			oc.append(triangleTextured(p4, p3, p2, t4, t3, t2));
			return oc.getMesh(false);
		}
	}

	/**
	 * Creates a cuboid from 8 points with texture coordinates.
	 * 
	 * @param p1 The first point, close upper left, +++
	 * @param p2 The second point, close upper right, -++
	 * @param p3 The third point, close lower left, +-+
	 * @param p4 The fourth point, close lower right, --+
	 * @param p5 The fifth point, far upper left, ++-
	 * @param p6 The sixth point, far upper right, -+-
	 * @param p7 The seventh point, far lower left, +--
	 * @param p8 The eight point, far lower right, ---
	 * @param t1 The first texture coordinate, upper left, ++
	 * @param t2 The second texture coordinate, upper right, -+
	 * @param t3 The third texture coordinate, lower left, +-
	 * @param t4 The fourth texture coordinate, lower right, --
	 * @return A mesh that is a quad, based on the 4 points
	 */
	public static Mesh cubiodTextured(Vector3f p1, Vector3f p2, Vector3f p3, Vector3f p4, Vector3f p5, Vector3f p6,
			Vector3f p7, Vector3f p8, Vector2f t1, Vector2f t2, Vector2f t3, Vector2f t4) {
		ObjectCreator oc = new ObjectCreator();
		oc.newObject();
		oc.append(ObjectCreator.quadTextured(p1, p2, p3, p4, t1, t2, t3, t4));
		oc.append(ObjectCreator.quadTextured(p5, p7, p6, p8, t1, t2, t3, t4));
		oc.append(ObjectCreator.quadTextured(p1, p3, p5, p7, t1, t2, t3, t4));
		oc.append(ObjectCreator.quadTextured(p2, p6, p4, p8, t1, t2, t3, t4));
		oc.append(ObjectCreator.quadTextured(p1, p5, p2, p6, t1, t2, t3, t4));
		oc.append(ObjectCreator.quadTextured(p3, p4, p7, p8, t1, t2, t3, t4));
		return oc.getMesh(false);
	}

	/**
	 * Creates a sphere from a radius and resolution.
	 * 
	 * @param pos        The position of the center of the sphere.
	 * @param radius     The radius of the sphere.
	 * @param resolution How detailed the sphere is. Keep even.
	 * @return A mesh for a sphere, with texture coordinates and normals.
	 */
	public static Mesh sphere(Vector3f pos, float radius, int resolution) {
		int vertRes = resolution / 2;
		int verticesRequired = (resolution + 1) * (resolution + 1);
		int tris = (resolution * (resolution - 1)) * 2 + resolution * 2;
		float[] vertices = new float[verticesRequired * 3];
		float[] normals = new float[verticesRequired * 3];
		int[] indices = new int[tris * 3];
		float[] textureCoordinates = new float[verticesRequired * 2];
		float angle = (float) ((2 * Math.PI) / resolution);
		int vertIndex = 0;
		int indicesIndex = 0;
		for (int j = 0; j < vertRes; j++) {// Half 1
			for (int i = 0; i <= resolution; i++, vertIndex++) {
				float cosX = (float) Math.cos(angle * i);
				float sinY = (float) Math.sin(angle * i);
				float val = (float) (Math.PI / 2f * (j / ((float) vertRes)));
				float radiusMultiple = (float) Math.cos(val);
				vertices[vertIndex * 3 + 0] = cosX * radius * radiusMultiple;
				vertices[vertIndex * 3 + 1] = (float) (radius * Math.sin(val));
				vertices[vertIndex * 3 + 2] = sinY * radius * radiusMultiple;
				float effectiveHeight = vertices[vertIndex * 3 + 1] / radius;
				effectiveHeight += 1;
				effectiveHeight /= 2;
				textureCoordinates[vertIndex * 2 + 0] = (float) i / ((float) resolution - 0);
				textureCoordinates[vertIndex * 2 + 1] = effectiveHeight;
				if (j < vertRes - 1 && i < resolution) {
					indices[indicesIndex * 3 + 0] = vertIndex;
					indices[indicesIndex * 3 + 1] = vertIndex + resolution + 1;
					indices[indicesIndex * 3 + 2] = vertIndex + 1;
					indicesIndex++;
					indices[indicesIndex * 3 + 0] = vertIndex + 1;
					indices[indicesIndex * 3 + 1] = vertIndex + resolution + 1;
					indices[indicesIndex * 3 + 2] = vertIndex + resolution + 2;
					indicesIndex++;
				}
			}
		}

		for (int j = 1; j < vertRes; j++) {// Half 2
			for (int i = 0; i <= resolution; i++, vertIndex++) {
				float cosX = (float) Math.cos(angle * i);
				float sinY = (float) Math.sin(angle * i);
				float val = (float) (Math.PI / 2f * (j / ((float) vertRes)));
				float radiusMultiple = (float) Math.cos(val);
				vertices[vertIndex * 3 + 0] = cosX * radius * radiusMultiple;
				vertices[vertIndex * 3 + 1] = (float) -(radius * Math.sin(val));
				vertices[vertIndex * 3 + 2] = sinY * radius * radiusMultiple;
				float effectiveHeight = vertices[vertIndex * 3 + 1] / radius;
				effectiveHeight += 1;
				effectiveHeight /= 2;
				textureCoordinates[vertIndex * 2 + 0] = (float) i / (float) resolution;
				textureCoordinates[vertIndex * 2 + 1] = effectiveHeight;
				if (j < vertRes - 1 && i < resolution) {
					indices[indicesIndex * 3 + 0] = vertIndex;
					indices[indicesIndex * 3 + 1] = vertIndex + 1;
					indices[indicesIndex * 3 + 2] = vertIndex + resolution + 1;
					indicesIndex++;
					indices[indicesIndex * 3 + 0] = vertIndex + 1;
					indices[indicesIndex * 3 + 1] = vertIndex + resolution + 2;
					indices[indicesIndex * 3 + 2] = vertIndex + resolution + 1;
					indicesIndex++;
				}
			}
		}

		int val = (resolution + 1) * vertRes;// Join halves
		for (int i = 0; i <= resolution; i++) {
			indices[indicesIndex * 3 + 0] = i;
			indices[indicesIndex * 3 + 1] = i + 1;
			indices[indicesIndex * 3 + 2] = i + val;
			indicesIndex++;
			indices[indicesIndex * 3 + 0] = i + 1;
			indices[indicesIndex * 3 + 1] = i + val + 1;
			indices[indicesIndex * 3 + 2] = i + val;
			indicesIndex++;
		}

		int inital = resolution * (vertRes - 1);
		for (int i = 0; i <= resolution; i++) {// Top
			vertices[vertIndex * 3 + 0] = 0;
			vertices[vertIndex * 3 + 1] = radius;
			vertices[vertIndex * 3 + 2] = 0;
			textureCoordinates[vertIndex * 2 + 0] = (float) i / (float) resolution;
			textureCoordinates[vertIndex * 2 + 1] = 1;
			indices[indicesIndex * 3 + 0] = vertIndex;
			indices[indicesIndex * 3 + 1] = inital + i + 1;
			indices[indicesIndex * 3 + 2] = inital + i;
			vertIndex++;
			indicesIndex++;
		}

		inital += resolution * (vertRes - 1);

		for (int i = 0; i <= resolution; i++) {// Bottom
			vertices[vertIndex * 3 + 0] = 0;
			vertices[vertIndex * 3 + 1] = -radius;
			vertices[vertIndex * 3 + 2] = 0;
			textureCoordinates[vertIndex * 2 + 0] = (float) i / (float) resolution;
			textureCoordinates[vertIndex * 2 + 1] = 0;
			indices[indicesIndex * 3 + 0] = vertIndex;
			indices[indicesIndex * 3 + 1] = inital + i;
			indices[indicesIndex * 3 + 2] = inital + i + 1;
			vertIndex++;
			indicesIndex++;
		}

		for (int i = 0; i < vertices.length / 3; i++) {// Normals
			Vector3f vec = new Vector3f(vertices[i * 3 + 0], vertices[i * 3 + 1], vertices[i * 3 + 2]);
			vec.normalize();
			normals[i * 3 + 0] = vec.x;
			normals[i * 3 + 1] = vec.y;
			normals[i * 3 + 2] = vec.z;
		}

		for (int i = 0; i < vertices.length / 3; i++) {// Move mesh
			vertices[i * 3 + 0] += pos.x;
			vertices[i * 3 + 1] += pos.y;
			vertices[i * 3 + 2] += pos.z;
		}
		return new Mesh(new VertexedProperty(vertices, VertexPropertyType.VERTEX_POS),
				new VertexedProperty(indices, VertexPropertyType.INDICES),
				new VertexedProperty[] { new VertexedProperty(normals, VertexPropertyType.NORMALS),
						new VertexedProperty(textureCoordinates, VertexPropertyType.TEXTURE_COORDINATES) });
	}

	/**
	 * Creates a sphere from a radius and resolution.
	 * 
	 * @param radius     The radius of the sphere.
	 * @param resolution How detailed the sphere is. Keep even.
	 * @return A mesh for a sphere, with texture coordinates and normals.
	 */
	public static Mesh sphere(float radius, int resolution) {
		return sphere(new Vector3f(), radius, resolution);
	}

	/**
	 * Creates and returns a new sphere object. This uses a new instance of
	 * ObjectCreator.
	 * 
	 * @param pos        The position of the center of the sphere.
	 * @param radius     The radius of the sphere
	 * @param resolution The resolution of the sphere
	 * @param material   The material to use
	 * @param normals    If to do normal tangent/bitangent calculations
	 * @return A sphere engine object
	 */
	public static EngineObjectMesh instaSphere(Vector3f pos, float radius, int resolution, Material material,
			boolean normals) {
		return new ObjectCreator().newObject().append(sphere(pos, radius, resolution)).getObject(material, normals);
	}

	/**
	 * Creates and returns a new cube object. This uses a new instance of
	 * ObjectCreator.
	 * 
	 * @param pos      The position of the center of the cube.
	 * @param size     The dimension of the cube
	 * @param material The material to use
	 * @param normals  If to do normal tangent/bitangent calculations
	 * @return A sphere engine object
	 */
	public static EngineObjectMesh instaCube(Vector3f pos, float size, Material material, boolean normals) {
		return new ObjectCreator().newObject().append(cube(pos, size)).getObject(material, normals);
	}

	/**
	 * Creates and returns a new sphere object. This uses a new instance of
	 * ObjectCreator.
	 * 
	 * @param radius     The radius of the sphere
	 * @param resolution The resolution of the sphere
	 * @param material   The material to use
	 * @param normals    If to do normal tangent/bitangent calculations
	 * @return A sphere engine object
	 */
	public static EngineObjectMesh instaSphere(float radius, int resolution, Material material, boolean normals) {
		return new ObjectCreator().newObject().append(sphere(radius, resolution)).getObject(material, normals);
	}

	/**
	 * Creates and returns a new cube object. This uses a new instance of
	 * ObjectCreator.
	 * 
	 * @param size     The dimension of the cube
	 * @param material The material to use
	 * @param normals  If to do normal tangent/bitangent calculations
	 * @return A sphere engine object
	 */
	public static EngineObjectMesh instaCube(float size, Material material, boolean normals) {
		return new ObjectCreator().newObject().append(cube(size)).getObject(material, normals);
	}

	/**
	 * The standard UI quad, that is a quad from -1, -1 0 to 1, 1, 0 with texture
	 * coordinates.
	 * 
	 * @return The standard quad.
	 */
	public static Mesh quadUI() {
		return quadTextured(new Vector3f(-1, -1, 0), new Vector3f(1, -1, 0), new Vector3f(-1, 1, 0),
				new Vector3f(1, 1, 0), new Vector2f(0, 1), new Vector2f(1, 1), new Vector2f(0, 0), new Vector2f(1, 0));
	}

}