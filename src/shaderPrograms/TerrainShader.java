package shaderPrograms;

import java.util.List;

import lights.DirectionalLight;
import math.Maths;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import camera.Camera;
import lights.Light;
import org.lwjgl.util.vector.Vector4f;


public class TerrainShader extends ShaderProgram
{
	private static final int MAX_LIGHTS = 4;
	private static final String VERTEX_FILE = "src/shaders/terrainV.glsl";
	private static final String FRAG_FILE = "src/shaders/terrainF.glsl";
	
	
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_transformationMatrix;
	
	private int[] location_lightPosition;
	private int[] location_lightColor;
	private int[] location_atten;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColor;
	
	private int location_backgroundTexture;
	private int location_rTexture;
	private int location_gTexture;
	private int location_bTexture;
	private int location_blendMap;

	private int location_plane;

	// Base Light
	private int location_baseLightColor;
	private int location_baseLightIntensity;
	// Directional Light
	private int location_directionalLightColor;
	private int location_directionalLightAmbientIntensity;
	private int location_directionalLightDirection;

	
	public TerrainShader()
	{
		super(VERTEX_FILE, FRAG_FILE);
	}
	
	
	public TerrainShader(String vertFile, String fragFile) 
	{
		super(vertFile, fragFile);
		
	}

	
	@Override
	protected void bindAttributes() 
	{
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texCoord");
		super.bindAttribute(2, "normal");
	}


	@Override
	protected void getAllUniformLocations() 
	{
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		
		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColor = new int[MAX_LIGHTS];
		location_atten = new int[MAX_LIGHTS];
		
		for (int i = 0; i < MAX_LIGHTS; i++)
		{
			location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			location_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
			location_atten[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		
		location_skyColor = super.getUniformLocation("skyColor");
		
		location_backgroundTexture = super.getUniformLocation("backgroundTexture");
		location_rTexture = super.getUniformLocation("rTexture");
		location_gTexture = super.getUniformLocation("gTexture");
		location_bTexture = super.getUniformLocation("bTexture");
		location_blendMap = super.getUniformLocation("blendMap");

		location_plane = super.getUniformLocation("plane");

		// Base Light
		location_baseLightColor = super.getUniformLocation("gBaseLight.color");
		location_baseLightIntensity = super.getUniformLocation("gBaseLight.intensity");
		// Directional Light
		location_directionalLightColor = super.getUniformLocation("gDirectionalLight.color");
		location_directionalLightAmbientIntensity = super.getUniformLocation("gDirectionalLight.intensity");
		location_directionalLightDirection = super.getUniformLocation("gDirectionalLight.direction");
	}

	
	public void loadProjectionMatrix(Matrix4f matrix)
	{
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	
	
	public void loadViewMatrix(Camera camera)
	{
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	
	public void loadTransformationMatrix(Matrix4f matrix)
	{
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	
	public void loadLights(List<Light> lights)
	{
		for (int i = 0; i < MAX_LIGHTS; i++)
		{
			if (i < lights.size())
			{
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColor[i], lights.get(i).getColor());
				super.loadVector(location_atten[i], lights.get(i).getAtten());
			}
			else
			{
				super.loadVector(location_lightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector(location_lightColor[i], new Vector3f(0, 0, 0));
				super.loadVector(location_atten[i], new Vector3f(1, 0, 0));
			}
		}	
	}
	
	
	public void loadShineVariables(float damper, float reflectivity)
	{
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}


	public void loadSkyColor(float r, float g, float b)
	{
		super.loadVector(location_skyColor, new Vector3f(r, g, b));
	}
	
	
	public void connectTextureUnits()
	{
		super.loadInt(location_backgroundTexture, 0);
		super.loadInt(location_rTexture, 1);
		super.loadInt(location_gTexture, 2);
		super.loadInt(location_bTexture, 3);
		super.loadInt(location_blendMap, 4);
	}


	public void loadClippingPlane(Vector4f plane)
	{
		super.load4DVector(location_plane, plane);
	}


	/**************************************************
	                    LIGHTING
	 **************************************************/


	public void loadBaseLightColor(Vector3f color)
	{
		super.loadVector(location_baseLightColor, color);
	}


	public void loadBaseLightAmbience(float ambientIntensity)
	{
		super.loadFloat(location_baseLightIntensity, ambientIntensity);
	}


	public void loadDirectionalLight(DirectionalLight light)
	{
		loadBaseLightAmbience(light.getIntensity());
		loadBaseLightColor(light.getColor());
		super.loadVector(location_directionalLightDirection, light.getDirection());
		super.loadVector(location_directionalLightColor, light.getColor());
		super.loadFloat(location_directionalLightAmbientIntensity, light.getIntensity());
	}





	public void loadDirectionalLightAmbience(float ambientIntensity) {

	}
}
