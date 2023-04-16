package su.grinev.engine.toolbox;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Maths {
    public static Matrix4f updateTransformationMatrix(Matrix4f matrix4f, Vector3f translation, float rx, float ry, float rz, float scale) {
        matrix4f.identity();
        matrix4f = matrix4f.translate(translation);
        matrix4f.rotateX((float) Math.toRadians(rx));
        matrix4f.rotateY((float) Math.toRadians(ry));
        matrix4f.rotateZ((float) Math.toRadians(rz));
        matrix4f.scale(scale);
        return matrix4f;
    }
}
