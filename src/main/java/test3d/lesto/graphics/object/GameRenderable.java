package test3d.lesto.graphics.object;

import test3d.lesto.component.Model3d;

public abstract class GameRenderable {

	public Model3d model;

	public GameRenderable(Model3d model) {
		this.model = model;
	}

	public abstract void render();

	public abstract void renderInterleavedDrawArray();

}
