package su.grinev;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class CullingDemo {
    GLFWErrorCallback errorCallback;
    GLFWKeyCallback   keyCallback;
    GLFWFramebufferSizeCallback fbCallback;

    long window;
    int width = 600;
    int height = 600;

    Matrix4f projMatrix = new Matrix4f();
    Matrix4f viewMatrix = new Matrix4f();
    FloatBuffer fb = BufferUtils.createFloatBuffer(16);
    boolean cullTheFront;
    boolean frontIsCw;

    void run() {
        try {
            init();
            loop();
            glfwDestroyWindow(window);
            keyCallback.free();
        } finally {
            glfwTerminate();
            errorCallback.free();
        }
    }
    void init() {
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure our window
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(width, height, "Hello Culling!", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        System.out.println("Press 'F' to toggle between frontface and backface culling (defaults to backface)");
        System.out.println("Press 'C' to toggle between clockwise or counter-clockwise frontface winding (defaults to counter-clockwise)");
        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    glfwSetWindowShouldClose(window, true);
                else if (key == GLFW_KEY_F && action == GLFW_RELEASE) {
                    cullTheFront = !cullTheFront;
                    if (cullTheFront)
                        System.out.println("Frontfaces are now culled");
                    else
                        System.out.println("Backfaces are now culled");
                } else if (key == GLFW_KEY_C && action == GLFW_RELEASE) {
                    frontIsCw = !frontIsCw;
                    if (frontIsCw)
                        System.out.println("Frontfaces use clockwise winding");
                    else
                        System.out.println("Frontfaces use counter-clockwise winding");
                }
            }
        });
        glfwSetFramebufferSizeCallback(window, fbCallback = new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                if (w > 0 && h > 0) {
                    width = w;
                    height = h;
                }
            }
        });
        glfwMakeContextCurrent(window);
        glfwShowWindow(window);
    }
    void renderCube() {
        // Render a cube with counter-clockwise front faces
        glBegin(GL_QUADS);
        glColor3f(  0.0f,  0.0f,  0.2f);
        glVertex3f( 1, -1, -1);
        glVertex3f(-1, -1, -1);
        glVertex3f(-1,  1, -1);
        glVertex3f( 1,  1, -1);
        glColor3f(  0.0f,  0.0f,  1.0f);
        glVertex3f( 1, -1,  1);
        glVertex3f( 1,  1,  1);
        glVertex3f(-1,  1,  1);
        glVertex3f(-1, -1,  1);
        glColor3f(  1.0f,  0.0f,  0.0f);
        glVertex3f( 1, -1, -1);
        glVertex3f( 1,  1, -1);
        glVertex3f( 1,  1,  1);
        glVertex3f( 1, -1,  1);
        glColor3f(  0.2f,  0.0f,  0.0f);
        glVertex3f(-1, -1,  1);
        glVertex3f(-1,  1,  1);
        glVertex3f(-1,  1, -1);
        glVertex3f(-1, -1, -1);
        glColor3f(  0.0f,  1.0f,  0.0f);
        glVertex3f( 1,  1,  1);
        glVertex3f( 1,  1, -1);
        glVertex3f(-1,  1, -1);
        glVertex3f(-1,  1,  1);
        glColor3f(  0.0f,  0.2f,  0.0f);
        glVertex3f( 1, -1, -1);
        glVertex3f( 1, -1,  1);
        glVertex3f(-1, -1,  1);
        glVertex3f(-1, -1, -1);
        glEnd();
    }
    void loop() {
        createCapabilities();

        glClearColor(0.6f, 0.7f, 0.8f, 1.0f);
        // Enable depth testing and culling
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);

        long firstTime = System.nanoTime();
        while (!glfwWindowShouldClose(window)) {
            long thisTime = System.nanoTime();
            float diff = (thisTime - firstTime) / 1E9f;
            float angle = diff;

            projMatrix.setPerspective((float) Math.toRadians(60f), (float) width / height, 0.01f, 100.0f).get(fb);
            glMatrixMode(GL_PROJECTION);
            glLoadMatrixf(fb);
            viewMatrix.setLookAt(0.0f, 2.0f, 5.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f).rotateY(angle * (float) Math.toRadians(45)).get(fb);
            glMatrixMode(GL_MODELVIEW);
            glLoadMatrixf(fb);

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glCullFace(GL_FRONT);
            glFrontFace(GL_CW);
            glViewport(0, 0, width, height);
            renderCube();

            glfwSwapBuffers(window);
            glfwPollEvents();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void main(String[] args) {
        new CullingDemo().run();
    }
}