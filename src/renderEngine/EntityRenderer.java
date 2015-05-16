package renderEngine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import shaderPrograms.StaticShader;
import textures.ModelTexture;
import math.Maths;
import models.RawModel;
import models.TexturedModel;

public class EntityRenderer 
{

	
	private StaticShader mShader;
	
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix)
	{
		mShader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	
	
	
	public void render(Map<TexturedModel, List<Entity>> entities)
	{
		for (TexturedModel model : entities.keySet())
		{
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			
			for (Entity entity : batch)
			{
				prepareInstance(entity);
				glDrawElements(GL_TRIANGLES, model.getModel().getVertexCount(), GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}
	
	
	private void prepareTexturedModel(TexturedModel model)
	{
		RawModel rawModel = model.getModel();
		glBindVertexArray(rawModel.getVAOID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		ModelTexture texture = model.getTexture();
		mShader.loadNumberOfRows(texture.getNumberOfRows());
		if (texture.hasTransparency())
		{
			MasterRenderer.disableCulling();
		}
		mShader.loadFakeLighting(texture.hasFakeLighting());
		mShader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, model.getTexture().getID());
	}
	
	
	private void unbindTexturedModel()
	{
		MasterRenderer.enableCulling();
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
	}
	
	
	private void prepareInstance(Entity entity)
	{
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), 
				entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		mShader.loadTransformationMatrix(transformationMatrix);
		mShader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
	}
	
	
	
}
