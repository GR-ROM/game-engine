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

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

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
        camera = new Camera(0, 0, 0, 0, 0, -1);
    }

    @Override
    public void run() {
        GL.createCapabilities();

        float[] model = {
               -0.5f, 0.5f, 0f,
               -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f
        };

        int[] indices = {
          0, 1, 3,
          3, 1, 2
        };

        float[] textureCoordinates = {
                0f, 0f,
                0f, 1f,
                1f, 1f,
                1f, 0f
        };

        RawModel rawModel = loader.loadToVao(model, indices, textureCoordinates);
        ModelTexture modelTexture = loader.loadTexture("C:\\Users\\Roman\\Desktop\\GameEngine\\src\\main\\resources\\image.png");
        TexturedModel staticModel = new TexturedModel(rawModel, modelTexture);
        List<Entity> entityList = new ArrayList<>();
        for (int i = 0; i != 1000; i++) {
            Entity entity = new Entity(staticModel, new Vector3f(0f, 0, (float)-i * 0.001f), 0, 0, 0, 1);
            entityList.add(entity);
        }
        while (!glfwWindowShouldClose(displayManager.getWindow())) {
            camera.look(0.0f, 0.01f);
            camera.moveBackward(0.1f);

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
