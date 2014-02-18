package test3d.lesto.component;

import com.artemis.Component;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;

public class ComponentTransform extends Component{

	Transform posizione3d = new Transform();
	private RigidBody body;
/*	
	public Transform get() {
		return posizione3d;
	}
*/
	public void set(Transform posizione) {
		posizione3d = posizione;
	}

	public void set(RigidBody body) {
		this.body = body;
	}

	public RigidBody getBody() {
		// TODO Auto-generated method stub
		return body;
	}

}
