package su.grinev.engine.font;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class FontRenderer {
    private int[] indices = {
            0, 1, 3,
            1, 2, 3
    };
    private static final int BATCH_SIZE = 100;
    private static final int VERTEX_SIZE = 7;
    private final Matrix4f projection;
    private final int width;
    private final int height;
    private final int[] indicesBuffer;

    public FontRenderer(int width, int height) {
        this.width = width;
        this.height = height;
        this.indicesBuffer = new int[BATCH_SIZE * 3];
        projection = new Matrix4f();
        projection.identity();
        projection.ortho(0, width, 0, height, 1f, 100f);
    }

    private void fillEbo() {

    }
}