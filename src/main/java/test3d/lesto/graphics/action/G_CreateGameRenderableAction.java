package test3d.lesto.graphics.action;

public class G_CreateGameRenderableAction extends GraphicAction {

	public Integer iD;
	public float[] transform;
	public String modelName;

	public G_CreateGameRenderableAction(int iD, String modelName, float[] transform) {
		super(ActionType.CREATE);
		this.iD = iD;
		this.modelName = modelName;
		this.transform = transform;
	}

}
