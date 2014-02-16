package test3d.lesto.graphics.action;

public class G_RemoveGameRenderable extends GraphicAction {

	public Integer iD;

	public G_RemoveGameRenderable(int iD) {
		super(ActionType.REMOVE);
		this.iD = iD;
	}
}