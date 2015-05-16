package com.game.core;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import models.RawModel;

import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import textures.TextureData;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;



public class Loader 
{
	private static List<Integer> vaoList = new ArrayList<Integer>();
	private static List<Integer> vboList = new ArrayList<Integer>();
	private static List<Integer> textureList = new ArrayList<Integer>();
	
	
	public Loader()
	{
		
	}
	
	
	private TextureData decodeTextureFile(String fileName) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			FileInputStream in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ", didn't work");
			System.exit(-1);
		}
		return new TextureData(buffer, width, height);
	}
	
	
	public int loadCubeMap(String[] textureFiles)
	{
		int texID = glGenTextures();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_CUBE_MAP, texID);
		
		for (int i = 0; i < textureFiles.length; i++)
		{
			TextureData data = decodeTextureFile("res/" + textureFiles[i] + ".png");
			glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA, data.getWidth(), data.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, data.getBuffer());
		}
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		textureList.add(texID);
		
		return texID;
	}
	
	public RawModel loadToVAO(float[] positions, float[] texCoords, int[] indices)
	{
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		vaoList.add(vaoID);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, texCoords);
		unbindVAO();
		
		return new RawModel(vaoID, indices.length);
	}
	
	
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices)
	{
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		vaoList.add(vaoID);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		
		return new RawModel(vaoID, indices.length);
	}
	
	
	public RawModel loadToVAO(float[] positions)
	{
		int vaoID = createVAO();
		storeDataInAttributeList(0, 2, positions);
		unbindVAO();
		
		return new RawModel(vaoID, positions.length / 2);
	}
	
	
	public RawModel loadToVAO(float[] positions, int dimensions)
	{
		int vaoID = createVAO();
		this.storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		
		return new RawModel(vaoID, positions.length / dimensions);
	}
	
	
	private int createVAO()
	{
		int vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		return vaoID;
	}
	
	
	public int loadTexture(String fileName)
	{
		Texture texture = null;
		try 
		{
			if (fileName != null && !fileName.isEmpty())
				texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + fileName + ".png"));
			else
				texture = TextureLoader.getTexture("PNG", new FileInputStream("res/default.png"));
			
			glGenerateMipmap(GL_TEXTURE_2D);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.4f);
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		int textureID = texture.getTextureID();
		textureList.add(textureID);
		
		return textureID;
	}
	
	private void storeDataInAttributeList(int attributeNumber, int coordSize, float[] data)
	{
		int vboID = glGenBuffers();
		vboList.add(vboID);
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(attributeNumber, coordSize, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	
	private void bindIndicesBuffer(int[] indices)
	{
		int vboID = glGenBuffers();
		vboList.add(vboID);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
	}
	
	
	private IntBuffer storeDataInIntBuffer(int[] data)
	{
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	
	
	private FloatBuffer storeDataInFloatBuffer(float[] data)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	
	
	private void unbindVAO()
	{
		glBindVertexArray(0);
	}
	
	
	public void cleanup()
	{
		for (int id : vaoList)
			glDeleteVertexArrays(id);
		
		for (int id : vboList)
			glDeleteBuffers(id);
		
		for (int id : textureList)
			glDeleteTextures(id);
	}
}
