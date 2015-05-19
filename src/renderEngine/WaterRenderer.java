package renderEngine;

import java.util.List;

import math.Maths;
import models.RawModel;
 



import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
 






import shaderPrograms.WaterShader;
import camera.Camera;

import com.game.core.Loader;

import water.WaterTile;

public class WaterRenderer {
 
    private RawModel quad;
    private WaterShader shader;
 
    public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
        setUpVAO(loader);
    }
 
    public void render(List<WaterTile> water, Camera camera) {
        prepareRender(camera);  
        for (WaterTile tile : water) {
            Matrix4f modelMatrix = Maths.createTransformationMatrix(
                    new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0,
                    WaterTile.TILE_SIZE);
            shader.loadModelMatrix(modelMatrix);
            glDrawArrays(GL_TRIANGLES, 0, quad.getVertexCount());
        }
        unbind();
    }
     
    private void prepareRender(Camera camera){
        shader.start();
        shader.loadViewMatrix(camera);
        glBindVertexArray(quad.getVAOID());
        glEnableVertexAttribArray(0);
    }
     
    private void unbind(){
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        shader.stop();
    }
 
    private void setUpVAO(Loader loader) {
        // Just x and z vectex positions here, y is set to 0 in v.shader
        float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
        quad = loader.loadToVAO(vertices, 2);
    }
}
 