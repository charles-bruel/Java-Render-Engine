package net.chazzvader.core.generic.engine.creator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import net.chazzvader.core.generic.Configuration;
import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.mesh.Mesh;
import net.chazzvader.core.generic.engine.mesh.VertexedProperty;
import net.chazzvader.core.generic.engine.mesh.VertexedProperty.VertexPropertyType;
import net.chazzvader.core.generic.math.Vector2f;
import net.chazzvader.core.generic.math.Vector3f;
import net.chazzvader.core.generic.util.Utils;

public class OBJLoader {
	
	private OBJLoader() {
	}
	
	
	//TODO: OBJReader null checks
	public static Mesh loadFromFilePath(String filePath) {
		try {
			return loadFromReader(new BufferedReader(new InputStreamReader(Utils.getFromPath(filePath), "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Mesh loadFromReader(BufferedReader reader) {//TODO: Fix arrays
		try {
			ArrayList<Vector3f> positions = new ArrayList<>();
			ArrayList<Vector2f> texCoords = new ArrayList<>();
			ArrayList<Vector3f> normals = new ArrayList<>();
			ArrayList<Integer> indices = new ArrayList<>();
			String buffer = "";
			while ((buffer = reader.readLine()) != null) {
				char[] ca = buffer.toCharArray();
				String[] split = buffer.split(" ");
				if(ca.length == 0) {
					/*Empty line, just skip it*/
				} else if(ca[0] == '#') {
					/*Comment, just skip it*/
				} else if(ca[0] == 'v') {
					if(ca[1] == ' ') {
						positions.add(new Vector3f(Float.parseFloat(split[1]), Float.parseFloat(split[2]), Float.parseFloat(split[3])));
					} else if(ca[1] == 't') {
						texCoords.add(new Vector2f(Float.parseFloat(split[1]), Float.parseFloat(split[2])));
					} else if(ca[1] == 'n') {
						normals.add(new Vector3f(Float.parseFloat(split[1]), Float.parseFloat(split[2]), Float.parseFloat(split[3])));
					} else {
						Logging.log("Unsupported command " + split[0], "OBJ Loader", LoggingLevel.DEBUG);
					}
				}else if(ca[0] == 'f') {
					if(buffer.contains("/")) {
						Logging.log("Face commands with different normal/texture coordinate indices currently unsupported.", "OBJLoader", LoggingLevel.ERR);
						return null;
					}
					int numParams = split.length-1;
					if(numParams < 3) {
						Logging.log("Face command with only " + numParams + " parameters, ignoring", "OBJLoader", LoggingLevel.WARN);
					} else if(numParams == 3) {/*Single triangle*/
						indices.add(Integer.parseInt(split[1])-1);
						indices.add(Integer.parseInt(split[2])-1);
						indices.add(Integer.parseInt(split[3])-1);
					} else {
						for(int j = 1;j < numParams-1;j ++) {/*Polygon, treat as coplanar, convex polygon and use triangle fan.*/
							indices.add(Integer.parseInt(split[1])-1);
							indices.add(Integer.parseInt(split[j+1])-1);
							indices.add(Integer.parseInt(split[j+2])-1);
						}
					}
				} else {
					Logging.log("Unsupported command " + split[0], "OBJ Loader", LoggingLevel.DEBUG);
				}
			}
			reader.close();
			
			float[] positionsFinal = new float[positions.size()*3];
			for(int i = 0;i < positions.size();i ++) {
				Vector3f pos = positions.get(i);
				positionsFinal[i*3+0] = pos.x;
				positionsFinal[i*3+1] = pos.y;
				positionsFinal[i*3+2] = pos.z;
			}
			VertexedProperty positionsProperty = new VertexedProperty(positionsFinal, VertexPropertyType.VERTEX_POS);
			
			int[] indicesFinal = new int[indices.size()];
			for(int i = 0;i < indices.size();i ++) {
				indicesFinal[i] = indices.get(i);
			}
			VertexedProperty indicesProperty = new VertexedProperty(indicesFinal, VertexPropertyType.INDICES);

			if(texCoords.size() != 0) {
				float[] texCoordsFinal = new float[texCoords.size()*2];
				for(int i = 0;i < texCoords.size();i ++) {
					Vector2f tc = texCoords.get(i);
					texCoordsFinal[i*2+0] = tc.x;
					texCoordsFinal[i*2+1] = tc.y;
				}
				VertexedProperty texCoordsProperty = new VertexedProperty(texCoordsFinal, VertexPropertyType.TEXTURE_COORDINATES);

				if(normals.size() != 0) {/*Normals & TC*/					
					float[] normalsFinal = new float[normals.size()*3];
					for(int i = 0;i < normals.size();i ++) {
						Vector3f norm = normals.get(i);
						normalsFinal[i*3+0] = norm.x;
						normalsFinal[i*3+1] = norm.y;
						normalsFinal[i*3+2] = norm.z;
					}
					VertexedProperty normalsProperty = new VertexedProperty(normalsFinal, VertexPropertyType.NORMALS);
					return new Mesh(positionsProperty, indicesProperty, texCoordsProperty, normalsProperty);
				} else {/*TC*/
					return new Mesh(positionsProperty, indicesProperty, texCoordsProperty);
				}
			} else {
				if(normals.size() != 0) {/*Normals*/					
					float[] normalsFinal = new float[normals.size()*3];
					for(int i = 0;i < normals.size();i ++) {
						Vector3f norm = normals.get(i);
						normalsFinal[i*3+0] = norm.x;
						normalsFinal[i*3+1] = norm.y;
						normalsFinal[i*3+2] = norm.z;
					}
					VertexedProperty normalsProperty = new VertexedProperty(normalsFinal, VertexPropertyType.NORMALS);
					
					return new Mesh(positionsProperty, indicesProperty, normalsProperty);
				} else {/**/
					return new Mesh(positionsProperty, indicesProperty);
				}
			}
			
			
		} catch (IOException e) {
			Logging.log("Invalid input: IO Exception", "OBJLoader", LoggingLevel.ERR);
			if(Configuration.isDebugMode()) {
				e.printStackTrace();
			}
			return null;
		} catch (ArrayIndexOutOfBoundsException e) {
			Logging.log("Malformed OBJ: Array Index Out Of Bounds", "OBJLoader", LoggingLevel.ERR);
			if(Configuration.isDebugMode()) {
				e.printStackTrace();
			}
			return null;
		} catch (NumberFormatException e) {
			Logging.log("Malformed OBJ: Number Format", "OBJLoader", LoggingLevel.ERR);
			if(Configuration.isDebugMode()) {
				e.printStackTrace();
			}
			return null;
		} catch (NullPointerException e) {
			Logging.log("Malformed OBJ: Null Pointer", "OBJLoader", LoggingLevel.ERR);
			if(Configuration.isDebugMode()) {
				e.printStackTrace();
			}
			return null;
		} catch (Exception e) {
			Logging.log("OBJ Loading Error: " + e.getClass().getSimpleName(), "OBJLoader", LoggingLevel.ERR);
			if(Configuration.isDebugMode()) {
				e.printStackTrace();
			}
			return null;
		}
	}

}
