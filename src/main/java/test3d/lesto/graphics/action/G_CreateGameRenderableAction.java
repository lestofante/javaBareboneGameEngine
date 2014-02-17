package test3d.lesto.graphics.action;

import test3d.lesto.component.ComponentModel3d;

public class G_CreateGameRenderableAction extends GraphicAction {

	public Integer iD;
	public String modelName;
	public ComponentModel3d model;

	public G_CreateGameRenderableAction(int iD, String modelName, ComponentModel3d modello) {
		super(ActionType.CREATE);
		this.iD = iD;
		this.modelName = modelName;
		this.model = modello;
	}

}
