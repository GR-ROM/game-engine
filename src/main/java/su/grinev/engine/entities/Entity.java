package su.grinev.engine.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;
import su.grinev.engine.models.TexturedModel;

@AllArgsConstructor
@Getter
@Setter
public class Entity {
    private TexturedModel texturedModel;
    private Vector3f position;
    private float rotationX;
    private float rotationY;
    private float rotationZ;
    private float scale;

    public void increasePosition(float dx, float dy, float dz) {
        position.x += dx;
        position.y += dy;
        position.z += dz;
    }

    public void increaseRotation(float dx, float dy, float dz) {
        rotationX += dx;
        rotationY += dy;
        rotationZ += dz;
    }

}
