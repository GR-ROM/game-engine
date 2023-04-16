package su.grinev.engine.voxels;

public class Chunk {
    public static final int TRANSPARENT_VOXEL_ID = 0;
    public static final int CHUNK_SIZE = 16;
    private static final int CHUNK_SIZE_SQUARED = CHUNK_SIZE * CHUNK_SIZE;
    public static final int CHUNK_SIZE_HEIGHT = 16;
    private static final int CHUNK_MAX_SIZE = CHUNK_SIZE_SQUARED * CHUNK_SIZE_HEIGHT;
    private float[] vertices;
    private float[] textureCoords;
    private int[] indices;
    private byte[] map;
    private int originX;
    private int originZ;
    private int currentVertex;
    private int currentTexture;
    private int currentIndex;

    public Chunk(int originX, int originZ) {
        this.originX = originX;
        this.originZ = originZ;

        this.currentVertex = 0;
        this.currentIndex = 0;
        this.currentTexture = 0;

        this.vertices = new float[CHUNK_MAX_SIZE * 4 * 3 * 6];
        this.textureCoords = new float[CHUNK_MAX_SIZE * 4 * 2 * 6];
        this.indices = new int[CHUNK_MAX_SIZE * 6 * 6];

        this.map = new byte[CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE_HEIGHT];
    }

    public void setBlockId(int x, int y, int z, byte id) {
        if (x >= CHUNK_SIZE || x < 0 || y >= CHUNK_SIZE_HEIGHT || y < 0 || z >= CHUNK_SIZE || z < 0) return;
        map[x + (y * CHUNK_SIZE_SQUARED) + (z * CHUNK_SIZE_HEIGHT)] = id;
    }

    public byte getBlockId(int x, int y, int z) {
        if (x >= CHUNK_SIZE || x < 0 || y >= CHUNK_SIZE_HEIGHT || y < 0 || z >= CHUNK_SIZE || z < 0) {
            return TRANSPARENT_VOXEL_ID;
        }
        return map[x + (y * CHUNK_SIZE_SQUARED) + (z * CHUNK_SIZE_HEIGHT)];
    }

    public void generateMesh(boolean optimize) {
        long time = System.nanoTime();
        currentVertex = 0;
        currentIndex = 0;
        for (int x = 0; x != CHUNK_SIZE; x++) {
                for (int y = 0; y != CHUNK_SIZE_HEIGHT; y++) {
                    for (int z = 0; z != CHUNK_SIZE; z++) {
                    if (getBlockId(x, y, z) == TRANSPARENT_VOXEL_ID) { continue; }

                    if (getBlockId(x + 1, y, z) == TRANSPARENT_VOXEL_ID || !optimize) addBlockFace(x, y, z, CubeModel.FRONT_FACE);
                    if (getBlockId(x - 1, y, z) == TRANSPARENT_VOXEL_ID || !optimize) addBlockFace(x, y, z, CubeModel.BACK_FACE);
                    if (getBlockId(x, y + 1, z) == TRANSPARENT_VOXEL_ID || !optimize) addBlockFace(x, y, z, CubeModel.TOP_FACE);
                    if (getBlockId(x, y - 1, z) == TRANSPARENT_VOXEL_ID || !optimize) addBlockFace(x, y, z, CubeModel.BOTTOM_FACE);
                    if (getBlockId(x, y, z + 1) == TRANSPARENT_VOXEL_ID || !optimize) addBlockFace(x, y, z, CubeModel.LEFT_FACE);
                    if (getBlockId(x, y, z - 1) == TRANSPARENT_VOXEL_ID || !optimize) addBlockFace(x, y, z, CubeModel.RIGHT_FACE);;
                }
            }
        }
        time = (System.nanoTime() - time) / 1000;
        System.out.println("Meshed in: " + time + "us");
        System.out.println("Vertices: " + this.getVerticesSize());
        System.out.println("Indices: " + this.getIndices());
    }


    private void addBlockFace(int x, int y, int z, float[] face) {
        int index = currentVertex / 3;

        vertices[currentVertex++] = x + face[0];
        vertices[currentVertex++] = y + face[1];
        vertices[currentVertex++] = z + face[2];
        indices[currentIndex++] = index;

        vertices[currentVertex++] = x + face[3];
        vertices[currentVertex++] = y + face[4];
        vertices[currentVertex++] = z + face[5];
        indices[currentIndex++] = index + 1;

        vertices[currentVertex++] = x + face[6];
        vertices[currentVertex++] = y + face[7];
        vertices[currentVertex++] = z + face[8];
        indices[currentIndex++] = index + 2;

        vertices[currentVertex++] = x + face[9];
        vertices[currentVertex++] = y + face[10];
        vertices[currentVertex++] = z + face[11];
        indices[currentIndex++] = index + 2;

        indices[currentIndex++] = index + 3;
        indices[currentIndex++] = index;
    }

    public float[] getVertices() {
        return vertices;
    }

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
