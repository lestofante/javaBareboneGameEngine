package test3d.lesto;

import java.util.logging.Level;
import java.util.logging.Logger;

import test3d.lesto.component.Model3d;
import test3d.lesto.component.Position;
import test3d.lesto.component.Rotation;
import test3d.lesto.graphics.GraphicLock;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;

public class RenderingSystem extends EntitySystem {
	@Mapper ComponentMapper<Model3d> modello;
	@Mapper ComponentMapper<Position> posizione;
	@Mapper ComponentMapper<Rotation> rotazione;
	
	Logger log = Logger.getLogger( getClass().getName() );
	private GraphicLock lock;
	
	public RenderingSystem(GraphicLock lock) {
		super( Aspect.getAspectForAll(Model3d.class).one(Position.class, Rotation.class) );
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
		lock.rwl.writeLock().lock();
		try{
			elaborate (arg0);
		}catch(Throwable e){
			log.log(Level.SEVERE, "Errore imprevisto elaborazione entità", e);
		}finally{
			//write unlock
			lock.rwl.writeLock().unlock();
		}
	}

	private void elaborate(ImmutableBag<Entity> arg0) {
		for (Entity e: arg0){
			Model3d m = modello.get(e);
			Position p = posizione.get(e);
			Rotation r = rotazione.get(e);
			if(p != null){
				m.setPosizione( p.get() );
			}
			if (r != null){
				m.setRotazione( r.get() );
			}
		}
	}


}
