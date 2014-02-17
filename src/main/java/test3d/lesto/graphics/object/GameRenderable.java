package test3d.lesto.graphics.object;

import test3d.lesto.component.ComponentModel3d;

public abstract class GameRenderable {

	public ComponentModel3d model;

	public GameRenderable(ComponentModel3d model) {
		this.model = model;
	}

	public abstract void render();

	public abstract void renderInterleavedDrawArray();

}
