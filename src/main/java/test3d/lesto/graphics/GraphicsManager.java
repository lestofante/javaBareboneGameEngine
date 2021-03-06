package test3d.lesto.graphics;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import test3d.lesto.component.ComponentModel3d;
import test3d.lesto.graphics.action.G_CreateGameRenderableAction;
import test3d.lesto.graphics.action.G_FollowObjectWithCamera;
import test3d.lesto.graphics.action.G_RemoveGameRenderable;
import test3d.lesto.graphics.action.GraphicAction;
import test3d.lesto.graphics.object.GameRenderable;
import test3d.lesto.graphics.object.ObjectHandler;
import test3d.lesto.graphics.utils.Camera;

public class GraphicsManager implements Runnable {
	
	ComponentModel3d modello = new ComponentModel3d();

	protected final Logger log = Logger.getLogger( this.getClass().getName() );

	private static void loadNatives() {
		Logger staticLog = Logger.getLogger("Natives Loader");
		staticLog.fine("Loading natives");
		try {
			String osName = System.getProperty("os.name");

			staticLog.fine("Operating system name => "+ osName);

			File path = new File("native" + File.separator + osName.toLowerCase());

			System.setProperty("org.lwjgl.librarypath", path.getAbsolutePath());

		} catch (UnsatisfiedLinkError e) {
			staticLog.log(Level.SEVERE, "Native code library failed to load", e);
		}
		staticLog.info("Loaded natives");
	}

	private int fps;
	private final boolean fullScreen;
	private long lastFPS;
	private long lastFrame;
	private final DisplayMode mode;
	private FloatBuffer pos;
	private final HashMap<Integer, GameRenderable> toDraw = new HashMap<Integer, GameRenderable>();
	private ArrayList<GraphicAction> toProcess = new ArrayList<GraphicAction>();
	private final boolean vSync;

	private Camera camera;
	private ObjectHandler oHandler;

	private final AsyncActionBus asyncActionBus;
	
	private final GraphicLock lock = new GraphicLock();

	private float increment = 0.05f;

	/**
	 * Manages graphics.
	 * 
	 * @param mode
	 *            DisplayMode to set
	 * @param fullscreen
	 *            true to enable, false not to
	 * @param vSync
	 *            true to enable, false not to
	 */

	public GraphicsManager(DisplayMode mode, boolean fullScreen, boolean vSync, AsyncActionBus asyncActionBus) {
		loadNatives();
		this.asyncActionBus = asyncActionBus;
		this.mode = mode;
		this.fullScreen = fullScreen;
		this.vSync = vSync;
	}

	public int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		return delta;
	}

	/**
	 * Get the time in milliseconds
	 * 
	 * @return The system time in milliseconds
	 */
	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	/**
	 * Initialize the screen, camera and light.
	 * 
	 * @param mode
	 *            DisplayMode to set
	 * @param fullscreen
	 *            true to enable, false not to
	 * @param vSync
	 *            true to enable, false not to
	 */

	// TODO separate camera in at least a different function, preferably in a
	// different class.

	private void init(DisplayMode mode, boolean fullScreen, boolean vSync) {

		// load objects
		oHandler = new ObjectHandler(Paths.get("Resources/Objects"));

		try {
			Display.setDisplayMode(mode);
			Display.setFullscreen(fullScreen);
			//Display.setVSyncEnabled(vSync);
			Display.create();

		} catch (Exception e) {
			log.severe("Error setting up display");
			System.exit(-1);
		}

		asyncActionBus.graphicsStarted.set(true);

		this.camera = new Camera();

		int width = Display.getDisplayMode().getWidth();
		int height = Display.getDisplayMode().getHeight();

		camera.perspective(25.0f, (float) width / (float) height, 1, 1000);

		lastFPS = getTime(); // call before loop to initialise fps timer

		GL11.glEnable(GL11.GL_DEPTH_TEST);

		float mat_specular[] = { 1.0f, 1.0f, 1.0f, 0.1f };
		float light_position[] = { 0.0f, 1.0f, 1.0f, 0.0f };
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glShadeModel(GL11.GL_SMOOTH);

		FloatBuffer spec = BufferUtils.createFloatBuffer(4).put(mat_specular);
		spec.flip();
		pos = BufferUtils.createFloatBuffer(4).put(light_position);
		pos.rewind();

		camera.position = new Vector3f(0, 0, 130);

		camera.lookAt(0, 0, 0);

		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, pos);

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		
		//smooth test purpoise
		asyncActionBus.addGraphicsAction(new G_CreateGameRenderableAction(-1, "suzanne.obj", modello));
	}

	protected void processActions() {
		toProcess = asyncActionBus.getGraphicActions();
		for (GraphicAction action : toProcess) {

			switch (action.actionType) {

			case CREATE:
				G_CreateGameRenderableAction toCreate =(G_CreateGameRenderableAction) action; 
				GameRenderable temp = toDraw.get( toCreate.iD );

				if (temp == null) {
					log.fine("Creating graphical object with ID: "+ toCreate.iD);
					GameRenderable tempRenderable = null;
					try {
						tempRenderable = oHandler.requestVBOMesh(toCreate.modelName, toCreate.model);
					} catch (Exception e) {
						log.log(Level.SEVERE, "Error loading VBOMesh", e);
						System.exit(-1);
					}
					toDraw.put(((G_CreateGameRenderableAction) action).iD, tempRenderable);
				} else {
					try {
						throw new Exception("Object ID already present: "+toCreate.iD);
					} catch (Exception e) {
						log.log(Level.SEVERE, "Object ID already present: "+ toCreate.iD, e);
						System.exit(-1);
					}
				}
				break;

			case REMOVE:
				temp = toDraw.remove(((G_RemoveGameRenderable) action).iD);

				if (temp != null) {
					log.fine("Removed graphical obejct with ID: "+ ((G_RemoveGameRenderable) action).iD);
				} else {
					try {
						throw new Exception();
					} catch (Exception e) {
						log.log(Level.SEVERE, "Object ID does not exist: "+ ((G_RemoveGameRenderable) action).iD, e);
						System.exit(-1);
					}
				}
				break;

			case FOLLOW_OBJECT:
				camera.sharedTransform = ((G_FollowObjectWithCamera) action).transform;
			}

		}
	}

	private void render() {
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, pos);

		//lock.rwl.readLock().lock(); //read lock
		try{
			camera.update();
		
			for (GameRenderable renderable : toDraw.values()) {
				renderable.render();
			}
		}catch(Throwable e){
			log.log(Level.SEVERE, "errore imprevisto rendering grafico", e);
		}finally{
			//lock.rwl.readLock().unlock(); //read UNlock
		}
		

		// GL11.glFlush();
		GL11.glFinish();
		updateFPS();
	}

	@Override
	public void run() {

		init(mode, fullScreen, vSync);

		boolean run = true;
		
		while (!Display.isCloseRequested() && run ) {

			update();
			
			Display.sync(120);
		}

		asyncActionBus.graphicsStarted.set(false);

		Display.destroy();

		// log.debug("Display closed");

		System.out.println("Display closed");
		System.out.flush();

	}

	public void update() {
		modello.pos[1] += increment;
		if (Math.abs(modello.pos[1])> 20 ){
			increment*=-1;
		}
		processActions();
		render();
		Display.update();
	}

	/**
	 * Calculate the FPS and set it in the title bar
	 */
	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps);
			System.out.println("fps: " + fps);
			fps = 0; // reset the FPS counter
			lastFPS += 1000; // add one second
		}
		fps++;
	}

	public GraphicLock getLock() {
		return lock;
	}

}
