package su.grinev.engine.voxels;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Chunk {
    public static final int TRANSPARENT_VOXEL_ID = 0;
    public static final int CHUNK_SIZE = 16;
    private static final int CHUNK_SIZE_SQUARED = CHUNK_SIZE * CHUNK_SIZE;
    public static final int CHUNK_SIZE_HEIGHT = 256;
    private static final int CHUNK_MAX_SIZE = CHUNK_SIZE_SQUARED * CHUNK_SIZE_HEIGHT;
    private static final float[] vertices = new float[CHUNK_MAX_SIZE * 3 * 4 * 6];
    private static final float[] textureCoords = new float[CHUNK_MAX_SIZE * 2 * 4 * 6];
    private static final int[] indices = new int[CHUNK_MAX_SIZE * 6 * 6];
    private final byte[] map;
    private static final byte[] lightMap = new byte[CHUNK_MAX_SIZE];
    private final List<LightState> lights = new ArrayList<>();
    private final int originX;
    private final int originZ;
    private static int currentVertex;
    private static int currentTextureCoord;
    private static int currentIndex;

    public Chunk(int originX, int originZ) {
        this.originX = originX;
        this.originZ = originZ;

        this.currentVertex = 0;
        this.currentIndex = 0;
        this.currentTextureCoord = 0;

        this.map = new byte[CHUNK_MAX_SIZE];
    }

    public static void setBlockId(Chunk chunk, int x, int y, int z, byte id) {
        if (x >= CHUNK_SIZE || x < 0 || y >= CHUNK_SIZE_HEIGHT || y < 0 || z >= CHUNK_SIZE || z < 0) {
            return;
        }
        chunk.map[y + (x * CHUNK_SIZE_SQUARED) + (z * CHUNK_SIZE_HEIGHT)] = id;
    }

    public static byte getBlockId(Chunk chunk, int x, int y, int z) {
        if (x >= CHUNK_SIZE || x < 0 || y >= CHUNK_SIZE_HEIGHT || y < 0 || z >= CHUNK_SIZE || z < 0) {
            return TRANSPARENT_VOXEL_ID;
        }
        return chunk.map[y + (x * CHUNK_SIZE_SQUARED) + (z * CHUNK_SIZE_HEIGHT)];
    }

    public static void generateMesh(Chunk chunk, boolean optimize) {
        long time = System.nanoTime();
        currentVertex = 0;
        currentIndex = 0;
        currentTextureCoord = 0;

        for (int x = 0; x != CHUNK_SIZE; x++) {
            for (int z = 0; z != CHUNK_SIZE; z++) {
                for (int y = 0; y != CHUNK_SIZE_HEIGHT; y++) {
                    if (getBlockId(chunk, x, y, z) == TRANSPARENT_VOXEL_ID) { continue; }

                    if (getBlockId(chunk,x + 1, y, z) == TRANSPARENT_VOXEL_ID || !optimize) addBlockFace(x, y, z, CubeModel.FRONT_FACE, CubeModel.SIDE_RIGHT_FACE_TEXTURE_CORDS);
                    if (getBlockId(chunk,x - 1, y, z) == TRANSPARENT_VOXEL_ID || !optimize) addBlockFace(x, y, z, CubeModel.BACK_FACE, CubeModel.SIDE_LEFT_FACE_TEXTURE_CORDS);
                    if (getBlockId(chunk, x, y + 1, z) == TRANSPARENT_VOXEL_ID || !optimize) addBlockFace(x, y, z, CubeModel.TOP_FACE, CubeModel.FRONT_FACE_TEXTURE_CORDS);
                    if (getBlockId(chunk, x, y - 1, z) == TRANSPARENT_VOXEL_ID || !optimize) addBlockFace(x, y, z, CubeModel.BOTTOM_FACE, CubeModel.FRONT_FACE_TEXTURE_CORDS);
                    if (getBlockId(chunk, x, y, z + 1) == TRANSPARENT_VOXEL_ID || !optimize) addBlockFace(x, y, z, CubeModel.LEFT_FACE, CubeModel.SIDE_LEFT_FACE_TEXTURE_CORDS);
                    if (getBlockId(chunk, x, y, z - 1) == TRANSPARENT_VOXEL_ID || !optimize) addBlockFace(x, y, z, CubeModel.RIGHT_FACE, CubeModel.SIDE_RIGHT_FACE_TEXTURE_CORDS);
                }
            }
        }
        time = (System.nanoTime() - time) / 1000;
        System.out.println("Meshed in: " + time + "us");
        System.out.println("Vertices: " + chunk.getVerticesSize() / 3 + " size: " + (float)chunk.getVerticesSize() * 4 / 1024f + "KB");
        System.out.println("Indices: " + chunk.getIndicesSize() + " size: " + (float)chunk.getIndicesSize() * 4 / 1024f + "KB");
    }

    private static void computeLightMap(Chunk chunk) {
        List<LightState> queue = new LinkedList<>();
        for (LightState lightState: chunk.lights) {
            queue.add(lightState);

            while (!queue.isEmpty()) {
                LightState currentLightState = queue.remove(0);
                // TODO: apply light level to map

                if (getBlockId(chunk, currentLightState.getX() + 1, currentLightState.getY(), currentLightState.getZ()) == TRANSPARENT_VOXEL_ID) {
                    queue.add(new LightState(
                            currentLightState.getLightLevel() - 1,
                            currentLightState.getX() + 1,
                            currentLightState.getY(),
                            currentLightState.getZ()));
                }
                if (getBlockId(chunk, currentLightState.getX() - 1, currentLightState.getY(), currentLightState.getZ()) == TRANSPARENT_VOXEL_ID) {
                    queue.add(new LightState(
                            currentLightState.getLightLevel() - 1,
                            currentLightState.getX() - 1,
                            currentLightState.getY(),
                            currentLightState.getZ()));
                }
                if (getBlockId(chunk, currentLightState.getX(), currentLightState.getY() + 1, currentLightState.getZ()) == TRANSPARENT_VOXEL_ID) {
                    queue.add(new LightState(
                            currentLightState.getLightLevel() - 1,
                            currentLightState.getX(),
                            currentLightState.getY() + 1,
                            currentLightState.getZ()));
                }
                if (getBlockId(chunk, currentLightState.getX(), currentLightState.getY() - 1, currentLightState.getZ()) == TRANSPARENT_VOXEL_ID) {
                    queue.add(new LightState(
                            currentLightState.getLightLevel() - 1,
                            currentLightState.getX(),
                            currentLightState.getY() - 1,
                            currentLightState.getZ()));
                }
                if (getBlockId(chunk, currentLightState.getX() - 1, currentLightState.getY(), currentLightState.getZ() + 1) == TRANSPARENT_VOXEL_ID) {
                    queue.add(new LightState(
                            currentLightState.getLightLevel() - 1,
                            currentLightState.getX(),
                            currentLightState.getY(),
                            currentLightState.getZ() + 1));
                }
                if (getBlockId(chunk, currentLightState.getX(), currentLightState.getY(), currentLightState.getZ() - 1) == TRANSPARENT_VOXEL_ID) {
                    queue.add(new LightState(
                            currentLightState.getLightLevel() - 1,
                            currentLightState.getX(),
                            currentLightState.getY(),
                            currentLightState.getZ() - 1));
                }
            }
        }
    }

    private static void addBlockFace(int x, int y, int z, float[] face, float[] faceTextureCoords) {
        vertices[currentVertex++] = x + face[0];
        vertices[currentVertex++] = y + face[1];
        vertices[currentVertex++] = z + face[2];

        vertices[currentVertex++] = x + face[3];
        vertices[currentVertex++] = y + face[4];
        vertices[currentVertex++] = z + face[5];

        vertices[currentVertex++] = x + face[6];
        vertices[currentVertex++] = y + face[7];
        vertices[currentVertex++] = z + face[8];

        vertices[currentVertex++] = x + face[9];
        vertices[currentVertex++] = y + face[10];
        vertices[currentVertex++] = z + face[11];

        int index = currentVertex / 3;
        indices[currentIndex++] = index;
        indices[currentIndex++] = index + 1;
        indices[currentIndex++] = index + 2;
        indices[currentIndex++] = index + 2;
        indices[currentIndex++] = index + 3;
        indices[currentIndex++] = index;

        textureCoords[currentTextureCoord++] = faceTextureCoords[0];
        textureCoords[currentTextureCoord++] = faceTextureCoords[1];

        textureCoords[currentTextureCoord++] = faceTextureCoords[2];
        textureCoords[currentTextureCoord++] = faceTextureCoords[3];

        textureCoords[currentTextureCoord++] = faceTextureCoords[4];
        textureCoords[currentTextureCoord++] = faceTextureCoords[5];

        textureCoords[currentTextureCoord++] = faceTextureCoords[6];
        textureCoords[currentTextureCoord++] = faceTextureCoords[7];
    }

    public float[] getVertices() {
        return vertices;
    }

    public float[] getTextureCoords() { return textureCoords; }

    public int[] getIndices() {
        return indices;
    }

    public int getVerticesSize() {
        return currentVertex;
    }

    public int getIndicesSize() {
        return currentIndex;
    }
}
