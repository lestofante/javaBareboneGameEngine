package test3d.lesto;

import test3d.lesto.component.ComponentBody;
import test3d.lesto.component.ComponentTransform;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;

public class MovementSystem extends EntitySystem {
	@Mapper ComponentMapper<ComponentBody> body;
	@Mapper ComponentMapper<ComponentTransform> posizione;
	
	@SuppressWarnings("unchecked")
	public MovementSystem() {
		super( Aspect.getAspectForAll(ComponentBody.class, ComponentTransform.class) );
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> arg0) {
		for (Entity e: arg0){
			ComponentBody b = body.get(e);
			ComponentTransform p = posizione.get(e);

			p.set( b.getTransform() );
		}
	}

}
