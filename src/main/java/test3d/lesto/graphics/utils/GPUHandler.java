package test3d.lesto.graphics.utils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBBufferObject;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GLContext;

public class GPUHandler {

	public static final Logger log = Logger.getLogger( GPUHandler.class.getName() );

	public static void bufferData(int id, FloatBuffer buffer, int GLHint) {
		if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
			ARBBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, id);
			ARBBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, buffer, GLHint);
			log.fine("VBO buffering succeded");
		} else {
			try {
				throw new Exception();
			} catch (Exception e) {
				log.log(Level.SEVERE, "VBO buffering failed", e);
			}
			System.exit(-1);
		}
	}

	public static void bufferElementData(int id, IntBuffer buffer, int GLHint) {
		if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
			ARBBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, id);
			ARBBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, buffer, GLHint);
			log.fine("VBO buffering succeded");
		} else {
			try {
				throw new Exception();
			} catch (Exception e) {
				log.log(Level.SEVERE, "VBO buffering failed", e);
			}
			System.exit(-1);
		}
	}

	public static int createVBOID() {
		if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
			IntBuffer buffer = BufferUtils.createIntBuffer(1);
			ARBBufferObject.glGenBuffersARB(buffer);
			log.fine("VBO creation succeded. VBOID: " + buffer.get(0));
			return buffer.get(0);
		} else {
			try {
				throw new Exception();
			} catch (Exception e) {
				log.log(Level.SEVERE, "VBO creation failed", e);
			}
			System.exit(-1);
		}
		return -1;
	}
}