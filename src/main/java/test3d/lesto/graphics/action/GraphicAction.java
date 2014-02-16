package test3d.lesto.graphics.action;

public abstract class GraphicAction {

	public static enum ActionType {
		CREATE, REMOVE, FOLLOW_OBJECT
	}

	public ActionType actionType;

	public GraphicAction(ActionType actionType) {
		this.actionType = actionType;
	}
}
