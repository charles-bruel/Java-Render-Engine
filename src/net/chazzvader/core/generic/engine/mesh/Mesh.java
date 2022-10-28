package net.chazzvader.core.generic.engine.mesh;

import java.util.Arrays;

import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.EngineItem;
import net.chazzvader.core.generic.engine.mesh.VertexedProperty.VertexPropertyDataType;
import net.chazzvader.core.generic.engine.mesh.VertexedProperty.VertexPropertyType;
import net.chazzvader.core.generic.math.Geometry;
import net.chazzvader.core.generic.math.Vector2f;
import net.chazzvader.core.generic.math.Vector3f;

/**
 * A collection of vertexed property that represent a Mesh. The only mandatory
 * ones are indices and positions
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class Mesh extends EngineItem {

	/**
	 * All the properties of the mesh
	 */
	public final VertexedProperty[] properties;

	/**
	 * The number of vertices
	 */
	public final int verticeCount;

	/**
	 * Creates a mesh with just position and indices, the bare minimum
	 * 
	 * @param vertexPos The positions of the vertices
	 * @param indices   The indices
	 */
	public Mesh(VertexedProperty vertexPos, VertexedProperty indices) {
		this(vertexPos, indices, new VertexedProperty[0]);
	}

	/**
	 * Creates a mesh with position and indices, as well as an array of other
	 * properties
	 * 
	 * @param vertexPos The positions of the vertices
	 * @param indices   The indices
	 * @param others     Additional vertexed properties
	 */
	public Mesh(VertexedProperty vertexPos, VertexedProperty indices, VertexedProperty... others) {
		if (indices != null && indices.dataType == VertexPropertyDataType.FLOAT) {
			Logging.log("Indices cannot be floating point", "Mesh", LoggingLevel.ERR);
		}
		int length = 0;
		if (vertexPos != null) {
			length = vertexPos.length();
		} else {
			length = others[0].length();
		}
		if (others == null) {
			others = new VertexedProperty[0];
		}
		for (int i = 0; i < others.length; i++) {
			if (others[i].length() != length && others[i].usage != VertexPropertyType.INDICES) {
				Logging.log("Length Mismatch!", "Mesh", LoggingLevel.ERR);
			}
		}
		int addValue = (vertexPos != null ? 1 : 0) + (indices != null ? 1 : 0);
		properties = new VertexedProperty[addValue + others.length];
		if (vertexPos != null) {
			properties[0] = vertexPos;
		}
		if (indices != null) {
			if (vertexPos != null) {
				properties[1] = indices;
			} else {
				properties[0] = indices;
			}
		}
		for (int i = 0; i < others.length; i++) {
			properties[i + addValue] = others[i];
		}
		verticeCount = length;
	}

	@Override
	public String toString() {
		return "Mesh [properties=" + Arrays.toString(properties) + ", verticeCount=" + verticeCount + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(properties);
		result = prime * result + verticeCount;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mesh other = (Mesh) obj;
		if (!Arrays.equals(properties, other.properties))
			return false;
		if (verticeCount != other.verticeCount)
			return false;
		return true;
	}

	/**
	 * Searches and returns the <code>VertexProperty</code> of the given type.
	 * 
	 * @param usage The property type to search for.
	 * @return The <code>VertexProperty</code> of the given type
	 * @see VertexedProperty
	 * @see VertexPropertyType
	 */
	public VertexedProperty getByUsage(VertexPropertyType usage) {
		for (int i = 0; i < properties.length; i++) {
			if (properties[i].usage == usage) {
				return properties[i];
			}
		}
		return null;
	}

	/**
	 * Calculates and adds tangent and bitangent normal data, based of the texture
	 * coordinates. This allows normal maps to be used.
	 * 
	 * @return This mesh object.
	 */
	public Mesh addTangentBitangent() {
		if (getByUsage(VertexPropertyType.NORMAL_TANGENT) != null)
			return this;
		int[] indices = new int[0];
		VertexedProperty vpIndices = getByUsage(VertexPropertyType.INDICES);
		switch (vpIndices.dataType) {
		case BYTE:
			indices = new int[vpIndices.byteProp.length];
			for (int i = 0; i < vpIndices.byteProp.length; i++) {
				indices[i] = vpIndices.byteProp[i];
			}
			break;
		case INT:
			indices = vpIndices.intProp;
			break;
		default:
			break;
		}
		VertexedProperty vpVert = getByUsage(VertexPropertyType.VERTEX_POS);
		float[] tangent = new float[vpVert.length() * 3];
		for (int i = 0; i < tangent.length; i++) {
			tangent[i] = -1;
		}
		float[] bitangent = new float[vpVert.length() * 3];
		float[] vertices = vpVert.getAsFloatArray();
		float[] texCoords = getByUsage(VertexPropertyType.TEXTURE_COORDINATES).getAsFloatArray();
		for (int i = 0; i < indices.length / 3; i++) {
			int index1 = indices[i * 3 + 0];
			int index2 = indices[i * 3 + 1];
			int index3 = indices[i * 3 + 2];
			Vector3f pos1 = new Vector3f(vertices[index1 * 3 + 0], vertices[index1 * 3 + 1], vertices[index1 * 3 + 2]);
			Vector3f pos2 = new Vector3f(vertices[index2 * 3 + 0], vertices[index2 * 3 + 1], vertices[index2 * 3 + 2]);
			Vector3f pos3 = new Vector3f(vertices[index3 * 3 + 0], vertices[index3 * 3 + 1], vertices[index3 * 3 + 2]);
			Vector2f tc1 = new Vector2f(texCoords[index1 * 2 + 0], texCoords[index1 * 2 + 1]);
			Vector2f tc2 = new Vector2f(texCoords[index2 * 2 + 0], texCoords[index2 * 2 + 1]);
			Vector2f tc3 = new Vector2f(texCoords[index3 * 2 + 0], texCoords[index3 * 2 + 1]);
			Vector3f edge1 = pos2.subCopy(pos1);
			Vector3f edge2 = pos3.subCopy(pos1);
			Vector2f dTc1 = tc2.subCopy(tc1);
			Vector2f dTc2 = tc3.subCopy(tc1);
			float f = 1.0f / (dTc1.x * dTc2.y - dTc2.x * dTc1.y);
			Vector3f cTangent = new Vector3f();
			cTangent.x = f * (dTc2.y * edge1.x - dTc1.y * edge2.x);
			cTangent.y = f * (dTc2.y * edge1.y - dTc1.y * edge2.y);
			cTangent.z = f * (dTc2.y * edge1.z - dTc1.y * edge2.z);
			cTangent.normalize();
			tangent[index1 * 3 + 0] = cTangent.x;
			tangent[index1 * 3 + 1] = cTangent.y;
			tangent[index1 * 3 + 2] = cTangent.z;
			tangent[index2 * 3 + 0] = cTangent.x;
			tangent[index2 * 3 + 1] = cTangent.y;
			tangent[index2 * 3 + 2] = cTangent.z;
			tangent[index3 * 3 + 0] = cTangent.x;
			tangent[index3 * 3 + 1] = cTangent.y;
			tangent[index3 * 3 + 2] = cTangent.z;
			Vector3f cBitangent = new Vector3f();
			cBitangent.x = f * (-dTc2.x * edge1.x + dTc1.x * edge2.x);
			cBitangent.y = f * (-dTc2.x * edge1.y + dTc1.x * edge2.y);
			cBitangent.z = f * (-dTc2.x * edge1.z + dTc1.x * edge2.z);
			cBitangent.normalize();
			bitangent[index1 * 3 + 0] = cBitangent.x;
			bitangent[index1 * 3 + 1] = cBitangent.y;
			bitangent[index1 * 3 + 2] = cBitangent.z;
			bitangent[index2 * 3 + 0] = cBitangent.x;
			bitangent[index2 * 3 + 1] = cBitangent.y;
			bitangent[index2 * 3 + 2] = cBitangent.z;
			bitangent[index3 * 3 + 0] = cBitangent.x;
			bitangent[index3 * 3 + 1] = cBitangent.y;
			bitangent[index3 * 3 + 2] = cBitangent.z;
		}
		VertexedProperty vpTangent = new VertexedProperty(tangent, VertexPropertyType.NORMAL_TANGENT);
		VertexedProperty vpBitangent = new VertexedProperty(bitangent, VertexPropertyType.NORMAL_BITANGENT);
		VertexedProperty[] newProperties = new VertexedProperty[properties.length + 2];
		for (int i = 0; i < properties.length; i++) {
			newProperties[i] = properties[i];
		}
		newProperties[newProperties.length - 2] = vpTangent;
		newProperties[newProperties.length - 1] = vpBitangent;
		return new Mesh(null, null, newProperties);
	}

	/**
	 * Adds another mesh of data to this.
	 * 
	 * @param mesh The other mesh to add.
	 * @return This mesh object.
	 */
	public Mesh append(Mesh mesh) {
		boolean[] completed = new boolean[properties.length];
		int indiceAddStart = 0;
		int indicePropertyIndex = -1;
		VertexedProperty[] newProperties = new VertexedProperty[properties.length];
		for (int i = 0; i < properties.length; i++) {
			VertexedProperty property = properties[i];
			if (property.usage == VertexPropertyType.INDICES) {
				indiceAddStart = property.length() * -1;
				indicePropertyIndex = i;
			}
			for (int k = 0; k < mesh.properties.length; k++) {
				if (mesh.properties[k].usage == property.usage) {
					newProperties[i] = combineProperty(property, mesh.properties[k]);
					completed[i] = true;
				}
			}
		}
		for (int i = 0; i < completed.length; i++) {
			if (!completed[i] && properties[i].usage != VertexPropertyType.INDICES) {
				Logging.log("A matching trait for " + properties[i].usage + " could not be found, suppling default",
						"Mesh", LoggingLevel.DEBUG);
				switch (properties[i].dataType) {
				case BYTE:
					byte[] ba = new byte[mesh.verticeCount * properties[i].usage.perVertice];
					newProperties[i] = combineProperty(properties[i], new VertexedProperty(ba, properties[i].usage));
					break;
				case FLOAT:
					float[] fa = new float[mesh.verticeCount * properties[i].usage.perVertice];
					newProperties[i] = combineProperty(properties[i], new VertexedProperty(fa, properties[i].usage));
					break;
				case INT:
					int[] ia = new int[mesh.verticeCount * properties[i].usage.perVertice];
					newProperties[i] = combineProperty(properties[i], new VertexedProperty(ia, properties[i].usage));
					break;
				}
			}
		}
		int toAdd = properties[0].length();// Number of vertices in old mesh, add to indice values in new mesh
		VertexedProperty indiceProperty = newProperties[indicePropertyIndex];
		switch (indiceProperty.dataType) {
		case BYTE:
			for (int i = indiceAddStart; i < indiceProperty.byteProp.length; i++) {
				indiceProperty.byteProp[i] += toAdd;
			}
			break;
		case INT:
			for (int i = indiceAddStart; i < indiceProperty.intProp.length; i++) {
				indiceProperty.intProp[i] += toAdd;
			}
			break;
		default:
			break;
		}
		return new Mesh(null, null, newProperties);
	}

	private VertexedProperty combineProperty(VertexedProperty p1, VertexedProperty p2) {
		if (p1.usage != p2.usage) {
			Logging.log("Tried to combine VertexedProperties with different usages", "Mesh", LoggingLevel.WARN);
		}
		VertexPropertyDataType type = p1.dataType;
		VertexedProperty ret = null;
		int index = 0;
		switch (type) {
		case BYTE:
			byte[] appendDataB = castByte(p2);
			byte[] finalB = new byte[appendDataB.length + p1.byteProp.length];
			for (int l = 0; l < p1.byteProp.length; l++, index++) {
				finalB[index] = p1.byteProp[l];
			}
			for (int l = 0; l < appendDataB.length; l++, index++) {
				finalB[index] = appendDataB[l];
			}
			return new VertexedProperty(finalB, p1.usage);
		case FLOAT:
			float[] appendDataF = castFloat(p2);
			float[] finalF = new float[appendDataF.length + p1.floatProp.length];
			for (int l = 0; l < p1.floatProp.length; l++, index++) {
				finalF[index] = p1.floatProp[l];
			}
			for (int l = 0; l < appendDataF.length; l++, index++) {
				finalF[index] = appendDataF[l];
			}
			return new VertexedProperty(finalF, p1.usage);
		case INT:
			int[] appendDataI = castInt(p2);
			int[] finalI = new int[appendDataI.length + p1.intProp.length];
			for (int l = 0; l < p1.intProp.length; l++, index++) {
				finalI[index] = p1.intProp[l];
			}
			for (int l = 0; l < appendDataI.length; l++, index++) {
				finalI[index] = appendDataI[l];
			}
			return new VertexedProperty(finalI, p1.usage);
		}
		return ret;
	}

	private byte[] castByte(VertexedProperty prop) {
		byte[] ret;
		switch (prop.dataType) {
		case BYTE:
			return prop.byteProp;
		case FLOAT:
			Logging.log("Downgrading from float to byte!", "Mesh", LoggingLevel.WARN);
			ret = new byte[prop.floatProp.length];
			for (int i = 0; i < prop.floatProp.length; i++) {
				ret[i] = (byte) prop.floatProp[i];
			}
			return ret;
		case INT:
			Logging.log("Downgrading from int to byte!", "Mesh", LoggingLevel.WARN);
			ret = new byte[prop.intProp.length];
			for (int i = 0; i < prop.intProp.length; i++) {
				ret[i] = (byte) prop.intProp[i];
			}
			return ret;
		}
		return null;
	}

	private int[] castInt(VertexedProperty prop) {
		int[] ret;
		switch (prop.dataType) {
		case INT:
			return prop.intProp;
		case FLOAT:
			Logging.log("Downgrading from float to int!", "Mesh", LoggingLevel.WARN);
			ret = new int[prop.floatProp.length];
			for (int i = 0; i < prop.floatProp.length; i++) {
				ret[i] = (int) prop.floatProp[i];
			}
			return ret;
		case BYTE:
			Logging.log("Upgrading from byte to int", "Mesh", LoggingLevel.DEBUG);
			ret = new int[prop.byteProp.length];
			for (int i = 0; i < prop.byteProp.length; i++) {
				ret[i] = (int) prop.byteProp[i];
			}
			return ret;
		}
		return null;
	}

	private float[] castFloat(VertexedProperty prop) {
		float[] ret;
		switch (prop.dataType) {
		case FLOAT:
			return prop.floatProp;
		case INT:
			Logging.log("Upgrading from int to float", "Mesh", LoggingLevel.DEBUG);
			ret = new float[prop.intProp.length];
			for (int i = 0; i < prop.intProp.length; i++) {
				ret[i] = (int) prop.intProp[i];
			}
			return ret;
		case BYTE:
			Logging.log("Upgrading from byte to float", "Mesh", LoggingLevel.DEBUG);
			ret = new float[prop.byteProp.length];
			for (int i = 0; i < prop.byteProp.length; i++) {
				ret[i] = (int) prop.byteProp[i];
			}
			return ret;
		}
		return null;
	}

	/**
	 * Checks if the mesh is
	 * <a href="https://en.wikipedia.org/wiki/Coplanarity">coplanar</a>.
	 * 
	 * @return True if the mesh is coplanar and false if it isn't <em>or has 5 or
	 *         more vertices because there is not a algorithm for that yet</em>
	 */
	public boolean coplanar() {
		if (verticeCount < 4) {
			return true;
		} else if (verticeCount > 4) {
			return false;
		} else {
			VertexedProperty vertices = getByUsage(VertexPropertyType.VERTEX_POS);
			Vector3f[] vectors = new Vector3f[4];
			for (int i = 0; i < vectors.length; i++) {
				switch (vertices.dataType) {
				case BYTE:
					vectors[i] = new Vector3f(vertices.byteProp[i * 3 + 0], vertices.byteProp[i * 3 + 1],
							vertices.byteProp[i * 3 + 2]);
					break;
				case FLOAT:
					vectors[i] = new Vector3f(vertices.floatProp[i * 3 + 0], vertices.floatProp[i * 3 + 1],
							vertices.floatProp[i * 3 + 2]);
					break;
				case INT:
					vectors[i] = new Vector3f(vertices.intProp[i * 3 + 0], vertices.intProp[i * 3 + 1],
							vertices.intProp[i * 3 + 2]);
					break;
				}
			}
			return Geometry.coplanar(vectors[0], vectors[1], vectors[2], vectors[3]);
		}
	}

	public Mesh calculateNormals() {
		if(getByUsage(VertexPropertyType.NORMALS) != null) {
			return this;
		}
		Vector3f[] normals = new Vector3f[verticeCount];
		VertexedProperty indices = getByUsage(VertexPropertyType.INDICES);
		VertexedProperty positions = getByUsage(VertexPropertyType.VERTEX_POS);
		switch(indices.dataType) {
		case BYTE:
			byte[] indicesArrayB = indices.byteProp;
			for(int i = 0;i < indicesArrayB.length;i += 3) {
				Vector3f normal = null;
				Vector3f pos1, pos2, pos3;
				switch(positions.dataType) {
				case BYTE:
					byte[] positionArrayB = positions.byteProp;
					pos1 = new Vector3f(positionArrayB[indicesArrayB[i+0]*3+0], positionArrayB[indicesArrayB[i+0]*3+1], positionArrayB[indicesArrayB[i+0]*3+2]);
					pos2 = new Vector3f(positionArrayB[indicesArrayB[i+1]*3+0], positionArrayB[indicesArrayB[i+1]*3+1], positionArrayB[indicesArrayB[i+1]*3+2]);
					pos3 = new Vector3f(positionArrayB[indicesArrayB[i+2]*3+0], positionArrayB[indicesArrayB[i+2]*3+1], positionArrayB[indicesArrayB[i+2]*3+2]);
					normal = Geometry.calculateNormal(pos1, pos2, pos3);
					break;
				case FLOAT:
					float[] positionArrayF = positions.floatProp;
					pos1 = new Vector3f(positionArrayF[indicesArrayB[i+0]*3+0], positionArrayF[indicesArrayB[i+0]*3+1], positionArrayF[indicesArrayB[i+0]*3+2]);
					pos2 = new Vector3f(positionArrayF[indicesArrayB[i+1]*3+0], positionArrayF[indicesArrayB[i+1]*3+1], positionArrayF[indicesArrayB[i+1]*3+2]);
					pos3 = new Vector3f(positionArrayF[indicesArrayB[i+2]*3+0], positionArrayF[indicesArrayB[i+2]*3+1], positionArrayF[indicesArrayB[i+2]*3+2]);
					normal = Geometry.calculateNormal(pos1, pos2, pos3);
					break;
				case INT:
					int[] positionArrayI = positions.intProp;
					pos1 = new Vector3f(positionArrayI[indicesArrayB[i+0]*3+0], positionArrayI[indicesArrayB[i+0]*3+1], positionArrayI[indicesArrayB[i+0]*3+2]);
					pos2 = new Vector3f(positionArrayI[indicesArrayB[i+1]*3+0], positionArrayI[indicesArrayB[i+1]*3+1], positionArrayI[indicesArrayB[i+1]*3+2]);
					pos3 = new Vector3f(positionArrayI[indicesArrayB[i+2]*3+0], positionArrayI[indicesArrayB[i+2]*3+1], positionArrayI[indicesArrayB[i+2]*3+2]);
					normal = Geometry.calculateNormal(pos1, pos2, pos3);
					break;
				}
				if(normals[indicesArrayB[i+0]] == null) {
					normals[indicesArrayB[i+0]] = Vector3f.copyOf(normal);
				} else {
					normals[indicesArrayB[i+0]].add(normal);
				}
				if(normals[indicesArrayB[i+1]] == null) {
					normals[indicesArrayB[i+1]] = Vector3f.copyOf(normal);
				} else {
					normals[indicesArrayB[i+1]].add(normal);
				}
				if(normals[indicesArrayB[i+2]] == null) {
					normals[indicesArrayB[i+2]] = Vector3f.copyOf(normal);
				} else {
					normals[indicesArrayB[i+2]].add(normal);
				}
			}
			break;
		case INT:
			int[] indicesArrayI = indices.intProp;
			for(int i = 0;i < indicesArrayI.length;i += 3) {
				Vector3f normal = null;
				Vector3f pos1, pos2, pos3;
				switch(positions.dataType) {
				case BYTE:
					byte[] positionArrayB = positions.byteProp;
					pos1 = new Vector3f(positionArrayB[indicesArrayI[i+0]*3+0], positionArrayB[indicesArrayI[i+0]*3+1], positionArrayB[indicesArrayI[i+0]*3+2]);
					pos2 = new Vector3f(positionArrayB[indicesArrayI[i+1]*3+0], positionArrayB[indicesArrayI[i+1]*3+1], positionArrayB[indicesArrayI[i+1]*3+2]);
					pos3 = new Vector3f(positionArrayB[indicesArrayI[i+2]*3+0], positionArrayB[indicesArrayI[i+2]*3+1], positionArrayB[indicesArrayI[i+2]*3+2]);
					normal = Geometry.calculateNormal(pos1, pos2, pos3);
					break;
				case FLOAT:
					float[] positionArrayF = positions.floatProp;
					pos1 = new Vector3f(positionArrayF[indicesArrayI[i+0]*3+0], positionArrayF[indicesArrayI[i+0]*3+1], positionArrayF[indicesArrayI[i+0]*3+2]);
					pos2 = new Vector3f(positionArrayF[indicesArrayI[i+1]*3+0], positionArrayF[indicesArrayI[i+1]*3+1], positionArrayF[indicesArrayI[i+1]*3+2]);
					pos3 = new Vector3f(positionArrayF[indicesArrayI[i+2]*3+0], positionArrayF[indicesArrayI[i+2]*3+1], positionArrayF[indicesArrayI[i+2]*3+2]);
					normal = Geometry.calculateNormal(pos1, pos2, pos3);
					break;
				case INT:
					int[] positionArrayI = positions.intProp;
					pos1 = new Vector3f(positionArrayI[indicesArrayI[i+0]*3+0], positionArrayI[indicesArrayI[i+0]*3+1], positionArrayI[indicesArrayI[i+0]*3+2]);
					pos2 = new Vector3f(positionArrayI[indicesArrayI[i+1]*3+0], positionArrayI[indicesArrayI[i+1]*3+1], positionArrayI[indicesArrayI[i+1]*3+2]);
					pos3 = new Vector3f(positionArrayI[indicesArrayI[i+2]*3+0], positionArrayI[indicesArrayI[i+2]*3+1], positionArrayI[indicesArrayI[i+2]*3+2]);
					normal = Geometry.calculateNormal(pos1, pos2, pos3);
					break;
				}
				if(normals[indicesArrayI[i+0]] == null) {
					normals[indicesArrayI[i+0]] = Vector3f.copyOf(normal);
				} else {
					normals[indicesArrayI[i+0]].add(normal);
				}
				if(normals[indicesArrayI[i+1]] == null) {
					normals[indicesArrayI[i+1]] = Vector3f.copyOf(normal);
				} else {
					normals[indicesArrayI[i+1]].add(normal);
				}
				if(normals[indicesArrayI[i+2]] == null) {
					normals[indicesArrayI[i+2]] = Vector3f.copyOf(normal);
				} else {
					normals[indicesArrayI[i+2]].add(normal);
				}
			}
			break;
		case FLOAT:
			/*If you have floating point indices, you are way more fucked up than this function can deal with*/
			break;
		}
		
		float[] finalNormals = new float[normals.length*3];
		for(int i = 0;i < normals.length;i ++) {
			if(normals[i] == null) {
				normals[i] = new Vector3f();
			}
			normals[i].normalize();
			finalNormals[i*3+0] = (normals[i].x/2) + 0.5f;
			finalNormals[i*3+1] = (normals[i].y/2) + 0.5f;
			finalNormals[i*3+2] = (normals[i].z/2) + 0.5f;
		}
		
		VertexedProperty normalsProperty = new VertexedProperty(finalNormals, VertexPropertyType.NORMALS);
		VertexedProperty[] newProperties = new VertexedProperty[properties.length+1];
		
		for(int i = 0;i < properties.length;i ++) {
			newProperties[i] = properties[i];
		}
		
		newProperties[newProperties.length-1] = normalsProperty;
		
		return new Mesh(null, null,newProperties);
	}

}
