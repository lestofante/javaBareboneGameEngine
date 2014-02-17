package test3d.lesto.graphics.object;

import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.lwjgl.opengl.ARBBufferObject;

import test3d.lesto.component.Model3d;
import test3d.lesto.graphics.loader.Mesh;
import test3d.lesto.graphics.loader.SimpleObjLoader;
import test3d.lesto.graphics.loader.Triangle;
import test3d.lesto.graphics.utils.GPUHandler;

public class ObjectHandler {

	private final Logger log = Logger.getLogger(this.getClass().getName());
	private final HashMap<String, Mesh> models = new HashMap<>();;
	private final Path modelLocation;

	public ObjectHandler(Path modelLocation) {
		this.modelLocation = modelLocation;
		init();
	}

	private Mesh getMesh(String modelName) throws Exception {
		Mesh out = models.get(modelName);

		if (out == null) {
			throw new Exception("Graphical model does not exist: " + modelName);
		}

		return out;
	}

	private void init() {
		log.fine("Loading graphical models");
		ArrayList<Path> modelPaths = searchForModels();

		Mesh temp;
		for (Path pathToModel : modelPaths) {
			temp = loadMesh(pathToModel);
			models.put(pathToModel.getFileName().toString(), temp);
		}
		log.info("Loaded graphical models");
	}

	private Mesh loadMesh(Path pathToModel) {
		log.fine("Loading: " + pathToModel.toString());
		ArrayList<Triangle> triangles = SimpleObjLoader.loadGeometry(pathToModel);

		log.fine("Loaded: "+pathToModel.getFileName().toString()+" with {} triangles "+ triangles.size());

		Mesh out = new Mesh(triangles, pathToModel.getFileName().toString());
		return out;
	}

	public RAMRenderable requestRAMMesh(String modelName, Model3d model) throws Exception {
		Mesh temp = getMesh(modelName);
		RAMRenderable out = new RAMRenderable(temp.verticesBuffer, temp.normalsBuffer, temp.interleavedBuffer, model);
		return out;
	}

	public VBORenderable requestVBOMesh(String modelName, Model3d model) throws Exception {
		Mesh temp = getMesh(modelName);
		if (!temp.VBOInitialized) {
			temp.vertexVBOID = GPUHandler.createVBOID();
			GPUHandler.bufferData(temp.vertexVBOID, temp.verticesBuffer, ARBBufferObject.GL_STATIC_DRAW_ARB);

			temp.normalVBOID = GPUHandler.createVBOID();
			GPUHandler.bufferData(temp.normalVBOID, temp.normalsBuffer, ARBBufferObject.GL_STATIC_DRAW_ARB);

			VBORenderable out = new VBORenderable(temp.vertexVBOID, temp.normalVBOID, temp.triangles.size(), model);
			temp.VBOInitialized = true;
			return out;
		} else {
			VBORenderable out = new VBORenderable(temp.vertexVBOID, temp.normalVBOID, temp.triangles.size(), model);
			return out;
		}
	}

	private ArrayList<Path> searchForModels() {
		ArrayList<Path> out = new ArrayList<>();

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(modelLocation)) {
			for (Path file : stream) {
				String temp = file.getFileName().toString();
				if (temp.endsWith(".obj")) {
					out.add(file);
				}
			}
		} catch (IOException | DirectoryIteratorException x) {
			// IOException can never be thrown
			// by the iteration. In this snippet,
			// it can only be thrown by
			// newDirectoryStream.
			System.err.println(x);
		}

		return out;
	}

}