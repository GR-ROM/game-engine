package su.grinev.engine.voxels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LightState {
    private final int x;
    private final int y;
    private final int z;
    private int lightLevel;

    public LightState(int lightLevel, int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.lightLevel = lightLevel;
    }
}
