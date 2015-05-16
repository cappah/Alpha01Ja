package renderEngine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*; 

import java.util.List;

import math.Maths;
import models.RawModel;

import org.lwjgl.util.vector.Matrix4f;

import com.game.core.Loader;

import shaderPrograms.GuiShader;
import textures.GUITexture;



public class GUIRenderer 
{
	private GuiShader shader;
	private final RawModel quad;
	private float[] positions =
		{
			-1, 1, -1, -1, 1, 1, 1, -1
		};
	
	
	public GUIRenderer(Loader loader)
	{
		quad = loader.loadToVAO(positions, 2);
		shader = new GuiShader();
	}
	
	
	public void render(List<GUITexture> guis)
	{
		shader.start();
		glBindVertexArray(quad.getVAOID());
		glEnableVertexAttribArray(0);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_DEPTH_TEST);
		for (GUITexture gui : guis)
		{
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, gui.getTexture());
			Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale());
			shader.loadTransformation(matrix);
			glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}	
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader.stop();
	}
	
	
	public void cleanup()
	{
		shader.cleanup();
	}
}
