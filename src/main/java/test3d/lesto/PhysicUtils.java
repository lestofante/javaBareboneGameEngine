package test3d.lesto;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ObjectArrayList;

public class PhysicUtils {
	
	public ObjectArrayList<CollisionShape> collisionShapes = new ObjectArrayList<CollisionShape>();
	
	//ArrayList<RigidBody> b = new ArrayList<>(); 
	
	DiscreteDynamicsWorld dynamicsWorld;
	
	public DynamicsWorld initPhysics() {

		// collision configuration contains default setup for memory, collision setup
		DefaultCollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();

		// use the default collision dispatcher. For parallel processing you can use a diffent dispatcher (see Extras/BulletMultiThreaded)
		CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);

		DbvtBroadphase broadphase = new DbvtBroadphase();

		// the default constraint solver. For parallel processing you can use a different solver (see Extras/BulletMultiThreaded)
		SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();

		// TODO: needed for SimpleDynamicsWorld
		// sol.setSolverMode(sol.getSolverMode() & ~SolverMode.SOLVER_CACHE_FRIENDLY.getMask());

		dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);

		dynamicsWorld.setGravity(new Vector3f(0f, -10f, 0f));

		// create a few basic rigid bodies
		CollisionShape groundShape = new BoxShape(new Vector3f(50f, 50f, 50f));

		collisionShapes.add(groundShape);

		Transform groundTransform = new Transform();
		groundTransform.setIdentity();
		groundTransform.origin.set(0, -70, 0);

		// We can also use DemoApplication::localCreateRigidBody, but for clarity it is provided here:
		{
			float mass = 0f;

			// rigidbody is dynamic if and only if mass is non zero, otherwise static
			boolean isDynamic = (mass != 0f);

			Vector3f localInertia = new Vector3f(0, 0, 0);
			if (isDynamic) {
				groundShape.calculateLocalInertia(mass, localInertia);
			}

			// using motionstate is recommended, it provides interpolation capabilities, and only synchronizes 'active' objects
			DefaultMotionState myMotionState = new DefaultMotionState(groundTransform);
			RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, myMotionState, groundShape, localInertia);
			RigidBody body = new RigidBody(rbInfo);
			body.setRestitution(1);

			// add the body to the dynamics world
			dynamicsWorld.addRigidBody(body);
		}
		
		return dynamicsWorld;
	}
	
	public RigidBody loadRigidBody(){
		CollisionShape colShape = new BoxShape(new Vector3f(1, 1, 1));
		
		collisionShapes.add(colShape);

		// Create Dynamic Objects
		Transform startTransform = new Transform();
		startTransform.setIdentity();

		float mass = 1f;

		// rigidbody is dynamic if and only if mass is non zero, otherwise static
		boolean isDynamic = (mass != 0f);

		Vector3f localInertia = new Vector3f(0, 0, 0);
		if (isDynamic) {
			colShape.calculateLocalInertia(mass, localInertia);
		}
		startTransform.origin.set(0,0,0);

		// using motionstate is recommended, it provides interpolation capabilities, and only synchronizes 'active' objects
		DefaultMotionState myMotionState = new DefaultMotionState(startTransform);
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, myMotionState, colShape, localInertia);
		rbInfo.restitution = 1;
		
		RigidBody body = new RigidBody(rbInfo);
		//body.setActivationState(RigidBody.ISLAND_SLEEPING);

		dynamicsWorld.addRigidBody(body);
		//body.setActivationState(RigidBody.ISLAND_SLEEPING);
		
		//b.add(body);
		
		return body;
	}
}
