package test3d.lesto.component;

import com.artemis.Component;
import com.bulletphysics.linearmath.Transform;

public class ComponentModel3d extends Component{

	public float pos[] = {0,0,0}; //position
	public float rot[] = {0,0,0,0}; //rotation
	
	float tmp[] = new float[3];

	public void set(Transform transform) {
		// TODO here we have to clone position androtation to pos and rot. pay attention to CLONE, as this method is called sincroniusly from RenderingSystem!
		transform.origin.get(tmp);
		pos[0] = tmp[0];
		pos[1] = tmp[1];
		pos[2] = tmp[2];
	}

}
