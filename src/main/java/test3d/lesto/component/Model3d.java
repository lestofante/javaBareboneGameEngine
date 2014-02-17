package test3d.lesto.component;

import org.lwjgl.util.vector.Quaternion;

import com.artemis.Component;

public class Model3d extends Component{

	public float pos[] = {0,0,0}; //position
	public float rot[] = {0,0,0,0}; //rotation

	public void setPosizione(float[] fs) {
		pos = fs;
	}

	public void setRotazione(Quaternion quaternion) {
		// TODO Auto-generated method stub
		//TODO: i dunno how this math work, i've to learn
	}

}
