package shaderPrograms;

import java.util.List;

import math.Maths;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import camera.Camera;
import entities.Light;



public class StaticShader extends ShaderProgram
{
	private static final int MAX_LIGHTS = 4;
	private static final String VERTEX_FILE = "src/shaders/basicV.glsl";
	private static final String FRAG_FILE = "src/shaders/basicF.glsl";
	
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_transformationMatrix;
	
	private int[] location_lightPosition;
	private int[] location_lightColor;
	private int[] location_atten;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_useFakeLighting;
	private int location_skyColor;
	private int location_numberOfRows;
	private int location_offset;
	
	public StaticShader()
	{
		super(VERTEX_FILE, FRAG_FILE);
	}
	
	
	public StaticShader(String vertFile, String fragFile) 
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
		location_useFakeLighting = super.getUniformLocation("useFakeLighting");
		
		location_skyColor = super.getUniformLocation("skyColor");
		
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");
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
	
	
	public void loadFakeLighting(boolean useFake)
	{
		super.loadBoolean(location_useFakeLighting, useFake);
	}
	
	
	public void loadSkyColor(float r, float g, float b)
	{
		super.loadVector(location_skyColor, new Vector3f(r, g, b));
	}
	
	
	public void loadNumberOfRows(int numberOfRows)
	{
		super.loadFloat(location_numberOfRows, numberOfRows);
	}
	
	
	public void loadOffset(float x, float y)
	{
		super.load2DVector(location_offset, new Vector2f(x, y));
	}
}
