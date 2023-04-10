package su.grinev.engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class Camera {
        private Vector3f position;
        private Vector3f direction;
        private Matrix4f viewMatrix;
        private Vector3f target;
        private Vector3f left;
        private Vector3fc up;

        public Camera(float x, float y, float z, float dx, float dy, float dz) {
            position = new Vector3f(x, y, z);
            direction = new Vector3f(dx, dy, dz).normalize();
            viewMatrix = new Matrix4f();
            up = new Vector3f(0, 1, 0);
            target = new Vector3f();
            left = new Vector3f();
            updateViewMatrix();
        }

        public void moveForward(float distance) {
            direction.get(target);
            position.add(target.mul(distance));
            updateViewMatrix();
        }

        public void moveBackward(float distance) {
            direction.get(target);
            position.sub(target.mul(distance));
            updateViewMatrix();
        }

        public void strafeLeft(float distance) {
            direction.get(left);
            left.cross(up).normalize();
            position.sub(left.mul(distance));
            updateViewMatrix();
        }

        public void strafeRight(float distance) {
            direction.get(left);
            left.cross(up).normalize();
            updateViewMatrix();
        }

        public void look(float dx, float dy) {
            direction.rotateY(-dy);
            direction.rotateX(-dx);
            updateViewMatrix();
        }

        public Matrix4f getViewMatrix() {
            return viewMatrix;
        }

        private void updateViewMatrix() {
            position.get(target);
            target.add(direction);
            viewMatrix.identity();
            viewMatrix.lookAt(position, target, up);
        }
}
