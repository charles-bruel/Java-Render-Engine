package net.chazzvader.core.generic.engine.mesh;

import java.util.Arrays;

import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.object.EngineObject;

/**
 * A property that can have a different value per vertex on a mesh.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class VertexedProperty extends EngineObject {

	@Override
	public String toString() {
		return "VertexedProperty [intProp=" + Arrays.toString(intProp) + ", floatProp=" + Arrays.toString(floatProp)
				+ ", byteProp=" + Arrays.toString(byteProp) + ", type=" + dataType + ", usage=" + usage + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(byteProp);
		result = prime * result + Arrays.hashCode(floatProp);
		result = prime * result + Arrays.hashCode(intProp);
		result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
		result = prime * result + ((usage == null) ? 0 : usage.hashCode());
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
		VertexedProperty other = (VertexedProperty) obj;
		if (!Arrays.equals(byteProp, other.byteProp))
			return false;
		if (!Arrays.equals(floatProp, other.floatProp))
			return false;
		if (!Arrays.equals(intProp, other.intProp))
			return false;
		if (dataType != other.dataType)
			return false;
		if (usage != other.usage)
			return false;
		return true;
	}

	/**
	 * Acceptable values for the property
	 * 
	 * @author csbru
	 * @since 1.0
	 * @version 1
	 */
	@SuppressWarnings("javadoc")
	public enum VertexPropertyDataType {
		BYTE, FLOAT, INT
	}

	/**
	 * What purpose this property fills
	 * 
	 * @author csbru
	 * @since 1.0
	 * @version 1
	 */ 
	@SuppressWarnings("javadoc")
	public enum VertexPropertyType {
		VERTEX_POS(3), TEXTURE_COORDINATES(2), INDICES(-1), COLOR(3), NORMALS(3), NORMAL_TANGENT(3), NORMAL_BITANGENT(3), OTHER1(3), OTHER2(3);
		
		public final int perVertice;
		
		VertexPropertyType(int perVertice) {
			this.perVertice = perVertice;
		}
	}

	/**
	 * The integer array for the property. Null if {@link #dataType} isn't <code>INT</code>.
	 */
	public final int[] intProp;
	/**
	 * The floating point number array for the property. Null if {@link #dataType} isn't <code>FLOAT</code>.
	 */
	public final float[] floatProp;
	/**
	 * The byte array for the property. Null if {@link #dataType} isn't <code>BYTE</code>.
	 */
	public final byte[] byteProp;

	/**
	 * The data type this property has
	 */
	public final VertexPropertyDataType dataType;

	/**
	 * The intended use for this property
	 */
	public final VertexPropertyType usage;

	/**
	 * Creates a VertexedProperty with integer data
	 * @param data The integer data
	 * @param usage The intended use for this property
	 */
	public VertexedProperty(int[] data, VertexPropertyType usage) {
		dataType = VertexPropertyDataType.INT;
		intProp = data;
		floatProp = null;
		byteProp = null;
		this.usage = usage;
	}
	
	/**
	 * Creates a VertexedProperty with floating point data data
	 * @param data The floating point data data
	 * @param usage The intended use for this property
	 */
	public VertexedProperty(float[] data, VertexPropertyType usage) {
		if(usage == VertexPropertyType.INDICES) {
			Logging.log("Indices cannot be floating point", "Vertex", LoggingLevel.ERR);
		}
		dataType = VertexPropertyDataType.FLOAT;
		intProp = null;
		floatProp = data;
		byteProp = null;
		this.usage = usage;
	}
	
	/**
	 * Creates a VertexedProperty with byte data
	 * @param data The byte data
	 * @param usage The intended use for this property
	 */
	public VertexedProperty(byte[] data, VertexPropertyType usage) {
		dataType = VertexPropertyDataType.BYTE;
		intProp = null;
		floatProp = null;
		byteProp = data;
		this.usage = usage;
	}

	/**
	 * Returns the amount of vertices in this property
	 * @return The amount of vertices in this property
	 */
	public int length() {
		int length = 0;
		switch(dataType) {
		case BYTE:
			length = byteProp.length;
			break;
		case FLOAT:
			length = floatProp.length;
			break;
		case INT:
			length = intProp.length;
			break;
		}
		return length/usage.perVertice;
	}

	/**
	 * Returns the data as the most flexible data type, float
	 * @return The data as a float[]
	 */
	public float[] getAsFloatArray() {
		switch(dataType) {
		case BYTE:
			float[] baRet = new float[intProp.length];
			for(int i = 0;i < baRet.length;i ++) {
				baRet[i] = intProp[i];
			}
			return baRet;
		case FLOAT:
			return floatProp;
		case INT:
			float[] faRet = new float[intProp.length];
			for(int i = 0;i < faRet.length;i ++) {
				faRet[i] = intProp[i];
			}
			return faRet;
		}
		return null;
	}
	
}
