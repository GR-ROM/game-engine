package su.grinev.engine.toolbox;

import org.joml.Matrix4f;
import org.joml.Vector3fc;

public class Frustum {
    private final Matrix4f projViewMatrix;
    private final Matrix4f viewMatrix;
    private final Matrix4f invViewMatrix;
    private final Matrix4f projectionMatrix;
    private final Plane[] planes;

    public Frustum(Matrix4f projectionMatrix, Matrix4f viewMatrix) {
        this.planes = new Plane[6];
        this.projViewMatrix = new Matrix4f();
        this.invViewMatrix = new Matrix4f();
        this.viewMatrix = viewMatrix;
        this.projectionMatrix = projectionMatrix;

        projViewMatrix.set(projectionMatrix);
        invViewMatrix.set(viewMatrix);

        projViewMatrix.invert();
        invViewMatrix.invert();
        projViewMatrix.mul(invViewMatrix);
        extractPlanes();
    }

    private void extractPlanes() {
        // Extract frustum planes from the combined projViewMatrix
        planes[0].set(projViewMatrix.m03() + projViewMatrix.m00(), projViewMatrix.m13() + projViewMatrix.m10(), projViewMatrix.m23() + projViewMatrix.m20(), projViewMatrix.m33() + projViewMatrix.m30()); // Left plane
        planes[1].set(projViewMatrix.m03() - projViewMatrix.m00(), projViewMatrix.m13() - projViewMatrix.m10(), projViewMatrix.m23() - projViewMatrix.m20(), projViewMatrix.m33() - projViewMatrix.m30()); // Right plane
        planes[2].set(projViewMatrix.m03() + projViewMatrix.m01(), projViewMatrix.m13() + projViewMatrix.m11(), projViewMatrix.m23() + projViewMatrix.m21(), projViewMatrix.m33() + projViewMatrix.m31()); // Bottom plane
        planes[3].set(projViewMatrix.m03() - projViewMatrix.m01(), projViewMatrix.m13() - projViewMatrix.m11(), projViewMatrix.m23() - projViewMatrix.m21(), projViewMatrix.m33() - projViewMatrix.m31()); // Top plane
        planes[4].set(projViewMatrix.m03() + projViewMatrix.m02(), projViewMatrix.m13() + projViewMatrix.m12(), projViewMatrix.m23() + projViewMatrix.m22(), projViewMatrix.m33() + projViewMatrix.m32()); // Near plane
        planes[5].set(projViewMatrix.m03() - projViewMatrix.m02(), projViewMatrix.m13() - projViewMatrix.m12(), projViewMatrix.m23() - projViewMatrix.m22(), projViewMatrix.m33() - projViewMatrix.m32()); // Far plane

        // Normalize the frustum planes
        for (int i = 0; i < 6; i++) {
            planes[i].normalize();
        }
    }

    public boolean containsChunk(Vector3fc chunkPosition) {
        for (int i = 0; i < 6; i++) {
            if (planes[i].distance(chunkPosition) < 0) {
                return false;
            }
        }
        return true;
    }
}

