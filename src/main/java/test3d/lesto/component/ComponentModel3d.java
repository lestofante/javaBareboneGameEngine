package test3d.lesto.component;

import com.artemis.Component;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;

public class ComponentModel3d extends Component{

	public Transform transform = new Transform();

	//public float pos[] = {0,0,0}; //position
	public float rot[] = {0,0,0,0}; //rotation

	private RigidBody body;
	
	//float tmp[] = new float[3];

	//public Vector3f pos = new Vector3f();

	public void set(Transform transform) {
		// TODO here we have to clone position androtation to pos and rot. pay attention to CLONE, as this method is called sincroniusly from RenderingSystem!
		//transform.origin.get(pos);
		
		this.transform  = transform;
		
		//pos[0] = tmp[0];
		//pos[1] = tmp[1];
		//pos[2] = tmp[2];
	}

	public void set(RigidBody body) {
		this.body = body;
	}

	public Transform getTransform() {
		return body.getWorldTransform(transform);
	}

}
