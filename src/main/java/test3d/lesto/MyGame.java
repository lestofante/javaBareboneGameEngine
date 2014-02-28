package test3d.lesto;

import org.lwjgl.opengl.DisplayMode;

import test3d.lesto.component.ComponentBody;
import test3d.lesto.component.ComponentModel3d;
import test3d.lesto.component.ComponentTransform;
import test3d.lesto.graphics.AsyncActionBus;
import test3d.lesto.graphics.GraphicsManager;
import test3d.lesto.graphics.action.G_CreateGameRenderableAction;

import com.artemis.Entity;
import com.artemis.World;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;

/**
 * Hello world!
 * 
 */
public class MyGame {

	private static final long TIME = 10;

	public static void main(String[] args) {
		System.out.println("Hello World!");
		new MyGame();
	}

	private World world;

	private final AsyncActionBus graphicBus = new AsyncActionBus();// THIS IS THE BUS TO USE THE GRAPHIC MANAGER
	private GraphicsManager graphicEngine = new GraphicsManager(new DisplayMode(800, 600), false, true, graphicBus);
	PhysicUtils physic = new PhysicUtils();

	private float incremento = 0.05f;

	public MyGame() {
		world = new World();

		world.setSystem(new MovementSystem());
		world.setSystem(new RenderingSystem(graphicEngine.getLock()));

		world.initialize();

		Thread graphicsThread = new Thread(graphicEngine);
		graphicsThread.setName("Client Graphics");
		graphicsThread.start();

		DynamicsWorld dynamicsWorld = physic.initPhysics();

		// dynamicsWorld.addRigidBody( createGroundBody() );

		addTestObjects();

		long lastProcess = System.currentTimeMillis(), tmpProcess;
		long elapsed;
		long t;

		while (true) {

			tmpProcess = System.currentTimeMillis();

			elapsed = tmpProcess - lastProcess;

			t = System.nanoTime();
			world.setDelta(elapsed);
			// System.out.println("tempo escuzione artemis1: "+ (System.nanoTime()-t)/1000000 );
			// t = System.nanoTime();
			world.process();
			// System.out.println("tempo escuzione artemis2: "+ (System.nanoTime()-t)/1000000 );

			// t = System.nanoTime();
			dynamicsWorld.stepSimulation(elapsed / 1000.0f, 10);
			// System.out.println("tempo escuzione jbullet: "+ (System.nanoTime()-t)/1000000 );
			
			asd.get().origin.x += incremento;
			
			if (Math.abs(asd.get().origin.x) > 30){
				incremento *=-1; 
			}
			

			System.out.println("tempo escuzione ciclo: " + (System.nanoTime() - t) / 1000000);
			
			System.out.println("asd.get().origin.x: " + asd.get().origin.x);
			
			lastProcess = tmpProcess;

			System.out.println("time elapsed: " + elapsed);
			if (elapsed < TIME) {
				try {
					Thread.sleep(TIME);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println("time elapsed: " + elapsed);
			}
		}
	}
	
	ComponentTransform asd = new ComponentTransform();

	private void addTestObjects() {
		{
			Entity giocatore1 = world.createEntity();

			// aggiungi parte di posizione
			giocatore1.addComponent(new ComponentTransform());

			// aggiungi parte grafica
			ComponentModel3d modello = new ComponentModel3d();
			graphicBus.addGraphicsAction(new G_CreateGameRenderableAction(0, "suzanne.obj", modello));

			giocatore1.addComponent(modello);

			// aggiungi parte fisica
			RigidBody boxBody = physic.loadRigidBody();
			ComponentBody componentBody = new ComponentBody(boxBody);
			boxBody.setUserPointer(componentBody);

			giocatore1.addComponent(componentBody);

			giocatore1.addToWorld();
		}

		{
			Entity giocatore2 = world.createEntity();

			// aggiungi parte di posizione
			giocatore2.addComponent(asd);

			// aggiungi parte grafica
			ComponentModel3d modello2 = new ComponentModel3d();
			graphicBus.addGraphicsAction(new G_CreateGameRenderableAction(1, "suzanne.obj", modello2));

			giocatore2.addComponent(modello2);
			
			giocatore2.addToWorld();
		}

	}

}
