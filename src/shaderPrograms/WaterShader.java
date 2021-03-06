package shaderPrograms;

import math.Maths;

import org.lwjgl.util.vector.Matrix4f;

import camera.Camera;
import shaderPrograms.ShaderProgram;

 
public class WaterShader extends ShaderProgram {
 
    private final static String VERTEX_FILE = "src/shaders/waterV.glsl";
    private final static String FRAGMENT_FILE = "src/shaders/waterF.glsl";
 
    private int location_modelMatrix;
    private int location_viewMatrix;
    private int location_projectionMatrix;
 
    public WaterShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
 
    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
    }
 
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = getUniformLocation("projectionMatrix");
        location_viewMatrix = getUniformLocation("viewMatrix");
        location_modelMatrix = getUniformLocation("modelMatrix");
    }
 
    public void loadProjectionMatrix(Matrix4f projection) {
        loadMatrix(location_projectionMatrix, projection);
    }
     
    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        loadMatrix(location_viewMatrix, viewMatrix);
    }
 
    public void loadModelMatrix(Matrix4f modelMatrix){
        loadMatrix(location_modelMatrix, modelMatrix);
    }
 
}
