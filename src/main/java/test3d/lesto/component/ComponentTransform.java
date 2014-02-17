package test3d.lesto.component;

import com.artemis.Component;
import com.bulletphysics.linearmath.Transform;

public class ComponentTransform extends Component{

	Transform posizione3d = new Transform();
	
	public Transform get() {
		return posizione3d;
	}

	public void set(Transform posizione) {
		posizione3d = posizione;
	}

}
