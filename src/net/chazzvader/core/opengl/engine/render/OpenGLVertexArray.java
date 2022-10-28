package net.chazzvader.core.opengl.engine.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import net.chazzvader.core.generic.Configuration;
import net.chazzvader.core.generic.Configuration.Renderer;
import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.IDeletable;
import net.chazzvader.core.generic.engine.mesh.Mesh;
import net.chazzvader.core.generic.engine.mesh.VertexedProperty;
import net.chazzvader.core.generic.engine.mesh.VertexedProperty.VertexPropertyDataType;
import net.chazzvader.core.generic.engine.mesh.VertexedProperty.VertexPropertyType;
import net.chazzvader.core.generic.util.Utils;
import net.chazzvader.core.opengl.shader.OpenGLShaderLocations;

/**
 * OpenGL vertex array
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class OpenGLVertexArray implements IDeletable {

	private int vao, ibo, count, dataType;
	private int[] otherBuffers;

	/**
	 * Creates and binds a vertex array based on a mesh
	 * 
	 * @param mesh The mesh to make the array out of
	 */
	public OpenGLVertexArray(Mesh mesh) {
		Configuration.assertRenderer(Renderer.OPEN_GL);
		// Verify it has position and indices and make sure no duplicates
		VertexedProperty[] array = mesh.properties;
		VertexPropertyType[] types = VertexPropertyType.values();
		int[] counts = new int[types.length];
		boolean vp = false, in = false;
		for (int i = 0; i < array.length; i++) {
			VertexPropertyType type = array[i].usage;
			for (int j = 0; j < types.length; j++) {
				if (type == types[j]) {
					counts[j]++;
				}
				if (type == VertexPropertyType.VERTEX_POS) {
					vp = true;
				}
				if (type == VertexPropertyType.INDICES) {
					in = true;
				}
			}
		}
		for (int i = 0; i < counts.length; i++) {
			if (counts[i] > 1) {
				Logging.log("Duplicate vertex data types!", "OpenGL Vertex Array", LoggingLevel.ERR);
				throw new IllegalArgumentException("Duplicate vertex data types!");
			}
		}
		if (vp == false) {
			Logging.log("No vertex position data!", "OpenGL Vertex Array", LoggingLevel.ERR);
			throw new IllegalArgumentException("No vertex position data!");
		}
		if (in == false) {
			Logging.log("No indices data!", "OpenGL Vertex Array", LoggingLevel.ERR);
			throw new IllegalArgumentException("No indices data!");
		}
		// Actually make vertex array
		vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);

		otherBuffers = new int[array.length];
		
		for (int i = 0; i < array.length; i++) {
			if (array[i].usage == VertexPropertyType.INDICES) {
				if (array[i].dataType == VertexPropertyDataType.INT) {
					count = array[i].intProp.length;
				} else {
					count = array[i].byteProp.length;
				}
				ibo = GL15.glGenBuffers();
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
				if (array[i].dataType == VertexPropertyDataType.INT) {
					GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Utils.createIntBuffer(array[i].intProp),
							GL15.GL_STATIC_DRAW);
					dataType = GL11.GL_UNSIGNED_INT;
				} else {
					GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Utils.createByteBuffer(array[i].byteProp),
							GL15.GL_STATIC_DRAW);
					dataType = GL11.GL_UNSIGNED_BYTE;
				}
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
			} else {
				int temp = GL15.glGenBuffers();
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, temp);
				switch (array[i].dataType) {
				case BYTE:
					GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Utils.createByteBuffer(array[i].byteProp),
							GL15.GL_STATIC_DRAW);
					GL20.glVertexAttribPointer(OpenGLShaderLocations.fromVertexData(array[i].usage),
							OpenGLShaderLocations.sizeFromVertexData(array[i].usage), GL11.GL_BYTE, false, 0, 0);
					break;
				case FLOAT:
					GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Utils.createFloatBuffer(array[i].floatProp),
							GL15.GL_STATIC_DRAW);
					GL20.glVertexAttribPointer(OpenGLShaderLocations.fromVertexData(array[i].usage),
							OpenGLShaderLocations.sizeFromVertexData(array[i].usage), GL11.GL_FLOAT, false, 0, 0);
					break;
				case INT:
					GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Utils.createIntBuffer(array[i].intProp),
							GL15.GL_STATIC_DRAW);
					GL20.glVertexAttribPointer(OpenGLShaderLocations.fromVertexData(array[i].usage),
							OpenGLShaderLocations.sizeFromVertexData(array[i].usage), GL11.GL_INT, false, 0, 0);
					break;
				}
				GL20.glEnableVertexAttribArray(OpenGLShaderLocations.fromVertexData(array[i].usage));
				otherBuffers[i] = temp;
			}
		}

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}

	/**
	 * Binds (loads to graphics card) the vertex array
	 */
	public void bind() {
		checkDelete();
		GL30.glBindVertexArray(vao);
		if (ibo > 0)
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
	}

	/**
	 * Unbinds this vertex array and all other vertex arrays
	 */
	public void unbind() {
		checkDelete();
		if (ibo > 0)
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}

	/**
	 * Draw the vertex array on screen.<br>
	 * This requires the array to be bound!
	 */
	public void draw() {
		checkDelete();
		if (ibo > 0) {
			GL15.glDrawElements(GL11.GL_TRIANGLES, count, dataType, 0);
		} else {
			GL15.glDrawArrays(GL11.GL_TRIANGLES, 0, count);
		}
	}

	/**
	 * Binds, and draws the array. Full draw cycle
	 * @see OpenGLVertexArray#bind()
	 * @see OpenGLVertexArray#draw()
	 */
	public void render() {
		checkDelete();
		bind();
		draw();
	}

	@Override
	public void delete() {
		checkDelete();
		GL30.glDeleteVertexArrays(vao);
		GL15.glDeleteBuffers(ibo);
		for(int i = 0;i < otherBuffers.length;i ++) {
			GL15.glDeleteBuffers(otherBuffers[i]);
		}
		deleted = true;
	}
	
	private boolean deleted = false;
	
	private void checkDelete() {
		if(deleted) {
			Logging.log("Array Deleted", "OpenGL Vertex Array", LoggingLevel.ERR);
		}
	}

}
