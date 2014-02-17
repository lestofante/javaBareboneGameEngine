package test3d.lesto;

import org.lwjgl.opengl.DisplayMode;

import test3d.lesto.component.Model3d;
import test3d.lesto.graphics.AsyncActionBus;
import test3d.lesto.graphics.GraphicsManager;
import test3d.lesto.graphics.action.G_CreateGameRenderableAction;

import com.artemis.Entity;
import com.artemis.World;

/**
 * Hello world!
 *
 */
public class MyGame 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        new MyGame();
    }

	private World world;
	
	private final AsyncActionBus graphicBus = new AsyncActionBus();//THIS IS THE BUS TO USE THE GRAPHIC MANAGER
	private GraphicsManager graphicEngine = new GraphicsManager(new DisplayMode(800, 600), false, true, graphicBus);
    
    public MyGame() {
    	world = new World();
    		
    	//world.setSystem(new MovementSystem());
    	//world.setSystem(new RotationSystem());
    	world.setSystem( new RenderingSystem( graphicEngine.getLock() ) );
    		
    	world.initialize();
    	
    	
    	Thread graphicsThread = new Thread(graphicEngine);
    	graphicsThread.setName("Client Graphics");
    	graphicsThread.start();
    	
    	
    	addTestObjects();
    	
    	
    	modello.rot[1]= 1; //set rotation axis
    	
    	long lastProcess = System.currentTimeMillis(), tmpProcess;
    	while(true) {
    		tmpProcess = System.currentTimeMillis();
    		world.setDelta(tmpProcess-lastProcess);
    		lastProcess = tmpProcess;
    		world.process();
    		
    		modello.rot[0]++; //add rotation

    		try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    Model3d modello = new Model3d();
	private void addTestObjects() {
		Entity giocatore1 = world.createEntity();
		
		giocatore1.addComponent(modello);
		graphicBus.addGraphicsAction(new G_CreateGameRenderableAction(0, "suzanne.obj", modello));
	}
}
