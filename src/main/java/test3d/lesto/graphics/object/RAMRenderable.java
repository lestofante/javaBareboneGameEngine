package test3d.lesto.graphics.object;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

import test3d.lesto.component.ComponentModel3d;

public class RAMRenderable extends GameRenderable {

	protected FloatBuffer normalsBuffer;
	protected FloatBuffer verticesBuffer;
	protected FloatBuffer interleavedBuffer;
/*
	private RAMRenderable(FloatBuffer verticesBuffer, FloatBuffer normalsBuffer, FloatBuffer interleavedBuffer) {
		super( new Model3d() );
		setBuffers(verticesBuffer, normalsBuffer, interleavedBuffer);
	}
*/
	public RAMRenderable(FloatBuffer verticesBuffer, FloatBuffer normalsBuffer, FloatBuffer interleavedBuffer, ComponentModel3d model) {
		super(model);
		setBuffers(verticesBuffer, normalsBuffer, interleavedBuffer);
	}

	@Override
	public void render() {

		GL11.glPushMatrix();

		verticesBuffer.rewind();

		GL11.glTranslatef(model.pos[0], model.pos[1], model.pos[2]);
		GL11.glRotatef((float) Math.toDegrees(model.rot[0]), model.rot[1], model.rot[2], model.rot[3]);

		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glVertexPointer(3, 0, verticesBuffer);
		GL11.glNormalPointer(0, normalsBuffer);

		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, verticesBuffer.capacity() / 3);

		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);

		GL11.glPopMatrix();

	}

	@Override
	public void renderInterleavedDrawArray() {

		GL11.glPushMatrix();

		GL11.glTranslatef(model.pos[0], model.pos[1], model.pos[2]);
		GL11.glRotatef((float) Math.toDegrees(model.rot[0]), model.rot[1], model.rot[2], model.rot[3]);

		GL11.glInterleavedArrays(GL11.GL_N3F_V3F, 0, interleavedBuffer);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, interleavedBuffer.capacity() / 6);

		GL11.glPopMatrix();

	}

	public void setBuffers(FloatBuffer verticesBuffer, FloatBuffer normalsBuffer, FloatBuffer interleavedBuffer) {

		this.verticesBuffer = verticesBuffer;
		this.normalsBuffer = normalsBuffer;
		this.interleavedBuffer = interleavedBuffer;

	}
}