package su.grinev.engine;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import su.grinev.DisplayManager;
import su.grinev.engine.entities.Entity;
import su.grinev.engine.models.RawModel;
import su.grinev.engine.models.TexturedModel;
import su.grinev.engine.shaders.StaticShader;
import su.grinev.engine.toolbox.Maths;

import java.util.Arrays;

import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glFrontFace;
import static org.lwjgl.opengl.GL20.glGetUniformfv;

public class Renderer {
    private final int width;
    private final int height;
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;
    private Matrix4f projectionMatrix;

    public Renderer(int width, int height) {
        this.width = width;
        this.height = height;
        projectionMatrix = createProjectionMatrix(width, height, FOV, NEAR_PLANE, FAR_PLANE);
    }

    public void prepare() {
        GL11.glClearColor(0.33f, 0.66f, 0.99f, 1f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LESS);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        // GL11.glCullFace(GL11.GL_FRONT);
    }

    public void render(Camera camera, Entity entity, StaticShader shader) {
        TexturedModel model = entity.getTexturedModel();

        GL30.glBindVertexArray(model.rawModel().vaoId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        //GL15.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());

        Matrix4f transformationMatrix = Maths.createTransformationMatrix(
                entity.getPosition(),
                entity.getRotationX(),
                entity.getRotationY(),
                entity.getRotationZ(),
                entity.getScale());

        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadViewMatrix(camera.getViewMatrix());
        shader.loadProjectionMatrix(projectionMatrix);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.modelTexture().textureId());
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.rawModel().vertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    private Matrix4f createProjectionMatrix(int width, int height, float fov, float nearPlane, float farPlane) {
        float aspect = (float) width / (float) height;
        float yScale = (float) ((1 / Math.tan(Math.toRadians(fov / 2))) * aspect);
        float xScale = yScale / aspect;
        float frustumLength = farPlane - nearPlane;
        Matrix4f projectionMatrix = new Matrix4f();
        projectionMatrix.m00(xScale);
        projectionMatrix.m11(yScale);
        projectionMatrix.m22(-(farPlane + nearPlane) / frustumLength);
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * nearPlane * farPlane) / frustumLength));
        projectionMatrix.m33(0);
        return projectionMatrix;
    }
}
