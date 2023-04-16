package su.grinev;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import su.grinev.engine.Camera;
import su.grinev.engine.Loader;
import su.grinev.engine.Renderer;
import su.grinev.engine.entities.Entity;
import su.grinev.engine.models.RawModel;
import su.grinev.engine.models.TexturedModel;
import su.grinev.engine.shaders.StaticShader;
import su.grinev.engine.textures.ModelTexture;
import su.grinev.engine.voxels.Chunk;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static su.grinev.engine.voxels.CubeModel.texCoords;

public class Game implements Runnable {
    private final static int WIDTH = 1280;
    private final static int HEIGHT = 720;
    private DisplayManager displayManager;
    private Renderer renderer;
    private Loader loader;
    private StaticShader staticShader;
    private Camera camera;

    public Game() {
        displayManager = new DisplayManager();
        displayManager.createDisplay(WIDTH, HEIGHT);
        renderer = new Renderer(WIDTH, HEIGHT);
        loader = new Loader();
        staticShader = new StaticShader();
        camera = new Camera(0, 1, -5, 0, 0, 1);
    }

    @Override
    public void run() {
        GL.createCapabilities();
        Chunk chunk = new Chunk(0, 0);

        for (int i = 0; i!= 3; i++) {
            for (int x = 0; x != 16; x++) {
                for (int z = 0; z != 16; z++) {
                    chunk.setBlockId(x, i, z, (byte) 1);
                }
            }
        }

            chunk.generateMesh(true);


        RawModel rawModel = loader.loadToVao(chunk.getVertices(), chunk.getIndices(), texCoords);
        ModelTexture modelTexture = loader.loadTexture("C:\\Users\\Roman\\Desktop\\GameEngine\\src\\main\\resources\\image.png");
        TexturedModel staticModel = new TexturedModel(rawModel, modelTexture);
        List<Entity> entityList = new ArrayList<>();
        for (int i = 0; i != 1; i++) {
            Entity entity = new Entity(staticModel, new Vector3f(0f, 0, (float)-i * 0.001f), 0, 0, 0, 1);
            entityList.add(entity);
        }
        while (!glfwWindowShouldClose(displayManager.getWindow())) {
            //camera.look(0.0f, 0.01f);
            //camera.moveBackward(0.001f);
            entityList.get(0).increaseRotation(0.1f,0f, 0);

            renderer.prepare();
            staticShader.start();

            entityList.forEach(entity -> renderer.render(camera, entity, staticShader));

            staticShader.stop();
            displayManager.updateDisplay();
        }

        staticShader.cleanUp();
        loader.cleanUp();
        displayManager.closeDisplay();
    }
}
