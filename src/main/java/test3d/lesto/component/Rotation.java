package test3d.lesto.component;

import org.lwjgl.util.vector.Quaternion;

import com.artemis.Component;

public class Rotation extends Component{

	Quaternion rotazione = new Quaternion();
	
	public Quaternion get() {
		return rotazione;
	}

}
