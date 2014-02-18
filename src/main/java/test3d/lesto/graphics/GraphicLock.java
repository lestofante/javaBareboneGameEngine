package test3d.lesto.graphics;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GraphicLock {

	public final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	//public final AtomicBoolean lock = new AtomicBoolean(false); 

}
