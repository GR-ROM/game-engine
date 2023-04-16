package su.grinev.engine.toolbox;

import org.joml.Vector3f;
import org.joml.Vector3fc;

public class Plane {
    private final Vector3f normal;
    private float distance;

    public Plane(Vector3fc normal, float distance) {
        this.normal = new Vector3f(normal);
        this.distance = distance;
    }

    public Plane set(float a, float b, float c, float d) {
        this.normal.set(a, b, c);
        this.distance = d;
        return this;
    }

    public float distance(Vector3fc point) {
        return normal.dot(point) + distance;
    }

    public void normalize() {
        float l = normal.length();
        normal.normalize();
        distance /= l;
    }

    public enum Side {
        Front, Back, OnPlane
    }

    public Side testPoint(Vector3fc point, float epsilon) {
        float dist = distance(point);
        if (dist > epsilon) {
            return Side.Front;
        }
        if (dist < -epsilon) {
            return Side.Back;
        }
        return Side.OnPlane;
    }
}