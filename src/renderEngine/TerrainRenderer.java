package renderEngine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.List;

import math.Maths;
import models.RawModel;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import shaderPrograms.TerrainShader;
import terrain.Terrain;
import textures.TerrainTexturePack;



public class TerrainRenderer 
{
	
	private TerrainShader mShader;
	
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix)
	{
		this.mShader = shader;
		mShader.start();
		mShader.loadProjectionMatrix(projectionMatrix);
		mShader.connectTextureUnits();
		mShader.stop();	
	}
	
	
	
	
	
	public void render(List<Terrain> terrains)
	{
		for (Terrain terrain : terrains)
		{
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			
			glDrawElements(GL_TRIANGLES, terrain.getModel().getVertexCount(), GL_UNSIGNED_INT, 0);
			unbindTexturedModel();
		}
	}
	
	
	private void prepareTerrain(Terrain terrain)
	{
		RawModel rawModel = terrain.getModel();
		glBindVertexArray(rawModel.getVAOID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		bindTextures(terrain);
		mShader.loadShineVariables(1, 0);
		
	}
	
	
	private void bindTextures(Terrain terrain)
	{
		TerrainTexturePack texturePack = terrain.getTexturePack();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texturePack.getBackgroundTexture().getID());
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, texturePack.getRTexture().getID());
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, texturePack.getGTexture().getID());
		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, texturePack.getBTexture().getID());
		glActiveTexture(GL_TEXTURE4);
		glBindTexture(GL_TEXTURE_2D, terrain.getBlendMap().getID());
	}
	
	private void loadModelMatrix(Terrain terrain)
	{
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(
				new Vector3f(terrain.getX(), 0, terrain.getZ()), 
				0, 0, 0, 1);
		
		mShader.loadTransformationMatrix(transformationMatrix);
	}
	
	private void unbindTexturedModel()
	{
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
	}
}
