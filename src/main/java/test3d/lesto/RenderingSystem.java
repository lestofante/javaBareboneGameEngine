package test3d.lesto;

import test3d.lesto.component.Model3d;
import test3d.lesto.component.Position;
import test3d.lesto.component.Rotation;

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
	
	public RenderingSystem() {
		super( Aspect.getAspectForAll(Model3d.class).getAspectForAll(Position.class, Rotation.class) );
	}

	@Override
	protected boolean checkProcessing() {
		//TODO: was false, dunno exactly what it does.
		return true;
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> arg0) {
		if(posizione != null && rotazione != null){
			
		}
	}


}
