package su.grinev.engine.voxels;

public class CubeModel {
    public static final float TEXTURE_STEP = 1 / 8f;
    public static float[] FRONT_FACE = {
            // Front face
            0.5f,-0.5f,0.5f,  // 0
            0.5f,-0.5f,-0.5f,   // 1
            0.5f,0.5f,-0.5f,    // 2
            0.5f,0.5f,0.5f   // 3
    };

    public static float[] BACK_FACE = {
            // Back face
            -0.5f,0.5f,-0.5f,
            -0.5f,-0.5f,-0.5f,
            -0.5f,-0.5f,0.5f,
            -0.5f,0.5f,0.5f
    };

    public static float[] LEFT_FACE = {
            // Left face
            -0.5f,0.5f,0.5f,
            -0.5f,-0.5f,0.5f,
            0.5f,-0.5f,0.5f,
            0.5f,0.5f,0.5f
    };

    public static float[] RIGHT_FACE = {
            // Right face
            0.5f,-0.5f,-0.5f,
            -0.5f,-0.5f,-0.5f,
            -0.5f,0.5f,-0.5f,
            0.5f,0.5f,-0.5f

    };

    public static float[] TOP_FACE = {
            // Top face
            0.5f,0.5f,-0.5f,
            -0.5f,0.5f,-0.5f,
            -0.5f,0.5f,0.5f,
            0.5f,0.5f,0.5f
    };

    public static float[] BOTTOM_FACE = {
            // Bottom face
            -0.5f,-0.5f,0.5f,
            -0.5f,-0.5f,-0.5f,
            0.5f,-0.5f,-0.5f,
            0.5f,-0.5f,0.5f
    };

    public static float[] FRONT_FACE_TEXTURE_CORDS = {
            0.0f * TEXTURE_STEP, (8 - 0.0f) * TEXTURE_STEP,
            1.0f * TEXTURE_STEP, (8 - 0.0f) * TEXTURE_STEP,
            1.0f * TEXTURE_STEP, (8 - 1.0f) * TEXTURE_STEP,
            0.0f * TEXTURE_STEP, (8 - 1.0f) * TEXTURE_STEP,
    };

    public static float[] SIDE_RIGHT_FACE_TEXTURE_CORDS = {
            (1 + 0.0f) * TEXTURE_STEP, (8 - 1.0f) * TEXTURE_STEP,
            (1 + 1.0f) * TEXTURE_STEP, (8 - 1.0f) * TEXTURE_STEP,
            (1 + 1.0f) * TEXTURE_STEP, (8 - 0.0f) * TEXTURE_STEP,
            (1 + 0.0f) * TEXTURE_STEP, (8 - 0.0f) * TEXTURE_STEP,
    };

    public static float[] SIDE_LEFT_FACE_TEXTURE_CORDS = {
            (1 + 1.0f) * TEXTURE_STEP, (8 - 0.0f) * TEXTURE_STEP,
            (1 + 1.0f) * TEXTURE_STEP, (8 - 1.0f) * TEXTURE_STEP,
            (1 + 0.0f) * TEXTURE_STEP, (8 - 1.0f) * TEXTURE_STEP,
            (1 + 0.0f) * TEXTURE_STEP, (8 - 0.0f) * TEXTURE_STEP
    };

    public static float[] FRONT_FACE_TEXTURE_COORDS = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
    };

    public static float[] BACK_FACE_TEXTURE_COORDS = {
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 0.0f
    };

    public static float[] LEFT_FACE_TEXTURE_COORDS = {
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 0.0f
    };

    public static float[] RIGHT_FACE_TEXTURE_COORDS = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
    };

    public static float[] TOP_FACE_TEXTURE_COORDS = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f
    };

    public static float[] BOTTOM_FACE_TEXTURE_COORDS = {
            1.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f
    };
    public static int[] indices = {
            0, 1, 2, 0, 2, 3,      // Front face
            4, 5, 6, 4, 6, 7,      // Back face
            8, 9, 10, 8, 10, 11,    // Top face
            12, 13, 14, 12, 14, 15, // Bottom face
            16, 17, 18, 16, 18, 19, // Right face
            20, 21, 22, 20, 22, 23  // Left face
    };

}
