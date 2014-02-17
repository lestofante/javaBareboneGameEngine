package test3d.lesto.graphics.object;

import org.lwjgl.opengl.ARBBufferObject;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;

import test3d.lesto.component.Model3d;

public class VBORenderable extends GameRenderable {

	private int verticesBufferID;
	private int normalsBufferID;
	private int triangleCount;

	public VBORenderable(int vertexBufferID, int normalBufferID, int triangleCount, Model3d model) {
		super(model);
		this.verticesBufferID = vertexBufferID;
		this.normalsBufferID = normalBufferID;
		this.triangleCount = triangleCount;
	}

	@Override
	public void render() {

		GL11.glPushMatrix();

		GL11.glTranslatef(model.pos[0], model.pos[1], model.pos[2]);
		GL11.glRotatef((float) Math.toDegrees(model.rot[0]), model.rot[1], model.rot[2], model.rot[3]);

		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		ARBBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, verticesBufferID);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);

		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		ARBBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, normalsBufferID);
		GL11.glNormalPointer(GL11.GL_FLOAT, 0, 0);

		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, triangleCount * 3);

		GL11.glPopMatrix();

	}

	@Override
	public void renderInterleavedDrawArray() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

}