package test3d.lesto.graphics;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import test3d.lesto.graphics.action.GraphicAction;

public class AsyncActionBus {

	//public ReentrantReadWriteLock sharedLock = new ReentrantReadWriteLock(false);

	/*
	 * GRAPHICS
	 */
	private final ConcurrentLinkedQueue<GraphicAction> graphicActions = new ConcurrentLinkedQueue<>();

	public AtomicBoolean graphicsStarted = new AtomicBoolean(false);

	public void addGraphicsAction(GraphicAction a) {
		graphicActions.offer(a);
	}
	
	

	public ArrayList<GraphicAction> getGraphicActions() {
		ArrayList<GraphicAction> returnActions = new ArrayList<>();
		GraphicAction temp = graphicActions.poll();
		while (temp != null) {
			returnActions.add(temp);
			temp = graphicActions.poll();
		}
		return returnActions;
	}


}