package test3d.lesto;

import java.util.logging.Level;
import java.util.logging.Logger;

import test3d.lesto.component.ComponentTransform;
import test3d.lesto.component.ComponentModel3d;
import test3d.lesto.graphics.GraphicLock;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;

public class RenderingSystem extends EntitySystem {
	@Mapper ComponentMapper<ComponentModel3d> modello;
	@Mapper ComponentMapper<ComponentTransform> posizione;

	
	Logger log = Logger.getLogger( getClass().getName() );
	private GraphicLock lock;
	
	@SuppressWarnings("unchecked")
	public RenderingSystem(GraphicLock lock) {
		super( Aspect.getAspectForAll(ComponentModel3d.class).one(ComponentTransform.class) );
		this.lock = lock;
	}

	@Override
	protected boolean checkProcessing() {
		//TODO: was false, dunno exactly what it does.
		return true;
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> arg0) {
		//write lock
		//long t = System.nanoTime();
		lock.rwl.writeLock().lock();
		//System.out.println("tempo escuzione lock: "+ (System.nanoTime()-t)/1000000 );
		try{
			elaborate (arg0);
		}catch(Throwable e){
			log.log(Level.SEVERE, "Errore imprevisto elaborazione entità", e);
		}finally{
			//System.out.println("tempo escuzione lock+elaborate: "+ (System.nanoTime()-t)/1000000 );
			////write unlock
			lock.rwl.writeLock().unlock();
		}
		//System.out.println("tempo escuzione RenderingSystem: "+ (System.nanoTime()-t)/1000000 );
	}

	private void elaborate(ImmutableBag<Entity> arg0) {
		for (Entity e: arg0){
			ComponentModel3d m = modello.get(e);
			ComponentTransform p = posizione.get(e);
			
			m.set( p.get() );
		}
	}


}
