package su.grinev.engine.shaders;

import org.joml.Matrix4f;

public class StaticShader extends ShaderProgram {
        private static final String VERTEX_FILE = "VertexShader.glsl";
        private static final String FRAGMENT_FILE = "FragmentShader.glsl";
        private int locationTransformationMatrix;
        private int locationProjectionMatrix;
        private int locationViewMatrix;

        public StaticShader() {
            super(VERTEX_FILE, FRAGMENT_FILE);
        }

        @Override
        protected void bindAttributes() {
            super.bindAttribute(0, "position");
            super.bindAttribute(1, "textureCoords");
        }

    @Override
    protected void getAllUniformLocations() {
        locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        locationViewMatrix = super.getUniformLocation("viewMatrix");
    }

    public void loadTransformationMatrix(Matrix4f matrix4f) {
        super.loadMatrix(locationTransformationMatrix, matrix4f);
    }

    public void loadProjectionMatrix(Matrix4f matrix4f) {
        super.loadMatrix(locationProjectionMatrix, matrix4f);
    }

    public void loadViewMatrix(Matrix4f matrix4f) {
        super.loadMatrix(locationViewMatrix, matrix4f);
    }

    public int getLocationTransformationMatrix() {
        return locationTransformationMatrix;
    }

    public int getLocationViewMatrix() {
        return locationViewMatrix;
    }
}
