package test3d.lesto.component;

import com.artemis.Component;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;

public class ComponentBody extends Component{

	private RigidBody myBody;
	Transform ris = new Transform();

	public ComponentBody(RigidBody body) {
		this.myBody = body;
	}

	public Transform getTransform() {
		return myBody.getWorldTransform(ris);
	}

}
